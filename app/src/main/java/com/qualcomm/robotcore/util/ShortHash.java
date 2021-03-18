package com.qualcomm.robotcore.util;

import android.util.Log;

public class ShortHash {
  private static final String DEFAULT_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
  
  private static final int DEFAULT_MIN_HASH_LENGTH = 0;
  
  private static final String DEFAULT_SALT = "";
  
  private static final String DEFAULT_SEPS = "cfhistuCFHISTU";
  
  private static final int GUARD_DIV = 12;
  
  public static final long MAX_NUMBER = 9007199254740992L;
  
  private static final int MIN_ALPHABET_LENGTH = 16;
  
  private static final double SEP_DIV = 3.5D;
  
  private static final String TAG = "ShortHash";
  
  private final String alphabet;
  
  private final String guards;
  
  private final int minHashLength;
  
  private final String salt;
  
  private final String seps;
  
  public ShortHash() {
    this("");
  }
  
  public ShortHash(String paramString) {
    this(paramString, 0);
  }
  
  public ShortHash(String paramString, int paramInt) {
    this(paramString, paramInt, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
  }
  
  public ShortHash(String paramString1, int paramInt, String paramString2) {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial <init> : ()V
    //   4: aload_1
    //   5: ifnull -> 11
    //   8: goto -> 14
    //   11: ldc ''
    //   13: astore_1
    //   14: aload_0
    //   15: aload_1
    //   16: putfield salt : Ljava/lang/String;
    //   19: iload_2
    //   20: ifle -> 26
    //   23: goto -> 28
    //   26: iconst_0
    //   27: istore_2
    //   28: aload_0
    //   29: iload_2
    //   30: putfield minHashLength : I
    //   33: new java/lang/StringBuilder
    //   36: dup
    //   37: invokespecial <init> : ()V
    //   40: astore_1
    //   41: iconst_0
    //   42: istore_2
    //   43: iload_2
    //   44: aload_3
    //   45: invokevirtual length : ()I
    //   48: if_icmpge -> 84
    //   51: aload_1
    //   52: aload_3
    //   53: iload_2
    //   54: invokevirtual charAt : (I)C
    //   57: invokestatic valueOf : (C)Ljava/lang/String;
    //   60: invokevirtual indexOf : (Ljava/lang/String;)I
    //   63: iconst_m1
    //   64: if_icmpne -> 77
    //   67: aload_1
    //   68: aload_3
    //   69: iload_2
    //   70: invokevirtual charAt : (I)C
    //   73: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   76: pop
    //   77: iload_2
    //   78: iconst_1
    //   79: iadd
    //   80: istore_2
    //   81: goto -> 43
    //   84: aload_1
    //   85: invokevirtual toString : ()Ljava/lang/String;
    //   88: astore_3
    //   89: aload_3
    //   90: invokevirtual length : ()I
    //   93: bipush #16
    //   95: if_icmplt -> 506
    //   98: aload_3
    //   99: ldc ' '
    //   101: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   104: ifne -> 496
    //   107: ldc 'cfhistuCFHISTU'
    //   109: astore_1
    //   110: iconst_0
    //   111: istore_2
    //   112: iload_2
    //   113: aload_1
    //   114: invokevirtual length : ()I
    //   117: if_icmpge -> 245
    //   120: aload_3
    //   121: aload_1
    //   122: iload_2
    //   123: invokevirtual charAt : (I)C
    //   126: invokevirtual indexOf : (I)I
    //   129: istore #4
    //   131: iload #4
    //   133: iconst_m1
    //   134: if_icmpne -> 188
    //   137: new java/lang/StringBuilder
    //   140: dup
    //   141: invokespecial <init> : ()V
    //   144: astore #5
    //   146: aload #5
    //   148: aload_1
    //   149: iconst_0
    //   150: iload_2
    //   151: invokevirtual substring : (II)Ljava/lang/String;
    //   154: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   157: pop
    //   158: aload #5
    //   160: ldc ' '
    //   162: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   165: pop
    //   166: aload #5
    //   168: aload_1
    //   169: iload_2
    //   170: iconst_1
    //   171: iadd
    //   172: invokevirtual substring : (I)Ljava/lang/String;
    //   175: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   178: pop
    //   179: aload #5
    //   181: invokevirtual toString : ()Ljava/lang/String;
    //   184: astore_1
    //   185: goto -> 238
    //   188: new java/lang/StringBuilder
    //   191: dup
    //   192: invokespecial <init> : ()V
    //   195: astore #5
    //   197: aload #5
    //   199: aload_3
    //   200: iconst_0
    //   201: iload #4
    //   203: invokevirtual substring : (II)Ljava/lang/String;
    //   206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: pop
    //   210: aload #5
    //   212: ldc ' '
    //   214: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: pop
    //   218: aload #5
    //   220: aload_3
    //   221: iload #4
    //   223: iconst_1
    //   224: iadd
    //   225: invokevirtual substring : (I)Ljava/lang/String;
    //   228: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   231: pop
    //   232: aload #5
    //   234: invokevirtual toString : ()Ljava/lang/String;
    //   237: astore_3
    //   238: iload_2
    //   239: iconst_1
    //   240: iadd
    //   241: istore_2
    //   242: goto -> 112
    //   245: aload_3
    //   246: ldc '\s+'
    //   248: ldc ''
    //   250: invokevirtual replaceAll : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   253: astore #5
    //   255: aload_1
    //   256: ldc '\s+'
    //   258: ldc ''
    //   260: invokevirtual replaceAll : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   263: aload_0
    //   264: getfield salt : Ljava/lang/String;
    //   267: invokestatic consistentShuffle : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   270: astore #6
    //   272: aload #6
    //   274: invokevirtual isEmpty : ()Z
    //   277: ifne -> 307
    //   280: aload #5
    //   282: astore_3
    //   283: aload #6
    //   285: astore_1
    //   286: aload #5
    //   288: invokevirtual length : ()I
    //   291: i2f
    //   292: aload #6
    //   294: invokevirtual length : ()I
    //   297: i2f
    //   298: fdiv
    //   299: f2d
    //   300: ldc2_w 3.5
    //   303: dcmpl
    //   304: ifle -> 407
    //   307: aload #5
    //   309: invokevirtual length : ()I
    //   312: i2d
    //   313: ldc2_w 3.5
    //   316: ddiv
    //   317: invokestatic ceil : (D)D
    //   320: d2i
    //   321: istore #4
    //   323: iload #4
    //   325: istore_2
    //   326: iload #4
    //   328: iconst_1
    //   329: if_icmpne -> 337
    //   332: iload #4
    //   334: iconst_1
    //   335: iadd
    //   336: istore_2
    //   337: iload_2
    //   338: aload #6
    //   340: invokevirtual length : ()I
    //   343: if_icmple -> 396
    //   346: iload_2
    //   347: aload #6
    //   349: invokevirtual length : ()I
    //   352: isub
    //   353: istore_2
    //   354: new java/lang/StringBuilder
    //   357: dup
    //   358: invokespecial <init> : ()V
    //   361: astore_1
    //   362: aload_1
    //   363: aload #6
    //   365: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   368: pop
    //   369: aload_1
    //   370: aload #5
    //   372: iconst_0
    //   373: iload_2
    //   374: invokevirtual substring : (II)Ljava/lang/String;
    //   377: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   380: pop
    //   381: aload_1
    //   382: invokevirtual toString : ()Ljava/lang/String;
    //   385: astore_1
    //   386: aload #5
    //   388: iload_2
    //   389: invokevirtual substring : (I)Ljava/lang/String;
    //   392: astore_3
    //   393: goto -> 407
    //   396: aload #6
    //   398: iconst_0
    //   399: iload_2
    //   400: invokevirtual substring : (II)Ljava/lang/String;
    //   403: astore_1
    //   404: aload #5
    //   406: astore_3
    //   407: aload_3
    //   408: aload_0
    //   409: getfield salt : Ljava/lang/String;
    //   412: invokestatic consistentShuffle : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   415: astore_3
    //   416: aload_3
    //   417: invokevirtual length : ()I
    //   420: i2d
    //   421: ldc2_w 12.0
    //   424: ddiv
    //   425: invokestatic ceil : (D)D
    //   428: d2i
    //   429: istore_2
    //   430: aload_3
    //   431: invokevirtual length : ()I
    //   434: iconst_3
    //   435: if_icmpge -> 459
    //   438: aload_1
    //   439: iconst_0
    //   440: iload_2
    //   441: invokevirtual substring : (II)Ljava/lang/String;
    //   444: astore #6
    //   446: aload_1
    //   447: iload_2
    //   448: invokevirtual substring : (I)Ljava/lang/String;
    //   451: astore #5
    //   453: aload #6
    //   455: astore_1
    //   456: goto -> 479
    //   459: aload_3
    //   460: iconst_0
    //   461: iload_2
    //   462: invokevirtual substring : (II)Ljava/lang/String;
    //   465: astore #6
    //   467: aload_3
    //   468: iload_2
    //   469: invokevirtual substring : (I)Ljava/lang/String;
    //   472: astore_3
    //   473: aload_1
    //   474: astore #5
    //   476: aload #6
    //   478: astore_1
    //   479: aload_0
    //   480: aload_1
    //   481: putfield guards : Ljava/lang/String;
    //   484: aload_0
    //   485: aload_3
    //   486: putfield alphabet : Ljava/lang/String;
    //   489: aload_0
    //   490: aload #5
    //   492: putfield seps : Ljava/lang/String;
    //   495: return
    //   496: new java/lang/IllegalArgumentException
    //   499: dup
    //   500: ldc 'alphabet cannot contains spaces'
    //   502: invokespecial <init> : (Ljava/lang/String;)V
    //   505: athrow
    //   506: new java/lang/IllegalArgumentException
    //   509: dup
    //   510: ldc 'alphabet must contain at least 16 unique characters'
    //   512: invokespecial <init> : (Ljava/lang/String;)V
    //   515: athrow
  }
  
  private String _encode(long paramLong) {
    String str = this.alphabet;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.salt);
    stringBuilder.append(str);
    return hash(paramLong, consistentShuffle(str, stringBuilder.toString().substring(0, str.length())));
  }
  
  private static String consistentShuffle(String paramString1, String paramString2) {
    if (paramString2.length() <= 0)
      return paramString1; 
    char[] arrayOfChar = paramString1.toCharArray();
    int i = arrayOfChar.length - 1;
    int k = 0;
    int j = 0;
    while (i > 0) {
      k %= paramString2.length();
      char c2 = paramString2.charAt(k);
      j += c2;
      int m = (c2 + k + j) % i;
      char c1 = arrayOfChar[m];
      arrayOfChar[m] = arrayOfChar[i];
      arrayOfChar[i] = c1;
      i--;
      k++;
    } 
    return new String(arrayOfChar);
  }
  
  private static String hash(long paramLong, String paramString) {
    int i = paramString.length();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Alphabet length ");
    stringBuilder.append(i);
    Log.i("ShortHash", stringBuilder.toString());
    String str = "";
    while (true) {
      long l = i;
      int j = (int)(paramLong % l);
      String str1 = str;
      if (j >= 0) {
        str1 = str;
        if (j < paramString.length()) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append(paramString.charAt(j));
          stringBuilder1.append(str);
          str1 = stringBuilder1.toString();
        } 
      } 
      l = paramLong / l;
      str = str1;
      paramLong = l;
      if (l <= 0L)
        return str1; 
    } 
  }
  
  public String encode(long paramLong) {
    return _encode(paramLong);
  }
  
  public int getAlphabetLength() {
    return this.alphabet.length();
  }
  
  public String getVersion() {
    return "1.0.0";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\ShortHash.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */