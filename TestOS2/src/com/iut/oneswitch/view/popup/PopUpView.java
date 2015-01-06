package com.iut.oneswitch.view.popup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
	private Canvas theCanvas;
	private Button selected;
	private View view;
	private PopUpCtrl theCtrl;
	

	public PopUpView(Context context, PopUpCtrl ctrl) {
		super(context);
		theCtrl = ctrl;
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
		theCanvas = canvas;
		float density = getResources().getDisplayMetrics().density;


		System.out.println("drawing popup view");

		popUp = new PopupWindow(this.getContext());

		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		view = inflater.inflate(R.layout.popup,null);
		popUp.setContentView(view);

		popUp.showAtLocation(this, Gravity.NO_GRAVITY, 0, 0);
		popUp.update((int)density*28, 0, (int)(canvas.getWidth()-(28*density)), canvas.getHeight());


		Paint paintCircle = new Paint();
		paintCircle.setColor(Color.BLACK);
		paintCircle.setAlpha(255);
		paintCircle.setStrokeWidth((int)(2*density));
		paintCircle.setStyle(Paint.Style.STROKE);
		paintCircle.setAntiAlias(true);

		canvas.drawCircle(14*density, (canvas.getHeight()/2), 12*density, paintCircle);
		canvas.drawCircle(14*density, (canvas.getHeight()/2), 1*density, paintCircle);

		paintCircle.setAlpha(64);
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(14*density, (canvas.getHeight()/2), 12*density, paintCircle);

		selected = (Button)view.findViewById(R.id.but_glisser);
		theCtrl.startThread(); //select buttons l'un apres l'autre
		
		Button butClic = (Button)view.findViewById(R.id.but_clic);
		Button butClicLong = (Button)view.findViewById(R.id.but_clic_long);
		Button butGlisser = (Button)view.findViewById(R.id.but_glisser);
		
		butClic.setOnClickListener(new PopUpHandler());
		butClicLong.setOnClickListener(new PopUpHandler());
		butClicLong.setOnClickListener(new PopUpHandler());


	}

}
