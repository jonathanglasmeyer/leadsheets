package net.jonathanwerner.leadsheets.components.folders;

import android.content.Context;

import net.jonathanwerner.leadsheets.R;
import net.jonathanwerner.leadsheets.base.Controller;
import net.jonathanwerner.leadsheets.di.helper.Bus;
import net.jonathanwerner.leadsheets.events.ChangeFolders;
import net.jonathanwerner.leadsheets.events.FolderNew;
import net.jonathanwerner.leadsheets.helpers.Dialog;
import net.jonathanwerner.leadsheets.helpers.Resources;
import net.jonathanwerner.leadsheets.stores.FileStore;

import javax.inject.Inject;

/**
 * Created by jwerner on 3/6/15.
 */
public class FoldersController extends Controller {
    private final Context mActivityContext;
    private final FoldersFragment mFragment;
    @Inject Dialog mDialog;
    @Inject FileStore mFileStore;
    @Inject Resources mResources;

    public FoldersController(final Context activityContext, FoldersFragment fragment) {
        super(activityContext);
        mActivityContext = activityContext;
        mFragment = fragment;
        fragment.getComponent().inject(this);
    }

    @Bus public void onEvent(ChangeFolders event) {
        mFragment.refresh();
    }

    @Bus public void onEvent(FolderNew event) {
        mDialog.showInputDialog(mActivityContext,
                mResources.getString(R.string.dialog_new_folder), mResources.getString(R.string.dialog_ok),
                mFileStore::newFolder);
    }

}
