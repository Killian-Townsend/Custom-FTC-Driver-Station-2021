package org.firstinspires.ftc.robotcore.internal.android.dex;

import java.io.UTFDataFormatException;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ByteInput;

public final class Mutf8 {
  private static long countBytes(String paramString, boolean paramBoolean) throws UTFDataFormatException {
    int j = paramString.length();
    long l = 0L;
    int i = 0;
    while (i < j) {
      long l1;
      char c = paramString.charAt(i);
      if (c != '\000' && c <= '') {
        l1 = 1L;
      } else if (c <= '߿') {
        l1 = 2L;
      } else {
        l1 = 3L;
      } 
      l += l1;
      if (!paramBoolean || l <= 65535L) {
        i++;
        continue;
      } 
      throw new UTFDataFormatException("String more than 65535 UTF bytes long");
    } 
    return l;
  }
  
  public static String decode(ByteInput paramByteInput, char[] paramArrayOfchar) throws UTFDataFormatException {
    int i = 0;
    while (true) {
      char c = (char)(paramByteInput.readByte() & 0xFF);
      if (c == '\000')
        return new String(paramArrayOfchar, 0, i); 
      paramArrayOfchar[i] = c;
      if (c < '') {
        i++;
        continue;
      } 
      if ((c & 0xE0) == 192) {
        int j = paramByteInput.readByte() & 0xFF;
        if ((j & 0xC0) == 128) {
          int k = i + 1;
          paramArrayOfchar[i] = (char)((c & 0x1F) << 6 | j & 0x3F);
          i = k;
          continue;
        } 
        throw new UTFDataFormatException("bad second byte");
      } 
      if ((c & 0xF0) == 224) {
        int j = paramByteInput.readByte() & 0xFF;
        int k = paramByteInput.readByte() & 0xFF;
        if ((j & 0xC0) == 128) {
          if ((k & 0xC0) == 128) {
            int m = i + 1;
            paramArrayOfchar[i] = (char)((c & 0xF) << 12 | (j & 0x3F) << 6 | k & 0x3F);
            i = m;
            continue;
          } 
          throw new UTFDataFormatException("bad second or third byte");
        } 
        continue;
      } 
      throw new UTFDataFormatException("bad byte");
    } 
  }
  
  public static void encode(byte[] paramArrayOfbyte, int paramInt, String paramString) {
    int j = paramString.length();
    for (int i = 0; i < j; i++) {
      char c = paramString.charAt(i);
      if (c != '\000' && c <= '') {
        int k = paramInt + 1;
        paramArrayOfbyte[paramInt] = (byte)c;
        paramInt = k;
      } else if (c <= '߿') {
        int k = paramInt + 1;
        paramArrayOfbyte[paramInt] = (byte)(c >> 6 & 0x1F | 0xC0);
        paramInt = k + 1;
        paramArrayOfbyte[k] = (byte)(c & 0x3F | 0x80);
      } else {
        int k = paramInt + 1;
        paramArrayOfbyte[paramInt] = (byte)(c >> 12 & 0xF | 0xE0);
        int m = k + 1;
        paramArrayOfbyte[k] = (byte)(c >> 6 & 0x3F | 0x80);
        paramInt = m + 1;
        paramArrayOfbyte[m] = (byte)(c & 0x3F | 0x80);
      } 
    } 
  }
  
  public static byte[] encode(String paramString) throws UTFDataFormatException {
    byte[] arrayOfByte = new byte[(int)countBytes(paramString, true)];
    encode(arrayOfByte, 0, paramString);
    return arrayOfByte;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dex\Mutf8.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */