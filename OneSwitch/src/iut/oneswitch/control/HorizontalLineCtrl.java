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

public class HorizontalLineCtrl
{
	private Handler handler;
	private WindowManager.LayoutParams horizParams;
	private boolean isMoving = false;
	private boolean isMovingDown = true;
	private boolean isShown = false;
	private int iterations;
	private int lineThickness;
	private Runnable runnable;
	private float speed;
	private HorizontalLine theLine;
	private OneSwitchService theService;
	private Point size;
	private SharedPreferences sp;

	public HorizontalLineCtrl(OneSwitchService paramOneSwitchService){
		theService = paramOneSwitchService;
		init();
	}

	public void addIterations(){
		iterations++;
	}

	public int getIterations(){
		return iterations;
	}

	public int getThickness(){
		return lineThickness;
	}

	public int getX(){
		return horizParams.x;
	}

	public int getY(){
		return horizParams.y;
	}

	public void init(){
		//The object containing all preferences
		sp = PreferenceManager.getDefaultSharedPreferences(theService);
		
		size = theService.getScreenSize();
		theLine = new HorizontalLine(theService);
		theLine.setVisibility(4);
		theLine.setId(200);
		
		//Get the speed from preferences
		speed = Integer.parseInt(sp.getString("lign_speed","4"));
		speed *= theLine.getResources().getDisplayMetrics().density;
		
		//Get the line size from preferences
		lineThickness = Integer.parseInt(sp.getString("lign_size","3"));
		lineThickness *= theLine.getResources().getDisplayMetrics().density;
		
		horizParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
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
		handler = new Handler();
		runnable = new HorizLineRunnable();
	}

	public boolean isMoving(){
		return isMoving;
	}

	public boolean isShown(){
		return isShown;
	}

	public void pause(){
		isMoving = false;
	}

	public void removeView(){
		if (theLine != null) {
			theService.removeView(theLine);
		}
	}

	protected void start(){
		isMoving = true;
		theLine.setVisibility(View.VISIBLE);
		handler.postDelayed(runnable, Integer.parseInt(sp.getString("Delay", "1000")));
		iterations = 0;
	}

	public void stop(){
		isMoving = false;
		theLine.setVisibility(View.INVISIBLE);
		horizParams.x = 0;
		horizParams.y = 0;
		theService.updateViewLayout(theLine, horizParams);
	}
	
	public void setInverse() {
		iterations = 0;
		if(isMovingDown) isMovingDown = false;
		else isMovingDown = true;
		
	}
	

	public void restart() {
		horizParams.x = 0;
		horizParams.y = 0;
		theService.updateViewLayout(theLine, horizParams);
	}

	class HorizLineRunnable implements Runnable{
		public void run(){
			try{
				if (getIterations()== Integer.parseInt(sp.getString("iterations","3"))){
					stop();
				}
				if(isMoving){
					if((horizParams.y <= size.y)&&(isMovingDown)){
						horizParams.y += speed;
						
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
					theService.updateViewLayout(theLine, horizParams);
					handler.postDelayed(this, 10);
				}	
			}
			catch (Exception localException) {}
		}
	}

}
