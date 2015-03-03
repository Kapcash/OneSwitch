package com.example.oneswitch.control;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.oneswitch.appliction.OneSwitchService;
import com.example.oneswitch.view.CircleView;
import com.example.oneswitch.view.PopupView;

public class PopupCtrl {
	private OneSwitchService theService;
	private CircleView circle;
	private WindowManager.LayoutParams circleParams;
	private PopupView thePopup;
	private WindowManager.LayoutParams popupParams;
	private int posX = 0;
	private int posY = 0;
	private float density;
	private PopupRunnable runnable;
	private Handler handler;
	private boolean isStarted = false;


	public PopupCtrl(OneSwitchService service, int x, int y){
		posX = x;
		posY = y;
		theService = service;
		init();
	}
	
	public OneSwitchService getService(){
		return theService;
	}

	private void init(){
		float density = theService.getResources().getDisplayMetrics().density;

		/* Drawing Circle */
		circle = new CircleView(theService, this);
		circleParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		circleParams.gravity = Gravity.TOP | Gravity.LEFT;
		circleParams.x = (int) (this.posX-(14*density)); //14 = rayon du cercle (12) + largeur du trait (2)
		circleParams.y = (int) (this.posY-(14*density));
		circleParams.height = (int) (density*28);
		circleParams.width  = (int) (density*28);
		theService.addView(circle, circleParams);
		/*End Drawing Circle*/

		/* Drawing Popup */
		thePopup = new PopupView(theService, this);
		popupParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		popupParams.gravity = Gravity.TOP | Gravity.LEFT;

		int rightPadding = (theService.getScreenSize().x - posX);
		if(rightPadding>(140*density)) popupParams.x = (int) (this.posX-(14*density)); //14 = rayon du cercle (12) + largeur du trait (2)
		else  popupParams.x = (int) (this.posX-(166*density));
		popupParams.y = (int) (this.posY-(76*density));
		popupParams.height = (int) (density*152);
		popupParams.width  = (int) (density*152);
		theService.addView(thePopup, popupParams);
		/*End Drawing Popup*/

		handler = new Handler();
		runnable = new PopupRunnable();
	}
	
	public View getSelected(){
		Button ret = null;
		if(thePopup!=null){
			ret = thePopup.getSelected();
		}
		return ret;
	}

	public void start(){
		isStarted = true;
		handler.post(runnable);
	}

	public Point getPos(){
		return new Point(posX,posY);
	}

	public boolean isShow(){
		return isStarted;
	}


	public void removeView(){
		isStarted = false;
		if(thePopup != null) theService.removeView(thePopup);
		if(circle != null) theService.removeView(circle);
	}

	class PopupRunnable implements Runnable{
		@Override
		public void run() {
			if(isStarted){
				thePopup.selectNext();
				handler.postDelayed(this, 1000);
			}

		}
	}
}

