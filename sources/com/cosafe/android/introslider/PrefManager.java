package com.cosafe.android.introslider;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefManager {
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_FIRST_TIME_LAUNCH_GUIDE = "IsFirstTimeLaunchGuide";
    private static final String PREF_NAME = "coronakavach-welcome";
    int PRIVATE_MODE = 0;
    Context _context;
    Editor editor;
    SharedPreferences pref;

    public PrefManager(Context context) {
        this._context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        this.pref = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }

    public void setFirstTimeLaunch(boolean z) {
        this.editor.putBoolean(IS_FIRST_TIME_LAUNCH, z);
        this.editor.commit();
    }

    public void setFirstTimeLaunchGuide(boolean z) {
        this.editor.putBoolean(IS_FIRST_TIME_LAUNCH_GUIDE, z);
        this.editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return this.pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public boolean isFirstTimeLaunchGuide() {
        return this.pref.getBoolean(IS_FIRST_TIME_LAUNCH_GUIDE, true);
    }
}
