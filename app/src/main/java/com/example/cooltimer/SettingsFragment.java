package com.example.cooltimer;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

/*Наследуемся от PreferenceFragmentCompat, который был добавлен в
    Gradle Scripts/build.gradle : implementation 'androidx.preference:preference:1.2.0'*/
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        //Ссылаемся на созданную разметку
        addPreferencesFromResource(R.xml.timer_preferences);
    }
}
