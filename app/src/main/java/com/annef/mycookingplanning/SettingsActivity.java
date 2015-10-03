package com.annef.mycookingplanning;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by annefrancoisemarie on 01/10/15.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

    }

}
