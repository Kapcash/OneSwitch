package com.iut.oneswitch.view.popup;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.View;

public class PopUpView extends View{
	private ArrayList<PopUpPage> pages;

	public PopUpView(Context context) {
		super(context);
		pages = new ArrayList<PopUpPage>();
	}

	public void addPage(PopUpPage page){
		pages.add(page);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		System.out.println("drawing popup view");
		//canvas.drawARGB(128, 0, 0, 0);
		
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
		
		Paint paintRect = new Paint();
		paintRect.setColor(Color.BLACK);
		paintRect.setAlpha(150);
		paintRect.setStrokeWidth(5);
		paintRect.setStyle(Paint.Style.FILL);
		paintRect.setAntiAlias(true);


		float center_x, center_y;
		center_x = canvas.getWidth()/4;
		center_y = canvas.getHeight()/4;

		final RectF rect = new RectF();
		

		rect.set(12+28,0,canvas.getWidth(),canvas.getHeight());

		canvas.drawRoundRect(rect, 12, 12, paintRect);
		
		
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		paint.setStrokeWidth(2);
		paint.setColor(android.graphics.Color.BLACK);     
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setAlpha(150);
		paint.setAntiAlias(true);

		
		
		Point point1_draw = new Point(11+28,(canvas.getHeight()/2)-8);        
		Point point2_draw = new Point(11+28,(canvas.getHeight()/2)+8);    
		Point point3_draw = new Point(1+28,(canvas.getHeight()/2));


		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(point1_draw.x,point1_draw.y);
		path.lineTo(point2_draw.x,point2_draw.y);
		path.lineTo(point3_draw.x,point3_draw.y);
		path.close();

		canvas.drawPath(path, paint);
		
		//testing
		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		textPaint.setTextSize(20);
		textPaint.setColor(android.graphics.Color.WHITE);     
		textPaint.setAntiAlias(true);
		
		for(PopUpPage p : pages){
			int index = 0;
			for(PopUpItem i : p.getItems()){
				canvas.drawText(i.getName(), 20, 20+20*index, textPaint);
				index++;
			}
		}
		
		

	}

}
