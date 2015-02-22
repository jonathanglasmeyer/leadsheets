package com.example.jwerner.mmd.components.setlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.jwerner.mmd.R;
import com.example.jwerner.mmd.widgets.DragSwipeRecyclerFragment;

import javax.inject.Inject;

public class SetlistFragment extends DragSwipeRecyclerFragment {
    @Inject SetlistAdapter mSetlistAdapter;
    private SetlistController mSetlistController;

    @Override public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View headerView = View.inflate(getActivity(), R.layout.list_item_caption, null);
    }

    public RecyclerView.Adapter getAdapter() {
        return mSetlistAdapter;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mSetlistController == null) {
            mSetlistController = new SetlistController(getActivity());
        }
    }

}
