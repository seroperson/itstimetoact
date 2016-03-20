package com.seroperson.itstimetoact;

import android.content.Context;
import android.os.Build;

import com.seroperson.itstimetoact.event.ActEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collection;
import java.util.Set;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class CoreTest {

    private TimeToAct timeToAct;

    @Before
    public void setup() {
        timeToAct = new TimeToAct(RuntimeEnvironment.application) {
            @Override
            protected Set<ActEvent> loadEventData(Context context) {
                return super.loadEventData(context);
            }

            @Override
            protected boolean storeEventData(Collection<ActEvent> eventSet, Context context) {
                return super.storeEventData(eventSet, context);
            }
        };
    }

    @Test
    public void test() {
    }

}