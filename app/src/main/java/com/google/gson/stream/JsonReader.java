package com.google.gson.stream;

import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class JsonReader implements Closeable {
  private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
  
  private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
  
  private static final int NUMBER_CHAR_DECIMAL = 3;
  
  private static final int NUMBER_CHAR_DIGIT = 2;
  
  private static final int NUMBER_CHAR_EXP_DIGIT = 7;
  
  private static final int NUMBER_CHAR_EXP_E = 5;
  
  private static final int NUMBER_CHAR_EXP_SIGN = 6;
  
  private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
  
  private static final int NUMBER_CHAR_NONE = 0;
  
  private static final int NUMBER_CHAR_SIGN = 1;
  
  private static final int PEEKED_BEGIN_ARRAY = 3;
  
  private static final int PEEKED_BEGIN_OBJECT = 1;
  
  private static final int PEEKED_BUFFERED = 11;
  
  private static final int PEEKED_DOUBLE_QUOTED = 9;
  
  private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
  
  private static final int PEEKED_END_ARRAY = 4;
  
  private static final int PEEKED_END_OBJECT = 2;
  
  private static final int PEEKED_EOF = 17;
  
  private static final int PEEKED_FALSE = 6;
  
  private static final int PEEKED_LONG = 15;
  
  private static final int PEEKED_NONE = 0;
  
  private static final int PEEKED_NULL = 7;
  
  private static final int PEEKED_NUMBER = 16;
  
  private static final int PEEKED_SINGLE_QUOTED = 8;
  
  private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
  
  private static final int PEEKED_TRUE = 5;
  
  private static final int PEEKED_UNQUOTED = 10;
  
  private static final int PEEKED_UNQUOTED_NAME = 14;
  
  private final char[] buffer = new char[1024];
  
  private final Reader in;
  
  private boolean lenient = false;
  
  private int limit = 0;
  
  private int lineNumber = 0;
  
  private int lineStart = 0;
  
  private int[] pathIndices;
  
  private String[] pathNames;
  
  int peeked = 0;
  
  private long peekedLong;
  
  private int peekedNumberLength;
  
  private String peekedString;
  
  private int pos = 0;
  
  private int[] stack;
  
  private int stackSize;
  
  static {
    JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() {
        public void promoteNameToValue(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader instanceof JsonTreeReader) {
            ((JsonTreeReader)param1JsonReader).promoteNameToValue();
            return;
          } 
          int j = param1JsonReader.peeked;
          int i = j;
          if (j == 0)
            i = param1JsonReader.doPeek(); 
          if (i == 13) {
            param1JsonReader.peeked = 9;
            return;
          } 
          if (i == 12) {
            param1JsonReader.peeked = 8;
            return;
          } 
          if (i == 14) {
            param1JsonReader.peeked = 10;
            return;
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Expected a name but was ");
          stringBuilder.append(param1JsonReader.peek());
          stringBuilder.append(param1JsonReader.locationString());
          throw new IllegalStateException(stringBuilder.toString());
        }
      };
  }
  
  public JsonReader(Reader paramReader) {
    int[] arrayOfInt = new int[32];
    this.stack = arrayOfInt;
    this.stackSize = 0;
    this.stackSize = 0 + 1;
    arrayOfInt[0] = 6;
    this.pathNames = new String[32];
    this.pathIndices = new int[32];
    if (paramReader != null) {
      this.in = paramReader;
      return;
    } 
    throw new NullPointerException("in == null");
  }
  
  private void checkLenient() throws IOException {
    if (this.lenient)
      return; 
    throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
  }
  
  private void consumeNonExecutePrefix() throws IOException {
    nextNonWhitespace(true);
    int i = this.pos - 1;
    this.pos = i;
    char[] arrayOfChar = NON_EXECUTE_PREFIX;
    if (i + arrayOfChar.length > this.limit && !fillBuffer(arrayOfChar.length))
      return; 
    i = 0;
    while (true) {
      arrayOfChar = NON_EXECUTE_PREFIX;
      if (i < arrayOfChar.length) {
        if (this.buffer[this.pos + i] != arrayOfChar[i])
          return; 
        i++;
        continue;
      } 
      this.pos += arrayOfChar.length;
      return;
    } 
  }
  
  private boolean fillBuffer(int paramInt) throws IOException {
    char[] arrayOfChar = this.buffer;
    int j = this.lineStart;
    int i = this.pos;
    this.lineStart = j - i;
    j = this.limit;
    if (j != i) {
      j -= i;
      this.limit = j;
      System.arraycopy(arrayOfChar, i, arrayOfChar, 0, j);
    } else {
      this.limit = 0;
    } 
    this.pos = 0;
    while (true) {
      Reader reader = this.in;
      i = this.limit;
      i = reader.read(arrayOfChar, i, arrayOfChar.length - i);
      if (i != -1) {
        j = this.limit + i;
        this.limit = j;
        i = paramInt;
        if (this.lineNumber == 0) {
          int k = this.lineStart;
          i = paramInt;
          if (k == 0) {
            i = paramInt;
            if (j > 0) {
              i = paramInt;
              if (arrayOfChar[0] == '﻿') {
                this.pos++;
                this.lineStart = k + 1;
                i = paramInt + 1;
              } 
            } 
          } 
        } 
        paramInt = i;
        if (this.limit >= i)
          return true; 
        continue;
      } 
      return false;
    } 
  }
  
  private boolean isLiteral(char paramChar) throws IOException {
    if (paramChar != '\t' && paramChar != '\n' && paramChar != '\f' && paramChar != '\r' && paramChar != ' ')
      if (paramChar != '#') {
        if (paramChar != ',')
          if (paramChar != '/' && paramChar != '=') {
            if (paramChar != '{' && paramChar != '}' && paramChar != ':')
              if (paramChar != ';') {
                switch (paramChar) {
                  default:
                    return true;
                  case '\\':
                    checkLenient();
                    break;
                  case '[':
                  case ']':
                    break;
                } 
                return false;
              }  
            return false;
          }  
        return false;
      }  
    return false;
  }
  
  private String locationString() {
    int i = this.lineNumber;
    int j = this.pos;
    int k = this.lineStart;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(" at line ");
    stringBuilder.append(i + 1);
    stringBuilder.append(" column ");
    stringBuilder.append(j - k + 1);
    stringBuilder.append(" path ");
    stringBuilder.append(getPath());
    return stringBuilder.toString();
  }
  
  private int nextNonWhitespace(boolean paramBoolean) throws IOException {
    char[] arrayOfChar = this.buffer;
    int i = this.pos;
    int j = this.limit;
    while (true) {
      StringBuilder stringBuilder2;
      StringBuilder stringBuilder3;
      int m = i;
      int k = j;
      if (i == j) {
        this.pos = i;
        if (!fillBuffer(1)) {
          if (!paramBoolean)
            return -1; 
          stringBuilder3 = new StringBuilder();
          stringBuilder3.append("End of input");
          stringBuilder3.append(locationString());
          throw new EOFException(stringBuilder3.toString());
        } 
        m = this.pos;
        k = this.limit;
      } 
      i = m + 1;
      StringBuilder stringBuilder1 = stringBuilder3[m];
      if (stringBuilder1 == 10) {
        this.lineNumber++;
        this.lineStart = i;
      } else if (stringBuilder1 != 32 && stringBuilder1 != 13 && stringBuilder1 != 9) {
        int n;
        if (stringBuilder1 == 47) {
          this.pos = i;
          if (i == k) {
            this.pos = i - 1;
            boolean bool = fillBuffer(2);
            this.pos++;
            if (!bool)
              return stringBuilder1; 
          } 
          checkLenient();
          i = this.pos;
          stringBuilder2 = stringBuilder3[i];
          if (stringBuilder2 != 42) {
            if (stringBuilder2 != 47)
              return stringBuilder1; 
            this.pos = i + 1;
            skipToEndOfLine();
            i = this.pos;
            n = this.limit;
            continue;
          } 
          this.pos = i + 1;
          if (skipTo("*/")) {
            i = this.pos + 2;
            n = this.limit;
            continue;
          } 
          throw syntaxError("Unterminated comment");
        } 
        if (n == 35) {
          this.pos = i;
          checkLenient();
          skipToEndOfLine();
          i = this.pos;
          n = this.limit;
          continue;
        } 
        this.pos = i;
        return n;
      } 
      stringBuilder1 = stringBuilder2;
    } 
  }
  
  private String nextQuotedValue(char paramChar) throws IOException {
    char[] arrayOfChar = this.buffer;
    StringBuilder stringBuilder = new StringBuilder();
    while (true) {
      int i = this.pos;
      int j = this.limit;
      label21: while (true) {
        int k = i;
        while (true) {
          int m = k;
          if (m < j) {
            k = m + 1;
            m = arrayOfChar[m];
            if (m == paramChar) {
              this.pos = k;
              stringBuilder.append(arrayOfChar, i, k - i - 1);
              return stringBuilder.toString();
            } 
            if (m == 92) {
              this.pos = k;
              stringBuilder.append(arrayOfChar, i, k - i - 1);
              stringBuilder.append(readEscapeCharacter());
              i = this.pos;
              j = this.limit;
              continue label21;
            } 
            if (m == 10) {
              this.lineNumber++;
              this.lineStart = k;
            } 
            continue;
          } 
          stringBuilder.append(arrayOfChar, i, m - i);
          this.pos = m;
          break;
        } 
        break;
      } 
      if (fillBuffer(1))
        continue; 
      throw syntaxError("Unterminated string");
    } 
  }
  
  private String nextUnquotedValue() throws IOException {
    byte b;
    String str;
    boolean bool = false;
    StringBuilder stringBuilder = null;
    while (true) {
      b = 0;
      while (true) {
        int i = this.pos;
        if (i + b < this.limit) {
          i = this.buffer[i + b];
          if (i != 9 && i != 10 && i != 12 && i != 13 && i != 32)
            if (i != 35) {
              if (i != 44)
                if (i != 47 && i != 61) {
                  if (i != 123 && i != 125 && i != 58)
                    if (i != 59) {
                      switch (i) {
                        case 92:
                          checkLenient();
                          break;
                        case 91:
                        case 93:
                          break;
                      } 
                      continue;
                    }  
                  break;
                }  
              break;
            }  
          break;
        } 
        if (b < this.buffer.length) {
          if (fillBuffer(b + 1))
            continue; 
          break;
        } 
        StringBuilder stringBuilder1 = stringBuilder;
        if (stringBuilder == null)
          stringBuilder1 = new StringBuilder(); 
        stringBuilder1.append(this.buffer, this.pos, b);
        this.pos += b;
        stringBuilder = stringBuilder1;
        if (!fillBuffer(1)) {
          stringBuilder = stringBuilder1;
          b = bool;
          break;
        } 
      } 
      break;
    } 
    if (stringBuilder == null) {
      str = new String(this.buffer, this.pos, b);
    } else {
      str.append(this.buffer, this.pos, b);
      str = str.toString();
    } 
    this.pos += b;
    return str;
  }
  
  private int peekKeyword() throws IOException {
    String str1;
    String str2;
    char c = this.buffer[this.pos];
    if (c == 't' || c == 'T') {
      c = '\005';
      str1 = "true";
      str2 = "TRUE";
    } else if (c == 'f' || c == 'F') {
      c = '\006';
      str1 = "false";
      str2 = "FALSE";
    } else if (c == 'n' || c == 'N') {
      c = '\007';
      str1 = "null";
      str2 = "NULL";
    } else {
      return 0;
    } 
    int j = str1.length();
    for (int i = 1; i < j; i++) {
      if (this.pos + i >= this.limit && !fillBuffer(i + 1))
        return 0; 
      char c1 = this.buffer[this.pos + i];
      if (c1 != str1.charAt(i) && c1 != str2.charAt(i))
        return 0; 
    } 
    if ((this.pos + j < this.limit || fillBuffer(j + 1)) && isLiteral(this.buffer[this.pos + j]))
      return 0; 
    this.pos += j;
    this.peeked = c;
    return c;
  }
  
  private int peekNumber() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield buffer : [C
    //   4: astore #14
    //   6: aload_0
    //   7: getfield pos : I
    //   10: istore #5
    //   12: aload_0
    //   13: getfield limit : I
    //   16: istore #4
    //   18: iconst_1
    //   19: istore_3
    //   20: iconst_0
    //   21: istore #6
    //   23: iload #6
    //   25: istore_2
    //   26: iload_2
    //   27: istore #7
    //   29: lconst_0
    //   30: lstore #10
    //   32: iload #5
    //   34: istore #9
    //   36: iload #4
    //   38: istore #8
    //   40: iload #5
    //   42: iload #6
    //   44: iadd
    //   45: iload #4
    //   47: if_icmpne -> 86
    //   50: iload #6
    //   52: aload #14
    //   54: arraylength
    //   55: if_icmpne -> 60
    //   58: iconst_0
    //   59: ireturn
    //   60: aload_0
    //   61: iload #6
    //   63: iconst_1
    //   64: iadd
    //   65: invokespecial fillBuffer : (I)Z
    //   68: ifne -> 74
    //   71: goto -> 305
    //   74: aload_0
    //   75: getfield pos : I
    //   78: istore #9
    //   80: aload_0
    //   81: getfield limit : I
    //   84: istore #8
    //   86: aload #14
    //   88: iload #9
    //   90: iload #6
    //   92: iadd
    //   93: caload
    //   94: istore_1
    //   95: iconst_3
    //   96: istore #4
    //   98: iload_1
    //   99: bipush #43
    //   101: if_icmpeq -> 467
    //   104: iload_1
    //   105: bipush #69
    //   107: if_icmpeq -> 447
    //   110: iload_1
    //   111: bipush #101
    //   113: if_icmpeq -> 447
    //   116: iload_1
    //   117: bipush #45
    //   119: if_icmpeq -> 418
    //   122: iload_1
    //   123: bipush #46
    //   125: if_icmpeq -> 405
    //   128: iload_1
    //   129: bipush #48
    //   131: if_icmplt -> 297
    //   134: iload_1
    //   135: bipush #57
    //   137: if_icmple -> 143
    //   140: goto -> 297
    //   143: iload_2
    //   144: iconst_1
    //   145: if_icmpeq -> 270
    //   148: iload_2
    //   149: ifne -> 155
    //   152: goto -> 270
    //   155: iload_2
    //   156: iconst_2
    //   157: if_icmpne -> 233
    //   160: lload #10
    //   162: lconst_0
    //   163: lcmp
    //   164: ifne -> 169
    //   167: iconst_0
    //   168: ireturn
    //   169: ldc2_w 10
    //   172: lload #10
    //   174: lmul
    //   175: iload_1
    //   176: bipush #48
    //   178: isub
    //   179: i2l
    //   180: lsub
    //   181: lstore #12
    //   183: lload #10
    //   185: ldc2_w -922337203685477580
    //   188: lcmp
    //   189: istore #4
    //   191: iload #4
    //   193: ifgt -> 218
    //   196: iload #4
    //   198: ifne -> 212
    //   201: lload #12
    //   203: lload #10
    //   205: lcmp
    //   206: ifge -> 212
    //   209: goto -> 218
    //   212: iconst_0
    //   213: istore #4
    //   215: goto -> 221
    //   218: iconst_1
    //   219: istore #4
    //   221: iload_3
    //   222: iload #4
    //   224: iand
    //   225: istore #4
    //   227: iload_2
    //   228: istore #5
    //   230: goto -> 284
    //   233: iload_2
    //   234: iconst_3
    //   235: if_icmpne -> 243
    //   238: iconst_4
    //   239: istore_2
    //   240: goto -> 479
    //   243: iload_2
    //   244: iconst_5
    //   245: if_icmpeq -> 264
    //   248: iload_2
    //   249: istore #5
    //   251: iload_3
    //   252: istore #4
    //   254: lload #10
    //   256: lstore #12
    //   258: iload_2
    //   259: bipush #6
    //   261: if_icmpne -> 284
    //   264: bipush #7
    //   266: istore_2
    //   267: goto -> 479
    //   270: iload_1
    //   271: bipush #48
    //   273: isub
    //   274: ineg
    //   275: i2l
    //   276: lstore #12
    //   278: iconst_2
    //   279: istore #5
    //   281: iload_3
    //   282: istore #4
    //   284: iload #5
    //   286: istore_2
    //   287: iload #4
    //   289: istore_3
    //   290: lload #12
    //   292: lstore #10
    //   294: goto -> 479
    //   297: aload_0
    //   298: iload_1
    //   299: invokespecial isLiteral : (C)Z
    //   302: ifne -> 403
    //   305: iload_2
    //   306: iconst_2
    //   307: if_icmpne -> 367
    //   310: iload_3
    //   311: ifeq -> 367
    //   314: lload #10
    //   316: ldc2_w -9223372036854775808
    //   319: lcmp
    //   320: ifne -> 328
    //   323: iload #7
    //   325: ifeq -> 367
    //   328: iload #7
    //   330: ifeq -> 336
    //   333: goto -> 341
    //   336: lload #10
    //   338: lneg
    //   339: lstore #10
    //   341: aload_0
    //   342: lload #10
    //   344: putfield peekedLong : J
    //   347: aload_0
    //   348: aload_0
    //   349: getfield pos : I
    //   352: iload #6
    //   354: iadd
    //   355: putfield pos : I
    //   358: aload_0
    //   359: bipush #15
    //   361: putfield peeked : I
    //   364: bipush #15
    //   366: ireturn
    //   367: iload_2
    //   368: iconst_2
    //   369: if_icmpeq -> 388
    //   372: iload_2
    //   373: iconst_4
    //   374: if_icmpeq -> 388
    //   377: iload_2
    //   378: bipush #7
    //   380: if_icmpne -> 386
    //   383: goto -> 388
    //   386: iconst_0
    //   387: ireturn
    //   388: aload_0
    //   389: iload #6
    //   391: putfield peekedNumberLength : I
    //   394: aload_0
    //   395: bipush #16
    //   397: putfield peeked : I
    //   400: bipush #16
    //   402: ireturn
    //   403: iconst_0
    //   404: ireturn
    //   405: iload_2
    //   406: iconst_2
    //   407: if_icmpne -> 416
    //   410: iload #4
    //   412: istore_2
    //   413: goto -> 479
    //   416: iconst_0
    //   417: ireturn
    //   418: bipush #6
    //   420: istore #4
    //   422: iload_2
    //   423: ifne -> 434
    //   426: iconst_1
    //   427: istore_2
    //   428: iload_2
    //   429: istore #7
    //   431: goto -> 479
    //   434: iload_2
    //   435: iconst_5
    //   436: if_icmpne -> 445
    //   439: iload #4
    //   441: istore_2
    //   442: goto -> 479
    //   445: iconst_0
    //   446: ireturn
    //   447: iload_2
    //   448: iconst_2
    //   449: if_icmpeq -> 462
    //   452: iload_2
    //   453: iconst_4
    //   454: if_icmpne -> 460
    //   457: goto -> 462
    //   460: iconst_0
    //   461: ireturn
    //   462: iconst_5
    //   463: istore_2
    //   464: goto -> 479
    //   467: bipush #6
    //   469: istore #4
    //   471: iload_2
    //   472: iconst_5
    //   473: if_icmpne -> 496
    //   476: iload #4
    //   478: istore_2
    //   479: iload #6
    //   481: iconst_1
    //   482: iadd
    //   483: istore #6
    //   485: iload #9
    //   487: istore #5
    //   489: iload #8
    //   491: istore #4
    //   493: goto -> 32
    //   496: iconst_0
    //   497: ireturn
  }
  
  private void push(int paramInt) {
    int i = this.stackSize;
    int[] arrayOfInt = this.stack;
    if (i == arrayOfInt.length) {
      int[] arrayOfInt1 = new int[i * 2];
      int[] arrayOfInt2 = new int[i * 2];
      String[] arrayOfString = new String[i * 2];
      System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, i);
      System.arraycopy(this.pathIndices, 0, arrayOfInt2, 0, this.stackSize);
      System.arraycopy(this.pathNames, 0, arrayOfString, 0, this.stackSize);
      this.stack = arrayOfInt1;
      this.pathIndices = arrayOfInt2;
      this.pathNames = arrayOfString;
    } 
    arrayOfInt = this.stack;
    i = this.stackSize;
    this.stackSize = i + 1;
    arrayOfInt[i] = paramInt;
  }
  
  private char readEscapeCharacter() throws IOException {
    if (this.pos != this.limit || fillBuffer(1)) {
      char[] arrayOfChar = this.buffer;
      int i = this.pos;
      int j = i + 1;
      this.pos = j;
      char c = arrayOfChar[i];
      if (c != '\n') {
        if (c != '"' && c != '\'' && c != '/' && c != '\\') {
          if (c != 'b') {
            if (c != 'f') {
              if (c != 'n') {
                if (c != 'r') {
                  if (c != 't') {
                    if (c == 'u') {
                      if (j + 4 <= this.limit || fillBuffer(4)) {
                        c = Character.MIN_VALUE;
                        j = this.pos;
                        i = j;
                        while (true) {
                          int k = i;
                          if (k < j + 4) {
                            i = this.buffer[k];
                            char c1 = (char)(c << 4);
                            if (i >= 48 && i <= 57) {
                              i -= 48;
                            } else {
                              if (i >= 97 && i <= 102) {
                                i -= 97;
                              } else if (i >= 65) {
                                if (i <= 70) {
                                  i -= 65;
                                } else {
                                  StringBuilder stringBuilder = new StringBuilder();
                                  stringBuilder.append("\\u");
                                  stringBuilder.append(new String(this.buffer, this.pos, 4));
                                  throw new NumberFormatException(stringBuilder.toString());
                                } 
                              } else {
                                continue;
                              } 
                              i += 10;
                            } 
                            c = (char)(c1 + i);
                            i = k + 1;
                            continue;
                          } 
                          this.pos += 4;
                          return c;
                        } 
                      } 
                      throw syntaxError("Unterminated escape sequence");
                    } 
                    throw syntaxError("Invalid escape sequence");
                  } 
                  return '\t';
                } 
                return '\r';
              } 
              return '\n';
            } 
            return '\f';
          } 
          return '\b';
        } 
      } else {
        this.lineNumber++;
        this.lineStart = j;
      } 
      return c;
    } 
    throw syntaxError("Unterminated escape sequence");
  }
  
  private void skipQuotedValue(char paramChar) throws IOException {
    char[] arrayOfChar = this.buffer;
    while (true) {
      int i = this.pos;
      int j = this.limit;
      while (i < j) {
        int k = i + 1;
        i = arrayOfChar[i];
        if (i == paramChar) {
          this.pos = k;
          return;
        } 
        if (i == 92) {
          this.pos = k;
          readEscapeCharacter();
          i = this.pos;
          j = this.limit;
          continue;
        } 
        if (i == 10) {
          this.lineNumber++;
          this.lineStart = k;
        } 
        i = k;
      } 
      this.pos = i;
      if (fillBuffer(1))
        continue; 
      throw syntaxError("Unterminated string");
    } 
  }
  
  private boolean skipTo(String paramString) throws IOException {
    label19: while (true) {
      int j = this.pos;
      int k = paramString.length();
      int m = this.limit;
      int i = 0;
      if (j + k <= m || fillBuffer(paramString.length())) {
        char[] arrayOfChar = this.buffer;
        j = this.pos;
        if (arrayOfChar[j] == '\n') {
          this.lineNumber++;
          this.lineStart = j + 1;
          continue;
        } 
        while (i < paramString.length()) {
          if (this.buffer[this.pos + i] != paramString.charAt(i)) {
            this.pos++;
            continue label19;
          } 
          i++;
        } 
        return true;
      } 
      return false;
    } 
  }
  
  private void skipToEndOfLine() throws IOException {
    while (this.pos < this.limit || fillBuffer(1)) {
      char[] arrayOfChar = this.buffer;
      int j = this.pos;
      int i = j + 1;
      this.pos = i;
      j = arrayOfChar[j];
      if (j == 10) {
        this.lineNumber++;
        this.lineStart = i;
        return;
      } 
      if (j == 13)
        break; 
    } 
  }
  
  private void skipUnquotedValue() throws IOException {
    do {
      int i = 0;
      while (true) {
        int j = this.pos;
        if (j + i < this.limit) {
          j = this.buffer[j + i];
          if (j != 9 && j != 10 && j != 12 && j != 13 && j != 32)
            if (j != 35) {
              if (j != 44)
                if (j != 47 && j != 61) {
                  if (j != 123 && j != 125 && j != 58)
                    if (j != 59) {
                      switch (j) {
                        default:
                          i++;
                          continue;
                        case 92:
                          checkLenient();
                          break;
                        case 91:
                        case 93:
                          break;
                      } 
                    } else {
                    
                    }  
                } else {
                
                }  
            } else {
            
            }  
          this.pos += i;
          return;
        } 
        this.pos = j + i;
        break;
      } 
    } while (fillBuffer(1));
  }
  
  private IOException syntaxError(String paramString) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(locationString());
    throw new MalformedJsonException(stringBuilder.toString());
  }
  
  public void beginArray() throws IOException {
    int j = this.peeked;
    int i = j;
    if (j == 0)
      i = doPeek(); 
    if (i == 3) {
      push(1);
      this.pathIndices[this.stackSize - 1] = 0;
      this.peeked = 0;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected BEGIN_ARRAY but was ");
    stringBuilder.append(peek());
    stringBuilder.append(locationString());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public void beginObject() throws IOException {
    int j = this.peeked;
    int i = j;
    if (j == 0)
      i = doPeek(); 
    if (i == 1) {
      push(3);
      this.peeked = 0;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected BEGIN_OBJECT but was ");
    stringBuilder.append(peek());
    stringBuilder.append(locationString());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public void close() throws IOException {
    this.peeked = 0;
    this.stack[0] = 8;
    this.stackSize = 1;
    this.in.close();
  }
  
  int doPeek() throws IOException {
    int[] arrayOfInt = this.stack;
    int j = this.stackSize;
    int i = arrayOfInt[j - 1];
    if (i == 1) {
      arrayOfInt[j - 1] = 2;
    } else if (i == 2) {
      j = nextNonWhitespace(true);
      if (j != 44) {
        if (j != 59) {
          if (j == 93) {
            this.peeked = 4;
            return 4;
          } 
          throw syntaxError("Unterminated array");
        } 
        checkLenient();
      } 
    } else {
      if (i == 3 || i == 5) {
        this.stack[this.stackSize - 1] = 4;
        if (i == 5) {
          j = nextNonWhitespace(true);
          if (j != 44) {
            if (j != 59) {
              if (j == 125) {
                this.peeked = 2;
                return 2;
              } 
              throw syntaxError("Unterminated object");
            } 
            checkLenient();
          } 
        } 
        j = nextNonWhitespace(true);
        if (j != 34) {
          if (j != 39) {
            if (j != 125) {
              checkLenient();
              this.pos--;
              if (isLiteral((char)j)) {
                this.peeked = 14;
                return 14;
              } 
              throw syntaxError("Expected name");
            } 
            if (i != 5) {
              this.peeked = 2;
              return 2;
            } 
            throw syntaxError("Expected name");
          } 
          checkLenient();
          this.peeked = 12;
          return 12;
        } 
        this.peeked = 13;
        return 13;
      } 
      if (i == 4) {
        arrayOfInt[j - 1] = 5;
        j = nextNonWhitespace(true);
        if (j != 58)
          if (j == 61) {
            checkLenient();
            if (this.pos < this.limit || fillBuffer(1)) {
              char[] arrayOfChar = this.buffer;
              j = this.pos;
              if (arrayOfChar[j] == '>')
                this.pos = j + 1; 
            } 
          } else {
            throw syntaxError("Expected ':'");
          }  
      } else if (i == 6) {
        if (this.lenient)
          consumeNonExecutePrefix(); 
        this.stack[this.stackSize - 1] = 7;
      } else if (i == 7) {
        if (nextNonWhitespace(false) == -1) {
          this.peeked = 17;
          return 17;
        } 
        checkLenient();
        this.pos--;
      } else if (i == 8) {
        throw new IllegalStateException("JsonReader is closed");
      } 
    } 
    j = nextNonWhitespace(true);
    if (j != 34) {
      if (j != 39) {
        if (j != 44 && j != 59)
          if (j != 91) {
            if (j != 93) {
              if (j != 123) {
                this.pos--;
                i = peekKeyword();
                if (i != 0)
                  return i; 
                i = peekNumber();
                if (i != 0)
                  return i; 
                if (isLiteral(this.buffer[this.pos])) {
                  checkLenient();
                  this.peeked = 10;
                  return 10;
                } 
                throw syntaxError("Expected value");
              } 
              this.peeked = 1;
              return 1;
            } 
            if (i == 1) {
              this.peeked = 4;
              return 4;
            } 
          } else {
            this.peeked = 3;
            return 3;
          }  
        if (i == 1 || i == 2) {
          checkLenient();
          this.pos--;
          this.peeked = 7;
          return 7;
        } 
        throw syntaxError("Unexpected value");
      } 
      checkLenient();
      this.peeked = 8;
      return 8;
    } 
    this.peeked = 9;
    return 9;
  }
  
  public void endArray() throws IOException {
    int j = this.peeked;
    int i = j;
    if (j == 0)
      i = doPeek(); 
    if (i == 4) {
      i = this.stackSize - 1;
      this.stackSize = i;
      int[] arrayOfInt = this.pathIndices;
      arrayOfInt[--i] = arrayOfInt[i] + 1;
      this.peeked = 0;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected END_ARRAY but was ");
    stringBuilder.append(peek());
    stringBuilder.append(locationString());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public void endObject() throws IOException {
    int j = this.peeked;
    int i = j;
    if (j == 0)
      i = doPeek(); 
    if (i == 2) {
      i = this.stackSize - 1;
      this.stackSize = i;
      this.pathNames[i] = null;
      int[] arrayOfInt = this.pathIndices;
      arrayOfInt[--i] = arrayOfInt[i] + 1;
      this.peeked = 0;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected END_OBJECT but was ");
    stringBuilder.append(peek());
    stringBuilder.append(locationString());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public String getPath() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('$');
    int j = this.stackSize;
    for (int i = 0; i < j; i++) {
      int k = this.stack[i];
      if (k != 1 && k != 2) {
        if (k == 3 || k == 4 || k == 5) {
          stringBuilder.append('.');
          String[] arrayOfString = this.pathNames;
          if (arrayOfString[i] != null)
            stringBuilder.append(arrayOfString[i]); 
        } 
      } else {
        stringBuilder.append('[');
        stringBuilder.append(this.pathIndices[i]);
        stringBuilder.append(']');
      } 
    } 
    return stringBuilder.toString();
  }
  
  public boolean hasNext() throws IOException {
    int j = this.peeked;
    int i = j;
    if (j == 0)
      i = doPeek(); 
    return (i != 2 && i != 4);
  }
  
  public final boolean isLenient() {
    return this.lenient;
  }
  
  public boolean nextBoolean() throws IOException {
    int j = this.peeked;
    int i = j;
    if (j == 0)
      i = doPeek(); 
    if (i == 5) {
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      i = this.stackSize - 1;
      arrayOfInt[i] = arrayOfInt[i] + 1;
      return true;
    } 
    if (i == 6) {
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      i = this.stackSize - 1;
      arrayOfInt[i] = arrayOfInt[i] + 1;
      return false;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected a boolean but was ");
    stringBuilder.append(peek());
    stringBuilder.append(locationString());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public double nextDouble() throws IOException {
    int j = this.peeked;
    int i = j;
    if (j == 0)
      i = doPeek(); 
    if (i == 15) {
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      i = this.stackSize - 1;
      arrayOfInt[i] = arrayOfInt[i] + 1;
      return this.peekedLong;
    } 
    if (i == 16) {
      this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
      this.pos += this.peekedNumberLength;
    } else if (i == 8 || i == 9) {
      byte b;
      if (i == 8) {
        b = 39;
      } else {
        b = 34;
      } 
      this.peekedString = nextQuotedValue(b);
    } else if (i == 10) {
      this.peekedString = nextUnquotedValue();
    } else if (i != 11) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Expected a double but was ");
      stringBuilder1.append(peek());
      stringBuilder1.append(locationString());
      throw new IllegalStateException(stringBuilder1.toString());
    } 
    this.peeked = 11;
    double d = Double.parseDouble(this.peekedString);
    if (this.lenient || (!Double.isNaN(d) && !Double.isInfinite(d))) {
      this.peekedString = null;
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      i = this.stackSize - 1;
      arrayOfInt[i] = arrayOfInt[i] + 1;
      return d;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("JSON forbids NaN and infinities: ");
    stringBuilder.append(d);
    stringBuilder.append(locationString());
    throw new MalformedJsonException(stringBuilder.toString());
  }
  
  public int nextInt() throws IOException {
    int j = this.peeked;
    int i = j;
    if (j == 0)
      i = doPeek(); 
    if (i == 15) {
      long l = this.peekedLong;
      i = (int)l;
      if (l == i) {
        this.peeked = 0;
        int[] arrayOfInt = this.pathIndices;
        j = this.stackSize - 1;
        arrayOfInt[j] = arrayOfInt[j] + 1;
        return i;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Expected an int but was ");
      stringBuilder1.append(this.peekedLong);
      stringBuilder1.append(locationString());
      throw new NumberFormatException(stringBuilder1.toString());
    } 
    if (i == 16) {
      this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
      this.pos += this.peekedNumberLength;
    } else if (i == 8 || i == 9 || i == 10) {
      if (i == 10) {
        this.peekedString = nextUnquotedValue();
      } else {
        byte b;
        if (i == 8) {
          b = 39;
        } else {
          b = 34;
        } 
        this.peekedString = nextQuotedValue(b);
      } 
      try {
        i = Integer.parseInt(this.peekedString);
        this.peeked = 0;
        int[] arrayOfInt = this.pathIndices;
        j = this.stackSize - 1;
        arrayOfInt[j] = arrayOfInt[j] + 1;
        return i;
      } catch (NumberFormatException numberFormatException) {}
    } else {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Expected an int but was ");
      stringBuilder1.append(peek());
      stringBuilder1.append(locationString());
      throw new IllegalStateException(stringBuilder1.toString());
    } 
    this.peeked = 11;
    double d = Double.parseDouble(this.peekedString);
    i = (int)d;
    if (i == d) {
      this.peekedString = null;
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      j = this.stackSize - 1;
      arrayOfInt[j] = arrayOfInt[j] + 1;
      return i;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected an int but was ");
    stringBuilder.append(this.peekedString);
    stringBuilder.append(locationString());
    throw new NumberFormatException(stringBuilder.toString());
  }
  
  public long nextLong() throws IOException {
    int j = this.peeked;
    int i = j;
    if (j == 0)
      i = doPeek(); 
    if (i == 15) {
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      i = this.stackSize - 1;
      arrayOfInt[i] = arrayOfInt[i] + 1;
      return this.peekedLong;
    } 
    if (i == 16) {
      this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
      this.pos += this.peekedNumberLength;
    } else if (i == 8 || i == 9 || i == 10) {
      if (i == 10) {
        this.peekedString = nextUnquotedValue();
      } else {
        byte b;
        if (i == 8) {
          b = 39;
        } else {
          b = 34;
        } 
        this.peekedString = nextQuotedValue(b);
      } 
      try {
        long l1 = Long.parseLong(this.peekedString);
        this.peeked = 0;
        int[] arrayOfInt = this.pathIndices;
        i = this.stackSize - 1;
        arrayOfInt[i] = arrayOfInt[i] + 1;
        return l1;
      } catch (NumberFormatException numberFormatException) {}
    } else {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Expected a long but was ");
      stringBuilder1.append(peek());
      stringBuilder1.append(locationString());
      throw new IllegalStateException(stringBuilder1.toString());
    } 
    this.peeked = 11;
    double d = Double.parseDouble(this.peekedString);
    long l = (long)d;
    if (l == d) {
      this.peekedString = null;
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      i = this.stackSize - 1;
      arrayOfInt[i] = arrayOfInt[i] + 1;
      return l;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected a long but was ");
    stringBuilder.append(this.peekedString);
    stringBuilder.append(locationString());
    throw new NumberFormatException(stringBuilder.toString());
  }
  
  public String nextName() throws IOException {
    StringBuilder stringBuilder;
    int j = this.peeked;
    int i = j;
    if (j == 0)
      i = doPeek(); 
    if (i == 14) {
      String str = nextUnquotedValue();
    } else if (i == 12) {
      String str = nextQuotedValue('\'');
    } else {
      if (i == 13) {
        String str = nextQuotedValue('"');
        this.peeked = 0;
        this.pathNames[this.stackSize - 1] = str;
        return str;
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("Expected a name but was ");
      stringBuilder.append(peek());
      stringBuilder.append(locationString());
      throw new IllegalStateException(stringBuilder.toString());
    } 
    this.peeked = 0;
    this.pathNames[this.stackSize - 1] = (String)stringBuilder;
    return (String)stringBuilder;
  }
  
  public void nextNull() throws IOException {
    int j = this.peeked;
    int i = j;
    if (j == 0)
      i = doPeek(); 
    if (i == 7) {
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      i = this.stackSize - 1;
      arrayOfInt[i] = arrayOfInt[i] + 1;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected null but was ");
    stringBuilder.append(peek());
    stringBuilder.append(locationString());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public String nextString() throws IOException {
    StringBuilder stringBuilder;
    int j = this.peeked;
    int i = j;
    if (j == 0)
      i = doPeek(); 
    if (i == 10) {
      String str = nextUnquotedValue();
    } else if (i == 8) {
      String str = nextQuotedValue('\'');
    } else if (i == 9) {
      String str = nextQuotedValue('"');
    } else if (i == 11) {
      String str = this.peekedString;
      this.peekedString = null;
    } else if (i == 15) {
      String str = Long.toString(this.peekedLong);
    } else {
      if (i == 16) {
        String str = new String(this.buffer, this.pos, this.peekedNumberLength);
        this.pos += this.peekedNumberLength;
        this.peeked = 0;
        int[] arrayOfInt1 = this.pathIndices;
        i = this.stackSize - 1;
        arrayOfInt1[i] = arrayOfInt1[i] + 1;
        return str;
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("Expected a string but was ");
      stringBuilder.append(peek());
      stringBuilder.append(locationString());
      throw new IllegalStateException(stringBuilder.toString());
    } 
    this.peeked = 0;
    int[] arrayOfInt = this.pathIndices;
    i = this.stackSize - 1;
    arrayOfInt[i] = arrayOfInt[i] + 1;
    return (String)stringBuilder;
  }
  
  public JsonToken peek() throws IOException {
    int j = this.peeked;
    int i = j;
    if (j == 0)
      i = doPeek(); 
    switch (i) {
      default:
        throw new AssertionError();
      case 17:
        return JsonToken.END_DOCUMENT;
      case 15:
      case 16:
        return JsonToken.NUMBER;
      case 12:
      case 13:
      case 14:
        return JsonToken.NAME;
      case 8:
      case 9:
      case 10:
      case 11:
        return JsonToken.STRING;
      case 7:
        return JsonToken.NULL;
      case 5:
      case 6:
        return JsonToken.BOOLEAN;
      case 4:
        return JsonToken.END_ARRAY;
      case 3:
        return JsonToken.BEGIN_ARRAY;
      case 2:
        return JsonToken.END_OBJECT;
      case 1:
        break;
    } 
    return JsonToken.BEGIN_OBJECT;
  }
  
  public final void setLenient(boolean paramBoolean) {
    this.lenient = paramBoolean;
  }
  
  public void skipValue() throws IOException {
    for (int i = 0;; i = j) {
      int j = this.peeked;
      int k = j;
      if (j == 0)
        k = doPeek(); 
      if (k == 3) {
        push(1);
      } else if (k == 1) {
        push(3);
      } else {
        if (k == 4) {
          this.stackSize--;
        } else if (k == 2) {
          this.stackSize--;
        } else {
          if (k == 14 || k == 10) {
            skipUnquotedValue();
            j = i;
          } else if (k == 8 || k == 12) {
            skipQuotedValue('\'');
            j = i;
          } else if (k == 9 || k == 13) {
            skipQuotedValue('"');
            j = i;
          } else {
            j = i;
            if (k == 16) {
              this.pos += this.peekedNumberLength;
              j = i;
            } 
          } 
          this.peeked = 0;
          i = j;
        } 
        j = i - 1;
        this.peeked = 0;
        i = j;
      } 
      j = i + 1;
      this.peeked = 0;
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getClass().getSimpleName());
    stringBuilder.append(locationString());
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\stream\JsonReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */