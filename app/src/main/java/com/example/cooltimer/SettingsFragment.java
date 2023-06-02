package com.example.cooltimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceScreen;
//import androidx.preference.CheckBoxPreference;
//import androidx.preference.ListPreference;
//import androidx.preference.Preference;
//import androidx.preference.PreferenceScreen;
//import android.content.SharedPreferences;

/*Наследуемся от PreferenceFragmentCompat, который был добавлен в
    Gradle Scripts/build.gradle : implementation 'androidx.preference:preference:1.2.0'*/
public class SettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        //Ссылаемся на созданную разметку
        addPreferencesFromResource(R.xml.timer_preferences);

        Preference preference = findPreference("default_interval");
        preference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
        if (preference.getKey().equals("default_interval")) {
            String defaultIntervalString = String.valueOf(newValue);
            try {
                int defaultInterval = Integer.parseInt(defaultIntervalString);
                if (defaultInterval > 1800) {
                    Toast.makeText(getContext(), "Please enter a value not greater then 1800",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
                if (defaultInterval < 1) {
                    Toast.makeText(getContext(), "Please enter a value greater then 0 ",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            } catch (NumberFormatException ex) {
                Toast.makeText(getContext(), "Please enter a numeric value",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }
}
/* Сложный и устаревший способ создания подписей у ListPreferencex. В androidx
для ListPreference и EditTextPreference можно поставить app:useSimpleSummaryProvider=”true”,
что будет равносильно всему нижележащему коду

  public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        //Ссылаемся на созданную разметку
        addPreferencesFromResource(R.xml.timer_preferences);

        SharedPreferences sharedPreferences = getPreferenceScreen()
                .getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < count; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceLabel(preference, value);
            }
        }
    }

    private void setPreferenceLabel(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            if (index >= 0) {
                listPreference.setSummary(listPreference.getEntries()[index]);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (!(preference instanceof CheckBoxPreference)) {
            String value = sharedPreferences.getString(preference.getKey(), "");
            setPreferenceLabel(preference, value);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
*/