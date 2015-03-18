package net.jonathanwerner.leadsheets.helpers;

import android.content.Context;
import android.util.TypedValue;

import javax.inject.Singleton;

/**
 * Created by jwerner on 3/7/15.
 */
@Singleton public class Resources {
    private final Context mAppContext;

    public Resources(Context appContext) {
        mAppContext = appContext;
    }

    public int dipToPixel(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, mAppContext.getResources().getDisplayMetrics());
    }

    public int spToPixel(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, mAppContext.getResources().getDisplayMetrics());
    }

    public int getColor(int id) {
        return mAppContext.getResources().getColor(id);
    }


    public String getString(int id) {
        return mAppContext.getString(id);
    }


}
