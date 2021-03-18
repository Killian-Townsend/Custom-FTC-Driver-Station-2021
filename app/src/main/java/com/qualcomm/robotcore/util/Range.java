package com.qualcomm.robotcore.util;

public class Range {
  public static byte clip(byte paramByte1, byte paramByte2, byte paramByte3) {
    return (paramByte1 < paramByte2) ? paramByte2 : ((paramByte1 > paramByte3) ? paramByte3 : paramByte1);
  }
  
  public static double clip(double paramDouble1, double paramDouble2, double paramDouble3) {
    return (paramDouble1 < paramDouble2) ? paramDouble2 : ((paramDouble1 > paramDouble3) ? paramDouble3 : paramDouble1);
  }
  
  public static float clip(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (paramFloat1 < paramFloat2) ? paramFloat2 : ((paramFloat1 > paramFloat3) ? paramFloat3 : paramFloat1);
  }
  
  public static int clip(int paramInt1, int paramInt2, int paramInt3) {
    return (paramInt1 < paramInt2) ? paramInt2 : ((paramInt1 > paramInt3) ? paramInt3 : paramInt1);
  }
  
  public static short clip(short paramShort1, short paramShort2, short paramShort3) {
    return (paramShort1 < paramShort2) ? paramShort2 : ((paramShort1 > paramShort3) ? paramShort3 : paramShort1);
  }
  
  public static double scale(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
    paramDouble5 = paramDouble4 - paramDouble5;
    paramDouble3 = paramDouble2 - paramDouble3;
    return paramDouble5 / paramDouble3 * paramDouble1 + paramDouble4 - paramDouble2 * paramDouble5 / paramDouble3;
  }
  
  public static void throwIfRangeIsInvalid(double paramDouble1, double paramDouble2, double paramDouble3) throws IllegalArgumentException {
    if (paramDouble1 >= paramDouble2 && paramDouble1 <= paramDouble3)
      return; 
    throw new IllegalArgumentException(String.format("number %f is invalid; valid ranges are %f..%f", new Object[] { Double.valueOf(paramDouble1), Double.valueOf(paramDouble2), Double.valueOf(paramDouble3) }));
  }
  
  public static void throwIfRangeIsInvalid(int paramInt1, int paramInt2, int paramInt3) throws IllegalArgumentException {
    if (paramInt1 >= paramInt2 && paramInt1 <= paramInt3)
      return; 
    throw new IllegalArgumentException(String.format("number %d is invalid; valid ranges are %d..%d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) }));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\Range.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */