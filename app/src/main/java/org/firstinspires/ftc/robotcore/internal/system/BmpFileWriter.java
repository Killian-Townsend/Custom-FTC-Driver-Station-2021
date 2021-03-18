package org.firstinspires.ftc.robotcore.internal.system;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.qualcomm.robotcore.util.ClassUtil;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class BmpFileWriter {
  protected static final int BI_BITFIELDS = 3;
  
  protected static boolean useNativeCopyPixels = true;
  
  protected final Bitmap bitmap;
  
  protected final int cbDibHeader;
  
  protected final int cbFile;
  
  protected final int cbFileHeader;
  
  protected final int cbImage;
  
  protected final int cbPadding;
  
  protected final int cbPerPixel;
  
  protected final int cbPerRow;
  
  protected final int cbRowMultiple;
  
  protected final int dibImageData;
  
  protected final int height;
  
  protected final byte[] rgbPadding;
  
  protected final int width;
  
  static {
    System.loadLibrary("RobotCore");
  }
  
  public BmpFileWriter(Bitmap paramBitmap) {
    if (paramBitmap.getConfig() == Bitmap.Config.ARGB_8888) {
      this.bitmap = paramBitmap;
      this.cbRowMultiple = 4;
      this.cbPerPixel = 4;
      this.width = paramBitmap.getWidth();
      this.height = paramBitmap.getHeight();
      int i = this.cbPerPixel * this.width;
      this.cbPerRow = i;
      int j = this.cbRowMultiple;
      if (i % j == 0) {
        i = 0;
      } else {
        i = j - i % j;
      } 
      this.cbPadding = i;
      byte[] arrayOfByte = new byte[i];
      this.rgbPadding = arrayOfByte;
      i = (this.cbPerRow + arrayOfByte.length) * this.height;
      this.cbImage = i;
      this.cbFileHeader = 14;
      this.cbDibHeader = 56;
      j = 14 + 56;
      this.dibImageData = j;
      this.cbFile = j + i;
      return;
    } 
    throw new IllegalArgumentException("unsupported bitmap format");
  }
  
  protected static byte[] getByteMask(int paramInt) {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[paramInt] = -1;
    return arrayOfByte;
  }
  
  protected static native void nativeCopyPixelsRGBA(int paramInt1, int paramInt2, int paramInt3, Bitmap paramBitmap, long paramLong, int paramInt4);
  
  protected byte[] getAlphaMask() {
    return getByteMask(3);
  }
  
  protected byte[] getBlueMask() {
    return getByteMask(2);
  }
  
  protected byte[] getGreenMask() {
    return getByteMask(1);
  }
  
  protected byte[] getRedMask() {
    return getByteMask(0);
  }
  
  public int getSize() {
    return this.cbFile;
  }
  
  public void save(File paramFile) throws IOException {
    RandomAccessFile randomAccessFile = new RandomAccessFile(paramFile, "rw");
    randomAccessFile.setLength(this.cbFile);
    FileChannel fileChannel = randomAccessFile.getChannel();
    save(fileChannel.map(FileChannel.MapMode.READ_WRITE, 0L, this.cbFile));
    fileChannel.close();
    randomAccessFile.close();
  }
  
  public void save(MappedByteBuffer paramMappedByteBuffer) {
    boolean bool;
    paramMappedByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    paramMappedByteBuffer.put((byte)66);
    paramMappedByteBuffer.put((byte)77);
    paramMappedByteBuffer.putInt(this.cbFile);
    paramMappedByteBuffer.putShort((short)0);
    paramMappedByteBuffer.putShort((short)0);
    paramMappedByteBuffer.putInt(this.dibImageData);
    if (paramMappedByteBuffer.position() == this.cbFileHeader) {
      bool = true;
    } else {
      bool = false;
    } 
    Assert.assertTrue(bool);
    paramMappedByteBuffer.putInt(this.cbDibHeader);
    paramMappedByteBuffer.putInt(this.width);
    paramMappedByteBuffer.putInt(this.height);
    paramMappedByteBuffer.putShort((short)1);
    paramMappedByteBuffer.putShort((short)(this.cbPerPixel * 8));
    paramMappedByteBuffer.putInt(3);
    paramMappedByteBuffer.putInt(this.cbImage);
    paramMappedByteBuffer.putInt(0);
    paramMappedByteBuffer.putInt(0);
    paramMappedByteBuffer.putInt(0);
    paramMappedByteBuffer.putInt(0);
    paramMappedByteBuffer.put(getRedMask());
    paramMappedByteBuffer.put(getGreenMask());
    paramMappedByteBuffer.put(getBlueMask());
    paramMappedByteBuffer.put(getAlphaMask());
    if (paramMappedByteBuffer.position() == this.dibImageData) {
      bool = true;
    } else {
      bool = false;
    } 
    Assert.assertTrue(bool);
    if (!useNativeCopyPixels) {
      int i = this.width;
      int j = this.height;
      int[] arrayOfInt = new int[i * j];
      this.bitmap.getPixels(arrayOfInt, 0, i, 0, 0, i, j);
      for (i = this.height - 1; i >= 0; i--) {
        j = this.width * i;
        int k = 0;
        while (k < this.width) {
          int m = arrayOfInt[j];
          paramMappedByteBuffer.put((byte)Color.red(m));
          paramMappedByteBuffer.put((byte)Color.green(m));
          paramMappedByteBuffer.put((byte)Color.blue(m));
          paramMappedByteBuffer.put((byte)Color.alpha(m));
          k++;
          j++;
        } 
        paramMappedByteBuffer.put(this.rgbPadding);
      } 
    } else {
      long l = ClassUtil.memoryAddressFrom(paramMappedByteBuffer);
      int i = paramMappedByteBuffer.position();
      nativeCopyPixelsRGBA(this.width, this.height, this.cbPadding, this.bitmap, l, i);
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\BmpFileWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */