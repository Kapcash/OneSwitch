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
	private Button butClic;
	private Button butClicLong;
	private Button butGlisser;
	private int iterations;
	private PopupWindow popUp;
	private Button selected;
	private PopupCtrl theCtrl;
	private View view;

	public PopupView(Context paramContext, PopupCtrl paramPopupCtrl){
		super(paramContext);
		theCtrl = paramPopupCtrl;
		popUp = new PopupWindow(getContext());
	}

	private void listener(){
		butClic.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				clickPanel().setVisible(false);
				theCtrl.removeAllViews();
				int x = clickPanel().getPos().x;
				int y = clickPanel().getPos().y;
				try{
					Runtime.getRuntime().exec("su -c input tap " + x + " " + y);
				}
				catch (IOException e){
					e.printStackTrace();
				}
			}
		});
		butClicLong.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				clickPanel().setVisible(false);
				theCtrl.removeAllViews();
				int x = clickPanel().getPos().x;
				int y = clickPanel().getPos().y;
				try{
					Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + x + " " + y + " 800");
					return;
				}
				catch (IOException localIOException){
					localIOException.printStackTrace();
				}
			}
		});
		butGlisser.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				theCtrl.closePopup();
				clickPanel().setForSwipe(true);
			}
		});
	}

	public ClickPanelCtrl clickPanel(){
		return theCtrl.getService().getClickPanelCtrl();
	}

	public Button getButClic(){
		return butClic;
	}

	public Button getButClicLong(){
		return butClicLong;
	}

	public Button getButGlisser(){
		return butGlisser;
	}

	public PopupCtrl getCtrl(){
		return theCtrl;
	}

	public Point getPos(){
		return theCtrl.getPos();
	}

	public Button getSelected(){
		return selected;
	}

	public void onDraw(Canvas canvas){
		float density = getResources().getDisplayMetrics().density;
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
		theCtrl.start();
		iterations = 0;
		listener();
	}

	public void selectNext(){
		if (iterations == 3){
			clickPanel().closePopupCtrl();
		}
		else{
			Button butClic = (Button)view.findViewById(R.id.but_clic);
			Button butClicLong = (Button)view.findViewById(R.id.but_clic_long);
			Button butGlisser = (Button)view.findViewById(R.id.but_glisser);

			if(selected == butGlisser){
				butClic.getBackground().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
				butClic.setTextColor(Color.BLACK);
				butClicLong.setTextColor(Color.WHITE);
				butGlisser.setTextColor(Color.WHITE);
				butClicLong.getBackground().clearColorFilter();
				butGlisser.getBackground().clearColorFilter();
				selected = butClic;
			}	
			else if(selected == butClic){
				butClic.getBackground().clearColorFilter();
				butClicLong.getBackground().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
				butClicLong.setTextColor(Color.BLACK);
				butClic.setTextColor(Color.WHITE);
				butGlisser.setTextColor(Color.WHITE);
				butGlisser.getBackground().clearColorFilter();
				selected = butClicLong;
			}	
			else if(selected == butClicLong){
				butClic.getBackground().clearColorFilter();
				butClicLong.getBackground().clearColorFilter();
				butGlisser.getBackground().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
				butGlisser.setTextColor(Color.BLACK);
				butClicLong.setTextColor(Color.WHITE);
				butClic.setTextColor(Color.WHITE);
				selected = butGlisser;
				iterations++;
			}	
			popUp.setContentView(view);
		}
	}
}
