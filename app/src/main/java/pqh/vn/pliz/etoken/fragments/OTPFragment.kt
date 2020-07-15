package pqh.vn.pliz.etoken.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_otp.*
import pqh.vn.pliz.etoken.R
import pqh.vn.pliz.etoken.logic.AppController


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OTPFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OTPFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        mView = mView ?: inflater.inflate(R.layout.fragment_otp, container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configView()
    }


    override fun onResume() {
        super.onResume()

    }

    private fun configView(){
        var sofType = "c"
        val url = AppController.getInstance().callAppUrl
        if (url != null ) {
            val scheme = url.scheme
            if (scheme != null && scheme.equals(getString(R.string.app_scheme), true)) {
                val data = url.getQueryParameter("data")
                sofType = url.getQueryParameter("type") ?: "c"
                if (sofType.equals("c",true)){

                } else if (sofType.equals("d",true)){
                    param1 = data
                }
            }
        }

        val adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                when(position){
                    0 -> {
                        return NormalCodeFragment.newInstance(param1,param2)
                    }
                    1 -> return SignatureCodeFragment.newInstance(param1,param2)
                }
                return Fragment()
            }

            override fun getCount(): Int {
                return 2
            }

            override fun getPageTitle(position: Int): CharSequence? {
                when(position){
                    0-> return  getString(R.string.otp_normal_title)
                    1-> return getString(R.string.otp_signature_title)
                }
                return ""
            }
        }

        otpViewpager.adapter = adapter
        tabOTP.setupWithViewPager(otpViewpager)
        otpViewpager.clearOnPageChangeListeners()
        otpViewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabOTP))
        if (sofType.equals("c",true)) {
            otpViewpager.currentItem = 0
            tabOTP.getTabAt(0)?.select()
        } else {
            otpViewpager.currentItem = 1
            tabOTP.getTabAt(1)?.select()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String?, param2: String?) =
            OTPFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
