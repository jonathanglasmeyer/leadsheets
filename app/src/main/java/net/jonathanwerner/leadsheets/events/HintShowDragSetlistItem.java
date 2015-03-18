package net.jonathanwerner.leadsheets.events;

/**
 * Created by jwerner on 3/18/15.
 */
public class HintShowDragSetlistItem implements Hint {
    public String message;

    public HintShowDragSetlistItem(final String message) {
        this.message = message;
    }
}
