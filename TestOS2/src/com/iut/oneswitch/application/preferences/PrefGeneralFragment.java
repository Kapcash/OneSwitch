package com.iut.oneswitch.application.preferences;

import com.example.oneswitch.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Classe créant le fragment de préférences d'après le pref_os.xml.
 * @author OneSwitch B
 */
public class PrefGeneralFragment extends PreferenceFragment {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_os);
	}
}
