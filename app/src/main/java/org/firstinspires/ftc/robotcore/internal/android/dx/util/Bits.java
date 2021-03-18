package org.firstinspires.ftc.robotcore.internal.android.dx.util;

public final class Bits {
  public static boolean anyInRange(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    paramInt1 = findFirst(paramArrayOfint, paramInt1);
    return (paramInt1 >= 0 && paramInt1 < paramInt2);
  }
  
  public static int bitCount(int[] paramArrayOfint) {
    int k = paramArrayOfint.length;
    int i = 0;
    int j = 0;
    while (i < k) {
      j += Integer.bitCount(paramArrayOfint[i]);
      i++;
    } 
    return j;
  }
  
  public static void clear(int[] paramArrayOfint, int paramInt) {
    int i = paramInt >> 5;
    paramArrayOfint[i] = 1 << (paramInt & 0x1F) & paramArrayOfint[i];
  }
  
  public static int findFirst(int paramInt1, int paramInt2) {
    paramInt2 = Integer.numberOfTrailingZeros(paramInt1 & (1 << paramInt2) - 1);
    paramInt1 = paramInt2;
    if (paramInt2 == 32)
      paramInt1 = -1; 
    return paramInt1;
  }
  
  public static int findFirst(int[] paramArrayOfint, int paramInt) {
    int j = paramArrayOfint.length;
    int i = paramInt & 0x1F;
    for (paramInt >>= 5; paramInt < j; paramInt++) {
      int k = paramArrayOfint[paramInt];
      if (k != 0) {
        i = findFirst(k, i);
        if (i >= 0)
          return (paramInt << 5) + i; 
      } 
      i = 0;
    } 
    return -1;
  }
  
  public static boolean get(int[] paramArrayOfint, int paramInt) {
    return ((paramArrayOfint[paramInt >> 5] & 1 << (paramInt & 0x1F)) != 0);
  }
  
  public static int getMax(int[] paramArrayOfint) {
    return paramArrayOfint.length * 32;
  }
  
  public static boolean isEmpty(int[] paramArrayOfint) {
    int j = paramArrayOfint.length;
    for (int i = 0; i < j; i++) {
      if (paramArrayOfint[i] != 0)
        return false; 
    } 
    return true;
  }
  
  public static int[] makeBitSet(int paramInt) {
    return new int[paramInt + 31 >> 5];
  }
  
  public static void or(int[] paramArrayOfint1, int[] paramArrayOfint2) {
    for (int i = 0; i < paramArrayOfint2.length; i++)
      paramArrayOfint1[i] = paramArrayOfint1[i] | paramArrayOfint2[i]; 
  }
  
  public static void set(int[] paramArrayOfint, int paramInt) {
    int i = paramInt >> 5;
    paramArrayOfint[i] = 1 << (paramInt & 0x1F) | paramArrayOfint[i];
  }
  
  public static void set(int[] paramArrayOfint, int paramInt, boolean paramBoolean) {
    int i = paramInt >> 5;
    paramInt = 1 << (paramInt & 0x1F);
    if (paramBoolean) {
      paramArrayOfint[i] = paramInt | paramArrayOfint[i];
      return;
    } 
    paramArrayOfint[i] = paramInt & paramArrayOfint[i];
  }
  
  public static String toHuman(int[] paramArrayOfint) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('{');
    int j = paramArrayOfint.length;
    int i = 0;
    for (boolean bool = false; i < j * 32; bool = bool1) {
      boolean bool1 = bool;
      if (get(paramArrayOfint, i)) {
        if (bool)
          stringBuilder.append(','); 
        stringBuilder.append(i);
        bool1 = true;
      } 
      i++;
    } 
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\Bits.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */