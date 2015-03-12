package iut.oneswitch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class HorizontalLine extends View {
  Paint paint = new Paint();
  
  public HorizontalLine(Context paramContext)
  {
    super(paramContext);
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    this.paint.setColor(-16711681);
    this.paint.setStrokeWidth(2 * paramCanvas.getHeight());
    paramCanvas.drawLine(0.0F, 0.0F, paramCanvas.getWidth(), 0.0F, this.paint);
  }
}
