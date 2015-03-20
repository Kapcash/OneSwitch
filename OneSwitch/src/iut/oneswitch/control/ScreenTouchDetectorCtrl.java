package iut.oneswitch.control;

import iut.oneswitch.app.OneSwitchService;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ScreenTouchDetectorCtrl{
	private OneSwitchService theService;
	private LinearLayout touchLayout;
	private View avoidStatusBar;
	private WindowManager.LayoutParams panelBas,panelHaut;
	private View haut, bas;

	public ScreenTouchDetectorCtrl(OneSwitchService service){
		theService = service;
		init();
		listener();
	}

	private void init(){
		touchLayout = new LinearLayout(theService);
		avoidStatusBar = new View(theService);
		LayoutParams lp = new LayoutParams(0, 0);
		touchLayout.setLayoutParams(lp);

		haut = new View(theService);
		bas = new View(theService);

		WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
				0,
				0,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		mParams.gravity = Gravity.START | Gravity.TOP; 
		
		WindowManager.LayoutParams paramStatusBar = new WindowManager.LayoutParams(
				theService.getScreenSize().x,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		paramStatusBar.gravity = Gravity.START | Gravity.TOP; 
		paramStatusBar.x = 0;
		paramStatusBar.y = 0;
		paramStatusBar.width = theService.getScreenSize().x;
		paramStatusBar.height = theService.getStatusBarHeight();

		panelBas = new WindowManager.LayoutParams(
				0,
				0,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		panelBas.gravity = Gravity.START | Gravity.TOP;

		panelHaut = new WindowManager.LayoutParams(
				0,
				0,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		panelHaut.gravity = Gravity.START | Gravity.TOP; 
		
		//avoidStatusBar.setBackgroundColor(Color.WHITE);
		//haut.setBackgroundColor(Color.BLACK);
		//bas.setBackgroundColor(Color.WHITE);

		theService.addView(touchLayout, mParams);
		theService.addView(avoidStatusBar, paramStatusBar);
		theService.addView(bas, panelBas);
		theService.addView(haut, panelHaut);
	}

	private void listener(){
		touchLayout.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent){
				if (paramAnonymousMotionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
					close();
				}
				return true;
			}
		});
		avoidStatusBar.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				close();
			}
		});
	}

	public void close(){
		theService.getClickPanelCtrl().clickDone();
		removeView();
	}

	public void giveCoord(int x, int y){
		panelBas.y = y+2;
		panelBas.x = 0;
		panelBas.width = theService.getScreenSize().x;
		panelBas.height = theService.getScreenSize().y-(y+2);

		panelHaut.y=0;
		panelHaut.x=0;
		panelHaut.width = theService.getScreenSize().x;
		panelHaut.height = (theService.getScreenSize().y)-(theService.getScreenSize().y-(y-2));

		theService.updateViewLayout(haut, panelHaut);
		theService.updateViewLayout(bas, panelBas);
	}

	public void removeView(){
		if(touchLayout!=null){
			touchLayout.setOnClickListener(null);
			theService.removeView(touchLayout);
			theService.removeView(bas);
			theService.removeView(haut);
			avoidStatusBar.setOnClickListener(null);
			theService.removeView(avoidStatusBar);
			touchLayout = null;
			bas = null;
			haut = null;
			avoidStatusBar=null;
		}
	}
}
