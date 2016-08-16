package com.seroperson.itstimetoact.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.seroperson.itstimetoact.TimeToAct;
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
public class GsonTest {

    // TODO a lot of code duplication (see SerializableTest)

    public static final String KEY = "key";

    private TimeToAct timeToAct;

    @Before
    public void setup() {
        timeToAct = new GsonTimeToAct(RuntimeEnvironment.application) {

            @Override
            protected GsonBuilder createGsonBuilder(GsonBuilder gsonBuilder) {
                return super.createGsonBuilder(gsonBuilder)
                            .registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(ActEvent.class)
                                                                                 .registerSubtype(TestEvent.class)
                                                                                 .registerSubtype(SecondTestEvent.class));
            }
        };
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

    private static class TestEvent extends ActEvent {

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

    private static class SecondTestEvent extends TestEvent {

        public SecondTestEvent(String eventKey) {
            super(eventKey);
        }

        @Override
        public boolean isHappened() {
            return false;
        }

    }

}
