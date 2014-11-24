package com.iut.oneswitch.view;


import java.io.IOException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.testos2.R;
import com.iut.oneswitch.model.OneSwitchService;
import com.iut.oneswitch.model.OneSwitchService.LocalBinder;

public class MainActivity extends Activity {
 
	OneSwitchService mService;
    boolean mBound = false;
    MainActivity mainActivity;
	
	Button play;
	int button_status=1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			Runtime.getRuntime().exec("su -c chmod 666 /dev/input/event0");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	TextView t = (TextView) findViewById(R.id.textView1);
            	if(t.getText() == "LOL"){
            		t.setText("BLAGUE");
            	}
            	else{
            		t.setText("LOL");
            	}
            
            }
        });
        mainActivity = this;
        Button start = (Button) findViewById(R.id.btStart);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(mService!=null){
            		mService.btStartListener();
            	}
            }
        });
        Button stop = (Button) findViewById(R.id.btStop);
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(mService!=null){
            		mService.stopIt();
            	}
            }
        });
		
		Intent intent = new Intent(this, OneSwitchService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onStop(){
		super.onStop();
		
		if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
	}
	
	/** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}

