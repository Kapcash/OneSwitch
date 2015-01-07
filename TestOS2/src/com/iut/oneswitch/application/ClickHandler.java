package com.iut.oneswitch.application;

import android.graphics.Point;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
/**
 * Gère les actions à effectuer lors d'un clic sur le contacteur "oneswitch"
 * @author OneSwitch B
 *
 */
public class ClickHandler {

	private static HorizontalLineCtrl horizLine;
	private static VerticalLineCtrl verticalLine;
	private static boolean isInPopupMenu = false;
	private static boolean isInContextMenu = false;
	private static PopUpCtrl thePopup;
	private static ButtonMenuCtrl theButtonMenu;
	
	/**
	 * 1) demarre la ligne horizontale
	 * 2) met en pause la ligne horizontale et demarre la ligne verticale
	 * 3) met en pause la ligne verticale et effectue le click
	 * 4) supprime les deux lignes, et retour en 1
	 * @param service le service de l'application
	 * @param panel la vue interceptant le touch
	 */
	public void handleClick(OneSwitchService service, final ClickPanelCtrl panel){
		if(!isInPopupMenu && !isInContextMenu){
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


				int realX = verticalLine.getX() + verticalLine.getThickness();
				int realY = horizLine.getY() + horizLine.getThickness()/2;
				//SIMULATION DU CLICK
				//ClickHandler.touchAsRoot(verticalLine.getX(), horizLine.getY(), 200); //click de 200ms

				panel.remove();
				
				thePopup = new PopUpCtrl(service,realX, realY);
				isInPopupMenu = true;
				
				Handler mHandler = new Handler();
				mHandler.postDelayed(new Runnable() {
					public void run() {
						panel.init();
					}
				}, 200);
				
				

			}

			else if(horizLine.isShown()&&verticalLine.isShown()&&!horizLine.isMoving()&&!verticalLine.isMoving()){ //quatrieme click : deux lignes affichées, aucun mouvement
				verticalLine.remove();							//suppression des lignes
				horizLine.remove();							//suppression des lignes
			}

		}
		else if(isInPopupMenu && !isInContextMenu){ //PopUp Cliquer/Glisser/Glisser
			Button theSelected = thePopup.getSelected();
			panel.remove();
			theSelected.performClick();
			isInPopupMenu = false;

			Handler mHandler = new Handler();
			mHandler.postDelayed(new Runnable() {
				public void run() {
					panel.init();
				}
			}, 900);

		}
		else if(!isInPopupMenu && isInContextMenu){ //PopUp d'appuie long
			Button theSelected = theButtonMenu.getSelected();
			theSelected.performClick();
			isInContextMenu = false;
		}

	}


	public void handleSwipe(OneSwitchService service, final ClickPanelCtrl panel){
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


			int realX = verticalLine.getX() + verticalLine.getThickness();
			int realY = horizLine.getY() + horizLine.getThickness()/2;
			verticalLine.remove();							//suppression des lignes
			horizLine.remove();	
			panel.remove();
			Point pos1 = thePopup.getPos();
			Point pos2 = new Point(realX, realY);
			ActionGesture actionGesture = new ActionGesture();
			actionGesture.swipeAsRoot(pos1, pos2);
			Handler mHandler = new Handler();
			mHandler.postDelayed(new Runnable() {
				public void run() {
					panel.init();
				}
			}, 900);
			thePopup.getService().getClickPanelCtrl().setForSwipe(false);
		}
		else if(horizLine.isShown()&&verticalLine.isShown()&&!horizLine.isMoving()&&!verticalLine.isMoving()){ //quatrieme click : deux lignes affichées, aucun mouvement

		}

	}
	
	
	public void handleLongClick(OneSwitchService service, final ClickPanelCtrl panel){
		//Rend impossible l'affichage de la popup si les lignes sont actives.
		panel.remove();
		if((horizLine == null || !horizLine.isMoving()) && (verticalLine == null || !verticalLine.isMoving())&& !isInPopupMenu){
			theButtonMenu = new ButtonMenuCtrl(service);
			isInContextMenu = true;
		}
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				panel.init();
			}
		}, 200);
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
