package pqh.vn.pliz.etoken.activity

import android.content.ClipData
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.ClipboardManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import pqh.vn.pliz.etoken.R
import pqh.vn.pliz.etoken.activity.ui.LoadingDialog
import pqh.vn.pliz.etoken.ui.AlertDialog


open class BaseActivity : AppCompatActivity() {

    private var mLastDialogFragmentTag : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Synchronized open fun displayAlert(title: String? = getString(R.string.alert_title), message: String?, textView: View? = null, forceLogout: Boolean = false, listener: AlertDialog.AlertButtonClick? = null, idRequest: String? = null, isShowButtonOk: Boolean = false, isShowHeaderIcon: Boolean = false, okButtonText: String? = null, closeButtonText : String? = null) {
        runOnUiThread {
            dismissProgressLoading()
            val prev = supportFragmentManager.findFragmentByTag("alert_dialog")
            if (prev != null && prev is DialogFragment) {
                prev.dismissAllowingStateLoss()
            }
            // Create and show the dialog.
            val newFragment = AlertDialog.newInstance()
            newFragment.mBaseActivity = this
            newFragment.title = title
            newFragment.messageString = message
            newFragment.forceLogout = forceLogout
            newFragment.textView = textView
            newFragment.alertListener = listener
            newFragment.idRequest = idRequest
            newFragment.isShowButtonOk = isShowButtonOk
            newFragment.isShowIcon = isShowHeaderIcon
            newFragment.okButtonText = okButtonText
            newFragment.closeButtonText = closeButtonText

            try {
                val ft = supportFragmentManager.beginTransaction()
                newFragment.show(ft, "alert_dialog")
            } catch (e: Exception) {
                val ft = supportFragmentManager.beginTransaction()
                ft.add(newFragment, "alert_dialog")
                ft.commitAllowingStateLoss()
            }
        }
    }

    open fun setClipboard(
        text: String
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard =
                this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = text
        } else {
            val clipboard =
                this.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", text)
            clipboard.setPrimaryClip(clip)
        }
    }
    fun displayProgressLoading() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog_loading")

        if (prev != null && prev is DialogFragment) {
            prev.dismissAllowingStateLoss()
        }
        // Create and show the dialog.
        val newFragment = LoadingDialog.newInstance()
        newFragment.show(ft,"dialog_loading")
        supportFragmentManager.executePendingTransactions()
    }

    fun dismissProgressLoading() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog_loading")
        if (prev != null && prev is DialogFragment) {
            prev.dismissAllowingStateLoss()
        }
    }
    fun requestEditText(editText: EditText) {
        runOnUiThread {
            editText.requestFocus()
            showKeyBoard(editText)
        }
    }
    open fun hideKeyBoard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    open fun showKeyBoard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }


    fun showToast(message: String?) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
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

        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag(mLastDialogFragmentTag)
        if (prev != null && prev is DialogFragment) {
            prev.dismissAllowingStateLoss()
        }
        try {
            val ft = supportFragmentManager.beginTransaction()
            dialog.show(ft, mLastDialogFragmentTag)
        } catch (e: Exception) {
            val ft = supportFragmentManager.beginTransaction()
            ft.add(dialog, mLastDialogFragmentTag)
            ft.commitAllowingStateLoss()
        }
    }

    open fun dialogFragmentHide() {
        // Hide fragment if exists
        if (mLastDialogFragmentTag != null) {
            val fragment: Fragment? =
                supportFragmentManager.findFragmentByTag(mLastDialogFragmentTag)
            if (fragment != null && fragment is DialogFragment) {
                fragment.dismissAllowingStateLoss()
            }
            mLastDialogFragmentTag = null // NOPMD
        }
    }
    companion object {
        private val TAG = "BaseActivity"
    }
}