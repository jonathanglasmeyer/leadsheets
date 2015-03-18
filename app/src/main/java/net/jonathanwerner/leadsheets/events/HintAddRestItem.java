package net.jonathanwerner.leadsheets.events;

/**
 * Created by jwerner on 3/18/15.
 */
public class HintAddRestItem implements Hint {
    public String message;

    public HintAddRestItem(final String message) {

        this.message = message;
    }
}
