package net.jonathanwerner.leadsheets.components.setlist;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.transition.Fade;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.common.base.Preconditions;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import net.jonathanwerner.leadsheets.R;
import net.jonathanwerner.leadsheets.base.Controller;
import net.jonathanwerner.leadsheets.components.EditFragment;
import net.jonathanwerner.leadsheets.di.helper.Bus;
import net.jonathanwerner.leadsheets.events.AllSongsChanged;
import net.jonathanwerner.leadsheets.events.SetlistGeneralClick;
import net.jonathanwerner.leadsheets.events.SetlistItemClick;
import net.jonathanwerner.leadsheets.events.SetlistReorder;
import net.jonathanwerner.leadsheets.events.SetlistReset;
import net.jonathanwerner.leadsheets.events.ShowUndoSnackbar;
import net.jonathanwerner.leadsheets.events.SongChanged;
import net.jonathanwerner.leadsheets.events.SongMoved;
import net.jonathanwerner.leadsheets.events.SongRemoved;
import net.jonathanwerner.leadsheets.events.SongRename;
import net.jonathanwerner.leadsheets.events.ToggleAlphabeticMode;
import net.jonathanwerner.leadsheets.events.ToggleLockMode;
import net.jonathanwerner.leadsheets.helpers.Dialog;
import net.jonathanwerner.leadsheets.stores.UIState;

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
        mFragment.getComponent().inject(this);
        Preconditions.checkNotNull(mSetlistData);
        Preconditions.checkNotNull(mSetlistAdapter);
    }

    @Bus public void onEvent(SetlistGeneralClick event) {
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

    @Bus public void onEvent(final SongRename event) {
        Dialog.showInputDialog(mActivityContext, "Rename Song", "Rename", s ->
                mSetlistData.renameItem(event.position, s));
    }

    @Bus public void onEvent(final ToggleLockMode event) {
        mSetlistData.toggleLockMode();
        mFragment.showLockSnackbarHint(UIState.lock);
        mFragment.setLockToggleIcon(event.item);
        mSetlistAdapter.onChangeItem(0); // update the caption at position 0 to show/hide the delete button
        mSetlistAdapter.notifyDataSetChanged(); // all items have to be notified that the click listeners should update
    }

    @Bus public void onEvent(final ToggleAlphabeticMode event) {
        mFragment.switchMode();
        mFragment.setAlphabeticToggleIcon(event.item);
    }

    @Bus public void onEvent(final SongRemove event) {
        mSetlistData.removeItem(event.position);
    }

    @Bus public void onEvent(final SongRemoved event) {
        mSetlistAdapter.notifyItemRemoved(event.position);
    }

    @Bus public void onEvent(final SongMoved event) {
        mSetlistAdapter.notifyItemMoved(event.oldPosition, event.newPosition);
    }

    // TODO: deprecate this because of animation glitches
    @Bus public void onEvent(final AllSongsChanged event) {
        mFragment.refresh();
    }

    @Bus public void onEvent(final SongChanged event) {
        mSetlistAdapter.notifyItemChanged(event.position);
    }

    @Bus public void onEvent(ShowUndoSnackbar event) {
        showUndoSnackbar();
    }

    @Bus public void onEvent(SetlistReorder event) {
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

    @Bus public void onEvent(final SetlistReset event) {
        Timber.d("onEvent: reset");
        Dialog.showQuestionDialog(mActivityContext, "Reset Setlist?", "Reset", new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(final MaterialDialog dialog) {
                mSetlistData.resetSetlist();
                mSetlistAdapter.notifyDataSetChanged();
            }
        });
    }

}