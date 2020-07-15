package pqh.vn.pliz.etoken.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.thalesgroup.mobileprotector.commonutils.helpers.Lifespan
import com.thalesgroup.mobileprotector.commonutils.helpers.OtpValue
import kotlinx.android.synthetic.main.fragment_normal_code.*
import pqh.vn.pliz.etoken.R
import pqh.vn.pliz.etoken.logic.AppController
import pqh.vn.pliz.etoken.logic.OtpLogic
import pqh.vn.pliz.etoken.logic.ProvisioningLogic
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
class NormalCodeFragment : BaseFragment(), View.OnClickListener {
    private var param1: String? = null
    private var param2: String? = null
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
        mView = mView ?: inflater.inflate(R.layout.fragment_normal_code, container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configView()
    }

    override fun onDestroy() {
        super.onDestroy()
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
        normalOTPCopy.setOnClickListener(this)
    }

    fun startCountDown(lifespan: Lifespan) {
        if (!isCountDown) {
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
        val authInput = AppController.getInstance().securePin(AppController.getInstance().pin)
        val otpValue = OtpLogic.generateOtp(token, authInput)
        normalOTPCode.setText(otpValue.otp.toString())
        startCountDown(otpValue.lifespan)
        otpValue.wipe()
    }

    override fun onClick(v: View?) {
        if (v?.id == normalOTPCopy.id) {
            mBaseActivity?.setClipboard(normalOTPCode.text.toString().replace(" ", ""))
            mBaseActivity?.showToast("copy to clipboard")
            mBaseActivity?.finish()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String?, param2: String?) =
            NormalCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
