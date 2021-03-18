package org.firstinspires.ftc.robotcore.internal.tfod;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import java.util.Iterator;
import java.util.Vector;

public class BorderedText {
  private final Paint exteriorPaint;
  
  private final Paint interiorPaint;
  
  private final float textSize;
  
  public BorderedText(float paramFloat) {
    this(-1, -16777216, paramFloat);
  }
  
  public BorderedText(int paramInt1, int paramInt2, float paramFloat) {
    Paint paint = new Paint();
    this.interiorPaint = paint;
    paint.setTextSize(paramFloat);
    this.interiorPaint.setColor(paramInt1);
    this.interiorPaint.setStyle(Paint.Style.FILL);
    this.interiorPaint.setAntiAlias(false);
    this.interiorPaint.setAlpha(255);
    paint = new Paint();
    this.exteriorPaint = paint;
    paint.setTextSize(paramFloat);
    this.exteriorPaint.setColor(paramInt2);
    this.exteriorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    this.exteriorPaint.setStrokeWidth(paramFloat / 8.0F);
    this.exteriorPaint.setAntiAlias(false);
    this.exteriorPaint.setAlpha(255);
    this.textSize = paramFloat;
  }
  
  public void drawLines(Canvas paramCanvas, float paramFloat1, float paramFloat2, Vector<String> paramVector) {
    Iterator<String> iterator = paramVector.iterator();
    int i;
    for (i = 0; iterator.hasNext(); i++) {
      String str = iterator.next();
      drawText(paramCanvas, paramFloat1, paramFloat2 - getTextSize() * (paramVector.size() - i - 1), str);
    } 
  }
  
  public void drawText(Canvas paramCanvas, float paramFloat1, float paramFloat2, String paramString) {
    paramCanvas.drawText(paramString, paramFloat1, paramFloat2, this.exteriorPaint);
    paramCanvas.drawText(paramString, paramFloat1, paramFloat2, this.interiorPaint);
  }
  
  public void getTextBounds(String paramString, int paramInt1, int paramInt2, Rect paramRect) {
    this.interiorPaint.getTextBounds(paramString, paramInt1, paramInt2, paramRect);
  }
  
  public float getTextSize() {
    return this.textSize;
  }
  
  public void setAlpha(int paramInt) {
    this.interiorPaint.setAlpha(paramInt);
    this.exteriorPaint.setAlpha(paramInt);
  }
  
  public void setExteriorColor(int paramInt) {
    this.exteriorPaint.setColor(paramInt);
  }
  
  public void setInteriorColor(int paramInt) {
    this.interiorPaint.setColor(paramInt);
  }
  
  public void setTextAlign(Paint.Align paramAlign) {
    this.interiorPaint.setTextAlign(paramAlign);
    this.exteriorPaint.setTextAlign(paramAlign);
  }
  
  public void setTypeface(Typeface paramTypeface) {
    this.interiorPaint.setTypeface(paramTypeface);
    this.exteriorPaint.setTypeface(paramTypeface);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\BorderedText.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */