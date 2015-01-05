package com.iut.oneswitch.application;

import java.io.IOException;

import android.os.Handler;

import com.iut.oneswitch.view.popup.PopUpView;
/**
 * Gère les actions à effectuer lors d'un clic sur le contacteur "oneswitch"
 * @author OneSwitch B
 *
 */
public class ClickHandler {
	
	private static HorizontalLineCtrl horizLine;
	private static VerticalLineCtrl verticalLine;

	/**
	 * 1) demarre la ligne horizontale
	 * 2) met en pause la ligne horizontale et demarre la ligne verticale
	 * 3) met en pause la ligne verticale et effectue le click
	 * 4) supprime les deux lignes, et retour en 1
	 * @param service le service de l'application
	 * @param panel la vue interceptant le touch
	 */
	public void handleClick(OneSwitchService service, final ClickPanelCtrl panel){
		
		if(horizLine == null)
			horizLine = new HorizontalLineCtrl(service);

		if(verticalLine == null)
			verticalLine = new VerticalLineCtrl(service);

		if((!horizLine.isShown())&&(!verticalLine.isShown())&&(!horizLine.isMoving())&&(!verticalLine.isMoving())){ //aucune ligne affichée et aucun mouvement : premier click
			horizLine.add();								//demarrage ligne horizontale
		}

		else if(horizLine.isShown()&&!verticalLine.isShown()&&horizLine.isMoving()&&!verticalLine.isMoving()){ //deuxieme click : ligne horizontale affichée
			horizLine.pause();							//pause de la ligne horizontale
			verticalLine.add();							//ajout ligne verticale
		}

		else if(horizLine.isShown()&&verticalLine.isShown()&&!horizLine.isMoving()&&verticalLine.isMoving()){ //troisieme click : deux lignes affichées
			verticalLine.pause();							//pause de la ligne vertical
			panel.remove();
			
			int realX = verticalLine.getX() + verticalLine.getThickness();
			int realY = horizLine.getY() + horizLine.getThickness()/2;
			//SIMULATION DU CLICK
			//ClickHandler.touchAsRoot(verticalLine.getX(), horizLine.getY(), 200); //click de 200ms
			
			//REAJOUT DU PANEL
			Handler mHandler = new Handler();
	        mHandler.postDelayed(new Runnable() {
	            public void run() {
	                panel.init();
	            }
	        }, 500);
	        
	        PopUpCtrl thePopup = new PopUpCtrl(service,realX, realY);
	        
	        
		}
		
		else if(horizLine.isShown()&&verticalLine.isShown()&&!horizLine.isMoving()&&!verticalLine.isMoving()){ //quatrieme click : deux lignes affichées, aucun mouvement
			verticalLine.remove();							//suppression des lignes
			horizLine.remove();							//suppression des lignes
		}
	}

	/**
	 * Simulation du clic sur l'écran via le "input swipe"
	 * ROOT nécessaire
	 * @param x coordonée abscisse
	 * @param y coordonée ordonnée
	 * @param time le temps d'appui à simuler
	 */
	private static void touchAsRoot(int x, int y, int time){
		try {
			//Runtime.getRuntime().exec("su -c input tap " + x + " " + y);

			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + x + " " + y + " " + time);
			System.out.println("su -c input swipe " + x + " " + y + " " + x + " " + y + " " + time);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * supprime les deux lignes
	 */
	public void stop(){
		if(horizLine != null)
			horizLine.remove();	

		if(verticalLine != null)
			verticalLine.remove();	
	}
	
}
