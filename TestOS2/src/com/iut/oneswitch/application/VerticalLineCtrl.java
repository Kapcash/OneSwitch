package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.WindowManager;

import com.iut.oneswitch.view.VerticalLine;

/**
 * Permet la gestion d'une ligne verticale (paramètres et mouvements).
 * @author OneSwitch B
 * @see VerticalLine#VerticalLine(android.content.Context)
 */
public class VerticalLineCtrl extends LineController{
	
	/**
	 * Epaisseur de la ligne.
	 */
	private int lineThickness;
	
	/**
	 * Rapidité du mouvement de la ligne.
	 */
	private float speed;

	/**
	 * Le service de notre application.
	 */
	private OneSwitchService theService;
	
	/**
	 * 
	 */
	private Handler handler;
	
	/**
	 * 
	 */
	private Runnable runnable;
	
	/**
	 * La ligne allant être gérée.
	 */
	private VerticalLine theLine;
	
	/**
	 * Les paramètres d'affichage de la ligne.
	 */
	private WindowManager.LayoutParams verticalParams;
	
	/**
	 * Booléen indiquant si la ligne est en mouvement.
	 */
	private boolean isMovingRight = true;
	
	/**
	 * Constructeur du contrôleur de la ligne verticale.
	 * @param service Le service de notre application.
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
		this.speed = 2 * theLine.getResources().getDisplayMetrics().density;
	}
	
	/**
	 * Initialise la ligne sans l'afficher.
	 * Mise en place des paramètres (taille, position initiale).
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
	 * Ajout de la vue (la ligne) à notre service. Ceci permet l'affichage à l'écran.
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
	 * Supprime la vue (la ligne) de notre service. Ceci permet l'effaçage de la ligne.
	 */
	public void remove(){
		if((isShown)&&(!isMoving)){
			theService.removeView(theLine);
			isShown = false;
		}
	}
	
	/**
	 * Démarre le mouvement de la ligne à l'aide d'un fil d'exécution ("Thread").
	 */
	protected void start(){	
		isMoving = true;
		handler.postDelayed(runnable, 1000); //start après 1 seconde
		iterations=0;
	}

	/**
	 * Met en pause le déplacement de la ligne.
	 */
	public void pause(){
		isMoving = false;
	}

	/**
	 * 
	 * @return Coordonée en abscisse de la ligne.
	 */
	public int getX(){
		return verticalParams.x;
	}
	
	/**
	 * 
	 * @return Coordonée en ordonée de la ligne.
	 */
	public int getY(){
		return verticalParams.y;
	}
	
	/**
	 * 
	 * @return L'épaisseur de la ligne.
	 */
	public int getThickness(){
		return lineThickness;
	}

	/**
	 * Permet le déplacement automatique de la ligne en tâche de fond.
	 * @author OneSwitch B
	 *
	 */
	class VerticalLineRunnable implements Runnable{

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