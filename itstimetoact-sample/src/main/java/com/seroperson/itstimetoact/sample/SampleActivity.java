package com.seroperson.itstimetoact.sample;

import com.seroperson.itstimetoact.TimeToAct;
import com.seroperson.itstimetoact.extras.AfterUpdateEvent;
import com.seroperson.itstimetoact.extras.OneShotEvent;
import com.seroperson.itstimetoact.extras.StepEvent;
import com.seroperson.itstimetoact.extras.TimeEvent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class SampleActivity extends AppCompatActivity {

    private TimeToAct timeToAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        timeToAct = new SampleGsonTimeToAct(this);
        timeToAct.loadEventData();

        String afterInstallKey = getString(R.string.event_key_after_install);
        String afterUpdateKey = getString(R.string.event_key_after_update);
        String delayKey = getString(R.string.event_key_delay);
        String clickKey = getString(R.string.event_key_click);

        StepEvent showWelcomeToast = timeToAct.watchEvent(new OneShotEvent(afterInstallKey));
        if(!showWelcomeToast.isHappened()) {
            showWelcomeToast.step();
            makeText(this, R.string.after_install_toast, LENGTH_LONG).show();
        }

        TimeEvent timeEvent = timeToAct.watchEvent(new TimeEvent(TimeUnit./*DAYS*/MINUTES.toMillis(1), delayKey));
        if(timeEvent.isHappened()) {
            makeText(this, R.string.welcome_back, LENGTH_LONG).show();
            timeToAct.watchLastDropped(delayKey);
        }

        AfterUpdateEvent afterUpdate = timeToAct.watchEvent(new AfterUpdateEvent(this, afterUpdateKey));
        if(afterUpdate.isHappened()) {
            int from = afterUpdate.getSavedVersion(), to = afterUpdate.getCurrentVersion();
            makeText(this, getString(R.string.after_update_toast, from, to), LENGTH_LONG).show();
            timeToAct.watchLastDropped(afterUpdateKey);
        }

        int count = getResources().getInteger(R.integer.click_count);
        final StepEvent threeStepsEvent = timeToAct.watchEvent(new StepEvent(count, clickKey));
        final int savedTotal = threeStepsEvent.getStepCount();
        final Button threeStepsButton = (Button) findViewById(R.id.button_step);
        final String clickEventHappenedText = getString(R.string.click_happened);
        if(threeStepsEvent.isHappened()) {
            threeStepsButton.setText(clickEventHappenedText);
        }
        else {
            threeStepsButton.setText(clickedNTimes(savedTotal - threeStepsEvent.getRemainingStepCount()));
            threeStepsButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int remaining = threeStepsEvent.step();
                    threeStepsButton.setText(remaining == 0 ? clickEventHappenedText : clickedNTimes(savedTotal - remaining));
                }
            });
        }
    }

    private String clickedNTimes(int n) {
        return getString(R.string.clicked_n_times, n);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeToAct.storeEventData();
    }

}