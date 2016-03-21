package com.seroperson.itstimetoact.serializable;

import com.seroperson.itstimetoact.TimeToAct;
import com.seroperson.itstimetoact.event.ActEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.os.Build;

import java.io.Serializable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.seroperson.itstimetoact.BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class SerializableTest {

    // TODO a lot of code duplication (see GsonTest)

    public static final String KEY = "key";

    private TimeToAct timeToAct;

    @Before
    public void setup() {
        timeToAct = new SerializableTimeToAct(RuntimeEnvironment.application);
    }

    @Test
    public void testSaving() {
        timeToAct.watchEvent(new TestEvent(KEY));
        timeToAct.storeEventData();
        timeToAct.clear();
        assertFalse(timeToAct.isWatchingFor(KEY));
        timeToAct.loadEventData();
        assertTrue(timeToAct.isWatchingFor(KEY));
        assertTrue(timeToAct.getEvent(KEY) instanceof TestEvent);
    }

    @Test
    public void testTwoTypeSaving() {
        String secondKey = "key-2";
        timeToAct.watchEvent(new TestEvent(KEY)).setSomeValue(1);
        timeToAct.watchEvent(new SecondTestEvent(secondKey)).setSomeValue(2);
        timeToAct.storeEventData();
        timeToAct.clear();
        assertFalse(timeToAct.isWatchingFor(KEY));
        assertFalse(timeToAct.isWatchingFor(secondKey));
        timeToAct.loadEventData();

        assertTrue(timeToAct.isWatchingFor(KEY));
        TestEvent restoredFirst = timeToAct.getEvent(KEY);
        assertEquals(restoredFirst.getSomeValue(), 1);

        assertTrue(timeToAct.isWatchingFor(secondKey));
        SecondTestEvent restoredSecond = timeToAct.getEvent(secondKey);
        assertEquals(restoredSecond.getSomeValue(), 2);
    }

    private static class TestEvent extends ActEvent implements Serializable {

        private int someValue = 0;

        public TestEvent(String eventKey) {
            super(eventKey);
        }

        public int getSomeValue() {
            return someValue;
        }

        public void setSomeValue(int value) {
            this.someValue = value;
        }

        @Override
        public boolean isHappened() {
            return false;
        }

    }

    private static class SecondTestEvent extends TestEvent implements Serializable {

        public SecondTestEvent(String eventKey) {
            super(eventKey);
        }

        @Override
        public boolean isHappened() {
            return false;
        }

    }

}
