package iut.oneswitch.app;

import java.io.IOException;

import iut.oneswitch.action.ActionButton;
import iut.oneswitch.preference.PrefGeneralFragment;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.os.Bundle;

public class OneSwitchActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		try {
			Process root = Runtime.getRuntime().exec("su");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getFragmentManager().beginTransaction().replace(android.R.id.content,new PrefGeneralFragment()).commit();
	
	}
	
	@Override
	public void onBackPressed(){
		if(isMyServiceRunning(OneSwitchService.class)){
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