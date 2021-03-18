package com.google.gson.typeadapters;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public final class UtcDateTypeAdapter extends TypeAdapter<Date> {
  private static final String GMT_ID = "GMT";
  
  private final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
  
  private static boolean checkOffset(String paramString, int paramInt, char paramChar) {
    return (paramInt < paramString.length() && paramString.charAt(paramInt) == paramChar);
  }
  
  private static String format(Date paramDate, boolean paramBoolean, TimeZone paramTimeZone) {
    int j;
    GregorianCalendar gregorianCalendar = new GregorianCalendar(paramTimeZone, Locale.US);
    gregorianCalendar.setTime(paramDate);
    if (paramBoolean) {
      i = 4;
    } else {
      i = 0;
    } 
    if (paramTimeZone.getRawOffset() == 0) {
      j = 1;
    } else {
      j = 6;
    } 
    StringBuilder stringBuilder = new StringBuilder(19 + i + j);
    padInt(stringBuilder, gregorianCalendar.get(1), 4);
    byte b = 45;
    stringBuilder.append('-');
    padInt(stringBuilder, gregorianCalendar.get(2) + 1, 2);
    stringBuilder.append('-');
    padInt(stringBuilder, gregorianCalendar.get(5), 2);
    stringBuilder.append('T');
    padInt(stringBuilder, gregorianCalendar.get(11), 2);
    stringBuilder.append(':');
    padInt(stringBuilder, gregorianCalendar.get(12), 2);
    stringBuilder.append(':');
    padInt(stringBuilder, gregorianCalendar.get(13), 2);
    if (paramBoolean) {
      stringBuilder.append('.');
      padInt(stringBuilder, gregorianCalendar.get(14), 3);
    } 
    int i = paramTimeZone.getOffset(gregorianCalendar.getTimeInMillis());
    if (i != 0) {
      int k = i / 60000;
      j = Math.abs(k / 60);
      k = Math.abs(k % 60);
      if (i >= 0)
        b = 43; 
      stringBuilder.append(b);
      padInt(stringBuilder, j, 2);
      stringBuilder.append(':');
      padInt(stringBuilder, k, 2);
    } else {
      stringBuilder.append('Z');
    } 
    return stringBuilder.toString();
  }
  
  private static void padInt(StringBuilder paramStringBuilder, int paramInt1, int paramInt2) {
    String str = Integer.toString(paramInt1);
    for (paramInt1 = paramInt2 - str.length(); paramInt1 > 0; paramInt1--)
      paramStringBuilder.append('0'); 
    paramStringBuilder.append(str);
  }
  
  private static Date parse(String paramString, ParsePosition paramParsePosition) throws ParseException {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getIndex : ()I
    //   4: istore_3
    //   5: iload_3
    //   6: iconst_4
    //   7: iadd
    //   8: istore #4
    //   10: aload_0
    //   11: iload_3
    //   12: iload #4
    //   14: invokestatic parseInt : (Ljava/lang/String;II)I
    //   17: istore #9
    //   19: iload #4
    //   21: istore_3
    //   22: aload_0
    //   23: iload #4
    //   25: bipush #45
    //   27: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   30: ifeq -> 38
    //   33: iload #4
    //   35: iconst_1
    //   36: iadd
    //   37: istore_3
    //   38: iload_3
    //   39: iconst_2
    //   40: iadd
    //   41: istore #4
    //   43: aload_0
    //   44: iload_3
    //   45: iload #4
    //   47: invokestatic parseInt : (Ljava/lang/String;II)I
    //   50: istore #10
    //   52: iload #4
    //   54: istore_3
    //   55: aload_0
    //   56: iload #4
    //   58: bipush #45
    //   60: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   63: ifeq -> 71
    //   66: iload #4
    //   68: iconst_1
    //   69: iadd
    //   70: istore_3
    //   71: iload_3
    //   72: iconst_2
    //   73: iadd
    //   74: istore #8
    //   76: aload_0
    //   77: iload_3
    //   78: iload #8
    //   80: invokestatic parseInt : (Ljava/lang/String;II)I
    //   83: istore #11
    //   85: aload_0
    //   86: iload #8
    //   88: bipush #84
    //   90: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   93: ifeq -> 669
    //   96: iload #8
    //   98: iconst_1
    //   99: iadd
    //   100: istore_3
    //   101: iload_3
    //   102: iconst_2
    //   103: iadd
    //   104: istore #5
    //   106: aload_0
    //   107: iload_3
    //   108: iload #5
    //   110: invokestatic parseInt : (Ljava/lang/String;II)I
    //   113: istore #4
    //   115: iload #5
    //   117: istore_3
    //   118: aload_0
    //   119: iload #5
    //   121: bipush #58
    //   123: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   126: ifeq -> 134
    //   129: iload #5
    //   131: iconst_1
    //   132: iadd
    //   133: istore_3
    //   134: iload_3
    //   135: iconst_2
    //   136: iadd
    //   137: istore #6
    //   139: aload_0
    //   140: iload_3
    //   141: iload #6
    //   143: invokestatic parseInt : (Ljava/lang/String;II)I
    //   146: istore #5
    //   148: iload #6
    //   150: istore_3
    //   151: aload_0
    //   152: iload #6
    //   154: bipush #58
    //   156: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   159: ifeq -> 167
    //   162: iload #6
    //   164: iconst_1
    //   165: iadd
    //   166: istore_3
    //   167: aload_0
    //   168: invokevirtual length : ()I
    //   171: iload_3
    //   172: if_icmple -> 659
    //   175: aload_0
    //   176: iload_3
    //   177: invokevirtual charAt : (I)C
    //   180: istore #6
    //   182: iload #6
    //   184: bipush #90
    //   186: if_icmpeq -> 659
    //   189: iload #6
    //   191: bipush #43
    //   193: if_icmpeq -> 659
    //   196: iload #6
    //   198: bipush #45
    //   200: if_icmpeq -> 659
    //   203: iload_3
    //   204: iconst_2
    //   205: iadd
    //   206: istore #6
    //   208: aload_0
    //   209: iload_3
    //   210: iload #6
    //   212: invokestatic parseInt : (Ljava/lang/String;II)I
    //   215: istore #7
    //   217: aload_0
    //   218: iload #6
    //   220: bipush #46
    //   222: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   225: ifeq -> 646
    //   228: iload #6
    //   230: iconst_1
    //   231: iadd
    //   232: istore #6
    //   234: iload #6
    //   236: iconst_3
    //   237: iadd
    //   238: istore_3
    //   239: aload_0
    //   240: iload #6
    //   242: iload_3
    //   243: invokestatic parseInt : (Ljava/lang/String;II)I
    //   246: istore #6
    //   248: goto -> 656
    //   251: aload_0
    //   252: invokevirtual length : ()I
    //   255: iload_3
    //   256: if_icmple -> 499
    //   259: aload_0
    //   260: iload_3
    //   261: invokevirtual charAt : (I)C
    //   264: istore_2
    //   265: ldc 'GMT'
    //   267: astore #12
    //   269: iload_2
    //   270: bipush #43
    //   272: if_icmpeq -> 334
    //   275: iload_2
    //   276: bipush #45
    //   278: if_icmpne -> 284
    //   281: goto -> 334
    //   284: iload_2
    //   285: bipush #90
    //   287: if_icmpne -> 297
    //   290: iload_3
    //   291: iconst_1
    //   292: iadd
    //   293: istore_3
    //   294: goto -> 381
    //   297: new java/lang/StringBuilder
    //   300: dup
    //   301: invokespecial <init> : ()V
    //   304: astore #12
    //   306: aload #12
    //   308: ldc 'Invalid time zone indicator '
    //   310: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   313: pop
    //   314: aload #12
    //   316: iload_2
    //   317: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   320: pop
    //   321: new java/lang/IndexOutOfBoundsException
    //   324: dup
    //   325: aload #12
    //   327: invokevirtual toString : ()Ljava/lang/String;
    //   330: invokespecial <init> : (Ljava/lang/String;)V
    //   333: athrow
    //   334: aload_0
    //   335: iload_3
    //   336: invokevirtual substring : (I)Ljava/lang/String;
    //   339: astore #13
    //   341: new java/lang/StringBuilder
    //   344: dup
    //   345: invokespecial <init> : ()V
    //   348: astore #12
    //   350: aload #12
    //   352: ldc 'GMT'
    //   354: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   357: pop
    //   358: aload #12
    //   360: aload #13
    //   362: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   365: pop
    //   366: aload #12
    //   368: invokevirtual toString : ()Ljava/lang/String;
    //   371: astore #12
    //   373: iload_3
    //   374: aload #13
    //   376: invokevirtual length : ()I
    //   379: iadd
    //   380: istore_3
    //   381: aload #12
    //   383: invokestatic getTimeZone : (Ljava/lang/String;)Ljava/util/TimeZone;
    //   386: astore #13
    //   388: aload #13
    //   390: invokevirtual getID : ()Ljava/lang/String;
    //   393: aload #12
    //   395: invokevirtual equals : (Ljava/lang/Object;)Z
    //   398: ifeq -> 491
    //   401: new java/util/GregorianCalendar
    //   404: dup
    //   405: aload #13
    //   407: invokespecial <init> : (Ljava/util/TimeZone;)V
    //   410: astore #12
    //   412: aload #12
    //   414: iconst_0
    //   415: invokevirtual setLenient : (Z)V
    //   418: aload #12
    //   420: iconst_1
    //   421: iload #9
    //   423: invokevirtual set : (II)V
    //   426: aload #12
    //   428: iconst_2
    //   429: iload #10
    //   431: iconst_1
    //   432: isub
    //   433: invokevirtual set : (II)V
    //   436: aload #12
    //   438: iconst_5
    //   439: iload #11
    //   441: invokevirtual set : (II)V
    //   444: aload #12
    //   446: bipush #11
    //   448: iload #4
    //   450: invokevirtual set : (II)V
    //   453: aload #12
    //   455: bipush #12
    //   457: iload #5
    //   459: invokevirtual set : (II)V
    //   462: aload #12
    //   464: bipush #13
    //   466: iload #7
    //   468: invokevirtual set : (II)V
    //   471: aload #12
    //   473: bipush #14
    //   475: iload #6
    //   477: invokevirtual set : (II)V
    //   480: aload_1
    //   481: iload_3
    //   482: invokevirtual setIndex : (I)V
    //   485: aload #12
    //   487: invokevirtual getTime : ()Ljava/util/Date;
    //   490: areturn
    //   491: new java/lang/IndexOutOfBoundsException
    //   494: dup
    //   495: invokespecial <init> : ()V
    //   498: athrow
    //   499: new java/lang/IllegalArgumentException
    //   502: dup
    //   503: ldc 'No time zone indicator'
    //   505: invokespecial <init> : (Ljava/lang/String;)V
    //   508: athrow
    //   509: astore #12
    //   511: goto -> 539
    //   514: astore #12
    //   516: goto -> 539
    //   519: astore #12
    //   521: goto -> 539
    //   524: astore #12
    //   526: goto -> 539
    //   529: astore #12
    //   531: goto -> 526
    //   534: astore #12
    //   536: goto -> 526
    //   539: aload_0
    //   540: ifnonnull -> 548
    //   543: aconst_null
    //   544: astore_0
    //   545: goto -> 586
    //   548: new java/lang/StringBuilder
    //   551: dup
    //   552: invokespecial <init> : ()V
    //   555: astore #13
    //   557: aload #13
    //   559: bipush #34
    //   561: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   564: pop
    //   565: aload #13
    //   567: aload_0
    //   568: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   571: pop
    //   572: aload #13
    //   574: ldc '''
    //   576: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   579: pop
    //   580: aload #13
    //   582: invokevirtual toString : ()Ljava/lang/String;
    //   585: astore_0
    //   586: new java/lang/StringBuilder
    //   589: dup
    //   590: invokespecial <init> : ()V
    //   593: astore #13
    //   595: aload #13
    //   597: ldc 'Failed to parse date ['
    //   599: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   602: pop
    //   603: aload #13
    //   605: aload_0
    //   606: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   609: pop
    //   610: aload #13
    //   612: ldc ']: '
    //   614: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   617: pop
    //   618: aload #13
    //   620: aload #12
    //   622: invokevirtual getMessage : ()Ljava/lang/String;
    //   625: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   628: pop
    //   629: new java/text/ParseException
    //   632: dup
    //   633: aload #13
    //   635: invokevirtual toString : ()Ljava/lang/String;
    //   638: aload_1
    //   639: invokevirtual getIndex : ()I
    //   642: invokespecial <init> : (Ljava/lang/String;I)V
    //   645: athrow
    //   646: iconst_0
    //   647: istore #8
    //   649: iload #6
    //   651: istore_3
    //   652: iload #8
    //   654: istore #6
    //   656: goto -> 251
    //   659: iconst_0
    //   660: istore #7
    //   662: iload #7
    //   664: istore #6
    //   666: goto -> 251
    //   669: iconst_0
    //   670: istore #4
    //   672: iload #4
    //   674: istore #5
    //   676: iload #5
    //   678: istore #6
    //   680: iload #6
    //   682: istore #7
    //   684: iload #8
    //   686: istore_3
    //   687: goto -> 251
    // Exception table:
    //   from	to	target	type
    //   0	5	534	java/lang/IndexOutOfBoundsException
    //   0	5	529	java/lang/NumberFormatException
    //   0	5	524	java/lang/IllegalArgumentException
    //   10	19	534	java/lang/IndexOutOfBoundsException
    //   10	19	529	java/lang/NumberFormatException
    //   10	19	524	java/lang/IllegalArgumentException
    //   22	33	534	java/lang/IndexOutOfBoundsException
    //   22	33	529	java/lang/NumberFormatException
    //   22	33	524	java/lang/IllegalArgumentException
    //   43	52	534	java/lang/IndexOutOfBoundsException
    //   43	52	529	java/lang/NumberFormatException
    //   43	52	524	java/lang/IllegalArgumentException
    //   55	66	534	java/lang/IndexOutOfBoundsException
    //   55	66	529	java/lang/NumberFormatException
    //   55	66	524	java/lang/IllegalArgumentException
    //   76	96	534	java/lang/IndexOutOfBoundsException
    //   76	96	529	java/lang/NumberFormatException
    //   76	96	524	java/lang/IllegalArgumentException
    //   106	115	534	java/lang/IndexOutOfBoundsException
    //   106	115	529	java/lang/NumberFormatException
    //   106	115	524	java/lang/IllegalArgumentException
    //   118	129	534	java/lang/IndexOutOfBoundsException
    //   118	129	529	java/lang/NumberFormatException
    //   118	129	524	java/lang/IllegalArgumentException
    //   139	148	534	java/lang/IndexOutOfBoundsException
    //   139	148	529	java/lang/NumberFormatException
    //   139	148	524	java/lang/IllegalArgumentException
    //   151	162	534	java/lang/IndexOutOfBoundsException
    //   151	162	529	java/lang/NumberFormatException
    //   151	162	524	java/lang/IllegalArgumentException
    //   167	182	534	java/lang/IndexOutOfBoundsException
    //   167	182	529	java/lang/NumberFormatException
    //   167	182	524	java/lang/IllegalArgumentException
    //   208	228	534	java/lang/IndexOutOfBoundsException
    //   208	228	529	java/lang/NumberFormatException
    //   208	228	524	java/lang/IllegalArgumentException
    //   239	248	534	java/lang/IndexOutOfBoundsException
    //   239	248	529	java/lang/NumberFormatException
    //   239	248	524	java/lang/IllegalArgumentException
    //   251	265	534	java/lang/IndexOutOfBoundsException
    //   251	265	529	java/lang/NumberFormatException
    //   251	265	524	java/lang/IllegalArgumentException
    //   297	334	534	java/lang/IndexOutOfBoundsException
    //   297	334	529	java/lang/NumberFormatException
    //   297	334	524	java/lang/IllegalArgumentException
    //   334	381	534	java/lang/IndexOutOfBoundsException
    //   334	381	529	java/lang/NumberFormatException
    //   334	381	524	java/lang/IllegalArgumentException
    //   381	480	534	java/lang/IndexOutOfBoundsException
    //   381	480	529	java/lang/NumberFormatException
    //   381	480	524	java/lang/IllegalArgumentException
    //   480	491	519	java/lang/IndexOutOfBoundsException
    //   480	491	514	java/lang/NumberFormatException
    //   480	491	509	java/lang/IllegalArgumentException
    //   491	499	519	java/lang/IndexOutOfBoundsException
    //   491	499	514	java/lang/NumberFormatException
    //   491	499	509	java/lang/IllegalArgumentException
    //   499	509	519	java/lang/IndexOutOfBoundsException
    //   499	509	514	java/lang/NumberFormatException
    //   499	509	509	java/lang/IllegalArgumentException
  }
  
  private static int parseInt(String paramString, int paramInt1, int paramInt2) throws NumberFormatException {
    if (paramInt1 >= 0 && paramInt2 <= paramString.length() && paramInt1 <= paramInt2) {
      int i = 0;
      int j = paramInt1;
      if (paramInt1 < paramInt2) {
        i = Character.digit(paramString.charAt(paramInt1), 10);
        if (i >= 0) {
          i = -i;
          j = paramInt1 + 1;
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Invalid number: ");
          stringBuilder.append(paramString);
          throw new NumberFormatException(stringBuilder.toString());
        } 
      } 
      while (j < paramInt2) {
        paramInt1 = Character.digit(paramString.charAt(j), 10);
        if (paramInt1 >= 0) {
          i = i * 10 - paramInt1;
          j++;
          continue;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid number: ");
        stringBuilder.append(paramString);
        throw new NumberFormatException(stringBuilder.toString());
      } 
      return -i;
    } 
    throw new NumberFormatException(paramString);
  }
  
  public Date read(JsonReader paramJsonReader) throws IOException {
    try {
      if (null.$SwitchMap$com$google$gson$stream$JsonToken[paramJsonReader.peek().ordinal()] != 1)
        return parse(paramJsonReader.nextString(), new ParsePosition(0)); 
      paramJsonReader.nextNull();
      return null;
    } catch (ParseException parseException) {
      throw new JsonParseException(parseException);
    } 
  }
  
  public void write(JsonWriter paramJsonWriter, Date paramDate) throws IOException {
    if (paramDate == null) {
      paramJsonWriter.nullValue();
      return;
    } 
    paramJsonWriter.value(format(paramDate, true, this.UTC_TIME_ZONE));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\typeadapters\UtcDateTypeAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */