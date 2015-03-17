package net.jonathanwerner.leadsheets.components.folders;

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

import net.jonathanwerner.leadsheets.R;
import net.jonathanwerner.leadsheets.base.BaseFragment;
import net.jonathanwerner.leadsheets.base.Controller;
import net.jonathanwerner.leadsheets.components.main.MainActivity;
import net.jonathanwerner.leadsheets.di.AppComponent;
import net.jonathanwerner.leadsheets.events.FolderClick;
import net.jonathanwerner.leadsheets.helpers.Dialog;
import net.jonathanwerner.leadsheets.stores.FileStore;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class FoldersFragment extends BaseFragment {
    @InjectView(R.id.folders_list) ListView mFoldersListView;
    @Inject FileStore mFileStore;
    private FoldersAdapter mAdapter;
    private FoldersController mController;
    private AppComponent mComponent;

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

        Timber.d("onCreate: " + mFileStore);

        mAdapter = new FoldersAdapter(getActivity(), mFileStore.getFolders());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folders_list, container, false);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);
        mFoldersListView.addHeaderView(inflater.inflate(R.layout.list_item_caption, mFoldersListView, false));

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
                MainActivity mainActivity = (MainActivity) getActivity();
                // TODO
                Dialog.showSnackbarInfo(getActivity(), "Yet to be implemented.");
//                if (mainActivity.mIabReady) {
//                    Dialog.showSnackbarInfo(getActivity(), "i'm ready");

//                    mainActivity.mIabHelper.launchPurchaseFlow(getActivity(), MainActivity.SKU_SUCCESS, 10001,
//                            (result, info) -> {
//                                if (result.isSuccess()) {
//                                    Dialog.showSnackbarInfo(getActivity(), "buy sucess!");
//                                } else {
//                                    Dialog.showSnackbarInfo(getActivity(), "buy fail!");
//                                }
//                            }, "");
//                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_folders, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    void refresh() {
        mAdapter.clear();
        mAdapter.addAll(mFileStore.getFolders());
        mAdapter.notifyDataSetChanged();
    }

    @Override public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFoldersListView.setAdapter(mAdapter);
        mFoldersListView.setOnItemClickListener((parent, view1, position, id) ->
                EventBus.getDefault().post(new FolderClick(mAdapter.getItem(position))));

        registerForContextMenu(mFoldersListView);

    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        final String item = mAdapter.getItem(((AdapterView.AdapterContextMenuInfo) menuInfo).position);
        menu.setHeaderTitle(item);
        inflater.inflate(R.menu.contextmenu_folder, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_remove:
                Dialog.showQuestionDialog(getActivity(), "Do you really want to remove this project?", "Remove", new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(final MaterialDialog dialog) {
                        mFileStore.removeFolder(mAdapter.getItem(info.position));
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

}
