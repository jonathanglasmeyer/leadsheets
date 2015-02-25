package com.example.jwerner.mmd.components.setlist;

import timber.log.Timber;

/**
 * Created by jwerner on 2/25/15.
 */
public interface Foo {
    default void bar() {
        Timber.d("bar: ");

    }
}
