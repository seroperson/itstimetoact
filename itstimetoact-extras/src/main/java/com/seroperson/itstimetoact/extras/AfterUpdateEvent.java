package com.seroperson.itstimetoact.extras;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.seroperson.itstimetoact.event.ActEvent;

import java.io.Serializable;

public class AfterUpdateEvent extends ActEvent implements Serializable {

    private static final long serialVersionUID = 0L;

    private final String savedVersion;
    private String currentVersion;

    public AfterUpdateEvent(Context context, String eventKey) {
        this(getApplicationVersion(context), eventKey);
    }

    public AfterUpdateEvent(String savedVersion, String eventKey) {
        super(eventKey);
        this.savedVersion = savedVersion;
    }

    @Override
    public void onInitialize(Context context) {
        super.onInitialize(context);
        currentVersion = getApplicationVersion(context);
    }

    public String getSavedVersion() {
        return savedVersion;
    }

    @Override
    public boolean isHappened() {
        return savedVersion.equals(currentVersion);
    }

    private static String getApplicationVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // impossible
        }
        return info.versionName;
    }

}
