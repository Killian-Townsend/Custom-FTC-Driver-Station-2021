package org.firstinspires.ftc.robotcore.internal.android.dx.util;

public final class HexParser {
  public static byte[] parse(String paramString) {
    int k = paramString.length();
    int m = k / 2;
    byte[] arrayOfByte2 = new byte[m];
    int i = 0;
    int j = i;
    while (i < k) {
      String str1;
      int n = paramString.indexOf('\n', i);
      int i1 = n;
      if (n < 0)
        i1 = k; 
      n = paramString.indexOf('#', i);
      if (n >= 0 && n < i1) {
        str1 = paramString.substring(i, n);
      } else {
        str1 = paramString.substring(i, i1);
      } 
      i = str1.indexOf(':');
      String str2 = str1;
      if (i != -1) {
        n = str1.indexOf('"');
        if (n != -1 && n < i) {
          str2 = str1;
        } else {
          String str = str1.substring(0, i).trim();
          str2 = str1.substring(i + 1);
          if (Integer.parseInt(str, 16) != j) {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("bogus offset marker: ");
            stringBuilder1.append(str);
            throw new RuntimeException(stringBuilder1.toString());
          } 
        } 
      } 
      int i3 = str2.length();
      int i2 = 0;
      i = i2;
      n = -1;
      while (i2 < i3) {
        char c = str2.charAt(i2);
        if (i != 0) {
          if (c == '"') {
            i = 0;
          } else {
            arrayOfByte2[j] = (byte)c;
            j++;
          } 
        } else if (c > ' ') {
          if (c == '"') {
            if (n == -1) {
              i = 1;
            } else {
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("spare digit around offset ");
              stringBuilder1.append(Hex.u4(j));
              throw new RuntimeException(stringBuilder1.toString());
            } 
          } else {
            int i4 = Character.digit(c, 16);
            if (i4 != -1) {
              if (n == -1) {
                n = i4;
              } else {
                arrayOfByte2[j] = (byte)(n << 4 | i4);
                j++;
                n = -1;
              } 
            } else {
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("bogus digit character: \"");
              stringBuilder1.append(c);
              stringBuilder1.append("\"");
              throw new RuntimeException(stringBuilder1.toString());
            } 
          } 
        } 
        i2++;
      } 
      if (n == -1) {
        if (i == 0) {
          i = i1 + 1;
          continue;
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("unterminated quote around offset ");
        stringBuilder1.append(Hex.u4(j));
        throw new RuntimeException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("spare digit around offset ");
      stringBuilder.append(Hex.u4(j));
      throw new RuntimeException(stringBuilder.toString());
    } 
    byte[] arrayOfByte1 = arrayOfByte2;
    if (j < m) {
      arrayOfByte1 = new byte[j];
      System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, j);
    } 
    return arrayOfByte1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\HexParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */