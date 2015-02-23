package com.example.jwerner.mmd.di;

import android.app.Application;
import android.content.Context;

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
        Timber.plant(new Timber.DebugTree());


    }

    public void inject(Object object) {
        graph.inject(object);
    }

    public ObjectGraph getGraph() {
        return graph;
    }
}
