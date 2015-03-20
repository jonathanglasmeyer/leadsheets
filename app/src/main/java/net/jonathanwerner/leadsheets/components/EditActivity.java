package net.jonathanwerner.leadsheets.components;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;

import net.jonathanwerner.leadsheets.R;
import net.jonathanwerner.leadsheets.base.BaseActivity;
import net.jonathanwerner.leadsheets.components.setlist.SetlistData;
import net.jonathanwerner.leadsheets.di.AppComponent;
import net.jonathanwerner.leadsheets.stores.FileStore;

import java.io.File;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class EditActivity extends BaseActivity {
    public static final String FILEPATH = "filepath";
    @InjectView(R.id.toolbar) protected Toolbar mToolbar;
    @InjectView(R.id.header) View mHeader;
    @Inject SetlistData mSetlistData;
    @Inject FileStore mFileStore;
    private File mFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.inject(this);

        mFilePath = new File(getIntent().getStringExtra(FILEPATH));

        // toolbar
        setSupportActionBar(mToolbar);
        ViewCompat.setElevation(mHeader, getResources().getDimension(R.dimen.toolbar_elevation));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((mFileStore.getFileWithoutExtension(mFilePath)));

        if (savedInstanceState == null) {

            final EditFragment fragment = EditFragment.newInstance(mFilePath);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.setSharedElementReturnTransition(new Fade());
                fragment.setExitTransition(new Fade());
            }

            getFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return (super.onOptionsItemSelected(item));
    }

    @Override protected void onCreateComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

}