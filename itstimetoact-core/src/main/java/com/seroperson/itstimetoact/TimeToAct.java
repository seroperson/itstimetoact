package com.seroperson.itstimetoact;

import com.android.internal.util.Predicate;
import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;

import java.util.*;

import static com.seroperson.itstimetoact.Check.*;

public class TimeToAct {

    private final Context context;
    private final Map<String, ActEvent> eventMap = new HashMap<String, ActEvent>();
    private ActEvent lastDropped;

    public TimeToAct(Context context) {
        checkArgumentNotNull(context, "context == null");
        this.context = context;
    }

    public final void loadEventData() {
        clear(false);

        Set<ActEvent> loadedSet = loadEventData(context);
        if(checkIsNull(loadedSet)) {
            throw new IllegalStateException("TimeToAct#loadEventData(Context) returned null");
        }
        for(ActEvent event : loadedSet) {
            putEvent(event, false);
        }
    }

    public final boolean storeEventData() {
        return storeEventData(new HashSet<ActEvent>(eventMap.values()), context);
    }

    public final <T extends ActEvent> T watchEvent(T event) {
        return watchEvent(event, isNeedToAutoSave());
    }

    public <T extends ActEvent> T watchEvent(T event, boolean autoSave) {
        return putEvent(event, autoSave);
    }

    public <T extends ActEvent> T forceWatchEvent(T event) {
        return forceWatchEvent(event, isNeedToAutoSave());
    }

    public <T extends ActEvent> T forceWatchEvent(T event, boolean autoSave) {
        return putEvent(event, autoSave, true);
    }

    public boolean watchLastDropped(String key) {
        return watchLastDropped(key, isNeedToAutoSave());
    }

    public boolean watchLastDropped(String key, boolean autoSave) {
        if(lastDropped != null && lastDropped.getEventKey().equals(key)) {
            forceWatchEvent(lastDropped, autoSave);
            lastDropped = null;
            return true;
        }
        return false;
    }

    public final boolean removeEvent(Predicate<ActEvent> eventPredicate) {
        return removeEvent(eventPredicate, isNeedToAutoSave());
    }

    public boolean removeEvent(Predicate<ActEvent> eventPredicate, boolean autoSave) {
        checkArgumentNotNull(eventPredicate, "eventPredicate == null");
        boolean result = true;
        for(ActEvent event : eventMap.values()) {
            if(eventPredicate.apply(event)) {
                result = result && removeEvent(event, autoSave);
            }
        }
        return result;
    }

    public final boolean removeEvent(ActEvent event) {
        return removeEvent(event, isNeedToAutoSave());
    }

    public boolean removeEvent(ActEvent event, boolean autoSave) {
        checkArgumentNotNull(event, "event == null");
        return removeEvent(event.getEventKey(), autoSave);
    }

    public final boolean removeEvent(String eventKey) {
        return removeEvent(eventKey, isNeedToAutoSave());
    }

    public boolean removeEvent(String eventKey, boolean autoSave) {
        if(!isWatchingFor(eventKey)) {
            throw new IllegalArgumentException("There is no event with such key: "+eventKey);
        }
        eventMap.remove(eventKey);
        return storeIfTrueWithResult(autoSave);
    }

    public final boolean clear() {
        return clear(isNeedToAutoSave());
    }

    public boolean clear(boolean autoSave) {
        eventMap.clear();
        return storeIfTrueWithResult(autoSave);
    }

    public final boolean isWatchingFor(String eventKey) {
        checkArgumentNotEmpty(eventKey, "eventKey is empty or null");
        return eventMap.containsKey(eventKey);
    }

    public final boolean isHappened(String eventKey) {
        return getEvent(eventKey).isHappened();
    }

    public final <T extends ActEvent> T getEvent(String eventKey) {
        if(!isWatchingFor(eventKey)) {
            throw new IllegalArgumentException("There is no event with such key: "+eventKey);
        }
        return (T) eventMap.get(eventKey);
    }

    public final Set<ActEvent> getEventSet(Predicate<ActEvent> eventPredicate) {
        checkArgumentNotNull(eventPredicate, "eventPredicate == null");
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

    private <T extends ActEvent> T putEvent(T event, boolean autoSave) {
        return putEvent(event, autoSave, false);
    }

    private <T extends ActEvent> T putEvent(T event, boolean autoSave, boolean overwrite) {
        checkArgumentNotNull(event, "event == null");
        String key = event.getEventKey();
        if(overwrite || !isWatchingFor(key)) {
            event.onInitialize(context);
            eventMap.put(key, event);
            storeIfTrueWithResult(autoSave); // TODO unhandled
        }
        else {
            lastDropped = event;
        }
        return getEvent(key);
    }

    protected Map<String, ActEvent> getEventMap() {
        return eventMap;
    }

    protected boolean isNeedToAutoSave() {
        return false;
    }

    protected Set<ActEvent> loadEventData(Context context) {
        throw new UnsupportedOperationException();
    }

    protected boolean storeEventData(Collection<ActEvent> eventSet, Context context) {
        throw new UnsupportedOperationException();
    }

}
