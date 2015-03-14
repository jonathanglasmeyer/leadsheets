package com.example.jwerner.mmd.components;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jwerner.mmd.R;
import com.example.jwerner.mmd.base.BaseFragment;
import com.example.jwerner.mmd.components.setlist.SetlistData;
import com.example.jwerner.mmd.di.AppComponent;
import com.example.jwerner.mmd.helpers.Dialog;
import com.example.jwerner.mmd.lib.FluentBundle;
import com.example.jwerner.mmd.lib.FragmentArgsSetter;
import com.example.jwerner.mmd.stores.FileStore;

import java.io.File;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jwerner on 2/25/15.
 */

public class EditFragment extends BaseFragment {
    public static final String FILEPATH = "text";
    @InjectView(R.id.edit_text)
    EditText mEditText;
    @Inject FileStore mFileStore;
    @Inject SetlistData mSetlistData;
    private File mFilePath;
    private boolean mDeleteMode;

    public static EditFragment newInstance(File filePath) {
        final FluentBundle bundle = FluentBundle.newFluentBundle()
                .put(FILEPATH, filePath.toString());
        return FragmentArgsSetter.setFragmentArguments(new EditFragment(), bundle);
    }

    @Nullable @Override public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);
        return view;
    }


    @Override public void onResume() {
        mDeleteMode = false;
        super.onResume();
    }

    @Override public void onStop() {
        super.onStop();
        if (!mDeleteMode) {
            // only write on back press if we aren't exiting because of a delete operation
            mFileStore.writeContent(mFilePath, mEditText.getText().toString());
        }
    }

    @Override public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFilePath = new File(getArguments().getString(FILEPATH));
        mEditText.setText(mFileStore.getContent(mFilePath));

    }

    @Override public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove:
                Dialog.showQuestionDialog(getActivity(), "Do you want to remove this song?", "Remove", new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(final MaterialDialog dialog) {
                        final File file = new File(getArguments().getString(FILEPATH));
                        mFileStore.delete(file);
                        final String fileName = file.getName();
                        mSetlistData.removeItem(fileName.substring(0, fileName.length() - 4));

                        mDeleteMode = true;
                        getActivity().onBackPressed();
                    }
                });

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override protected void onCreateComponent(AppComponent appComponent) {
        appComponent.inject(this);

    }
}
