package net.jonathanwerner.leadsheets.components;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.jonathanwerner.leadsheets.R;
import net.jonathanwerner.leadsheets.base.BaseActivity;
import net.jonathanwerner.leadsheets.di.AppComponent;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingsActivity extends BaseActivity {
    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.header) View mHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        ViewCompat.setElevation(mHeader, getResources().getDimension(R.dimen.toolbar_elevation));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new SettingsFragment())
                .commit();
    }

    @Override protected void onCreateComponent(final AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle(getResources().getString(R.string.action_help));
    }

}
