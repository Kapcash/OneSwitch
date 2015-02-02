package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.iut.oneswitch.view.popup.ShortcutMenuView;

/**
 * La classe ButtonMenuCtrl permet l'implémentation du menu regroupant les boutons dit "physiques" (Boutons de volumes, retour en avant etc.).
 * @author OneSwitch B
 */
public class ButtonMenuCtrl {
	
	/**
	 * Ce booléen permet de connaître "l'état" actuel du menu. Si il est actif ou non.
	 * Lorsqu'un bouton de ce menu est activé, le menu disparaît.
	 * Le booléen se voit alors assigné la valeur "false".
	 */
	private boolean isRunning = false;
	
	/**
	 * Cet attribut contiendra la vue correspondant au menu des boutons physiques.
	 */
	private ShortcutMenuView theShortcutMenu;
	
	/**
	 * 
	 */
	private Handler handler;
	
	/**
	 * 
	 */
	private Runnable runnable;
	
	/**
	 * Les paramètres d'affichage du menu.
	 */
	private WindowManager.LayoutParams shortcutMenuParams;
	
	/**
	 * Stockera le service de l'application.
	 */
	private OneSwitchService theService;
	
	/**
	 * Lance une méthode permettant la mise en place du menu.
	 * @see ButtonMenuCtrl#init()
	 * @param service Le service de l'application.
	 */
	public ButtonMenuCtrl(OneSwitchService service) {
		this.theService = service;
		isRunning = false;
		init();
	}
	
	/**
	 * Créer un objet de type ShortcutMenuView (la vue du menu).
	 * On initialise les paramètres d'affichage de la vue.
	 * Enfin, un fil d'exécution est mis en place.
	 */
	private void init(){
		/* Drawing Popup */
			theShortcutMenu = new ShortcutMenuView(theService, this);
			shortcutMenuParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
					theService.getStatusBarHeight(),
					WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
					WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
					WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
					PixelFormat.TRANSLUCENT);
			shortcutMenuParams.gravity = Gravity.TOP | Gravity.LEFT;
			
			shortcutMenuParams.x = 0;
			shortcutMenuParams.y = 0;
			shortcutMenuParams.height = theService.getScreenSize().y;
			shortcutMenuParams.width = theService.getScreenSize().x;
			theService.addView(theShortcutMenu, shortcutMenuParams);
		/*End Drawing Popup*/
			
		handler = new Handler();
		runnable = new PopupMenuRunnable();
	}
	
	/**
	 * Permet de lancer le fil d'exécution.
	 * Après le temps indiqué en paramètre de la méthode "postDelayed", la méthode "run" de l'attribut "runnable" sera appelée.
	 */
	public void startThread(){
		isRunning = true;
		handler.postDelayed(runnable, 1000);
	}
	
	/**
	 * Permet de retirer le menu de l'écran.
	 */
	public void removeView() {
		theService.removeView(theShortcutMenu);
	}
	
	/**
	 * L'attribut de type booléen "isRunning" prend pour valeur, "false".
	 * Cela provoque l'arrêt du fil d'exécution.
	 * @see PopupMenuRunnable#run()
	 */
	public void stopThread(){
		isRunning = false;
	}
	
	/**
	 * Fais appel à la méthode "getSelected" de la classe ShortcutMenuView sur la vue theShortcutMenu.
	 * @see ShortcutMenuView#getSelected()
	 * @return Le bouton actuellement en surbrillance dans la popUp.
	 */
	public Button getSelected() {
		return theShortcutMenu.getSelected();
	}
	
	/**
	 * Fais appel à la méthode getButton de la classe ShortcutMenuView.
	 * @see ShortcutMenuView#getButton(int)
	 * @param i Un entier représentant un index de la popUp où se trouve un bouton.
	 * @return Le bouton à l'index renseigné en paramètre.
	 */
	public View getButton(int i) {
		return theShortcutMenu.getButton(i);
	}
	
	/**
	 * Permet la selection d'un bouton du menu.
	 * La méthode "run" permet l'appel de la méthode "selectNext" de la classe ShortcutMenuView.
	 * Tant que le fil d'exécution n'est pas arrêté (via la méthode stopThread de ButtonMenuCtrl), la méthode selectNext sera lancée en boucle.
	 * @see ShortcutMenuView#selectNext()
	 * @author OneSwitch B
	 */
	class PopupMenuRunnable implements Runnable{
		
		@Override
		public void run() {
			try {
				if(isRunning){ 
					theShortcutMenu.selectNext();
					handler.postDelayed(this, 1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}