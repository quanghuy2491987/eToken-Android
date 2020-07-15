package pqh.vn.pliz.etoken.logic;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.gemalto.idp.mobile.authentication.AuthenticationModule;
import com.gemalto.idp.mobile.authentication.mode.pin.PinAuthInput;
import com.gemalto.idp.mobile.authentication.mode.pin.PinAuthService;
import com.gemalto.idp.mobile.core.ApplicationContextHolder;
import com.gemalto.idp.mobile.core.HookingDetectionListener;
import com.gemalto.idp.mobile.core.IdpCore;
import com.gemalto.idp.mobile.core.SecurityDetectionService;
import com.gemalto.idp.mobile.core.passwordmanager.PasswordManager;
import com.gemalto.idp.mobile.core.passwordmanager.PasswordManagerException;
import com.gemalto.idp.mobile.core.root.RootDetector;
import com.gemalto.idp.mobile.otp.OtpConfiguration;

import pqh.vn.pliz.etoken.activity.MainActivity;
import pqh.vn.pliz.etoken.fake.FakeAppData;
import pqh.vn.pliz.etoken.util.Common;
import pqh.vn.pliz.etoken.util.Constant;

public class AppController extends Application {
    private static AppController instance;
    public String pin;
    public int logInCount = 0;
    public long lastTimeAccess = 0;
    public long backgroundTimeToExit = 60000;
    public Uri callAppUrl  = null;

    public static AppController getInstance() {
        return instance;
    }

    private PinAuthService pinAuthService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //installOtp();
    }

    public void installOtp() {
        ApplicationContextHolder.setContext(getApplicationContext());
        System.setProperty("java.io.tmpdir", getDir("files", Context.MODE_PRIVATE).getPath());
        // Preload native libs to make sure it is loaded before any execution of Ezio SDK
        IdpCore.preLoad();

        SecurityDetectionService.setHookingDetectionListener(() -> true);
       SecurityDetectionService.clearHookingDetectionListener();
//        SecurityDetectionService.setDebuggerDetection(false);

        // WARNING: set Configuration once ONLY
        if (!IdpCore.isConfigured()) {
            // Initialize otp configuration
            // OtpConfiguration otpConfig = new OtpConfiguration.Builder()
            //       .setRootPolicy(OtpConfiguration.TokenRootPolicy.FAIL)
            //       .build();
            OtpConfiguration otpConfig = new OtpConfiguration.Builder()
                    .setRootPolicy(OtpConfiguration.TokenRootPolicy.IGNORE)
                    .build();
            // Configure the Core, activation Code will be required if restricted features
            // are to be used
            IdpCore.configure(FakeAppData.getActivationcode(), otpConfig);
            RootDetector detector = IdpCore.getInstance().getRootDetector();
            // Is the physical device rooted?
            RootDetector.RootStatus rootStatus = detector.getRootStatus();
            if (rootStatus == RootDetector.RootStatus.ROOTED) {
                // Besides the root policy configured,
                // application can decide what to do when device is rooted
                Log.e("check root","Device detected as rooted");
            } else {
                Log.e("check root","Device detected as clean");
            }
            try {

                PasswordManager pm = IdpCore.getInstance().getPasswordManager();
                // Login the Password Manager if not logged in
                if (!pm.isLoggedIn()) {
                    pm.login();
                }
            } catch (Exception e) {
                // handle error
                e.printStackTrace();
            }
        }

        AuthenticationModule authenticationModule = AuthenticationModule.create();
        // Create PinAuthService. It's the entry point for all pin authentication related features.
        pinAuthService = PinAuthService.create(authenticationModule);

    }

    public void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (editText != null && editText.getWindowToken() != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public void saveReference(String key, String data) {
        SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_STORE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, data);
        editor.commit();
    }

    public String getReference(String key) {
        SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_STORE_KEY, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    public void clearReference(String key) {
        SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_STORE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public void clearReference() {
        SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_STORE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public void login(String pin) {
        this.pin = pin;
        this.logInCount = 0;
    }

    public boolean checkLogin(String pin){
        String currentPin = getReference(Constant.PIN_STORE_KEY);
        if(Common.compairHashString(pin,currentPin)){
            return true;
        } else {
            logInCount ++;
            return false;
        }

    }
    public void logOut() {
        this.logInCount = 0;
        this.pin = null;
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
    }

    public void clearAll() {
        ProvisioningLogic.clearToken();
        clearReference();
    }

    public void reInstall() {
        clearAll();
        this.logInCount = 0;
        this.pin = null;
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
    }

    public PinAuthInput securePin(String pin) {
        return pinAuthService.createAuthInput(pin);
    }


}
