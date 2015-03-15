package net.jonathanwerner.leadsheets.components.detailedsetlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.common.collect.FluentIterable;

import net.jonathanwerner.leadsheets.R;
import net.jonathanwerner.leadsheets.base.BaseFragment;
import net.jonathanwerner.leadsheets.base.Controller;
import net.jonathanwerner.leadsheets.components.EditActivity;
import net.jonathanwerner.leadsheets.components.setlist.SetlistData;
import net.jonathanwerner.leadsheets.di.AppComponent;
import net.jonathanwerner.leadsheets.lib.FluentBundle;
import net.jonathanwerner.leadsheets.lib.FragmentArgsSetter;
import net.jonathanwerner.leadsheets.stores.FileStore;

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

    @Inject FileStore mFileStore;
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
                initialContent);
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

    @Override protected void onCreateComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

}
