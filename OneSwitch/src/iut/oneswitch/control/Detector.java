package iut.oneswitch.control;


import iut.oneswitch.app.OneSwitchService;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class Detector{
	private OneSwitchService theService;
	private LinearLayout touchLayout;

	public Detector(OneSwitchService paramOneSwitchService){
		theService = paramOneSwitchService;
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
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		mParams.gravity = Gravity.LEFT | Gravity.TOP;  

		theService.addView(touchLayout, mParams);
	}

	private void listener(){
		touchLayout.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent){
				if (paramAnonymousMotionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
					System.out.println("TOUCHED");
				}
				return true;
			}
		});
		
	
	}


}
