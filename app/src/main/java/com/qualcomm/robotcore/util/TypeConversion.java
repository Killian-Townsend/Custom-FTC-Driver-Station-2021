package com.qualcomm.robotcore.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class TypeConversion {
  private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
  
  public static int byteArrayToInt(byte[] paramArrayOfbyte) {
    return byteArrayToInt(paramArrayOfbyte, ByteOrder.BIG_ENDIAN);
  }
  
  public static int byteArrayToInt(byte[] paramArrayOfbyte, ByteOrder paramByteOrder) {
    return ByteBuffer.wrap(paramArrayOfbyte).order(paramByteOrder).getInt();
  }
  
  public static long byteArrayToLong(byte[] paramArrayOfbyte) {
    return byteArrayToLong(paramArrayOfbyte, ByteOrder.BIG_ENDIAN);
  }
  
  public static long byteArrayToLong(byte[] paramArrayOfbyte, ByteOrder paramByteOrder) {
    return ByteBuffer.wrap(paramArrayOfbyte).order(paramByteOrder).getLong();
  }
  
  public static short byteArrayToShort(byte[] paramArrayOfbyte) {
    return byteArrayToShort(paramArrayOfbyte, ByteOrder.BIG_ENDIAN);
  }
  
  public static short byteArrayToShort(byte[] paramArrayOfbyte, int paramInt, ByteOrder paramByteOrder) {
    return ByteBuffer.wrap(paramArrayOfbyte, paramInt, paramArrayOfbyte.length - paramInt).order(paramByteOrder).getShort();
  }
  
  public static short byteArrayToShort(byte[] paramArrayOfbyte, ByteOrder paramByteOrder) {
    return ByteBuffer.wrap(paramArrayOfbyte).order(paramByteOrder).getShort();
  }
  
  public static double doubleFromFixed(int paramInt1, int paramInt2) {
    return paramInt1 / power2(paramInt2);
  }
  
  public static double doubleFromFixed(long paramLong, int paramInt) {
    return paramLong / power2(paramInt);
  }
  
  public static int doubleToFixedInt(double paramDouble, int paramInt) {
    return (int)Math.round(paramDouble * power2(paramInt));
  }
  
  public static long doubleToFixedLong(double paramDouble, int paramInt) {
    return Math.round(paramDouble * power2(paramInt));
  }
  
  public static byte[] intToByteArray(int paramInt) {
    return intToByteArray(paramInt, ByteOrder.BIG_ENDIAN);
  }
  
  public static byte[] intToByteArray(int paramInt, ByteOrder paramByteOrder) {
    return ByteBuffer.allocate(4).order(paramByteOrder).putInt(paramInt).array();
  }
  
  public static byte[] longToByteArray(long paramLong) {
    return longToByteArray(paramLong, ByteOrder.BIG_ENDIAN);
  }
  
  public static byte[] longToByteArray(long paramLong, ByteOrder paramByteOrder) {
    return ByteBuffer.allocate(8).order(paramByteOrder).putLong(paramLong).array();
  }
  
  private static double power2(int paramInt) {
    if (paramInt != 20) {
      if (paramInt != 24) {
        if (paramInt != 32) {
          if (paramInt != 64) {
            double d = 2.0D;
            switch (paramInt) {
              default:
                return Math.pow(2.0D, paramInt);
              case 16:
                return 65536.0D;
              case 15:
                return 32768.0D;
              case 14:
                return 16384.0D;
              case 13:
                return 8192.0D;
              case 12:
                return 4096.0D;
              case 11:
                return 2048.0D;
              case 10:
                return 1024.0D;
              case 9:
                return 512.0D;
              case 8:
                return 256.0D;
              case 7:
                return 128.0D;
              case 6:
                return 64.0D;
              case 5:
                return 32.0D;
              case 4:
                return 16.0D;
              case 3:
                return 8.0D;
              case 2:
                d = 4.0D;
              case 1:
                return d;
              case 0:
                break;
            } 
            return 1.0D;
          } 
          return 1.8446744073709552E19D;
        } 
        return 4.294967296E9D;
      } 
      return 1.6777216E7D;
    } 
    return 1048576.0D;
  }
  
  public static byte[] shortToByteArray(short paramShort) {
    return shortToByteArray(paramShort, ByteOrder.BIG_ENDIAN);
  }
  
  public static byte[] shortToByteArray(short paramShort, ByteOrder paramByteOrder) {
    return ByteBuffer.allocate(2).order(paramByteOrder).putShort(paramShort).array();
  }
  
  public static byte[] stringToUtf8(String paramString) {
    byte[] arrayOfByte = paramString.getBytes(UTF8_CHARSET);
    if (paramString.equals(new String(arrayOfByte, UTF8_CHARSET)))
      return arrayOfByte; 
    throw new IllegalArgumentException(String.format("string cannot be cleanly encoded into %s - '%s' -> '%s'", new Object[] { UTF8_CHARSET.name(), paramString, new String(arrayOfByte, UTF8_CHARSET) }));
  }
  
  public static boolean toBoolean(Boolean paramBoolean) {
    return toBoolean(paramBoolean, false);
  }
  
  public static boolean toBoolean(Boolean paramBoolean, boolean paramBoolean1) {
    if (paramBoolean != null)
      paramBoolean1 = paramBoolean.booleanValue(); 
    return paramBoolean1;
  }
  
  public static double unsignedByteToDouble(byte paramByte) {
    return (paramByte & 0xFF);
  }
  
  public static int unsignedByteToInt(byte paramByte) {
    return paramByte & 0xFF;
  }
  
  public static long unsignedIntToLong(int paramInt) {
    return paramInt & 0xFFFFFFFFL;
  }
  
  public static int unsignedShortToInt(short paramShort) {
    return paramShort & 0xFFFF;
  }
  
  public static String utf8ToString(byte[] paramArrayOfbyte) {
    return new String(paramArrayOfbyte, UTF8_CHARSET);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\TypeConversion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */