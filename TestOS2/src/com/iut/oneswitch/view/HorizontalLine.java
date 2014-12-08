package com.iut.oneswitch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
/**
 * Représente une ligne horizontale faisant la largeur de l'écran
 * @author OneSwitch B
 *
 */
public class HorizontalLine extends View {
    Paint paint = new Paint();

    /**
     * Constructeur par défaut
     * @param context le contexte
     */
    public HorizontalLine(Context context) {
        super(context);            
    }

    /**
     * Dessine le composant qui remplira la largeur de son parent (canvas)
     */
    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.CYAN);
        paint.setStrokeWidth(canvas.getHeight()*2);
        canvas.drawLine(0, 0, canvas.getWidth(), 0, paint);
    }

}