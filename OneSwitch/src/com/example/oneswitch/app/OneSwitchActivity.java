package com.example.oneswitch.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;

import com.example.oneswitch.R;
import com.example.oneswitch.action.ActionButton;

public class OneSwitchActivity extends Activity{
	private Intent globalService;
	private boolean isStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_switch);
		globalService = new Intent(this, OneSwitchService.class);
	}

	public void buttonClicked(View v){
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

	@Override
	public void onBackPressed(){
		if(isStarted) {
			ActionButton.home();
		}
		else{
			super.onBackPressed();
		}
			
	}
}