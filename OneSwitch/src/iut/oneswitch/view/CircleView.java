package iut.oneswitch.view;

import iut.oneswitch.R;
import iut.oneswitch.control.PopupCtrl;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Cette classe permet l'impl�mentation d'un cercle dans une vue afin de distinguer clairement une zone de clic.
 * @author OneSwitch B
 *
 */
public class CircleView extends View{
	
	/**
	 * Constructeur de CircleView. Nous faisons appel au constructeur de la classe View.
	 * Pour plus d'informations sur l'affichage du cercle :
	 * @see CircleView#onDraw(Canvas)
	 * @param context Le contexte de l'application.
	 * @param ctrl Le contr�leur d'une popUp.
	 */
	public CircleView(Context context, PopupCtrl ctrl) {
		super(context);
	}

	@Override
	public void onDraw(Canvas canvas) {
		float density = getResources().getDisplayMetrics().density;

		System.out.println("Drawing Popup View");

		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		inflater.inflate(R.layout.popup,null);

		Paint paintCircle = new Paint();
		paintCircle.setColor(Color.BLACK);
		paintCircle.setAlpha(255);
		paintCircle.setStrokeWidth((int)(2*density));
		paintCircle.setStyle(Paint.Style.STROKE);
		paintCircle.setAntiAlias(true);

		canvas.drawCircle(14*density, (canvas.getHeight()/2), 12*density, paintCircle);
		canvas.drawCircle(14*density, (canvas.getHeight()/2), 1*density, paintCircle);

		paintCircle.setAlpha(64);
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(14*density, (canvas.getHeight()/2), 12*density, paintCircle);
	}
}