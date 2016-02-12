package com.seroperson.itstimetoact.event;

import android.util.SparseLongArray;

public abstract class ActEvent {

    private final String eventKey;

    public ActEvent(String eventKey) {
        this.eventKey = eventKey;
    }

    protected void receiveData(SparseLongArray data) {

    }

    public abstract boolean isHappened();

    public String getEventKey() {
        return eventKey;
    }

}