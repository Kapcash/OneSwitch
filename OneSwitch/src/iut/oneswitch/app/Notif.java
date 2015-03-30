package iut.oneswitch.app;

import iut.oneswitch.R;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Classe gérant les notifications de l'application OneSwitch.
 * Cette classe implémente le pattern Singleton.
 * @author OneSwitch B
 */
public class Notif{

	/**
	 * ID de la notification indiquant que le service est en cours.
	 */
	private final int ID_RUNNING_NOTIF = 1;

	/**
	 * Attribut du singleton.
	 */
	private static Notif notif;

	/**
	 * Objet de l'API gérant les créations et destructions de notifications.
	 */
	private NotificationManager nManager;

	/**
	 * Contexte de l'application nécessaire au NotificationManager.
	 */
	private Context context;

	/**
	 * Constructeur privé (Singleton), initialise les attributs
	 * @param os Context de l'application
	 */
	private Notif(Context os){
		context = os;
		nManager = (NotificationManager) os.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * Méthode du pattern Singleton.
	 * @param os Context de l'application.
	 * @return Retourne l'instance de la classe Notif.
	 */
	public static Notif getInstance(Context os){
		if(notif == null){
			notif = new Notif(os);
		}
		return notif;
	}

	/**
	 * Créer une notification indiquant que le service est en cours.
	 */
	public void createRunningNotification(){
		NotificationCompat.Builder nBuild = new NotificationCompat.Builder(context);
		nBuild.setSmallIcon(R.drawable.ic_launcher);
		nBuild.setTicker(context.getResources().getString(R.string.notif_desc));
		nBuild.setContentTitle(context.getResources().getString(R.string.notif_title));
		nBuild.setContentText(context.getResources().getString(R.string.notif_desc));

		//Action de la notification
		final Intent launchNotificationIntent = new Intent(context, OneSwitchActivity.class);
		final PendingIntent pendingIntent = PendingIntent.getActivity(context,
				1, launchNotificationIntent,
				PendingIntent.FLAG_ONE_SHOT);

		//Ajout de la notification
		nBuild.setContentIntent(pendingIntent);
		nManager.notify(ID_RUNNING_NOTIF, nBuild.build());
	}

	/**
	 * Supprime la notification indiquant que le service est en cours.
	 */
	public void removeRunningNotification(){
		nManager.cancel(ID_RUNNING_NOTIF);
	}
}
