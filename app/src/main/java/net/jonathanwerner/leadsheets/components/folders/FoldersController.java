package net.jonathanwerner.leadsheets.components.folders;

import android.content.Context;

import net.jonathanwerner.leadsheets.base.Controller;
import net.jonathanwerner.leadsheets.di.helper.Bus;
import net.jonathanwerner.leadsheets.events.ChangeFolders;

/**
 * Created by jwerner on 3/6/15.
 */
public class FoldersController extends Controller {
    private final Context mContext;
    private final FoldersFragment mFragment;

    public FoldersController(final Context context, FoldersFragment fragment) {
        super(context);
        mContext = context;
        mFragment = fragment;
    }

    @Bus public void onEvent(ChangeFolders event) {
        mFragment.refresh();
    }
}
