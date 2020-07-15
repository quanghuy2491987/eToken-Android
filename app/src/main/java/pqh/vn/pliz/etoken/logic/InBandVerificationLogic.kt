package pqh.vn.pliz.etoken.logic

import android.util.Base64
import androidx.annotation.NonNull
import com.gemalto.idp.mobile.authentication.AuthInput
import com.gemalto.idp.mobile.core.IdpException
import com.gemalto.idp.mobile.otp.oath.DualSeedOathToken
import com.gemalto.idp.mobile.otp.oath.soft.SoftOathToken
import com.thalesgroup.mobileprotector.commonutils.callbacks.GenericHandler
import com.thalesgroup.mobileprotector.commonutils.callbacks.GenericOtpHandler
import com.thalesgroup.mobileprotector.commonutils.helpers.AbstractBaseLogic
import com.thalesgroup.mobileprotector.commonutils.helpers.OtpValue
import com.thalesgroup.mobileprotector.commonutils.thread.ExecutionService
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*


/**
 * Logic for user authentication/OTP verification.
 */
object InBandVerificationLogic : AbstractBaseLogic() {
    private const val XML_TEMPLATE_AUTH = ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "    <AuthenticationRequest>" + "    <UserID>%s</UserID>"
            + "    <OTP>%s</OTP>" + "  <OpenSession>true</OpenSession>  </AuthenticationRequest>")

    /**
     * Validates token with authentication server.
     *
     * @param token             Token to be verified.
     * @param authInput         Selected authentication input.
     * @param completionHandler Completion handler triggered in UI thread once operation is done.
     */
    fun verifyWithToken(
        token: DualSeedOathToken,
        authInput: AuthInput?,
        completionHandler: GenericOtpHandler
    ) {
        try {
            verifyWithToken(
                token.name,
                OtpLogic.generateOtp(token, authInput),
                completionHandler
            )
        } catch (exception: IdpException) {
            completionHandler.onFinished(false, exception.message, null)
        }
    }

    /**
     * Validates generated OTP with authentication server.
     *
     * @param tokenName         User Id / Token Name
     * @param otpValue          Generated OTP.
     * @param completionHandler Callback to the application
     */
    private fun verifyWithToken(
        tokenName: String,
        otpValue: OtpValue,
        completionHandler: GenericOtpHandler
    ) {
        val toHash = java.lang.String.format(
            "%s:%s",
            InBandVerificationConfig.basicAuthenticationUsername,
            InBandVerificationConfig.basicAuthenticationPassword
        )
        val hash = Base64.encodeToString(
            toHash.toByteArray(StandardCharsets.UTF_8),
            Base64.DEFAULT
        )
        val body = String.format(
            XML_TEMPLATE_AUTH,
            tokenName,
            otpValue.otp.toString()
        )
        val headers: MutableMap<String, String> =
            HashMap()
        headers["Authorization"] = String.format("Basic %s", InBandVerificationConfig.basicAuthenticationPassword)

        // We don't need otp any more. Wipe it.
        otpValue.wipe()
        ExecutionService.getExecutionService().runOnBackgroudThread {
            doPostRequest(InBandVerificationConfig.authenticationUrl,
                "text/xml",
                headers,
                body,
                GenericHandler { success: Boolean, result: String ->
                    val valid =
                        success && result.equals("Authentication succeeded", ignoreCase = true)
                    completionHandler.onFinished(valid, result, otpValue.lifespan)
                }
            )
        }
    }

    @Throws(IOException::class)
    private fun createConnection(
        @NonNull hostUrl: String,
        @NonNull contentType: String,
        @NonNull headers: Map<String, String>
    ): HttpURLConnection {
        val url = URL(hostUrl)
        val connection =
            url.openConnection() as HttpURLConnection
        for ((key, value) in headers) {
            connection.setRequestProperty(key, value)
        }
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", contentType)
        connection.doOutput = true
        connection.useCaches = false
        connection.readTimeout = 10000
        connection.connectTimeout = 10000
        return connection
    }

    @Throws(IOException::class)
    private fun convertStreamToString(inputStream: InputStream?): String {
        if (inputStream != null) {
            val writer: Writer = StringWriter()
            val buffer = CharArray(1024)
            try {
                val reader: Reader = BufferedReader(
                    InputStreamReader(
                        inputStream,
                        StandardCharsets.UTF_8
                    ), 1024
                )
                var numberOfCharacters = reader.read(buffer)
                while (numberOfCharacters != -1) {
                    writer.write(buffer, 0, numberOfCharacters)
                    numberOfCharacters = reader.read(buffer)
                }
            } finally {
                inputStream.close()
            }
            return writer.toString()
        }
        return ""
    }

    /**
     * Does a POST request.
     *
     * @param hostUrl     URL.
     * @param contentType Content type.
     * @param headers     Headers.
     * @param body        Body.
     * @param callback    Callback back to the application.
     */
    private fun doPostRequest(
        @NonNull hostUrl: String,
        @NonNull contentType: String,
        @NonNull headers: Map<String, String>,
        @NonNull body: String,
        @NonNull callback: GenericHandler
    ) {
        try {
            val connection =
                createConnection(hostUrl, contentType, headers)
            OutputStreamWriter(connection.outputStream).use { outputStreamWriter ->
                outputStreamWriter.write(body)
                outputStreamWriter.flush()
            }
            val statusCode = connection.responseCode
            val responseBody: String // NOPMD - no reason to turn final local variable in to field
            responseBody = if (statusCode > 226) {
                ""
            } else {
                convertStreamToString(connection.inputStream)
            }
            ExecutionService.getExecutionService()
                .runOnMainUiThread { callback.onFinished(true, responseBody) }
        } catch (exception: IOException) {
            ExecutionService.getExecutionService().runOnMainUiThread {
                callback.onFinished(
                    false,
                    exception.message
                )
            }
        }
    }
}