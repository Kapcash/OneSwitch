package com.iut.oneswitch.view.popup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.oneswitch.R;
import com.iut.oneswitch.application.ButtonMenuCtrl;
import com.iut.oneswitch.application.ButtonMenuHandler;
import com.iut.oneswitch.application.ClickHandler;

public class ShortcutMenuView extends View{

	private PopupWindow popUp;

	private ButtonMenuCtrl theCtrl;

	private Button selected;
	private int selectedIndex;
	private View view;
	private Button buttonList[];
	private ButtonMenuHandler handle;
	private int iterations;

	public ShortcutMenuView(Context context, ButtonMenuCtrl ctrl) {
		super(context);
		theCtrl = ctrl;
		popUp = new PopupWindow(this.getContext());
		handle = new ButtonMenuHandler(this);
	}

	public Button getSelected(){
		return selected;
	}
	
	public Button getButton(int index){
		if(index < 0 || index > 7) throw new IllegalArgumentException("Mauvais index");
		return buttonList[index];
	}
	
	public void selectNext(){
		if(iterations==3){
			stopThread();
			removeView();
			ClickHandler.notifyContextMenuClosed();
		}
		else{
			selectedIndex=(selectedIndex+1)%7;
			selected = buttonList[selectedIndex];
			
			buttonList[selectedIndex].getBackground().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
	
			for(int i = 0; i<buttonList.length;i++){
				if(i != selectedIndex){
					buttonList[i].getBackground().clearColorFilter();
				}
			}
			if(selectedIndex == buttonList.length-1){
				iterations++;
			}
			popUp.setContentView(view);
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		float density = getResources().getDisplayMetrics().density;

		buttonList = new Button[7];

		LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		view = inflater.inflate(R.layout.contextpopup,null);
		popUp.setContentView(view);

		popUp.showAtLocation(this, Gravity.CENTER, 0, 0);
		popUp.update(28, 0, (int)(300*density), (int)(370*density));
		
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
		
		for(int i=0;i<=6;i++){
			buttonList[i].setOnClickListener(handle);
		}
		theCtrl.startThread();
	}

	public void removeView(){
		theCtrl.removeView();
	}

	/**
	 * Stop le thread du défilement	
	 */
	public void stopThread(){
		theCtrl.stopThread();
	}
}