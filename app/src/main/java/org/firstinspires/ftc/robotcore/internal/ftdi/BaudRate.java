package org.firstinspires.ftc.robotcore.internal.ftdi;

public final class BaudRate extends FtConstants {
  private static int calcBaudRate(int paramInt1, int paramInt2, boolean paramBoolean) {
    // Byte code:
    //   0: iload_0
    //   1: ifne -> 7
    //   4: ldc 3000000
    //   6: ireturn
    //   7: ldc -49153
    //   9: iload_0
    //   10: iand
    //   11: bipush #100
    //   13: imul
    //   14: istore_3
    //   15: iload_2
    //   16: ifne -> 72
    //   19: iload_0
    //   20: ldc 49152
    //   22: iand
    //   23: istore_0
    //   24: iload_0
    //   25: sipush #16384
    //   28: if_icmpeq -> 64
    //   31: iload_0
    //   32: ldc 32768
    //   34: if_icmpeq -> 56
    //   37: iload_0
    //   38: ldc 49152
    //   40: if_icmpeq -> 48
    //   43: iload_3
    //   44: istore_0
    //   45: goto -> 167
    //   48: iload_3
    //   49: bipush #12
    //   51: iadd
    //   52: istore_0
    //   53: goto -> 167
    //   56: iload_3
    //   57: bipush #25
    //   59: iadd
    //   60: istore_0
    //   61: goto -> 167
    //   64: iload_3
    //   65: bipush #50
    //   67: iadd
    //   68: istore_0
    //   69: goto -> 167
    //   72: iload_1
    //   73: ifne -> 105
    //   76: iload_0
    //   77: ldc 49152
    //   79: iand
    //   80: istore_0
    //   81: iload_0
    //   82: sipush #16384
    //   85: if_icmpeq -> 64
    //   88: iload_0
    //   89: ldc 32768
    //   91: if_icmpeq -> 56
    //   94: iload_0
    //   95: ldc 49152
    //   97: if_icmpeq -> 48
    //   100: iload_3
    //   101: istore_0
    //   102: goto -> 167
    //   105: iload_0
    //   106: ldc 49152
    //   108: iand
    //   109: istore_0
    //   110: iload_0
    //   111: ifeq -> 162
    //   114: iload_0
    //   115: sipush #16384
    //   118: if_icmpeq -> 154
    //   121: iload_0
    //   122: ldc 32768
    //   124: if_icmpeq -> 146
    //   127: iload_0
    //   128: ldc 49152
    //   130: if_icmpeq -> 138
    //   133: iload_3
    //   134: istore_0
    //   135: goto -> 167
    //   138: iload_3
    //   139: bipush #87
    //   141: iadd
    //   142: istore_0
    //   143: goto -> 167
    //   146: iload_3
    //   147: bipush #75
    //   149: iadd
    //   150: istore_0
    //   151: goto -> 167
    //   154: iload_3
    //   155: bipush #62
    //   157: iadd
    //   158: istore_0
    //   159: goto -> 167
    //   162: iload_3
    //   163: bipush #37
    //   165: iadd
    //   166: istore_0
    //   167: ldc 300000000
    //   169: iload_0
    //   170: idiv
    //   171: ireturn
  }
  
  private static int calcBaudRateHi(int paramInt1, int paramInt2) {
    if (paramInt1 == 0)
      return 12000000; 
    if (paramInt1 == 1)
      return 8000000; 
    int i = (0xFFFF3FFF & paramInt1) * 100;
    if ((paramInt2 & 0xFFFD) == 0) {
      paramInt1 &= 0xC000;
      if (paramInt1 != 16384) {
        if (paramInt1 != 32768) {
          if (paramInt1 != 49152) {
            paramInt1 = i;
          } else {
            paramInt1 = i + 12;
          } 
        } else {
          paramInt1 = i + 25;
        } 
      } else {
        paramInt1 = i + 50;
      } 
    } else {
      paramInt1 &= 0xC000;
      if (paramInt1 != 0) {
        if (paramInt1 != 16384) {
          if (paramInt1 != 32768) {
            if (paramInt1 != 49152) {
              paramInt1 = i;
            } else {
              paramInt1 = i + 87;
            } 
          } else {
            paramInt1 = i + 75;
          } 
        } else {
          paramInt1 = i + 62;
        } 
      } else {
        paramInt1 = i + 37;
      } 
    } 
    return 1200000000 / paramInt1;
  }
  
  private static byte calcDivisor(int paramInt, int[] paramArrayOfint, boolean paramBoolean) {
    // Byte code:
    //   0: iload_0
    //   1: ifne -> 6
    //   4: iconst_m1
    //   5: ireturn
    //   6: ldc 3000000
    //   8: iload_0
    //   9: idiv
    //   10: istore #4
    //   12: iload #4
    //   14: sipush #-16384
    //   17: iand
    //   18: ifle -> 23
    //   21: iconst_m1
    //   22: ireturn
    //   23: aload_1
    //   24: iconst_0
    //   25: iload #4
    //   27: iastore
    //   28: iconst_1
    //   29: istore_3
    //   30: aload_1
    //   31: iconst_1
    //   32: iconst_0
    //   33: iastore
    //   34: aload_1
    //   35: iconst_0
    //   36: iaload
    //   37: iconst_1
    //   38: if_icmpne -> 58
    //   41: ldc 3000000
    //   43: iload_0
    //   44: irem
    //   45: bipush #100
    //   47: imul
    //   48: iload_0
    //   49: idiv
    //   50: iconst_3
    //   51: if_icmpgt -> 58
    //   54: aload_1
    //   55: iconst_0
    //   56: iconst_0
    //   57: iastore
    //   58: aload_1
    //   59: iconst_0
    //   60: iaload
    //   61: ifne -> 66
    //   64: iconst_1
    //   65: ireturn
    //   66: ldc 3000000
    //   68: iload_0
    //   69: irem
    //   70: bipush #100
    //   72: imul
    //   73: iload_0
    //   74: idiv
    //   75: istore #4
    //   77: sipush #16384
    //   80: istore_0
    //   81: iload_2
    //   82: ifne -> 132
    //   85: iload #4
    //   87: bipush #6
    //   89: if_icmpgt -> 95
    //   92: goto -> 139
    //   95: iload #4
    //   97: bipush #18
    //   99: if_icmpgt -> 105
    //   102: goto -> 151
    //   105: iload #4
    //   107: bipush #37
    //   109: if_icmpgt -> 115
    //   112: goto -> 164
    //   115: iload #4
    //   117: bipush #75
    //   119: if_icmpgt -> 125
    //   122: goto -> 236
    //   125: iconst_0
    //   126: istore_3
    //   127: iload_3
    //   128: istore_0
    //   129: goto -> 236
    //   132: iload #4
    //   134: bipush #6
    //   136: if_icmpgt -> 144
    //   139: iconst_0
    //   140: istore_0
    //   141: goto -> 236
    //   144: iload #4
    //   146: bipush #18
    //   148: if_icmpgt -> 157
    //   151: ldc 49152
    //   153: istore_0
    //   154: goto -> 236
    //   157: iload #4
    //   159: bipush #31
    //   161: if_icmpgt -> 170
    //   164: ldc 32768
    //   166: istore_0
    //   167: goto -> 236
    //   170: iload #4
    //   172: bipush #43
    //   174: if_icmpgt -> 184
    //   177: aload_1
    //   178: iconst_1
    //   179: iconst_1
    //   180: iastore
    //   181: goto -> 139
    //   184: iload #4
    //   186: bipush #56
    //   188: if_icmpgt -> 194
    //   191: goto -> 236
    //   194: iload #4
    //   196: bipush #68
    //   198: if_icmpgt -> 208
    //   201: aload_1
    //   202: iconst_1
    //   203: iconst_1
    //   204: iastore
    //   205: goto -> 236
    //   208: iload #4
    //   210: bipush #81
    //   212: if_icmpgt -> 222
    //   215: aload_1
    //   216: iconst_1
    //   217: iconst_1
    //   218: iastore
    //   219: goto -> 164
    //   222: iload #4
    //   224: bipush #93
    //   226: if_icmpgt -> 125
    //   229: aload_1
    //   230: iconst_1
    //   231: iconst_1
    //   232: iastore
    //   233: goto -> 151
    //   236: aload_1
    //   237: iconst_0
    //   238: aload_1
    //   239: iconst_0
    //   240: iaload
    //   241: iload_0
    //   242: ior
    //   243: iastore
    //   244: iload_3
    //   245: ireturn
  }
  
  private static byte calcDivisorHi(int paramInt, int[] paramArrayOfint) {
    // Byte code:
    //   0: iload_0
    //   1: ifne -> 6
    //   4: iconst_m1
    //   5: ireturn
    //   6: ldc 12000000
    //   8: iload_0
    //   9: idiv
    //   10: istore_3
    //   11: iload_3
    //   12: sipush #-16384
    //   15: iand
    //   16: ifle -> 21
    //   19: iconst_m1
    //   20: ireturn
    //   21: iconst_1
    //   22: istore_2
    //   23: aload_1
    //   24: iconst_1
    //   25: iconst_2
    //   26: iastore
    //   27: iload_0
    //   28: ldc 11640000
    //   30: if_icmplt -> 45
    //   33: iload_0
    //   34: ldc 12360000
    //   36: if_icmpgt -> 45
    //   39: aload_1
    //   40: iconst_0
    //   41: iconst_0
    //   42: iastore
    //   43: iconst_1
    //   44: ireturn
    //   45: iload_0
    //   46: ldc 7760000
    //   48: if_icmplt -> 63
    //   51: iload_0
    //   52: ldc 8240000
    //   54: if_icmpgt -> 63
    //   57: aload_1
    //   58: iconst_0
    //   59: iconst_1
    //   60: iastore
    //   61: iconst_1
    //   62: ireturn
    //   63: aload_1
    //   64: iconst_0
    //   65: iload_3
    //   66: iastore
    //   67: aload_1
    //   68: iconst_1
    //   69: iconst_2
    //   70: iastore
    //   71: aload_1
    //   72: iconst_0
    //   73: iaload
    //   74: iconst_1
    //   75: if_icmpne -> 95
    //   78: ldc 12000000
    //   80: iload_0
    //   81: irem
    //   82: bipush #100
    //   84: imul
    //   85: iload_0
    //   86: idiv
    //   87: iconst_3
    //   88: if_icmpgt -> 95
    //   91: aload_1
    //   92: iconst_0
    //   93: iconst_0
    //   94: iastore
    //   95: aload_1
    //   96: iconst_0
    //   97: iaload
    //   98: ifne -> 103
    //   101: iconst_1
    //   102: ireturn
    //   103: ldc 12000000
    //   105: iload_0
    //   106: irem
    //   107: bipush #100
    //   109: imul
    //   110: iload_0
    //   111: idiv
    //   112: istore_3
    //   113: sipush #16384
    //   116: istore_0
    //   117: iload_3
    //   118: bipush #6
    //   120: if_icmpgt -> 128
    //   123: iconst_0
    //   124: istore_0
    //   125: goto -> 233
    //   128: iload_3
    //   129: bipush #18
    //   131: if_icmpgt -> 140
    //   134: ldc 49152
    //   136: istore_0
    //   137: goto -> 233
    //   140: iload_3
    //   141: bipush #31
    //   143: if_icmpgt -> 152
    //   146: ldc 32768
    //   148: istore_0
    //   149: goto -> 233
    //   152: iload_3
    //   153: bipush #43
    //   155: if_icmpgt -> 169
    //   158: aload_1
    //   159: iconst_1
    //   160: aload_1
    //   161: iconst_1
    //   162: iaload
    //   163: iconst_1
    //   164: ior
    //   165: iastore
    //   166: goto -> 123
    //   169: iload_3
    //   170: bipush #56
    //   172: if_icmpgt -> 178
    //   175: goto -> 233
    //   178: iload_3
    //   179: bipush #68
    //   181: if_icmpgt -> 195
    //   184: aload_1
    //   185: iconst_1
    //   186: aload_1
    //   187: iconst_1
    //   188: iaload
    //   189: iconst_1
    //   190: ior
    //   191: iastore
    //   192: goto -> 233
    //   195: iload_3
    //   196: bipush #81
    //   198: if_icmpgt -> 212
    //   201: aload_1
    //   202: iconst_1
    //   203: aload_1
    //   204: iconst_1
    //   205: iaload
    //   206: iconst_1
    //   207: ior
    //   208: iastore
    //   209: goto -> 146
    //   212: iload_3
    //   213: bipush #93
    //   215: if_icmpgt -> 229
    //   218: aload_1
    //   219: iconst_1
    //   220: aload_1
    //   221: iconst_1
    //   222: iaload
    //   223: iconst_1
    //   224: ior
    //   225: iastore
    //   226: goto -> 134
    //   229: iconst_0
    //   230: istore_2
    //   231: iload_2
    //   232: istore_0
    //   233: aload_1
    //   234: iconst_0
    //   235: aload_1
    //   236: iconst_0
    //   237: iaload
    //   238: iload_0
    //   239: ior
    //   240: iastore
    //   241: iload_2
    //   242: ireturn
  }
  
  public static byte getDivisor(int paramInt, int[] paramArrayOfint, boolean paramBoolean) {
    int i = calcDivisor(paramInt, paramArrayOfint, paramBoolean);
    if (i == -1)
      return -1; 
    if (i == 0)
      paramArrayOfint[0] = (paramArrayOfint[0] & 0xFFFF3FFF) + 1; 
    int j = calcBaudRate(paramArrayOfint[0], paramArrayOfint[1], paramBoolean);
    if (paramInt > j) {
      int k = paramInt * 100 / j - 100;
      paramInt = paramInt % j * 100 % j;
    } else {
      i = j * 100 / paramInt - 100;
      paramInt = j % paramInt * 100 % paramInt;
    } 
    return (i < 3) ? 1 : ((i == 3 && paramInt == 0) ? 1 : 0);
  }
  
  public static byte getDivisorHi(int paramInt, int[] paramArrayOfint) {
    int i = calcDivisorHi(paramInt, paramArrayOfint);
    if (i == -1)
      return -1; 
    if (i == 0)
      paramArrayOfint[0] = (paramArrayOfint[0] & 0xFFFF3FFF) + 1; 
    int j = calcBaudRateHi(paramArrayOfint[0], paramArrayOfint[1]);
    if (paramInt > j) {
      int k = paramInt * 100 / j - 100;
      int m = paramInt % j * 100 % j;
      paramInt = k;
      k = m;
    } else {
      int k = j * 100 / paramInt - 100;
      i = j % paramInt * 100 % paramInt;
      paramInt = k;
    } 
    return (paramInt < 3) ? 1 : ((paramInt == 3 && i == 0) ? 1 : 0);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\BaudRate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */