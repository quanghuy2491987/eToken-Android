package pqh.vn.pliz.etoken.logic

import androidx.annotation.NonNull
import com.gemalto.idp.mobile.authentication.AuthInput
import com.gemalto.idp.mobile.core.IdpCore
import com.gemalto.idp.mobile.core.IdpException
import com.gemalto.idp.mobile.core.root.RootDetector
import com.gemalto.idp.mobile.otp.OtpModule
import com.gemalto.idp.mobile.otp.oath.DualSeedOathToken
import com.gemalto.idp.mobile.otp.oath.OathService
import com.thalesgroup.mobileprotector.commonutils.helpers.AbstractBaseLogic
import com.thalesgroup.mobileprotector.commonutils.helpers.OtpValue

object OtpLogic : AbstractBaseLogic() {
    /**
     * Generates an OTP.
     *
     * @param token
     * Token to be used for OTP generation.
     * @param pin
     * PIN.
     * @return Generated OTP.
     * @throws IdpException
     * If error during OTP generation occurs.
     */
    @Throws(IdpException::class)
    fun generateOtp(@NonNull token: DualSeedOathToken?, @NonNull pin: AuthInput?): OtpValue {

        if (IdpCore.getInstance().rootDetector
                .rootStatus != RootDetector.RootStatus.NOT_ROOTED
        ) { //NOPMD
            // Handle root status according to app policy.
        }
        token?.selectKey(0)
        val oathFactory = OathService.create(OtpModule.create()).factory
        val softOathSettings = oathFactory.createSoftOathSettings()
        softOathSettings.setTotpLength(6)

        val oathDevice = oathFactory.createSoftOathDevice(token, softOathSettings)

        val pm = IdpCore.getInstance().passwordManager
        // Login the Password Manager if not logged in
        if (!pm.isLoggedIn) {
            pm.login()
        }

        return OtpValue(
            oathDevice.getTotp(pin),
            oathDevice.lastOtpLifespan,
            OtpConfig.oTPLifetime
        )
    }

}