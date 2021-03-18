package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class CstString extends TypedConstant {
  public static final CstString EMPTY_STRING = new CstString("");
  
  private final ByteArray bytes;
  
  private final String string;
  
  public CstString(String paramString) {
    if (paramString != null) {
      this.string = paramString.intern();
      this.bytes = new ByteArray(stringToUtf8Bytes(paramString));
      return;
    } 
    throw new NullPointerException("string == null");
  }
  
  public CstString(ByteArray paramByteArray) {
    if (paramByteArray != null) {
      this.bytes = paramByteArray;
      this.string = utf8BytesToString(paramByteArray).intern();
      return;
    } 
    throw new NullPointerException("bytes == null");
  }
  
  public static byte[] stringToUtf8Bytes(String paramString) {
    int k = paramString.length();
    byte[] arrayOfByte2 = new byte[k * 3];
    int j = 0;
    int i = j;
    while (j < k) {
      char c = paramString.charAt(j);
      if (c != '\000' && c < '') {
        arrayOfByte2[i] = (byte)c;
        i++;
      } else if (c < 'ࠀ') {
        arrayOfByte2[i] = (byte)(c >> 6 & 0x1F | 0xC0);
        arrayOfByte2[i + 1] = (byte)(c & 0x3F | 0x80);
        i += 2;
      } else {
        arrayOfByte2[i] = (byte)(c >> 12 & 0xF | 0xE0);
        arrayOfByte2[i + 1] = (byte)(c >> 6 & 0x3F | 0x80);
        arrayOfByte2[i + 2] = (byte)(c & 0x3F | 0x80);
        i += 3;
      } 
      j++;
    } 
    byte[] arrayOfByte1 = new byte[i];
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, i);
    return arrayOfByte1;
  }
  
  private static String throwBadUtf8(int paramInt1, int paramInt2) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("bad utf-8 byte ");
    stringBuilder.append(Hex.u1(paramInt1));
    stringBuilder.append(" at offset ");
    stringBuilder.append(Hex.u4(paramInt2));
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static String utf8BytesToString(ByteArray paramByteArray) {
    int i = paramByteArray.size();
    char[] arrayOfChar = new char[i];
    int k = 0;
    int j = k;
    while (i > 0) {
      char c;
      int n;
      int i1;
      int i2;
      int i3;
      int m = paramByteArray.getUnsignedByte(j);
      switch (m >> 4) {
        default:
          return throwBadUtf8(m, j);
        case 14:
          i -= 3;
          if (i < 0)
            return throwBadUtf8(m, j); 
          i2 = j + 1;
          n = paramByteArray.getUnsignedByte(i2);
          i1 = n & 0xC0;
          if (i1 != 128)
            return throwBadUtf8(n, i2); 
          i2 = j + 2;
          i3 = paramByteArray.getUnsignedByte(i2);
          if (i1 != 128)
            return throwBadUtf8(i3, i2); 
          m = (m & 0xF) << 12 | (n & 0x3F) << 6 | i3 & 0x3F;
          if (m < 2048)
            return throwBadUtf8(i3, i2); 
          c = (char)m;
          j += 3;
          break;
        case 12:
        case 13:
          i -= 2;
          if (i < 0)
            return throwBadUtf8(m, j); 
          n = j + 1;
          i1 = paramByteArray.getUnsignedByte(n);
          if ((i1 & 0xC0) != 128)
            return throwBadUtf8(i1, n); 
          m = (m & 0x1F) << 6 | i1 & 0x3F;
          if (m != 0 && m < 128)
            return throwBadUtf8(i1, n); 
          c = (char)m;
          j += 2;
          break;
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
          i--;
          if (m == 0)
            return throwBadUtf8(m, j); 
          c = (char)m;
          j++;
          break;
      } 
      arrayOfChar[k] = c;
      k++;
    } 
    return new String(arrayOfChar, 0, k);
  }
  
  protected int compareTo0(Constant paramConstant) {
    return this.string.compareTo(((CstString)paramConstant).string);
  }
  
  public boolean equals(Object paramObject) {
    return !(paramObject instanceof CstString) ? false : this.string.equals(((CstString)paramObject).string);
  }
  
  public ByteArray getBytes() {
    return this.bytes;
  }
  
  public String getString() {
    return this.string;
  }
  
  public Type getType() {
    return Type.STRING;
  }
  
  public int getUtf16Size() {
    return this.string.length();
  }
  
  public int getUtf8Size() {
    return this.bytes.size();
  }
  
  public int hashCode() {
    return this.string.hashCode();
  }
  
  public boolean isCategory2() {
    return false;
  }
  
  public String toHuman() {
    int j = this.string.length();
    StringBuilder stringBuilder = new StringBuilder(j * 3 / 2);
    int i;
    for (i = 0; i < j; i++) {
      char c = this.string.charAt(i);
      if (c >= ' ' && c < '') {
        if (c == '\'' || c == '"' || c == '\\')
          stringBuilder.append('\\'); 
        stringBuilder.append(c);
        continue;
      } 
      if (c <= '') {
        if (c != '\t') {
          if (c != '\n') {
            if (c != '\r') {
              byte b;
              Object object;
              if (i < j - 1) {
                b = this.string.charAt(i + 1);
              } else {
                b = 0;
              } 
              if (b >= 48 && b <= 55) {
                b = 1;
              } else {
                b = 0;
              } 
              stringBuilder.append('\\');
              int k = 6;
              while (true) {
                k -= 3;
                object = SYNTHETIC_LOCAL_VARIABLE_6;
              } 
              if (object == null)
                stringBuilder.append('0'); 
              continue;
            } 
            stringBuilder.append("\\r");
            continue;
          } 
          stringBuilder.append("\\n");
          continue;
        } 
        stringBuilder.append("\\t");
        continue;
      } 
      stringBuilder.append("\\u");
      stringBuilder.append(Character.forDigit(c >> 12, 16));
      stringBuilder.append(Character.forDigit(c >> 8 & 0xF, 16));
      stringBuilder.append(Character.forDigit(c >> 4 & 0xF, 16));
      stringBuilder.append(Character.forDigit(c & 0xF, 16));
      continue;
    } 
    return stringBuilder.toString();
  }
  
  public String toQuoted() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('"');
    stringBuilder.append(toHuman());
    stringBuilder.append('"');
    return stringBuilder.toString();
  }
  
  public String toQuoted(int paramInt) {
    String str2;
    String str1 = toHuman();
    if (str1.length() <= paramInt - 2) {
      str2 = "";
    } else {
      str1 = str1.substring(0, paramInt - 5);
      str2 = "...";
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('"');
    stringBuilder.append(str1);
    stringBuilder.append(str2);
    stringBuilder.append('"');
    return stringBuilder.toString();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("string{\"");
    stringBuilder.append(toHuman());
    stringBuilder.append("\"}");
    return stringBuilder.toString();
  }
  
  public String typeName() {
    return "utf8";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */