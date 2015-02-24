package com.example.jwerner.mmd.stores;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jwerner on 2/24/15.
 */
@Singleton public class UIState {

    public static final String SETLIST = "Setlist" ;
    private boolean dragMode;

    @Inject public UIState() {
       dragMode = false;
    }
}
