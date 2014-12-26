package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.iut.oneswitch.view.HorizontalLine;
/**
 * Permet la gestion de la ligne horizontale
 * @author OneSwitch B
 * @see com.iut.oneswitch.view.HorizontalLine
 */
public class HorizontalLineCtrl extends LineController {
	private int lineThickness = 3;
	private int speed = 3;

	private OneSwitchService theService;
	private Handler handler;
	private Runnable runnable;

	private HorizontalLine theLine;
	private WindowManager.LayoutParams horizParams;
	private boolean isShown = false;
	private boolean isMoving = false;
	private boolean isMovingDown = true;
	
	/**
	 * Constructeur de la ligne horizontale
	 * @param service
	 */
	public HorizontalLineCtrl(OneSwitchService service) {
		this.theLine = new HorizontalLine(service);
		this.theLine.setId(200);
		this.theService = service;
	}

	/**
	 * Initialise la ligne sans l'afficher
	 * Mise en place des paramètres (taille, position initiale)
	 */
	public void init(){
		horizParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);

		horizParams.gravity = Gravity.TOP | Gravity.LEFT;
		horizParams.x = 0;
		horizParams.y = 0;
		horizParams.height = lineThickness;
		horizParams.width = theService.getScreenSize().x;

		handler = new Handler();
		runnable = new HorizLineRunnable();

	}


	/**
	 * Ajoute la vue de la ligne sur l'écran
	 */
	public void add(){
		init();
		if(!isShown){
			System.out.println("horiz added");
			theService.addView(theLine, horizParams);
			isShown = true;
			start();
		}

	}

	/**
	 * Supprime la vue de la ligne de l'écran
	 */
	public void remove(){
		if((isShown)&&(!isMoving)){
			theService.removeView(theLine);
			isShown = false;
		}

	}

	/**
	 * Démarre le mouvement de la ligne
	 */
	protected void start(){	
		isMoving = true;
		handler.postDelayed(runnable, 1000); //start après 1 seconde
	}

	/**
	 * Met en pause le déplacement de la ligne
	 */
	public void pause(){
		isMoving = false;
	}


	/**
	 * Ligne affichée sur l'écran ?
	 * @return vrai ou faux
	 */
	public boolean isShown() {
		return isShown;
	}

	/**
	 * Ligne en déplacement sur l'écran
	 * @return vrai ou faux
	 */
	public boolean isMoving() {
		return isMoving;
	}

	/**
	 * Position en X sur l'écran
	 * @return coordonée en abscisse de la ligne
	 */
	public int getX(){
		return horizParams.x;
	}
	
	
	/**
	 * Position en Y sur l'écran
	 * @return coordonée en ordonée de la ligne
	 */
	public int getY(){
		return horizParams.y;
	}


	/**
	 * Permet le déplacement automatique en tâche de fond
	 * @author OneSwitch B
	 *
	 */
	class HorizLineRunnable implements Runnable{

		/**
		 * Permet le défilement de la ligne
		 */
		@Override
		public void run() {
			try {
				Point size = theService.getScreenSize();
				if((horizParams.y <= size.y)&&(isMovingDown == true)){
					horizParams.y += speed;

					theService.updateViewLayout(theLine, horizParams);
					if(horizParams.y >= (size.y-speed))
						isMovingDown = false;
				}
				else
				{
					horizParams.y -= speed;
					theService.updateViewLayout(theLine, horizParams);
					if(horizParams.y <= (0+speed))
						isMovingDown = true;
				}
				if(isMoving) //si elle doit continuer de bouger, alors on planifie le prochain mouvement
					handler.postDelayed(this, 10);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}


