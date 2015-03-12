package com.example.jwerner.mmd.data;

/**
 * Created by jwerner on 2/9/15.
 */
public abstract class AbstractDataProvider {

    public abstract int getCount();

    public abstract Data getItem(int index);

    public abstract int removeItem(int position);

    public abstract void moveItem(int fromPosition, int toPosition);

    public abstract void undoLastRemoval();

    public static abstract class Data {
        public abstract long getId();

        public abstract int getViewType();

        public abstract int getSwipeReactionType();

        public abstract String getText();

        public abstract boolean isPinnedToSwipeLeft();

        public abstract void setPinnedToSwipeLeft(boolean pinned);
    }

}
