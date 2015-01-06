package com.iut.oneswitch.view.popup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.oneswitch.R;
import com.iut.oneswitch.application.PopUpCtrl;


public class CircleView extends View{


	private PopupWindow popUp;
	private Canvas theCanvas;
	private View view;
	private PopUpCtrl theCtrl;

	

	public CircleView(Context context, PopUpCtrl ctrl) {
		super(context);
		theCtrl = ctrl;
	}


	@Override
	public void onDraw(Canvas canvas) {
		theCanvas = canvas;
		float density = getResources().getDisplayMetrics().density;


		System.out.println("drawing popup view");

		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		view = inflater.inflate(R.layout.popup,null);

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



	}

}
