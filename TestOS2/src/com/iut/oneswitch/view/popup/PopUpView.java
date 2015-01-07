package com.iut.oneswitch.view.popup;

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
import com.iut.oneswitch.application.PopUpCtrl;
import com.iut.oneswitch.application.PopUpHandler;

public class PopUpView extends View{

	private PopupWindow popUp;
	private Button selected;
	private View view;
	private PopUpCtrl theCtrl;
	private PopUpHandler handler;
	
	public PopUpCtrl getCtrl(){
		return theCtrl;
	}
	/**
	 * Bouton "clic" de la popup
	 */
	private Button butClic;
	/**
	 * Bouton "clic long" de la popup
	 */
	private Button butClicLong;
	/**
	 * Bouton "glisser" de la popup
	 */
	private Button butGlisser;

	public PopUpView(Context context, PopUpCtrl ctrl) {
		super(context);
		theCtrl = ctrl;
		popUp = new PopupWindow(this.getContext());
		handler = new PopUpHandler(this);
	}

	public Button getSelected(){
		return selected;
	}
	
	public void selectNext(){
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
		}	
		popUp.setContentView(view);
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
		theCtrl.startThread(); //select buttons l'un apres l'autre
		
		butClic.setOnClickListener(handler);
		butClicLong.setOnClickListener(handler);
		butGlisser.setOnClickListener(handler);
	}

	public void removeView(){
		theCtrl.removeView();
	}

	/**
	 * Stop le thread du défilement (via PopUpCtrl)	
	 */
	public void stopThread(){
		theCtrl.stopThread();
	}
	public Button getButClic() {
		return butClic;
	}

	public Button getButClicLong() {
		return butClicLong;
	}

	public Button getButGlisser() {
		return butGlisser;
	}
	
	public Point getPos(){
		return theCtrl.getPos();
	}
}