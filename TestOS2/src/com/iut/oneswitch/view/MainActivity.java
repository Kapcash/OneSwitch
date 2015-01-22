package com.iut.oneswitch.view;

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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.oneswitch.R;
import com.iut.oneswitch.application.OneSwitchService;
import com.iut.oneswitch.application.OneSwitchService.LocalBinder;
/**
 * Classe principale permettant le lancement de l'aplication.
 * @author OneSwitch B
 */
public class MainActivity extends Activity {
 
	/**
	 * Service de l'application
	 */
	private OneSwitchService mService;
    private boolean mBound = false;

    private static MainActivity mainActivity;
	
	Button play;
	int button_status=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button buttonTest = (Button) findViewById(R.id.btTest);
		buttonTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	TextView t = (TextView) findViewById(R.id.textView1);
            	if(t.getText() == "Click me again"){
            		t.setText("Click me");
            	}
            	else{
            		t.setText("Click me again");
            	}
            }
        });
        mainActivity = this;
        
        ToggleButton button = (ToggleButton) findViewById(R.id.toggleOnOff);
        button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         	@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
	            if(isChecked){ 
	            	if(mService!=null)mService.startService();
	            }
	            else {
	            	if(mService!=null) mService.stopService();
	            }
			}
        });
        /* ---------------------------------------- */
        
        /*Button start = (Button) findViewById(R.id.btStart);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(mService!=null){
            		mService.startService();
            	}
            }
        });
        
        Button stop = (Button) findViewById(R.id.btStop);
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(mService!=null){
            		mService.stopService();
            	}
            }
        });*/
        
		Intent intent = new Intent(this, OneSwitchService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public static MainActivity getActivity(){
		return mainActivity;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) { //Affiche les préférences (paramètres)
			Intent i = new Intent(this, SettingsOS.class);
        	startActivity(i);
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