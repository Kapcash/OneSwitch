package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;

import com.iut.oneswitch.view.popup.PopUpView;

public class PopUpCtrl {
	private OneSwitchService theService;
	private PopUpView thePopup;
	private WindowManager.LayoutParams popupParams;
	private int posX, posY;

	private Handler handler;
	private Runnable runnable;
	
	public PopUpCtrl(OneSwitchService service, int x, int y) {
		this.theService = service;
		this.posX = x;
		this.posY = y;
		init();
	}

	private void init(){
		thePopup = new PopUpView(theService, this);

		popupParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);



		popupParams.gravity = Gravity.TOP | Gravity.LEFT;
		popupParams.x = this.posX-14; //14 = rayon du cercle (12) + largeur du trait (2)
		//popupParams.x = this.posX;
		//popupParams.y = this.posY;
		popupParams.y = this.posY-76;
		popupParams.height = 152;
		popupParams.width = 178+28;

		theService.addView(thePopup, popupParams);
		
		
		
		handler = new Handler();
		runnable = new PopupRunnable();
	}

	public void startThread(){
		handler.post(runnable);
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
				thePopup.selectNext();
				handler.postDelayed(this, 1000);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
