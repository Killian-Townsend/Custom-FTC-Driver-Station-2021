package org.firstinspires.ftc.robotcore.internal.android.dex;

import org.firstinspires.ftc.robotcore.internal.android.dex.util.ByteInput;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ByteOutput;

public final class EncodedValueCodec {
  public static int readSignedInt(ByteInput paramByteInput, int paramInt) {
    int j = 0;
    for (int i = paramInt; i >= 0; i--)
      j = j >>> 8 | (paramByteInput.readByte() & 0xFF) << 24; 
    return j >> (3 - paramInt) * 8;
  }
  
  public static long readSignedLong(ByteInput paramByteInput, int paramInt) {
    long l = 0L;
    for (int i = paramInt; i >= 0; i--)
      l = l >>> 8L | (paramByteInput.readByte() & 0xFFL) << 56L; 
    return l >> (7 - paramInt) * 8;
  }
  
  public static int readUnsignedInt(ByteInput paramByteInput, int paramInt, boolean paramBoolean) {
    int i = 0;
    boolean bool = false;
    int j = paramInt;
    if (!paramBoolean) {
      i = paramInt;
      j = bool;
      while (i >= 0) {
        j = j >>> 8 | (paramByteInput.readByte() & 0xFF) << 24;
        i--;
      } 
      return j >>> (3 - paramInt) * 8;
    } 
    while (j >= 0) {
      i = (paramByteInput.readByte() & 0xFF) << 24 | i >>> 8;
      j--;
    } 
    return i;
  }
  
  public static long readUnsignedLong(ByteInput paramByteInput, int paramInt, boolean paramBoolean) {
    long l1 = 0L;
    long l2 = l1;
    int i = paramInt;
    if (!paramBoolean) {
      for (i = paramInt; i >= 0; i--)
        l1 = l1 >>> 8L | (paramByteInput.readByte() & 0xFFL) << 56L; 
      return l1 >>> (7 - paramInt) * 8;
    } 
    while (i >= 0) {
      l2 = l2 >>> 8L | (paramByteInput.readByte() & 0xFFL) << 56L;
      i--;
    } 
    return l2;
  }
  
  public static void writeRightZeroExtendedValue(ByteOutput paramByteOutput, int paramInt, long paramLong) {
    int j = 64 - Long.numberOfTrailingZeros(paramLong);
    int i = j;
    if (j == 0)
      i = 1; 
    i = i + 7 >> 3;
    paramLong >>= 64 - i * 8;
    paramByteOutput.writeByte(paramInt | i - 1 << 5);
    for (paramInt = i; paramInt > 0; paramInt--) {
      paramByteOutput.writeByte((byte)(int)paramLong);
      paramLong >>= 8L;
    } 
  }
  
  public static void writeSignedIntegralValue(ByteOutput paramByteOutput, int paramInt, long paramLong) {
    int i = 65 - Long.numberOfLeadingZeros(paramLong >> 63L ^ paramLong) + 7 >> 3;
    paramByteOutput.writeByte(paramInt | i - 1 << 5);
    for (paramInt = i; paramInt > 0; paramInt--) {
      paramByteOutput.writeByte((byte)(int)paramLong);
      paramLong >>= 8L;
    } 
  }
  
  public static void writeUnsignedIntegralValue(ByteOutput paramByteOutput, int paramInt, long paramLong) {
    int j = 64 - Long.numberOfLeadingZeros(paramLong);
    int i = j;
    if (j == 0)
      i = 1; 
    i = i + 7 >> 3;
    paramByteOutput.writeByte(paramInt | i - 1 << 5);
    for (paramInt = i; paramInt > 0; paramInt--) {
      paramByteOutput.writeByte((byte)(int)paramLong);
      paramLong >>= 8L;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dex\EncodedValueCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */