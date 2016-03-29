package com.seroperson.itstimetoact.sample;

import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.seroperson.itstimetoact.event.ActEvent;
import com.seroperson.itstimetoact.extras.AfterUpdateEvent;
import com.seroperson.itstimetoact.extras.OneShotEvent;
import com.seroperson.itstimetoact.extras.StepEvent;
import com.seroperson.itstimetoact.extras.TimeEvent;
import com.seroperson.itstimetoact.gson.GsonTimeToAct;

import android.content.Context;

public class SampleGsonTimeToAct extends GsonTimeToAct {

    public SampleGsonTimeToAct(Context context) {
        super(context);
    }

    @Override
    protected GsonBuilder createGsonBuilder(GsonBuilder gsonBuilder) {
        return super.createGsonBuilder(gsonBuilder)
                    .registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(ActEvent.class)
                                                                         .registerSubtype(AfterUpdateEvent.class)
                                                                         .registerSubtype(OneShotEvent.class)
                                                                         .registerSubtype(StepEvent.class)
                                                                         .registerSubtype(TimeEvent.class));
    }

}
