package com.iut.oneswitch.model;

import com.iut.oneswitch.view.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootUpReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
           // Intent i = new Intent(context, MainActivity.class);  
           //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           //context.startActivity(i); 
    	//System.out.println("BOOT");
           
           if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
               Intent serviceIntent = new Intent(context, OneSwitchService.class);
               context.startService(serviceIntent);
           }
    }

}