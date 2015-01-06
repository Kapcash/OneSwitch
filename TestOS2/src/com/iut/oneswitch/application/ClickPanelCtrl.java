package com.iut.oneswitch.application;

import com.example.oneswitch.R;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * @author OneSwitch B
 *Classe permettant de gèrer le panel attrapant le clic
 */
public class ClickPanelCtrl implements OnClickListener, OnLongClickListener {
	
	private OneSwitchService theService;
	private View thePanel;
	private WindowManager.LayoutParams clickParams;
	private ClickHandler theHandler;
	
	private boolean isShown = false;
	
	public ClickPanelCtrl(OneSwitchService service) {
		//this.thePanel = new View(service);
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
			
		//thePanel.setOnTouchListener(this); 
		
		thePanel.setOnClickListener(this);
		thePanel.setOnLongClickListener(this);

		
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
	public void onClick(View v) {
		theHandler = new ClickHandler();
		theHandler.handleClick(theService, this);
	}

	@Override
	public boolean onLongClick(View v) {
		//AJOUT DU MENU DE RACCOURCIS
		//Toast.makeText(theService,"COUCOU LE LONG CLICK AU BOULOT YOANN", 
        //        Toast.LENGTH_SHORT).show();
		System.out.println("TEST");

		PopupWindow popUp = new PopupWindow(theService);

		LayoutInflater inflater = (LayoutInflater)theService.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.contextpopup,null);
		popUp.setContentView(view);

		popUp.showAtLocation(thePanel, Gravity.CENTER, 0, 0);
		popUp.update(28, 0, 400, 400);
	    
		return true;
	}
	

	
}
