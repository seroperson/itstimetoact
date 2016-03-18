package com.seroperson.itstimetoact.extras;

import com.seroperson.itstimetoact.event.ActEvent;

public class StepEvent extends ActEvent {

    private int remainingStepCount;
    private int stepCount;

    public StepEvent(int stepCount, String eventKey) {
        super(eventKey);
        this.stepCount = stepCount;
        this.remainingStepCount = stepCount;
    }

    public int getStepCount() {
        return stepCount;
    }

    public int getRemainingStepCount() {
        return remainingStepCount;
    }

    @Override
    public boolean isHappened() {
        return getRemainingStepCount() <= 0;
    }

}
