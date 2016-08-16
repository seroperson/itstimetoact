package com.seroperson.itstimetoact.extras;

import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.io.Serializable;

/** Event that happens after application updated. */
public class AfterUpdateEvent extends ActEvent implements Serializable {

    private static final long serialVersionUID = 0L;

    private final int savedVersion;
    private transient int currentVersion;

    /**
     * Creates object and sets initial state via context.
     *
     * @param context  context to retrieve current application version.
     *                 Object don't holds an link on it so there is no any memory leaks.
     *                 Must be not null.
     * @param eventKey event key. Must be not null.
     */
    public AfterUpdateEvent(@NonNull Context context, @NonNull String eventKey) {
        this(getApplicationVersion(context), eventKey);
    }

    private AfterUpdateEvent(int savedVersion, @NonNull String eventKey) {
        super(eventKey);
        this.savedVersion = savedVersion;
    }

    @Override
    public void onInitialize(@NonNull Context context) {
        super.onInitialize(context);
        currentVersion = getApplicationVersion(context);
    }

    /**
     * Returns current application version code
     * It's equals {@link AfterUpdateEvent#getSavedVersion()} while {@link AfterUpdateEvent#isHappened()}
     * returns false.
     */
    public int getCurrentVersion() {
        return currentVersion;
    }

    /**
     * Returns previous application version code.
     * It's equals {@link AfterUpdateEvent#getCurrentVersion()} while {@link AfterUpdateEvent#isHappened()}
     * returns false.
     */
    public int getSavedVersion() {
        return savedVersion;
    }

    @Override
    public boolean isHappened() {
        return savedVersion < currentVersion;
    }

    private static int getApplicationVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch(PackageManager.NameNotFoundException e) {
            return 1; // impossible
        }
        return info.versionCode;
    }

}