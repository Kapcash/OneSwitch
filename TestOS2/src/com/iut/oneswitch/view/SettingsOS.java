package com.iut.oneswitch.view;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import com.iut.oneswitch.application.PrefGeneralFragment;

/**
 * Page de préférences contenant les différents fragments définis
 * dans les classes Pref***Fragment
 * @author Florent
 */
public class SettingsOS extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  
		  getFragmentManager().beginTransaction().replace(android.R.id.content,new PrefGeneralFragment()).commit();
		  //Button back on the top icon bar
		  ActionBar actionBar = getActionBar();
		  actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	/**
	 * Retourne sur la vue précédente lors d'un clic sur l'icone
	 * en haut à gauche
	 */
	public boolean onOptionsItemSelected(MenuItem item){
	    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
	    startActivityForResult(myIntent, 0);
	    return true;
	}
	
}