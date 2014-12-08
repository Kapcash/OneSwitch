package com.iut.oneswitch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
/**
 * Représente une ligne verticale faisant la hauteur de l'écran
 * @author OneSwitch B
 *
 */
public class VerticalLine extends View{
    Paint paint = new Paint();

    /**
     * Constructeur par défaut
     * @param context le contexte
     */
    public VerticalLine(Context context) {
        super(context);            
    }

    /**
     * Dessine le composant qui remplira la largeur de son parent (canvas)
     */
    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(canvas.getWidth()*2);
        canvas.drawLine(0, 0, 0, canvas.getHeight(), paint);
    }
}
