package com.example.oneswitch.control;

import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.oneswitch.app.OneSwitchService;
import com.example.oneswitch.view.ShortcutMenuView;

public class ShortcutMenuCtrl{
	private Handler handler;
	private boolean isRunning = false;
	private Runnable runnable;
	private WindowManager.LayoutParams shortcutMenuParams;
	private OneSwitchService theService;
	private ShortcutMenuView theShortcutMenu;

	public ShortcutMenuCtrl(OneSwitchService service){
		theService = service;
		isRunning = false;
		init();
	}

	private void init(){
		theShortcutMenu = new ShortcutMenuView(theService, this);
		shortcutMenuParams = new WindowManager.LayoutParams(-1, theService.getStatusBarHeight(), 2010, 296, -3);
		shortcutMenuParams.gravity = 51;
		shortcutMenuParams.x = 0;
		shortcutMenuParams.y = 0;
		shortcutMenuParams.height = theService.getScreenSize().y;
		shortcutMenuParams.width = theService.getScreenSize().x;
		theService.addView(theShortcutMenu, shortcutMenuParams);
		handler = new Handler();
		runnable = new PopupMenuRunnable();
	}

	public View getButton(int paramInt){
		return theShortcutMenu.getButton(paramInt);
	}

	public Button getSelected(){
		return theShortcutMenu.getSelected();
	}

	public OneSwitchService getService(){
		return theService;
	}

	public void removeView(){
		isRunning = false;
		if (theShortcutMenu != null){
			theService.removeView(theShortcutMenu);
			theService.getClickPanelCtrl().closePopupCtrl();
		}
	}

	public void startThread(){
		isRunning = true;
		handler.postDelayed(runnable, 1000L);
	}

	class PopupMenuRunnable implements Runnable{
		@Override
		public void run(){
			try{
				if(isRunning){
					theShortcutMenu.selectNext();
					handler.postDelayed(this, 1000L);
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
