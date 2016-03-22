package com.seroperson.itstimetoact.extras;

import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.Serializable;

public class AfterUpdateEvent extends ActEvent implements Serializable {

    private static final long serialVersionUID = 0L;

    private final int savedVersion;
    private transient int currentVersion;

    public AfterUpdateEvent(Context context, String eventKey) {
        this(getApplicationVersion(context), eventKey);
    }

    private AfterUpdateEvent(int savedVersion, String eventKey) {
        super(eventKey);
        this.savedVersion = savedVersion;
    }

    @Override
    public void onInitialize(Context context) {
        super.onInitialize(context);
        currentVersion = getApplicationVersion(context);
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

    public int getSavedVersion() {
        return savedVersion;
    }

    @Override
    public boolean isHappened() {
        return savedVersion < currentVersion;
    }

    private static int getApplicationVersion(Context context) {
        if(context == null) {
            throw new IllegalArgumentException("");
        }
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // impossible
        }
        return info.versionCode;
    }

}
