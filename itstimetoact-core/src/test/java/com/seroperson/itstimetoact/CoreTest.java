package com.seroperson.itstimetoact;

import com.android.internal.util.Predicate;
import com.seroperson.itstimetoact.event.ActEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.os.Build;

import static junit.framework.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class CoreTest {

    public static final String KEY = "key";

    private TimeToAct timeToAct;

    @Before
    public void setup() {
        timeToAct = new TimeToAct(RuntimeEnvironment.application);
    }

    @Test
    public void testCorrectWatching() {
        ActEvent first = timeToAct.watchEvent(new AlreadyHappened(KEY));
        assertTrue(timeToAct.isWatchingFor(KEY));
        assertSame(first, timeToAct.watchEvent(new AlreadyHappened(KEY)));
        assertNotSame(first, timeToAct.forceWatchEvent(new AlreadyHappened(KEY)));
    }

    @Test
    public void testCorrectRemovingByKey() {
        timeToAct.watchEvent(new AlreadyHappened(KEY));
        timeToAct.removeEvent(KEY);
        assertFalse(timeToAct.isWatchingFor(KEY));
    }

    @Test
    public void testCorrectRemovingByEvent() {
        timeToAct.removeEvent(timeToAct.watchEvent(new AlreadyHappened(KEY)));
        assertFalse(timeToAct.isWatchingFor(KEY));
    }

    @Test
    public void testCorrectRemovingByPredicate() {
        timeToAct.watchEvent(new AlreadyHappened(KEY));
        timeToAct.removeEvent(new Predicate<ActEvent>() {
            @Override
            public boolean apply(ActEvent event) {
                return true;
            }
        });
        assertFalse(timeToAct.isWatchingFor(KEY));
    }

    @Test
    public void testCorrectCleanup() {
        int n = 5;
        String[] keys = new String[n];
        for(int index = 0; index < n; index++) {
            keys[index] = KEY.concat(String.valueOf(index));
            timeToAct.watchEvent(new AlreadyHappened(keys[index++]));
        }
        timeToAct.clear();
        for(int index = 0; index < n; index++) {
            assertFalse(timeToAct.isWatchingFor(keys[index++]));
        }
    }

    @Test
    public void testCorrectHappening() {
        timeToAct.watchEvent(new AlreadyHappened(KEY));
        assertTrue(timeToAct.isHappened(KEY));

        timeToAct.clear();

        timeToAct.watchEvent(new NeverHappened(KEY));
        assertFalse(timeToAct.isHappened(KEY));
    }

    private static class AlreadyHappened extends ActEvent {

        public AlreadyHappened(String eventKey) {
            super(eventKey);
        }

        @Override
        public boolean isHappened() {
            return true;
        }

    }

    private static class NeverHappened extends ActEvent {

        public NeverHappened(String eventKey) {
            super(eventKey);
        }

        @Override
        public boolean isHappened() {
            return false;
        }

    }

}