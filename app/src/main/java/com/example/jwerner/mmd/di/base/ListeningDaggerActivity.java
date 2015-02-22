package com.example.jwerner.mmd.di.base;

import android.os.Bundle;

import de.greenrobot.event.EventBus;

/**
 * Created by jwerner on 2/17/15.
 */
public abstract class ListeningDaggerActivity extends DaggerActivity {
    @Override protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

    }
}
