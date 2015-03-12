package com.example.jwerner.mmd.components.folders;

import android.content.Context;

import com.example.jwerner.mmd.base.Controller;
import com.example.jwerner.mmd.events.ChangeFolders;

/**
 * Created by jwerner on 3/6/15.
 */
public class FoldersController extends Controller {
    private final Context mContext;
    private final FoldersFragment mFragment;

    public FoldersController(final Context context, FoldersFragment fragment) {
        super(context);
        mContext = context;
        mFragment = fragment;
    }

    public void onEvent(ChangeFolders event) {
        mFragment.refresh();
    }
}
