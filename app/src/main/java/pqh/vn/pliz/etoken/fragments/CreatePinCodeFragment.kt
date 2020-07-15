package pqh.vn.pliz.etoken.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.gemalto.idp.mobile.authentication.mode.pin.PinAuthInput
import com.gemalto.idp.mobile.otp.oath.soft.SoftOathToken
import com.thalesgroup.mobileprotector.commonutils.callbacks.AuthPinsHandler
import com.thalesgroup.mobileprotector.commonutils.callbacks.GenericOtpHandler
import com.thalesgroup.mobileprotector.commonutils.helpers.Lifespan
import kotlinx.android.synthetic.main.fragment_create_pin_code.*
import pqh.vn.pliz.etoken.R
import pqh.vn.pliz.etoken.activity.ui.PinKeyView
import pqh.vn.pliz.etoken.logic.AppController
import pqh.vn.pliz.etoken.logic.ChangePinLogic
import pqh.vn.pliz.etoken.logic.InBandVerificationLogic
import pqh.vn.pliz.etoken.logic.ProvisioningLogic
import pqh.vn.pliz.etoken.util.Common
import pqh.vn.pliz.etoken.util.Constant

class CreatePinCodeFragment : BaseFragment(), PinKeyView.PinKeyDelegate {

    private var type = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt("type", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_pin_code, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configView()
    }

    fun configView() {
        pinInputView.editText = txtNewPin
        pinInputView.keyDelegate = this
        txtReNewPin.setOnClickListener {
            pinInputView.editText = txtReNewPin
        }
        txtNewPin.setOnClickListener { pinInputView.editText = txtNewPin }
        btCreatePin.setOnClickListener {
            if (type == 0) {
                changePin_GetAndVerifyNewPin()
            } else {
                checkChangPin()
            }
        }
    }

    override fun onTextInputClear(editText: EditText?) {
        when (editText?.id) {
            txtNewPin.id -> {

            }
            txtReNewPin.id -> {
                pinInputView.editText = txtNewPin
            }
        }
    }

    override fun onTextInputComplete(editText: EditText?, text: String) {
        when (editText?.id) {
            txtNewPin.id -> {
                pinInputView.editText = txtReNewPin
            }
            txtReNewPin.id -> {
                if(type == 0) {
                    changePin_GetAndVerifyNewPin()
                } else {
                    checkChangPin()
                }
            }
        }
    }

    //endregion
    //region Private Helpers
    private fun changePin_GetAndVerifyNewPin() {
        val newPin = txtNewPin.text.toString()
        val reNewPin = txtReNewPin.text.toString()
        val token: SoftOathToken? = ProvisioningLogic.getToken(Constant.USER_STORE_KEY)
        if (token == null) {
            AppController.getInstance().reInstall()
            return
        }
        val currenPin = Constant.DEFAULT_PIN
        val pin = AppController.getInstance().securePin(currenPin)


        val result = ChangePinLogic.changePin(
            token,
            oldPin = pin,
            newPin = AppController.getInstance().securePin(newPin),
            newPinConfirmation = AppController.getInstance().securePin(reNewPin)
        )
        when (result) {
            ChangePinLogic.SUCCESS -> {
                AppController.getInstance()
                    .saveReference(Constant.PIN_STORE_KEY, Common.getSHA256HashString(newPin))
                findNavController().navigate(R.id.goToOtp)
            }
            ChangePinLogic.DIFFERENT -> {
                mBaseActivity?.displayAlert(message = getString(R.string.create_pin_no_equal))
                pinInputView.editText = txtReNewPin
                return
            }
            else -> {
                mBaseActivity?.displayAlert(message = getString(R.string.pin_exception))
                txtReNewPin.text.clear()
            }
        }
    }


    fun checkChangPin() {
        val newPin = txtNewPin.text.toString()
        val reNewPin = txtReNewPin.text.toString()
        val token: SoftOathToken? = ProvisioningLogic.getToken(Constant.USER_STORE_KEY)
        if (token == null) {
            AppController.getInstance().reInstall()
            return
        }
        if (Common.isEmpty( AppController.getInstance().pin )) {
            AppController.getInstance().logOut()
            return
        }
        val currenPin = AppController.getInstance().pin
        val pin = AppController.getInstance().securePin(currenPin)


        val result = ChangePinLogic.changePin(
            token,
            oldPin = pin,
            newPin = AppController.getInstance().securePin(newPin),
            newPinConfirmation = AppController.getInstance().securePin(reNewPin)
        )
        when (result) {
            ChangePinLogic.SUCCESS -> {
                AppController.getInstance()
                    .saveReference(Constant.PIN_STORE_KEY, Common.getSHA256HashString(newPin))
                AppController.getInstance().logOut()
            }
            ChangePinLogic.DIFFERENT -> {
                mBaseActivity?.displayAlert(message = getString(R.string.create_pin_no_equal))
                pinInputView.editText = txtReNewPin
                return
            }
            else -> {
                mBaseActivity?.displayAlert(message = getString(R.string.pin_exception))
                txtReNewPin.text.clear()
            }
        }
    }

}
