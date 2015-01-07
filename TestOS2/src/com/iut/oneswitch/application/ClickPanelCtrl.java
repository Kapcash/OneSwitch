package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;

/**
 * @author OneSwitch B
 *Classe permettant de gèrer le panel attrapant le clic
 */
public class ClickPanelCtrl implements OnClickListener, OnLongClickListener {
	
	private OneSwitchService theService;
	private View thePanel;
	private WindowManager.LayoutParams clickParams;
	private ClickHandler theHandler;
	private boolean forSwipe = false;
	
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
			
		thePanel.setOnLongClickListener(this);
		thePanel.setOnClickListener(this);
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
	
	public View getView(){
		return thePanel;
	}
	
	public void removeService(){
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				if((isShown)){
					if(theHandler != null){
						theHandler.stop();
					}
					theService.removeView(thePanel);
					isShown = false;
				}
			}
		}, 900);

	}
	
	public void bringToFront(){
		thePanel.bringToFront();
	}

	@Override
	public void onClick(View v) {
		theHandler = new ClickHandler();
		if(!forSwipe){
			theHandler.handleClick(theService, this);
		}
		else{
			theHandler.handleSwipe(theService, this);
		}
	}
	
	public void setForSwipe(boolean res){
		forSwipe = res;
	}

	@Override
	public boolean onLongClick(View v) {
		theHandler = new ClickHandler();
		theHandler.handleLongClick(theService, this);
		return true;
	}
}