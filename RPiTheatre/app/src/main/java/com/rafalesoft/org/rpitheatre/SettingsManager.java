package com.rafalesoft.org.rpitheatre;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;


public class SettingsManager extends PreferenceActivity
							 implements OnSharedPreferenceChangeListener 
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		/*addPreferencesFromResource(R.xml.preferences);

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Map<String,?> keys = sp.getAll();
		for(Map.Entry<String,?> entry : keys.entrySet())
		{
			Preference p = findPreference(entry.getKey());
			p.setSummary(sp.getString(entry.getKey(), ""));
		}
		sp.registerOnSharedPreferenceChangeListener(this);
		*/
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
										  String key)
	{
		Preference connectionPref = findPreference(key);
        // Set summary to be the user-description for the selected value
        connectionPref.setSummary(sharedPreferences.getString(key, ""));
	}
}
