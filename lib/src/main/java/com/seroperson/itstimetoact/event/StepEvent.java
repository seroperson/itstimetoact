package com.seroperson.itstimetoact.event;

public class StepEvent extends TemporaryEvent {

    public StepEvent(String eventKey) {
        super(eventKey);
    }

    @Override
    protected long getRemainingCount() {
        return getRemainingStepCount();
    }

    public int getStepCount() {
        return 0;
    }

    public int getRemainingStepCount() {
        return 0;
    }

}
