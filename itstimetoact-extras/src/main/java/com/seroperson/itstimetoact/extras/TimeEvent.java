package com.seroperson.itstimetoact.extras;

import com.seroperson.itstimetoact.event.ActEvent;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.io.Serializable;

import static java.lang.System.currentTimeMillis;

/** Event that happens after some time */
public class TimeEvent extends ActEvent implements Serializable {

    private static final long serialVersionUID = 0L;

    private final long targetDate;

    /**
     * Creates object that happens after given time
     *
     * @param happenAfter time in mls. Must be {@code >= 0}
     * @param eventKey    event key. Must be not null.
     */
    public TimeEvent(@IntRange(from = 0) long happenAfter, @NonNull String eventKey) {
        super(eventKey);
        this.targetDate = currentTimeMillis() + happenAfter;
    }

    /** Returns date in mls when event will be marked as happened. */
    public long getTargetDate() {
        return targetDate;
    }

    /** Returns remaining time in mls to mark this event as happened. */
    public long getRemainingTime() {
        return targetDate - currentTimeMillis(); // TODO switch to nanos everywhere?
    }

    @Override
    public boolean isHappened() {
        return getRemainingTime() <= 0;
    }

}