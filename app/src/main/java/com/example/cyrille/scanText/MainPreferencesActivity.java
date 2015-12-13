package com.example.cyrille.scanText;

import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Set;

/**
 * Created by cyrille on 08.12.15.
 */
public class MainPreferencesActivity extends PreferenceActivity
    {
    //TODO : manage the MultiselectListPreference to show the user that its not possible to uncheck all options

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.advanced_preferences);

        setCharsetsChangeListener();
        }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
        {
        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener()
        {
        @Override
        public void onClick(View v)
            {
            onBackPressed();
            }
        });
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
            Set<String> newCharsetValue = (Set<String>) newValue;

            if (newCharsetValue.isEmpty())
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
