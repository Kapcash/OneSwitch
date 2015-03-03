package com.example.oneswitch.control;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.example.oneswitch.appliction.OneSwitchService;
import com.example.oneswitch.view.HorizontalLine;

public class HorizontalLineCtrl{
	
	private int lineThickness;
	private float speed;
	private OneSwitchService theService;
	private Handler handler;
	private Runnable runnable;
	private HorizontalLine theLine;
	private WindowManager.LayoutParams horizParams;
	private boolean isShown = false;
	private boolean isMoving = false;
	private boolean isMovingDown = true;
	private int iterations;
	
	public HorizontalLineCtrl(OneSwitchService service) {
		this.theService = service;
		init();
		

	}

	public void init(){
		theLine = new HorizontalLine(theService);
		theLine.setVisibility(View.INVISIBLE);
		theLine.setId(200);
		
		speed = 3;
		this.speed *= theLine.getResources().getDisplayMetrics().density;
		lineThickness = 5;
		
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
		
		
		
		theService.addView(theLine, horizParams);
		

		handler = new Handler();
		runnable = new HorizLineRunnable();
	}


	public void removeView(){
		if(theLine!=null){
			theService.removeView(theLine);
		}
	}

	protected void start(){	
		isMoving = true;
		theLine.setVisibility(View.VISIBLE);
		handler.postDelayed(runnable, 1000); //start après 1 seconde
		iterations=0;
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
		return horizParams.x;
	}

	public int getY(){
		return horizParams.y;
	}

	public int getThickness(){
		return lineThickness;
	}
	
	public int getIterations(){
		return iterations;
	}
	
	public void addIterations(){
		iterations++;
	}
	
	public void stop(){
		isMoving = false;
		theLine.setVisibility(View.INVISIBLE);
		horizParams.x = 0;
		horizParams.y = 0;
		theService.updateViewLayout(theLine, horizParams);
	}

	class HorizLineRunnable implements Runnable{

		@Override
		public void run() {
			try {
				if(getIterations()==3){
					pause();
					stop();
				}
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
					if(horizParams.y <= (0+speed)){
						isMovingDown = true;
						addIterations();
					}
				}
				if(isMoving) //si elle doit continuer de bouger, alors on planifie le prochain mouvement
					handler.postDelayed(this, 10);
			} catch (Exception e) {
				// TODO: handle exception
				
			}
		}
	}
}
