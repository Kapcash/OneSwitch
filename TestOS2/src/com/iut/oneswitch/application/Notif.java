package com.iut.oneswitch.application;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.example.oneswitch.R;

/**
 * Classe gérant les notifications de l'application OneSwitch
 * Est de type Singleton
 * @author Florent
 */
public class Notif{
	
	/**
	 * ID de la notification indiquant que le service est en cours
	 */
	private final int ID_RUNNING_NOTIF = 1;
	/**
	 * Attribut du singleton
	 */
	private static Notif notif;
	/**
	 * Objet de l'API gérant les créations et destructions de notifications
	 */
	private NotificationManager nManager;
	/**
	 * Context de l'application nécessaire au NotificationManager
	 */
	private Context os;
	
	/**
	 * Constructeur privé (Singleton), initialise les attributs
	 * @param os Context de l'application
	 */
	private Notif(Context os){
		this.os = os;
		nManager = (NotificationManager) os.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	/**
	 * Méthode du pattern Singleton
	 * @param os Context de l'application
	 * @return Retourne l'instance de la classe Notif
	 */
	public static Notif getInstance(Context os){
		if(notif == null){
			notif = new Notif(os);
		}
		return notif;
	}
	
	/**
	 * Créer la notification indiquant que le service est en cours.
	 */
	public void createRunningNotification(){ 
		NotificationCompat.Builder nBuild = new NotificationCompat.Builder(os);
		nBuild.setSmallIcon(R.drawable.icon_os);
		nBuild.setContentTitle(os.getResources().getString(R.string.notif_title));
		nBuild.setContentText(os.getResources().getString(R.string.notif_desc));
		//Add the notification
		nManager.notify(ID_RUNNING_NOTIF, nBuild.build());
	}
	
	/**
	 * Supprime la notification indiquant que le service est en cours.
	 */
	public void removeRunningNotification(){
		nManager.cancel(ID_RUNNING_NOTIF);
	}
}
