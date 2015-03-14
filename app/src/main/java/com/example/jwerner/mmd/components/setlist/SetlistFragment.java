package com.example.jwerner.mmd.components.setlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jwerner.mmd.R;
import com.example.jwerner.mmd.base.Controller;
import com.example.jwerner.mmd.base.HasComponent;
import com.example.jwerner.mmd.di.AppComponent;
import com.example.jwerner.mmd.lib.FluentBundle;
import com.example.jwerner.mmd.lib.FragmentArgsSetter;
import com.example.jwerner.mmd.stores.UIState;
import com.example.jwerner.mmd.widgets.DragSwipeRecyclerFragment;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import javax.inject.Inject;

import timber.log.Timber;

public class SetlistFragment extends DragSwipeRecyclerFragment implements HasComponent<AppComponent> {
    public static final String FOLDER_NAME = "folderName";
    @Inject SetlistAdapter mSetlistAdapter;
    @Inject SetlistData mSetlistData;
    private SetlistController mSetlistController;
    private MenuItem menuItemLock;
    private AppComponent mComponent;

    public static SetlistFragment newInstance(String folderName) {
        return FragmentArgsSetter.setFragmentArguments(new SetlistFragment(),
                FluentBundle.newFluentBundle().put(FOLDER_NAME, folderName));
    }

    @Override public void setController() {
        if (mSetlistController == null)
            mSetlistController = new SetlistController(this, getActivity());
    }

    @Override public Controller getController() {
        return mSetlistController;
    }


    @Override
    public void onResume() {
        refresh();
        getActivity().invalidateOptionsMenu();
        super.onResume();
    }

    @Override public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        refresh();
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void refresh() {
        mSetlistData.changeCurrentDirAndRefreshData(getArguments().getString(FOLDER_NAME));
        mSetlistAdapter.notifyDataSetChanged();
    }

    @Override public void onPause() {
        super.onPause();
        mSetlistAdapter.setReorderMode(false);
    }

    public RecyclerView.Adapter getAdapter() {
        return mSetlistAdapter;
    }


    private void switchMode() {
        UIState.alphabetMode = !UIState.alphabetMode;
        if (UIState.alphabetMode) {
            mSetlistData.setAlphabeticalMode(true);
        } else {
            mSetlistData.setAlphabeticalMode(false);
        }
        refresh();
//        getActivity().invalidateOptionsMenu();
    }

    private void setAlphabeticToggleIcon(MenuItem item) {
        if (UIState.alphabetMode) {
            item.setIcon(R.drawable.ic_format_list_numbered_white_24dp);
//            menuItemLock.setVisible(false);
        } else {
            item.setIcon(R.drawable.ic_my_library_books_white_24dp);
//            menuItemLock.setVisible(true);
        }
    }

    private void setLockToggleIcon(MenuItem item) {
        if (UIState.lock) {
            item.setIcon(R.drawable.ic_lock_open_white_24dp);
        } else {
            item.setIcon(R.drawable.ic_lock_outline_white_24dp);
        }
    }

    @Override public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setlist, menu);
        menuItemLock = menu.findItem(R.id.action_lock);
        setAlphabeticToggleIcon(menu.findItem(R.id.action_alphabetical));
        setLockToggleIcon(menuItemLock);
//        if (UIState.alphabetMode) {
//            menu.findItem(R.id.action_lock).setVisible(false);
//        } else {
//            menu.findItem(R.id.action_lock).setVisible(true);
//        }
//        final Switch switch_ = (Switch) menu.findItem(R.id.action_switch).getActionView().findViewById(R.id.switch_mode);
//        switch_.setChecked(UIState.alphabetMode);
//        switch_.setOnCheckedChangeListener((buttonView, isChecked) -> switchMode(isChecked));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showLockSnackbarHint(boolean isLockedNow) {
        SnackbarManager.show(
                Snackbar.with(getActivity().getApplicationContext())
                        .text(isLockedNow ? "Setlist locked" : "Setlist unlocked")
                        .duration(2000)
                        .type(SnackbarType.SINGLE_LINE)
                        .swipeToDismiss(false)
                , getActivity());
    }

    @Override public boolean onOptionsItemSelected(final MenuItem item) {
        final int itemId = item.getItemId();

        switch (itemId) {
//            case R.id.action_reorder:
//                EventBus.getDefault().post(new SetlistReorder(true));
//                mMenu.clear();
//                mMenuInflater.inflate(R.menu.menu_done, mMenu);
//                break;

            case R.id.action_lock:
                mSetlistData.toggleLockMode();
                showLockSnackbarHint(UIState.lock);
                setLockToggleIcon(item);
                mSetlistAdapter.onChangeItem();
                mSetlistAdapter.notifyDataSetChanged();
//                refresh();
                break;

            case R.id.action_alphabetical:
                switchMode();
                setAlphabeticToggleIcon(item);
                break;

//            case R.id.action_numerical:
//                break;

//            case R.id.action_reset:

//                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();

        final int position = mSetlistAdapter.mLastLongClickedPosition;
        final SetlistData.ConcreteData item = (SetlistData.ConcreteData) mSetlistAdapter.getItem(position);

        menu.setHeaderTitle(item.getText());
        inflater.inflate(R.menu.contextmenu_folder, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove:
                final int position = mSetlistAdapter.mLastLongClickedPosition;
                final SetlistData.ConcreteData item1 = (SetlistData.ConcreteData) mSetlistAdapter.getItem(position);
                Timber.d("onContextItemSelected: " + item1.getText());
                mSetlistData.removeItem(position);
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
