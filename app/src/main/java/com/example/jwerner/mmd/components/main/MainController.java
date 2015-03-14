package com.example.jwerner.mmd.components.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.transition.Fade;

import com.example.jwerner.mmd.DetailListActivity;
import com.example.jwerner.mmd.R;
import com.example.jwerner.mmd.base.Controller;
import com.example.jwerner.mmd.components.setlist.SetlistFragment;
import com.example.jwerner.mmd.di.helper.Bus;
import com.example.jwerner.mmd.events.ChangeToolbarTitle;
import com.example.jwerner.mmd.events.FolderClick;
import com.example.jwerner.mmd.events.SetlistItemClick;
import com.example.jwerner.mmd.stores.UIState;

import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;

public class MainController extends Controller {
    private final Activity mContext;

    @DebugLog
    public MainController(Activity context) {
        super(context);
        mContext = context;
    }

    @Bus public void onEvent(SetlistItemClick event) {
        final Intent intent = new Intent(mContext, DetailListActivity.class);
        intent.putExtra("position", event.position);
//        mContext.getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        mContext.getWindow().setEnterTransition(new Explode());
//        mContext.getWindow().setExitTransition(new Explode());
//        mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mContext).toBundle());
        mContext.startActivity(intent);

    }

    @Bus public void onEvent(FolderClick event) {
        final SetlistFragment fragment = SetlistFragment.newInstance(event.folderName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setEnterTransition(new Fade());
        }
        mContext.getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(UIState.SETLIST)
                .commit();
        new Handler().postDelayed(() ->
                EventBus.getDefault().post(new ChangeToolbarTitle(event.folderName))
                , 50);
    }


}
