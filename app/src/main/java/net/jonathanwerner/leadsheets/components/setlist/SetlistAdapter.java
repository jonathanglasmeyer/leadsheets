package net.jonathanwerner.leadsheets.components.setlist;

import android.os.Handler;

import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;

import net.jonathanwerner.leadsheets.R;
import net.jonathanwerner.leadsheets.data.AbstractDataProvider;
import net.jonathanwerner.leadsheets.events.Hint;
import net.jonathanwerner.leadsheets.events.HintAddRestItem;
import net.jonathanwerner.leadsheets.events.HintShowDragSetlistItem;
import net.jonathanwerner.leadsheets.events.HintShowSwipeSetlistItem;
import net.jonathanwerner.leadsheets.events.SetlistGeneralClick;
import net.jonathanwerner.leadsheets.events.SetlistReset;
import net.jonathanwerner.leadsheets.helpers.Resources;
import net.jonathanwerner.leadsheets.stores.Hints;
import net.jonathanwerner.leadsheets.widgets.DragSwipeRecyclerAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by jwerner on 2/9/15.
 */
@Singleton public class SetlistAdapter extends DragSwipeRecyclerAdapter {
    protected boolean mReorderMode = true;
    @Inject SetlistData mSetlistData;
    @Inject Hints mHints;
    @Inject Resources mResources;

    @Inject public SetlistAdapter(SetlistData setlistData) {
        super(setlistData);
    }

    public void showTooltips() {
        Timber.d("showTooltips: ");
        Timber.d("getitemcount: " + getItemCount());
        Timber.d("setlist size: " + mSetlistData.getSetlistFromData().size());
        Timber.d("should show add_rest_item: " + mHints.shouldShow(Hints.ADD_REST_ITEM));
        Timber.d("should show swipe: " + mHints.shouldShow(Hints.SWIPE_SETLIST_ITEM));
        Timber.d("should show drag: " + mHints.shouldShow(Hints.DRAG_SETLIST_ITEM));

        if (mHints.shouldShow(Hints.ADD_REST_ITEM) && getItemCount() > 2) {
            Timber.d("showTooltips: should show ADDRESTITEM");
            mHints.setDone(Hints.ADD_REST_ITEM);
            postHint(new HintAddRestItem(mResources.getString(R.string.hint_add_rest_item)));
        }

        if (mHints.shouldShow(Hints.SWIPE_SETLIST_ITEM) && mSetlistData.getSetlistFromData().size() == 1) {
            Timber.d("showTooltips: should show swipeSetlistItem");
            mHints.setDone(Hints.SWIPE_SETLIST_ITEM);
            postHint(new HintShowSwipeSetlistItem(mResources.getString(R.string.hint_swipe_setlist_item)));
        }

        if (mHints.shouldShow(Hints.DRAG_SETLIST_ITEM) && mSetlistData.getSetlistFromData().size() == 2) {
            Timber.d("showTooltips: should show dragsetlistitem");
            mHints.setDone(Hints.DRAG_SETLIST_ITEM);
            postHint(new HintShowDragSetlistItem(mResources.getString(R.string.hint_drag_setlist_item)));
        }
    }

    private void postHint(Hint hint) {
        new Handler().postDelayed(() -> {
            EventBus.getDefault().post(hint);
        }, 500);

    }

    public void setReorderMode(boolean b) {
        mReorderMode = b;
        notifyDataSetChanged();
    }


//    @Override protected void handleLongClick(final int position, final View v) {
//        EventBus.getDefault().post(new SongLongClick(position, v));
//
//    }

    @Override
    protected void handleReset() {
        EventBus.getDefault().post(new SetlistReset());
    }

    @Override protected void handleClick(int position) {
        EventBus.getDefault().post(new SetlistGeneralClick(position));
    }

    @Override protected boolean itemCanBeDragged(final AbstractDataProvider.Data item) {
        final SetlistData.ConcreteData item1 = (SetlistData.ConcreteData) item;
        return item1.getItemType() == SetlistData.ITEM_TYPE_SETLIST;
    }

    @Override
    protected boolean itemHasNumber(final AbstractDataProvider.Data item) {
        return ((SetlistData.ConcreteData) item).getItemType() == SetlistData.ITEM_TYPE_SETLIST;

    }

//    @Override protected void handleRemove(final int position) {
//        EventBus.getDefault().post(new SetlistRemove(position));
//    }

    @Override protected boolean canStartDrag() {
        return mSetlistData.mSortable;
    }

    @Override protected void handleSwipeRight(int position) {
        EventBus.getDefault().post(new SongRemove(position));
    }

    @Override public ItemDraggableRange onGetItemDraggableRange(final MyViewHolder myViewHolder) {
        return new ItemDraggableRange(1, mSetlistData.getNewSetlistItemPos() - 1);

    }

}

