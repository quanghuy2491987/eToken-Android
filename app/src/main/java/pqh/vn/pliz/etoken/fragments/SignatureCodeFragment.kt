package pqh.vn.pliz.etoken.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import com.thalesgroup.mobileprotector.commonutils.helpers.Lifespan
import com.thalesgroup.mobileprotector.uicomponents.QrCodeReaderFragment
import com.thalesgroup.mobileprotector.uicomponents.QrCodeReaderFragmentDelegate
import kotlinx.android.synthetic.main.fragment_signature_code.*
import pqh.vn.pliz.etoken.R
import pqh.vn.pliz.etoken.logic.AppController
import pqh.vn.pliz.etoken.logic.ProvisioningLogic
import pqh.vn.pliz.etoken.logic.QrCodeBasicLogic
import pqh.vn.pliz.etoken.logic.TransactionSignLogic
import pqh.vn.pliz.etoken.util.Common
import pqh.vn.pliz.etoken.util.Constant


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NormalCodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignatureCodeFragment : BaseFragment(), View.OnClickListener , QrCodeReaderFragmentDelegate {
    private var param1: String? = null
    private var param2: String? = null
    private var otpText = Common.CreateRandomOTP(6)
    var countDownTimer: CountDownTimer? = null
    var isCountDown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = mView ?: inflater.inflate(R.layout.fragment_signature_code, container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configView()
    }

    override fun onResume() {
        super.onResume()
        doGetotp()
    }

    override fun onPause() {
        super.onPause()
        countDownTimer?.cancel()
        countDownTimer = null
        isCountDown = false
    }

    fun configView() {
        signatureOTPCopy.setOnClickListener(this)
        signatuerOTPTransCode.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //do here your stuff f
                doGetotp()
                true
            } else false
        })
        signatuerOTPTransCode.setOnFocusChangeListener { v, hasFocus ->
            doGetotp()
        }
        signatureOTPCode.setOnClickListener {
            signatuerOTPTransCode.clearFocus()
            AppController.getInstance().hideKeyboard(signatuerOTPTransCode)
        }
        signatureOTPScanQRButton.setOnClickListener {
            val qrCodeReaderFragment = QrCodeReaderFragment()
            qrCodeReaderFragment.setDelegate(this)

            mBaseActivity?.dialogFragmentShow(
                qrCodeReaderFragment,
                Constant.DIALOG_TAG_QR_CODE_READER,
                true
            )
        }

        if(param1 != null) {
            signatuerOTPTransCode.setText(param1)
        }

    }

    //endregion
    //region QrCodeReaderFragmentDelegate
    override fun onQRCodeProvided(qrCode: String?) {
        // Hide reader.
        mBaseActivity?.dialogFragmentHide()
        signatuerOTPTransCode.setText(qrCode)
        doGetotp()
    }

    override fun onPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == QrCodeReaderFragment.CAMERA_REQUEST_PERMISSION) {
            val qrCodeReaderFragment = QrCodeReaderFragment()
            qrCodeReaderFragment.setDelegate(this)

            mBaseActivity?.dialogFragmentShow(
                qrCodeReaderFragment,
                Constant.DIALOG_TAG_QR_CODE_READER,
                true
            )
        }
    }
    fun startCountDown(lifespan: Lifespan) {
        if (!isCountDown)
            isCountDown = true
        countDownTimer = object : CountDownTimer(lifespan.current * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                normalOTPCountdown?.setText(
                    Constant.stringToHTML(
                        getString(
                            R.string.otp_countdown_title,
                            millisUntilFinished / 1000
                        )
                    )
                )

                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                isCountDown = false
                doGetotp()
                val r = Runnable { countDownTimer?.start() }
                Handler().postDelayed(r, 0) //delay repeat timer 2 seconds
                countDownTimer?.cancel()
            }
        }.start()
    }


    private fun doGetotp() {
        val token = ProvisioningLogic.getToken(Constant.USER_STORE_KEY)
        if (token == null) {
            AppController.getInstance().clearAll()
            return
        }
        if (Common.isEmpty(AppController.getInstance().pin)) {
            AppController.getInstance().logOut()
            return
        }
        val tranCode = signatuerOTPTransCode.text.toString()
        if (Common.isEmpty(tranCode)) {
            return
        }
        val authInput = AppController.getInstance().securePin(AppController.getInstance().pin)
        val otpValue = TransactionSignLogic.generateOtp(token, authInput, tranCode)
        signatureOTPCode.setText(otpValue.otp.toString())
        startCountDown(otpValue.lifespan)
        otpValue.wipe()
    }

    override fun onClick(v: View?) {
        if (v?.id == signatureOTPCopy.id) {
            mBaseActivity?.setClipboard(signatureOTPCode.text.toString().replace(" ", ""))
            mBaseActivity?.showToast("copy to clipboard")
            mBaseActivity?.finish()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NormalCodeFragment.
         */
        @JvmStatic
        fun newInstance(param1: String?, param2: String?) =
            SignatureCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
