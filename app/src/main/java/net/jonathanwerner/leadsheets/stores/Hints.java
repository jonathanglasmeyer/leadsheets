package net.jonathanwerner.leadsheets.stores;

import com.google.common.collect.Lists;

import net.jonathanwerner.leadsheets.lib.TinyDB;

import java.util.ArrayList;

import javax.inject.Singleton;

/**
 * Created by jwerner on 3/18/15.
 */
@Singleton public class Hints {
    public static final String HINT = "hint_";
    public static final String SWIPE_SETLIST_ITEM = "swipe_setlist_item";
    public static final String DRAG_SETLIST_ITEM = "drag_setlist_item";
    public static final String ADD_REST_ITEM = "add_rest_item";
    public static final String OPEN_PERFORMANCE_VIEW = "open_performance_view";
    private final ArrayList<String> mHints;
    private TinyDB mTinyDB;

    public Hints(TinyDB tinyDB) {
        mTinyDB = tinyDB;
        mHints = Lists.newArrayList(
                SWIPE_SETLIST_ITEM,
                DRAG_SETLIST_ITEM,
                ADD_REST_ITEM,
                OPEN_PERFORMANCE_VIEW
        );
    }

    public boolean shouldShow(String name) {
        return mTinyDB.getBoolean(HINT + name, true);
    }

    public void setDone(String name) {
        mTinyDB.putBoolean(HINT + name, false);
    }

    private void setNotDone(String name) {
        mTinyDB.putBoolean(HINT + name, true);
    }

    public void reset() {
        for (String hint : mHints) {
            setNotDone(hint);
        }
    }

    public boolean allDone() {
        return !(shouldShow(SWIPE_SETLIST_ITEM) ||
                shouldShow(DRAG_SETLIST_ITEM) ||
                shouldShow(ADD_REST_ITEM) ||
                shouldShow(OPEN_PERFORMANCE_VIEW));
    }

}
