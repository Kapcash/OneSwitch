package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.iut.oneswitch.view.HorizontalLine;

public class HorizontalLineCtrl {
	private int lineThickness = 3;
	private int speed = 3;

	private OneSwitchService theService;
	private Handler handler;
	private Runnable runnable;

	private HorizontalLine theLine;
	private WindowManager.LayoutParams horizParams;
	private boolean isShown = false;
	private boolean isMoving = false;
	private boolean isMovingDown = true;

	public HorizontalLineCtrl(OneSwitchService service) {
		this.theLine = new HorizontalLine(service);
		this.theService = service;
		
	}

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
	
	public void initSize(){
		horizParams.width = theService.getScreenSize().x;
		theService.updateViewLayout(theLine, horizParams);
	}

	public void add(){
		init();
		if(!isShown){
			System.out.println("horiz added");
			theService.addView(theLine, horizParams);
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

	public int getY(){
		return horizParams.y;
	}




	class HorizLineRunnable implements Runnable{

		@Override
		public void run() {
			try {
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
					if(horizParams.y <= (0+speed))
						isMovingDown = true;
				}
				if(isMoving) //si elle doit continuer de bouger, alors on planifie le prochain mouvement
					handler.postDelayed(this, 10);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}
}


