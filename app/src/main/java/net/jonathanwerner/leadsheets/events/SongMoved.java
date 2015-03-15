package net.jonathanwerner.leadsheets.events;

/**
 * Created by jwerner on 3/15/15.
 */
public class SongMoved {
    public int oldPosition;
    public int newPosition;

    public SongMoved(int oldPosition, int newPosition) {
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
    }
}
