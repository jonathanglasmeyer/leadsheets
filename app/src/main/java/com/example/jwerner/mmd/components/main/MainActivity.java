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
import com.example.jwerner.mmd.base.BaseActivity;
import com.example.jwerner.mmd.base.Controller;
import com.example.jwerner.mmd.components.folders.FoldersFragment;
import com.example.jwerner.mmd.events.ToggleToolbar;
import com.example.jwerner.mmd.stores.UIState;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import timber.log.Timber;


public class MainActivity extends BaseActivity {
    @InjectView(R.id.toolbar) protected Toolbar mToolbar;
    @InjectView(R.id.header) View mHeader;
    @InjectView(R.id.content_frame) View mContentFrame;
    private MainController mMainController;
    private FragmentManager mFragmentManager;
    private ToolbarController mToolbarController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

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
            if (current.equals(UIState.SETLIST)) {
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

    @Override public void setController() {
        if (mMainController == null) mMainController = new MainController(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        mToolbarController.unregister();
    }

    @Override public Controller getController() {
        return mMainController;
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

}