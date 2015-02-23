package com.example.jwerner.mmd.components.main;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.jwerner.mmd.base.Controller;
import com.example.jwerner.mmd.events.ToggleToolbar;

/**
 * Created by jwerner on 2/21/15.
 */
public class ToolbarController extends Controller {
    private final int mStatusBarHeight;
    private final View mDecorView;
    private final Toolbar mToolbar;
    private final MainActivity mContext;
    private boolean mShowToolbar = true;
    private int mValue;
    private int mPaddingTop = 0;
    private float mY;
    private View mContentFrame;
    private View mHeader;

    protected ToolbarController(final Context context) {
        super(context);
        mContext = (MainActivity) context;

        mDecorView = mContext.getWindow().getDecorView();
        mToolbar = ((MainActivity) context).mToolbar;
        mHeader = mContext.mHeader;
        mContentFrame = mContext.mContentFrame;

        mStatusBarHeight = getStatusBarHeight();
        mPaddingTop = mPaddingTop == 0 ? mContentFrame.getPaddingTop() : mPaddingTop;

        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override public void onSystemUiVisibilityChange(final int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // sys bars visible
//                    mHeader.setY(getStatusBarHeight());
//                    mContentFrame.setPadding(0, mPaddingTop, 0, 0);
//                    mDecorView.invalidate();
                } else { // fullscreen
//                    mContentFrame.setPadding(0, 0, 0, 0);
//                    mValue = -mHeader.getBottom();
//                    mHeader.animate().translationY(mValue).setInterpolator(new AccelerateInterpolator()).start();

                }
            }
        });
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public void onEvent(ToggleToolbar event) {
        if (mShowToolbar) { // --> hide toolbar
//            hideSystemUI();
//
//            mY = mContentFrame.getY();
//            mPaddingTop = mPaddingTop == 0 ? mContentFrame.getPaddingTop() : mPaddingTop;
//            mContentFrame.setPadding(0, 0, 0, 0);


        } else { // show toolbar

//            showSystemUI();
//            final int top = mPaddingTop;
////            mHeader.setY(getStatusBarHeight());
//            mContentFrame.setPadding(0, top, 0, 0);
////            mHeader.animate().y(getStatusBarHeight()).setInterpolator(new DecelerateInterpolator()).start();
        }
        mShowToolbar = !mShowToolbar;
    }


}
