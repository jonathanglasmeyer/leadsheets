package net.jonathanwerner.leadsheets.stores;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jwerner on 2/24/15.
 */
@Singleton public class UIState {

    public static final String SETLIST = "Setlist" ;
    public static final String EDIT = "Edit";
    public static final String FOLDERS = "Folders";
    public static boolean alphabetMode = false;
    public static boolean lock;
    private final boolean dragMode;

    @Inject public UIState() {
       dragMode = false;
    }

    public static void setLock(boolean b) {
        lock = b;
    }
}
