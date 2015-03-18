package iut.oneswitch.app;

import iut.oneswitch.control.ClickPanelCtrl;
import iut.oneswitch.control.HorizontalLineCtrl;
import iut.oneswitch.control.VerticalLineCtrl;
import iut.oneswitch.preference.PrefGeneralFragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class OneSwitchService extends Service implements SensorEventListener{
	private ClickPanelCtrl clickCtrl;
	private HorizontalLineCtrl horizCtrl;
	private boolean isStarted = false;
	private VerticalLineCtrl verticalCtrl;
	private WindowManager windowManager;
	private OneSwitchService service;
	public static final String TAG = OneSwitchService.class.getName();
	public static final int SCREEN_OFF_RECEIVER_DELAY = 500;
	private boolean paused = false;

	private SensorManager mSensorManager = null;

	private static final String BCAST_CONFIGCHANGED = "android.intent.action.CONFIGURATION_CHANGED";

	public IBinder onBind(Intent paramIntent){
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		service = this;

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		registerReceiver(lockDetector, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		registerReceiver(unlockDetector, new IntentFilter(Intent.ACTION_SCREEN_ON));
		registerReceiver(userPresentDetector, new IntentFilter(Intent.ACTION_USER_PRESENT));

		if(!isStarted){
			isStarted = true;
			Notif.getInstance(this).createRunningNotification();
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
		super.onDestroy();

		PrefGeneralFragment.stop(); //Set the switchview to "off"

		stopService();
		unregisterReceiver(mBroadcastReceiver);
		unregisterReceiver(unlockDetector);
		unregisterReceiver(lockDetector);
		unregisterReceiver(userPresentDetector);
		unregisterListener();
		stopForeground(true);
		super.onDestroy();
	}

	public void removeView(View paramView){
		if(paramView != null){
			if(windowManager!=null){
				windowManager.removeView(paramView);
			}
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
			Notif.getInstance(this).removeRunningNotification();
			Toast.makeText(this, "Service arrêté !", Toast.LENGTH_SHORT).show();
			stopSelf();
		}
	}

	public void updateViewLayout(View paramView, WindowManager.LayoutParams paramLayoutParams){
		windowManager.updateViewLayout(paramView, paramLayoutParams);
	}

	public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent myIntent) {
			if (myIntent.getAction().equals( BCAST_CONFIGCHANGED ) ) {
				if(clickCtrl!=null){
					clickCtrl.stopAll();
					horizCtrl = new HorizontalLineCtrl(service);
					verticalCtrl = new VerticalLineCtrl(service);
					clickCtrl = new ClickPanelCtrl(service);
				}
			}
		}
	};

	private void pauseService(){
		if(clickCtrl!=null){
			System.out.println("Vérrouillé");
			clickCtrl.stopAll();
			System.out.println("MAIS OUI JE SUPPRIME BORDEL");
			paused = true;
		}
	}

	private void resumeService(){
		if(paused){
			horizCtrl = new HorizontalLineCtrl(service);
			verticalCtrl = new VerticalLineCtrl(service);
			clickCtrl = new ClickPanelCtrl(service);
			paused = false;
			System.out.println("Déverrouillé !!");
		}
		System.out.println("Déverrouillé 2!!");
	}




	//--------------VERRIFICATION DE SI L'ECRAN EST VERROUILLE OU NON ------------------------

	private void unregisterListener() {
		mSensorManager.unregisterListener(this);
	}

	private void registerListener() {
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public BroadcastReceiver unlockDetector = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (!intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				return;
			}

			Runnable runnable = new Runnable() {
				public void run() {
					System.out.println("Nous sommes sur le lockscreen");
					unregisterListener();
					registerListener();
				}
			};

			new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);
		}
	};

	public BroadcastReceiver userPresentDetector = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (!intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
				return;
			}

			Runnable runnable = new Runnable() {
				public void run() {
					System.out.println("USER PRESENCE");
					resumeService();
					unregisterListener();
					registerListener();
				}
			};

			new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);
		}
	};

	
	public BroadcastReceiver lockDetector = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				return;
			}

			Runnable runnable = new Runnable() {
				public void run() {
					pauseService();
					unregisterListener();
					registerListener();
				}
			};

			new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);
		}
	};
	
/*
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		registerListener();
		mWakeLock.acquire();
		resumeService();
		
		return START_STICKY;
	}*/

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
}
