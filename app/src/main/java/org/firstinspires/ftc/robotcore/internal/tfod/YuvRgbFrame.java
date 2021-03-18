package org.firstinspires.ftc.robotcore.internal.tfod;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import com.google.ftcresearch.tfod.util.ImageUtils;
import com.google.ftcresearch.tfod.util.Size;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class YuvRgbFrame {
  private static final String TAG = "YuvRgbFrame";
  
  private static final Paint paint;
  
  private int[] argb8888Array;
  
  private final Object argb8888Lock;
  
  private int argb8888Size;
  
  private Double argb8888ZoomAspectRatio;
  
  private Double argb8888ZoomMagnification;
  
  private final long frameTimeNanos;
  
  private byte[] luminosityArray;
  
  private final Object luminosityLock;
  
  private Size luminositySize;
  
  private final Bitmap rgb565Bitmap;
  
  private final Size size;
  
  private final String tag;
  
  static {
    Paint paint = new Paint();
    paint = paint;
    paint.setColor(-16777216);
    paint.setStyle(Paint.Style.FILL);
  }
  
  YuvRgbFrame(long paramLong, Size paramSize, ByteBuffer paramByteBuffer, ClippingMargins paramClippingMargins) {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial <init> : ()V
    //   4: aload_0
    //   5: new java/lang/Object
    //   8: dup
    //   9: invokespecial <init> : ()V
    //   12: putfield luminosityLock : Ljava/lang/Object;
    //   15: aload_0
    //   16: new java/lang/Object
    //   19: dup
    //   20: invokespecial <init> : ()V
    //   23: putfield argb8888Lock : Ljava/lang/Object;
    //   26: aload_0
    //   27: lload_1
    //   28: putfield frameTimeNanos : J
    //   31: aload_0
    //   32: aload_3
    //   33: putfield size : Lcom/google/ftcresearch/tfod/util/Size;
    //   36: new java/lang/StringBuilder
    //   39: dup
    //   40: invokespecial <init> : ()V
    //   43: astore #6
    //   45: aload #6
    //   47: ldc 'YuvRgbFrame.'
    //   49: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   52: pop
    //   53: aload #6
    //   55: lload_1
    //   56: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   59: pop
    //   60: aload_0
    //   61: aload #6
    //   63: invokevirtual toString : ()Ljava/lang/String;
    //   66: putfield tag : Ljava/lang/String;
    //   69: aload_3
    //   70: getfield width : I
    //   73: aload_3
    //   74: getfield height : I
    //   77: getstatic android/graphics/Bitmap$Config.RGB_565 : Landroid/graphics/Bitmap$Config;
    //   80: invokestatic createBitmap : (IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
    //   83: astore #6
    //   85: aload_0
    //   86: aload #6
    //   88: putfield rgb565Bitmap : Landroid/graphics/Bitmap;
    //   91: aload #6
    //   93: aload #4
    //   95: invokevirtual duplicate : ()Ljava/nio/ByteBuffer;
    //   98: invokevirtual copyPixelsFromBuffer : (Ljava/nio/Buffer;)V
    //   101: aload #5
    //   103: monitorenter
    //   104: aload #5
    //   106: getfield left : I
    //   109: ifgt -> 136
    //   112: aload #5
    //   114: getfield top : I
    //   117: ifgt -> 136
    //   120: aload #5
    //   122: getfield right : I
    //   125: ifgt -> 136
    //   128: aload #5
    //   130: getfield bottom : I
    //   133: ifle -> 200
    //   136: new android/graphics/Canvas
    //   139: dup
    //   140: aload_0
    //   141: getfield rgb565Bitmap : Landroid/graphics/Bitmap;
    //   144: invokespecial <init> : (Landroid/graphics/Bitmap;)V
    //   147: astore #4
    //   149: aload #4
    //   151: aload #5
    //   153: getfield left : I
    //   156: i2f
    //   157: aload #5
    //   159: getfield top : I
    //   162: i2f
    //   163: aload_3
    //   164: getfield width : I
    //   167: aload #5
    //   169: getfield right : I
    //   172: isub
    //   173: i2f
    //   174: aload_3
    //   175: getfield height : I
    //   178: aload #5
    //   180: getfield bottom : I
    //   183: isub
    //   184: i2f
    //   185: getstatic android/graphics/Region$Op.DIFFERENCE : Landroid/graphics/Region$Op;
    //   188: invokevirtual clipRect : (FFFFLandroid/graphics/Region$Op;)Z
    //   191: pop
    //   192: aload #4
    //   194: getstatic org/firstinspires/ftc/robotcore/internal/tfod/YuvRgbFrame.paint : Landroid/graphics/Paint;
    //   197: invokevirtual drawPaint : (Landroid/graphics/Paint;)V
    //   200: aload #5
    //   202: monitorexit
    //   203: return
    //   204: astore_3
    //   205: aload #5
    //   207: monitorexit
    //   208: aload_3
    //   209: athrow
    // Exception table:
    //   from	to	target	type
    //   104	136	204	finally
    //   136	200	204	finally
    //   200	203	204	finally
    //   205	208	204	finally
  }
  
  int[] getArgb8888Array(int paramInt, double paramDouble1, double paramDouble2) {
    synchronized (this.argb8888Lock) {
      if (paramInt == this.argb8888Size && this.argb8888ZoomMagnification != null && Zoom.areEqual(paramDouble1, this.argb8888ZoomMagnification.doubleValue()) && this.argb8888ZoomAspectRatio != null && Zoom.areEqual(paramDouble2, this.argb8888ZoomAspectRatio.doubleValue()))
        return this.argb8888Array; 
      if (this.argb8888Size != 0) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getArgb8888Array called for multiple sizes ");
        stringBuilder.append(this.argb8888Size);
        stringBuilder.append(" and ");
        stringBuilder.append(paramInt);
        Log.w("YuvRgbFrame", stringBuilder.toString());
      } 
      if (this.argb8888ZoomMagnification != null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getArgb8888Array called for multiple zoom magnifications ");
        stringBuilder.append(this.argb8888ZoomMagnification);
        stringBuilder.append(" and ");
        stringBuilder.append(paramDouble1);
        Log.w("YuvRgbFrame", stringBuilder.toString());
      } 
      if (this.argb8888ZoomAspectRatio != null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getArgb8888Array called for multiple zoom aspect ratios ");
        stringBuilder.append(this.argb8888ZoomAspectRatio);
        stringBuilder.append(" and ");
        stringBuilder.append(paramDouble2);
        Log.w("YuvRgbFrame", stringBuilder.toString());
      } 
      Timer timer = new Timer(this.tag);
      timer.start("YuvRgbFrame.getArgb8888Array");
      if (Zoom.isZoomed(paramDouble1)) {
        Rect rect = Zoom.getZoomArea(paramDouble1, paramDouble2, this.size.width, this.size.height);
        bitmap = Bitmap.createBitmap(this.rgb565Bitmap, rect.left, rect.top, rect.width(), rect.height());
      } else {
        bitmap = this.rgb565Bitmap;
      } 
      Bitmap bitmap = Bitmap.createScaledBitmap(bitmap, paramInt, paramInt, false).copy(Bitmap.Config.ARGB_8888, false);
      int[] arrayOfInt = new int[paramInt * 4 * paramInt];
      bitmap.getPixels(arrayOfInt, 0, paramInt, 0, 0, paramInt, paramInt);
      timer.end();
      this.argb8888Array = arrayOfInt;
      this.argb8888Size = paramInt;
      this.argb8888ZoomMagnification = Double.valueOf(paramDouble1);
      this.argb8888ZoomAspectRatio = Double.valueOf(paramDouble2);
      return this.argb8888Array;
    } 
  }
  
  Bitmap getCopiedBitmap() {
    Timer timer = new Timer(this.tag);
    timer.start("YuvRgbFrame.getCopiedBitmap");
    Bitmap bitmap = this.rgb565Bitmap;
    bitmap = bitmap.copy(bitmap.getConfig(), true);
    timer.end();
    return bitmap;
  }
  
  long getFrameTimeNanos() {
    return this.frameTimeNanos;
  }
  
  int getHeight() {
    return this.size.height;
  }
  
  byte[] getLuminosityArray(Size paramSize) {
    synchronized (this.luminosityLock) {
      if (paramSize.equals(this.luminositySize)) {
        arrayOfByte1 = this.luminosityArray;
        return arrayOfByte1;
      } 
      if (this.luminositySize != null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getLuminosityArray called for multiple sizes ");
        stringBuilder.append(this.luminositySize);
        stringBuilder.append(" and ");
        stringBuilder.append(arrayOfByte1);
        Log.w("YuvRgbFrame", stringBuilder.toString());
      } 
      Timer timer = new Timer(this.tag);
      timer.start("YuvRgbFrame.getLuminosityArray");
      Bitmap bitmap = Bitmap.createScaledBitmap(this.rgb565Bitmap, ((Size)arrayOfByte1).width, ((Size)arrayOfByte1).height, false).copy(Bitmap.Config.ARGB_8888, false);
      IntBuffer intBuffer = IntBuffer.allocate(((Size)arrayOfByte1).width * ((Size)arrayOfByte1).height);
      bitmap.copyPixelsToBuffer(intBuffer.duplicate());
      ByteBuffer byteBuffer = ByteBuffer.allocate(((Size)arrayOfByte1).width * 3 * ((Size)arrayOfByte1).height);
      ImageUtils.convertBuffersARGB8888ToYuv420SP(intBuffer, byteBuffer, ((Size)arrayOfByte1).width, ((Size)arrayOfByte1).height);
      byte[] arrayOfByte2 = new byte[((Size)arrayOfByte1).width * ((Size)arrayOfByte1).height];
      this.luminosityArray = arrayOfByte2;
      byteBuffer.get(arrayOfByte2, 0, ((Size)arrayOfByte1).width * ((Size)arrayOfByte1).height);
      this.luminositySize = (Size)arrayOfByte1;
      timer.end();
      byte[] arrayOfByte1 = this.luminosityArray;
      return arrayOfByte1;
    } 
  }
  
  Size getSize() {
    return this.size;
  }
  
  String getTag() {
    return this.tag;
  }
  
  int getWidth() {
    return this.size.width;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\YuvRgbFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */