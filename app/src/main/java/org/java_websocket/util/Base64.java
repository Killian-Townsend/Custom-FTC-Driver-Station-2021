package org.java_websocket.util;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Base64 {
  public static final int DO_BREAK_LINES = 8;
  
  public static final int ENCODE = 1;
  
  private static final byte EQUALS_SIGN = 61;
  
  public static final int GZIP = 2;
  
  private static final int MAX_LINE_LENGTH = 76;
  
  private static final byte NEW_LINE = 10;
  
  public static final int NO_OPTIONS = 0;
  
  public static final int ORDERED = 32;
  
  private static final String PREFERRED_ENCODING = "US-ASCII";
  
  public static final int URL_SAFE = 16;
  
  private static final byte WHITE_SPACE_ENC = -5;
  
  private static final byte[] _ORDERED_ALPHABET;
  
  private static final byte[] _ORDERED_DECODABET;
  
  private static final byte[] _STANDARD_ALPHABET = new byte[] { 
      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
      56, 57, 43, 47 };
  
  private static final byte[] _STANDARD_DECODABET = new byte[] { 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, 
      -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 
      54, 55, 56, 57, 58, 59, 60, 61, -9, -9, 
      -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 
      5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 
      15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 
      25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 
      29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 
      39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 
      49, 50, 51, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9 };
  
  private static final byte[] _URL_SAFE_ALPHABET = new byte[] { 
      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
      56, 57, 45, 95 };
  
  private static final byte[] _URL_SAFE_DECODABET = new byte[] { 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, 
      -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 
      54, 55, 56, 57, 58, 59, 60, 61, -9, -9, 
      -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 
      5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 
      15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 
      25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 
      29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 
      39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 
      49, 50, 51, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9 };
  
  static {
    _ORDERED_ALPHABET = new byte[] { 
        45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 
        57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 
        74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 
        84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 
        99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 
        109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 
        119, 120, 121, 122 };
    _ORDERED_DECODABET = new byte[] { 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, 
        -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 
        3, 4, 5, 6, 7, 8, 9, 10, -9, -9, 
        -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 
        16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 
        26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 
        36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 
        41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 
        51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 
        61, 62, 63, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9 };
  }
  
  private static int decode4to3(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2, int paramInt3) {
    if (paramArrayOfbyte1 != null) {
      if (paramArrayOfbyte2 != null) {
        if (paramInt1 >= 0) {
          int i = paramInt1 + 3;
          if (i < paramArrayOfbyte1.length) {
            if (paramInt2 >= 0) {
              int j = paramInt2 + 2;
              if (j < paramArrayOfbyte2.length) {
                byte[] arrayOfByte = getDecodabet(paramInt3);
                paramInt3 = paramInt1 + 2;
                if (paramArrayOfbyte1[paramInt3] == 61) {
                  paramInt3 = arrayOfByte[paramArrayOfbyte1[paramInt1]];
                  paramArrayOfbyte2[paramInt2] = (byte)(((arrayOfByte[paramArrayOfbyte1[paramInt1 + 1]] & 0xFF) << 12 | (paramInt3 & 0xFF) << 18) >>> 16);
                  return 1;
                } 
                if (paramArrayOfbyte1[i] == 61) {
                  j = arrayOfByte[paramArrayOfbyte1[paramInt1]];
                  paramInt1 = arrayOfByte[paramArrayOfbyte1[paramInt1 + 1]];
                  paramInt1 = (arrayOfByte[paramArrayOfbyte1[paramInt3]] & 0xFF) << 6 | (paramInt1 & 0xFF) << 12 | (j & 0xFF) << 18;
                  paramArrayOfbyte2[paramInt2] = (byte)(paramInt1 >>> 16);
                  paramArrayOfbyte2[paramInt2 + 1] = (byte)(paramInt1 >>> 8);
                  return 2;
                } 
                byte b = arrayOfByte[paramArrayOfbyte1[paramInt1]];
                paramInt1 = arrayOfByte[paramArrayOfbyte1[paramInt1 + 1]];
                paramInt3 = arrayOfByte[paramArrayOfbyte1[paramInt3]];
                paramInt1 = arrayOfByte[paramArrayOfbyte1[i]] & 0xFF | (paramInt1 & 0xFF) << 12 | (b & 0xFF) << 18 | (paramInt3 & 0xFF) << 6;
                paramArrayOfbyte2[paramInt2] = (byte)(paramInt1 >> 16);
                paramArrayOfbyte2[paramInt2 + 1] = (byte)(paramInt1 >> 8);
                paramArrayOfbyte2[j] = (byte)paramInt1;
                return 3;
              } 
            } 
            throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", new Object[] { Integer.valueOf(paramArrayOfbyte2.length), Integer.valueOf(paramInt2) }));
          } 
        } 
        throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", new Object[] { Integer.valueOf(paramArrayOfbyte1.length), Integer.valueOf(paramInt1) }));
      } 
      throw new IllegalArgumentException("Destination array was null.");
    } 
    throw new IllegalArgumentException("Source array was null.");
  }
  
  private static byte[] encode3to4(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte[] paramArrayOfbyte2, int paramInt3, int paramInt4) {
    byte b;
    byte[] arrayOfByte = getAlphabet(paramInt4);
    int i = 0;
    if (paramInt2 > 0) {
      paramInt4 = paramArrayOfbyte1[paramInt1] << 24 >>> 8;
    } else {
      paramInt4 = 0;
    } 
    if (paramInt2 > 1) {
      b = paramArrayOfbyte1[paramInt1 + 1] << 24 >>> 16;
    } else {
      b = 0;
    } 
    if (paramInt2 > 2)
      i = paramArrayOfbyte1[paramInt1 + 2] << 24 >>> 24; 
    paramInt1 = paramInt4 | b | i;
    if (paramInt2 != 1) {
      if (paramInt2 != 2) {
        if (paramInt2 != 3)
          return paramArrayOfbyte2; 
        paramArrayOfbyte2[paramInt3] = arrayOfByte[paramInt1 >>> 18];
        paramArrayOfbyte2[paramInt3 + 1] = arrayOfByte[paramInt1 >>> 12 & 0x3F];
        paramArrayOfbyte2[paramInt3 + 2] = arrayOfByte[paramInt1 >>> 6 & 0x3F];
        paramArrayOfbyte2[paramInt3 + 3] = arrayOfByte[paramInt1 & 0x3F];
        return paramArrayOfbyte2;
      } 
      paramArrayOfbyte2[paramInt3] = arrayOfByte[paramInt1 >>> 18];
      paramArrayOfbyte2[paramInt3 + 1] = arrayOfByte[paramInt1 >>> 12 & 0x3F];
      paramArrayOfbyte2[paramInt3 + 2] = arrayOfByte[paramInt1 >>> 6 & 0x3F];
      paramArrayOfbyte2[paramInt3 + 3] = 61;
      return paramArrayOfbyte2;
    } 
    paramArrayOfbyte2[paramInt3] = arrayOfByte[paramInt1 >>> 18];
    paramArrayOfbyte2[paramInt3 + 1] = arrayOfByte[paramInt1 >>> 12 & 0x3F];
    paramArrayOfbyte2[paramInt3 + 2] = 61;
    paramArrayOfbyte2[paramInt3 + 3] = 61;
    return paramArrayOfbyte2;
  }
  
  private static byte[] encode3to4(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt1, int paramInt2) {
    encode3to4(paramArrayOfbyte2, 0, paramInt1, paramArrayOfbyte1, 0, paramInt2);
    return paramArrayOfbyte1;
  }
  
  public static String encodeBytes(byte[] paramArrayOfbyte) {
    try {
      return encodeBytes(paramArrayOfbyte, 0, paramArrayOfbyte.length, 0);
    } catch (IOException iOException) {
      return null;
    } 
  }
  
  public static String encodeBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) throws IOException {
    paramArrayOfbyte = encodeBytesToBytes(paramArrayOfbyte, paramInt1, paramInt2, paramInt3);
    try {
      return new String(paramArrayOfbyte, "US-ASCII");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      return new String(paramArrayOfbyte);
    } 
  }
  
  public static byte[] encodeBytesToBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) throws IOException {
    if (paramArrayOfbyte != null) {
      if (paramInt1 >= 0) {
        if (paramInt2 >= 0) {
          byte[] arrayOfByte;
          if (paramInt1 + paramInt2 <= paramArrayOfbyte.length) {
            Exception exception;
            boolean bool;
            if ((paramInt3 & 0x2) != 0) {
              byte[] arrayOfByte2;
              OutputStream outputStream2;
              byte[] arrayOfByte3;
              byte[] arrayOfByte4;
              Exception exception1 = null;
              OutputStream outputStream1 = null;
              try {
                ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                try {
                  OutputStream outputStream;
                } catch (IOException iOException) {
                
                } finally {
                  Exception exception2;
                  paramArrayOfbyte = null;
                  outputStream1 = null;
                } 
              } catch (IOException iOException) {
              
              } finally {
                Exception exception2;
                paramArrayOfbyte = null;
                outputStream2 = null;
                outputStream1 = outputStream2;
              } 
              try {
                throw outputStream2;
              } finally {
                exception1 = null;
                outputStream2 = outputStream1;
                arrayOfByte4 = paramArrayOfbyte;
                arrayOfByte2 = arrayOfByte3;
              } 
              if (arrayOfByte4 != null)
                try {
                  arrayOfByte4.close();
                } catch (Exception exception2) {} 
              if (arrayOfByte2 != null)
                try {
                  arrayOfByte2.close();
                } catch (Exception exception2) {} 
              if (outputStream2 != null)
                try {
                  outputStream2.close();
                } catch (Exception exception2) {} 
              throw exception;
            } 
            if ((paramInt3 & 0x8) != 0) {
              bool = true;
            } else {
              bool = false;
            } 
            int j = paramInt2 / 3;
            if (paramInt2 % 3 > 0) {
              i = 4;
            } else {
              i = 0;
            } 
            int i = j * 4 + i;
            int k = i;
            if (bool)
              k = i + i / 76; 
            byte[] arrayOfByte1 = new byte[k];
            int m = 0;
            i = 0;
            j = 0;
            while (m < paramInt2 - 2) {
              encode3to4((byte[])exception, m + paramInt1, 3, arrayOfByte1, i, paramInt3);
              j += 4;
              if (bool && j >= 76) {
                arrayOfByte1[i + 4] = 10;
                i++;
                j = 0;
              } 
              m += 3;
              i += 4;
            } 
            j = i;
            if (m < paramInt2) {
              encode3to4((byte[])exception, m + paramInt1, paramInt2 - m, arrayOfByte1, i, paramInt3);
              j = i + 4;
            } 
            if (j <= k - 1) {
              arrayOfByte = new byte[j];
              System.arraycopy(arrayOfByte1, 0, arrayOfByte, 0, j);
              return arrayOfByte;
            } 
            return arrayOfByte1;
          } 
          throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(arrayOfByte.length) }));
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Cannot have length offset: ");
        stringBuilder1.append(paramInt2);
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot have negative offset: ");
      stringBuilder.append(paramInt1);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("Cannot serialize a null array.");
  }
  
  private static final byte[] getAlphabet(int paramInt) {
    return ((paramInt & 0x10) == 16) ? _URL_SAFE_ALPHABET : (((paramInt & 0x20) == 32) ? _ORDERED_ALPHABET : _STANDARD_ALPHABET);
  }
  
  private static final byte[] getDecodabet(int paramInt) {
    return ((paramInt & 0x10) == 16) ? _URL_SAFE_DECODABET : (((paramInt & 0x20) == 32) ? _ORDERED_DECODABET : _STANDARD_DECODABET);
  }
  
  public static class OutputStream extends FilterOutputStream {
    private byte[] b4;
    
    private boolean breakLines;
    
    private byte[] buffer;
    
    private int bufferLength;
    
    private byte[] decodabet;
    
    private boolean encode;
    
    private int lineLength;
    
    private int options;
    
    private int position;
    
    private boolean suspendEncoding;
    
    public OutputStream(java.io.OutputStream param1OutputStream) {
      this(param1OutputStream, 1);
    }
    
    public OutputStream(java.io.OutputStream param1OutputStream, int param1Int) {
      super(param1OutputStream);
      byte b;
      boolean bool1;
      boolean bool2 = true;
      if ((param1Int & 0x8) != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.breakLines = bool1;
      if ((param1Int & 0x1) != 0) {
        bool1 = bool2;
      } else {
        bool1 = false;
      } 
      this.encode = bool1;
      if (bool1) {
        b = 3;
      } else {
        b = 4;
      } 
      this.bufferLength = b;
      this.buffer = new byte[b];
      this.position = 0;
      this.lineLength = 0;
      this.suspendEncoding = false;
      this.b4 = new byte[4];
      this.options = param1Int;
      this.decodabet = Base64.getDecodabet(param1Int);
    }
    
    public void close() throws IOException {
      flushBase64();
      super.close();
      this.buffer = null;
      this.out = null;
    }
    
    public void flushBase64() throws IOException {
      if (this.position > 0) {
        if (this.encode) {
          this.out.write(Base64.encode3to4(this.b4, this.buffer, this.position, this.options));
          this.position = 0;
          return;
        } 
        throw new IOException("Base64 input not properly padded.");
      } 
    }
    
    public void write(int param1Int) throws IOException {
      if (this.suspendEncoding) {
        this.out.write(param1Int);
        return;
      } 
      if (this.encode) {
        byte[] arrayOfByte = this.buffer;
        int i = this.position;
        int j = i + 1;
        this.position = j;
        arrayOfByte[i] = (byte)param1Int;
        if (j >= this.bufferLength) {
          this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength, this.options));
          param1Int = this.lineLength + 4;
          this.lineLength = param1Int;
          if (this.breakLines && param1Int >= 76) {
            this.out.write(10);
            this.lineLength = 0;
          } 
          this.position = 0;
          return;
        } 
      } else {
        byte[] arrayOfByte = this.decodabet;
        int i = param1Int & 0x7F;
        if (arrayOfByte[i] > -5) {
          arrayOfByte = this.buffer;
          i = this.position;
          int j = i + 1;
          this.position = j;
          arrayOfByte[i] = (byte)param1Int;
          if (j >= this.bufferLength) {
            param1Int = Base64.decode4to3(arrayOfByte, 0, this.b4, 0, this.options);
            this.out.write(this.b4, 0, param1Int);
            this.position = 0;
            return;
          } 
        } else if (arrayOfByte[i] != -5) {
          throw new IOException("Invalid character in Base64 data.");
        } 
      } 
    }
    
    public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      if (this.suspendEncoding) {
        this.out.write(param1ArrayOfbyte, param1Int1, param1Int2);
        return;
      } 
      int i;
      for (i = 0; i < param1Int2; i++)
        write(param1ArrayOfbyte[param1Int1 + i]); 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocke\\util\Base64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */