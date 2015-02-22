package com.example.jwerner.mmd.base;

import android.app.Fragment;
import android.os.Bundle;

import com.example.jwerner.mmd.di.App;

/**
 * Created by jwerner on 2/20/15.
 */
public class BaseFragment extends Fragment {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Perform injection so that when this call returns all dependencies will be available for use.
        ((App) getActivity().getApplication()).inject(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }
}
