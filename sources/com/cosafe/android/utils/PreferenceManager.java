package com.cosafe.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceManager {
    public static final String HOME_LOCATION = "home_loc";
    public static final String IS_INSTRUCTION_SHOWN = "isInstructionShown";
    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String KEY_TOKEN = "keytoken";
    public static final String QURENTINE = "quentine";
    public static final String USER_ID = "userId";
    public static final String USER_MOBILE_NO = "umobile";
    private static SharedPreferences mSharedPref;

    private PreferenceManager() {
    }

    public static void init(Context context) {
        if (mSharedPref == null) {
            mSharedPref = context.getSharedPreferences(context.getPackageName(), 0);
        }
    }

    public static String read(String str, String str2) {
        return mSharedPref.getString(str, str2);
    }

    public static void write(String str, String str2) {
        Editor edit = mSharedPref.edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static boolean read(String str) {
        return mSharedPref.getBoolean(str, false);
    }

    public static void write(String str, boolean z) {
        Editor edit = mSharedPref.edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    public static Integer read(String str, int i) {
        return Integer.valueOf(mSharedPref.getInt(str, i));
    }

    public static void write(String str, int i) {
        mSharedPref.edit().putInt(str, i).apply();
    }

    public static void writeLong(String str, long j) {
        mSharedPref.edit().putLong(str, j).apply();
    }

    public static Long readLong(String str, long j) {
        return Long.valueOf(mSharedPref.getLong(str, j));
    }

    public static void remove(String str) {
        mSharedPref.edit().remove(str).apply();
    }

    public static boolean isLoggedIn() {
        return read(IS_LOGGED_IN);
    }

    public static boolean isInstructionShown() {
        return read(IS_INSTRUCTION_SHOWN);
    }
}
