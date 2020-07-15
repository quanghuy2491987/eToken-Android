package pqh.vn.pliz.etoken.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.gemalto.idp.mobile.otp.oath.soft.SoftOathToken
import kotlinx.android.synthetic.main.fragment_login.*

import pqh.vn.pliz.etoken.R
import pqh.vn.pliz.etoken.activity.ui.PinKeyView
import pqh.vn.pliz.etoken.logic.AppController
import pqh.vn.pliz.etoken.logic.ChangePinLogic
import pqh.vn.pliz.etoken.logic.ProvisioningLogic
import pqh.vn.pliz.etoken.ui.AlertDialog
import pqh.vn.pliz.etoken.util.Common
import pqh.vn.pliz.etoken.util.Constant

class LoginFragment : BaseFragment(), PinKeyView.PinKeyDelegate {
    var type = 0
    var newPin : String? = null
    var oldPin : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt("type",0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configView()
    }
    fun configView(){
        pinCodeView.keyDelegate = this
    }

    @SuppressLint("StringFormatMatches")
    fun login(){
        val pin = txtPinCode.text.toString()
        txtPinCode.text.clear()
        if(Common.isEmpty(pin)){
            mBaseActivity?.displayAlert(message = getString(R.string.activity_login_enter_access_code))
            return
        }

        if(AppController.getInstance().checkLogin(pin)){
            AppController.getInstance().login(pin)
           if (type == 1) {
               val arguments = Bundle().apply {
                   putInt("type", type)
               }
               findNavController().navigate(R.id.goToChangePin,arguments)
           } else {
               findNavController().navigate(R.id.goToOtp)
           }
        } else {
            if (AppController.getInstance().logInCount >= 5 ) {
                mBaseActivity?.displayAlert(message = getString(R.string.pin_fail_multi,AppController.getInstance().logInCount), listener = object : AlertDialog.AlertButtonClick {
                    override fun onAlertButtonCloseClick(view: DialogFragment) {
                        AppController.getInstance().reInstall()
                    }

                    override fun onAlertButtonOKClick(view: DialogFragment) {
                        AppController.getInstance().reInstall()
                    }
                })
            } else {
                mBaseActivity?.displayAlert(message = getString(R.string.pin_not_correct))
                txtPinCode.text.clear()
                return
            }
        }

    }


    override fun onTextInputComplete(editText: EditText?, text: String) {
        login()
    }

    override fun onTextInputClear(editText: EditText?) {

    }

}
