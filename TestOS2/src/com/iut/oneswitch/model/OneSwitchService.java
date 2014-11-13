package com.iut.oneswitch.model;

import com.iut.oneswitch.view.VerticalLine;
import com.iut.oneswitch.view.HorizontalLine;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class OneSwitchService extends Service {

	private WindowManager windowManager;
	private HorizontalLine chatHead;
	private VerticalLine chatHead2;
	private View chatHead3;


	@Override public void onCreate() {
		super.onCreate();

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		//chatHead = new ImageView(this);
		//chatHead.setImageResource(R.drawable.ic_launcher);


		chatHead = new HorizontalLine(this);
		chatHead.setBackgroundColor(Color.WHITE);


		chatHead2 = new VerticalLine(this);
		chatHead2.setBackgroundColor(Color.WHITE);

		chatHead3 = new View(this);
		//chatHead3.setBackgroundColor(Color.YELLOW);
		//chatHead3.setAlpha((float) 0.1);

		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 0;
		params.height = 2;
		params.width = size.x;
		
		
		final WindowManager.LayoutParams params2 = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);


		params2.gravity = Gravity.TOP | Gravity.LEFT;
		params2.x = 0;
		params2.y = 0;
		params2.height = size.y;
		params2.width = 2;
		
		final WindowManager.LayoutParams params3 = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);


		params3.gravity = Gravity.TOP | Gravity.LEFT;
		params3.x = 0;
		params3.y = 0;
		params3.height = size.y;
		params3.width = size.x;



		windowManager.addView(chatHead, params);
		windowManager.addView(chatHead2, params2);
		//windowManager.addView(chatHead3, params3);
		try {
			final Handler handler = new Handler();
			
			Runnable runnable = new Runnable() {
				boolean down = true;
				   @Override
				   public void run() {
				       try {
				    	   
				    	   Point size = new Point();
				    	   windowManager.getDefaultDisplay().getSize(size);
				    	   if((params.y <= size.y)&&(down == true)){
							   params.y += 1;
							   
								windowManager.updateViewLayout(chatHead, params);
								if(params.y == size.y)
									down = false;
						   }else
						   {
							   params.y -= 1;
								
								windowManager.updateViewLayout(chatHead, params);
								if(params.y == 0)
									down = true;
						   }
				    	   handler.postDelayed(this, 10);
					} catch (Exception e) {
						// TODO: handle exception
					}
					  
				     
				      
				   }
				};
				handler.postDelayed(runnable, 2000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			final Handler handler = new Handler();
			
			Runnable runnable = new Runnable() {
				boolean down = true;
				   @Override
				   public void run() {
					   Point size = new Point();
			    	   windowManager.getDefaultDisplay().getSize(size);
				       try {
				    	   if((params2.x <= size.x)&&(down == true)){
							   params2.x += 1;
							   
								windowManager.updateViewLayout(chatHead2, params2);
								if(params2.x == size.x)
									down = false;
						   }else
						   {
							   params2.x -= 1;
								
								windowManager.updateViewLayout(chatHead2, params2);
								if(params2.x == 0)
									down = true;
						   }
				    	   handler.postDelayed(this, 10);
					} catch (Exception e) {
						// TODO: handle exception
					}
					  				      
				    }
				};
				handler.postDelayed(runnable, 2000);
		} catch (Exception e) {
			// TODO: handle exception
		}

			
		chatHead3.setOnTouchListener(new View.OnTouchListener() {
		

			  @Override public boolean onTouch(View v, MotionEvent event) {
			    switch (event.getAction()) {
			      case MotionEvent.ACTION_DOWN:
			        System.out.println("DOOOOWN");
			        
			        return true;
			      case MotionEvent.ACTION_UP:
			    	  System.out.println("UUUUUPP");
			        return true;
			     
			    }
			    return false;
			  }
			});
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatHead != null) windowManager.removeView(chatHead);
		if (chatHead2 != null) windowManager.removeView(chatHead2);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}

