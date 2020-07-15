package pqh.vn.pliz.etoken.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.gemalto.idp.mobile.core.IdpCore
import com.gemalto.idp.mobile.core.root.RootDetector.RootStatus
import kotlinx.android.synthetic.main.activity_main.*
import pqh.vn.pliz.etoken.R
import pqh.vn.pliz.etoken.logic.AppController
import pqh.vn.pliz.etoken.ui.AlertDialog
import java.util.*


class MainActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callApp(intent)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        leftToolbarButton.setOnClickListener(this)
       // disableScreenShot()

        AppController.getInstance().installOtp()
        checkAvaliable()
        AppController.getInstance().lastTimeAccess = Date().time
    }

    override fun onPause() {
        super.onPause()
        AppController.getInstance().lastTimeAccess = Date().time
    }

    override fun onResume() {
        super.onResume()
        val currentTime = Date().time
        if ((currentTime - AppController.getInstance().lastTimeAccess) > AppController.getInstance().backgroundTimeToExit) {
            finish()
            AppController.getInstance().logOut()
        } else {
            AppController.getInstance().lastTimeAccess = Date().time
        }
    }
    fun checkAvaliable() {

        // DETECTING IF A ROOTED DEVICE
        // Note: For a real application, detecting rooted devices
        // is fast enough to be done in the UI thread. The application may
        // decide if this is done in a separate execution thread.
        val detector = IdpCore.getInstance().rootDetector
        // Is the physical device rooted?
        // Is the physical device rooted?
        val rootStatus = detector.rootStatus
        if (rootStatus == RootStatus.ROOTED) {
            displayAlert(message = "Thiết bị của bạn đã được root. Vì lý do bảo mật ứng dụng sẽ không thể chạy trên thiết bị này", listener = object :
                AlertDialog.AlertButtonClick {
                override fun onAlertButtonOKClick(view: DialogFragment) {
                    finish()
                }

                override fun onAlertButtonCloseClick(view: DialogFragment) {
                    finish()
                }
            })
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.leftToolbarButton -> {
                onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_changepin -> {
                val arguments = Bundle().apply {
                    putInt("type", 1)
                }
                findNavController(R.id.nav_host_fragment).navigate(R.id.loginFragment,arguments)

            }
            R.id.clear_token-> {
                AppController.getInstance().reInstall()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun disableScreenShot() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    fun callApp(callIntent: Intent?){
        val url = callIntent?.data
        AppController.getInstance().callAppUrl = null
        if (url != null ) {
            val scheme = url.scheme
            if (scheme != null && scheme.equals(getString(R.string.app_scheme), true)) {
                AppController.getInstance().callAppUrl = url
            }
        }
        val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val tasks = activityManager.getRunningTasks(Integer.MAX_VALUE)

        for (task in tasks) {
            if (this.getPackageName().equals(task.baseActivity.packageName,true)){
                if(!this.javaClass.name.equals(task.baseActivity.className)){
                    finish()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        callApp(intent)
    }
}
