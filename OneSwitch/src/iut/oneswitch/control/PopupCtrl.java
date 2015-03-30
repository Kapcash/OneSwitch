package iut.oneswitch.control;

import iut.oneswitch.app.OneSwitchService;
import iut.oneswitch.view.CircleView;
import iut.oneswitch.view.PopupView;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Classe permettant de gèrer la popUp sur un appuie simple.
 * @author OneSwitch B
 *
 */
public class PopupCtrl{
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
	private SharedPreferences sp;

	/**
	 * Constructeur de la classe.
	 * @param service
	 * @param x
	 * @param y
	 */
	public PopupCtrl(OneSwitchService service, int x, int y){
		posX = x;
		posY = y;
		theService = service;
		sp = PreferenceManager.getDefaultSharedPreferences(service);
		init();
	}

	/**
	 * Permet d'initialiser la popUp.
	 */
	private void init(){
		int widthCircle = 28;
		int heightCircle = 28;
		int largeurTrait = 2;
		
		density = theService.getResources().getDisplayMetrics().density;
		circle = new CircleView(theService, this);
		circleParams = new WindowManager.LayoutParams(0,0,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		circleParams.gravity = Gravity.TOP | Gravity.START;
		circleParams.x = (int) (this.posX-((widthCircle/largeurTrait)*density));
		circleParams.y = (int) (this.posY-((heightCircle/largeurTrait)*density));
		circleParams.height = (int) (density*heightCircle);
		circleParams.width  = (int) (density*widthCircle);
		theService.addView(circle, circleParams);

		thePopup = new PopupView(theService, this);
		popupParams = new WindowManager.LayoutParams(0,0,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		popupParams.gravity = Gravity.TOP | Gravity.START;
		
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

	/**
	 * Permet de générer un nouveau point
	 * @return un nouveau point
	 */
	public Point getPos(){
		return new Point(posX, posY);
	}

	/**
	 * Ressort le bouton actuellement sélectionné
	 * @return le bouton sélectionné
	 */
	public View getSelected(){
		Button localButton = null;
		if (thePopup != null) {
			localButton = thePopup.getSelected();
		}
		return localButton;
	}

	/**
	 * Retourne le service
	 * @return the Service
	 */
	public OneSwitchService getService(){
		return theService;
	}

	/**
	 * Supprime le cercle
	 */
	public void removeCircle(){
		if (circle != null) {
			theService.removeView(circle);
			circle = null;
		}
	}

	/**
	 * Suprimes la pop-up
	 */
	public void closePopup(){
		isStarted = false;
		handler.removeCallbacksAndMessages(runnable);
		if (thePopup != null) {
			theService.removeView(thePopup);
			thePopup = null;
		}
	}

	/**
	 * Permet de supprimer les vues.
	 */
	public void removeAllViews(){
		isStarted = false;
		handler.removeCallbacksAndMessages(runnable);
		if (thePopup != null) {
			theService.removeView(thePopup);
			thePopup = null;
		}
		if (circle != null) {
			theService.removeView(circle);
			circle = null;
		}
	}

	/**
	 * Permet de démarrer la sélection des boutons dans la pop-up
	 */
	public void start(){
		isStarted = true;
		handler.post(runnable);
	}

	/**
	 * Permet de gèrer la sélection dans la pop-up
	 * @author OneSwitch_B
	 *
	 */
	class PopupRunnable implements Runnable{
		public void run(){
			if (isStarted){
				thePopup.selectNext();
				if(sp.getBoolean("vocal",false)) {
					handler.postDelayed(this, 1700);
				}
				else
					handler.postDelayed(this, 1000);
			}
		}
	}
}
