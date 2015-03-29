package iut.oneswitch.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * Classe contenant la vue d'une barre horizontal.
 * @author OneSwitch B
 *
 */
public class HorizontalLine extends View {
	Paint paint = new Paint();
	private SharedPreferences sp;
	private int lineThickness;
	private float density;

	/**
	 * Constructeur de la classe
	 * @param paramContext
	 */
	public HorizontalLine(Context paramContext){
		super(paramContext);
		sp = PreferenceManager.getDefaultSharedPreferences(paramContext);
		density = getResources().getDisplayMetrics().density;
		lineThickness = Integer.parseInt(sp.getString("lign_size","3"));
		lineThickness *= density;

	}

	/**
	 * Méthode qui permet de dessiner la barre horizontal.
	 */
	public void onDraw(Canvas paramCanvas){
		try{
			this.paint.setColor(sp.getInt("color1", 16711681));

			this.paint.setStrokeWidth(2*lineThickness);
			paramCanvas.drawLine(0, 0, paramCanvas.getWidth(), 0, this.paint);
			invalidate();
		}
		catch(Exception e){}
	}



}
