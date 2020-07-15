package pqh.vn.pliz.etoken.activity.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.progress_layout.*
import pqh.vn.pliz.etoken.R


class LoadingDialog : DialogFragment() {

    companion object {
        fun newInstance(): LoadingDialog {
            val load = LoadingDialog()
            return load
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewGroup = inflater?.inflate(R.layout.progress_layout, container, false)
        return viewGroup
    }

    override fun onResume() {
        super.onResume()
        val rotate = RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        )

        rotate.duration = 900
        rotate.repeatCount = Animation.INFINITE
        imgLoading.startAnimation(rotate)

    }

    override fun onPause() {
        imgLoading.clearAnimation()
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.isCancelable = false
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme)
    }
}