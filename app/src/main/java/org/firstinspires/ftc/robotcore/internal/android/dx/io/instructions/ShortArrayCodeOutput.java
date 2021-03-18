package org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions;

public final class ShortArrayCodeOutput extends BaseCodeCursor implements CodeOutput {
  private final short[] array;
  
  public ShortArrayCodeOutput(int paramInt) {
    if (paramInt >= 0) {
      this.array = new short[paramInt];
      return;
    } 
    throw new IllegalArgumentException("maxSize < 0");
  }
  
  public short[] getArray() {
    int i = cursor();
    short[] arrayOfShort1 = this.array;
    if (i == arrayOfShort1.length)
      return arrayOfShort1; 
    short[] arrayOfShort2 = new short[i];
    System.arraycopy(arrayOfShort1, 0, arrayOfShort2, 0, i);
    return arrayOfShort2;
  }
  
  public void write(short paramShort) {
    this.array[cursor()] = paramShort;
    advance(1);
  }
  
  public void write(short paramShort1, short paramShort2) {
    write(paramShort1);
    write(paramShort2);
  }
  
  public void write(short paramShort1, short paramShort2, short paramShort3) {
    write(paramShort1);
    write(paramShort2);
    write(paramShort3);
  }
  
  public void write(short paramShort1, short paramShort2, short paramShort3, short paramShort4) {
    write(paramShort1);
    write(paramShort2);
    write(paramShort3);
    write(paramShort4);
  }
  
  public void write(short paramShort1, short paramShort2, short paramShort3, short paramShort4, short paramShort5) {
    write(paramShort1);
    write(paramShort2);
    write(paramShort3);
    write(paramShort4);
    write(paramShort5);
  }
  
  public void write(byte[] paramArrayOfbyte) {
    int k = paramArrayOfbyte.length;
    boolean bool = true;
    int j = 0;
    int i = j;
    while (j < k) {
      byte b = paramArrayOfbyte[j];
      if (bool) {
        i = b & 0xFF;
        bool = false;
      } else {
        i = b << 8 | i;
        write((short)i);
        bool = true;
      } 
      j++;
    } 
    if (!bool)
      write((short)i); 
  }
  
  public void write(int[] paramArrayOfint) {
    int j = paramArrayOfint.length;
    for (int i = 0; i < j; i++)
      writeInt(paramArrayOfint[i]); 
  }
  
  public void write(long[] paramArrayOflong) {
    int j = paramArrayOflong.length;
    for (int i = 0; i < j; i++)
      writeLong(paramArrayOflong[i]); 
  }
  
  public void write(short[] paramArrayOfshort) {
    int j = paramArrayOfshort.length;
    for (int i = 0; i < j; i++)
      write(paramArrayOfshort[i]); 
  }
  
  public void writeInt(int paramInt) {
    write((short)paramInt);
    write((short)(paramInt >> 16));
  }
  
  public void writeLong(long paramLong) {
    write((short)(int)paramLong);
    write((short)(int)(paramLong >> 16L));
    write((short)(int)(paramLong >> 32L));
    write((short)(int)(paramLong >> 48L));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\instructions\ShortArrayCodeOutput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */