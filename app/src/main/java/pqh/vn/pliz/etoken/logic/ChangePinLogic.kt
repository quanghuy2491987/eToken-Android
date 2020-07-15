package pqh.vn.pliz.etoken.logic

import com.gemalto.idp.mobile.authentication.mode.pin.PinAuthInput
import com.gemalto.idp.mobile.core.IdpCore
import com.gemalto.idp.mobile.core.IdpException
import com.gemalto.idp.mobile.otp.oath.soft.SoftOathToken
import com.thalesgroup.mobileprotector.commonutils.helpers.AbstractBaseLogic

object ChangePinLogic : AbstractBaseLogic() {
    const val SUCCESS = "success"
    const val DIFFERENT = "different"

    fun changePin(
        token: SoftOathToken,
        oldPin: PinAuthInput,
        newPin: PinAuthInput,
        newPinConfirmation: PinAuthInput
    ): String? {
        val retValue: String?

        // Check both entries consistency.
        retValue = if (newPin.equals(newPinConfirmation)) {
            // Try to change pin.
            try {

                // This is to make sure passwordManager has been logged in
                // It is not mandatory as long as the application make sure it is logged in before
                // the token is to be retrieved
                val pm = IdpCore.getInstance().passwordManager
                // Login the Password Manager if not logged in
                // Login the Password Manager if not logged in
                if (!pm.isLoggedIn) {
                    pm.login()
                }
                token.changePin(oldPin, newPin)
                SUCCESS
            } catch (exception: IdpException) {
                exception.message
            }
        } else {
            DIFFERENT
        }

        // Wipe all auth inputs
        oldPin.wipe()
        newPin.wipe()
        newPinConfirmation.wipe()
        return retValue
    }

}