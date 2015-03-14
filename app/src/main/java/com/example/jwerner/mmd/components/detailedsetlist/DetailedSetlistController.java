package com.example.jwerner.mmd.components.detailedsetlist;

import android.content.Context;

import com.example.jwerner.mmd.base.Controller;
import com.example.jwerner.mmd.di.helper.Bus;
import com.example.jwerner.mmd.events.ChangeContent;

/**
 * Created by jwerner on 3/6/15.
 */
public class DetailedSetlistController extends Controller {
    private final DetailedSetlistFragment mFragment;

    public DetailedSetlistController(final Context context, final DetailedSetlistFragment fragment) {
        super(context);
        mFragment = fragment;
    }

    @Bus public void onEventMainThread(final ChangeContent event) {
        mFragment.refresh();

    }
}
