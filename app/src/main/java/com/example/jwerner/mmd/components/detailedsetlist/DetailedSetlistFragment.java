package com.example.jwerner.mmd.components.detailedsetlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jwerner.mmd.R;
import com.example.jwerner.mmd.base.BaseFragment;
import com.example.jwerner.mmd.components.setlist.SetlistData;
import com.example.jwerner.mmd.data.FileLayer;
import com.example.jwerner.mmd.lib.FluentBundle;
import com.example.jwerner.mmd.lib.FragmentArgsSetter;
import com.google.common.collect.FluentIterable;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jwerner on 2/20/15.
 */
public class DetailedSetlistFragment extends BaseFragment {
    @InjectView(R.id.detailed_setlist) ListView mDetailedSetlist;

    @Inject FileLayer mFileLayer;
    @Inject SetlistData mSetlistData;

    DetailedSetlistAdapter mDetailedSetlistAdapter;

    public static DetailedSetlistFragment newInstance(int position) {
        final FluentBundle bundle = FluentBundle.newFluentBundle()
                .put("position", position);
        return FragmentArgsSetter.setFragmentArguments(new DetailedSetlistFragment(), bundle);
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
                R.layout.detailedsetlist_item, initialContent);
        mDetailedSetlist.setAdapter(mDetailedSetlistAdapter);
        final int position = getArguments().getInt("position") - 1;
        mDetailedSetlist.setSelection(position);


//        hideSystemUI();

    }

    private ArrayList<String[]> getFNameContentTuples() {
        final FluentIterable<String> setlist = mSetlistData.getSetlistFromData();
        final String currentDir = mSetlistData.getCurrentDir();
        return mFileLayer.getFNameContentTuples(currentDir, setlist);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
//        showSystemUI();
    }

    private void updateMainList() {
        mDetailedSetlistAdapter.clear();
        mDetailedSetlistAdapter.addAll(getFNameContentTuples());
    }


}
