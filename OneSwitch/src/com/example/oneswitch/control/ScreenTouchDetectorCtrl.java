package com.example.oneswitch.control;

import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.example.oneswitch.appliction.OneSwitchService;

public class ScreenTouchDetectorCtrl {

		private LinearLayout touchLayout;
		private OneSwitchService theService;
		
		public ScreenTouchDetectorCtrl(OneSwitchService service){
			theService = service;
			
			init();
			listener();
			
		}
		
		private void init(){
			touchLayout = new LinearLayout(theService);
			LayoutParams lp = new LayoutParams(0, 0);
			touchLayout.setLayoutParams(lp);
			
			WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
				        0,
				        0,
				        WindowManager.LayoutParams.TYPE_PHONE | WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
				        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				        PixelFormat.TRANSLUCENT);
	         mParams.gravity = Gravity.LEFT | Gravity.TOP;   
		     theService.addView(touchLayout, mParams);
		}
		
		private void listener(){
			touchLayout.setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					 if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
						 theService.getClickPanelCtrl().clickDone();;
					 }
					return true;
				}

			});
		}
		
		public void removeView(){
			if(touchLayout != null) theService.removeView(touchLayout);
		}
}

