package com.iut.oneswitch.view;

import com.example.testos2.R;
import com.example.testos2.R.id;
import com.example.testos2.R.layout;
import com.example.testos2.R.menu;
import com.iut.oneswitch.model.OneSwitchService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button play;
	int button_status=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("click");
		play=(Button)findViewById(R.id.button1);

        play.setOnClickListener(new OnClickListener() {         
            @Override
            public void onClick(View arg0) {
            	
                // TODO Auto-generated method stub
                if(button_status == 1)//play the service
                {
                button_status=0;
        		startService(new Intent(MainActivity.this, OneSwitchService.class));

                }
                else//stop the service
                {
                button_status=1;
        		stopService(new Intent(MainActivity.this, OneSwitchService.class));

                } 
            }});
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
}
