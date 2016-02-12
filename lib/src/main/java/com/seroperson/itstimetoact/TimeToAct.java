package com.seroperson.itstimetoact;

import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class TimeToAct {

    private final Context context;
    private final Map<String, ActEvent> eventMap = new HashMap<String, ActEvent>();

    public TimeToAct(Context context) {
        if(context == null) {
            throw new IllegalArgumentException("");
        }
        this.context = context;
    }

    public void loadStoredEventData() {
        eventMap.clear();

        Set<ActEvent> loadedSet = loadStoredEventData(context);
        for(ActEvent event : loadedSet) {
            eventMap.put(event.getEventKey(), event);
        }
    }

    public void storeEventData() {
        storeEventData(eventMap.values(), context);
    }

    protected abstract Set<ActEvent> loadStoredEventData(Context context);

    protected abstract void storeEventData(Collection<ActEvent> eventSet, Context context);

}
