package pqh.vn.pliz.etoken.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import pqh.vn.pliz.etoken.activity.BaseActivity

open  class BaseFragment : Fragment() {
    var mView : View? = null
    var mBaseActivity : BaseActivity? = null
    private var mLastDialogFragmentTag : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(context is BaseActivity){
            mBaseActivity = context as BaseActivity
        }
    }

    open fun dialogFragmentShow(
        dialog: DialogFragment,
        dialogTag: String,
        fullscreen: Boolean
    ) {
        // Hide any previous dialogs if exists.
        dialogFragmentHide()

        // If desired make dialog appear in fullscreen.
        if (fullscreen) {
            dialog.setStyle(
                DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen
            )
        }


        // Save last tag and display fragment.
        mLastDialogFragmentTag = dialogTag

        val ft = childFragmentManager.beginTransaction()
        val prev = childFragmentManager.findFragmentByTag(mLastDialogFragmentTag)
        if (prev != null && prev is DialogFragment) {
            prev.dismissAllowingStateLoss()
        }
        dialog.show(ft, mLastDialogFragmentTag)
    }

    open fun dialogFragmentHide() {
        // Hide fragment if exists
        if (mLastDialogFragmentTag != null) {
            val fragment: Fragment? =
                childFragmentManager.findFragmentByTag(mLastDialogFragmentTag)
            if (fragment != null && fragment is DialogFragment) {
                fragment.dismissAllowingStateLoss()
            }
            mLastDialogFragmentTag = null // NOPMD
        }
    }
}