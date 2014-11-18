package com.iut.oneswitch.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Instrumentation;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.Display;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import com.iut.oneswitch.view.HorizontalLine;
import com.iut.oneswitch.view.VerticalLine;

public class OneSwitchService extends Service implements OnTouchListener{

	private final IBinder mBinder = new LocalBinder();

	private WindowManager windowManager;
	private HorizontalLine horizLine;
	private VerticalLine verticalLine;
	private View clickPanel;

	private boolean horizMoving = false;
	private boolean horizInit = false;
	private WindowManager.LayoutParams horizParams;

	private boolean verticalMoving = false;
	private boolean verticalInit = false;
	private WindowManager.LayoutParams verticalParams;


	public class LocalBinder extends Binder {
		public OneSwitchService getService() {
			// Return this instance of service so clients can call public methods
			return OneSwitchService.this;
		}
	}

	@Override public void onCreate() {
		super.onCreate();
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE); //UI access

		drawClickPanel(); //view used to incercept click on all the screen

	}

	private void drawClickPanel() {
		clickPanel = new View(this);
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT); 


		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 0;
		params.height = getScreenDimensions().y;
		params.width = getScreenDimensions().x;

		windowManager.addView(clickPanel, params);



		clickPanel.setOnTouchListener(this);

	}

	public void createVerticalLine(){

		if(!verticalInit){
			System.out.println("VERTICAL LINE");
			verticalLine = new VerticalLine(this);

			verticalParams = new WindowManager.LayoutParams(
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_PHONE,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					PixelFormat.TRANSLUCENT);



			verticalParams.gravity = Gravity.TOP | Gravity.LEFT;
			verticalParams.x = 0;
			verticalParams.y = 0;
			verticalParams.height = getScreenDimensions().y;
			verticalParams.width = 2;

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

							Point size = new Point();
							windowManager.getDefaultDisplay().getSize(size);
							if((verticalParams.x <= size.x)&&(down == true)){
								verticalParams.x += 1;

								windowManager.updateViewLayout(verticalLine, verticalParams);
								if(verticalParams.x == size.x)
									down = false;
							}else
							{
								verticalParams.x -= 1;

								windowManager.updateViewLayout(verticalLine, verticalParams);
								if(verticalParams.x == 0)
									down = true;
							}
							if(verticalMoving)
								handler.postDelayed(this, 10);
						} catch (Exception e) {
							// TODO: handle exception
						}



					}
				};
				handler.postDelayed(runnable, 500);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public void createHorizontalLine(){

		if(!horizInit){ //si la barre horizontale n'existe pas, on la crée
			System.out.println("HORIZONTAL LINE");
			horizLine = new HorizontalLine(this);

			horizParams = new WindowManager.LayoutParams(
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_PHONE,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					PixelFormat.TRANSLUCENT);

			horizParams.gravity = Gravity.TOP | Gravity.LEFT;
			horizParams.x = 0;
			horizParams.y = 0;
			horizParams.height = 2;
			horizParams.width = getScreenDimensions().x;

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

							Point size = new Point();
							windowManager.getDefaultDisplay().getSize(size);
							if((horizParams.y <= size.y)&&(down == true)){
								horizParams.y += 1;

								windowManager.updateViewLayout(horizLine, horizParams);
								if(horizParams.y == size.y)
									down = false;
							}else
							{
								horizParams.y -= 1;

								windowManager.updateViewLayout(horizLine, horizParams);
								if(horizParams.y == 0)
									down = true;
							}
							if(horizMoving) //si elle doit continuer de bouger, alors on planifie le prochain mouvement
								handler.postDelayed(this, 10);
						} catch (Exception e) {
							// TODO: handle exception
						}



					}
				};
				handler.postDelayed(runnable, 100); //demarrage apres 0.1 seconde
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private Point getScreenDimensions(){
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			System.out.println("DOOOOWN");


			return true;
		case MotionEvent.ACTION_UP:
			System.out.println("UUUUPP");

			handleClick();

			return true;
		}
		return false;
	}


	private void handleClick(){
		if(!horizInit){
			createHorizontalLine();
			horizMoving = true;
		}
		else{
			if(horizMoving){
				horizMoving = false;
				if(!verticalInit){
					createVerticalLine();
					verticalMoving = true;
				}

			}
			else{
				if(verticalMoving){
					verticalMoving = false;
					//click
					windowManager.removeView(clickPanel);

					System.out.println("X " + verticalParams.x);
					System.out.println("Y " + horizParams.y);

					try {
						String cmds[] = new String[6];
						
						   cmds[0] = "sendevent /dev/input/event0 3 0 " + verticalParams.x;
					        cmds[1] = "sendevent /dev/input/event0 3 1 " + horizParams.y;
					       // cmds[0] = "sendevent /dev/input/event0 3 0 165";
					       // cmds[1] = "sendevent /dev/input/event0 3 1 215";
					        cmds[2] = "sendevent /dev/input/event0 1 330 1";
					        cmds[3] = "sendevent /dev/input/event0 0 0 0";
					        cmds[4] = "sendevent /dev/input/event0 1 330 0";
					        cmds[5] = "sendevent /dev/input/event0 0 0 0";
						
					        Process d = Runtime.getRuntime().exec("su"); 
					        
						 Process p = Runtime.getRuntime().exec("su");
					        DataOutputStream os = new DataOutputStream(p.getOutputStream());            
					        for (String tmpCmd : cmds) {
					        	System.out.println(tmpCmd);
					                os.writeBytes(tmpCmd+"\n");
					        }           
					        os.writeBytes("exit\n");  
					        os.flush();
					      

					       /* for (String tmpCmd : cmds) {
					        	System.out.println(tmpCmd);
					        	  //Runtime.getRuntime().exec(new String[] { "su", "-c", tmpCmd });
					        	Runtime.getRuntime().exec(tmpCmd);
					        } */ 
					   

						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//windowManager.addView(clickPanel, this);

				}
				else if(!verticalMoving&&!horizMoving&&verticalInit&&horizInit){
					verticalInit = false;
					horizInit = false;
					windowManager.removeView(horizLine);
					windowManager.removeView(verticalLine);

				}
			}
		}






	}

	

}

