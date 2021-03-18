package org.firstinspires.ftc.robotcore.internal.android.dex;

import org.firstinspires.ftc.robotcore.internal.android.dex.util.ByteInput;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ByteOutput;

public final class Leb128 {
  public static int readSignedLeb128(ByteInput paramByteInput) {
    int k;
    int n;
    int i1;
    int m = 0;
    int i = -1;
    int j = 0;
    while (true) {
      i1 = paramByteInput.readByte() & 0xFF;
      k = m | (i1 & 0x7F) << j * 7;
      n = i << 7;
      int i2 = j + 1;
      i1 &= 0x80;
      if (i1 == 128) {
        m = k;
        j = i2;
        i = n;
        if (i2 >= 5)
          break; 
        continue;
      } 
      break;
    } 
    if (i1 != 128) {
      i = k;
      if ((n >> 1 & k) != 0)
        i = k | n; 
      return i;
    } 
    throw new DexException("invalid LEB128 sequence");
  }
  
  public static int readUnsignedLeb128(ByteInput paramByteInput) {
    int k;
    int m;
    int j = 0;
    int i = 0;
    while (true) {
      m = paramByteInput.readByte() & 0xFF;
      k = j | (m & 0x7F) << i * 7;
      int n = i + 1;
      m &= 0x80;
      if (m == 128) {
        j = k;
        i = n;
        if (n >= 5)
          break; 
        continue;
      } 
      break;
    } 
    if (m != 128)
      return k; 
    throw new DexException("invalid LEB128 sequence");
  }
  
  public static int signedLeb128Size(int paramInt) {
    byte b;
    int k = paramInt >> 7;
    if ((Integer.MIN_VALUE & paramInt) == 0) {
      b = 0;
    } else {
      b = -1;
    } 
    int j = 0;
    int m = 1;
    int i = paramInt;
    paramInt = k;
    while (true) {
      k = paramInt;
      if (m) {
        if (k != b || (k & 0x1) != (i >> 6 & 0x1)) {
          i = 1;
        } else {
          i = 0;
        } 
        paramInt = k >> 7;
        j++;
        m = i;
        i = k;
        continue;
      } 
      return j;
    } 
  }
  
  public static int unsignedLeb128Size(int paramInt) {
    int i = paramInt >> 7;
    for (paramInt = 0; i != 0; paramInt++)
      i >>= 7; 
    return paramInt + 1;
  }
  
  public static void writeSignedLeb128(ByteOutput paramByteOutput, int paramInt) {
    byte b;
    int i = paramInt >> 7;
    if ((Integer.MIN_VALUE & paramInt) == 0) {
      b = 0;
    } else {
      b = -1;
    } 
    int j = 1;
    while (true) {
      int m = paramInt;
      int k = i;
      if (j) {
        if (k != b || (k & 0x1) != (m >> 6 & 0x1)) {
          paramInt = 1;
        } else {
          paramInt = 0;
        } 
        if (paramInt != 0) {
          i = 128;
        } else {
          i = 0;
        } 
        paramByteOutput.writeByte((byte)(m & 0x7F | i));
        i = k >> 7;
        j = paramInt;
        paramInt = k;
        continue;
      } 
      break;
    } 
  }
  
  public static void writeUnsignedLeb128(ByteOutput paramByteOutput, int paramInt) {
    while (true) {
      int i = paramInt >>> 7;
      if (i != 0) {
        paramByteOutput.writeByte((byte)(paramInt & 0x7F | 0x80));
        paramInt = i;
        continue;
      } 
      paramByteOutput.writeByte((byte)(paramInt & 0x7F));
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dex\Leb128.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */