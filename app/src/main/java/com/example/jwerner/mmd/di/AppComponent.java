package com.example.jwerner.mmd.di;

import com.example.jwerner.mmd.components.EditActivity;
import com.example.jwerner.mmd.components.EditFragment;
import com.example.jwerner.mmd.components.detailedsetlist.DetailedSetlistFragment;
import com.example.jwerner.mmd.components.folders.FoldersFragment;
import com.example.jwerner.mmd.components.main.MainActivity;
import com.example.jwerner.mmd.components.setlist.SetlistController;
import com.example.jwerner.mmd.components.setlist.SetlistFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jwerner on 3/13/15.
 */
@Singleton @Component(modules = AppModule.class)
public interface AppComponent {

    void inject(App app);

    void inject(MainActivity activity);

    void inject(EditActivity activity);

    void inject(DetailedSetlistFragment fragment);

    void inject(FoldersFragment fragment);

    void inject(EditFragment fragment);

    void inject(SetlistFragment setlistFragment);

    void inject(SetlistController setlistController);

    /**
     * An initializer that creates the graph from an application.
     */
    final class Initializer {
        private Initializer() {
        } // No instances.

        static AppComponent init(App app) {
            return Dagger_AppComponent.builder()
                    .appModule(new AppModule(app))
                    .build();
        }
    }

}
