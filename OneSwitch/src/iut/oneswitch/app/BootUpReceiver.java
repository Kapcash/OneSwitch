package iut.oneswitch.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class BootUpReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		if(sp.getBoolean("key_switch_auto", false)){
			Intent i = new Intent(context, OneSwitchService.class);
        	context.startService(i);
		}
	}
}
