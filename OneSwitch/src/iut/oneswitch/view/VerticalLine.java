package iut.oneswitch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class VerticalLine extends View{
    Paint paint = new Paint();

    /**
     * Constructeur de la classe VerticalLine.
     * Celui-ci utilise le constructeur de la classe View.
     * Pour plus de précisions sur l'affichage de la ligne :
     * @see VerticalLine#onDraw(Canvas)
     * @param context le contexte
     */
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