package com.seroperson.itstimetoact.event;

import android.util.SparseLongArray;

public abstract class ActEvent {

    private final String eventKey;

    public ActEvent(String eventKey) {
        if(eventKey == null) {
            throw new IllegalArgumentException("");
        }
        this.eventKey = eventKey;
    }

    protected void receiveData(SparseLongArray data) {

    }

    public abstract boolean isHappened();

    public String getEventKey() {
        return eventKey;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        ActEvent actEvent = (ActEvent) o;
        return eventKey.equals(actEvent.eventKey);
    }

    @Override
    public int hashCode() {
        return eventKey.hashCode();
    }

}