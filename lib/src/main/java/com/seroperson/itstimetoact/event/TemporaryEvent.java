package com.seroperson.itstimetoact.event;

public abstract class TemporaryEvent extends ActEvent {

    public TemporaryEvent(String eventKey) {
        super(eventKey);
    }

    protected abstract long getRemainingCount();

    @Override
    public boolean isHappened() {
        return getRemainingCount() <= 0L;
    }

}
