package com.seroperson.itstimetoact.event;

public class TimeEvent extends TemporaryEvent {

    public TimeEvent(String eventKey) {
        super(eventKey);
    }

    @Override
    protected long getRemainingCount() {
        return getRemainingTime();
    }

    public long getTargetDate() {
        return 0L;
    }

    public long getRemainingTime() {
        return 0L;
    }

}
