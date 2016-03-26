package com.seroperson.itstimetoact.extras;

import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.Serializable;

import static com.seroperson.itstimetoact.Check.checkArgumentNotNull;

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
     * @param eventKey event key. Must be not empty and not null.
     */
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
        checkArgumentNotNull(context, "context == null");
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch(PackageManager.NameNotFoundException e) {
            // impossible
        }
        return info.versionCode;
    }

}
