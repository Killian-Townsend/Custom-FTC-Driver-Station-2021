package org.firstinspires.ftc.robotcore.internal.ui;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

public class FilledPolygonDrawable extends PaintedPathDrawable {
  protected int numSides;
  
  public FilledPolygonDrawable(int paramInt1, int paramInt2) {
    super(paramInt1);
    this.paint.setStyle(Paint.Style.FILL);
    this.numSides = paramInt2;
  }
  
  protected Path computePath(double paramDouble, int paramInt1, int paramInt2) {
    double d1 = 6.283185307179586D / this.numSides;
    paramDouble /= 2.0D;
    Path path = new Path();
    double d2 = paramInt1;
    float f = (float)(Math.cos(0.0D) * paramDouble + d2);
    double d3 = paramInt2;
    path.moveTo(f, (float)(Math.sin(0.0D) * paramDouble + d3));
    for (paramInt1 = 1; paramInt1 < this.numSides; paramInt1++) {
      double d = paramInt1 * d1;
      path.lineTo((float)(Math.cos(d) * paramDouble + d2), (float)(Math.sin(d) * paramDouble + d3));
    } 
    path.close();
    return path;
  }
  
  protected void computePath(Rect paramRect) {
    this.path = new Path();
    this.path.setFillType(Path.FillType.EVEN_ODD);
    this.path.addPath(computePath(Math.min(paramRect.width(), paramRect.height()), paramRect.centerX(), paramRect.centerY()));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\ui\FilledPolygonDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */