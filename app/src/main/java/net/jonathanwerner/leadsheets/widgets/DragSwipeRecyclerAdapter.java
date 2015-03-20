package net.jonathanwerner.leadsheets.widgets;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.common.base.Preconditions;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;

import net.jonathanwerner.leadsheets.R;
import net.jonathanwerner.leadsheets.data.AbstractDataProvider;
import net.jonathanwerner.leadsheets.helpers.Resources;
import net.jonathanwerner.leadsheets.stores.UIState;

import javax.inject.Inject;

/**
 * Created by jwerner on 2/18/15.
 */
public abstract class DragSwipeRecyclerAdapter
        extends RecyclerView.Adapter<DragSwipeRecyclerAdapter.MyViewHolder>
        implements DraggableItemAdapter<DragSwipeRecyclerAdapter.MyViewHolder>,
        SwipeableItemAdapter<DragSwipeRecyclerAdapter.MyViewHolder> {

    private final AbstractDataProvider mDataProvider;
    public int mLastLongClickedPosition;
    @Inject
    Resources mResources;
    private boolean mDragMode = false;

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

        if (itemCanBeDragged(item)) {
            if (UIState.lock) {
                // for the normally draggable items, just have no long click action in case of lock mode
                holder.mContainer.setOnLongClickListener(v -> true);
            } else {

                holder.mContainer.setOnLongClickListener(v -> {
                    mDragMode = true;
                    int bgResId = R.drawable.bg_item_dragging_active_state;
                    holder.mContainer.setBackgroundResource(bgResId);
                    return true;
                });

                holder.mContainer.setOnTouchListener((v, event) -> {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            mDragMode = false;
                            int bgResId = R.drawable.bg_item_normal_state;
                            holder.mContainer.setBackgroundResource(bgResId);
                            break;
                    }
                    return false;
                });
            }
        } else {
            // eat the long click for the non-draggable items
            holder.mContainer.setOnLongClickListener(v -> {
                mLastLongClickedPosition = holder.getPosition();
                return false;
            });
        }

        // set text
        holder.mTextView.setText(item.getText());

        // setlist caption
        if (item.getText().equals("Setlist") && holder.mDeleteButtonWrap != null) {
            if (!UIState.lock) {
                holder.mDeleteButtonWrap.setVisibility(View.VISIBLE);
                holder.mDeleteButton.setOnClickListener(v -> handleReset());
            }
            if (UIState.lock) {
                holder.mLockHint.setVisibility(View.VISIBLE);
                holder.mDeleteButtonWrap.setVisibility(View.GONE);
            } else {
                holder.mLockHint.setVisibility(View.GONE);
            }

            if (hideDeleteButton()) {
                holder.mDeleteButtonWrap.setVisibility(View.GONE);
            } else {
                holder.mDeleteButtonWrap.setVisibility(View.VISIBLE);
            }

        } else if (holder.mDeleteButtonWrap != null) {
            holder.mDeleteButtonWrap.setVisibility(View.GONE);
            holder.mDeleteButton.setOnClickListener(null);
            holder.mLockHint.setVisibility(View.GONE);
        }

        if (holder.mImage != null && itemHasNumber(item)) {
            holder.mImageWrap.setVisibility(View.VISIBLE);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.argb(200, 255, 255, 255))
                    .useFont(Typeface.DEFAULT)
                    .fontSize(mResources.spToPixel(20))
                    .bold()
                    .endConfig()
                    .buildRound(position + "", mResources.getColor(R.color.md_indigo_300));
            holder.mImage.setImageDrawable(drawable);
        }
        if (!itemHasNumber(item)) {
            if (holder.mImage != null) {
                holder.mImageWrap.setVisibility(View.GONE);
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

    protected abstract boolean hideDeleteButton();

    protected abstract void handleReset();

    protected abstract void handleClick(int position);

    protected abstract boolean itemCanBeDragged(final AbstractDataProvider.Data item);

    protected abstract boolean itemHasNumber(final AbstractDataProvider.Data item);

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

    public AbstractDataProvider.Data getItem(int position) {
        return mDataProvider.getItem(position);
    }

    @Override
    public int onGetSwipeReactionType(MyViewHolder holder, int x, int y) {
        if (onCheckCanStartDrag(holder, x, y)) {
            return RecyclerViewSwipeManager.REACTION_CAN_NOT_SWIPE_RIGHT;
        } else {
            if (UIState.lock && itemCanBeDragged(getItem(holder.getPosition())))
                return RecyclerViewSwipeManager.REACTION_CAN_NOT_SWIPE_RIGHT_WITH_RUBBER_BAND_EFFECT;
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

        return mDragMode;
//        return canStartDrag() && ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
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

    public void onChangeItem(int position) {
        notifyItemChanged(position);
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

        final int position = holder.getPosition();

        if (reaction == RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM) {
            handleSwipeRight(position);
//            mDataProvider.removeItem(position);
//
        }
    }

    protected abstract void handleSwipeRight(int position);


    public static class MyViewHolder extends AbstractDraggableSwipeableItemViewHolder {

        public final ViewGroup mContainerOuter;
        public final ViewGroup mContainer;
        public final View mDragHandle;
        public final ImageView mImage;
        public final ViewGroup mImageWrap;
        public final TextView mTextView;
        public final ViewGroup mDeleteButtonWrap;
        public final ImageButton mDeleteButton;
        public final ImageView mLockHint;

        public MyViewHolder(final View v) {
            super(v);
            // this is the viewholder for the normal items AND the caption items
            // that means that image & draghandle might be null
            mContainer = (ViewGroup) v.findViewById(R.id.container);
            mContainerOuter = (ViewGroup) v.findViewById(R.id.container_outer);
            mImage = (ImageView) v.findViewById(R.id.image_number);
            mImageWrap = (ViewGroup) v.findViewById(R.id.image_wrap);
            mDragHandle = v.findViewById(R.id.drag_handle);
            mTextView = (TextView) v.findViewById(R.id.list_item_text);
            mDeleteButtonWrap = (ViewGroup) v.findViewById(R.id.delete_button_wrap);
            mDeleteButton = (ImageButton) v.findViewById(R.id.delete_button);
            mLockHint = (ImageView) v.findViewById(R.id.lock_hint);

            Preconditions.checkNotNull(mContainer, "mContainer is NULL");
            Preconditions.checkNotNull(mTextView, "mTextView is NULL");

        }


        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }
    }

}

