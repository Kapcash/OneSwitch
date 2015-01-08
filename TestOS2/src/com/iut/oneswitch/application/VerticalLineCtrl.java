package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.WindowManager;

import com.iut.oneswitch.view.VerticalLine;

/**
 * Permet la gestion de la ligne horizontale
 * @author OneSwitch B
 * @see com.iut.oneswitch.view.HorizontalLine
 */
public class VerticalLineCtrl extends LineController{
	private int lineThickness;
	private float speed;

	private OneSwitchService theService;
	private Handler handler;
	private Runnable runnable;

	private VerticalLine theLine;
	private WindowManager.LayoutParams verticalParams;

	private boolean isMovingRight = true;
	
	/**
	 * Constructeur de la ligne horizontale
	 * @param service
	 */
	public VerticalLineCtrl(OneSwitchService service) {
		this.theLine = new VerticalLine(service);
		this.theLine.setId(201);
		this.theService = service;
		/*
		 * Récupère la taille indiquée en paramètre
		 * (3 en valeur par défaut si échec de récupération du paramètre)
		 */
		this.lineThickness = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(service).getString("lign_size","3"));
		this.lineThickness *= theLine.getResources().getDisplayMetrics().density;
		this.speed = 5 * theLine.getResources().getDisplayMetrics().density;
	}
	
	/**
	 * Initialise la ligne sans l'afficher
	 * Mise en place des paramètres (taille, position initiale)
	 */
	public void init(){
		verticalParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);

		verticalParams.gravity = Gravity.TOP | Gravity.LEFT;
		verticalParams.x = 0;
		verticalParams.y = 0;
		verticalParams.height = theService.getScreenSize().y;
		verticalParams.width = lineThickness;

		handler = new Handler();
		runnable = new VerticalLineRunnable();
	}
	
	/**
	 * Ajoute la vue de la ligne sur l'écran
	 */
	public void add(){
		init();
		if(!isShown){
			theService.addView(theLine, verticalParams);
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
		iterations=0;
	}

	/**
	 * Met en pause le déplacement de la ligne
	 */
	public void pause(){
		isMoving = false;
	}

	/**
	 * Position en X sur l'écran
	 * @return coordonée en abscisse de la ligne
	 */
	public int getX(){
		return verticalParams.x;
	}
	
	/**
	 * Position en Y sur l'écran
	 * @return coordonée en ordonée de la ligne
	 */
	public int getY(){
		return verticalParams.y;
	}
	
	/**
	 * Epaisseur de la ligne
	 * @return epaisseur de la ligne
	 */
	public int getThickness(){
		return lineThickness;
	}

	/**
	 * Permet le déplacement automatique en tâche de fond
	 * @author OneSwitch B
	 *
	 */
	class VerticalLineRunnable implements Runnable{
		/**
		 * Permet le défilement de la ligne
		 */
		@Override
		public void run() {
			try {
				if(getIterations()==3){
					pause();
					remove();
					ClickHandler.getHorizontalLineCtrl().pause();
					ClickHandler.getHorizontalLineCtrl().remove();
				}
				Point size = theService.getScreenSize();
				if((verticalParams.x <= size.x)&&(isMovingRight == true)){
					verticalParams.x += speed;

					theService.updateViewLayout(theLine, verticalParams);
					if(verticalParams.x >= (size.x -speed))
						isMovingRight = false;
				}else
				{
					verticalParams.x -= speed;

					theService.updateViewLayout(theLine, verticalParams);
					if(verticalParams.x <= (0+speed)){
						isMovingRight = true;
						addIterations();
					}
				}
				if(isMoving)
					handler.postDelayed(this, 10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}