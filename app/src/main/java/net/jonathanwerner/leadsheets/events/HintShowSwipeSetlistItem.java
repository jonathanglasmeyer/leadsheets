package net.jonathanwerner.leadsheets.events;

/**
 * Created by jwerner on 3/17/15.
 */
public class HintShowSwipeSetlistItem implements Hint {
    public String message;

    public HintShowSwipeSetlistItem(String message) {
        this.message = message;
    }
}
