package com.seroperson.itstimetoact;

import com.android.internal.util.Predicate;
import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;

import java.util.*;

public abstract class TimeToAct {

    private final Context context;
    private final Map<String, ActEvent> eventMap = new HashMap<String, ActEvent>();

    public TimeToAct(Context context) {
        if(context == null) {
            throw new IllegalArgumentException("");
        }
        this.context = context;
    }

    public final void loadEventData() {
        clear();

        Set<ActEvent> loadedSet = loadEventData(context);
        for(ActEvent event : loadedSet) {
            putEvent(event);
        }
    }

    public final boolean storeEventData() {
        return storeEventData(eventMap.values(), context);
    }

    public final boolean watchEvent(ActEvent event) {
        return watchEvent(event, isNeedToAutoSave());
    }

    public boolean watchEvent(ActEvent event, boolean autoSave) {
        return putEvent(event) && storeIfTrueWithResult(autoSave);
    }

    public final boolean removeEvent(Predicate<ActEvent> eventPredicate) {
        return removeEvent(eventPredicate, isNeedToAutoSave());
    }

    public boolean removeEvent(Predicate<ActEvent> eventPredicate, boolean autoSave) {
        boolean result = true;
        for(ActEvent event : eventMap.values()) {
            if(eventPredicate.apply(event)) {
                result = result && removeEvent(event, autoSave);
            }
        }
        return result;
    }

    public final boolean removeEvent(String eventKey) {
        return removeEvent(eventKey, isNeedToAutoSave());
    }

    public boolean removeEvent(String eventKey, boolean autoSave) {
        return removeEvent(getEvent(eventKey), autoSave);
    }

    public final boolean removeEvent(ActEvent event) {
        return removeEvent(event, isNeedToAutoSave());
    }

    public boolean removeEvent(ActEvent event, boolean autoSave) {
        eventMap.remove(event.getEventKey());
        return storeIfTrueWithResult(autoSave);
    }

    public final boolean clear() {
        return clear(true);
    }

    public boolean clear(boolean autoSave) {
        eventMap.clear();
        return storeIfTrueWithResult(autoSave);
    }

    public final boolean isWatchingFor(String eventKey) {
        return eventMap.containsKey(eventKey);
    }

    public final boolean isHappened(String eventKey) {
        return getEvent(eventKey).isHappened();
    }

    public final ActEvent getEvent(String eventKey) {
        return eventMap.get(eventKey);
    }

    public final Set<ActEvent> getEventSet(Predicate<ActEvent> eventPredicate) {
        Set<ActEvent> result = new HashSet<ActEvent>();
        for(ActEvent event : eventMap.values()) {
            if(eventPredicate.apply(event)) {
                result.add(event);
            }
        }
        return result;
    }

    private boolean storeIfTrueWithResult(boolean value) {
        boolean result = true;
        if(value) {
            result = storeEventData();
        }
        return result;
    }

    private boolean putEvent(ActEvent event) {
        String key = event.getEventKey();
        if(!isWatchingFor(key)) {
            eventMap.put(key, event);
            return true;
        }
        return false;
    }

    protected Map<String, ActEvent> getEventMap() {
        return eventMap;
    }

    protected boolean isNeedToAutoSave() {
        return true;
    }

    protected Set<ActEvent> loadEventData(Context context) {
        throw new UnsupportedOperationException();
    }

    protected boolean storeEventData(Collection<ActEvent> eventSet, Context context) {
        throw new UnsupportedOperationException();
    }

}
