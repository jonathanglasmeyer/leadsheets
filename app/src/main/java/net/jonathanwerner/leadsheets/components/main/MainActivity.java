package net.jonathanwerner.leadsheets.components.main;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.common.collect.Lists;
import com.melnykov.fab.FloatingActionButton;

import net.jonathanwerner.leadsheets.BuildConfig;
import net.jonathanwerner.leadsheets.R;
import net.jonathanwerner.leadsheets.base.BaseActivity;
import net.jonathanwerner.leadsheets.base.Controller;
import net.jonathanwerner.leadsheets.components.folders.FoldersFragment;
import net.jonathanwerner.leadsheets.components.setlist.SetlistData;
import net.jonathanwerner.leadsheets.di.AppComponent;
import net.jonathanwerner.leadsheets.events.ChangeToolbarTitle;
import net.jonathanwerner.leadsheets.events.FolderNew;
import net.jonathanwerner.leadsheets.events.SongNew;
import net.jonathanwerner.leadsheets.events.ToggleToolbar;
import net.jonathanwerner.leadsheets.helpers.Resources;
import net.jonathanwerner.leadsheets.helpers.Strings;
import net.jonathanwerner.leadsheets.lib.TinyDB;
import net.jonathanwerner.leadsheets.lib.billing_util.IabHelper;
import net.jonathanwerner.leadsheets.stores.Constants;
import net.jonathanwerner.leadsheets.stores.FileStore;
import net.jonathanwerner.leadsheets.stores.Hints;
import net.jonathanwerner.leadsheets.stores.Preferences;
import net.jonathanwerner.leadsheets.stores.Sku;
import net.jonathanwerner.leadsheets.stores.UIState;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import de.greenrobot.event.EventBus;
import timber.log.Timber;


public class MainActivity extends BaseActivity {
    public IabHelper mIabHelper;
    public boolean mIabReady = false;
    public boolean mDisableAds;
    @InjectView(R.id.toolbar) protected Toolbar mToolbar;
    @InjectView(R.id.header) View mHeader;
    @InjectView(R.id.content_frame) View mContentFrame;
    @InjectView(R.id.fab) FloatingActionButton mFab;
    @Optional @InjectView(R.id.adview) AdView mAd;
    @Optional @InjectView(R.id.ad_container) ViewGroup mAdContainer;
    @Inject SetlistData mSetlistData;
    @Inject FileStore mFileStore;
    @Inject TinyDB mTinyDB;
    @Inject Resources mResources;
    @Inject Hints mHints;
    private MainController mMainController;
    private FragmentManager mFragmentManager;
    private ToolbarController mToolbarController;
    private boolean mIsEmulator;

    private void initView() {
        mDisableAds = mTinyDB.getBoolean(Preferences.DISABLE_ADS, false);
        if (BuildConfig.DEBUG || mIsEmulator) mDisableAds = true;
        setContentView(mDisableAds ? R.layout.activity_main : R.layout.activity_main_with_ads);
        ButterKnife.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsEmulator = Build.BRAND.equals("generic");
        initView();

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        mToolbarController = new ToolbarController(this);
        mToolbarController.register();
        mFragmentManager = getFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this::handleBackstackUIChanges);

        mFab.setOnClickListener(this::handleFabClicked);

        if (!BuildConfig.DEBUG && !mIsEmulator) queryIabForDisableAdChange();

        initAds();

        // toolbar
        setSupportActionBar(mToolbar);
        shouldDisplayHomeUp();
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

    private void queryIabForDisableAdChange() {

        mIabHelper = new IabHelper(this, Constants.PUBKEY);
        mIabHelper.enableDebugLogging(true);

        mIabHelper.startSetup(result -> {
            if (result.isSuccess()) {
                mIabReady = true;
                mIabHelper.queryInventoryAsync(true, Lists.newArrayList(Sku.DISABLE_ADS), (result1, inv) -> {
                    if (result1.isSuccess()) {
                        boolean hasDisabledAds = inv.hasPurchase(Sku.DISABLE_ADS);
                        if (hasDisabledAds != mDisableAds) {
                            mTinyDB.putBoolean(Preferences.DISABLE_ADS, hasDisabledAds);
                            restartActivity();
                        }
                    }
                });
            }
        });
    }

    public void restartActivity() {
        finish();
        startActivity(getIntent());
    }

    private void initAds() {
        if (!mDisableAds) {
            AdRequest ad = new AdRequest.Builder()
                    .addTestDevice(Constants.NEXUS5)
                    .addTestDevice(Constants.GENYMOTION_NEXUS5)
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mAd.loadAd(ad);
        }
    }

    private void handleFabClicked(final View view) {
        EventBus.getDefault().post(getCurrentScreen().equals(UIState.SETLIST) ?
                new SongNew() : new FolderNew());
    }

    @Override
    public boolean onSupportNavigateUp() {
        getFragmentManager().popBackStack();
        return true;
    }

    private void shouldDisplayHomeUp() {
        boolean canGoBack = getFragmentManager().getBackStackEntryCount() > 1;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
    }

    @Override
    public void onStart() {
        super.onStart();
        handleBackstackUIChanges();

    }


    private void handleBackstackUIChanges() {
//        shouldDisplayHomeUp();

        final int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            mToolbar.setTitle(mResources.getString(R.string.app_name));
            return;
        }

        final String current = getCurrentScreen();
        switch (current) {
            case UIState.SETLIST:
                EventBus.getDefault().post(new ChangeToolbarTitle(mSetlistData.getCurrentDir()));
                break;
            case UIState.EDIT:
                EventBus.getDefault().post(new ChangeToolbarTitle(Strings.capitalize(mFileStore.getCurrentFileWithoutExtension())));
                break;
            default:
                break;
        }
    }

    private String getCurrentScreen() {
        final int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            return UIState.FOLDERS;
        }

        return mFragmentManager.getBackStackEntryAt(backStackEntryCount - 1).getName();
    }

    @Override public void setController() {
        if (mMainController == null) mMainController = new MainController(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        mToolbarController.unregister();
        if (mIabHelper != null) mIabHelper.dispose();
        mIabHelper = null;

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

    @Override protected void onCreateComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.d("onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mIabHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Timber.d("onActivityResult handled by IABUtil.");
        }
    }

}
