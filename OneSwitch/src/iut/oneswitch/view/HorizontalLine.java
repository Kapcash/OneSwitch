package iut.oneswitch.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.View;

public class HorizontalLine extends View {
  Paint paint = new Paint();
  private SharedPreferences sp;
  
  public HorizontalLine(Context paramContext)
  {
    super(paramContext);
	sp = PreferenceManager.getDefaultSharedPreferences(paramContext);

  }
  
  public void onDraw(Canvas paramCanvas)
  {
    this.paint.setColor(sp.getInt("color1", 16711681));

    this.paint.setStrokeWidth(2 * paramCanvas.getHeight());
    paramCanvas.drawLine(0.0F, 0.0F, paramCanvas.getWidth(), 0.0F, this.paint);
  }
}
