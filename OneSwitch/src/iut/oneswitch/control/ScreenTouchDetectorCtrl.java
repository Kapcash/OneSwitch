package iut.oneswitch.control;

import iut.oneswitch.app.OneSwitchService;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ScreenTouchDetectorCtrl{
	private OneSwitchService theService;
	private LinearLayout touchLayout;
	private WindowManager.LayoutParams panelBas,panelHaut,panelGauche,panelDroite;
	private View haut, bas, gauche, droite;

	public ScreenTouchDetectorCtrl(OneSwitchService service){
		theService = service;
		init();
		listener();
	}

	private void init(){
		touchLayout = new LinearLayout(theService);
		LayoutParams lp = new LayoutParams(0, 0);
		touchLayout.setLayoutParams(lp);

		haut = new View(theService);
		bas = new View(theService);
		droite = new View(theService);
		gauche = new View(theService);

		WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
				0,
				0,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		mParams.gravity = Gravity.START | Gravity.TOP;  

		panelBas = new WindowManager.LayoutParams(
				0,
				0,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		panelBas.gravity = Gravity.START | Gravity.TOP;

		panelHaut = new WindowManager.LayoutParams(
				0,
				0,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		panelHaut.gravity = Gravity.START | Gravity.TOP; 

		haut.setBackgroundColor(Color.BLACK);
		bas.setBackgroundColor(Color.WHITE);

		theService.addView(touchLayout, mParams);

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
	}

	public void close(){
		theService.getClickPanelCtrl().clickDone();
		removeView();
		touchLayout = null;
		bas = null;
		haut = null;
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

		/*panelDroite.x = x+2;
        panelDroite.y = 0;
        panelDroite.width = theService.getScreenSize().x-(x+2);
        panelDroite.height = theService.getScreenSize().y;

        panelGauche.x = 0;
        panelGauche.y = 0;
        panelGauche.width =theService.getScreenSize().x-panelDroite.width-2;
        panelGauche.height = theService.getScreenSize().y;
		 */

		//theService.updateViewLayout(haut, panelHaut);
		//theService.updateViewLayout(bas, panelBas);
		//theService.updateViewLayout(droite, panelDroite);
		//theService.updateViewLayout(gauche, panelGauche);
	}

	public void removeView(){
		if(touchLayout!=null){
			touchLayout.setOnClickListener(null);
			theService.removeView(touchLayout);
			theService.removeView(bas);
			theService.removeView(haut);
			touchLayout = null;
		}
	}
}
