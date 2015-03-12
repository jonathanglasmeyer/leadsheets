package com.example.jwerner.mmd.helpers;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by jwerner on 3/7/15.
 */
public class Resources {
    private final Context mContext;

    public Resources(Context context) {
        mContext = context;
    }

    public int dipToPixel(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, mContext.getResources().getDisplayMetrics());
    }

    public int spToPixel(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, mContext.getResources().getDisplayMetrics());
    }

    public int getColor(int id) {
        return mContext.getResources().getColor(id);
    }


}
