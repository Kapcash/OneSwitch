package iut.oneswitch.preference;

import iut.oneswitch.R;
import iut.oneswitch.app.OneSwitchService;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

/**
 * Classe créant le fragment de préférences d'après le pref_os.xml.
 * @author OneSwitch B
 */
public class PrefGeneralFragment extends PreferenceFragment{
	
	private Intent globalService;
	private static SwitchPreference sw;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_os);
		
		globalService = new Intent(getActivity(), OneSwitchService.class);
		
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
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(sw != null){
			//Check is the service is currently running and update the switch
			if(isMyServiceRunning(OneSwitchService.class)){
				System.out.println("ENABLED");
				sw.setChecked(true);
			}
			else{
				System.out.println("DISABLED");
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
}