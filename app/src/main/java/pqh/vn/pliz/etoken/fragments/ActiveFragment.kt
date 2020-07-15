package pqh.vn.pliz.etoken.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gemalto.idp.mobile.core.IdpCore
import kotlinx.android.synthetic.main.fragment_active.*
import pqh.vn.pliz.etoken.R
import pqh.vn.pliz.etoken.logic.AppController
import pqh.vn.pliz.etoken.logic.ProvisioningLogic
import pqh.vn.pliz.etoken.ui.AlertDialog
import pqh.vn.pliz.etoken.util.Constant

/**
 * A simple [Fragment] subclass.
 * Use the [ActiveFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActiveFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = mView ?: inflater.inflate(R.layout.fragment_active, container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configView()
    }

    fun configView() {
        btActive.setOnClickListener {
             doProvisioning()
        }
    }

    private fun doProvisioning() {
        AppController.getInstance().hideKeyboard(txtActiveCode)
        val regCode = txtActiveCode.text.toString()

        if ( regCode.isEmpty()) {
            mBaseActivity?.displayAlert(message = getString(R.string.empty_user_id_or_reg_code))
            return
        }

        mBaseActivity?.displayProgressLoading()
        val registrationCode = IdpCore.getInstance().secureContainerFactory
            .fromString(regCode)


        AppController.getInstance().clearAll()
        ProvisioningLogic.provisionWithUserId(Constant.USER_STORE_KEY, registrationCode) { success, result ->
            // Wipe registration code. Since it's not needed anymore.
            registrationCode.wipe()

            // Hide loading overlay.
            mBaseActivity?.dismissProgressLoading()

            // Display result.
            if (success) {
                txtActiveCode.text.clear()
                onActiveSuccess(Constant.USER_STORE_KEY)
            } else {
                mBaseActivity?.displayAlert(message = result)
                onActiveFail()
            }
        }


    }

    private fun onActiveSuccess(userId : String) {
        AppController.getInstance().saveReference(Constant.USER_STORE_KEY,userId)
        mBaseActivity?.displayAlert(
            message = getString(R.string.active_success_text),
            listener = object : AlertDialog.AlertButtonClick {
                override fun onAlertButtonOKClick(view: DialogFragment) {

                }

                override fun onAlertButtonCloseClick(view: DialogFragment) {
                    findNavController().navigate(R.id.gotoCreatePin)
                }
            })
    }

    fun onActiveFail() {

    }
}
