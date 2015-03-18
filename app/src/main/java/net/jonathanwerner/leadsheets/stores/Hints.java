package net.jonathanwerner.leadsheets.stores;

import net.jonathanwerner.leadsheets.lib.TinyDB;

import javax.inject.Singleton;

/**
 * Created by jwerner on 3/18/15.
 */
@Singleton public class Hints {
    public static final String HINT = "hint_";
    public static final String SWIPE_SETLIST_ITEM = "swipe_setlist_item";
    public static final String DRAG_SETLIST_ITEM = "drag_setlist_item";
    public static final String ADD_REST_ITEM = "add_rest_item";
    private TinyDB mTinyDB;

    public Hints(TinyDB tinyDB) {
        mTinyDB = tinyDB;
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
        setNotDone(SWIPE_SETLIST_ITEM);
        setNotDone(DRAG_SETLIST_ITEM);
        setNotDone(ADD_REST_ITEM);
    }

}
