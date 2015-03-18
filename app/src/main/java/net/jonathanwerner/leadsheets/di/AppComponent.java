package net.jonathanwerner.leadsheets.di;

import net.jonathanwerner.leadsheets.components.EditActivity;
import net.jonathanwerner.leadsheets.components.EditFragment;
import net.jonathanwerner.leadsheets.components.detailedsetlist.DetailedSetlistFragment;
import net.jonathanwerner.leadsheets.components.folders.FoldersController;
import net.jonathanwerner.leadsheets.components.folders.FoldersFragment;
import net.jonathanwerner.leadsheets.components.main.MainActivity;
import net.jonathanwerner.leadsheets.components.setlist.SetlistController;
import net.jonathanwerner.leadsheets.components.setlist.SetlistFragment;

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

    void inject(FoldersController foldersController);

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
