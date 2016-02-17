package com.seroperson.itstimetoact;

import com.android.internal.util.Predicate;
import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;

import java.util.*;

public abstract class TimeToAct {

    private final Context context;
    private final boolean autoSave;
    private final Map<String, ActEvent> eventMap = new HashMap<String, ActEvent>();

    public TimeToAct(Context context) {
        this(context, true);
    }

    public TimeToAct(Context context, boolean autoSave) {
        if(context == null) {
            throw new IllegalArgumentException("");
        }
        this.context = context;
        this.autoSave = autoSave;
    }

    public void loadEventData() {
        clear();

        Set<ActEvent> loadedSet = loadEventData(context);
        for(ActEvent event : loadedSet) {
            putEvent(event);
        }
    }

    public boolean storeEventData() {
        return storeEventData(eventMap.values(), context);
    }

    public boolean watchEvent(ActEvent event) {
        return watchEvent(event, autoSave);
    }

    public boolean watchEvent(ActEvent event, boolean autoSave) {
        putEvent(event);
        return storeIfTrueWithResult(autoSave);
    }

    public boolean removeEvent(Predicate<ActEvent> eventPredicate) {
        return removeEvent(eventPredicate, autoSave);
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

    public boolean removeEvent(String eventKey) {
        return removeEvent(eventKey, autoSave);
    }

    public boolean removeEvent(String eventKey, boolean autoSave) {
        return removeEvent(getEvent(eventKey), autoSave);
    }

    public boolean removeEvent(ActEvent event) {
        return removeEvent(event, autoSave);
    }

    public boolean removeEvent(ActEvent event, boolean autoSave) {
        eventMap.remove(event.getEventKey());
        return storeIfTrueWithResult(autoSave);
    }

    public boolean clear() {
        return clear(true);
    }

    public boolean clear(boolean autoSave) {
        eventMap.clear();
        return storeIfTrueWithResult(autoSave);
    }

    public boolean isWatchingFor(String eventKey) {
        return eventMap.containsKey(eventKey);
    }

    public boolean isHappened(String eventKey) {
        return getEvent(eventKey).isHappened();
    }

    public ActEvent getEvent(String eventKey) {
        return eventMap.get(eventKey);
    }

    public Set<ActEvent> getEventSet(Predicate<ActEvent> eventPredicate) {
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

    private void putEvent(ActEvent event) {
        eventMap.put(event.getEventKey(), event);
    }

    protected abstract Set<ActEvent> loadEventData(Context context);

    protected abstract boolean storeEventData(Collection<ActEvent> eventSet, Context context);

}
