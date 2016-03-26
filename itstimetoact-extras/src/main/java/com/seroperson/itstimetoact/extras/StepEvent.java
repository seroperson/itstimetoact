package com.seroperson.itstimetoact.extras;

import com.seroperson.itstimetoact.event.ActEvent;

import java.io.Serializable;

/**
 * Event that happens after some number of 'steps'. The count of steps sets via constructor.
 * To make a step just call {@link StepEvent#step()}.
 */
public class StepEvent extends ActEvent implements Serializable {

    private static final long serialVersionUID = 0L;

    private final int stepCount;
    private int remainingStepCount;

    /**
     * Creates object with given count of steps.
     *
     * @param stepCount you must call {@link StepEvent#step()} this count times to make this event happened.
     *                  Must be {@code >= 0}.
     * @param eventKey  event key. Must be not empty and not null.
     */
    public StepEvent(int stepCount, String eventKey) {
        super(eventKey);
        if(stepCount < 0) {
            throw new IllegalArgumentException("stepCount < 0");
        }
        this.stepCount = stepCount;
        this.remainingStepCount = stepCount;
    }

    /** Defines total count of steps to make this event happened. */
    public int getStepCount() {
        return stepCount;
    }

    /**
     * Returns count of remaining steps to make this event happened.
     * It always {@code >= 0} and if it {@code == 0} then {@link StepEvent#isHappened()} returns {@code true}.
     */
    public int getRemainingStepCount() {
        return remainingStepCount;
    }

    /**
     * Makes one step.
     *
     * @return the result of calling {@link StepEvent#getRemainingStepCount()}
     */
    public int step() {
        return remainingStepCount > 0 ? --remainingStepCount : remainingStepCount;
    }

    @Override
    public boolean isHappened() {
        return getRemainingStepCount() == 0;
    }

}
