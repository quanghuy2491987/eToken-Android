package pqh.vn.pliz.etoken.util

import android.text.Html
import android.text.Spanned

class Constant {
    companion object {

        const val PREFERENCE_STORE_KEY = "pqh.vn.pliz.etoken.preference"
        const val PIN_STORE_KEY = "pin_authentication_key"
        const val PROVISSIONG_STORE_KEY = "provission_key"
        const val PIN_STEP_BUNDLE_KEY = "pin_step_bundle"
        const val USER_STORE_KEY = "user_id_store"
        const val DEFAULT_PIN = "123456"
        const val DIALOG_TAG_QR_CODE_READER = "DIALOG_TAG_QR_CODE_READER"

        val MAX_OTP_TIME = 30 * 1000
        fun stringToHTML(html: String?): Spanned {
            var sequence: Spanned
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                sequence = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                sequence = Html.fromHtml(html)
            }
            return sequence
        }

        fun encrypt(srcStr: String): String {
            var encr = ""

            return encr
        }

    }

}