package net.jonathanwerner.leadsheets.components;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import net.jonathanwerner.leadsheets.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HelpActivity extends ActionBarActivity {
    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.header) View mHeader;
    @InjectView(R.id.help_text) TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        ViewCompat.setElevation(mHeader, getResources().getDimension(R.dimen.toolbar_elevation));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextView.setText(Html.fromHtml((String) getResources().getText(R.string.help_text)));

    }

    @Override protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle(getResources().getString(R.string.action_help));
    }
}
