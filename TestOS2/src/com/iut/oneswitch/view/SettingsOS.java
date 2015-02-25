package com.iut.oneswitch.view;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.iut.oneswitch.application.preferences.PrefGeneralFragment;

/**
 * Page de préférences contenant les différents fragments définis dans les classes PrefGeneralFragment.
 * @author OneSwtich B
 */
public class SettingsOS extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  
		  getFragmentManager().beginTransaction().replace(android.R.id.content,new PrefGeneralFragment()).commit();
		  //Bouton "retour" sur l'icon en haut à gauche (icone)
		  ActionBar actionBar = getActionBar();
		  actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	/**
	 * Retourne sur la vue précédente lors d'un clic sur l'icone figurant en haut à gauche de l'écran.
	 */
	public boolean onOptionsItemSelected(MenuItem item){
	    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
	    startActivityForResult(myIntent, 0);
	    return true;
	}
	
	@Override
	protected void onPause(){
		super.onPause();

		PreferenceManager.getDefaultSharedPreferences(this).edit().apply();
	}
}