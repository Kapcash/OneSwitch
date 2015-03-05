package com.example.oneswitch.control;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.example.oneswitch.app.OneSwitchService;
import com.example.oneswitch.view.VerticalLine;

public class VerticalLineCtrl
{
	private Handler handler;
	private boolean isMoving = false;
	private boolean isMovingRight = true;
	private boolean isShown = false;
	private int iterations;
	private int lineThickness;
	private Runnable runnable;
	private float speed;
	private VerticalLine theLine;
	private OneSwitchService theService;
	private WindowManager.LayoutParams verticalParams;

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
		theLine = new VerticalLine(theService);
		theLine.setVisibility(4);
		theLine.setId(200);
		speed = 3;
		speed *= theLine.getResources().getDisplayMetrics().density;
		lineThickness = 5;
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
		handler = new Handler();
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
		theLine.setVisibility(View.INVISIBLE);
		verticalParams.x = 0;
		verticalParams.y = 0;
		theService.updateViewLayout(theLine, verticalParams);
	}

	class VerticalLineRunnable implements Runnable {
		@Override
		public void run(){
			try{
				if (getIterations() == 3){
					pause();
					stop();
					theService.getHorizontalLineCtrl().pause();
					theService.getHorizontalLineCtrl().stop();
				}
				Point size = theService.getScreenSize();
				if((verticalParams.x <= size.x)&&(isMovingRight == true)){
					verticalParams.x += speed;

					theService.updateViewLayout(theLine, verticalParams);
					if(verticalParams.x >= (size.x -speed))
						isMovingRight = false;
				}
				else{
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

	public void setInverse() {
		iterations = 0;
		if(isMovingRight) isMovingRight = false;
		else isMovingRight = true;
		
	}
}
