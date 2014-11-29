package com.iut.oneswitch.application;

import com.iut.oneswitch.view.VerticalLine;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;


public class VerticalLineCtrl {
	private int lineThickness = 3;
	private int speed = 3;

	private OneSwitchService theService;
	private Handler handler;
	private Runnable runnable;

	private VerticalLine theLine;
	private WindowManager.LayoutParams verticalParams;
	private boolean isShown = false;
	private boolean isMoving = false;
	private boolean isMovingRight = true;

	public VerticalLineCtrl(OneSwitchService service) {
		this.theLine = new VerticalLine(service);
		this.theService = service;
		
	}

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
	
	public void add(){
		init();
		if(!isShown){
			theService.addView(theLine, verticalParams);
			isShown = true;
			start();
		}

	}

	public void remove(){
		if((isShown)&&(!isMoving)){
			theService.removeView(theLine);
			isShown = false;
		}

	}

	private void start(){	
		isMoving = true;
		handler.postDelayed(runnable, 1000); //start après 1 seconde
	}

	public void pause(){
		isMoving = false;
	}



	
	

	public boolean isShown() {
		return isShown;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public int getX(){
		return verticalParams.x;
	}





	class VerticalLineRunnable implements Runnable{

		@Override
		public void run() {
			try {
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
					if(verticalParams.x <= (0+speed))
						isMovingRight = true;
				}
				if(isMoving)
					handler.postDelayed(this, 10);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}
}


