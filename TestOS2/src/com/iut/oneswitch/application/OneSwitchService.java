package com.iut.oneswitch.application;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
/**
 * Cette classe représente le service de l'application
 * Il s'exécute en tâche de fond et permet de gérer les différentes actions possibles
 * @author OneSwitch B
 *
 */
public class OneSwitchService extends Service{

	private final IBinder mBinder = new LocalBinder();
	private WindowManager windowManager;

	private static ClickPanelCtrl clickCtrl;

	/**
	 * Permet l'accès au service depuis d'autres classes
	 * @author OneSwitch B
	 *
	 */
	public class LocalBinder extends Binder {
		/**
		 * @return une instance du service pour que des "clients" puissent accéder aux méthodes publiques
		 */
		public OneSwitchService getService() {
			return OneSwitchService.this;
		}
	}
	
	/**
	 * Renvoie l'attribut mBinder
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	//=================================
	
	/**
	 * Appelée à la création du service
	 * Récupère l'accès à l'interface utilisateur du périphérique
	 */
	@Override public void onCreate() {
		super.onCreate();
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE); //UI access
	}

	/**
	 * Démarrage du service de détection du clic et balayage
	 */
	public void startService(){
		System.out.println("SERVICE STARTED");
		//Disable auto rotation
		Settings.System.putInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
		clickCtrl = new ClickPanelCtrl(this); //click panel to incercept click anywhere on the display
		
		//Add running Notification
		Notif.getInstance(this).createRunningNotification();
	}

	/**
	 * Arret du service de détection du clic et de balayage
	 */
	public void stopService(){
		if(clickCtrl != null){
			clickCtrl.remove();
			//Enable auto rotation
			Settings.System.putInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
			System.out.println("SERVICE STOPPED");
			//Remove running Notification
			Notif.getInstance(this).removeRunningNotification();
		}
	}
	
	//=================================
	
	/**
	 * Permet de récupérer la hauteur de la barre de statut
	 * @return la hauteur en pixels
	 */
	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * Permet de récupérer la taille de l'écran
	 * @return la définition de l'écran
	 */
	public Point getScreenSize(){
		Point size = new Point();
		windowManager.getDefaultDisplay().getSize(size);
		return size;
	}


	//windowsManager utils
	/**
	 * Permet de faire ajouter une vue par le windowManager
	 * @param view la vue
	 * @param params les paramètres
	 */
	public void addView(View view, LayoutParams params) {
		windowManager.addView(view, params);
	}
	
	/**
	 * Permet de faire supprimer une vue par le windowManager
	 * @param view la vue
	 */
	public void removeView(View view) {
		windowManager.removeView(view);
	}

	/**
	 * Permet de faire mettre à jour une vue par le windowManager
	 * @param view la vue
	 * @param params les nouveaux paramètres
	 */
	public void updateViewLayout(View view, LayoutParams params) {
		windowManager.updateViewLayout(view, params);
	}

	



}

