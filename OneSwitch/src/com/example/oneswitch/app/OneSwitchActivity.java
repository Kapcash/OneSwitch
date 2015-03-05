package com.example.oneswitch.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.oneswitch.R;
import com.example.oneswitch.action.ActionButton;

public class OneSwitchActivity extends Activity{
	private Intent globalService;
	private boolean isStarted = false;
	private Button startStopBt;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_switch);
		init();
		listener();
		
		
	}
	
	private void init(){
		startStopBt = (Button) findViewById(R.id.button1);
		if(isMyServiceRunning(OneSwitchService.class)){
			isStarted=true;
			startStopBt.setText("Stop Service");
		}
		globalService = new Intent(this, OneSwitchService.class);
	}
	
	private void listener(){
		startStopBt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!isStarted){
					startService(globalService);
					isStarted = true;
					((Button)v).setText("Stop Service");
				}
				else{
					stopService(globalService);
					isStarted = false;
					((Button)v).setText("Start Service");
				}
			}
		});
	}

	@Override
	public void onBackPressed(){
		if(isStarted) {
			ActionButton.home();
		}
		else{
			super.onBackPressed();
		}	
	}
	
	private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
}