package iut.oneswitch.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.View;

public class VerticalLine extends View{
	Paint paint = new Paint();
	private SharedPreferences sp;
	private int lineThickness;
	private float density;

	/**
	 * Constructeur de la classe VerticalLine.
	 * Celui-ci utilise le constructeur de la classe View.
	 * Pour plus de précisions sur l'affichage de la ligne :
	 * @see VerticalLine#onDraw(Canvas)
	 * @param context le contexte
	 */
	public VerticalLine(Context context) {
		super(context);
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		density = getResources().getDisplayMetrics().density;
		lineThickness = Integer.parseInt(sp.getString("lign_size","3"));
		lineThickness *= density;
	}

	@Override
	public void onDraw(Canvas canvas) {
		try{
			paint.setColor(sp.getInt("color2", 16711681));

			paint.setStrokeWidth(2*lineThickness);
			canvas.drawLine(0, 0, 0, canvas.getHeight(), paint);
			invalidate();
		}
		catch(Exception e){}
	}
}