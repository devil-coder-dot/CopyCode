package com.cosafe.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyPreferences {
    public static final String PREF_MOBILE_NUMBER = "PrefMobileNumber";
    public static final String PREF_TOKEN_KEY = "Preftokenkey";
    SharedPreferences pref;

    public MyPreferences(Context context) {
        this.pref = context.getSharedPreferences(context.getPackageName(), 0);
    }

    public void savePref(String str, String str2) {
        Editor edit = this.pref.edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public String getPref(String str, String str2) {
        return this.pref.getString(str, str2);
    }
}
