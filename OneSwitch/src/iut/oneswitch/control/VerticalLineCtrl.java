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
	private boolean stopIteration = false;
	private int ite;
	private float speedKeyboard;
	private int currentColumn = 0;
	private boolean clavier = false;

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
		return verticalParams.x;
	}

	public int getY(){
		return verticalParams.y;
	}

	/**
	 * Initialise la ligne
	 * Lance la ligne
	 */
	public void init(){
		//Objet contenant les préférences
		sp = PreferenceManager.getDefaultSharedPreferences(theService);
		
		handler = new Handler();
		runnable = new VerticalLineRunnable();

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
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);

		verticalParams.gravity = Gravity.TOP | Gravity.START;
		verticalParams.x = 0;
		verticalParams.y = 0;
		verticalParams.height = theService.getScreenSize().y;
		verticalParams.width = lineThickness;

		theService.addView(theLine, verticalParams);
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

		if(clavier){
			verticalParams.height = (int) (300*(theLine.getResources().getDisplayMetrics().density));
			verticalParams.y = (int) (theService.getScreenSize().y-300*(theLine.getResources().getDisplayMetrics().density));
			verticalParams.x = (int) (15*(theLine.getResources().getDisplayMetrics().density));

		}
		else{
			verticalParams.height = theService.getScreenSize().y;
			verticalParams.y = 0;
			isMovingRight=true;
		}
		isMoving = true;
		stopIteration = false;
		theLine.setVisibility(View.VISIBLE);
		if(speed > 4 && speed <= 7) density *= 2;
		else if(speed > 7 && speed <= 10) density *= 3;
		currentColumn = 0;
		iterations = 0;
		theService.updateViewLayout(theLine, verticalParams);
		handler.postDelayed(runnable, delay);
	}

	/**
	 * Arrête le déplacement de la ligne
	 */
	public void stop(){
		isMoving = false;
		if(theLine!=null)
			theLine.setVisibility(View.INVISIBLE);
		
		restart();
	}

	/**
	 * Déplace la ligne en sens inverse (de bas en haut)
	 */
	public void setInverse() {
		iterations = 0;
		if(isMovingRight) isMovingRight = false;
		else isMovingRight = true;
	}

	/**
	 * Replace la ligne dans le coin supérieur supérieur gauche
	 */
	public void restart() {
		verticalParams.x = 0;
		verticalParams.y = 0;
		theService.updateViewLayout(theLine, verticalParams);
		density = theLine.getResources().getDisplayMetrics().density;
	}
	
	

	class VerticalLineRunnable implements Runnable {
		@Override
		public void run(){
			try{
				if(getIterations() == ite){
					stop();
					theService.getHorizontalLineCtrl().stop();
				}
				if(isMoving){
					if((verticalParams.x <= size.x)&&(isMovingRight)){
						if(!clavier)
							verticalParams.x += density;
						else{
							if(currentColumn<9){
								verticalParams.x += speedKeyboard;
								currentColumn++;
							}else{
								isMovingRight = false;
								addIterations();
							}
						}
						if(verticalParams.x >= (size.x-density))
							isMovingRight = false;
					} 
					else{
						if(!clavier)
							verticalParams.y -= density;
						else{
							if(currentColumn>0){
								System.out.println("LIIINNNEEE    "+currentColumn);
								verticalParams.x -= speedKeyboard;
								currentColumn--;
							}else{
								isMovingRight = true;
								addIterations();
							}
						}
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
	
	/*private class VerticalLineTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values){
			super.onProgressUpdate(values);
			if(!stopIteration)
				theService.updateViewLayout(theLine, verticalParams);
			else{
				stop();
				theService.getHorizontalLineCtrl().restart();
			}
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {}
			while(isMoving){
				if (getIterations()== Integer.parseInt(sp.getString("iterations","3"))){
					stopIteration = true;
					this.publishProgress(arg0);
				}
				if((verticalParams.x <= size.x)&&(isMovingRight)){
					verticalParams.x += density;
					
					if(verticalParams.x >= (size.x - density))
						isMovingRight = false;
				}
				else{
					verticalParams.x -= density;
					if(verticalParams.x <= density){
						isMovingRight = true;
						addIterations();
					}
				}
				
				try {
					Thread.sleep((int)(50/speed));
				} catch (InterruptedException e) {}
				this.publishProgress(arg0);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}*/

	
}
