package com.iut.oneswitch.application;

import java.io.IOException;

import android.os.Handler;

public class ClickHandler {

	private static HorizontalLineCtrl horizLine;
	private static VerticalLineCtrl verticalLine;

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
			ClickHandler.touchAsRoot(verticalLine.getX(), horizLine.getY(), 200); //click de 200ms

			Handler mHandler = new Handler();
	        mHandler.postDelayed(new Runnable() {
	            public void run() {
	                panel.init();
	            }
	        }, 500);
			//panel.init();
		}

		else if(horizLine.isShown()&&verticalLine.isShown()&&!horizLine.isMoving()&&!verticalLine.isMoving()){ //quatrieme click : deux lignes affichées, aucun mouvement
			verticalLine.remove();							//suppression des lignes
			horizLine.remove();							//suppression des lignes
		}


	}


	private static void touchAsRoot(int x, int y, int time){
		try {
			//Runtime.getRuntime().exec("su -c input tap " + x + " " + y);

			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + x + " " + y + " " + time);
			System.out.println("su -c input swipe " + x + " " + y + " " + x + " " + y + " " + time);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void stop(){
		if(horizLine != null)
			horizLine.remove();	

		if(verticalLine != null)
			verticalLine.remove();	
	}
	
	public void reinit(){
		if((horizLine.isMoving()))
			horizLine.init();	

		if((verticalLine.isMoving())){
			horizLine.initSize();
			verticalLine.init();
		}
				
			
	}
}
