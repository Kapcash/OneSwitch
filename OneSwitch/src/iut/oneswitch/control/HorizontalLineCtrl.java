package iut.oneswitch.control;

import iut.oneswitch.app.OneSwitchService;
import iut.oneswitch.view.HorizontalLine;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Controlleur de la ligne horizontale
 * Indique ses différents états, initialise ses paramètres, l'active ou la désactive
 */
public class HorizontalLineCtrl {
	private WindowManager.LayoutParams horizParams;
	/**
	 * Indique si la ligne se déplace
	 */
	private boolean isMoving = false;
	/**
	 * Indique si la ligne se déplace vers le bas (vers la navigation bar)
	 */
	private boolean isMovingDown = true;
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
	private HorizontalLine theLine;
	private OneSwitchService theService;
	private Point size;
	private int posY = 0;
	private SharedPreferences sp;
	private HorizLineTask horizTask;

	/**
	 * Initialise le controlleur avec le service
	 * @param paramOneSwitchService Le service (contexte) de l'application
	 */
	public HorizontalLineCtrl(OneSwitchService paramOneSwitchService){
		theService = paramOneSwitchService;
		init();
	}

	/**
	 * Ajoute une iterations à la ligne, lorsqu'elle a terminé un aller-retour
	 */
	public void addIterations(){
		iterations++;
	}

	/**
	 * 
	 * @return Retourne le nombre d'itérations actuel de la ligne
	 */
	public int getIterations(){
		return iterations;
	}

	/**
	 * 
	 * @return Retourne l'épaisseur de la ligne
	 */
	public int getThickness(){
		return lineThickness;
	}

	/**
	 * 
	 * @return Retourne la position horizontale de la ligne (en pixel)
	 */
	public int getX(){
		return horizParams.x;
	}

	/**
	 * 
	 * @return Retourne la position verticale de la ligne (en pixel)
	 */
	public int getY(){
		return horizParams.y;
	}

	/**
	 * Initialise la ligne sur le coin supérieur gauche de l'écran
	 * Lance la ligne
	 */
	public void init(){
		//The object containing all preferences
		sp = PreferenceManager.getDefaultSharedPreferences(theService);
		
		size = theService.getScreenSize();
		theLine = new HorizontalLine(theService);
		theLine.setVisibility(4);
		theLine.setId(200);
		
		//Get the speed from preferences
		speed = sp.getInt("lign_speed",5);
		speed *= theLine.getResources().getDisplayMetrics().density;
		
		//Get the line size from preferences
		lineThickness = Integer.parseInt(sp.getString("lign_size","3"));
		lineThickness *= theLine.getResources().getDisplayMetrics().density;
		
		horizParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);

		horizParams.gravity = Gravity.TOP | Gravity.START;
		horizParams.x = 0;
		horizParams.y = 0;
		horizParams.height = lineThickness;
		horizParams.width = theService.getScreenSize().x;
		
		theService.addView(theLine, horizParams);
		
		//runnable = new HorizLineRunnable();
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
		stopIteration = false;
		isMoving = true;
		theLine.setVisibility(View.VISIBLE);
		horizTask = new HorizLineTask();
		horizTask.execute();
		iterations = 0;
	}

	/**
	 * Arrête le déplacement de la ligne
	 */
	public void stop(){
		if(isMoving && horizTask!=null)
			horizTask.cancel(true);
		isMoving = false;
		theLine.setVisibility(View.INVISIBLE);
		restart();
	}
	
	/**
	 * Déplace la ligne en sens inverse (de bas en haut)
	 */
	public void setInverse() {
		iterations = 0;
		isMovingDown = !isMovingDown;
	}

	/**
	 * Replace la ligne dans le coin supérieur supérieur gauche
	 */
	public void restart() {
		horizParams.x = 0;
		horizParams.y = 0;
		theService.updateViewLayout(theLine, horizParams);
	}
	
private boolean stopIteration = false;
	
	class HorizLineTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values){
			super.onProgressUpdate(values);
			if(!stopIteration){
				theService.updateViewLayout(theLine, horizParams);
			}
			else{
				stop();
			}
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {}
			while(isMoving){
				if (getIterations()== Integer.parseInt(sp.getString("iterations","3"))){
					this.publishProgress(arg0);
				}
				if((horizParams.y <= size.y)&&(isMovingDown)){
					horizParams.y += speed;
					//TODO Changer utilisation de la vitesse
					if(horizParams.y >= (size.y-speed))
						isMovingDown = false;
				}
				else{
					horizParams.y -= speed;
					if(horizParams.y <= speed){
						isMovingDown = true;
						addIterations();
					}
				}
				try {
					Thread.sleep(10); //TODO ?
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
	class HorizLineRunnable implements Runnable{
		public void run(){
			try{
				if (getIterations()== Integer.parseInt(sp.getString("iterations","3"))){
					stop();
				}
				if(isMoving){
					if((horizParams.y <= size.y)&&(isMovingDown)){
						horizParams.y += speed;
						posY++;
						
						if(horizParams.y >= (size.y-speed))
							isMovingDown = false;
					}
					else{
						horizParams.y -= speed;
						//posY--;
						if(horizParams.y <= speed){
							isMovingDown = true;
							addIterations();
						}
					}
					theService.updateViewLayout(theLine, horizParams);
					handler.postDelayed(this, 10);
				}
				
			}
			catch (Exception localException) {}
		}
		
	}*/

}
