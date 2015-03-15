package net.jonathanwerner.leadsheets.components.setlist;

import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;

import net.jonathanwerner.leadsheets.data.AbstractDataProvider;
import net.jonathanwerner.leadsheets.events.SetlistGeneralClick;
import net.jonathanwerner.leadsheets.events.SetlistReset;
import net.jonathanwerner.leadsheets.widgets.DragSwipeRecyclerAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.event.EventBus;

/**
 * Created by jwerner on 2/9/15.
 */
@Singleton public class SetlistAdapter extends DragSwipeRecyclerAdapter {
    protected boolean mReorderMode = true;
    @Inject SetlistData mSetlistData;

    @Inject public SetlistAdapter(SetlistData setlistData) {
        super(setlistData);
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

