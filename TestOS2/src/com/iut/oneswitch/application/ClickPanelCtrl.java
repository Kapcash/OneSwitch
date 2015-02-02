package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;

/**
 * Classe permettant de gèrer le panel (la vue) attrapant le clic.
 * @author OneSwitch B
 */
public class ClickPanelCtrl implements OnClickListener, OnLongClickListener {
	
	/**
	 * Cet attribut stockera le service de notre application.
	 */
	private OneSwitchService theService;
	
	/**
	 * 
	 */
	private View thePanel;
	
	/**
	 * 
	 */
	private WindowManager.LayoutParams clickParams;
	
	/**
	 * 
	 */
	private ClickHandler theHandler;
	
	/**
	 * 
	 */
	private boolean forSwipe = false;
	
	/**
	 * 
	 */
	private boolean clickable = true;
	
	/**
	 * 
	 */
	private boolean isShown = false;
	
	/**
	 * Constructeur de ClickPanelCtrl.
	 * Initialise la vue thePanel et le service avec celui de l'application.
	 * @param service Le service de l'application.
	 */
	public ClickPanelCtrl(OneSwitchService service) {
		this.thePanel = new View(service);
		this.theService = service;
		init();
	}
	
	/**
	 * Création du panel prenant en compte la surface de l'écran.
	 * Met sur écoute la vue "thePanel" dans le but de gérer les clic.
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
	 * Cette méthode permet d'ajouter "thePanel" à notre service afin de le faire apparâitre à l'écran.
	 */
	private void add(){
		if(!isShown){
			theService.addView(thePanel, clickParams);
			isShown = true;
		}
	}
	
	/**
	 * Retire "thePanel" du service le faisant disparaître de l'écran.
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
	
	/**
	 * 
	 * @return La vue "thePanel" prenant la surface de l'écran.
	 */
	public View getView(){
		return thePanel;
	}
	
	/**
	 * 
	 * @return Vrai si la vue "thePanel" à été effacée de l'écran et que le clic est de nouveau disponible.
	 */
	public boolean isClickable(){
		return clickable;
	}
	
	/**
	 * Retire "thePanel" du service le faisant disparaître de l'écran.
	 * Indique, au travers du booléen clickable, que le clic est de nouveau disponible.
	 */
	public void removeService(){
		clickable = false;
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				if((isShown)){
					if(theHandler != null){
						theHandler.stop();
					}
					theService.removeView(thePanel);
					isShown = false;
					clickable = true;
				}
			}
		}, 900);

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
	
	/**
	 * Donne à l'attribut "forSwipe" la même valeur que l'attribut passé en paramètre.
	 * @param res La valeur à attribuer à forSwipe.
	 */
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