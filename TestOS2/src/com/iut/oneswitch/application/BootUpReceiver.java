package com.iut.oneswitch.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * Non utilisée pour l'instant
 * En prévision d'un prochain sprint
 * @author OneSwitch B
 *
 */
public class BootUpReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {           
           if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
               Intent serviceIntent = new Intent(context, OneSwitchService.class);
               context.startService(serviceIntent);
           }
    }

}