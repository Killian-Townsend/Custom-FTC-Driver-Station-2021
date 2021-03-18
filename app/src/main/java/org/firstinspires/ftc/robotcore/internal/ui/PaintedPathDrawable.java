package org.firstinspires.ftc.robotcore.internal.ui;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public abstract class PaintedPathDrawable extends Drawable {
  protected Paint paint;
  
  protected Path path;
  
  protected PaintedPathDrawable(int paramInt) {
    Paint paint = new Paint();
    this.paint = paint;
    paint.setAntiAlias(true);
    this.paint.setColor(paramInt);
  }
  
  protected abstract void computePath(Rect paramRect);
  
  public void draw(Canvas paramCanvas) {
    paramCanvas.drawPath(this.path, this.paint);
  }
  
  public int getAlpha() {
    return this.paint.getAlpha();
  }
  
  public ColorFilter getColorFilter() {
    return this.paint.getColorFilter();
  }
  
  public int getOpacity() {
    return -3;
  }
  
  protected void onBoundsChange(Rect paramRect) {
    super.onBoundsChange(paramRect);
    computePath(paramRect);
    invalidateSelf();
  }
  
  public void setAlpha(int paramInt) {
    this.paint.setAlpha(paramInt);
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.paint.setColorFilter(paramColorFilter);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\ui\PaintedPathDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */