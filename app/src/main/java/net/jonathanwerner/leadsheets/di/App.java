package net.jonathanwerner.leadsheets.di;

import android.app.Application;
import android.content.Context;

import net.jonathanwerner.leadsheets.BuildConfig;
import net.jonathanwerner.leadsheets.lib.TinyDB;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by jwerner on 2/17/15.
 */
public class App extends Application {
    @Inject TinyDB mtinydb;
    private AppComponent component;
    //    private ObjectGraph graph;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override public void onCreate() {
        super.onCreate();
        setupTimber();

        buildComponentAndInject();
        Timber.d("oncreate" + mtinydb);

    }

    private void buildComponentAndInject() {
        component = AppComponent.Initializer.init(this);
        component.inject(this);
    }

    public AppComponent component() {
        return component;
    }

    private void setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }


    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.HollowTree {
        @Override public void i(String message, Object... args) {
            // TODO e.g., Crashlytics.log(String.format(message, args));
        }

        @Override public void i(Throwable t, String message, Object... args) {
            i(message, args); // Just add to the log.
        }

        @Override public void e(String message, Object... args) {
            i("ERROR: " + message, args); // Just add to the log.
        }

        @Override public void e(Throwable t, String message, Object... args) {
            e(message, args);

            // TODO e.g., Crashlytics.logException(t);
        }
    }
}
