package iut.oneswitch.control;

import iut.oneswitch.app.OneSwitchService;
import iut.oneswitch.view.ShortcutMenuView;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Button;

public class ShortcutMenuCtrl{
	private Handler handler;
	private boolean isRunning = false;
	private Runnable runnable;
	private WindowManager.LayoutParams shortcutMenuParams;
	private OneSwitchService theService;
	private ShortcutMenuView theShortcutMenu;
	private int selectedIndex=-1;

	public ShortcutMenuCtrl(OneSwitchService service){
		theService = service;
		isRunning = false;
		init();
	}

	private void init(){
		float density = theService.getResources().getDisplayMetrics().density;
		int width = 305;
		int height = 245;
		
		theShortcutMenu = new ShortcutMenuView(theService, this);
		shortcutMenuParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		
		shortcutMenuParams.height = (int)(height*density);
		shortcutMenuParams.width = (int)(width*density);
		theService.addView(theShortcutMenu, shortcutMenuParams);
		handler = new Handler();
		runnable = new PopupMenuRunnable();
	}



	public Button getSelected(){
		return theShortcutMenu.getButton(selectedIndex);
	}

	public OneSwitchService getService(){
		return theService;
	}

	public void removeView(){
		isRunning = false;
		if (theShortcutMenu != null){
			theService.removeView(theShortcutMenu);
			theService.getClickPanelCtrl().closePopupCtrl();
		}
	}

	public void startThread(){
		isRunning = true;
		handler.postDelayed(runnable, 1);
	}
	
	private void nextIndex(){
		if(selectedIndex<5) selectedIndex++;
		else selectedIndex = 0;
	}

	class PopupMenuRunnable implements Runnable{
		@Override
		public void run(){
			try{
				if(isRunning){
					nextIndex();
					theShortcutMenu.selectNext();
					handler.postDelayed(this, 1000);
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
