package com.example.jwerner.mmd.base;

import android.content.Context;

import de.greenrobot.event.EventBus;

/**
 * Created by jwerner on 2/20/15.
 */
public abstract class Controller {
    protected Controller(Context context) {
//        App.get(context).component().inject(this);
    }

    public void unregister() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().registerSticky(this);
        }
    }
}
