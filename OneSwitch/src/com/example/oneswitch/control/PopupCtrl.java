package com.example.oneswitch.control;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.oneswitch.app.OneSwitchService;
import com.example.oneswitch.view.CircleView;
import com.example.oneswitch.view.PopupView;

public class PopupCtrl
{
	private CircleView circle;
	private WindowManager.LayoutParams circleParams;
	private float density;
	private Handler handler;
	private boolean isStarted = false;
	private WindowManager.LayoutParams popupParams;
	private int posX = 0;
	private int posY = 0;
	private PopupRunnable runnable;
	private PopupView thePopup;
	private OneSwitchService theService;

	public PopupCtrl(OneSwitchService service, int x, int y){
		posX = x;
		posY = y;
		theService = service;
		init();
	}

	private void init(){
		int widthCircle = 28;
		int heightCircle = 28;
		int largeurTrait = 2;
		
		density = theService.getResources().getDisplayMetrics().density;
		circle = new CircleView(theService, this);
		circleParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		circleParams.gravity = Gravity.TOP | Gravity.LEFT;
		circleParams.x = (int) (this.posX-((widthCircle/largeurTrait)*density));
		circleParams.y = (int) (this.posY-((heightCircle/largeurTrait)*density));
		circleParams.height = (int) (density*heightCircle);
		circleParams.width  = (int) (density*widthCircle);
		theService.addView(circle, circleParams);

		thePopup = new PopupView(theService, this);
		popupParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		popupParams.gravity = Gravity.TOP | Gravity.LEFT;

		
		int heightPopup = 160;
		int widthPopup = 190;
		
		popupParams.width  = (int) (density*widthPopup);
		popupParams.height = (int) (density*heightPopup);
		int rightPadding = (theService.getScreenSize().x - posX);
		if(rightPadding>(heightPopup*density))	popupParams.x = circleParams.x;
		else	popupParams.x = (int) circleParams.x - popupParams.width;
		popupParams.y = (int) (this.posY-((heightPopup/2)*density));
		theService.addView(thePopup, popupParams);
		
		handler = new Handler();
		runnable = new PopupRunnable();
	}

	public Point getPos(){
		return new Point(posX, posY);
	}

	public View getSelected(){
		Button localButton = null;
		if (thePopup != null) {
			localButton = thePopup.getSelected();
		}
		return localButton;
	}

	public OneSwitchService getService(){
		return theService;
	}

	public boolean isShow(){
		return isStarted;
	}

	public void removeCircle(){
		if (circle != null) {
			theService.removeView(circle);
			circle = null;
		}
	}

	public void closePopup(){
		isStarted = false;
		if (thePopup != null) {
			theService.removeView(thePopup);
			thePopup = null;
		}
	}

	public void removeAllViews(){
		isStarted = false;
		if (thePopup != null) {
			theService.removeView(thePopup);
			thePopup = null;
		}
		if (circle != null) {
			theService.removeView(circle);
			circle = null;
		}
	}

	public void start(){
		isStarted = true;
		handler.post(runnable);
	}

	class PopupRunnable implements Runnable{
		public void run(){
			if (isStarted){
				thePopup.selectNext();
				handler.postDelayed(this, 1000);
			}
		}
	}
}
