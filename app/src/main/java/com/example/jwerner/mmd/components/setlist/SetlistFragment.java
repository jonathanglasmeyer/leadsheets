package com.example.jwerner.mmd.components.setlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jwerner.mmd.R;
import com.example.jwerner.mmd.base.Controller;
import com.example.jwerner.mmd.events.SetlistReorder;
import com.example.jwerner.mmd.lib.FluentBundle;
import com.example.jwerner.mmd.lib.FragmentArgsSetter;
import com.example.jwerner.mmd.widgets.DragSwipeRecyclerFragment;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class SetlistFragment extends DragSwipeRecyclerFragment {
    public static final String FOLDER_NAME = "folderName";
    @Inject SetlistAdapter mSetlistAdapter;
    @Inject SetlistData mSetlistData;
    private Menu mMenu;
    private MenuInflater mMenuInflater;
    private SetlistController mSetlistController;

    public static SetlistFragment newInstance(String folderName) {
        return FragmentArgsSetter.setFragmentArguments(new SetlistFragment(),
                FluentBundle.newFluentBundle().put(FOLDER_NAME, folderName));
    }

    @Override public void setController() {
        if (mSetlistController == null) mSetlistController = new SetlistController(getActivity());
    }

    @Override public Controller getController() {
        return mSetlistController;
    }

    @Override public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        refreshData();
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void refreshData() {
        mSetlistData.changeCurrentDirAndRefreshData(getArguments().getString(FOLDER_NAME));
        mSetlistAdapter.notifyDataSetChanged();
    }

    @Override public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public void onPause() {
        super.onPause();
        mSetlistAdapter.setReorderMode(false);
        mMenu.clear();
        mMenuInflater.inflate(R.menu.menu_setlist, mMenu);
    }

    public RecyclerView.Adapter getAdapter() {
        return mSetlistAdapter;
    }

    @Override public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        mMenu = menu;
        mMenuInflater = inflater;
        mMenuInflater.inflate(R.menu.menu_setlist, mMenu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override public boolean onOptionsItemSelected(final MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            return true;
        } else if (itemId == R.id.action_reorder) {
            EventBus.getDefault().post(new SetlistReorder(true));
            mMenu.clear();
            mMenuInflater.inflate(R.menu.menu_done, mMenu);
        } else if (itemId == R.id.action_done) {
            EventBus.getDefault().post(new SetlistReorder(false));
            mMenu.clear();
            mMenuInflater.inflate(R.menu.menu_setlist, mMenu);

        }
        return super.onOptionsItemSelected(item);
    }

}
