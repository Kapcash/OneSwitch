package iut.oneswitch.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Permet le boot Auto du service.
 * @author OneSwitch B
 *
 */
public class BootUpReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//Vérifie que l'option est activée
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		if(sp.getBoolean("key_switch_auto", false)){
			Intent i = new Intent(context, OneSwitchService.class);
			context.startService(i);
		}
	}
}
