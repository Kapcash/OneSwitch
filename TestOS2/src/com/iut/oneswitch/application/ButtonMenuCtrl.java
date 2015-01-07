package com.iut.oneswitch.application;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.oneswitch.R;

public class ButtonMenuCtrl {
	boolean isRunning = false;
	
	private Handler handler;
	private Runnable runnable;
	
	private Button selected;
	private int selectedIndex;
	private View view;
	private Button buttonList[];
	
	private OneSwitchService theService;
	
	private PopupWindow popUp;
	
	
	public ButtonMenuCtrl(OneSwitchService service, View panel) {
		theService = service;
		buttonList = new Button[7];
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
		
		startThread();
	}
	
	public Button getSelected(){
		return selected;
	}
	
	public Button getButton(int index){
		if(index < 0 || index > 7) throw new IllegalArgumentException("Mauvais index");
		return buttonList[index];
	}
	
	public void selectNext(){
		selectedIndex=(selectedIndex+1)%7;
		selected = buttonList[selectedIndex];
		
		buttonList[selectedIndex].getBackground().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));

		for(int i = 0; i<buttonList.length;i++){
			if(i != selectedIndex){
				buttonList[i].getBackground().clearColorFilter();
			}
		}
		
	
		popUp.setContentView(view);
	}
	
	
	public void startThread(){
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
	
	public void stopThread(){
		isRunning = false;
	}
	
	public void removeView(){
		popUp.dismiss();
	}
	
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
					selectNext();

					handler.postDelayed(this, 1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
