package com.example.oneswitch.appliction;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.oneswitch.R;

public class OneSwitchActivity extends Activity {
	private Intent globalService;
	private boolean isStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_switch);
		globalService = new Intent(this,OneSwitchService.class);
	}

	public void buttonClicked(View v){

		if(v.getTag() == null){
			startService(globalService);
			isStarted = true;
			v.setTag("on");
			((Button)v).setText("Stop");
			Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();
		}
		else{
			stopService(globalService);
			isStarted = false;
			v.setTag(null);
			((Button)v).setText("Start");
			Toast.makeText(this, "Stop Service", Toast.LENGTH_SHORT).show();
		}

	}


	@Override
	public void onBackPressed(){
		if(isStarted){
			try {
				Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_HOME);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			super.onBackPressed();
		}
		
	}

}
