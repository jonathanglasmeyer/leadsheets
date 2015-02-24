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
import com.example.jwerner.mmd.components.folders.FoldersFragment;
import com.example.jwerner.mmd.di.base.DaggerActivity;
import com.example.jwerner.mmd.events.ToggleToolbar;
import com.example.jwerner.mmd.stores.UIState;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import timber.log.Timber;


public class MainActivity extends DaggerActivity {
//    public static final String FRAGMENT_LIST_VIEW = "list view";
    @InjectView(R.id.toolbar) protected Toolbar mToolbar;
    @InjectView(R.id.header) View mHeader;
    @InjectView(R.id.content_frame) View mContentFrame;
    private MainController mMainController;
    private FragmentManager mFragmentManager;
    private ToolbarController mToolbarController;
    private View mDecorView;
    private int mStatusBarHeight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // this is a test for pivotal tracker.. test continue

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        if (mMainController == null) mMainController = new MainController(this);
        mMainController.register();
        mToolbarController = new ToolbarController(this);
        mToolbarController.register();
        mFragmentManager = getFragmentManager();


        mFragmentManager.addOnBackStackChangedListener(() -> {

            final int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
            if (backStackEntryCount == 0) {
                mToolbar.setTitle("Leadsheets");
                return;
            }

            final String current = mFragmentManager.getBackStackEntryAt(backStackEntryCount - 1).getName();
            if (current == UIState.SETLIST) {
                Timber.d("backstack: " + UIState.SETLIST);
            } else {
            }
        });

        // toolbar
        setSupportActionBar(mToolbar);
        ViewCompat.setElevation(mHeader, getResources().getDimension(R.dimen.toolbar_elevation));

        if (savedInstanceState == null) {

            final FoldersFragment fragment = new FoldersFragment();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.setSharedElementReturnTransition(new Slide());
                fragment.setExitTransition(new Fade());
            }

            getFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment)
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




    @Override protected void onDestroy() {
        super.onDestroy();
        mMainController.unregister();
        mToolbarController.unregister();
    }

}