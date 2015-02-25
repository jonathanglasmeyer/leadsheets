package com.example.jwerner.mmd.components.setlist;

import android.content.Context;
import android.content.Intent;

import com.example.jwerner.mmd.R;
import com.example.jwerner.mmd.base.Controller;
import com.example.jwerner.mmd.events.SetlistGeneralClick;
import com.example.jwerner.mmd.events.SetlistItemClick;
import com.example.jwerner.mmd.events.SetlistRemove;
import com.example.jwerner.mmd.events.SetlistReorder;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;
import timber.log.Timber;

public class SetlistController extends Controller {

    private final Context mContext;
    @Inject SetlistData mSetlistData;
    @Inject SetlistAdapter mSetlistAdapter;

    @DebugLog
    public SetlistController(Context context) {
        super(context);
        mContext = context;
    }

    public void onEvent(SetlistGeneralClick event) {
        final int position = event.position;
        final SetlistData.ConcreteData item = (SetlistData.ConcreteData) mSetlistData.getItem(position);
        final int itemType = item.getItemType();
        Timber.d("onEvent click: " + itemType);
        if (itemType == SetlistData.ITEM_TYPE_REST) {
            mSetlistAdapter.onMoveItem(position, mSetlistData.getNewSetlistItemPos()); // notify adapter that list has changed
        } else if (itemType == SetlistData.ITEM_TYPE_SETLIST) {
            EventBus.getDefault().post(new SetlistItemClick(position));
        } else if (itemType == SetlistData.ITEM_TYPE_ALPHABETICAL) {
            final Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(item.getFilePath(), "text/plain");
            mContext.startActivity(intent);
            Timber.d("onEvent: " + item.getFilePath());

        }
    }

    public void onEvent(SetlistRemove event) {
//        showUndoSnackbar();
    }

    public void onEvent(SetlistReorder event) {
        mSetlistAdapter.setReorderMode(event.reorderMode);
    }

    private void showUndoSnackbar() {
        SnackbarManager.show(
                Snackbar.with(mContext.getApplicationContext())
                        .text(R.string.snack_bar_text_item_removed)
                        .actionLabel(R.string.snack_bar_action_undo)
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                onItemUndoActionClicked();
                            }
                        })
                        .actionColorResource(R.color.snackbar_action_color_done)
                        .duration(5000)
                        .type(SnackbarType.SINGLE_LINE)
                        .swipeToDismiss(false)
                , (android.app.Activity) mContext);
    }

    private void onItemUndoActionClicked() {
        int position = mSetlistData.undoLastRemoval();
        if (position >= 0) {
//            getFragment().notifyItemInserted(position);
        }
    }

//    private SetlistFragment getFragment() {
//        return ((SetlistFragment) ((MainActivity) mContext).getFragmentManager().findFragmentByTag(MainActivity.FRAGMENT_LIST_VIEW));
//    }


}
