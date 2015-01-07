package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;

import com.iut.oneswitch.view.popup.CircleView;
import com.iut.oneswitch.view.popup.PopUpView;

public class PopUpCtrl {
	private OneSwitchService theService;
	private PopUpView thePopup;
	private CircleView circle;
	private WindowManager.LayoutParams popupParams;
	private WindowManager.LayoutParams circleParams;
	private int posX, posY;
	private boolean isRunning;

	private Handler handler;
	private Runnable runnable;
	
	
	public PopUpCtrl(OneSwitchService service, int x, int y) {
		this.theService = service;
		this.posX = x;
		this.posY = y;
		isRunning = false;
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
			circleParams.height = (int)density*28;
			circleParams.width  = (int)density*28;
			theService.addView(circle, circleParams);
		/*End Drawing Circle*/
			
			
		/* Drawing Popup */
			thePopup = new PopUpView(theService, this);
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
			popupParams.height = (int)density*152;
			popupParams.width  = (int)density*152;
			theService.addView(thePopup, popupParams);
		/*End Drawing Popup*/
			
		
		
		handler = new Handler();
		runnable = new PopupRunnable();
	}
	
	
	
	public void removeView(){
		theService.removeView(thePopup);
		theService.removeView(circle);
	}

	public void startThread(){
		isRunning = true;
		handler.post(runnable);
	}
	
	public void stopThread(){
		isRunning = false;
	}
	
	public Button getSelected(){
		return thePopup.getSelected();
	}
	
	public Point getPos(){
		return new Point(posX,posY);
	}

	/**
	 * Permet la selection d'un bouton du menu popup
	 * @author OneSwitch B
	 *
	 */
	class PopupRunnable implements Runnable{

		/**
		 * Permet le défilement des boutons
		 */
		@Override
		public void run() {
			try {
				if(isRunning){ 
					thePopup.selectNext();
					handler.postDelayed(this, 1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}