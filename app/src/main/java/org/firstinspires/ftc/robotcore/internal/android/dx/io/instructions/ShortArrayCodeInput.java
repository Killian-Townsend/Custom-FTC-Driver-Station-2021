package org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions;

import java.io.EOFException;

public final class ShortArrayCodeInput extends BaseCodeCursor implements CodeInput {
  private final short[] array;
  
  public ShortArrayCodeInput(short[] paramArrayOfshort) {
    if (paramArrayOfshort != null) {
      this.array = paramArrayOfshort;
      return;
    } 
    throw new NullPointerException("array == null");
  }
  
  public boolean hasMore() {
    return (cursor() < this.array.length);
  }
  
  public int read() throws EOFException {
    try {
      short s = this.array[cursor()];
      advance(1);
      return s & 0xFFFF;
    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
      throw new EOFException();
    } 
  }
  
  public int readInt() throws EOFException {
    return read() | read() << 16;
  }
  
  public long readLong() throws EOFException {
    return read() | read() << 16L | read() << 32L | read() << 48L;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\instructions\ShortArrayCodeInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */