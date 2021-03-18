package org.firstinspires.ftc.robotcore.internal.android.dx.util;

public final class Hex {
  public static String dump(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    int i = paramInt1 + paramInt2;
    if ((paramInt1 | paramInt2 | i) >= 0 && i <= paramArrayOfbyte.length) {
      if (paramInt3 >= 0) {
        if (paramInt2 == 0)
          return ""; 
        StringBuffer stringBuffer = new StringBuffer(paramInt2 * 4 + 6);
        int j = 0;
        i = paramInt3;
        paramInt3 = paramInt1;
        paramInt1 = j;
        while (paramInt2 > 0) {
          if (paramInt1 == 0) {
            String str;
            if (paramInt5 != 2) {
              if (paramInt5 != 4) {
                if (paramInt5 != 6) {
                  str = u4(i);
                } else {
                  str = u3(i);
                } 
              } else {
                str = u2(i);
              } 
            } else {
              str = u1(i);
            } 
            stringBuffer.append(str);
            stringBuffer.append(": ");
          } else if ((paramInt1 & 0x1) == 0) {
            stringBuffer.append(' ');
          } 
          stringBuffer.append(u1(paramArrayOfbyte[paramInt3]));
          i++;
          paramInt3++;
          j = paramInt1 + 1;
          paramInt1 = j;
          if (j == paramInt4) {
            stringBuffer.append('\n');
            paramInt1 = 0;
          } 
          paramInt2--;
        } 
        if (paramInt1 != 0)
          stringBuffer.append('\n'); 
        return stringBuffer.toString();
      } 
      throw new IllegalArgumentException("outOffset < 0");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("arr.length ");
    stringBuilder.append(paramArrayOfbyte.length);
    stringBuilder.append("; ");
    stringBuilder.append(paramInt1);
    stringBuilder.append("..!");
    stringBuilder.append(i);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public static String s1(int paramInt) {
    char[] arrayOfChar = new char[3];
    int i = 0;
    if (paramInt < 0) {
      arrayOfChar[0] = '-';
      paramInt = -paramInt;
    } else {
      arrayOfChar[0] = '+';
    } 
    while (i < 2) {
      arrayOfChar[2 - i] = Character.forDigit(paramInt & 0xF, 16);
      paramInt >>= 4;
      i++;
    } 
    return new String(arrayOfChar);
  }
  
  public static String s2(int paramInt) {
    char[] arrayOfChar = new char[5];
    int i = 0;
    if (paramInt < 0) {
      arrayOfChar[0] = '-';
      paramInt = -paramInt;
    } else {
      arrayOfChar[0] = '+';
    } 
    while (i < 4) {
      arrayOfChar[4 - i] = Character.forDigit(paramInt & 0xF, 16);
      paramInt >>= 4;
      i++;
    } 
    return new String(arrayOfChar);
  }
  
  public static String s4(int paramInt) {
    char[] arrayOfChar = new char[9];
    int i = 0;
    if (paramInt < 0) {
      arrayOfChar[0] = '-';
      paramInt = -paramInt;
    } else {
      arrayOfChar[0] = '+';
    } 
    while (i < 8) {
      arrayOfChar[8 - i] = Character.forDigit(paramInt & 0xF, 16);
      paramInt >>= 4;
      i++;
    } 
    return new String(arrayOfChar);
  }
  
  public static String s8(long paramLong) {
    char[] arrayOfChar = new char[17];
    int i = 0;
    if (paramLong < 0L) {
      arrayOfChar[0] = '-';
      paramLong = -paramLong;
    } else {
      arrayOfChar[0] = '+';
    } 
    while (i < 16) {
      arrayOfChar[16 - i] = Character.forDigit((int)paramLong & 0xF, 16);
      paramLong >>= 4L;
      i++;
    } 
    return new String(arrayOfChar);
  }
  
  public static String u1(int paramInt) {
    char[] arrayOfChar = new char[2];
    boolean bool = false;
    int i = paramInt;
    for (paramInt = bool; paramInt < 2; paramInt++) {
      arrayOfChar[1 - paramInt] = Character.forDigit(i & 0xF, 16);
      i >>= 4;
    } 
    return new String(arrayOfChar);
  }
  
  public static String u2(int paramInt) {
    char[] arrayOfChar = new char[4];
    boolean bool = false;
    int i = paramInt;
    for (paramInt = bool; paramInt < 4; paramInt++) {
      arrayOfChar[3 - paramInt] = Character.forDigit(i & 0xF, 16);
      i >>= 4;
    } 
    return new String(arrayOfChar);
  }
  
  public static String u2or4(int paramInt) {
    return (paramInt == (char)paramInt) ? u2(paramInt) : u4(paramInt);
  }
  
  public static String u3(int paramInt) {
    char[] arrayOfChar = new char[6];
    boolean bool = false;
    int i = paramInt;
    for (paramInt = bool; paramInt < 6; paramInt++) {
      arrayOfChar[5 - paramInt] = Character.forDigit(i & 0xF, 16);
      i >>= 4;
    } 
    return new String(arrayOfChar);
  }
  
  public static String u4(int paramInt) {
    char[] arrayOfChar = new char[8];
    boolean bool = false;
    int i = paramInt;
    for (paramInt = bool; paramInt < 8; paramInt++) {
      arrayOfChar[7 - paramInt] = Character.forDigit(i & 0xF, 16);
      i >>= 4;
    } 
    return new String(arrayOfChar);
  }
  
  public static String u8(long paramLong) {
    char[] arrayOfChar = new char[16];
    for (int i = 0; i < 16; i++) {
      arrayOfChar[15 - i] = Character.forDigit((int)paramLong & 0xF, 16);
      paramLong >>= 4L;
    } 
    return new String(arrayOfChar);
  }
  
  public static String uNibble(int paramInt) {
    return new String(new char[] { Character.forDigit(paramInt & 0xF, 16) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\Hex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */