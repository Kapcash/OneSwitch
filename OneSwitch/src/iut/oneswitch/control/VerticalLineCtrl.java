package iut.oneswitch.control;

import iut.oneswitch.app.OneSwitchService;
import iut.oneswitch.view.VerticalLine;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.AsyncTask;
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
	private VerticalLine theLine;
	private OneSwitchService theService;
	private WindowManager.LayoutParams verticalParams;
	private Point size;
	private SharedPreferences sp;
	private VerticalLineTask verticalTask;

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
		//The object containing all preferences
		sp = PreferenceManager.getDefaultSharedPreferences(theService);

		size = theService.getScreenSize();
		theLine = new VerticalLine(theService);
		theLine.setVisibility(4);
		theLine.setId(200);

		//Get the speed from preferences
		speed = sp.getInt("lign_speed",5);
		speed = speed *theLine.getResources().getDisplayMetrics().density;

		//Get the line size from preferences
		lineThickness = Integer.parseInt(sp.getString("lign_size","3"));
		lineThickness *= theLine.getResources().getDisplayMetrics().density;

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
	 * 
	 * @return Retourne "true" si la ligne se déplace, "false" sinon
	 */
	public boolean isMoving(){
		return isMoving;
	}

	
	/**
	 * 
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
		isMoving = true;
		stopIteration = false;
		theLine.setVisibility(View.VISIBLE);
		verticalTask = new VerticalLineTask();
		verticalTask.execute();
		iterations = 0;
	}

	/**
	 * Arrête le déplacement de la ligne
	 */
	public void stop(){
		isMoving = false;
		if(isMoving && verticalTask!=null)
			verticalTask.cancel(true);
		
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
	}
	
private boolean stopIteration = false;
	
	class VerticalLineTask extends AsyncTask<Void, Void, Void>{

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
					verticalParams.x += speed;
					
					if(verticalParams.x >= (size.x-speed))
						isMovingRight = false;
				}
				else{
					verticalParams.x -= speed;
					if(verticalParams.x <= speed){
						isMovingRight = true;
						addIterations();
					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
				this.publishProgress(arg0);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}
/*
	class VerticalLineRunnable implements Runnable {
		@Override
		public void run(){
			try{
				if(getIterations() == Integer.parseInt(sp.getString("iterations","3"))){
					stop();
					theService.getHorizontalLineCtrl().stop();
				}
				if(isMoving){
					if((verticalParams.x <= size.x)  && (isMovingRight)){
						verticalParams.x += speed;
						if(verticalParams.x >= (size.x -speed))
							isMovingRight = false;
					}
					else{
						verticalParams.x -= speed;
						if(verticalParams.x <= (0+speed)){
							isMovingRight = true;
							addIterations();
						}
					}
					theService.updateViewLayout(theLine, verticalParams);
					handler.postDelayed(this, 10);
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
*/
}
