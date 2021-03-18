package com.google.gson.internal.bind.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ISO8601Utils {
  private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");
  
  private static final String UTC_ID = "UTC";
  
  private static boolean checkOffset(String paramString, int paramInt, char paramChar) {
    return (paramInt < paramString.length() && paramString.charAt(paramInt) == paramChar);
  }
  
  public static String format(Date paramDate) {
    return format(paramDate, false, TIMEZONE_UTC);
  }
  
  public static String format(Date paramDate, boolean paramBoolean) {
    return format(paramDate, paramBoolean, TIMEZONE_UTC);
  }
  
  public static String format(Date paramDate, boolean paramBoolean, TimeZone paramTimeZone) {
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
  
  private static int indexOfNonDigit(String paramString, int paramInt) {
    while (paramInt < paramString.length()) {
      char c = paramString.charAt(paramInt);
      if (c >= '0') {
        if (c > '9')
          return paramInt; 
        paramInt++;
        continue;
      } 
      return paramInt;
    } 
    return paramString.length();
  }
  
  private static void padInt(StringBuilder paramStringBuilder, int paramInt1, int paramInt2) {
    String str = Integer.toString(paramInt1);
    for (paramInt1 = paramInt2 - str.length(); paramInt1 > 0; paramInt1--)
      paramStringBuilder.append('0'); 
    paramStringBuilder.append(str);
  }
  
  public static Date parse(String paramString, ParsePosition paramParsePosition) throws ParseException {
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
    //   74: istore #5
    //   76: aload_0
    //   77: iload_3
    //   78: iload #5
    //   80: invokestatic parseInt : (Ljava/lang/String;II)I
    //   83: istore #11
    //   85: aload_0
    //   86: iload #5
    //   88: bipush #84
    //   90: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   93: istore #13
    //   95: iload #13
    //   97: ifne -> 138
    //   100: aload_0
    //   101: invokevirtual length : ()I
    //   104: iload #5
    //   106: if_icmpgt -> 138
    //   109: new java/util/GregorianCalendar
    //   112: dup
    //   113: iload #9
    //   115: iload #10
    //   117: iconst_1
    //   118: isub
    //   119: iload #11
    //   121: invokespecial <init> : (III)V
    //   124: astore #14
    //   126: aload_1
    //   127: iload #5
    //   129: invokevirtual setIndex : (I)V
    //   132: aload #14
    //   134: invokevirtual getTime : ()Ljava/util/Date;
    //   137: areturn
    //   138: iload #13
    //   140: ifeq -> 1025
    //   143: iload #5
    //   145: iconst_1
    //   146: iadd
    //   147: istore_3
    //   148: iload_3
    //   149: iconst_2
    //   150: iadd
    //   151: istore #4
    //   153: aload_0
    //   154: iload_3
    //   155: iload #4
    //   157: invokestatic parseInt : (Ljava/lang/String;II)I
    //   160: istore #5
    //   162: iload #4
    //   164: istore_3
    //   165: aload_0
    //   166: iload #4
    //   168: bipush #58
    //   170: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   173: ifeq -> 181
    //   176: iload #4
    //   178: iconst_1
    //   179: iadd
    //   180: istore_3
    //   181: iload_3
    //   182: iconst_2
    //   183: iadd
    //   184: istore #4
    //   186: aload_0
    //   187: iload_3
    //   188: iload #4
    //   190: invokestatic parseInt : (Ljava/lang/String;II)I
    //   193: istore #6
    //   195: iload #4
    //   197: istore_3
    //   198: aload_0
    //   199: iload #4
    //   201: bipush #58
    //   203: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   206: ifeq -> 214
    //   209: iload #4
    //   211: iconst_1
    //   212: iadd
    //   213: istore_3
    //   214: aload_0
    //   215: invokevirtual length : ()I
    //   218: iload_3
    //   219: if_icmple -> 1008
    //   222: aload_0
    //   223: iload_3
    //   224: invokevirtual charAt : (I)C
    //   227: istore #4
    //   229: iload #4
    //   231: bipush #90
    //   233: if_icmpeq -> 1008
    //   236: iload #4
    //   238: bipush #43
    //   240: if_icmpeq -> 1008
    //   243: iload #4
    //   245: bipush #45
    //   247: if_icmpeq -> 1008
    //   250: iload_3
    //   251: iconst_2
    //   252: iadd
    //   253: istore #7
    //   255: aload_0
    //   256: iload_3
    //   257: iload #7
    //   259: invokestatic parseInt : (Ljava/lang/String;II)I
    //   262: istore_3
    //   263: iload_3
    //   264: istore #4
    //   266: iload_3
    //   267: bipush #59
    //   269: if_icmple -> 285
    //   272: iload_3
    //   273: istore #4
    //   275: iload_3
    //   276: bipush #63
    //   278: if_icmpge -> 285
    //   281: bipush #59
    //   283: istore #4
    //   285: aload_0
    //   286: iload #7
    //   288: bipush #46
    //   290: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   293: ifeq -> 987
    //   296: iload #7
    //   298: iconst_1
    //   299: iadd
    //   300: istore #8
    //   302: aload_0
    //   303: iload #8
    //   305: iconst_1
    //   306: iadd
    //   307: invokestatic indexOfNonDigit : (Ljava/lang/String;I)I
    //   310: istore #7
    //   312: iload #7
    //   314: iload #8
    //   316: iconst_3
    //   317: iadd
    //   318: invokestatic min : (II)I
    //   321: istore #12
    //   323: aload_0
    //   324: iload #8
    //   326: iload #12
    //   328: invokestatic parseInt : (Ljava/lang/String;II)I
    //   331: istore_3
    //   332: iload #12
    //   334: iload #8
    //   336: isub
    //   337: istore #8
    //   339: iload #8
    //   341: iconst_1
    //   342: if_icmpeq -> 961
    //   345: iload #8
    //   347: iconst_2
    //   348: if_icmpeq -> 953
    //   351: goto -> 966
    //   354: aload_0
    //   355: invokevirtual length : ()I
    //   358: iload #5
    //   360: if_icmple -> 748
    //   363: aload_0
    //   364: iload #5
    //   366: invokevirtual charAt : (I)C
    //   369: istore_2
    //   370: iload_2
    //   371: bipush #90
    //   373: if_icmpne -> 1047
    //   376: getstatic com/google/gson/internal/bind/util/ISO8601Utils.TIMEZONE_UTC : Ljava/util/TimeZone;
    //   379: astore #14
    //   381: iload #5
    //   383: iconst_1
    //   384: iadd
    //   385: istore #5
    //   387: goto -> 658
    //   390: new java/lang/StringBuilder
    //   393: dup
    //   394: invokespecial <init> : ()V
    //   397: astore #14
    //   399: aload #14
    //   401: ldc 'Invalid time zone indicator ''
    //   403: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   406: pop
    //   407: aload #14
    //   409: iload_2
    //   410: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   413: pop
    //   414: aload #14
    //   416: ldc '''
    //   418: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   421: pop
    //   422: new java/lang/IndexOutOfBoundsException
    //   425: dup
    //   426: aload #14
    //   428: invokevirtual toString : ()Ljava/lang/String;
    //   431: invokespecial <init> : (Ljava/lang/String;)V
    //   434: athrow
    //   435: aload_0
    //   436: iload #5
    //   438: invokevirtual substring : (I)Ljava/lang/String;
    //   441: astore #14
    //   443: aload #14
    //   445: invokevirtual length : ()I
    //   448: iconst_5
    //   449: if_icmplt -> 455
    //   452: goto -> 487
    //   455: new java/lang/StringBuilder
    //   458: dup
    //   459: invokespecial <init> : ()V
    //   462: astore #15
    //   464: aload #15
    //   466: aload #14
    //   468: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   471: pop
    //   472: aload #15
    //   474: ldc '00'
    //   476: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   479: pop
    //   480: aload #15
    //   482: invokevirtual toString : ()Ljava/lang/String;
    //   485: astore #14
    //   487: iload #5
    //   489: aload #14
    //   491: invokevirtual length : ()I
    //   494: iadd
    //   495: istore #5
    //   497: ldc '+0000'
    //   499: aload #14
    //   501: invokevirtual equals : (Ljava/lang/Object;)Z
    //   504: ifne -> 653
    //   507: ldc '+00:00'
    //   509: aload #14
    //   511: invokevirtual equals : (Ljava/lang/Object;)Z
    //   514: ifeq -> 520
    //   517: goto -> 653
    //   520: new java/lang/StringBuilder
    //   523: dup
    //   524: invokespecial <init> : ()V
    //   527: astore #15
    //   529: aload #15
    //   531: ldc 'GMT'
    //   533: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   536: pop
    //   537: aload #15
    //   539: aload #14
    //   541: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   544: pop
    //   545: aload #15
    //   547: invokevirtual toString : ()Ljava/lang/String;
    //   550: astore #15
    //   552: aload #15
    //   554: invokestatic getTimeZone : (Ljava/lang/String;)Ljava/util/TimeZone;
    //   557: astore #14
    //   559: aload #14
    //   561: invokevirtual getID : ()Ljava/lang/String;
    //   564: astore #16
    //   566: aload #16
    //   568: aload #15
    //   570: invokevirtual equals : (Ljava/lang/Object;)Z
    //   573: ifne -> 1062
    //   576: aload #16
    //   578: ldc ':'
    //   580: ldc ''
    //   582: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   585: aload #15
    //   587: invokevirtual equals : (Ljava/lang/Object;)Z
    //   590: ifeq -> 596
    //   593: goto -> 1062
    //   596: new java/lang/StringBuilder
    //   599: dup
    //   600: invokespecial <init> : ()V
    //   603: astore #16
    //   605: aload #16
    //   607: ldc 'Mismatching time zone indicator: '
    //   609: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   612: pop
    //   613: aload #16
    //   615: aload #15
    //   617: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   620: pop
    //   621: aload #16
    //   623: ldc ' given, resolves to '
    //   625: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   628: pop
    //   629: aload #16
    //   631: aload #14
    //   633: invokevirtual getID : ()Ljava/lang/String;
    //   636: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   639: pop
    //   640: new java/lang/IndexOutOfBoundsException
    //   643: dup
    //   644: aload #16
    //   646: invokevirtual toString : ()Ljava/lang/String;
    //   649: invokespecial <init> : (Ljava/lang/String;)V
    //   652: athrow
    //   653: getstatic com/google/gson/internal/bind/util/ISO8601Utils.TIMEZONE_UTC : Ljava/util/TimeZone;
    //   656: astore #14
    //   658: new java/util/GregorianCalendar
    //   661: dup
    //   662: aload #14
    //   664: invokespecial <init> : (Ljava/util/TimeZone;)V
    //   667: astore #14
    //   669: aload #14
    //   671: iconst_0
    //   672: invokevirtual setLenient : (Z)V
    //   675: aload #14
    //   677: iconst_1
    //   678: iload #9
    //   680: invokevirtual set : (II)V
    //   683: aload #14
    //   685: iconst_2
    //   686: iload #10
    //   688: iconst_1
    //   689: isub
    //   690: invokevirtual set : (II)V
    //   693: aload #14
    //   695: iconst_5
    //   696: iload #11
    //   698: invokevirtual set : (II)V
    //   701: aload #14
    //   703: bipush #11
    //   705: iload_3
    //   706: invokevirtual set : (II)V
    //   709: aload #14
    //   711: bipush #12
    //   713: iload #7
    //   715: invokevirtual set : (II)V
    //   718: aload #14
    //   720: bipush #13
    //   722: iload #4
    //   724: invokevirtual set : (II)V
    //   727: aload #14
    //   729: bipush #14
    //   731: iload #6
    //   733: invokevirtual set : (II)V
    //   736: aload_1
    //   737: iload #5
    //   739: invokevirtual setIndex : (I)V
    //   742: aload #14
    //   744: invokevirtual getTime : ()Ljava/util/Date;
    //   747: areturn
    //   748: new java/lang/IllegalArgumentException
    //   751: dup
    //   752: ldc 'No time zone indicator'
    //   754: invokespecial <init> : (Ljava/lang/String;)V
    //   757: athrow
    //   758: astore #14
    //   760: goto -> 770
    //   763: astore #14
    //   765: goto -> 770
    //   768: astore #14
    //   770: aload_0
    //   771: ifnonnull -> 779
    //   774: aconst_null
    //   775: astore_0
    //   776: goto -> 817
    //   779: new java/lang/StringBuilder
    //   782: dup
    //   783: invokespecial <init> : ()V
    //   786: astore #15
    //   788: aload #15
    //   790: bipush #34
    //   792: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   795: pop
    //   796: aload #15
    //   798: aload_0
    //   799: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   802: pop
    //   803: aload #15
    //   805: ldc '''
    //   807: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   810: pop
    //   811: aload #15
    //   813: invokevirtual toString : ()Ljava/lang/String;
    //   816: astore_0
    //   817: aload #14
    //   819: invokevirtual getMessage : ()Ljava/lang/String;
    //   822: astore #16
    //   824: aload #16
    //   826: ifnull -> 841
    //   829: aload #16
    //   831: astore #15
    //   833: aload #16
    //   835: invokevirtual isEmpty : ()Z
    //   838: ifeq -> 887
    //   841: new java/lang/StringBuilder
    //   844: dup
    //   845: invokespecial <init> : ()V
    //   848: astore #15
    //   850: aload #15
    //   852: ldc '('
    //   854: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   857: pop
    //   858: aload #15
    //   860: aload #14
    //   862: invokevirtual getClass : ()Ljava/lang/Class;
    //   865: invokevirtual getName : ()Ljava/lang/String;
    //   868: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   871: pop
    //   872: aload #15
    //   874: ldc ')'
    //   876: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   879: pop
    //   880: aload #15
    //   882: invokevirtual toString : ()Ljava/lang/String;
    //   885: astore #15
    //   887: new java/lang/StringBuilder
    //   890: dup
    //   891: invokespecial <init> : ()V
    //   894: astore #16
    //   896: aload #16
    //   898: ldc 'Failed to parse date ['
    //   900: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   903: pop
    //   904: aload #16
    //   906: aload_0
    //   907: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   910: pop
    //   911: aload #16
    //   913: ldc ']: '
    //   915: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   918: pop
    //   919: aload #16
    //   921: aload #15
    //   923: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   926: pop
    //   927: new java/text/ParseException
    //   930: dup
    //   931: aload #16
    //   933: invokevirtual toString : ()Ljava/lang/String;
    //   936: aload_1
    //   937: invokevirtual getIndex : ()I
    //   940: invokespecial <init> : (Ljava/lang/String;I)V
    //   943: astore_0
    //   944: aload_0
    //   945: aload #14
    //   947: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   950: pop
    //   951: aload_0
    //   952: athrow
    //   953: iload_3
    //   954: bipush #10
    //   956: imul
    //   957: istore_3
    //   958: goto -> 966
    //   961: iload_3
    //   962: bipush #100
    //   964: imul
    //   965: istore_3
    //   966: iload_3
    //   967: istore #8
    //   969: iload #5
    //   971: istore_3
    //   972: iload #7
    //   974: istore #5
    //   976: iload #6
    //   978: istore #7
    //   980: iload #8
    //   982: istore #6
    //   984: goto -> 354
    //   987: iload #5
    //   989: istore_3
    //   990: iload #7
    //   992: istore #5
    //   994: iconst_0
    //   995: istore #8
    //   997: iload #6
    //   999: istore #7
    //   1001: iload #8
    //   1003: istore #6
    //   1005: goto -> 354
    //   1008: iload_3
    //   1009: istore #4
    //   1011: iload #5
    //   1013: istore_3
    //   1014: iload #4
    //   1016: istore #5
    //   1018: iload #6
    //   1020: istore #4
    //   1022: goto -> 1030
    //   1025: iconst_0
    //   1026: istore_3
    //   1027: iconst_0
    //   1028: istore #4
    //   1030: iconst_0
    //   1031: istore #6
    //   1033: iconst_0
    //   1034: istore #8
    //   1036: iload #4
    //   1038: istore #7
    //   1040: iload #8
    //   1042: istore #4
    //   1044: goto -> 354
    //   1047: iload_2
    //   1048: bipush #43
    //   1050: if_icmpeq -> 435
    //   1053: iload_2
    //   1054: bipush #45
    //   1056: if_icmpne -> 390
    //   1059: goto -> 435
    //   1062: goto -> 658
    // Exception table:
    //   from	to	target	type
    //   0	5	768	java/lang/IndexOutOfBoundsException
    //   0	5	763	java/lang/NumberFormatException
    //   0	5	758	java/lang/IllegalArgumentException
    //   10	19	768	java/lang/IndexOutOfBoundsException
    //   10	19	763	java/lang/NumberFormatException
    //   10	19	758	java/lang/IllegalArgumentException
    //   22	33	768	java/lang/IndexOutOfBoundsException
    //   22	33	763	java/lang/NumberFormatException
    //   22	33	758	java/lang/IllegalArgumentException
    //   43	52	768	java/lang/IndexOutOfBoundsException
    //   43	52	763	java/lang/NumberFormatException
    //   43	52	758	java/lang/IllegalArgumentException
    //   55	66	768	java/lang/IndexOutOfBoundsException
    //   55	66	763	java/lang/NumberFormatException
    //   55	66	758	java/lang/IllegalArgumentException
    //   76	95	768	java/lang/IndexOutOfBoundsException
    //   76	95	763	java/lang/NumberFormatException
    //   76	95	758	java/lang/IllegalArgumentException
    //   100	138	768	java/lang/IndexOutOfBoundsException
    //   100	138	763	java/lang/NumberFormatException
    //   100	138	758	java/lang/IllegalArgumentException
    //   153	162	768	java/lang/IndexOutOfBoundsException
    //   153	162	763	java/lang/NumberFormatException
    //   153	162	758	java/lang/IllegalArgumentException
    //   165	176	768	java/lang/IndexOutOfBoundsException
    //   165	176	763	java/lang/NumberFormatException
    //   165	176	758	java/lang/IllegalArgumentException
    //   186	195	768	java/lang/IndexOutOfBoundsException
    //   186	195	763	java/lang/NumberFormatException
    //   186	195	758	java/lang/IllegalArgumentException
    //   198	209	768	java/lang/IndexOutOfBoundsException
    //   198	209	763	java/lang/NumberFormatException
    //   198	209	758	java/lang/IllegalArgumentException
    //   214	229	768	java/lang/IndexOutOfBoundsException
    //   214	229	763	java/lang/NumberFormatException
    //   214	229	758	java/lang/IllegalArgumentException
    //   255	263	768	java/lang/IndexOutOfBoundsException
    //   255	263	763	java/lang/NumberFormatException
    //   255	263	758	java/lang/IllegalArgumentException
    //   285	296	768	java/lang/IndexOutOfBoundsException
    //   285	296	763	java/lang/NumberFormatException
    //   285	296	758	java/lang/IllegalArgumentException
    //   302	332	768	java/lang/IndexOutOfBoundsException
    //   302	332	763	java/lang/NumberFormatException
    //   302	332	758	java/lang/IllegalArgumentException
    //   354	370	768	java/lang/IndexOutOfBoundsException
    //   354	370	763	java/lang/NumberFormatException
    //   354	370	758	java/lang/IllegalArgumentException
    //   376	381	768	java/lang/IndexOutOfBoundsException
    //   376	381	763	java/lang/NumberFormatException
    //   376	381	758	java/lang/IllegalArgumentException
    //   390	435	768	java/lang/IndexOutOfBoundsException
    //   390	435	763	java/lang/NumberFormatException
    //   390	435	758	java/lang/IllegalArgumentException
    //   435	452	768	java/lang/IndexOutOfBoundsException
    //   435	452	763	java/lang/NumberFormatException
    //   435	452	758	java/lang/IllegalArgumentException
    //   455	487	768	java/lang/IndexOutOfBoundsException
    //   455	487	763	java/lang/NumberFormatException
    //   455	487	758	java/lang/IllegalArgumentException
    //   487	517	768	java/lang/IndexOutOfBoundsException
    //   487	517	763	java/lang/NumberFormatException
    //   487	517	758	java/lang/IllegalArgumentException
    //   520	593	768	java/lang/IndexOutOfBoundsException
    //   520	593	763	java/lang/NumberFormatException
    //   520	593	758	java/lang/IllegalArgumentException
    //   596	653	768	java/lang/IndexOutOfBoundsException
    //   596	653	763	java/lang/NumberFormatException
    //   596	653	758	java/lang/IllegalArgumentException
    //   653	658	768	java/lang/IndexOutOfBoundsException
    //   653	658	763	java/lang/NumberFormatException
    //   653	658	758	java/lang/IllegalArgumentException
    //   658	748	768	java/lang/IndexOutOfBoundsException
    //   658	748	763	java/lang/NumberFormatException
    //   658	748	758	java/lang/IllegalArgumentException
    //   748	758	768	java/lang/IndexOutOfBoundsException
    //   748	758	763	java/lang/NumberFormatException
    //   748	758	758	java/lang/IllegalArgumentException
  }
  
  private static int parseInt(String paramString, int paramInt1, int paramInt2) throws NumberFormatException {
    if (paramInt1 >= 0 && paramInt2 <= paramString.length() && paramInt1 <= paramInt2) {
      int i;
      int j;
      if (paramInt1 < paramInt2) {
        i = paramInt1 + 1;
        j = Character.digit(paramString.charAt(paramInt1), 10);
        if (j >= 0) {
          j = -j;
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Invalid number: ");
          stringBuilder.append(paramString.substring(paramInt1, paramInt2));
          throw new NumberFormatException(stringBuilder.toString());
        } 
      } else {
        j = 0;
        i = paramInt1;
      } 
      while (i < paramInt2) {
        int k = Character.digit(paramString.charAt(i), 10);
        if (k >= 0) {
          j = j * 10 - k;
          i++;
          continue;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid number: ");
        stringBuilder.append(paramString.substring(paramInt1, paramInt2));
        throw new NumberFormatException(stringBuilder.toString());
      } 
      return -j;
    } 
    throw new NumberFormatException(paramString);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\internal\bin\\util\ISO8601Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */