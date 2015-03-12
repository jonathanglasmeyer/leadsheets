package com.example.jwerner.mmd.components.detailedsetlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jwerner.mmd.R;
import com.example.jwerner.mmd.base.BaseFragment;
import com.example.jwerner.mmd.base.Controller;
import com.example.jwerner.mmd.components.EditActivity;
import com.example.jwerner.mmd.components.setlist.SetlistData;
import com.example.jwerner.mmd.lib.FluentBundle;
import com.example.jwerner.mmd.lib.FragmentArgsSetter;
import com.example.jwerner.mmd.stores.FileStore;
import com.google.common.collect.FluentIterable;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

/**
 * Created by jwerner on 2/20/15.
 */
public class DetailedSetlistFragment extends BaseFragment {
    public static final String POSITION = "position";
    public static final String FOLDER_NAME = "folderName";
    @InjectView(R.id.detailed_setlist) ListView mDetailedSetlist;

    @Inject
    FileStore mFileStore;
    @Inject SetlistData mSetlistData;


    DetailedSetlistAdapter mDetailedSetlistAdapter;
    private DetailedSetlistController mController;

    public static DetailedSetlistFragment newInstance(int position) {
        final FluentBundle bundle = FluentBundle.newFluentBundle()
                .put(POSITION, position);
        return FragmentArgsSetter.setFragmentArguments(new DetailedSetlistFragment(), bundle);
    }

    @Override
    public Controller getController() {
        return mController;
    }

    @Override
    public void setController() {
        mController = new DetailedSetlistController(getActivity(), this);
    }

    public void refresh() {
        mDetailedSetlistAdapter.clear();
        mDetailedSetlistAdapter.addAll(getFNameContentTuples());
        mDetailedSetlistAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_detailedsetlist, container, false);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String[]> initialContent = getFNameContentTuples();
        mDetailedSetlistAdapter = new DetailedSetlistAdapter(getActivity(),
                R.layout.list_item_detailed, initialContent);
        mDetailedSetlist.setAdapter(mDetailedSetlistAdapter);
        final int position = getArguments().getInt("position") - 1;
        mDetailedSetlist.setSelection(position);

        mDetailedSetlist.setOnItemClickListener((parent, view1, position1, id) ->
                startEditActivity(position1));
    }

    private void startEditActivity(final int position1) {
        final SetlistData.ConcreteData item = (SetlistData.ConcreteData) mSetlistData.getItem(position1 + 1);
        Timber.d("startEditActivity: " + item.getText());
        final String itemFilePath = item.getFilePath().toString();
        final Intent intent = new Intent(getActivity(), EditActivity.class);
        intent.putExtra(EditActivity.FILEPATH, itemFilePath);
        getActivity().startActivity(intent);
    }

    private ArrayList<String[]> getFNameContentTuples() {
        // get fresh content from the current setlist
        final FluentIterable<String> setlist = mSetlistData.getSetlistFromData();
        final String currentDir = mSetlistData.getCurrentDir();
        return mFileStore.getFNameContentTuples(currentDir, setlist);
    }

}
