package com.iut.oneswitch.view.popup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.oneswitch.R;
import com.iut.oneswitch.application.PopUpCtrl;


public class PopUpView extends View{


	private PopupWindow popUp;
	private Canvas theCanvas;
	private int selected;
	private View view;
	private PopUpCtrl theCtrl;
	
	public PopUpView(Context context, PopUpCtrl ctrl) {
		super(context);
		theCtrl = ctrl;
	}

	public void selectNext(){

		Button butClic = (Button)view.findViewById(R.id.but_clic);
		Button butClicLong = (Button)view.findViewById(R.id.but_clic_long);
		Button butGlisser = (Button)view.findViewById(R.id.but_glisser);
		switch (selected) {
		case 0:
			butClic.setBackgroundColor(Color.LTGRAY);
			butClicLong.setBackgroundColor(Color.DKGRAY);
			butGlisser.setBackgroundColor(Color.DKGRAY);
			selected++;
			break;
		case 1:
			butClic.setBackgroundColor(Color.DKGRAY);
			butClicLong.setBackgroundColor(Color.LTGRAY);
			butGlisser.setBackgroundColor(Color.DKGRAY);

			selected++;
			break;
		case 2:
			butClic.setBackgroundColor(Color.DKGRAY);
			butClicLong.setBackgroundColor(Color.DKGRAY);
			butGlisser.setBackgroundColor(Color.LTGRAY);
			selected=0;
			break;

		default:
			break;
		}
		
		popUp.setContentView(view);
	}

	@Override
	public void onDraw(Canvas canvas) {
		theCanvas = canvas;
		selected = 0;

		System.out.println("drawing popup view");

		popUp = new PopupWindow(this.getContext());

		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		view = inflater.inflate(R.layout.popup,null);
		popUp.setContentView(view);

		popUp.showAtLocation(this, Gravity.NO_GRAVITY, 0, 0);
		popUp.update(28, 0, canvas.getWidth()-28, canvas.getHeight());


		Paint paintCircle = new Paint();
		paintCircle.setColor(Color.BLACK);
		paintCircle.setAlpha(255);
		paintCircle.setStrokeWidth(2);
		paintCircle.setStyle(Paint.Style.STROKE);
		paintCircle.setAntiAlias(true);

		canvas.drawCircle(14, (canvas.getHeight()/2), 12, paintCircle);
		canvas.drawCircle(14, (canvas.getHeight()/2), 1, paintCircle);

		paintCircle.setAlpha(64);
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(14, (canvas.getHeight()/2), 12, paintCircle);

		
		theCtrl.startThread(); //select buttons l'un apres l'autre
		
	}

}
