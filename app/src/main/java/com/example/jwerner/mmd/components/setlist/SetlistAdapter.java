package com.example.jwerner.mmd.components.setlist;

import com.example.jwerner.mmd.data.AbstractDataProvider;
import com.example.jwerner.mmd.events.SetlistGeneralClick;
import com.example.jwerner.mmd.widgets.DragSwipeRecyclerAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.event.EventBus;

/**
 * Created by jwerner on 2/9/15.
 */
@Singleton public class SetlistAdapter extends DragSwipeRecyclerAdapter {
    private SetlistData mSetlistData;

    @Inject public SetlistAdapter(SetlistData setlistData) {
        super(setlistData);

        mSetlistData = setlistData;
    }

    @Override protected void handleClick(int position) {
        EventBus.getDefault().post(new SetlistGeneralClick(position));
    }

    @Override protected boolean itemCanBeDragged(final AbstractDataProvider.Data item) {
        return ((SetlistData.ConcreteData) item).getItemType() == SetlistData.ITEM_TYPE_SETLIST;
    }


//    @Override protected void handleRemove(final int position) {
//        EventBus.getDefault().post(new SetlistRemove(position));
//    }

    @Override protected boolean canStartDrag() {
        return mSetlistData.mSortable;
    }
}
