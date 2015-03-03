package com.example.oneswitch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.View;

/**
 * Représente une ligne horizontale couvrant la largeur totale de l'écran.
 * @author OneSwitch B
 *
 */
public class HorizontalLine extends View {
    Paint paint = new Paint();

    /**
     * Construit une vue à l'aide du constucteur de la classe View.
     * Pour plus d'informations sur l'affichage de la ligne :
     * @see HorizontalLine#onDraw(Canvas)
     * @param context Le contexte de notre application.
     */
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