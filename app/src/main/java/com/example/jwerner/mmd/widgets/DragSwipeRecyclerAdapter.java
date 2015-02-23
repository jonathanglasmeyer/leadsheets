package com.example.jwerner.mmd.widgets;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jwerner.mmd.R;
import com.example.jwerner.mmd.data.AbstractDataProvider;
import com.example.jwerner.mmd.lib.ViewUtils;
import com.google.common.base.Preconditions;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;

/**
 * Created by jwerner on 2/18/15.
 */
public abstract class DragSwipeRecyclerAdapter
        extends RecyclerView.Adapter<DragSwipeRecyclerAdapter.MyViewHolder>
        implements DraggableItemAdapter<DragSwipeRecyclerAdapter.MyViewHolder>,
        SwipeableItemAdapter<DragSwipeRecyclerAdapter.MyViewHolder> {

    private AbstractDataProvider mDataProvider;

    protected DragSwipeRecyclerAdapter(AbstractDataProvider dataProvider) {
        mDataProvider = dataProvider;
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate((viewType == 0) ? R.layout.list_item : R.layout.list_item_caption, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AbstractDataProvider.Data item = mDataProvider.getItem(position);


        holder.mContainer.setOnClickListener(v -> handleClick(holder.getPosition()));

        // set text
        holder.mTextView.setText(item.getText());

        if (itemCanBeDragged(item)) {
            holder.mDragHandle.setVisibility(View.VISIBLE);
        } else {
            if (holder.mDragHandle != null) {
                holder.mDragHandle.setVisibility(View.GONE);
            }
        }

        // set background resource (target view ID: container);
        final int dragState = holder.getDragStateFlags();
        final int swipeState = holder.getSwipeStateFlags();

        if (((dragState & RecyclerViewDragDropManager.STATE_FLAG_IS_UPDATED) != 0) ||
                ((swipeState & RecyclerViewDragDropManager.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;

            if ((dragState & RecyclerViewDragDropManager.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_dragging_active_state;
            } else if ((dragState & RecyclerViewDragDropManager.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else if ((swipeState & RecyclerViewSwipeManager.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_swiping_active_state;
            } else if ((swipeState & RecyclerViewSwipeManager.STATE_FLAG_SWIPING) != 0) {
                bgResId = R.drawable.bg_item_swiping_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            holder.mContainer.setBackgroundResource(bgResId);
        }

        // set swiping properties
        holder.setSwipeItemSlideAmount(
                item.isPinnedToSwipeLeft() ? RecyclerViewSwipeManager.OUTSIDE_OF_THE_WINDOW_LEFT : 0);

    }

    protected abstract void handleClick(int position);

    protected abstract boolean itemCanBeDragged(final AbstractDataProvider.Data item);

    @Override
    public int getItemViewType(final int position) {
        return mDataProvider.getItem(position).getViewType();
    }

    @Override
    public long getItemId(final int position) {
        return mDataProvider.getItem(position).getId();
    }

    @Override
    public int getItemCount() {
        return mDataProvider.getCount();
    }

    @Override
    public int onGetSwipeReactionType(MyViewHolder holder, int x, int y) {
        if (onCheckCanStartDrag(holder, x, y)) {
            return RecyclerViewSwipeManager.REACTION_CAN_NOT_SWIPE_RIGHT;
        } else {
            return mDataProvider.getItem(holder.getPosition()).getSwipeReactionType();
        }
    }

    @Override
    public boolean onCheckCanStartDrag(final MyViewHolder holder, final int x, final int y) {
        if (!itemCanBeDragged(mDataProvider.getItem(holder.getPosition()))) {
            // for captions
            return false;
        }
        // x, y --- relative from the itemView's top-left
        final ViewGroup containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return canStartDrag() && ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    protected abstract boolean canStartDrag();

    @Override
    public void onMoveItem(final int fromPosition, final int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        mDataProvider.moveItem(fromPosition, toPosition);
        notifyItemChanged(fromPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSetSwipeBackground(MyViewHolder holder, int type) {
        int bgRes = 0;
        switch (type) {
            case RecyclerViewSwipeManager.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_neutral;
                break;
            case RecyclerViewSwipeManager.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_left;
                break;
            case RecyclerViewSwipeManager.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_right;
                break;
        }

        holder.itemView.setBackgroundResource(bgRes);
    }

    @Override
    public int onSwipeItem(MyViewHolder holder, int result) {
//        Log.d(TAG, "onSwipeItem(result = " + result + ")");

        switch (result) {
            // swipe right --- remove
            case RecyclerViewSwipeManager.RESULT_SWIPED_RIGHT:
                return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM;
            // swipe left -- pin
            case RecyclerViewSwipeManager.RESULT_SWIPED_LEFT:
                return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_MOVE_TO_SWIPED_DIRECTION;
            // other --- do nothing
            case RecyclerViewSwipeManager.RESULT_CANCELED:
            default:
                return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_DEFAULT;
        }
    }

//    protected abstract void handleRemove(final int position);

    @Override
    public void onPerformAfterSwipeReaction(MyViewHolder holder, int result, int reaction) {
//        Log.d(TAG, "onPerformAfterSwipeReaction(result = " + result + ", reaction = " + reaction + ")");

        final int position = holder.getPosition();

        if (reaction == RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM) {
            int newPos = mDataProvider.removeItem(position);
            notifyDataSetChanged();

//            handleRemove(position);
        }
    }

    // hassssssle. for the controller.
    public void onItemRemove(int position) {
        notifyItemRemoved(position);

    }

    public static class MyViewHolder extends AbstractDraggableSwipeableItemViewHolder {

        public ViewGroup mContainer;
        public View mDragHandle;
        public TextView mTextView;

        public MyViewHolder(final View v) {
            super(v);
            mContainer = (ViewGroup) v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.drag_handle);
            mTextView = (TextView) v.findViewById(R.id.list_item_text);
            Preconditions.checkNotNull(mContainer, "mContainer is NULL");
            Preconditions.checkNotNull(mTextView, "mTextView is NULL");

        }


        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }
    }
}

