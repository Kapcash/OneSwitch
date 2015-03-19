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

public class PrefGeneralFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
	
	private Intent globalService;
	private SharedPreferences sp;
	private static SwitchPreference sw;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_os);
		
		globalService = new Intent(getActivity(), OneSwitchService.class);
		sp = getPreferenceManager().getSharedPreferences();
		
		sw = (SwitchPreference) findPreference("key_switch");
	    sw.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	        @Override
	        public boolean onPreferenceChange(Preference preference, Object newValue) {
	            if(newValue.equals(true)){
	            	getActivity().startService(globalService);
	            }
	            else{
	            	getActivity().stopService(globalService);
	            }
	            return true;
	        }
	    });
	    
	    Preference reset = (Preference) findPreference("reset");
	    reset.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				PrefGeneralFragment.this.reloadPref();
				return true;
			}
		});
	    
	    Preference about = (Preference) findPreference("about");
	    about.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
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
			//Check is the service is currently running and update the switch
			if(isMyServiceRunning(OneSwitchService.class)){
				sw.setChecked(true);
			}
			else{
				sw.setChecked(false);
			}
		}
	}
	
	public static void stop(){
		sw.setChecked(false);
	}
	
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
		Editor editor = getPreferenceScreen().getSharedPreferences().edit();
		editor.clear();
		editor.commit();
		PreferenceManager.setDefaultValues(PrefGeneralFragment.this.getActivity(), R.xml.pref_os, true);
		
		SwitchPreference switchPref = (SwitchPreference) findPreference("key_switch_auto");
		switchPref.setChecked(false);
		
		SwitchPreference switchVocal = (SwitchPreference) findPreference("vocal");
		switchVocal.setChecked(false);
		
		ColorPickerPreference colorH = (ColorPickerPreference) findPreference("color1");
		colorH.onColorChanged(0xff0000ff);
		
		ColorPickerPreference colorV = (ColorPickerPreference) findPreference("color2");
		colorV.onColorChanged(0xff00ffff);
		
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(PrefGeneralFragment.this);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
		Preference pref = findPreference(key);
		if (pref instanceof AutoSummaryListPreference) {
	        AutoSummaryListPreference listPref = (AutoSummaryListPreference) pref;
	        listPref.setValue(sp.getString(key,"0"));
	    }
	}
}