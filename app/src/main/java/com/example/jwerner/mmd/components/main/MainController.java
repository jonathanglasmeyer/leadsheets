package com.example.jwerner.mmd.components.main;

import android.app.Activity;
import android.content.Intent;

import com.example.jwerner.mmd.DetailListActivity;
import com.example.jwerner.mmd.base.Controller;
import com.example.jwerner.mmd.events.SetlistItemClick;

public class MainController extends Controller {
    private final Activity mContext;


    public MainController(Activity context) {
        super(context);
        mContext = context;
    }

    public void onEvent(SetlistItemClick event) {
//        mContext.getFragmentManager().beginTransaction()
//                .replace(R.id.content_frame, DetailedSetlistFragment.newInstance(event.position))
//                .addToBackStack("DetailSetlist")
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .commit();
        final Intent intent = new Intent(mContext, DetailListActivity.class);
        intent.putExtra("position", event.position);
        mContext.startActivity(intent);

//        EventBus.getDefault().post(new ToggleToolbar());

    }


}
