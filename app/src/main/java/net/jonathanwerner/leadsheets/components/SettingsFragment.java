package net.jonathanwerner.leadsheets.components;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

import net.jonathanwerner.leadsheets.R;

/**
 * Created by jwerner on 3/19/15.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Resources mResources;
    private String mId_Folder;

    @Override public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        mResources = getActivity().getResources();
        mId_Folder = mResources.getString(R.string.pref_id_folder);

        EditTextPreference editTextPref = (EditTextPreference) findPreference(mId_Folder);
        SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();

        editTextPref.setSummary(sharedPref.getString(mId_Folder, "default"));

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
//        if (key.equals(mId_Folder) {
//            findPreference(key).setSummary(sharedPreferences.getString(key, ""));
//        }

    }
}
