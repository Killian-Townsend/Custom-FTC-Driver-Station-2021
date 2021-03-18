package org.firstinspires.ftc.driverstation.internal;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import org.firstinspires.ftc.robotcore.internal.ui.PaintedPathDrawable;

public class StopWatchDrawable extends PaintedPathDrawable {
  final float bigDiam = 67.0F;
  
  final float bigHand = 20.1F;
  
  final float height = 100.5F;
  
  final float littleHand = 13.400001F;
  
  final float smallDiam = 12.5F;
  
  final float stem = 15.0F;
  
  final float stroke = 6.0F;
  
  final float width = 73.0F;
  
  public StopWatchDrawable() {
    this(-16711936);
  }
  
  public StopWatchDrawable(int paramInt) {
    super(paramInt);
    this.paint.setStyle(Paint.Style.STROKE);
    this.paint.setStrokeCap(Paint.Cap.ROUND);
  }
  
  protected void computePath(Rect paramRect) {
    float f1 = Math.min(paramRect.width() / 73.0F, paramRect.height() / 100.5F);
    this.paint.setStrokeWidth(6.0F * f1);
    this.path = new Path();
    float f2 = 3.0F * f1;
    RectF rectF2 = new RectF();
    rectF2.set(paramRect.left + f2, paramRect.bottom - 67.0F * f1, paramRect.right - f2, paramRect.bottom - f2);
    this.path.addOval(rectF2, Path.Direction.CCW);
    f2 = paramRect.exactCenterX();
    float f3 = rectF2.top - 15.0F * f1;
    this.path.moveTo(f2, rectF2.top);
    this.path.lineTo(f2, f3);
    RectF rectF1 = new RectF();
    float f4 = 6.25F * f1;
    rectF1.set(f2 - f4, f3 - 12.5F * f1, f2 + f4, f3);
    this.path.addOval(rectF1, Path.Direction.CCW);
    this.path.moveTo(rectF2.centerX(), rectF2.centerY());
    this.path.rLineTo(0.0F, -20.1F * f1);
    this.path.moveTo(rectF2.centerX(), rectF2.centerY());
    this.path.rLineTo(f1 * 13.400001F, 0.0F);
  }
  
  public int getIntrinsicHeight() {
    return Math.round(100.5F);
  }
  
  public int getIntrinsicWidth() {
    return Math.round(73.0F);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\driverstation\internal\StopWatchDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */