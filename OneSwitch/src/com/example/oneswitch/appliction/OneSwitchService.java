package com.example.oneswitch.appliction;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.os.Binder;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.example.oneswitch.control.ClickPanelCtrl;
import com.example.oneswitch.control.HorizontalLineCtrl;
import com.example.oneswitch.control.ScreenTouchDetectorCtrl;
import com.example.oneswitch.control.VerticalLineCtrl;

public class OneSwitchService extends Service{

	private WindowManager windowManager;
	private ClickPanelCtrl clickCtrl;
	private HorizontalLineCtrl horizCtrl;
	private VerticalLineCtrl verticalCtrl;
	
	private boolean isStarted = false;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		if(!isStarted){
			isStarted = true;
			init();
			try {
				Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_HOME);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void stopService(){
		if(isStarted){
			if(windowManager != null) {
				clickCtrl.removeView();
				horizCtrl.removeView();
			}
			isStarted = false;
		}
	}

	public ClickPanelCtrl getClickPanelCtrl(){
		return clickCtrl;
	}

	private void init(){
		horizCtrl = new HorizontalLineCtrl(this);
		verticalCtrl = new VerticalLineCtrl(this);
		clickCtrl = new ClickPanelCtrl(this);
	}

	public void addView(View view, LayoutParams params){
		if(windowManager != null) {
			windowManager.addView(view, params);
		}
	}


	public void removeView(View view){	
		if(windowManager != null) {
			try{
				windowManager.removeView(view);
			}
			catch(IllegalArgumentException e){
				System.out.println("ERREUR: " + e.getMessage());
			}
		}
	}

	public void updateViewLayout(View view, LayoutParams params){
		windowManager.updateViewLayout(view, params);
	}



	@Override
	public void onDestroy() {
		stopService();
		super.onDestroy();
	}

	public HorizontalLineCtrl getHorizontalLineCtrl(){
		return horizCtrl;
	}
	public VerticalLineCtrl getVerticalLineCtrl() {
		return verticalCtrl;
	}


	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * Permet de récupérer la taille de l'écran.
	 * @return La définition de l'écran.
	 */
	public Point getScreenSize(){
		Point size = new Point();
		windowManager.getDefaultDisplay().getSize(size);
		return size;
	}



}
