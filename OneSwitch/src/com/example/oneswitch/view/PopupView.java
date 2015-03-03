package com.example.oneswitch.view;

import java.io.IOException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.oneswitch.R;
import com.example.oneswitch.control.ClickPanelCtrl;
import com.example.oneswitch.control.PopupCtrl;

public class PopupView extends View{

	private PopupWindow popUp;
	private Button selected;
	private View view;
	private PopupCtrl theCtrl;
	private int iterations;
	public PopupCtrl getCtrl(){
		return theCtrl;
	}
	private Button butClic;
	private Button butClicLong;
	private Button butGlisser;

	public PopupView(Context context, PopupCtrl ctrl) {
		super(context);
		theCtrl = ctrl;
		popUp = new PopupWindow(this.getContext());
	}
	
	public Button getSelected(){
		return selected;
	}
	
	public void selectNext(){
		if(iterations==3){
			clickPanel().closePopupCtrl();
		}
		else{
			Button butClic = (Button)view.findViewById(R.id.but_clic);
			Button butClicLong = (Button)view.findViewById(R.id.but_clic_long);
			Button butGlisser = (Button)view.findViewById(R.id.but_glisser);
	
			if(selected == butGlisser){
				butClic.getBackground().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
				butClicLong.getBackground().clearColorFilter();
				butGlisser.getBackground().clearColorFilter();
				selected = butClic;
			}	
			else if(selected == butClic){
				butClic.getBackground().clearColorFilter();
				butClicLong.getBackground().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
				butGlisser.getBackground().clearColorFilter();
				selected = butClicLong;
			}	
			else if(selected == butClicLong){
				butClic.getBackground().clearColorFilter();
				butClicLong.getBackground().clearColorFilter();
				butGlisser.getBackground().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
				selected = butGlisser;
				iterations++;
			}	
			popUp.setContentView(view);
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		float density = getResources().getDisplayMetrics().density;

		System.out.println("Drawing popup view");

		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		view = inflater.inflate(R.layout.popup,null);
		popUp.setContentView(view);

		popUp.showAtLocation(this, Gravity.NO_GRAVITY, 0, 0);
		popUp.update((int)density*28, 0, (int)(canvas.getWidth()-(28*density)), canvas.getHeight());

		butClic = (Button)view.findViewById(R.id.but_clic);
		butClicLong = (Button)view.findViewById(R.id.but_clic_long);
		butGlisser = (Button)view.findViewById(R.id.but_glisser);
		
		selected = butGlisser;
		theCtrl.start(); //select buttons l'un apres l'autre
		iterations=0;
		
		listener();
	}
	
	private void listener(){
		butClic.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				clickPanel().setVisible(false);
				int x = clickPanel().getPos().x;
				int y = clickPanel().getPos().y;
				try {
					Runtime.getRuntime().exec("su -c input tap " + x + " " + y);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		butClicLong.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				clickPanel().setVisible(false);
				int x = clickPanel().getPos().x;
				int y = clickPanel().getPos().y;
				try {
					Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + x + " " + y + " 800");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		butGlisser.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				clickPanel().setForSwipe(true);
				
			}
		});
	}
	
	public ClickPanelCtrl clickPanel(){
		return theCtrl.getService().getClickPanelCtrl();
	}

	
	/**
	 * Accesseur de l'attribut butClic.
	 * @return Le bouton "clic" de la popUp.
	 */
	public Button getButClic() {
		return butClic;
	}
	
	/**
	 * Accesseur de butClicLong.
	 * @return Le bouton "clic long" de la popUp.
	 */
	public Button getButClicLong() {
		return butClicLong;
	}
	
	/**
	 * Le bouton "glisser" de la popUp.
	 * @return
	 */
	public Button getButGlisser() {
		return butGlisser;
	}
	
	/**
	 * 
	 * @return La position de la popUp.
	 */
	public Point getPos(){
		return theCtrl.getPos();
	}
}