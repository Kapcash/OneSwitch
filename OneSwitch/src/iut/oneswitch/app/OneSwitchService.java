package iut.oneswitch.app;

import iut.oneswitch.action.SpeakAText;
import iut.oneswitch.control.ClickPanelCtrl;
import iut.oneswitch.control.HorizontalLineCtrl;
import iut.oneswitch.control.VerticalLineCtrl;
import iut.oneswitch.preference.PrefGeneralFragment;
import iut.oneswitch.view.PanelView;

import java.io.IOException;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
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
	private SharedPreferences sp;

	private SensorManager mSensorManager = null;

	private static final String BCAST_CONFIGCHANGED = "android.intent.action.CONFIGURATION_CHANGED";

	//Broadcast pour l'appel
	private BroadcastReceiver callReceive;
	//Panel pour appel
	private PanelView thePanel;
	private boolean call=false;


	public IBinder onBind(Intent paramIntent){
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		service = this;

		sp = PreferenceManager.getDefaultSharedPreferences(service);

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
		if(sp.getBoolean("vocal", false)) {
			SpeakAText.speak(getApplicationContext(), "Synthèse vocale initialisée");
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(BCAST_CONFIGCHANGED);
		this.registerReceiver(mBroadcastReceiver, filter);

		horizCtrl = new HorizontalLineCtrl(this);
		verticalCtrl = new VerticalLineCtrl(this);
		clickCtrl = new ClickPanelCtrl(this);
		bindCallReceiver();

	}

	public void bindCallReceiver(){
		//Ajout du broadcastReceiver sur un appel
		IntentFilter filterCall = new IntentFilter();
		filterCall.addAction("android.intent.action.PHONE_STATE");
		//      filterCall.addAction("android.intent.action.NEW_OUTGOING_CALL");
		callReceive = new MyReceiver();
		registerReceiver(callReceive, filterCall);
	}
	//Classe interne pour un appel
	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			//Réception d'un appel
			if (  intent != null
					&& intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
					&& intent.hasExtra(TelephonyManager.EXTRA_STATE)
					&& intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)
					&& !call){

				call=true;
				service.pauseService();
				thePanel = new PanelView(service);
				//panelCall.setColor(0xCCff0000);

				if(sp.getBoolean("vocal", false)) {
					String incNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
					String contactName = SpeakAText.retrieveContact(getApplicationContext(), incNumber);

					if(incNumber == null) {
						SpeakAText.speak(getApplicationContext(), "Appel entrant d'un numéro masqué");
					}
					else if(contactName == null) {
						SpeakAText.speak(getApplicationContext(), "Appel entrant");
					}
					else {
						SpeakAText.speak(getApplicationContext(), "Appel entrant de "+contactName+".");
					}


					SpeakAText.speak(getApplicationContext(), "Clic court pour décrocher.");
					SpeakAText.speak(getApplicationContext(), "Clic long pour refuser l'appel.");
				}
				for(int i=0;i<4;i++){
					Toast.makeText(context, "Clic court : Décrocher\nClic long : Raccrocher", Toast.LENGTH_LONG).show();
				}

			}

			//Pendant l'appel
			else if (  intent != null
					&& intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
					&& intent.hasExtra(TelephonyManager.EXTRA_STATE)
					&& intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){

				if(call!=true){
					call=true;      
					service.pauseService();
					thePanel = new PanelView(service);
					System.out.println("TU RENTRE DANS LE IF");
					thePanel.setColor(0xCCff0000);
					for(int i=0;i<4;i++){
						Toast.makeText(context, "Clic long : Raccrocher", Toast.LENGTH_LONG).show();
					}

				}
				AudioManager audioManager =  (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				audioManager.setMode(AudioManager.MODE_IN_CALL);
				audioManager.setSpeakerphoneOn(true);
			}

			//Fin de l'appel
			else if (  intent != null
					&& intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
					&& intent.hasExtra(TelephonyManager.EXTRA_STATE)
					&& intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)){

				unregisterReceiver(this);

				thePanel.removeView();
				System.out.println("on remove le panel");
				service.resumeService();
				bindCallReceiver();
				call=false;
			}

			listeners(context,intent);
		}

		private void listeners(final Context context, final Intent intent) {

			//Sur un clic simple, on décroche.
			thePanel.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					try {
						Runtime.getRuntime().exec("su -c input keyevent " +
								Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));
					} catch (IOException e) {
						// Runtime.exec(String) had an I/O problem, try to fall back
						Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
						buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
						context.sendOrderedBroadcast(buttonDown,"android.permission.CALL_PRIVILEGED");

						// froyo and beyond trigger on buttonUp instead of buttonDown
						Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
						buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
						context.sendOrderedBroadcast(buttonUp,"android.permission.CALL_PRIVILEGED");
					}

				}
			});


			//Sur un long clic, on raccroche.
			thePanel.setOnLongClickListener(new View.OnLongClickListener(){

				@Override
				public boolean onLongClick(View v) {
					//raccroche
					try {
						// Get the boring old TelephonyManager
						TelephonyManager telephonyManager =
								(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

						// Get the getITelephony() method
						Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
						Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

						// Ignore that the method is supposed to be private
						methodGetITelephony.setAccessible(true);


						// Invoke getITelephony() to get the ITelephony interface
						Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

						// Get the endCall method from ITelephony
						Class<?> telephonyInterfaceClass =  
								Class.forName(telephonyInterface.getClass().getName());
						Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

						// Invoke endCall()
						methodEndCall.invoke(telephonyInterface);

					}

					catch (Exception ex) { // Many things can go wrong with reflection calls
						Log.d(TAG,"PhoneStateReceiver **" + ex.toString());
						return false;
					}
					return true;
				}
			});
		}

		// constructor
		public MyReceiver(){

		}
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
			Notif.getInstance(this).removeRunningNotification();
			Toast.makeText(this, "Service arrêté !", Toast.LENGTH_SHORT).show();
			stopSelf();
		}
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
		};
	};

	public void updateViewLayout(View paramView, WindowManager.LayoutParams paramLayoutParams){
		windowManager.updateViewLayout(paramView, paramLayoutParams);
	}

	private void pauseService(){
		if(clickCtrl!=null){
			clickCtrl.stopAll();
			paused = true;
		}
	}

	private void resumeService(){
		if(paused){
			horizCtrl = new HorizontalLineCtrl(service);
			verticalCtrl = new VerticalLineCtrl(service);
			clickCtrl = new ClickPanelCtrl(service);
			paused = false;
		}
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



	@Override
	public void onSensorChanged(SensorEvent event) {
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
