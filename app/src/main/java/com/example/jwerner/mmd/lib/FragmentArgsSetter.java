package com.example.jwerner.mmd.lib;

/**
 * Created by jwerner on 2/21/15.
 */

import android.app.Fragment;

public class FragmentArgsSetter {

    public static <T extends Fragment> T setFragmentArguments(T fragment, FluentBundle fluentBundle) {
        fragment.setArguments(fluentBundle.get());
        return fragment;
    }

    public static <T extends android.support.v4.app.Fragment> T setFragmentArguments(T fragment, FluentBundle fluentBundle) {
        fragment.setArguments(fluentBundle.get());
        return fragment;
    }
}
