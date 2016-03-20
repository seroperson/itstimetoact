package com.seroperson.itstimetoact.extras;

import com.seroperson.itstimetoact.event.ActEvent;

import java.io.Serializable;

public class StepEvent extends ActEvent implements Serializable {

    private static final long serialVersionUID = 0L;

    private final int stepCount;
    private int remainingStepCount;

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

    public int step() {
        return remainingStepCount > 0 ? --remainingStepCount : remainingStepCount;
    }

    @Override
    public boolean isHappened() {
        return getRemainingStepCount() <= 0;
    }

}
