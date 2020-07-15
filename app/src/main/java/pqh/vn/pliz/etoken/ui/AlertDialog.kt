package pqh.vn.pliz.etoken.ui

import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spanned
import android.text.SpannedString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.alert_dialog_layout.*
import pqh.vn.pliz.etoken.R
import pqh.vn.pliz.etoken.activity.BaseActivity
import pqh.vn.pliz.etoken.fragments.BaseFragment
import pqh.vn.pliz.etoken.util.Constant

class AlertDialog : DialogFragment(), View.OnClickListener {

    var isShowIcon = false
    var isShowButtonClose = true
    var isShowButtonOk = false
    var messageString: String? = null
    var title: String? = null
    var alertListener: AlertButtonClick? = null
    var forceLogout = false
    var mBaseActivity: BaseActivity? = null
    var idRequest: String? = null
    var textView: View? = null
    var okButtonText: String? = null
    var closeButtonText : String? = null

    companion object {
        fun newInstance(): AlertDialog {
            val alert = AlertDialog()
            return alert
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.alert_dialog_layout, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isShowIcon) {
            iconSuccess.visibility = View.VISIBLE
            txtHeader.visibility = View.GONE
        } else {
            iconSuccess.visibility = View.GONE
            txtHeader.visibility = View.VISIBLE
        }

        if (isShowButtonClose) {
            btClose.visibility = View.VISIBLE
            if (closeButtonText != null) {
                btClose.setText(closeButtonText)
            }
            btClose.setOnClickListener(this)
        } else {
            btClose.visibility = View.GONE
        }

        if (isShowButtonOk) {
            btOk.visibility = View.VISIBLE
            btOk.setOnClickListener(this)
            if (okButtonText != null) {
                btOk.setText(okButtonText)
            }
        } else {
            btOk.visibility = View.GONE
            var typeFace = Typeface.createFromAsset(context!!.assets, "fonts/Nexa Bold.ttf")
            btClose.typeface = typeFace
        }

        txtHeader.text = title
        txtMessage.setText(Constant.stringToHTML(messageString))
        animation()
    }

    fun animation() {
        val w = Resources.getSystem().displayMetrics.widthPixels
        val h = Resources.getSystem().displayMetrics.heightPixels
        val currerY = alertBox?.translationY
        alertBox?.translationY = h * 1f
        alertBox?.animate()?.translationY(currerY ?: 0f)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            btOk.id -> {

                alertListener?.onAlertButtonOKClick(this)
                if (textView != null && textView is EditText) {
                    mBaseActivity?.requestEditText(textView as EditText)
                }
                if (forceLogout) {
                }
            }
            btClose.id -> {

                alertListener?.onAlertButtonCloseClick(this)
                if (textView != null && textView is EditText) {
                    mBaseActivity?.requestEditText(textView as EditText)
                }
                if (forceLogout) {
                }
            }
        }
        this.dismiss()
    }

    open interface AlertButtonClick {
        fun onAlertButtonOKClick(view: DialogFragment)
        fun onAlertButtonCloseClick(view: DialogFragment)
    }
}