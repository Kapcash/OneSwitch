package com.example.testos2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();

    public DrawView(Context context) {
        super(context);            
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.CYAN);
        paint.setStrokeWidth(canvas.getHeight()*2);
        canvas.drawLine(0, 0, canvas.getWidth(), 0, paint);
    }

}