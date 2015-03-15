package net.jonathanwerner.leadsheets.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import net.jonathanwerner.leadsheets.di.App;
import net.jonathanwerner.leadsheets.di.AppComponent;

/**
 * Created by jwerner on 2/17/15.
 */
public abstract class BaseActivity extends ActionBarActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        App app = App.get(this);
        onCreateComponent(app.component());

        // this has to happen after the fragment injection
        setController();
    }

    public void setController() {
    }

    @Override public void onDestroy() {
        super.onDestroy();
        final Controller controller = getController();
        if (controller != null) {
            controller.unregister();
        }
    }

    public Controller getController() {
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Controller controller = getController();
        if (controller != null) {
            controller.register();
        }
    }


    protected abstract void onCreateComponent(AppComponent appComponent);

}
