package com.seroperson.itstimetoact;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.SharedPreferences;

public class TimeToAct {

    private static final String DEFAULT_PREFERENCES_NAME = "time_to_act";
    private static final int DEFAULT_PREFERENCES_MODE = Context.MODE_PRIVATE;

    private final SharedPreferences preferences;

    public TimeToAct(Context context) {
        this(context, DEFAULT_PREFERENCES_NAME);
    }

    public TimeToAct(Context context, String preferencesName) {
        this(context, preferencesName, DEFAULT_PREFERENCES_MODE);
    }

    public TimeToAct(Context context, String preferencesName, int mode) {
        preferences = context.getSharedPreferences(preferencesName, mode);
    }

}
