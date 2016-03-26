package com.seroperson.itstimetoact;

import com.android.internal.util.Predicate;
import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;

import java.util.*;

import static com.seroperson.itstimetoact.Check.*;

/**
 * The base class that manages, performs serialization and deserialization, provides access and resolves
 * some storing conflicts of objects.
 * You can implement your serialization/deserialization logic by overriding {@link TimeToAct#loadEventData(Context)}
 * and {@link TimeToAct#storeEventData(Collection, Context)}.
 * All operations aren't async, just keep in mind.
 */
public class TimeToAct {

    private final Context context;
    private final Map<String, ActEvent> eventMap = new HashMap<String, ActEvent>();
    private ActEvent lastDropped;

    /**
     * Creates class instance that depends on given context.
     * It will be great if you call {@link TimeToAct#loadEventData()} immediately after object creation to load all
     * previously saved events.
     *
     * @param context any context that will be used to perform serialization/deserialization operations.
     *                Object holds link on this context, so be aware about leaks.
     *                Must be not null.
     */
    public TimeToAct(Context context) {
        checkArgumentNotNull(context, "context == null");
        this.context = context;
    }

    /**
     * Loads previously saved via {@link TimeToAct#storeEventData()} events, so they become
     * available via {@link TimeToAct#getEvent(String)}.
     * This method is not async so be careful with calling it in UI thread or something like that.
     * Needs {@link TimeToAct#loadEventData(Context)} to be overridden.
     */
    public final void loadEventData() {
        clear();

        Set<ActEvent> loadedSet = loadEventData(context);
        if(checkIsNull(loadedSet)) {
            throw new IllegalStateException("TimeToAct#loadEventData(Context) returned null");
        }
        for(ActEvent event : loadedSet) {
            putEvent(event, false);
        }
    }

    /**
     * Saves events so they become available to load via {@link TimeToAct#loadEventData()}
     * with their current state after application was closed (or in any other time when you want).
     * This method is not async so be careful with calling it in UI thread or something like that.
     * Needs {@link TimeToAct#storeEventData(Collection, Context)} to be overridden.
     *
     * @return {@code true} if data successfully saved and {@code false} otherwise
     */
    public final boolean storeEventData() {
        // Wrapping it with HashSet because link Collection, which hashMap.values() returns, isn't serializable
        return storeEventData(new HashSet<ActEvent>(eventMap.values()), context);
    }

    /**
     * Saves given event in memory so you can get it via {@link TimeToAct#getEvent(String)}
     * Changes will be saved when {@link TimeToAct#storeEventData()} will be called.
     * Nothing will happen if there is already event with same key i.e {@link TimeToAct#isWatchingFor(ActEvent)}
     * for this event returns {@code true}. If you need to overwrite previous event use
     * {@link TimeToAct#forceWatchEvent(ActEvent)} or {@link TimeToAct#watchLastDropped(String)}.
     *
     * @param event event to watch for. Must be not null.
     * @param <T>   type of given event. Be careful, because there is unchecked cast and it can throw an exception if
     *              it already watches for event with same key but with other class (so I don't recommend to do such
     *              things).
     *
     * @return the result of calling {@link TimeToAct#getEvent(String)} with key of given event. Respectively it will be
     * given argument or event with same key if it was here before.
     */
    public final <T extends ActEvent> T watchEvent(T event) {
        return putEvent(event, false);
    }

    /**
     * If previous {@link TimeToAct#watchEvent(ActEvent)} was called when event with same key already in, then given
     * event wont saved in memory, but it can be enforced by calling this method with the corresponding key. Same action
     * can be performed with {@link TimeToAct#forceWatchEvent(ActEvent)}.
     * Changes will be saved when {@link TimeToAct#storeEventData()} will be called.
     *
     * @param key the key of event that don't saved in memory after {@link TimeToAct#watchEvent(ActEvent)} call.
     *            Must be not empty and not null.
     */
    public final void watchLastDropped(String key) {
        checkArgumentNotEmpty(key, "key is empty or null");
        if(lastDropped == null) {
            throw new IllegalStateException("There is nothing to watch");
        }
        if(lastDropped.getEventKey().equals(key)) {
            forceWatchEvent(lastDropped);
            lastDropped = null;
        }
        else {
            throw new IllegalStateException("Given key isn't equals to last 'dropped' event key");
        }
    }

    /**
     * Saves given event in memory so you can get it via {@link TimeToAct#getEvent(String)}.
     * Changes will be saved when {@link TimeToAct#storeEventData()} will be called.
     * Overwrites event with same key if it already in i.e if {@link TimeToAct#isWatchingFor(ActEvent)}
     * for this event returns {@code true}. If you don't need to overwrite previous event use
     * {@link TimeToAct#watchEvent(ActEvent)}.
     *
     * @param event event to watch for. Must be not null.
     * @param <T>   type of given event.
     *
     * @return given argument.
     */
    public final <T extends ActEvent> T forceWatchEvent(T event) {
        return putEvent(event, true);
    }

    /**
     * Removes all events that was filtered by given predicate.
     * Changes will be saved when {@link TimeToAct#storeEventData()} will be called.
     *
     * @param eventPredicate predicate to filter events. Must be not null.
     */
    public final void removeEvent(Predicate<ActEvent> eventPredicate) {
        checkArgumentNotNull(eventPredicate, "eventPredicate == null");
        for(ActEvent event : eventMap.values()) {
            if(eventPredicate.apply(event)) {
                removeEvent(event);
            }
        }
    }

    /**
     * Removes event which has key that equals key of given argument.
     * Changes will be saved when {@link TimeToAct#storeEventData()} will be called.
     *
     * @param event event which key will be used to find the event to remove. Throws an exception if
     *              there is no such event i.e {@link TimeToAct#isWatchingFor(ActEvent)} returns {@code false}.
     *              Must be not null.
     */
    public final void removeEvent(ActEvent event) {
        checkArgumentNotNull(event, "event == null");
        removeEvent(event.getEventKey());
    }

    /**
     * Removes event with given key.
     * Changes will be saved when {@link TimeToAct#storeEventData()} will be called.
     *
     * @param eventKey key which will be used to find the event to remove. Throws an exception if there is no
     *                 such event i.e {@link TimeToAct#isWatchingFor(ActEvent)} returns {@code false}.
     *                 Must be not empty and not null.
     */
    public final void removeEvent(String eventKey) {
        if(!isWatchingFor(eventKey)) {
            throw new IllegalArgumentException("There is no event with such key: " + eventKey);
        }
        eventMap.remove(eventKey);
    }

    /**
     * Removes all currently watching events.
     * Changes will be saved when {@link TimeToAct#storeEventData()} will be called.
     */
    public final void clear() {
        eventMap.clear();
    }

    /**
     * Checks for the event that equals given argument.
     *
     * @param event event to check. Must be not null.
     *
     * @return {@code false} if there is no such event and {@code true} otherwise.
     */
    public final boolean isWatchingFor(ActEvent event) {
        checkArgumentNotNull(event, "event == null");
        return isWatchingFor(event.getEventKey());
    }

    /**
     * Checks for the event with given key.
     *
     * @param eventKey key to check. Must be not empty and not null.
     *
     * @return {@code false} if there is no such event and {@code true} otherwise.
     */
    public final boolean isWatchingFor(String eventKey) {
        checkArgumentNotEmpty(eventKey, "eventKey is empty or null");
        return eventMap.containsKey(eventKey);
    }

    /**
     * Checks if event with given key already happened.
     *
     * @param eventKey key to check. Throws an exception if there is no event with such
     *                 key i.e {@link TimeToAct#isWatchingFor(String)} returns {@code false}.
     *                 Must be not empty and not null.
     *
     * @return {@code true} if event with given key already happened and {@code false} otherwise.
     */
    public final boolean isHappened(String eventKey) {
        return getEvent(eventKey).isHappened();
    }

    /**
     * Returns the event with given key.
     *
     * @param eventKey key to search for the event. Throws an exception if there is no event with such
     *                 key i.e {@link TimeToAct#isWatchingFor(String)} returns {@code false}.
     *                 Must be not empty and not null.
     * @param <T>      event with given key will be casted to this type.
     *
     * @return event with given key.
     */
    public final <T extends ActEvent> T getEvent(String eventKey) {
        if(!isWatchingFor(eventKey)) {
            throw new IllegalArgumentException("There is no event with such key: " + eventKey);
        }
        return (T) eventMap.get(eventKey);
    }

    /**
     * Returns all events that was filtered by given predicate.
     *
     * @param eventPredicate predicate to filter events. Must be not null.
     *
     * @return set of events filtered by given predicate.
     */
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

    private <T extends ActEvent> T putEvent(T event, boolean overwrite) {
        checkArgumentNotNull(event, "event == null");
        String key = event.getEventKey();
        if(overwrite || !isWatchingFor(key)) {
            event.onInitialize(context);
            eventMap.put(key, event);
        }
        else {
            lastDropped = event;
        }
        return getEvent(key);
    }

    /**
     * Will be called when there is need for data deserialization.
     *
     * @param context context to perform your operations.
     *
     * @return the set of deserialized events.
     */
    protected Set<ActEvent> loadEventData(Context context) {
        throw new UnsupportedOperationException();
    }

    /**
     * Will be called when there is need for data serialization.
     *
     * @param eventSet the set of events for serialization.
     * @param context  context to perform your operations.
     *
     * @return {@code true} if serialization finished successfully, {@code false} otherwise.
     */
    protected boolean storeEventData(Collection<ActEvent> eventSet, Context context) {
        throw new UnsupportedOperationException();
    }

}
