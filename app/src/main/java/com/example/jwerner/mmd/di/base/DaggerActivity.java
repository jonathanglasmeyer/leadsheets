package com.example.jwerner.mmd.di.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.jwerner.mmd.di.App;

/**
 * Created by jwerner on 2/17/15.
 */
public abstract class DaggerActivity extends ActionBarActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Perform injection so that when this call returns all dependencies will be available for use.
        ((App) getApplication()).inject(this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }
}
