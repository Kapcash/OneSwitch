package com.example.oneswitch.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.oneswitch.control.ClickPanelCtrl;
import com.example.oneswitch.control.HorizontalLineCtrl;
import com.example.oneswitch.control.VerticalLineCtrl;

public class OneSwitchService extends Service{
	private ClickPanelCtrl clickCtrl;
	private HorizontalLineCtrl horizCtrl;
	private boolean isStarted = false;
	private VerticalLineCtrl verticalCtrl;
	private WindowManager windowManager;
	private OneSwitchService service;

	private static final String BCAST_CONFIGCHANGED = "android.intent.action.CONFIGURATION_CHANGED";

	public IBinder onBind(Intent paramIntent){
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		service = this;
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		if(!isStarted){
			isStarted = true;
			Toast.makeText(this, "Service démarré !", Toast.LENGTH_SHORT).show();
			init();
		}
	}

	private void init(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(BCAST_CONFIGCHANGED);
		this.registerReceiver(mBroadcastReceiver, filter);

		horizCtrl = new HorizontalLineCtrl(this);
		verticalCtrl = new VerticalLineCtrl(this);
		clickCtrl = new ClickPanelCtrl(this);
	}

	public void addView(View paramView, WindowManager.LayoutParams paramLayoutParams){
		if (windowManager != null) {
			windowManager.addView(paramView, paramLayoutParams);
		}
	}

	public ClickPanelCtrl getClickPanelCtrl(){
		return clickCtrl;
	}

	public HorizontalLineCtrl getHorizontalLineCtrl(){
		return horizCtrl;
	}

	public Point getScreenSize(){
		Point localPoint = new Point();
		windowManager.getDefaultDisplay().getSize(localPoint);
		return localPoint;
	}

	public int getStatusBarHeight(){
		int i = getResources().getIdentifier("status_bar_height", "dimen", "android");
		int j = 0;
		if (i > 0) {
			j = getResources().getDimensionPixelSize(i);
		}
		return j;
	}

	public VerticalLineCtrl getVerticalLineCtrl(){
		return verticalCtrl;
	}

	@Override
	public void onDestroy(){
		stopService();
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	public void removeView(View paramView){
		if(paramView != null && windowManager!=null){
			windowManager.removeView(paramView);
		}

	}

	public void stopService(){
		if (isStarted){
			if(windowManager != null){
				clickCtrl.removeView();
				horizCtrl.removeView();
				verticalCtrl.removeView();
			}
			isStarted = false;
			stopSelf();
		}
	}

	public void updateViewLayout(View paramView, WindowManager.LayoutParams paramLayoutParams){
		windowManager.updateViewLayout(paramView, paramLayoutParams);
	}

	public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent myIntent) {

			if ( myIntent.getAction().equals( BCAST_CONFIGCHANGED ) ) {
				if(clickCtrl!=null){
					clickCtrl.stopAll();
					horizCtrl = new HorizontalLineCtrl(service);
					verticalCtrl = new VerticalLineCtrl(service);
					clickCtrl = new ClickPanelCtrl(service);
				}
			}
		}
	};
}
