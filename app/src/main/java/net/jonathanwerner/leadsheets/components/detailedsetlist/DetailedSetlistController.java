package net.jonathanwerner.leadsheets.components.detailedsetlist;

import android.content.Context;

import net.jonathanwerner.leadsheets.base.Controller;
import net.jonathanwerner.leadsheets.di.helper.Bus;
import net.jonathanwerner.leadsheets.events.AllSongsChanged;

/**
 * Created by jwerner on 3/6/15.
 */
public class DetailedSetlistController extends Controller {
    private final DetailedSetlistFragment mFragment;

    public DetailedSetlistController(final Context context, final DetailedSetlistFragment fragment) {
        super(context);
        mFragment = fragment;
    }

    @Bus public void onEventMainThread(final AllSongsChanged event) {
        mFragment.refresh();

    }
}
