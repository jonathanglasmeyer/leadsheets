package com.example.jwerner.mmd.components.folders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jwerner.mmd.R;
import com.example.jwerner.mmd.base.BaseFragment;
import com.example.jwerner.mmd.data.FileLayer;
import com.example.jwerner.mmd.events.FolderClick;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class FoldersFragment extends BaseFragment {
    @InjectView(R.id.folders_list) ListView mFoldersListView;
    private FoldersAdapter mAdapter;
    @Inject FileLayer mFileLayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new FoldersAdapter(getActivity(), mFileLayer.getFolders());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folders, container, false);
        ButterKnife.inject(this, view);
        mFoldersListView.addHeaderView(inflater.inflate(R.layout.list_item_caption, mFoldersListView, false));

        return view;
    }

    @Override public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFoldersListView.setAdapter(mAdapter);
        mFoldersListView.setOnItemClickListener((parent, view1, position, id) ->
                EventBus.getDefault().post(new FolderClick(mAdapter.getItem(position-1))));
    }

}
