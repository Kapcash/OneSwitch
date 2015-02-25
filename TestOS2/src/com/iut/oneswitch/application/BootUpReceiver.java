package com.iut.oneswitch.application;

import com.iut.oneswitch.view.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Non utilisé pour l'instant
 * En prévision du sprint 4
 * @author OneSwitch B
 */
public class BootUpReceiver extends BroadcastReceiver{

 
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
         if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
        	 Intent myStarterIntent = new Intent(context, MainActivity.class);
             myStarterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(myStarterIntent);


         }
         }
}