package com.iut.oneswitch.application;

import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.iut.oneswitch.view.popup.PopUpItem;
import com.iut.oneswitch.view.popup.PopUpPage;
import com.iut.oneswitch.view.popup.PopUpView;

public class PopUpCtrl {
	private OneSwitchService theService;
	private PopUpView thePopup;
	private WindowManager.LayoutParams popupParams;
	private int posX, posY;
	
	public PopUpCtrl(OneSwitchService service, int x, int y) {
		this.theService = service;
		this.posX = x;
		this.posY = y;
		init();
	}
	
	private void init(){
		thePopup = new PopUpView(theService);
		PopUpPage popupPage1 = new PopUpPage();
		PopUpItem popupItem1 = new PopUpItem("item1");
		PopUpItem popupItem2 = new PopUpItem("item2");
		PopUpItem popupItem3 = new PopUpItem("item3");
		
		popupPage1.addItem(popupItem1);
		popupPage1.addItem(popupItem2);
		popupPage1.addItem(popupItem3);


		thePopup.addPage(popupPage1);
		
		popupParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);



		popupParams.gravity = Gravity.TOP | Gravity.LEFT;
		popupParams.x = this.posX-14; //14 = rayon du cercle (12) + largeur du trait (2)
		//popupParams.x = this.posX;
		//popupParams.y = this.posY;
		popupParams.y = this.posY-76;
		popupParams.height = 152;
		popupParams.width = 178+28;
		
		theService.addView(thePopup, popupParams);
	}
	
	public void display(){
		
	}
}
