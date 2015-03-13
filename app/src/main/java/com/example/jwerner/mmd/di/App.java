package com.example.jwerner.mmd.di;

import android.app.Application;
import android.content.Context;

import com.example.jwerner.mmd.BuildConfig;

import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * Created by jwerner on 2/17/15.
 */
public class App extends Application {
    private ObjectGraph graph;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override public void onCreate() {
        super.onCreate();

        graph = ObjectGraph.create(new AppModule(this));


        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    public void inject(Object object) {
        graph.inject(object);
    }

    public ObjectGraph getGraph() {
        return graph;
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
