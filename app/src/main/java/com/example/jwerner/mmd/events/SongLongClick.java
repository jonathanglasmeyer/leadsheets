package com.example.jwerner.mmd.events;

import android.view.View;

/**
 * Created by jwerner on 3/8/15.
 */
public class SongLongClick {
    public final int position;
    public final View v;

    public SongLongClick(final int position, View v) {
        this.position = position;
        this.v = v;
    }
}
