package net.jonathanwerner.leadsheets.events;

import android.view.MenuItem;

/**
 * Created by jwerner on 3/15/15.
 */
public class ToggleLockMode {
    // the item is needed to have reference to it for changing the toolbar symbol
    public MenuItem item;

    public ToggleLockMode(MenuItem item) {
        this.item = item;
    }
}
