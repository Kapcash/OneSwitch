package com.example.oneswitch.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootUpReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
            Intent i = new Intent(context, OneSwitchService.class);
            context.startService(i);
	}
}
