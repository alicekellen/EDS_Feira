package com.engenharia.feiraapplication;

import android.app.Application;
import android.util.Log;

public class FeiraApplication extends Application {

    private static FeiraApplication appInstance;
    private static String TAG = "Feira/Application";
    private static long userSession;
    private static boolean recoverPassword;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        Log.i(TAG, "Application started");
    }

    public static FeiraApplication getInstance() {
        return appInstance;
    }

    public void setUserSession(long user) {
        this.userSession = user;
    }

    public long getUserSession() {
        return this.userSession;
    }

    public static boolean isRecoverPassword() {
        return recoverPassword;
    }

    public static void setRecoverPassword(boolean recoverPassword) {
        FeiraApplication.recoverPassword = recoverPassword;
    }
}
