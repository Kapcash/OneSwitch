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

public class VerticalLineCtrl{
	private boolean isMoving = false;
	private boolean isMovingRight = true;
	private boolean isShown = false;
	private int iterations;
	private int lineThickness;
	//private VerticalLineTask mTask;
	private VerticalLineRunnable runnable;
	private Handler handler = new Handler();
	private float speed;
	private VerticalLine theLine;
	private OneSwitchService theService;
	private WindowManager.LayoutParams verticalParams;
	private Point size;
	private SharedPreferences sp;

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

	public void init(){
		//The object containing all preferences
		sp = PreferenceManager.getDefaultSharedPreferences(theService);
		
		size = theService.getScreenSize();
		theLine = new VerticalLine(theService);
		theLine.setVisibility(4);
		theLine.setId(200);
		
		speed = Integer.parseInt(sp.getString("lign_speed","4"));
		speed = speed *theLine.getResources().getDisplayMetrics().density;
		
		lineThickness = Integer.parseInt(sp.getString("lign_size","3"));
		
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
		
		theService.addView(theLine, verticalParams);
		runnable = new VerticalLineRunnable();
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
		handler.postDelayed(runnable, 1000);
		iterations = 0;
	}

	public void stop(){
		isMoving = false;
		//mTask.cancel(true);
		theLine.setVisibility(View.INVISIBLE);
		verticalParams.x = 0;
		verticalParams.y = 0;
		theService.updateViewLayout(theLine, verticalParams);
	}

	public void setInverse() {
		iterations = 0;
		if(isMovingRight) isMovingRight = false;
		else isMovingRight = true;

	}
	
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


}
