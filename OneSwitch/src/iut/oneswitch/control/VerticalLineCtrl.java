package iut.oneswitch.control;

import iut.oneswitch.app.OneSwitchService;
import iut.oneswitch.view.VerticalLine;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Controlleur de la ligne verticale
 * Indique ses différents états, initialise ses paramètres, l'active ou la désactive
 */
public class VerticalLineCtrl{
	/**
	 * Indique si la ligne se déplace
	 */
	private boolean isMoving = false;
	/**
	 * Indique si la ligne se déplace vers le bas (vers la navigation bar)
	 */
	private boolean isMovingRight = true;
	/**
	 * Indique si la ligne est affichée ou non
	 */
	private boolean isShown = false;
	/**
	 * Nombre d'itérations (aller-retours) de la ligne depuis son lancement
	 */
	private int iterations;
	/**
	 * Epaisseur de la ligne, en pixel
	 */
	private int lineThickness;

	/**
	 * Vitesse de déplacement de la ligne
	 */
	private float speed;
	private int delay;
	private VerticalLine theLine;
	private OneSwitchService theService;
	private WindowManager.LayoutParams verticalParams;
	private Point size;
	private SharedPreferences sp;
	private float density;
	private Handler handler;
	private VerticalLineRunnable runnable;
	private int ite;
	private float speedKeyboard;
	private int currentColumn = 0;
	private boolean clavier = false;
	private Runnable slideLeft, slideRight;

	/**
	 * Initialise le controlleur avec le service
	 * @param paramOneSwitchService Le service (contexte) de l'application
	 */
	public VerticalLineCtrl(OneSwitchService service){
		theService = service;
		init();
	}

	public void addIterations(){
		iterations++;
	}

	public int getIterations(){
		return iterations;
	}

	public int getThickness() {
		return lineThickness;
	}

	public int getX(){
		if(theService.doAnimation() &&!clavier)
			return (int)theLine.getX();
		else
			return verticalParams.x;
	}


	/**
	 * Initialise la ligne
	 * Lance la ligne
	 */
	public void init(){
		//Objet contenant les préférences
		sp = PreferenceManager.getDefaultSharedPreferences(theService);

		size = theService.getScreenSize();
		theLine = new VerticalLine(theService);
		theLine.setVisibility(4);
		theLine.setId(200);

		ite = Integer.parseInt(sp.getString("iterations","3"));

		density = theLine.getResources().getDisplayMetrics().density;

		//Récupère la vitesse
		speed = sp.getInt("lign_speed",5);
		speedKeyboard = (theService.getScreenSize().x)/10;

		//Récupère le délai de départ de la ligne
		delay = Integer.parseInt(sp.getString("delay","1000"));

		//Récupère l'épaisseur de la ligne
		lineThickness = Integer.parseInt(sp.getString("lign_size","3"));
		lineThickness *= density;

		verticalParams = new WindowManager.LayoutParams(
				theService.getScreenSize().x,
				theService.getScreenSize().y,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		theLine.setFocusable(false);
		theLine.setFocusableInTouchMode(false);
		verticalParams.gravity = Gravity.TOP | Gravity.START;
		verticalParams.x = 0;
		verticalParams.y = 0;

		handler = new Handler();
		runnable = new VerticalLineRunnable();

		if(theService.doAnimation()){
			verticalParams.height = theService.getScreenSize().y;
			verticalParams.width = theService.getScreenSize().x;
			theService.addView(theLine, verticalParams);
			slideLeft = new Runnable(){
				@Override
				public void run() {
					long distance = (long) (theLine.getX());
					long duration = (long)(distance/((double)(speed)/12));
					isMovingRight = false;
					theLine.setTranslationX(theLine.getX());
					theLine.animate().setDuration(duration).setInterpolator(null).translationX(0).withEndAction(slideRight).start();
				}
			};

			slideRight = new Runnable(){
				@Override
				public void run() {
					if (getIterations()>= ite){
						stop();
						theService.getHorizontalLineCtrl().stop();
					}
					else{
						long distance = (long) (theService.getScreenSize().x - theLine.getX());
						long duration = (long)(distance/((double)(speed)/12));
						isMovingRight = true;
						theLine.setTranslationX(theLine.getX());
						theLine.animate().setDuration(duration).setInterpolator(null).translationX(theService.getScreenSize().x).withEndAction(slideLeft).start();
						addIterations();
					}
				}

			};
		}
		else{
			verticalParams.height = theService.getScreenSize().y;
			verticalParams.width = lineThickness;
			theService.addView(theLine, verticalParams);
		}
	}

	/**
	 * @return Retourne "true" si la ligne se déplace, "false" sinon
	 */
	public boolean isMoving(){
		return isMoving;
	}

	/**
	 * @return Retourne "true" si la ligne est visible
	 */
	public boolean isShown(){
		return isShown;
	}

	/**
	 * Met la ligne en pause (stop le mouvement)
	 */
	public void pause(){
		if(theService.doAnimation())
			theLine.animate().cancel();
		isMoving = false;
	}

	/**
	 * Supprime la ligne
	 */
	public void removeView(){
		if (theLine != null) {
			theService.removeView(theLine);
		}
	}

	/**
	 * Lance le déplacement de la ligne
	 */
	protected void start(){
		clavier = theService.getClickPanelCtrl().getKeyboard();
		float theDensity = theLine.getResources().getDisplayMetrics().density;

		isMoving = true;
		theLine.setVisibility(View.VISIBLE);
		currentColumn = 0;
		iterations = 0;

		if(theService.doAnimation()&&!clavier){
			verticalParams.height = theService.getScreenSize().y;
			verticalParams.width = theService.getScreenSize().x;
			verticalParams.y = 0;
			verticalParams.x = 0;
			density = theLine.getResources().getDisplayMetrics().density;
			theService.updateViewLayout(theLine, verticalParams);
			handler.postDelayed(slideRight, delay);
			isMovingRight=true;
		}
		else{
			verticalParams.width = lineThickness;
			if(clavier){
				verticalParams.height = (int) (300*theDensity);
				verticalParams.y = (int) (theService.getScreenSize().y-300*theDensity);
				verticalParams.x = (int) ((size.x/20)-12*theDensity);
			}
			else{
				verticalParams.height = theService.getScreenSize().y;
				verticalParams.y = 0;
				isMovingRight=true;
			}
			if(speed > 4 && speed <= 7) density *= 2;
			else if(speed > 7 && speed <= 10) density *= 3;
			theService.updateViewLayout(theLine, verticalParams);
			handler.postDelayed(runnable, delay);
		}


	}

	/**
	 * Arrête le déplacement de la ligne
	 */
	public void stop(){
		isMoving = false;
		if(theLine!=null)
			theLine.setVisibility(View.INVISIBLE);

		if(theService.doAnimation()){
			theLine.animate().cancel();
			theLine.setX(0);
			theLine.setY(0);
		}
		verticalParams.x = 0;
		verticalParams.y = 0;
		density = theLine.getResources().getDisplayMetrics().density;

		theService.updateViewLayout(theLine, verticalParams);
	}

	/**
	 * Déplace la ligne en sens inverse (de bas en haut)
	 */
	public void setInverse() {
		iterations = 0;
		if(theService.doAnimation()){
			theLine.animate().cancel();
			if(isMovingRight)
				handler.post(slideLeft);
			else
				handler.post(slideRight);
		}
		else{
			isMovingRight = !isMovingRight;
		}


	}

	/**
	 * Replace la ligne dans le coin supérieur supérieur gauche
	 */
	public void restart() {
		if(theService.doAnimation()){
			theLine.animate().cancel();
			theLine.setX(0);
			theLine.setY(0);
			handler.postDelayed(slideRight, delay);
		}
		verticalParams.x = 0;
		verticalParams.y = 0;
		density = theLine.getResources().getDisplayMetrics().density;

		theService.updateViewLayout(theLine, verticalParams);
	}



	/**
	 * Classe permettant de déplacer la ligne verticale
	 * @author OneSwitch_B
	 *
	 */
	class VerticalLineRunnable implements Runnable {
		@Override
		public void run(){
			try{
				if(getIterations() == ite){
					stop();
					theService.getHorizontalLineCtrl().stop();
				}
				if(isMoving){//Si la bare est censé bouger
					if((verticalParams.x <= size.x)&&(isMovingRight)){//Si elle va à droite
						if(!clavier)//Si le clavier est ouvert ou non
							verticalParams.x += density;
						else{
							if(currentColumn<9){//Si la barre n'est pas encore arrivé à droite ou non
								verticalParams.x += speedKeyboard;
								currentColumn++;
							}else{
								isMovingRight = false;
								addIterations();
							}
						}//Si la barre arrive à droite de l'écran
						if(verticalParams.x >= (size.x-density)){
							isMovingRight = false;
						}

					}
					else{//Si la barre va à gauche
						if(!clavier){//Si le clavier est ouvert ou non
							verticalParams.x -= density;
						}

						else{
							if(currentColumn>0){//Si la barre n'est pas encore arrivé à gauche ou non
								verticalParams.x -= speedKeyboard;
								currentColumn--;
							}else{
								isMovingRight = true;
								addIterations();
							}
						}//Si la barre arrive à gauche de l'écran
						if(verticalParams.x <= density){
							isMovingRight = true;
							addIterations();
						}


					}
					theService.updateViewLayout(theLine, verticalParams);
					if(!clavier)handler.postDelayed(this, (int) (55-(5*speed)));
					else handler.postDelayed(this, 1000);
				}
			}
			catch (Exception e) {}
		}

	}

}
