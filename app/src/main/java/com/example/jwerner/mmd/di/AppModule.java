package com.example.jwerner.mmd.di;

/**
 * Created by jwerner on 2/17/15.
 */

import android.app.Application;
import android.content.Context;

import com.example.jwerner.mmd.helpers.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */
@Module(
        includes = {
                DataModule.class
        },
        injects = {
                App.class,
        }
)
public class AppModule {
    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link com.example.jwerner.mmd.di.helper.ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton Application provideApplication() {
        return application;
    }


    @Provides
    @Singleton
    Resources provideResources() {
        return new Resources(application);
    }


}
