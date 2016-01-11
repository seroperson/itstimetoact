package com.seroperson.itstimetoact.event;

public abstract class ActEvent {

    private final String eventKey;

    public ActEvent(String eventKey) {
        this.eventKey = eventKey;
    }

    public abstract boolean isHappened();

    public String getEventKey() {
        return eventKey;
    }

}
