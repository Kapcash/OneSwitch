package iut.oneswitch.preference;

import iut.oneswitch.R;
import iut.oneswitch.app.AboutActivity;
import iut.oneswitch.app.OneSwitchService;
import net.margaritov.preference.colorpicker.ColorPickerPreference;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

/**
 * Initialise la principale activité (PreferenceActivity)
 * Gère toutes les préférences
 * @author OneSwitch B
 */
public class PrefGeneralFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
	
	private Intent globalService;
	private SharedPreferences sp;
	/**
	 * Switch pour lancer le service
	 */
	private static SwitchPreference sw;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Charge le .xml de la vue
		addPreferencesFromResource(R.xml.pref_os);
		
		//Intent pour lancer le service OneSwitchService
		globalService = new Intent(getActivity(), OneSwitchService.class);
		sp = getPreferenceManager().getSharedPreferences();
		
		sw = (SwitchPreference) findPreference("key_switch");
	    sw.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	        @Override
	        public boolean onPreferenceChange(Preference preference, Object newValue) {
	            //Lance le service quand le switch est sur "Oui", le stop sinon
	        	if(newValue.equals(true)){
	            	getActivity().startService(globalService);
	            }
	            else{
	            	getActivity().stopService(globalService);
	            }
	            return true;
	        }
	    });
	    
	    //Preference de remise à zéro
	    Preference reset = (Preference) findPreference("reset");
	    reset.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				//Remet les valeurs par défaut
				PrefGeneralFragment.this.reloadPref();
				return true;
			}
		});
	    
	    Preference about = (Preference) findPreference("about");
	    about.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				//Ouvre l'activité "A propos"
				Intent intent = new Intent(PrefGeneralFragment.this.getActivity(),AboutActivity.class);
				PrefGeneralFragment.this.getActivity().startActivity(intent);
				return false;
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(sw != null){
			//Vérifie si le service est activé et met à jour le switch sur la bonne valeur
			if(isMyServiceRunning(OneSwitchService.class))
				sw.setChecked(true);
			else
				sw.setChecked(false);
		}
	}
	
	public static void stop(){
		sw.setChecked(false);
	}
	
	/**
	 * Permet de savoir si le service est actif ou non.
	 * @param serviceClass Le service du projet.
	 * @return Retourne true si le service est actif, false sinon
	 */
	private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * Remet les préférences par défaut (switch et color)
	 */
	private void reloadPref(){
		//Remet les valeurs par défaut, mais ne rafraichit pas l'interface
		Editor editor = getPreferenceScreen().getSharedPreferences().edit();
		editor.clear();
		editor.commit();
		PreferenceManager.setDefaultValues(PrefGeneralFragment.this.getActivity(), R.xml.pref_os, true);
		
		/* --- Remet les valeurs par défaut des switch et des PickColor manuellement --- */
		SwitchPreference switchPref = (SwitchPreference) findPreference("key_switch_auto");
		switchPref.setChecked(false);
		
		SwitchPreference switchVocal = (SwitchPreference) findPreference("vocal");
		switchVocal.setChecked(false);
		
		ColorPickerPreference colorH = (ColorPickerPreference) findPreference("color1");
		colorH.onColorChanged(0xff0000ff);
		
		ColorPickerPreference colorV = (ColorPickerPreference) findPreference("color2");
		colorV.onColorChanged(0xff00ffff);
		
		SeekBarPreference seek = (SeekBarPreference) findPreference ("lign_speed");
		seek.onSetInitialValue(false, 5);
		
		//Indique que les valeurs ont changé
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(PrefGeneralFragment.this);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
		Preference pref = findPreference(key);
		if (pref instanceof AutoSummaryListPreference) {
			//Met à jour les summary des ListPreference
	        AutoSummaryListPreference listPref = (AutoSummaryListPreference) pref;
	        listPref.setValue(sp.getString(key,"0"));
	    }
	}
}