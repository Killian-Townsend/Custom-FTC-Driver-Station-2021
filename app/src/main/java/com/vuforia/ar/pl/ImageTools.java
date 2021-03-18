package com.vuforia.ar.pl;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.YuvImage;
import java.io.ByteArrayOutputStream;

public class ImageTools {
  private static final int CAMERA_IMAGE_FORMAT_LUM = 268439809;
  
  private static final int CAMERA_IMAGE_FORMAT_NV12 = 268439815;
  
  private static final int CAMERA_IMAGE_FORMAT_NV21 = 268439817;
  
  private static final int CAMERA_IMAGE_FORMAT_RGB565 = 268439810;
  
  private static final String MODULENAME = "ImageTools";
  
  public static byte[] encodeImage(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    YuvImage yuvImage;
    if (paramArrayOfbyte == null)
      return null; 
    paramInt4 = 0;
    if (paramInt3 == 268439817) {
      yuvImage = new YuvImage(paramArrayOfbyte, 17, paramInt1, paramInt2, null);
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      return yuvImage.compressToJpeg(new Rect(0, 0, paramInt1, paramInt2), paramInt5, byteArrayOutputStream) ? byteArrayOutputStream.toByteArray() : null;
    } 
    if (paramInt3 == 268439809) {
      int i = paramInt1 * paramInt2;
      int[] arrayOfInt = new int[i];
      for (paramInt3 = paramInt4; paramInt3 < i; paramInt3++)
        arrayOfInt[paramInt3] = yuvImage[paramInt3] << 24 | 0xFFFFFF; 
      Bitmap bitmap = Bitmap.createBitmap(arrayOfInt, 0, paramInt1, paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      if (bitmap.compress(Bitmap.CompressFormat.JPEG, paramInt5, byteArrayOutputStream))
        return byteArrayOutputStream.toByteArray(); 
    } 
    return null;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\ImageTools.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */