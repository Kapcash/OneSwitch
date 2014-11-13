package com.iut.oneswitch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class VerticalLine extends View{
    Paint paint = new Paint();

    public VerticalLine(Context context) {
        super(context);            
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(canvas.getWidth()*2);
        canvas.drawLine(0, 0, 0, canvas.getHeight(), paint);
    }
}
