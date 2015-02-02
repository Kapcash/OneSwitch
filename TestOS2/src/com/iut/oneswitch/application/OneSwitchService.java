package com.iut.oneswitch.application;

import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * Cette classe représente le service de l'application.
 * Il s'exécute en tâche de fond et permet de gérer les différentes actions possibles.
 * @author OneSwitch B
 *
 */
public class OneSwitchService extends Service{
	
	/**
	 * 
	 */
	private final IBinder mBinder = new LocalBinder();
	
	/**
	 * 
	 */
	private WindowManager windowManager;
	
	/**
	 * 
	 */
	private static ClickPanelCtrl clickCtrl;
	
	/**
	 * 
	 */
	private boolean isStarted = false;
	
	/**
	 * Acesseur de l'attribut clickCtrl.
	 * @return Le contrôleur clickCtrl.
	 */
	public ClickPanelCtrl getClickPanelCtrl(){
		return clickCtrl;
	}
	
	/**
	 * Permet l'accès au service depuis d'autres classes.
	 * @author OneSwitch B
	 *
	 */
	public class LocalBinder extends Binder {
		
		/**
		 * @return Une instance du service pour que des "clients" puissent accéder aux méthodes publiques.
		 */
		public OneSwitchService getService() {
			return OneSwitchService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	//=================================
	
	@Override public void onCreate() {
		super.onCreate();
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE); //UI access
	}

	/**
	 * Démarrage du service de détection du clic et balayage.
	 */
	public void startService(){
		if(!isStarted){
			isStarted = true;
			System.out.println("SERVICE STARTED");
			//Désactive l'auto rotation
			Settings.System.putInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
			clickCtrl = new ClickPanelCtrl(this); //click panel to incercept click anywhere on the display
			
			//Ajoute "running Notification"
			Notif.getInstance(this).createRunningNotification();
		}
		else System.out.println("SERVICE ALREADY STARTED");
		
	}

	/**
	 * Arret du service de détection du clic et de balayage.
	 */
	public void stopService(){
		if(clickCtrl != null){
			clickCtrl.removeService();
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
	 * @return La hauteur en pixels de la barre de statut.
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
	 * Permet de récupérer la taille de l'écran.
	 * @return La définition de l'écran.
	 */
	public Point getScreenSize(){
		Point size = new Point();
		windowManager.getDefaultDisplay().getSize(size);
		return size;
	}


	/**
	 * Permet de faire ajouter une vue par le windowManager.
	 * @param view La vue à ajouter.
	 * @param params Les paramètres de cette vue.
	 */
	public void addView(View view, LayoutParams params) {
		windowManager.addView(view, params);
	}
	
	/**
	 * Permet de faire supprimer une vue par le windowManager.
	 * @param view La vue à supprimer.
	 */
	public void removeView(View view) {
		windowManager.removeView(view);
	}

	/**
	 * Permet la mise à jour d'une vue par le windowManager.
	 * @param view La vue à mettre à jour.
	 * @param params Les nouveaux paramètres de la vue.
	 */
	public void updateViewLayout(View view, LayoutParams params) {
		windowManager.updateViewLayout(view, params);
	}
}