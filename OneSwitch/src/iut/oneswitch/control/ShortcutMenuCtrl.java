package iut.oneswitch.control;

import iut.oneswitch.app.OneSwitchService;
import iut.oneswitch.view.ShortcutMenuView;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Classe permettant de gèrer la popUp sur un appuie long.
 * @author OneSwitch B
 *
 */
public class ShortcutMenuCtrl{
	private Handler handler;
	private boolean isStarted = false;
	private Runnable runnable;
	private WindowManager.LayoutParams shortcutMenuParams;
	private OneSwitchService theService;
	private ShortcutMenuView theShortcutMenu;
	private SharedPreferences sp;

	public ShortcutMenuCtrl(OneSwitchService service){
		theService = service;
		sp = PreferenceManager.getDefaultSharedPreferences(service);
		init();
	}

	/**
	 * Permet d'initialiser la popUp.
	 */
	private void init(){
		float density = theService.getResources().getDisplayMetrics().density;
		int width = 305;
		int height = 245;

		theShortcutMenu = new ShortcutMenuView(theService, this);
		shortcutMenuParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);

		theShortcutMenu.setFocusable(false);
		theShortcutMenu.setFocusableInTouchMode(false);

		shortcutMenuParams.height = (int)(height*density);
		shortcutMenuParams.width = (int)(width*density);
		theService.addView(theShortcutMenu, shortcutMenuParams);
		handler = new Handler();
		runnable = new PopupMenuRunnable();
	}



	public Button getSelected(){
		Button localButton = null;
		if(theShortcutMenu!=null)
			localButton = theShortcutMenu.getSelected();
		return localButton;
	}

	public OneSwitchService getService(){
		return theService;
	}

	public boolean isShow(){
		return isStarted;
	}

	/**
	 * Permet de supprimer les vues.
	 */
	public void removeView(){
		isStarted = false;
		handler.removeCallbacksAndMessages(runnable);
		if (theShortcutMenu != null){
			theService.removeView(theShortcutMenu);
			theShortcutMenu = null;
			theService.getClickPanelCtrl().closePopupCtrl();
		}
	}

	public void start(){
		isStarted = true;
		handler.post(runnable);
	}

	class PopupMenuRunnable implements Runnable{
		@Override
		public void run(){
			if(isStarted){
				theShortcutMenu.selectNext();
				if(sp.getBoolean("vocal",false)) {
					handler.postDelayed(this, 1700);
				}
				else
					handler.postDelayed(this, 1000);
			}
		}

	}
}
