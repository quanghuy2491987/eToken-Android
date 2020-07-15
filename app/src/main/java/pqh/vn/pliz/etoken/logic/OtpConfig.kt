package pqh.vn.pliz.etoken.logic

import com.gemalto.idp.mobile.core.IdpCore
import com.gemalto.idp.mobile.core.util.SecureString


/**
 * One Time Password configuration.
 */
object OtpConfig {
    /**
     * Suite will set all relevant OCRA settings accordingly.
     *
     * @return OCRA suite.
     */
    val ocraSuite: SecureString
        get() = IdpCore.getInstance().secureContainerFactory.fromString("OCRA-1:HOTP-SHA256-6:QH64-T30S")

    /**
     * Helper configuration for UI representation of OTP lifetime.
     * @return Number of second when OTP is valid.
     */
    val oTPLifetime: Int
        get() = 30
}