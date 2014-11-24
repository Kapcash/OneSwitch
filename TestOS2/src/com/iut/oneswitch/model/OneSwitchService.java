package com.iut.oneswitch.model;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.iut.oneswitch.view.HorizontalLine;
import com.iut.oneswitch.view.VerticalLine;

public class OneSwitchService extends Service implements OnTouchListener{

	private final IBinder mBinder = new LocalBinder();

	private WindowManager windowManager;
	private HorizontalLine horizLine;
	private VerticalLine verticalLine;
	private View clickPanel=null;
	private boolean clickPanelInit = false;

	private boolean horizMoving = false;
	private boolean horizInit = false;
	private WindowManager.LayoutParams horizParams;

	private boolean verticalMoving = false;
	private boolean verticalInit = false;
	private WindowManager.LayoutParams verticalParams;
	private WindowManager.LayoutParams clickParams;
	private boolean stoped=true;

	//-----ATTRIBUT PARAMETRABLE-----
	int speed =3;
	int lineThickness = 5;

	public class LocalBinder extends Binder {
		public OneSwitchService getService() {
			// Return this instance of service so clients can call public methods
			return OneSwitchService.this;
		}
	}
	@Override public void onCreate() {
		super.onCreate();
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE); //UI access
		//drawClickPanel(); //view used to incercept click on all the screen

	}


	public void btStartListener(){
		if(stoped){
			stoped=false;
			drawClickPanel();
			handleClick();
		}
	}

	private void drawClickPanel() {
		if(!stoped){
			clickPanel = new View(this);

			clickParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
					this.getStatusBarHeight(),
					WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
					WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
					WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
					PixelFormat.TRANSLUCENT); 

			clickParams.gravity = Gravity.TOP | Gravity.LEFT;
			clickParams.x = 0;
			clickParams.y = 0;
			clickParams.height = getScreenSize().y;
			clickParams.width = getScreenSize().x;
			clickPanel.setOnTouchListener(this);

			Handler mHandler = new Handler();
			mHandler.postDelayed(new Runnable() {
				public void run() {
					if(!stoped){
						try{
							windowManager.addView(clickPanel, clickParams);
						}
						catch(RuntimeException e){
						}
						//handleClick();
					}
				}
			}, 1000);
		}
	}

	public void createVerticalLine(){

		if(!verticalInit){
			System.out.println("VERTICAL LINE");
			verticalLine = new VerticalLine(this);

			verticalParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
					this.getStatusBarHeight(),
					WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
					WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
					WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
					PixelFormat.TRANSLUCENT);



			verticalParams.gravity = Gravity.TOP | Gravity.LEFT;
			verticalParams.x = 0;
			verticalParams.y = 0;
			verticalParams.height = getScreenSize().y;
			//verticalParams.height = getScreenDimensions().y;
			verticalParams.width = lineThickness;

			windowManager.addView(verticalLine, verticalParams);
			verticalInit = true;
		}

		if(verticalInit){
			try {
				final Handler handler = new Handler();

				Runnable runnable = new Runnable() {
					boolean down = true;
					@Override
					public void run() {
						try {
							Point size = getScreenSize();
							if((verticalParams.x <= size.x)&&(down == true)){
								verticalParams.x += speed;

								windowManager.updateViewLayout(verticalLine, verticalParams);
								if(verticalParams.x >= (size.x -speed))
									down = false;
							}else
							{
								verticalParams.x -= speed;

								windowManager.updateViewLayout(verticalLine, verticalParams);
								if(verticalParams.x <= (0+speed))
									down = true;
							}
							if(verticalMoving)
								handler.postDelayed(this, 10);
						} catch (Exception e) {
							// TODO: handle exception
						}



					}
				};
				handler.postDelayed(runnable, 1000);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public void stopIt(){
		horizInit = false;
		horizMoving = false;
		verticalInit = false;
		verticalMoving = false;
		stoped = true;
		try{
			System.out.println("avant de remove le clickPanel");
			windowManager.removeView(clickPanel);
			Toast.makeText(this, "clickPanel removed", Toast.LENGTH_SHORT).show();
		}
		catch(RuntimeException e){
			Toast.makeText(this, "clickPanel not removed", Toast.LENGTH_SHORT).show();
		}
		try{
			System.out.println("avant de remove le horizLine");
			windowManager.removeView(horizLine);
			Toast.makeText(this, "horizLine removed", Toast.LENGTH_SHORT).show();
		}
		catch(RuntimeException e){
			Toast.makeText(this, "horizLine not removed", Toast.LENGTH_SHORT).show();
		}
		try{
			System.out.println("avant de remove le verticalLine");
			windowManager.removeView(verticalLine);
			Toast.makeText(this, "verticalLine removed", Toast.LENGTH_SHORT).show();
		}
		catch(RuntimeException e){
			Toast.makeText(this, "verticalLine not removedd", Toast.LENGTH_SHORT).show();
		}
	}

	public void createHorizontalLine(){

		if(!horizInit){ //si la barre horizontale n'existe pas, on la crÃ©e
			System.out.println("HORIZONTAL LINE");
			horizLine = new HorizontalLine(this);

			horizParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
					this.getStatusBarHeight(),
					WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
					WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
					WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
					PixelFormat.TRANSLUCENT);

			horizParams.gravity = Gravity.TOP | Gravity.LEFT;
			horizParams.x = 0;
			horizParams.y = 0;
			horizParams.height = lineThickness;
			horizParams.width = getScreenSize().x;
			//horizParams.width = getScreenDimensions().x;

			windowManager.addView(horizLine, horizParams);
			horizInit = true;
		}

		if(horizInit){
			//gestion de son deplacement
			try {
				final Handler handler = new Handler();

				Runnable runnable = new Runnable() {
					boolean down = true;
					@Override
					public void run() {
						try {
							Point size = getScreenSize();
							if((horizParams.y <= size.y)&&(down == true)){
								horizParams.y += speed;

								windowManager.updateViewLayout(horizLine, horizParams);
								if(horizParams.y >= (size.y-speed))
									down = false;
							}else
							{
								horizParams.y -= speed;

								windowManager.updateViewLayout(horizLine, horizParams);
								if(horizParams.y <= (0+speed))
									down = true;
							}
							if(horizMoving) //si elle doit continuer de bouger, alors on planifie le prochain mouvement
								handler.postDelayed(this, 10);
						} catch (Exception e) {
							// TODO: handle exception
						}



					}
				};
				handler.postDelayed(runnable, 1000); //demarrage apres 0.1 seconde
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}


	private long startClick;
	private long timeClick;
	private boolean vertPaused=false;
	private boolean horizPaused=false;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//Detecte si la ligne verticale est en mouvement, et la met en pause
			if(verticalMoving){
				verticalMoving = false;
				vertPaused = true;
			}
			//Detecte si la ligne horizontale est en mouvement, et la met en pause
			else if(horizMoving){
				horizMoving = false;
				horizPaused = true;
			}
			//Le début de la durée du clique
			startClick = System.currentTimeMillis();
			return true;
		case MotionEvent.ACTION_UP:
			//Detecte si la ligne Veticale a été mis en pause
			if(vertPaused){
				verticalMoving = true;
				vertPaused = false;
			}
			//Detecte si la ligne Horizontale a été mis en pause
			else if(horizPaused){
				horizMoving = true;
				horizPaused = false;
			}
			//détermine la durée du clique
			timeClick = System.currentTimeMillis() - startClick;
			//On met une durée maximum de 2 secondes pour un appuie, evitant les conflits.
			if(timeClick>=2000) timeClick = 2000;
			handleClick();
			return true;
		}
		return false;
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		stopIt();
		stoped=false;
		drawClickPanel();
	}


	public void handleClick(){
		if(!horizInit&&!stoped){
			horizMoving = true;
			createHorizontalLine();
		}
		else{
			if(horizMoving){
				horizMoving = false;
				if(!verticalInit){
					verticalMoving = true;
					createVerticalLine();
				}
			}
			else{
				if(verticalMoving){
					horizInit = false;
					horizMoving = false;
					verticalInit = false;
					verticalMoving = false;

					try{
						windowManager.removeView(horizLine);
						windowManager.removeView(verticalLine);
						windowManager.removeView(clickPanel);
					}
					catch(RuntimeException e){}

					//click;
					click(verticalParams.x, horizParams.y);

					drawClickPanel();

				}

				/*else if(!verticalMoving&&!horizMoving&&verticalInit&&horizInit){

					//drawClickPanel();
					//windowManager.removeView(horizLine);
					//windowManager.removeView(verticalLine);

				}*/
			}
		}

	}

	private void click(int x, int y){
		try {
			//Runtime.getRuntime().exec("su -c input tap " + x + " " + y);
			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + x + " " + y + " " + this.timeClick);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//////// Getter ///////
	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public Point getScreenSize(){
		Point size = new Point();
		windowManager.getDefaultDisplay().getSize(size);
		return size;
	}


}

