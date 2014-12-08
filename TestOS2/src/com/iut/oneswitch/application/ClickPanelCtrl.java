package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;

/**
 * @author OneSwitch B
 *Classe permettant de gèrer le panel attrapant le clic
 */
public class ClickPanelCtrl implements OnTouchListener {
	
	private OneSwitchService theService;
	private View thePanel;
	private WindowManager.LayoutParams clickParams;
	private ClickHandler theHandler;
	
	private boolean isShown = false;
	
	public ClickPanelCtrl(OneSwitchService service) {
		this.thePanel = new View(service);
		this.theService = service;
		init();
	}
	
	/**
	 * création du panel prenant en compte la surface de l'écran
	 */
	public void init(){

		clickParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT); 

		clickParams.gravity = Gravity.TOP | Gravity.LEFT;
		clickParams.x = 0;
		clickParams.y = 0;
		clickParams.height = theService.getScreenSize().y;
		clickParams.width = theService.getScreenSize().x;
		
		
			
		thePanel.setOnTouchListener(this); 
		
		add();
	}
	
	/**
	 * Ajout du panel
	 */
	private void add(){
		if(!isShown){
			theService.addView(thePanel, clickParams);
			isShown = true;
		}
	
	}
	
	/**
	 * Enleve le panel de l'écran
	 */
	public void remove(){
		if((isShown)){
			if(theHandler != null){
				theHandler.stop();
			}
			theService.removeView(thePanel);
			isShown = false;
		}

	}
	
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			theHandler = new ClickHandler();
			theHandler.handleClick(theService, this);
		}
	
		return false;
	}

}
