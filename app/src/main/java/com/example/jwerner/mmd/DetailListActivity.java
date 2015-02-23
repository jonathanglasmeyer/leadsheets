package com.example.jwerner.mmd;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.example.jwerner.mmd.components.detailedsetlist.DetailedSetlistFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class DetailListActivity extends ActionBarActivity {
    @InjectView(R.id.content_frame) View mContentFrame;
    private View mDecorView;

    @Override protected void onPostResume() {
        super.onPostResume();
        hideSystemUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaillist);
        ButterKnife.inject(this);
        mDecorView = getWindow().getDecorView();

        if (savedInstanceState == null) {
            final int position = getIntent().getIntExtra("position", 0);
            getFragmentManager().beginTransaction()
                    .add(R.id.content_frame, DetailedSetlistFragment.newInstance(position))
                    .commit();
        }
        hideSystemUI();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override protected void onPause() {
        super.onPause();
        showSystemUI();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showSystemUI() {
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
