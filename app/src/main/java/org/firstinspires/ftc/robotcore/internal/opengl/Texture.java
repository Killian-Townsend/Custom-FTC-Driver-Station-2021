package org.firstinspires.ftc.robotcore.internal.opengl;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Texture {
  private static final String LOGTAG = "Vuforia_Texture";
  
  public int mChannels;
  
  public ByteBuffer mData;
  
  public int mHeight;
  
  public boolean mSuccess = false;
  
  public int[] mTextureID = new int[1];
  
  public int mWidth;
  
  public static Texture loadTextureFromApk(String paramString, AssetManager paramAssetManager) {
    try {
      Bitmap bitmap = BitmapFactory.decodeStream(new BufferedInputStream(paramAssetManager.open(paramString, 3)));
      int[] arrayOfInt = new int[bitmap.getWidth() * bitmap.getHeight()];
      bitmap.getPixels(arrayOfInt, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
      return loadTextureFromIntBuffer(arrayOfInt, bitmap.getWidth(), bitmap.getHeight());
    } catch (IOException iOException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Failed to log texture '");
      stringBuilder.append(paramString);
      stringBuilder.append("' from APK");
      Log.e("Vuforia_Texture", stringBuilder.toString());
      Log.i("Vuforia_Texture", iOException.getMessage());
      return null;
    } 
  }
  
  public static Texture loadTextureFromIntBuffer(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    int j = paramInt1 * paramInt2;
    int k = j * 4;
    byte[] arrayOfByte = new byte[k];
    boolean bool = false;
    int i;
    for (i = 0; i < j; i++) {
      int m = paramArrayOfint[i];
      int n = i * 4;
      arrayOfByte[n] = (byte)(m >>> 16);
      arrayOfByte[n + 1] = (byte)(m >>> 8);
      arrayOfByte[n + 2] = (byte)m;
      arrayOfByte[n + 3] = (byte)(m >>> 24);
    } 
    Texture texture = new Texture();
    texture.mWidth = paramInt1;
    texture.mHeight = paramInt2;
    texture.mChannels = 4;
    texture.mData = ByteBuffer.allocateDirect(k).order(ByteOrder.nativeOrder());
    paramInt2 = texture.mWidth * texture.mChannels;
    paramInt1 = bool;
    while (true) {
      i = texture.mHeight;
      if (paramInt1 < i) {
        texture.mData.put(arrayOfByte, (i - 1 - paramInt1) * paramInt2, paramInt2);
        paramInt1++;
        continue;
      } 
      texture.mData.rewind();
      texture.mSuccess = true;
      return texture;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\Texture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */