package org.java_websocket.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidEncodingException;

public class Charsetfunctions {
  private static final CodingErrorAction codingErrorAction = CodingErrorAction.REPORT;
  
  private static final int[] utf8d = new int[] { 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
      1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 
      9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 
      7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
      7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
      7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
      7, 7, 8, 8, 2, 2, 2, 2, 2, 2, 
      2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
      2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
      2, 2, 2, 2, 10, 3, 3, 3, 3, 3, 
      3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 
      11, 6, 6, 6, 5, 8, 8, 8, 8, 8, 
      8, 8, 8, 8, 8, 8, 0, 1, 2, 3, 
      5, 8, 7, 1, 1, 1, 4, 6, 1, 1, 
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
      1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 
      1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 
      1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 
      1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 
      1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 
      1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 
      1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 
      1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 
      1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 
      1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 
      1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
  
  public static byte[] asciiBytes(String paramString) {
    try {
      return paramString.getBytes("ASCII");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new InvalidEncodingException(unsupportedEncodingException);
    } 
  }
  
  public static boolean isValidUTF8(ByteBuffer paramByteBuffer) {
    return isValidUTF8(paramByteBuffer, 0);
  }
  
  public static boolean isValidUTF8(ByteBuffer paramByteBuffer, int paramInt) {
    int j = paramByteBuffer.remaining();
    if (j < paramInt)
      return false; 
    int i = 0;
    while (paramInt < j) {
      int[] arrayOfInt = utf8d;
      i = arrayOfInt[(i << 4) + 256 + arrayOfInt[paramByteBuffer.get(paramInt) & 0xFF]];
      if (i == 1)
        return false; 
      paramInt++;
    } 
    return true;
  }
  
  public static String stringAscii(byte[] paramArrayOfbyte) {
    return stringAscii(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public static String stringAscii(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    try {
      return new String(paramArrayOfbyte, paramInt1, paramInt2, "ASCII");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new InvalidEncodingException(unsupportedEncodingException);
    } 
  }
  
  public static String stringUtf8(ByteBuffer paramByteBuffer) throws InvalidDataException {
    CharsetDecoder charsetDecoder = Charset.forName("UTF8").newDecoder();
    charsetDecoder.onMalformedInput(codingErrorAction);
    charsetDecoder.onUnmappableCharacter(codingErrorAction);
    try {
      paramByteBuffer.mark();
      String str = charsetDecoder.decode(paramByteBuffer).toString();
      paramByteBuffer.reset();
      return str;
    } catch (CharacterCodingException characterCodingException) {
      throw new InvalidDataException(1007, characterCodingException);
    } 
  }
  
  public static String stringUtf8(byte[] paramArrayOfbyte) throws InvalidDataException {
    return stringUtf8(ByteBuffer.wrap(paramArrayOfbyte));
  }
  
  public static byte[] utf8Bytes(String paramString) {
    try {
      return paramString.getBytes("UTF8");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new InvalidEncodingException(unsupportedEncodingException);
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocke\\util\Charsetfunctions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */