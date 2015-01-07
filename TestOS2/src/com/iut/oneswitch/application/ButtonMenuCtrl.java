package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.iut.oneswitch.view.popup.ShortcutMenuView;

public class ButtonMenuCtrl {
	
	private ShortcutMenuView theShortcutMenu;
	
	boolean isRunning = false;
	
	private Handler handler;
	private Runnable runnable;
	
	private WindowManager.LayoutParams shortcutMenuParams;
	private int posX, posY;

	private OneSwitchService theService;
	
	public ButtonMenuCtrl(OneSwitchService service) {
		this.theService = service;

		isRunning = false;
		init();
		
/*		buttonList = new Button[7];
		popUp = new PopupWindow(service);

		LayoutInflater inflater = (LayoutInflater)service.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		view = inflater.inflate(R.layout.contextpopup,null);
		popUp.setContentView(view);

		float density = service.getResources().getDisplayMetrics().density;

		popUp.showAtLocation(panel, Gravity.CENTER, 0, 0);
		popUp.update(28, 0, (int)(400*density), (int)(400*density));
		
		handler = new Handler();
		runnable = new PopupMenuRunnable();
		
		startThread();*/
	}
	
	
	private void init(){
		float density = theService.getResources().getDisplayMetrics().density;

			
		/* Drawing Popup */
			theShortcutMenu = new ShortcutMenuView(theService, this);
			shortcutMenuParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
					theService.getStatusBarHeight(),
					WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
					WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
					WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
					PixelFormat.TRANSLUCENT);
			shortcutMenuParams.gravity = Gravity.TOP | Gravity.LEFT;
			
			shortcutMenuParams.x = 0;
			shortcutMenuParams.y = 0;
			shortcutMenuParams.height = theService.getScreenSize().y;
			shortcutMenuParams.width = theService.getScreenSize().x;
			theService.addView(theShortcutMenu, shortcutMenuParams);
		/*End Drawing Popup*/
			
		handler = new Handler();
		runnable = new PopupMenuRunnable();
	}

	public void startThread(){
		isRunning = true;
		handler.postDelayed(runnable, 1000);
	}
	
	public void removeView() {
		theService.removeView(theShortcutMenu);

	}
	/*public void startThread(){
		isRunning = true;
		
		Button butBack = (Button)view.findViewById(R.id.but_back);
		Button butHome = (Button)view.findViewById(R.id.but_home);
		Button butMenu = (Button)view.findViewById(R.id.but_menu);
		Button butVolup = (Button)view.findViewById(R.id.but_volup);
		Button butVoldown = (Button)view.findViewById(R.id.but_voldown);
		Button butLock = (Button)view.findViewById(R.id.but_lock);
		Button butShut = (Button)view.findViewById(R.id.but_shutdown);
		
		selected = butBack;
		selectedIndex = 0;

		buttonList[0] = butBack;
		buttonList[1] = butHome;
		buttonList[2] = butMenu;
		buttonList[3] = butVolup;
		buttonList[4] = butVoldown;
		buttonList[5] = butLock;
		buttonList[6] = butShut;
		
		buttonList[selectedIndex].getBackground().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));

		for(int i = 0; i<buttonList.length;i++){
			if(i != selectedIndex){
				buttonList[i].getBackground().clearColorFilter();
			}
		}
		
		popUp.setContentView(view);
		
		ButtonMenuHandler handle = new ButtonMenuHandler(this);
		for(int i=0;i<=6;i++){
			buttonList[i].setOnClickListener(handle);
		}
		handler.postDelayed(runnable, 1000);
	}
	*/
	
	public void stopThread(){
		isRunning = false;
	}
	
	
	public Button getSelected() {
		return theShortcutMenu.getSelected();
	}

	public View getButton(int i) {
		return theShortcutMenu.getButton(i);
	}
	
	/*public void removeView(){
		popUp.dismiss();
	}*/
	
	/**
	 * Permet la selection d'un bouton du menu
	 * @author OneSwitch B
	 *
	 */
	class PopupMenuRunnable implements Runnable{

		/**
		 * Permet le défilement des boutons
		 */
		@Override
		public void run() {
			try {
				if(isRunning){ 
					theShortcutMenu.selectNext();
					handler.postDelayed(this, 1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}






	
}
