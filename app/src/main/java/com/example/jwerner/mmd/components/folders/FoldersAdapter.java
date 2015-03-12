package com.example.jwerner.mmd.components.folders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jwerner.mmd.R;
import com.example.jwerner.mmd.stores.FileStore;

import java.util.List;

/**
 * Created by jwerner on 2/23/15.
 */
public class FoldersAdapter extends ArrayAdapter {
    private final Context mContext;
    private final List<FileStore.Folder> mFolders;

    public FoldersAdapter(final Context context, final List<FileStore.Folder> folders) {
        super(context, 0, folders);
        mContext = context;
        mFolders = folders;
    }

    @Override public String getItem(final int position) {
        // -1 because of the caption item, which counts
        return mFolders.get(position - 1).name;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View mView = v;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(R.layout.list_item_folder, parent, false);
        }
        TextView text = (TextView) mView.findViewById(R.id.text_foldername);
        TextView textSongsCount = (TextView) mView.findViewById(R.id.text_songscount);

        final FileStore.Folder folder = mFolders.get(position);
//        text.setText(folder.name + " (" + folder.count + " Songs)");
        text.setText(folder.name);
        textSongsCount.setText(folder.count + " Songs");

        return mView;
    }
}
