package iut.oneswitch.control;

import iut.oneswitch.app.OneSwitchService;
import iut.oneswitch.view.CircleView;
import iut.oneswitch.view.PopupView;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class PopupCtrl
{
	private CircleView circle;
	private WindowManager.LayoutParams circleParams;
	private float density;
	private Handler handler;
	private boolean isStarted = false;
	private WindowManager.LayoutParams popupParams;
	private int posX = 0;
	private int posY = 0;
	private PopupRunnable runnable;
	private PopupView thePopup;
	private OneSwitchService theService;

	public PopupCtrl(OneSwitchService service, int x, int y){
		posX = x;
		posY = y;
		theService = service;
		init();
	}

	private void init(){
		int widthCircle = 28;
		int heightCircle = 28;
		int largeurTrait = 2;
		
		density = theService.getResources().getDisplayMetrics().density;
		circle = new CircleView(theService, this);
		circleParams = new WindowManager.LayoutParams(0,0,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		circleParams.gravity = Gravity.TOP | Gravity.LEFT;
		circleParams.x = (int) (this.posX-((widthCircle/largeurTrait)*density));
		circleParams.y = (int) (this.posY-((heightCircle/largeurTrait)*density));
		circleParams.height = (int) (density*heightCircle);
		circleParams.width  = (int) (density*widthCircle);
		theService.addView(circle, circleParams);

		thePopup = new PopupView(theService, this);
		popupParams = new WindowManager.LayoutParams(0,0,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		popupParams.gravity = Gravity.TOP | Gravity.LEFT;

		
		//TAILLE DE LA POPUP (en DP)
		int widthPopup = 170;
		int heightPopup = 190;
		
		//ESPACE ENTRE LA POPUP ET LE CERCLE
		int spacing = 5;
		
		popupParams.width  = (int) (density*widthPopup);
		popupParams.height = (int) (density*heightPopup);
		int rightPadding = (theService.getScreenSize().x - (posX) + spacing);
		if(rightPadding>((widthPopup+5)*density))	popupParams.x = (circleParams.x+circleParams.width + spacing);
		else	popupParams.x = (int) circleParams.x - popupParams.width - spacing;
		popupParams.y = (int) (this.posY-((heightPopup/2)*density));
		theService.addView(thePopup, popupParams);
		
		handler = new Handler();
		runnable = new PopupRunnable();
		
		
	}

	public Point getPos(){
		return new Point(posX, posY);
	}

	public View getSelected(){
		Button localButton = null;
		if (thePopup != null) {
			localButton = thePopup.getSelected();
		}
		return localButton;
	}

	public OneSwitchService getService(){
		return theService;
	}

	public boolean isShow(){
		return isStarted;
	}

	public void removeCircle(){
		if (circle != null) {
			theService.removeView(circle);
			circle = null;
		}
	}

	public void closePopup(){
		isStarted = false;
		if (thePopup != null) {
			theService.removeView(thePopup);
			thePopup = null;
		}
	}

	public void removeAllViews(){
		isStarted = false;
		if (thePopup != null) {
			theService.removeView(thePopup);
			thePopup = null;
		}
		if (circle != null) {
			theService.removeView(circle);
			circle = null;
		}
	}

	public void start(){
		isStarted = true;
		handler.post(runnable);
	}

	class PopupRunnable implements Runnable{
		public void run(){
			if (isStarted){
				thePopup.selectNext();
				handler.postDelayed(this, 1000);
			}
		}
	}
}
