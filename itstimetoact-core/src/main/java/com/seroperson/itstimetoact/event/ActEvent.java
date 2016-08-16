package com.seroperson.itstimetoact.event;

import com.seroperson.itstimetoact.TimeToAct;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Base class for all events.
 * This class is abstract so child classes need to implement {@link ActEvent#isHappened()}.
 * They also can override {@link ActEvent#onInitialize(Context)} to access the context.
 * There is {@link ActEvent#equals(Object)} and {@link ActEvent#hashCode()} are marked as
 * final and uses only event's key - event's main characteristic.
 */
public abstract class ActEvent implements Serializable {

    private final String eventKey;

    /**
     * Creates instance with given key.
     *
     * @param eventKey event key. Must be not null.
     */
    public ActEvent(@NonNull String eventKey) {
        this.eventKey = eventKey;
    }

    /**
     * Will be called when event being watched by {@link TimeToAct}.
     *
     * @param context context from {@link TimeToAct}
     */
    public void onInitialize(@NonNull Context context) {

    }

    /**
     * Checks if event already happened.
     *
     * @return {@code true} if event happened and {@code false} otherwise.
     */
    public abstract boolean isHappened();

    /**
     * Provides associated with this event key.
     *
     * @return key that given in constructor.
     */
    @NonNull
    public final String getEventKey() {
        return eventKey;
    }

    @Override
    public final boolean equals(Object o) {
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
    public final int hashCode() {
        return eventKey.hashCode();
    }

}