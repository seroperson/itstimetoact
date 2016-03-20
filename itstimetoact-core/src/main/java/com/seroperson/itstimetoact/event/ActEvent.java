package com.seroperson.itstimetoact.event;

import android.content.Context;

import java.io.Serializable;

public abstract class ActEvent implements Serializable {

    private final String eventKey;

    public ActEvent(String eventKey) {
        if(eventKey == null) {
            throw new IllegalArgumentException("");
        }
        this.eventKey = eventKey;
    }

    public void onInitialize(Context context) {

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