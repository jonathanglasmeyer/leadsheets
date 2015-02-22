package com.example.jwerner.mmd.base;

import android.content.Context;

import com.example.jwerner.mmd.di.App;

import de.greenrobot.event.EventBus;

/**
 * Created by jwerner on 2/20/15.
 */
public abstract class Controller {
    protected Controller(Context context) {
        EventBus.getDefault().register(this);
        App.get(context).inject(this);
    }
}
