package com.example.jwerner.mmd.components.setlist;

import android.net.Uri;

import com.example.jwerner.mmd.data.AbstractDataProvider;
import com.example.jwerner.mmd.data.FileLayer;
import com.example.jwerner.mmd.helpers.Strings;
import com.example.jwerner.mmd.lib.TinyDB;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import timber.log.Timber;

@Singleton public class SetlistData extends AbstractDataProvider {
    public static final String FOLDER = "folder";
    public static final int ITEM_TYPE_CAPTION = 2;
    public static final int ITEM_TYPE_SETLIST = 0;
    public static final int ITEM_TYPE_REST = 1;
    public static final int ITEM_TYPE_ALPHABETICAL = 3;
    public boolean mSortable = true;
    @Inject TinyDB mTinyDB;
    @Inject FileLayer mFileLayer;
    private List<ConcreteData> mData;
    private ConcreteData mLastRemovedData;
    private int mLastRemovedPosition = -1;
    private String mCurrentDir;
    private ArrayList<String> mFileNames;
    private ArrayList<String> mSetlist;

    @Inject public SetlistData() {
//        mCurrentDir = mTinyDB.getString(FOLDER);
//        getFreshData(mCurrentDir);
    }

    public void changeCurrentDirAndRefreshData(final String currentDir) {
        mCurrentDir = currentDir;
        getFreshData(currentDir);
    }

    private void getFreshData(String folderName) {
        mFileNames = mFileLayer.getFilenamesForFolder(mCurrentDir);
        mSetlist = mTinyDB.getList(folderName);
        if (mSetlist.size() == 0) mSetlist = mFileNames;


        mData = new LinkedList<>();
        mData.add(new ConcreteData(mData.size(), 1, ITEM_TYPE_CAPTION, "Setlist"));

        for (String songName : mSetlist) {
            final int id = mData.size();
            final int viewType = 0;
            final String formattedSongName = Strings.capitalize(songName);
            mData.add(new ConcreteData(id, viewType, ITEM_TYPE_SETLIST, formattedSongName));
        }
        mData.add(new ConcreteData(mData.size(), 1, ITEM_TYPE_CAPTION, "All Songs"));

        for (String songName : songsNotInSetlist()) {
            final int viewType = 0;
            final String formattedSongName = Strings.capitalize(songName);
            mData.add(new ConcreteData(mData.size(), viewType, ITEM_TYPE_REST, formattedSongName));
        }

    }

    @DebugLog
    private Iterable<String> songsNotInSetlist() {
        final Iterable<String> setlistLowercase = Iterables.transform(mSetlist, String::toLowerCase);
        return Iterables.filter(mFileNames, fName -> !(Iterables.contains(setlistLowercase, fName.toLowerCase())));
    }

    public void getAlphabeticalData() {
        mFileNames = mFileLayer.getFilenamesForFolder(mCurrentDir);
        mData = new LinkedList<>();
        mData.add(new ConcreteData(mData.size(), 1, ITEM_TYPE_CAPTION, "Songs"));

        for (String songName : mFileNames) {
            final int id = mData.size();
            final int viewType = 0;
            final String formattedSongName = Strings.capitalize(songName);
            final Uri filePath = Uri.fromFile(new File(mFileLayer.getRootPath(), new File(mCurrentDir, songName + ".txt").toString()));
            mData.add(new ConcreteData(id, viewType, ITEM_TYPE_ALPHABETICAL, formattedSongName, filePath));
        }
    }

    public String getCurrentDir() {
        return mCurrentDir;
    }

    public int getNewSetlistItemPos() {
        final int firstIndexNotSetlistItem = Iterables.indexOf(mData.subList(1, mData.size()), input -> input.getItemType() != ITEM_TYPE_SETLIST);
        return Math.max(firstIndexNotSetlistItem + 1, 0);
    }


    public static final class ConcreteData extends Data {
        public static final int REACTION_SETLIST_ITEM = RecyclerViewSwipeManager.REACTION_CAN_SWIPE_RIGHT;
        public static final int REACTION_REST_ITEM = RecyclerViewSwipeManager.REACTION_CAN_NOT_SWIPE_BOTH;
        public static final int REACTION_CAPTION_ITEM = RecyclerViewSwipeManager.REACTION_CAN_NOT_SWIPE_BOTH;
        public static final int REACTION_ALPHABETICAL_ITEM = RecyclerViewSwipeManager.REACTION_CAN_NOT_SWIPE_BOTH;
        private final long mId;
        private final int mViewType;
        private final String mText;
        private Uri mFilePath;
        private int mItemType;
        private int mSwipeReaction;

        public ConcreteData(int id, int viewType, int itemType, String text) {
            this(id, viewType, itemType, text, null);
        }

        public ConcreteData(int id, int viewType, int itemType, String text, Uri filePath) {
            if (itemType == ITEM_TYPE_CAPTION) {
                mSwipeReaction = REACTION_CAPTION_ITEM;
            } else if (itemType == ITEM_TYPE_REST) {
                mSwipeReaction = REACTION_REST_ITEM;
            } else if (itemType == ITEM_TYPE_ALPHABETICAL) {
                mSwipeReaction = REACTION_ALPHABETICAL_ITEM;
            } else {
                mSwipeReaction = REACTION_SETLIST_ITEM;
            }

            mId = id;
            mViewType = viewType;
            mItemType = itemType;
            mText = text;
            mFilePath = filePath;
        }

        public Uri getFilePath() {
            return mFilePath;
        }

        public int getItemType() {
            return mItemType;
        }

        public void convertToSetlistItem() {
            mItemType = ITEM_TYPE_SETLIST;
            mSwipeReaction = REACTION_SETLIST_ITEM;
        }

        public void convertToRestItem() {
            mItemType = ITEM_TYPE_REST;
            mSwipeReaction = REACTION_REST_ITEM;
        }

        @Override
        public long getId() {
            return mId;
        }

        @Override
        public int getViewType() {
            return mViewType;
        }

        @Override
        public int getSwipeReactionType() {
            return mSwipeReaction;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public boolean isPinnedToSwipeLeft() {
            return false;
        }

        @Override
        public void setPinnedToSwipeLeft(boolean pinned) {

        }
    }

    private void backupSetlist() {
        mTinyDB.putList(mCurrentDir, Lists.newArrayList(getSetlistFromData()));
    }


    public FluentIterable<String> getSetlistFromData() {
        return FluentIterable.from(mData)
                .filter(input -> input.mItemType == ITEM_TYPE_SETLIST)
                .transform(ConcreteData::getText);
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Data getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }
        return mData.get(index);
    }

    @Override @DebugLog
    public int removeItem(int position) {
//        mLastRemovedData = mData.remove(position);
        final ConcreteData item = mData.get(position);
        item.convertToRestItem();
        mData.set(position, item);
        Timber.d("removeItem: " + mData.get(position).getItemType());
        mData.remove(position);
        final int newPosition = getRestInsertPos(item.getText());
        mData.add(newPosition, item);
        backupSetlist();
        return newPosition;
    }

    public int getRestInsertPos(final String itemText) {
        final int firstIndexBiggerWord = Iterables.indexOf(mData, input ->
                input.getItemType() == ITEM_TYPE_REST && input.getText().compareTo(itemText) > 0);
        return firstIndexBiggerWord > 0 ? firstIndexBiggerWord : mData.size();
    }

    @Override
    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        if (((ConcreteData) getItem(toPosition)).getItemType() == ITEM_TYPE_CAPTION) {
            Timber.d("moveItem: shouldn't happen");
        }

        final ConcreteData item = mData.remove(fromPosition);

        if (item.getItemType() == ITEM_TYPE_REST) {
            item.convertToSetlistItem();
        }


        mData.add(toPosition, item);
        mLastRemovedPosition = -1;
        backupSetlist();

    }

    @Override
    public int undoLastRemoval() {
        if (mLastRemovedData != null) {
            int insertedPosition;
            if (mLastRemovedPosition >= 0 && mLastRemovedPosition < mData.size()) {
                insertedPosition = mLastRemovedPosition;
            } else {
                insertedPosition = mData.size();
            }
            mData.add(insertedPosition, mLastRemovedData);
            mLastRemovedData = null;
            mLastRemovedPosition = -1;
            backupSetlist();
            return insertedPosition;
        } else {
            return -1;
        }

    }


}
