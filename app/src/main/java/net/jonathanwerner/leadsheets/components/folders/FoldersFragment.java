package net.jonathanwerner.leadsheets.components.folders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;

import net.jonathanwerner.leadsheets.BuildConfig;
import net.jonathanwerner.leadsheets.R;
import net.jonathanwerner.leadsheets.base.BaseFragment;
import net.jonathanwerner.leadsheets.base.Controller;
import net.jonathanwerner.leadsheets.base.HasComponent;
import net.jonathanwerner.leadsheets.components.HelpActivity;
import net.jonathanwerner.leadsheets.components.SettingsActivity;
import net.jonathanwerner.leadsheets.components.main.MainActivity;
import net.jonathanwerner.leadsheets.di.AppComponent;
import net.jonathanwerner.leadsheets.events.FolderClick;
import net.jonathanwerner.leadsheets.helpers.Dialog;
import net.jonathanwerner.leadsheets.helpers.Resources;
import net.jonathanwerner.leadsheets.lib.TinyDB;
import net.jonathanwerner.leadsheets.stores.FileStore;
import net.jonathanwerner.leadsheets.stores.Hints;
import net.jonathanwerner.leadsheets.stores.Sku;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class FoldersFragment extends BaseFragment implements HasComponent<AppComponent> {
    @InjectView(R.id.folders_list) ListView mFoldersListView;
    @Inject FileStore mFileStore;
    @Inject TinyDB mTinyDB;
    @Inject Dialog mDialog;
    @Inject Resources mResources;
    @Inject Hints mHints;
    private FoldersAdapter mAdapter;
    private FoldersController mController;
    private AppComponent mComponent;
    private View mView;
    private MenuItem mMenuItemAds;
    private MenuItem mMenuItemHints;

    @Override
    public Controller getController() {
        return mController;
    }

    @Override
    public void setController() {
        mController = new FoldersController(getActivity(), this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<FileStore.Folder> folders = mFileStore.getFolders();
        mAdapter = new FoldersAdapter(getActivity(), folders);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folders_list, container, false);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);
        mFoldersListView.addHeaderView(inflater.inflate(R.layout.list_item_caption_simple, mFoldersListView, false));

        return view;
    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove_ads:

                if (BuildConfig.DEBUG) {
                    Dialog.showSnackbarInfo(getActivity(), "Can't buy stuff in Debug Mode");
                    return true;
                }

                MainActivity mainActivity = (MainActivity) getActivity();
                if (!mainActivity.mIabReady) return true;

                mainActivity.mIabHelper.launchPurchaseFlow(getActivity(), Sku.DISABLE_ADS, 10001, (result, purchase) -> {
                    if (result.isSuccess()) {
                        mDialog.showInfoDialog(getActivity(), mResources.getString(R.string.dialog_thanks));
                        mainActivity.restartActivity();
                    }
                });

                return true;
            case R.id.action_settings:
                getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.action_help:
                getActivity().startActivity(new Intent(getActivity(), HelpActivity.class));
                return true;
            case R.id.action_feedback:
                Intent send = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode("jwerner@gmail.com") +
                        "?subject=" + Uri.encode(mResources.getString(R.string.feedback_mail_header)) +
                        "&body=" + Uri.encode("");
                Uri uri = Uri.parse(uriText);

                send.setData(uri);
                startActivity(Intent.createChooser(send, mResources.getString(R.string.action_send_mail)));
                return true;
            case R.id.action_rate:
                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return true;
            case R.id.action_show_hints:
                mHints.reset();
                Dialog.showSnackbarInfo(getActivity(), mResources.getString(R.string.snackbar_showing_hints_again));

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override public void onPrepareOptionsMenu(final Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean disableAds = ((MainActivity) getActivity()).mDisableAds;
        mMenuItemAds.setVisible(!disableAds);
        mMenuItemHints.setVisible(mHints.allDone());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_folders, menu);
        super.onCreateOptionsMenu(menu, inflater);
        mMenuItemAds = menu.findItem(R.id.action_remove_ads);
        mMenuItemHints = menu.findItem(R.id.action_show_hints);
    }

    void refresh() {
        mAdapter.clear();
        mAdapter.addAll(mFileStore.getFolders());
        mAdapter.notifyDataSetChanged();
        showEmptyTextView();
    }

    @Override public void onViewCreated(final View view, final Bundle savedInstanceState) {
        mView = view;
        super.onViewCreated(mView, savedInstanceState);

        mFoldersListView.setAdapter(mAdapter);
        mFoldersListView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position > 0)
                EventBus.getDefault().post(new FolderClick(mAdapter.getItem(position)));
        });

        showEmptyTextView();

        registerForContextMenu(mFoldersListView);

    }

    private void showEmptyTextView() {
        mView.findViewById(R.id.empty_folders_view).setVisibility(
                mAdapter.getCount() > 0 ? View.GONE : View.VISIBLE);
    }


    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        if (position > 0) {
            final String item = mAdapter.getItem(position);
            menu.setHeaderTitle(item);
            inflater.inflate(R.menu.contextmenu_folder, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_remove:
                mDialog.showQuestionDialog(getActivity(),
                        mResources.getString(R.string.dialog_remove_project),
                        mResources.getString(R.string.dialog_remove), new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(final MaterialDialog dialog) {
                                final boolean worked = mFileStore.removeFolder(mAdapter.getItem(info.position));
                                if (!worked) {
                                    Dialog.showSnackbarInfo(getActivity(), mResources.getString(R.string.dialog_delete_error));
                                }
                            }
                        });
                return true;
            case R.id.action_rename:
                String itemName = mAdapter.getItem(info.position);
                mDialog.showRenameDialog(getActivity(),
                        mResources.getString(R.string.dialog_rename_folder_question),
                        mResources.getString(R.string.action_rename), itemName, s -> {
                            boolean worked = mFileStore.renameFolder(itemName, s);
                            if (!worked) {
                                Dialog.showSnackbarInfo(getActivity(), mResources.getString(R.string.dialog_couldnt_rename));
                            } else {
                                refresh();
                            }
                        });
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override protected void onCreateComponent(AppComponent appComponent) {
        mComponent = appComponent;
        mComponent.inject(this);
    }

    @Override public AppComponent getComponent() {
        return mComponent;
    }

}
