package com.vuforia.ar.pl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import java.nio.ByteBuffer;

public class DrawOverlayView extends View {
  private static final String MODULENAME = "DrawOverlayView";
  
  private Drawable drawable = null;
  
  private double mLeft;
  
  private float[] mScale;
  
  private int[] mSize;
  
  private double mTop;
  
  private DisplayMetrics metrics;
  
  private Bitmap overlayBitmap;
  
  public DrawOverlayView(Context paramContext) {
    super(paramContext);
  }
  
  public DrawOverlayView(Context paramContext, byte[] paramArrayOfbyte, int paramInt1, int paramInt2, float[] paramArrayOffloat, int[] paramArrayOfint) {
    super(paramContext);
    this.mLeft = paramInt1;
    this.mTop = paramInt2;
    this.mScale = paramArrayOffloat;
    this.mSize = paramArrayOfint;
    byte[] arrayOfByte = new byte[paramArrayOfbyte.length * 4];
    paramInt1 = 0;
    while (true) {
      int[] arrayOfInt = this.mSize;
      if (paramInt1 < arrayOfInt[0] * arrayOfInt[1]) {
        paramInt2 = paramInt1 * 4;
        arrayOfByte[paramInt2] = paramArrayOfbyte[paramInt1];
        arrayOfByte[paramInt2 + 1] = paramArrayOfbyte[paramInt1];
        arrayOfByte[paramInt2 + 2] = paramArrayOfbyte[paramInt1];
        arrayOfByte[paramInt2 + 3] = -1;
        paramInt1++;
        continue;
      } 
      Bitmap bitmap = Bitmap.createBitmap(arrayOfInt[0], arrayOfInt[1], Bitmap.Config.ARGB_8888);
      this.overlayBitmap = bitmap;
      bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(arrayOfByte));
      this.drawable = (Drawable)new BitmapDrawable(this.overlayBitmap);
      this.metrics = new DisplayMetrics();
      ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(this.metrics);
      return;
    } 
  }
  
  public void addOverlay(Activity paramActivity) {
    ((ViewGroup)paramActivity.getWindow().getDecorView()).addView(this);
    setVisibility(0);
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.overlayBitmap == null) {
      dispatchDraw(paramCanvas);
      return;
    } 
    double d = this.metrics.heightPixels - (this.drawable.getIntrinsicHeight() * this.mScale[1]);
    if (d < this.mTop)
      this.mTop = d; 
    int i = (int)(this.mLeft + (this.drawable.getIntrinsicWidth() * this.metrics.density * this.mScale[0]));
    int j = (int)(this.mTop + (this.drawable.getIntrinsicHeight() * this.metrics.density * this.mScale[1]));
    this.drawable.setBounds((int)this.mLeft, (int)this.mTop, i, j);
    this.drawable.setAlpha(100);
    this.drawable.draw(paramCanvas);
    dispatchDraw(paramCanvas);
  }
  
  public void removeOverlay(Activity paramActivity, View paramView) {
    try {
      ((ViewGroup)paramActivity.getWindow().getDecorView()).removeView(paramView);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\DrawOverlayView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */