package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;

import com.iut.oneswitch.view.popup.CircleView;
import com.iut.oneswitch.view.popup.PopUpView;

/**
 * 
 * @author OneSwitch B
 *
 */
public class PopUpCtrl {
	
	/**
	 * Le service de notre application.
	 */
	private OneSwitchService theService;
	
	/**
	 * La popUp qui sera manipulée.
	 */
	private PopUpView thePopup;
	
	/**
	 * Un cercle désignant la zone où le clic à été effectué.
	 */
	private CircleView circle;
	
	/**
	 * Les paramètres de la popUp.
	 */
	private WindowManager.LayoutParams popupParams;
	
	/**
	 * Les paramètres du cercle.
	 */
	private WindowManager.LayoutParams circleParams;
	
	/**
	 * Position en abscisse et ordonnée de la popUp. Sera utilisé afin de définir "circleParams".
	 */
	private int posX, posY;
	
	/**
	 * Ce booléen atteste de la visibilité de la popUp.
	 */
	private boolean isRunning;

	
	private Handler handler;
	
	
	private Runnable runnable;
	
	/**
	 * 
	 * @param service Le service de notre application.
	 * @param x Un entier désignant la position en abscisse du clic.
	 * @param y Un entier désignant la position en ordonnée du clic.
	 */
	public PopUpCtrl(OneSwitchService service, int x, int y) {
		this.theService = service;
		this.posX = x;
		this.posY = y;
		isRunning = false;
		init();
	}
	
	/**
	 * 
	 * @return L'attribut service et donc le service de notre application.
	 */
	public OneSwitchService getService(){
		return theService;
	}
	
	/**
	 * Cette méthode permet l'affichage de la popUp ainsi que le cercle désignant la zone du clic à l'origine du déclenchement de la popUp.
	 * Nous utilisons les attributs de paramètres afin de définir l'affichage de la popUp et du cercle.
	 */
	private void init(){
		float density = theService.getResources().getDisplayMetrics().density;
		
		/* Drawing Circle */
			circle = new CircleView(theService, this);
			circleParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
					theService.getStatusBarHeight(),
					WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
					WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
					WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
					PixelFormat.TRANSLUCENT);
			circleParams.gravity = Gravity.TOP | Gravity.LEFT;
			circleParams.x = (int) (this.posX-(14*density)); //14 = rayon du cercle (12) + largeur du trait (2)
			circleParams.y = (int) (this.posY-(14*density));
			circleParams.height = ((int)density*28);
			circleParams.width  = ((int)density*28);
			theService.addView(circle, circleParams);
		/*End Drawing Circle*/
			
		/* Drawing Popup */
			thePopup = new PopUpView(theService, this);
			popupParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
					theService.getStatusBarHeight(),
					WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
					WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
					WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
					PixelFormat.TRANSLUCENT);
			popupParams.gravity = Gravity.TOP | Gravity.LEFT;
			
			int rightPadding = (theService.getScreenSize().x - posX);
			if(rightPadding>(140*density)) popupParams.x = (int) (this.posX-(14*density)); //14 = rayon du cercle (12) + largeur du trait (2)
			else  popupParams.x = (int) (this.posX-(166*density));
			popupParams.y = (int) (this.posY-(76*density));
			popupParams.height = (int)density*152;
			popupParams.width  = (int)density*152;
			theService.addView(thePopup, popupParams);
		/*End Drawing Popup*/
		
		handler = new Handler();
		runnable = new PopupRunnable();
	}
	
	/**
	 * Efface la popUp et le cercle de la vue. On utilise la méthode removeView de la classe OneSwitchService.
	 * @see OneSwitchService#removeView(android.view.View)
	 */
	public void removeView(){
		theService.removeView(thePopup);
		theService.removeView(circle);
	}

	/**
	 * Déclenche le défilement des boutons du menu popup en passe isRunning à vrai.
	 * @see PopupRunnable#run()
	 */
	public void startThread(){
		isRunning = true;
		handler.post(runnable);
	}
	
	/**
	 * Arrête le défilement des boutons en passant isRunning à faux.
	 * @see PopupRunnable#run()
	 */
	public void stopThread(){
		isRunning = false;
	}
	
	/**
	 * La méthode getSelected() de la classe PopUpView est appelée.
	 * @see PopUpView#getSelected()
	 * @return Le bouton de la popUp sélectionné.
	 */
	public Button getSelected(){
		return thePopup.getSelected();
	}
	
	/**
	 * 
	 * @return La zone (le point) de clic ayant provoqué l'affiche de la popUp.
	 */
	public Point getPos(){
		return new Point(posX,posY);
	}

	/**
	 * La méthode run permet de sélectionner le bouton suivant du menu toutes les secondes.
	 * La sélection se fait en boucle tant que l'attribut isRunning "est vrai".
	 * @author OneSwitch B
	 *
	 */
	class PopupRunnable implements Runnable{

		@Override
		public void run() {
			try {
				if(isRunning){ 
					thePopup.selectNext();
					handler.postDelayed(this, 1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}