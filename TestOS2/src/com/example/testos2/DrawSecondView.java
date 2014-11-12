package com.example.testos2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawSecondView extends View{
    Paint paint = new Paint();

    public DrawSecondView(Context context) {
        super(context);            
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(canvas.getWidth()*2);
        canvas.drawLine(0, 0, 0, canvas.getHeight(), paint);
    }
}
