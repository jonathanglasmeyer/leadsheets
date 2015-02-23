package com.example.jwerner.mmd.di;

import android.app.Application;

import com.example.jwerner.mmd.components.detailedsetlist.DetailedSetlistFragment;
import com.example.jwerner.mmd.components.main.MainActivity;
import com.example.jwerner.mmd.components.main.MainController;
import com.example.jwerner.mmd.components.main.ToolbarController;
import com.example.jwerner.mmd.components.setlist.SetlistController;
import com.example.jwerner.mmd.components.setlist.SetlistData;
import com.example.jwerner.mmd.components.setlist.SetlistFragment;
import com.example.jwerner.mmd.data.FileLayer;
import com.example.jwerner.mmd.lib.TinyDB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MainActivity.class,
                MainController.class,
                ToolbarController.class,
                SetlistData.class,
                FileLayer.class,
                SetlistFragment.class,
                SetlistController.class,
                DetailedSetlistFragment.class
        },
        complete = false,
        library = false
)
public class DataModule {

    @Provides @Singleton TinyDB provideTinyDB(Application app) {
        return new TinyDB(app);
    }


}
