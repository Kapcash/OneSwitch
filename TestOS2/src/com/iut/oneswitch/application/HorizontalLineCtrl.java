package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.iut.oneswitch.view.HorizontalLine;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.WindowManager;

import com.iut.oneswitch.view.HorizontalLine;

/**
 * Permet la gestion d'une ligne horizontale.
 * @author OneSwitch B
 * @see com.iut.oneswitch.view.HorizontalLine
 */
public class HorizontalLineCtrl extends LineController {
	
	/**
	 * Correspond à l'épaisseur de la ligne.
	 */
	private int lineThickness;
	
	/**
	 * Correspond à la vitesse de déplacement de la ligne.
	 */
	private float speed;
	
	/**
	 * Correspond au service de notre application.
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
	 * 
	 */
	private HorizontalLine theLine;
	
	/**
	 * Variable réunissant les paramètres de la ligne horizontale et son affichage.
	 */
	private WindowManager.LayoutParams horizParams;
	
	/**
	 * Indique si la ligne est affichée.
	 */
	private boolean isShown = false;
	
	/**
	 * Indique si la ligne est en mouvement.
	 */
	private boolean isMoving = false;
	
	/**
	 * 
	 */
	private boolean isMovingDown = true;

	/**
	 * Constructeur de HorizontalLineCtrl.
	 * Celui-ci initialise une ligne horizontale.
	 * @param service Le service de l'application.
	 */
	public HorizontalLineCtrl(OneSwitchService service) {
		this.theLine = new HorizontalLine(service);
		this.theLine.setId(200);
		this.theService = service;
		/* Récupère la taille indiquée en paramètre
		 * (3 en valeur par défaut si échec de récupération du paramètre) */
		this.lineThickness = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(service).getString("lign_size","3"));
		this.lineThickness*= theLine.getResources().getDisplayMetrics().density;
		this.speed = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(service).getString("lign_speed","2"));
		this.speed *= theLine.getResources().getDisplayMetrics().density;

	}

	/**
	 * Mise en place des paramètres (taille, position initiale) de la ligne.
	 * Met en place un fil d'exécution.
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
	 * Affiche la ligne à l'écran par ajout de la vue "theLine" au service.
	 * La méthode "start" est appelée.
	 * @see HorizontalLineCtrl#start()
	 */
	public void add(){
		init();
		if(!isShown){
			this.lineThickness = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(theService).getString("lign_size","3"));
			this.lineThickness*= theLine.getResources().getDisplayMetrics().density;
			this.speed = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(theService).getString("lign_speed","2"));
			this.speed *= theLine.getResources().getDisplayMetrics().density;
			
			init();
			
			System.out.println("horiz added");
			theService.addView(theLine, horizParams);
			isShown = true;
			start();
		}
	}

	/**
	 * Retire la ligne de l'écran par suppression de la vue "theLine" du service.
	 */
	public void remove(){
		if((isShown)&&(!isMoving)){
			theService.removeView(theLine);
			isShown = false;
		}
	}

	/**
	 * Démarre le mouvement de la ligne.
	 * On utilise ici un fil d'exécution.
	 * @see HorizLineRunnable#run()
	 */
	protected void start(){	
		isMoving = true;
		handler.postDelayed(runnable, 1000); //start après 1 seconde
		iterations=0;
	}

	/**
	 * Le booléen isMoving se voit assigné la valeur false.
	 * Le déplacement de la ligne est alors mis en pause. Ceci est le fruit d'une condition émise dans le fil d'exécution.
	 * @see HorizLineRunnable#run()
	 */
	public void pause(){
		isMoving = false;
	}

	/**
	 * 
	 * @return L'état de la ligne, vrai si celle-ci est affichée.
	 */
	public boolean isShown() {
		return isShown;
	}

	/**
	 * 
	 * @return Vrai si la ligne est en mouvement.
	 */
	public boolean isMoving() {
		return isMoving;
	}

	/**
	 * Acesseur de la position en abscisse de la ligne.
	 * @return Coordonée en abscisse de la ligne.
	 */
	public int getX(){
		return horizParams.x;
	}

	/**
	 * Acesseur de la position en ordonée de la ligne.
	 * @return Coordonée en ordonée de la ligne.
	 */
	public int getY(){
		return horizParams.y;
	}

	/**
	 * Acesseur de l'épaisseur de la ligne.
	 * @return L'epaisseur de la ligne.
	 */
	public int getThickness(){
		return lineThickness;
	}

	/**
	 * Permet le déplacement automatique de la ligne en tâche de fond.
	 * @author OneSwitch B
	 *
	 */
	class HorizLineRunnable implements Runnable{

		@Override
		public void run() {
			try {
				if(getIterations()==3){
					pause();
					remove();
				}
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
					if(horizParams.y <= (0+speed)){
						isMovingDown = true;
						addIterations();
					}
				}
				if(isMoving) //si elle doit continuer de bouger, alors on planifie le prochain mouvement
					handler.postDelayed(this, 10);
			} catch (Exception e) {
				// TODO: handle exception
				
			}
		}
	}
}