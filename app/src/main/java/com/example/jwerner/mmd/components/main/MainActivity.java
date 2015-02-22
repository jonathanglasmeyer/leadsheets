package com.example.jwerner.mmd.components.main;

import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;

import com.example.jwerner.mmd.R;
import com.example.jwerner.mmd.components.setlist.SetlistFragment;
import com.example.jwerner.mmd.di.base.DaggerActivity;
import com.example.jwerner.mmd.events.ToggleToolbar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;


public class MainActivity extends DaggerActivity {
    public static final String FRAGMENT_LIST_VIEW = "list view";
    @InjectView(R.id.toolbar) protected Toolbar mToolbar;
    @InjectView(R.id.header) View mHeader;
    @InjectView(R.id.content_frame) View mContentFrame;
    private MainController mMainController;
    private FragmentManager mFragmentManager;
    private ToolbarController mToolbarController;
    private View mDecorView;
    private int mStatusBarHeight;

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


        mMainController = new MainController(this);
        mToolbarController = new ToolbarController(this);
        mFragmentManager = getFragmentManager();

        // toolbar
        setSupportActionBar(mToolbar);
        ViewCompat.setElevation(mHeader, getResources().getDimension(R.dimen.toolbar_elevation));

        if (savedInstanceState == null) {
            final SetlistFragment fragment = new SetlistFragment();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.setSharedElementReturnTransition(new Slide());
                fragment.setExitTransition(new Fade());
            }

            getFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment, FRAGMENT_LIST_VIEW)
                    .addToBackStack("Setlist")
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() != 0) {
            mFragmentManager.popBackStack();
            EventBus.getDefault().post(new ToggleToolbar());
        } else {
            super.onBackPressed();
        }
    }


//    @OnCheckedChanged(R.id.checkbox_reorder)
//    public void onChecked(boolean checked) {
//        mSetlistData.mSortable = checked;
//        final ArrayList<String[]> fNameContentTuples = getFNameContentTuples();
//        Log.d(TAG, "onChecked " + fNameContentTuples);
//    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        updateMainList();
    }


}