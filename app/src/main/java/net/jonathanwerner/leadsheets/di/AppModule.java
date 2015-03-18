package net.jonathanwerner.leadsheets.di;

/**
 * Created by jwerner on 2/17/15.
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.jonathanwerner.leadsheets.di.helper.ForApplication;
import net.jonathanwerner.leadsheets.helpers.Dialog;
import net.jonathanwerner.leadsheets.helpers.Resources;
import net.jonathanwerner.leadsheets.lib.TinyDB;
import net.jonathanwerner.leadsheets.stores.Hints;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */
@Module
public class AppModule {
    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton Application provideApplication() {
        return application;
    }

    @Provides @Singleton Resources provideResources() {
        return new Resources(application);
    }

    @Provides @Singleton TinyDB provideTinyDB(Application app) {
        return new TinyDB(app);
    }

    @Provides @Singleton Dialog provideDialog(Resources resources) {
        return new Dialog(resources);
    }

    @Provides @Singleton Hints provideHints(TinyDB tinyDB) {
        return new Hints(tinyDB);
    }

    @Provides @Singleton SharedPreferences providePreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
