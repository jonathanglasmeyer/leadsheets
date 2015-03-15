package net.jonathanwerner.leadsheets.components.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.transition.Fade;

import net.jonathanwerner.leadsheets.DetailListActivity;
import net.jonathanwerner.leadsheets.R;
import net.jonathanwerner.leadsheets.base.Controller;
import net.jonathanwerner.leadsheets.components.setlist.SetlistFragment;
import net.jonathanwerner.leadsheets.di.helper.Bus;
import net.jonathanwerner.leadsheets.events.ChangeToolbarTitle;
import net.jonathanwerner.leadsheets.events.FolderClick;
import net.jonathanwerner.leadsheets.events.SetlistItemClick;
import net.jonathanwerner.leadsheets.stores.UIState;

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
