package com.seroperson.itstimetoact.extras;

import android.support.annotation.NonNull;

import java.io.Serializable;

/** Event that happens in one step */
public class OneShotEvent extends StepEvent implements Serializable {

    private static final long serialVersionUID = 0L;

    public OneShotEvent(@NonNull String eventKey) {
        super(1, eventKey);
    }

}