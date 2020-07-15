package pqh.vn.pliz.etoken.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pqh.vn.pliz.etoken.R
import pqh.vn.pliz.etoken.logic.AppController
import pqh.vn.pliz.etoken.logic.ProvisioningLogic
import pqh.vn.pliz.etoken.util.Common
import pqh.vn.pliz.etoken.util.Constant

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configView()
    }
    fun configView() {
        // Get stored token
        val token = ProvisioningLogic.getToken(Constant.USER_STORE_KEY)
        if(token == null) {
            //active
            AppController.getInstance().clearAll()
            findNavController().navigate(R.id.active_action)
        } else {
            val pin = AppController.getInstance().getReference(Constant.PIN_STORE_KEY)
            if(Common.isEmpty(pin)){
                findNavController().navigate(R.id.gotoCreatePin)
            } else {
                findNavController().navigate(R.id.gotoLogin)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
