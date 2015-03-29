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
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Service du projet OneSwitch
 * @author OneSwitch B
 */
public class OneSwitchService extends Service implements SensorEventListener{

	private OneSwitchService service;

	private ClickPanelCtrl clickCtrl;
	private HorizontalLineCtrl horizCtrl;
	private VerticalLineCtrl verticalCtrl;

	/**
	 * True si le service est démarré, false sinon
	 */
	private boolean isStarted = false;
	/**
	 * True si le service est mis en pause, false sinon
	 */
	private boolean paused = false;
	private WindowManager windowManager;
	private SensorManager mSensorManager = null;
	private boolean doAnimation=false;

	private SharedPreferences sp;

	public static final String TAG = OneSwitchService.class.getName();
	public static final int SCREEN_OFF_RECEIVER_DELAY = 500;

	private static final String BCAST_CONFIGCHANGED = "android.intent.action.CONFIGURATION_CHANGED";

	/**
	 * Broadcast pour détecter un appel téléphonique
	 */
	private BroadcastReceiver callReceive;
	/**
	 * Panel ajouté pour détecter les clics sur l'appel
	 */
	private PanelView thePanel;
	/**
	 * True si un appel est en cours, false sinon
	 */
	private boolean call=false;

	public IBinder onBind(Intent paramIntent){
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		service = this;
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 21){
		    doAnimation = true;
		} else{
		    doAnimation = false;
		}
		//Récupère les valeurs de préférences
		sp = PreferenceManager.getDefaultSharedPreferences(service);

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		registerReceiver(lockDetector, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		registerReceiver(unlockDetector, new IntentFilter(Intent.ACTION_SCREEN_ON));
		registerReceiver(userPresentDetector, new IntentFilter(Intent.ACTION_USER_PRESENT));

		//Initialise le service (notification, toast, booleen)
		if(!isStarted){
			isStarted = true;
			Notif.getInstance(this).createRunningNotification();
			Toast.makeText(this, "Service démarré !", Toast.LENGTH_SHORT).show();
			init();
		}
	}

	/**
	 * Permet d'initialiser le service
	 */
	private void init(){
		if(sp.getBoolean("vocal", false)) {
			SpeakAText.speak(getApplicationContext(), "Synthèse vocale initialisée");
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(BCAST_CONFIGCHANGED);
		this.registerReceiver(onOrientationChanged, filter);

		horizCtrl = new HorizontalLineCtrl(this);
		verticalCtrl = new VerticalLineCtrl(this);
		clickCtrl = new ClickPanelCtrl(this);

		bindCallReceiver();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		PrefGeneralFragment.stop(); //Set the switchview to "off"
		stopService();
		/* --- Supprime les receiver --- */
		unregisterReceiver(onOrientationChanged);
		unregisterReceiver(unlockDetector);
		unregisterReceiver(lockDetector);
		unregisterReceiver(userPresentDetector);
		unregisterListener();
		stopForeground(true);
		super.onDestroy();
	}


	//-------------- ACTIONS SUR LE SERVICE ------------------------

	/**
	 * Permet de stopper le sevice
	 */
	public void stopService(){
		if (isStarted){
			if(windowManager != null){
				clickCtrl.stopAll();
				horizCtrl.removeView();
				verticalCtrl.removeView();
			}
			windowManager=null;
			isStarted = false;
			Notif.getInstance(this).removeRunningNotification();
			Toast.makeText(this, "Service arrêté !", Toast.LENGTH_SHORT).show();
			stopSelf();
		}
	}

	/**
	 * Permet de mettre en pause le service
	 */
	private void pauseService(){
		if(clickCtrl!=null){
			clickCtrl.stopAll();
			paused = true;
		}
	}

	/**
	 * Permet de relancer le service.
	 */
	private void resumeService(){
		if(paused){
			horizCtrl = new HorizontalLineCtrl(service);
			verticalCtrl = new VerticalLineCtrl(service);
			clickCtrl = new ClickPanelCtrl(service);
			paused = false;
		}
	}

	/**
	 * Permet d'ajouter une vue
	 * @param paramView les paramètres de la vue
	 * @param paramLayoutParams
	 */
	public void addView(View paramView, WindowManager.LayoutParams paramLayoutParams){
		if (windowManager != null) windowManager.addView(paramView, paramLayoutParams);
	}

	/**
	 * Permet de supprimer une vue
	 * @param paramView la vue à supprimer
	 */
	public void removeView(View paramView){
		try{
			if(paramView != null && windowManager!=null)
				windowManager.removeView(paramView);
		}
		catch(RuntimeException e){
			e.printStackTrace();
		}
	}

	/**
	 * Permet de mettre à jour une vue
	 * @param paramView
	 * @param paramLayoutParams
	 */
	public void updateViewLayout(View paramView, WindowManager.LayoutParams paramLayoutParams){
		try{
			windowManager.updateViewLayout(paramView, paramLayoutParams);
		}
		catch(Exception e){}
	}

	//-------------- FIN DES ACTIONS SUR LE SERVICE ------------------------



	//-------------- LES GETTER ------------------------
	public boolean doAnimation(){
		return doAnimation;
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

	/**
	 * Retourne la hauteur de la status bar (barre de notifications en haut)
	 * @return Retourne la hauteur en pixel
	 */
	public int getStatusBarHeight(){
		int i = getResources().getIdentifier("status_bar_height", "dimen", "android");
		int j = 0;
		if (i > 0) j = getResources().getDimensionPixelSize(i);
		return j;
	}

	public VerticalLineCtrl getVerticalLineCtrl(){
		return verticalCtrl;
	}
	//-------------- FIN DES GETTER ------------------------



	//-------------- LES BROADCAST RECEIVER ------------------------

	/**
	 * Permet la détection de la rotation de l'écran
	 */
	public BroadcastReceiver onOrientationChanged = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent myIntent) {
			if (myIntent.getAction().equals( BCAST_CONFIGCHANGED ) ) {
				if(clickCtrl!=null){
					clickCtrl.stopAll();
					horizCtrl = new HorizontalLineCtrl(service);
					verticalCtrl = new VerticalLineCtrl(service);
					clickCtrl = new ClickPanelCtrl(service);
					if(call==true && thePanel!=null)
						thePanel.updateView();
					else if(call==false && thePanel!=null)
						thePanel.removeView();
				}
			}
		};
	};


	/**
	 * Permet de détecter l'écran déverouillé.
	 */
	public BroadcastReceiver unlockDetector = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (!intent.getAction().equals(Intent.ACTION_SCREEN_ON))
				return;
			/*Runnable runnable = new Runnable() {
				public void run() {
					unregisterListener();
					registerListener();
				}
			};
			new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);*/
			unregisterListener();
			registerListener();
		}
	};

	/**
	 * Permet de vérifier l'écran complètement déverouillée.
	 */
	public BroadcastReceiver userPresentDetector = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (!intent.getAction().equals(Intent.ACTION_USER_PRESENT))
				return;
			/*Runnable runnable = new Runnable() {
				public void run() {
					resumeService();
					unregisterListener();
					registerListener();
				}
			};
			new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);*/
			resumeService();
			unregisterListener();
			registerListener();
		}
	};

	/**
	 * Permet de détecter l'écran vérouillée
	 */
	public BroadcastReceiver lockDetector = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
				return;
			/*Runnable runnable = new Runnable() {
				public void run() {
					pauseService();
					unregisterListener();
					registerListener();
				}
			};
			new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);*/
			pauseService();
			unregisterListener();
			registerListener();
		}
	};

	/**
	 * Classe permettant de détecter un appel
	 * @author OneSwitch B
	 *
	 */
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

				clickCtrl.stopMove();
				thePanel = new PanelView(service);
				if(sp.getBoolean("vocal", false)) {
					String incNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
					String contactName = SpeakAText.retrieveContact(getApplicationContext(), incNumber);

					/* --- Synthèse Vocale --- */
					if(incNumber == null)
						SpeakAText.speak(getApplicationContext(), "Appel entrant d'un numéro masqué");
					else if(contactName == null)
						SpeakAText.speak(getApplicationContext(), "Appel entrant");
					else SpeakAText.speak(getApplicationContext(), "Appel entrant de "+contactName+".");
					SpeakAText.speak(getApplicationContext(), "Clic court pour décrocher.");
					SpeakAText.speak(getApplicationContext(), "Clic long pour refuser l'appel.");
					/* ----------------------- */
				}
				for(int i=0;i<4;i++)
					Toast.makeText(context, "Clic court : Décrocher\nClic long : Raccrocher", Toast.LENGTH_LONG).show();
			}
			//Pendant l'appel
			else if (  intent != null
					&& intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
					&& intent.hasExtra(TelephonyManager.EXTRA_STATE)
					&& intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
				if(call!=true){
					call=true;

					clickCtrl.stopMove();
					thePanel = new PanelView(service);
					for(int i=0;i<4;i++)
						Toast.makeText(context, "Clic long : Raccrocher", Toast.LENGTH_LONG).show();
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
				bindCallReceiver();
				call=false;
			}
			listeners(context,intent);
		}

		/**
		 * Les listenners sur le nouveau panel créé pour les appels
		 * @param context
		 * @param intent
		 */
		private void listeners(final Context context, final Intent intent) {
			//Sur un clic simple, on décroche.
			thePanel.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
					if(tm.getCallState() != TelephonyManager.CALL_STATE_OFFHOOK){
						try {
							Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_HEADSETHOOK);
						} catch (IOException e) {
							Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
							buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
							context.sendOrderedBroadcast(buttonDown,"android.permission.CALL_PRIVILEGED");

							Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
							buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
							context.sendOrderedBroadcast(buttonUp,"android.permission.CALL_PRIVILEGED");
						}
					}
				}
			});

			//Sur un long clic, on raccroche.
			thePanel.setOnLongClickListener(new View.OnLongClickListener(){
				@Override
				public boolean onLongClick(View v) {
					//raccroche
					try {
						TelephonyManager telephonyManager =
								(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

						Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
						Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

						methodGetITelephony.setAccessible(true);

						Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

						Class<?> telephonyInterfaceClass =  
								Class.forName(telephonyInterface.getClass().getName());
						Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

						methodEndCall.invoke(telephonyInterface);
					}
					catch (Exception ex) {
						return false;
					}
					return true;
				}
			});
		}

		//Constructeur vide
		public MyReceiver(){
			;
		}
	}

	//-------------- FIN DES BROADCAST RECEIVER ------------------------





	//-------------- A NE PAS CHANGER ------------------------
	/**
	 * Arrêter l'écoute d'un broadCastReceiver.
	 */
	private void unregisterListener() {
		mSensorManager.unregisterListener(this);
	}

	/**
	 * Enregistrer un listenner
	 */
	private void registerListener() {
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	/**
	 * Ajout du broadcast receiver sur un appel
	 */
	public void bindCallReceiver(){
		IntentFilter filterCall = new IntentFilter();
		filterCall.addAction("android.intent.action.PHONE_STATE");
		callReceive = new MyReceiver();
		try{
			registerReceiver(callReceive, filterCall);
		}
		catch(Exception e){
			unregisterReceiver(callReceive);
			registerReceiver(callReceive, filterCall);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
