package com.iut.oneswitch.application;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.example.oneswitch.R;

public class Notif{
	
	private static Notif notif;
	private NotificationManager nManager;
	private Context os;
	
	private Notif(Context os){
		this.os = os;
		nManager = (NotificationManager) os.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	public static Notif getInstance(Context os){
		if(notif == null){
			notif = new Notif(os);
		}
		return notif;
	}
	
	public void createRunningNotification(){ 
		NotificationCompat.Builder nBuild = new NotificationCompat.Builder(os);
		nBuild.setSmallIcon(R.drawable.icon_os);
		nBuild.setContentTitle(os.getResources().getString(R.string.notif_title));
		nBuild.setContentText(os.getResources().getString(R.string.notif_desc));
		//Add the notification
		nManager.notify(1, nBuild.build());
	}
	
	public void removeRunningNotification(){
		nManager.cancel(1);
	}
}
