package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.iut.oneswitch.view.popup.ShortcutMenuView;

public class ButtonMenuCtrl {
	
	private boolean isRunning = false;
	
	private ShortcutMenuView theShortcutMenu;
	private Handler handler;
	private Runnable runnable;
	private WindowManager.LayoutParams shortcutMenuParams;
	private OneSwitchService theService;

	public ButtonMenuCtrl(OneSwitchService service) {
		this.theService = service;
		isRunning = false;
		init();
	}
	
	private void init(){
		/* Drawing Popup */
			theShortcutMenu = new ShortcutMenuView(theService, this);
			shortcutMenuParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
					theService.getStatusBarHeight(),
					WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
					WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
					WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
					PixelFormat.TRANSLUCENT);
			shortcutMenuParams.gravity = Gravity.TOP | Gravity.LEFT;
			
			shortcutMenuParams.x = 0;
			shortcutMenuParams.y = 0;
			shortcutMenuParams.height = theService.getScreenSize().y;
			shortcutMenuParams.width = theService.getScreenSize().x;
			theService.addView(theShortcutMenu, shortcutMenuParams);
		/*End Drawing Popup*/
			
		handler = new Handler();
		runnable = new PopupMenuRunnable();
	}

	public void startThread(){
		isRunning = true;
		handler.postDelayed(runnable, 1000);
	}
	
	public void removeView() {
		theService.removeView(theShortcutMenu);
	}
	
	public void stopThread(){
		isRunning = false;
	}
	
	public Button getSelected() {
		return theShortcutMenu.getSelected();
	}

	public View getButton(int i) {
		return theShortcutMenu.getButton(i);
	}
	
	/**
	 * Permet la selection d'un bouton du menu
	 * @author OneSwitch B
	 */
	class PopupMenuRunnable implements Runnable{

		/**
		 * Permet le défilement des boutons
		 */
		@Override
		public void run() {
			try {
				if(isRunning){ 
					theShortcutMenu.selectNext();
					handler.postDelayed(this, 1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}