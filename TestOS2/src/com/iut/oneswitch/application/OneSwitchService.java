package com.iut.oneswitch.application;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class OneSwitchService extends Service{

	private final IBinder mBinder = new LocalBinder();

	private WindowManager windowManager;
	private static ClickPanelCtrl clickCtrl;

	public class LocalBinder extends Binder {
		public OneSwitchService getService() {
			// Return this instance of service so clients can call public methods
			return OneSwitchService.this;
		}
	}
	//binder
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	//=================================
	
	@Override public void onCreate() {
		super.onCreate();
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE); //UI access
	}

	
	public void startService(){
		System.out.println("SERVICE STARTED");
		
		clickCtrl = new ClickPanelCtrl(this); //click panel to incercept click anywhere on the display
		
	}

	public void stopService(){
		if(clickCtrl != null){
			clickCtrl.remove();
			System.out.println("SERVICE STOPPED");
		}
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	    	if(clickCtrl != null){
				clickCtrl.reinit();
			}
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	    	if(clickCtrl != null){
				clickCtrl.reinit();
			}
	    }
	}

	//=================================
	

	
	//useful getters
	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public Point getScreenSize(){
		Point size = new Point();
		windowManager.getDefaultDisplay().getSize(size);
		return size;
	}


	//windowsManager utils
	public void addView(View view, LayoutParams params) {
		windowManager.addView(view, params);
	}
	
	public void removeView(View view) {
		windowManager.removeView(view);
	}

	public void updateViewLayout(View view, LayoutParams params) {
		windowManager.updateViewLayout(view, params);
	}

	



}

