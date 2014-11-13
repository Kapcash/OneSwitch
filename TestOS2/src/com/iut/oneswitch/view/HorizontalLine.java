package com.iut.oneswitch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class HorizontalLine extends View {
    Paint paint = new Paint();

    public HorizontalLine(Context context) {
        super(context);            
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.CYAN);
        paint.setStrokeWidth(canvas.getHeight()*2);
        canvas.drawLine(0, 0, canvas.getWidth(), 0, paint);
    }

}