package com.seroperson.itstimetoact.extras;

import com.seroperson.itstimetoact.event.ActEvent;

import java.io.Serializable;

public class TimeEvent extends ActEvent implements Serializable {

    private static final long serialVersionUID = 0L;

    private final long targetDate;

    public TimeEvent(long targetDate, String eventKey) {
        super(eventKey);
        this.targetDate = targetDate;
    }

    public long getTargetDate() {
        return targetDate;
    }

    public long getRemainingTime() {
        return targetDate - System.currentTimeMillis(); // TODO switch to nanos?
    }

    @Override
    public boolean isHappened() {
        return getRemainingTime() <= 0;
    }

}
