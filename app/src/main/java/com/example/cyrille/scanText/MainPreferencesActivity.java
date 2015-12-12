package com.example.cyrille.scanText;

import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import java.util.Set;

/**
 * Created by cyrille on 08.12.15.
 */
public class MainPreferencesActivity extends PreferenceActivity
    {

    //TODO : add the navigation toolbar
    //TODO : manage the MultiselectListPreference to show the user that its not possible to uncheck all options


    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.advanced_preferences);

        setCharsetsChangeListener();
        }

    //pour éviter que la liste ne soit vidée par l'utilisateur
    private void setCharsetsChangeListener()
        {
        MultiSelectListPreference dataPref = (MultiSelectListPreference) findPreference("charsets_preference");
        dataPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue)
            {
            Set<String> newHostValue = (Set<String>) newValue;

            if (newHostValue.isEmpty())
                {
                return false;
                }
            else
                {
                return true;
                }
            }
        });
        }

    }
