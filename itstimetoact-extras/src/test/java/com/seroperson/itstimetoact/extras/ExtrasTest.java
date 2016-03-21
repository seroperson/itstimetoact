package com.seroperson.itstimetoact.extras;

import android.os.Build;

import com.seroperson.itstimetoact.TimeToAct;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.seroperson.itstimetoact.BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ExtrasTest {

    public static final String KEY = "key";

    private TimeToAct timeToAct;

    @Before
    public void setup() {
        timeToAct = new TimeToAct(RuntimeEnvironment.application);
    }

    @Test
    public void testStepEvent() {
        int n = 5;
        StepEvent event = timeToAct.watchEvent(new StepEvent(n, KEY));
        while (event.getRemainingStepCount() > 0) {
            assertTrue(--n == event.step());
        }
        assertTrue(event.isHappened());
    }

    @Test
    public void testTimeEvent() throws InterruptedException {
        long time = 1000L, half = time / 2;
        TimeEvent event = timeToAct.watchEvent(new TimeEvent(time, KEY));
        Thread.sleep(half);
        assertFalse(event.isHappened());
        Thread.sleep(half);
        assertTrue(event.isHappened());
    }

}