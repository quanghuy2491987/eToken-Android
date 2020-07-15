package pqh.vn.pliz.etoken.util

import android.util.Log

class Logger {
    companion object {
        private val LOGGING = true
        private val TAG = "LoginPadApp: "

        fun i(className: String, message: String) {
            if (LOGGING) {
                Log.i(TAG, "$className - $message")
            }
        }

        fun d(className: String, message: String) {
            if (LOGGING) {
                Log.d(TAG, "$className - $message")
            }
        }

        fun v(className: String, message: String) {
            if (LOGGING) {
                Log.v(TAG, "$className - $message")
            }
        }

        fun e(className: String, message: String, e: Exception) {
            if (LOGGING) {
                Log.e(TAG, "$className - $message", e)
            }
        }
    }
}