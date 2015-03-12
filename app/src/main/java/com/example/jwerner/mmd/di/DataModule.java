package com.example.jwerner.mmd.di;

import android.app.Application;

import com.example.jwerner.mmd.components.EditActivity;
import com.example.jwerner.mmd.components.EditFragment;
import com.example.jwerner.mmd.components.detailedsetlist.DetailedSetlistController;
import com.example.jwerner.mmd.components.detailedsetlist.DetailedSetlistFragment;
import com.example.jwerner.mmd.components.folders.FoldersController;
import com.example.jwerner.mmd.components.folders.FoldersFragment;
import com.example.jwerner.mmd.components.main.MainActivity;
import com.example.jwerner.mmd.components.main.MainController;
import com.example.jwerner.mmd.components.main.ToolbarController;
import com.example.jwerner.mmd.components.setlist.SetlistController;
import com.example.jwerner.mmd.components.setlist.SetlistData;
import com.example.jwerner.mmd.components.setlist.SetlistFragment;
import com.example.jwerner.mmd.lib.TinyDB;
import com.example.jwerner.mmd.stores.FileStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MainActivity.class,
                MainController.class,
                ToolbarController.class,
                SetlistData.class,
                FileStore.class,
                SetlistFragment.class,
                SetlistController.class,
                DetailedSetlistFragment.class,
                DetailedSetlistController.class,
                FoldersFragment.class,
                FoldersController.class,
                EditFragment.class,
                EditActivity.class,
        },
        complete = false,
        library = false
)
public class DataModule {

    @Provides @Singleton TinyDB provideTinyDB(Application app) {
        return new TinyDB(app);
    }


}
