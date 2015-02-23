package com.example.jwerner.mmd.components.setlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jwerner.mmd.lib.FluentBundle;
import com.example.jwerner.mmd.lib.FragmentArgsSetter;
import com.example.jwerner.mmd.widgets.DragSwipeRecyclerFragment;

import javax.inject.Inject;

public class SetlistFragment extends DragSwipeRecyclerFragment {
    public static final String FOLDER_NAME = "folderName";
    private SetlistController mSetlistController;
    @Inject SetlistAdapter mSetlistAdapter;
    @Inject SetlistData mSetlistData;

    public static SetlistFragment newInstance(String folderName) {
        return FragmentArgsSetter.setFragmentArguments(new SetlistFragment(),
                FluentBundle.newFluentBundle().put(FOLDER_NAME, folderName));
    }

    @Override public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        refreshData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void refreshData() {
        mSetlistData.changeCurrentDirAndRefreshData(getArguments().getString(FOLDER_NAME));
        mSetlistAdapter.notifyDataSetChanged();
    }

    @Override public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override public void onDestroy() {
        super.onDestroy();
        mSetlistController.unregister();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mSetlistController == null) {
            mSetlistController = new SetlistController(getActivity());
        }
        mSetlistController.register();
    }


    public RecyclerView.Adapter getAdapter() {
        return mSetlistAdapter;
    }

}
