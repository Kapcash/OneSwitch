package iut.oneswitch.control;

import iut.oneswitch.app.OneSwitchService;
import iut.oneswitch.view.HorizontalLine;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Controlleur de la ligne horizontale.
 * Indique ses différents états, initialise ses paramètres, l'active ou la désactive.
 */
public class HorizontalLineCtrl {
	private WindowManager.LayoutParams horizParams;
	/**
	 * Indique si la ligne se déplace.
	 */
	private boolean isMoving = false;
	/**
	 * Indique si la ligne se déplace vers le bas (vers la navigation bar).
	 */
	private boolean isMovingDown = true;
	/**
	 * Indique si la ligne est affichée ou non.
	 */
	private boolean isShown = false;
	/**
	 * Nombre d'itérations (aller-retours) de la ligne depuis son lancement.
	 */
	private int iterations;
	/**
	 * Epaisseur de la ligne, en pixel.
	 */
	private int lineThickness;

	/**
	 * Vitesse de déplacement de la ligne.
	 */
	private float speed;
	private float speedKeyboard;
	private HorizontalLine theLine;
	private OneSwitchService theService;
	private Point size;
	private SharedPreferences sp;
	private float density;
	private Handler handler;
	private HorizLineRunnable runnable;
	private Runnable slideUp, slideDown;
	private int delay;
	private int ite;
	private int currentLine=0;
	private boolean clavier = false;

	/**
	 * Initialise le controlleur avec le service.
	 * @param paramOneSwitchService Le service (contexte) de l'application.
	 */
	public HorizontalLineCtrl(OneSwitchService paramOneSwitchService){
		theService = paramOneSwitchService;
		init();
	}

	/**
	 * Ajoute une iterations à la ligne, lorsqu'elle a terminé un aller-retour.
	 */
	public void addIterations(){
		iterations++;
	}

	/**
	 *
	 * @return Retourne le nombre d'itérations actuel de la ligne.
	 */
	public int getIterations(){
		return iterations;
	}

	/**
	 *
	 * @return Retourne l'épaisseur de la ligne.
	 */
	public int getThickness(){
		return lineThickness;
	}

	/**
	 *
	 * @return Retourne la position verticale de la ligne (en pixel).
	 */
	public int getY(){
		if(theService.doAnimation() && !clavier)
			return (int)theLine.getY();
		else
			return horizParams.y;
	}

	/**
	 * Initialise la ligne sur le coin supérieur gauche de l'écran.
	 * Lance la ligne.
	 */
	public void init(){
		//Object contenant les préférences
		sp = PreferenceManager.getDefaultSharedPreferences(theService);

		size = theService.getScreenSize();
		theLine = new HorizontalLine(theService);
		theLine.setVisibility(4);
		theLine.setId(200);

		ite = Integer.parseInt(sp.getString("iterations","3"));


		density = theLine.getResources().getDisplayMetrics().density;
		speedKeyboard = 60;
		speedKeyboard *= density;
		//Récupère la vitesse
		speed = sp.getInt("lign_speed",5);

		//Récupère le délai de départ de la ligne
		delay = Integer.parseInt(sp.getString("delay","1000"));

		//Récupère l'épaisseur de la ligne
		lineThickness = Integer.parseInt(sp.getString("lign_size","3"));
		lineThickness *= density;

		horizParams = new WindowManager.LayoutParams(
				theService.getScreenSize().x,
				theService.getScreenSize().y,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		theLine.setFocusable(false);
		theLine.setFocusableInTouchMode(false);

		horizParams.gravity = Gravity.TOP | Gravity.START;
		horizParams.x = 0;
		horizParams.y = 0;

		handler = new Handler();
		runnable = new HorizLineRunnable();
		horizParams.width = theService.getScreenSize().x;
		if(theService.doAnimation()){
			horizParams.height = theService.getScreenSize().y;
			theService.addView(theLine, horizParams);
			slideDown = new Runnable(){
				@Override
				public void run() {
					if (getIterations()>= ite){
						stop();
					}
					else{
						long distance = (long) (theService.getScreenSize().y - theLine.getY());
						long duration = (long)(distance/((double)(speed)/12));
						isMovingDown = true;
						theLine.setY(theLine.getY());
						theLine.animate().setDuration(duration).setInterpolator(null).y(theService.getScreenSize().y).withEndAction(slideUp).start();
						addIterations();
					}
				}
			};

			slideUp = new Runnable(){
				@Override
				public void run() {
					long distance = (long) (theLine.getY());
					long duration = (long)(distance/((double)(speed)/12));
					isMovingDown = false;
					theLine.setY(theLine.getY());
					theLine.animate().setDuration(duration).setInterpolator(null).y(0).withEndAction(slideDown).start();
				}
			};
		}
		else{
			horizParams.height = lineThickness;
			theService.addView(theLine, horizParams);
		}
	}

	/**
	 * @return Retourne "true" si la ligne se déplace, "false" sinon.
	 */
	public boolean isMoving(){
		return isMoving;
	}

	/**
	 * @return Retourne "true" si la ligne est visible.
	 */
	public boolean isShown(){
		return isShown;
	}

	/**
	 * Met la ligne en pause (stop le mouvement).
	 */
	public void pause(){
		if(theService.doAnimation())
			theLine.animate().cancel();
		isMoving = false;
	}

	/**
	 * Supprime la ligne.
	 */
	public void removeView(){
		if (theLine != null) {
			theService.removeView(theLine);
		}
	}

	/**
	 * Lance le déplacement de la ligne.
	 */
	protected void start(){
		currentLine = 0;
		clavier = theService.getClickPanelCtrl().keyboard();
		float theDensity = theLine.getResources().getDisplayMetrics().density;

		horizParams.y = 0;
		horizParams.x = 0;
		isMoving = true;
		theLine.setVisibility(View.VISIBLE);

		horizParams.width = theService.getScreenSize().x;
		if(theService.doAnimation() && !clavier){
			horizParams.height = theService.getScreenSize().y;
			horizParams.y = 0;
			horizParams.x = 0;
			density = theLine.getResources().getDisplayMetrics().density;
			isMovingDown=true;
			theService.updateViewLayout(theLine, horizParams);
			handler.postDelayed(slideDown, 1000);
		}
		else{
			horizParams.height = lineThickness;
			if(clavier){
				horizParams.y = (int) (theService.getScreenSize().y-18*theDensity);
				isMovingDown=false;
			}
			else{
				horizParams.y=0;
				isMovingDown=true;
			}
			if(speed > 4 && speed <= 7) density *= 2;
			else if(speed > 7 && speed <= 10) density *= 3;
			theService.updateViewLayout(theLine, horizParams);
			handler.postDelayed(runnable, delay);
		}
		iterations = 0;
	}

	/**
	 * Arrête le déplacement de la ligne
	 */
	public void stop(){
		isMoving = false;
		theLine.setVisibility(View.INVISIBLE);

		if(theService.doAnimation()){
			theLine.animate().cancel();
			theLine.setX(0);
			theLine.setY(0);
		}
		else{
			horizParams.x = 0;
			horizParams.y = 0;
			density = theLine.getResources().getDisplayMetrics().density;
		}
		theService.updateViewLayout(theLine, horizParams);
	}

	/**
	 * Déplace la ligne en sens inverse (de bas en haut)
	 */
	public void setInverse() {
		if(theService.doAnimation()){
			theLine.animate().cancel();
			if(isMovingDown)
				handler.post(slideUp);
			else
				handler.post(slideDown);
		}
		else{
			isMovingDown = !isMovingDown;
		}
		iterations = 0;
	}

	/**
	 * Replace la ligne dans le coin supérieur supérieur gauche
	 */
	public void restart() {
		if(theService.doAnimation()){
			theLine.animate().cancel();
			theLine.setX(0);
			theLine.setY(0);
			handler.postDelayed(slideDown, 1000);
		}
		else{
			horizParams.x = 0;
			horizParams.y = 0;
			density = theLine.getResources().getDisplayMetrics().density;
		}
		theService.updateViewLayout(theLine, horizParams);
	}

	/**
	 * Classe permettant de déplacer la ligne horizontale
	 * @author OneSwitch_B
	 *
	 */
	class HorizLineRunnable implements Runnable{
		@Override
		public void run(){
			try{
				if (getIterations()== ite){
					stop();
				}
				if(isMoving){//Si la bare est censé bouger
					if((horizParams.y <= size.y)&&(isMovingDown)){//Si elle va en bas
						if(!clavier)//Si le clavier est ouvert ou non
							horizParams.y += density;
						else{
							if(currentLine>0){//Si la barre n'est pas encore arrivé en bas ou non
								horizParams.y += speedKeyboard;
								currentLine--;
							}else{
								isMovingDown = false;
								addIterations();
							}
						}//Si la barre arrive en bas de l'écran
						if(horizParams.y >= (size.y-density))
							isMovingDown = false;
					}
					else{//Si la barre va en haut
						if(!clavier)//Si le clavier est ouvert ou non
							horizParams.y -= density;
						else{
							if(currentLine<4){//Si la barre n'est pas encore arrivé en haut ou non
								horizParams.y -= speedKeyboard;
								currentLine++;
							}else{
								isMovingDown = true;
								addIterations();
							}
						}
						//Si la barre arrive en haut de l'écran
						if(horizParams.y <= density){
							isMovingDown = true;
							addIterations();
						}
					}
					theService.updateViewLayout(theLine, horizParams);
					if(!clavier)handler.postDelayed(this, (int) (55-(5*speed)));
					else handler.postDelayed(this,1000);
				}
			}


			catch (Exception localException) {}
		}

	}

}
