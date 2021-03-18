package com.google.ftcresearch.tfod.util;

import android.graphics.Matrix;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ImageUtils {
  private static final String TAG = "ImageUtils";
  
  static {
    System.loadLibrary("image_utils");
  }
  
  private static native void argb8888ToYuv420sp(IntBuffer paramIntBuffer, int[] paramArrayOfint, boolean paramBoolean1, ByteBuffer paramByteBuffer, byte[] paramArrayOfbyte, boolean paramBoolean2, int paramInt1, int paramInt2);
  
  public static native void convertARGB8888ToYUV420SP(int[] paramArrayOfint, byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  public static void convertBuffersARGB8888ToYuv420SP(IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) {
    int[] arrayOfInt;
    boolean bool = paramIntBuffer.isDirect();
    if (paramIntBuffer.hasArray()) {
      arrayOfInt = paramIntBuffer.array();
    } else {
      arrayOfInt = null;
    } 
    if (bool || arrayOfInt != null) {
      byte[] arrayOfByte;
      boolean bool1 = paramByteBuffer.isDirect();
      if (paramByteBuffer.hasArray()) {
        arrayOfByte = paramByteBuffer.array();
      } else {
        arrayOfByte = null;
      } 
      if (bool1 || arrayOfByte != null) {
        argb8888ToYuv420sp(paramIntBuffer, arrayOfInt, bool, paramByteBuffer, arrayOfByte, bool1, paramInt1, paramInt2);
        return;
      } 
      throw new RuntimeException("Output buffer is not direct and doesn't have array!");
    } 
    throw new RuntimeException("Input buffer is not direct and doesn't have array!");
  }
  
  public static void convertBuffersYUV420SPToARGB8888(ByteBuffer paramByteBuffer, IntBuffer paramIntBuffer, int paramInt1, int paramInt2, boolean paramBoolean) {
    byte[] arrayOfByte;
    boolean bool = paramByteBuffer.isDirect();
    if (paramByteBuffer.hasArray()) {
      arrayOfByte = paramByteBuffer.array();
    } else {
      arrayOfByte = null;
    } 
    if (bool || arrayOfByte != null) {
      int[] arrayOfInt;
      boolean bool1 = paramIntBuffer.isDirect();
      if (paramIntBuffer.hasArray()) {
        arrayOfInt = paramIntBuffer.array();
      } else {
        arrayOfInt = null;
      } 
      if (bool1 || arrayOfInt != null) {
        yuv420spToArgb8888(paramByteBuffer, arrayOfByte, bool, paramIntBuffer, arrayOfInt, bool1, paramInt1, paramInt2, paramBoolean);
        return;
      } 
      throw new RuntimeException("Output buffer is not direct and doesn't have array!");
    } 
    throw new RuntimeException("Input buffer is not direct and doesn't have array!");
  }
  
  public static native void convertYUV420SPToARGB8888(byte[] paramArrayOfbyte, int[] paramArrayOfint, int paramInt1, int paramInt2, boolean paramBoolean);
  
  public static void flipMatrixLeftRight(int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt1, int paramInt2) {
    if (paramArrayOfint1 != paramArrayOfint2) {
      int i;
      for (i = 0; i < paramInt2; i++) {
        int j;
        for (j = 0; j < paramInt1; j++) {
          int k = i * paramInt1;
          paramArrayOfint2[paramInt1 - j - 1 + k] = paramArrayOfint1[k + j];
        } 
      } 
      return;
    } 
    throw new IllegalArgumentException("Array A cannot be the same as Array B!");
  }
  
  public static void flipMatrixUpDown(int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt1, int paramInt2) {
    if (paramArrayOfint1 != paramArrayOfint2) {
      int i;
      for (i = 0; i < paramInt2; i++) {
        int j;
        for (j = 0; j < paramInt1; j++)
          paramArrayOfint2[(paramInt2 - i - 1) * paramInt1 + j] = paramArrayOfint1[i * paramInt1 + j]; 
      } 
      return;
    } 
    throw new IllegalArgumentException("Array A cannot be the same as Array B!");
  }
  
  public static Matrix getTransformationMatrix(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean) {
    int i;
    Matrix matrix = new Matrix();
    boolean bool = true;
    if (paramInt5 != 0) {
      if (paramInt5 % 90 != 0)
        Log.w("ImageUtils", String.format("Rotation of %d %% 90 != 0", new Object[] { Integer.valueOf(paramInt5) })); 
      matrix.postTranslate(-paramInt1 / 2.0F, -paramInt2 / 2.0F);
      matrix.postRotate(paramInt5);
    } 
    if ((Math.abs(paramInt5) + 90) % 180 != 0)
      bool = false; 
    if (bool) {
      i = paramInt2;
    } else {
      i = paramInt1;
    } 
    if (!bool)
      paramInt1 = paramInt2; 
    if (i != paramInt3 || paramInt1 != paramInt4) {
      float f1 = paramInt3 / i;
      float f2 = paramInt4 / paramInt1;
      if (paramBoolean) {
        f1 = Math.max(f1, f2);
        matrix.postScale(f1, f1);
      } else {
        matrix.postScale(f1, f2);
      } 
    } 
    if (paramInt5 != 0)
      matrix.postTranslate(paramInt3 / 2.0F, paramInt4 / 2.0F); 
    return matrix;
  }
  
  public static Size rotateMatrix(int[] paramArrayOfint, int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt3 == 90 || paramInt3 == 180 || paramInt3 == 270 || paramInt3 == 0) {
      int[] arrayOfInt = new int[paramInt1 * paramInt2];
      if (paramInt3 != 90) {
        if (paramInt3 != 180) {
          if (paramInt3 == 270) {
            transposeMatrix(paramArrayOfint, arrayOfInt, paramInt1, paramInt2);
            flipMatrixUpDown(arrayOfInt, paramArrayOfint, paramInt2, paramInt1);
          } 
        } else {
          flipMatrixLeftRight(paramArrayOfint, arrayOfInt, paramInt1, paramInt2);
          flipMatrixUpDown(arrayOfInt, paramArrayOfint, paramInt1, paramInt2);
        } 
      } else {
        transposeMatrix(paramArrayOfint, arrayOfInt, paramInt1, paramInt2);
        flipMatrixLeftRight(arrayOfInt, paramArrayOfint, paramInt2, paramInt1);
      } 
      return Size.getRotatedSize(new Size(paramInt1, paramInt2), paramInt3);
    } 
    throw new IllegalArgumentException("Rotation is not a simple rotation (0, 90, 180, 2700! Won't rotate!");
  }
  
  public static Matrix transformBetweenImageSizes(Size paramSize1, Size paramSize2) {
    float f1 = paramSize2.width / paramSize1.width;
    float f2 = paramSize2.height / paramSize1.height;
    Matrix matrix = new Matrix();
    matrix.setScale(f1, f2);
    return matrix;
  }
  
  public static void transposeMatrix(int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt1, int paramInt2) {
    if (paramArrayOfint1 != paramArrayOfint2) {
      int i;
      for (i = 0; i < paramInt2; i++) {
        int j;
        for (j = 0; j < paramInt1; j++)
          paramArrayOfint2[j * paramInt2 + i] = paramArrayOfint1[i * paramInt1 + j]; 
      } 
      return;
    } 
    throw new IllegalArgumentException("Array A cannot be the same as Array B!");
  }
  
  private static native void yuv420spToArgb8888(ByteBuffer paramByteBuffer, byte[] paramArrayOfbyte, boolean paramBoolean1, IntBuffer paramIntBuffer, int[] paramArrayOfint, boolean paramBoolean2, int paramInt1, int paramInt2, boolean paramBoolean3);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\ftcresearch\tfo\\util\ImageUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */