package com.example.jwerner.mmd.components.setlist;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.transition.Fade;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jwerner.mmd.R;
import com.example.jwerner.mmd.base.Controller;
import com.example.jwerner.mmd.components.EditFragment;
import com.example.jwerner.mmd.events.ChangeContent;
import com.example.jwerner.mmd.events.SetlistGeneralClick;
import com.example.jwerner.mmd.events.SetlistItemClick;
import com.example.jwerner.mmd.events.SetlistReorder;
import com.example.jwerner.mmd.events.SetlistReset;
import com.example.jwerner.mmd.events.ShowUndoSnackbar;
import com.example.jwerner.mmd.helpers.Dialog;
import com.example.jwerner.mmd.stores.UIState;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;
import timber.log.Timber;

public class SetlistController extends Controller {

    private final SetlistFragment mFragment;
    private final Context mActivityContext;
    @Inject SetlistData mSetlistData;
    @Inject SetlistAdapter mSetlistAdapter;

    @DebugLog
    public SetlistController(SetlistFragment fragment, Context activityContext) {
        super(activityContext);
        mFragment = fragment;
        mActivityContext = activityContext;
    }

    public void onEvent(SetlistGeneralClick event) {
        final int position = event.position;
        final SetlistData.ConcreteData item = (SetlistData.ConcreteData) mSetlistData.getItem(position);
        final int itemType = item.getItemType();
        switch (itemType) {

            case SetlistData.ITEM_TYPE_REST:
                if (!UIState.lock) {
                    mSetlistAdapter.onMoveItem(position, mSetlistData.getNewSetlistItemPos()); // notify adapter that list has changed
                }
                break;

            case SetlistData.ITEM_TYPE_SETLIST:
                EventBus.getDefault().post(new SetlistItemClick(position));
                break;

            case SetlistData.ITEM_TYPE_ALPHABETICAL:
                final EditFragment fragment = EditFragment.newInstance(item.getFilePath());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    fragment.setEnterTransition(new Fade());
                }
                ((Activity) mActivityContext).getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(UIState.EDIT)
                        .commit();
        }
    }

    public void onEvent(final ChangeContent event) {
        mFragment.refresh();
    }

    public void onEvent(ShowUndoSnackbar event) {
        showUndoSnackbar();
    }

    public void onEvent(SetlistReorder event) {
        mSetlistAdapter.setReorderMode(event.reorderMode);
    }

    private void showUndoSnackbar() {
        SnackbarManager.show(
                Snackbar.with(mActivityContext.getApplicationContext())
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
                , (android.app.Activity) mActivityContext);
    }

    private void onItemUndoActionClicked() {
        mSetlistData.undoLastRemoval();
//        if (position >= 0) {
//            mSetlistAdapter.notifyItemInserted(position);
//        }
    }

    public void onEvent(final SetlistReset event) {
        Timber.d("onEvent: reset");
        Dialog.showQuestionDialog(mActivityContext, "Reset Setlist?", "Reset", new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(final MaterialDialog dialog) {
                mSetlistData.resetSetlist();
                mSetlistAdapter.notifyDataSetChanged();
            }
        });
    }

//    private SetlistFragment getFragment() {
//        return ((SetlistFragment) ((MainActivity) mActivityContext).getFragmentManager().findFragmentByTag(MainActivity.FRAGMENT_LIST_VIEW));
//    }


}
