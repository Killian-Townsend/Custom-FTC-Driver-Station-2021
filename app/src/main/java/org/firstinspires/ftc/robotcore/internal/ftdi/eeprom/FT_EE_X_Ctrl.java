package org.firstinspires.ftc.robotcore.internal.ftdi.eeprom;

import org.firstinspires.ftc.robotcore.internal.ftdi.FtDevice;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class FT_EE_X_Ctrl extends FT_EE_Ctrl {
  private static final int BCD_ENABLE = 1;
  
  private static final int CBUS_DRIVE = 48;
  
  private static final int CBUS_SCHMITT = 128;
  
  private static final int CBUS_SLEW = 64;
  
  private static final int DBUS_DRIVE = 3;
  
  private static final int DBUS_SCHMITT = 8;
  
  private static final int DBUS_SLEW = 4;
  
  private static final int DEACTIVATE_SLEEP = 4;
  
  private static final String DEFAULT_PID = "6015";
  
  private static final int DEVICE_TYPE_EE_LOC = 73;
  
  private static final short EE_MAX_SIZE = 1024;
  
  private static final byte FIFO = 1;
  
  private static final int FORCE_POWER_ENABLE = 2;
  
  private static final byte FT1248 = 2;
  
  private static final int FT1248_BIT_ORDER = 32;
  
  private static final int FT1248_CLK_POLARITY = 16;
  
  private static final int FT1248_FLOW_CTRL = 64;
  
  private static final byte I2C = 3;
  
  private static final int I2C_DISABLE_SCHMITT = 128;
  
  private static final int INVERT_CTS = 2048;
  
  private static final int INVERT_DCD = 16384;
  
  private static final int INVERT_DSR = 8192;
  
  private static final int INVERT_DTR = 4096;
  
  private static final int INVERT_RI = 32768;
  
  private static final int INVERT_RTS = 1024;
  
  private static final int INVERT_RXD = 512;
  
  private static final int INVERT_TXD = 256;
  
  private static final int LOAD_DRIVER = 128;
  
  private static final int RS485_ECHO = 8;
  
  private static final byte UART = 0;
  
  private static final int VBUS_SUSPEND = 64;
  
  private static FtDevice ft_device;
  
  public FT_EE_X_Ctrl(FtDevice paramFtDevice) {
    super(paramFtDevice);
    ft_device = paramFtDevice;
    this.mEepromSize = 128;
    this.mEepromType = 1;
  }
  
  public int getUserSize() throws RobotUsbException {
    int j = readWord((short)9);
    int i = (j & 0xFF) / 2;
    j = ((j & 0xFF00) >> 8) / 2;
    return (this.mEepromSize - 1 - 1 - i + j + 1) * 2;
  }
  
  public short programEeprom(FT_EEPROM paramFT_EEPROM) throws RobotUsbException {
    // Byte code:
    //   0: aload_0
    //   1: getfield mEepromSize : I
    //   4: newarray int
    //   6: astore #8
    //   8: aload_1
    //   9: invokevirtual getClass : ()Ljava/lang/Class;
    //   12: ldc org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EEPROM_X_Series
    //   14: if_acmpeq -> 19
    //   17: iconst_1
    //   18: ireturn
    //   19: aload_1
    //   20: checkcast org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EEPROM_X_Series
    //   23: astore #9
    //   25: iconst_0
    //   26: istore_2
    //   27: aload #8
    //   29: iload_2
    //   30: aload_0
    //   31: iload_2
    //   32: invokevirtual readWord : (S)I
    //   35: iastore
    //   36: iload_2
    //   37: iconst_1
    //   38: iadd
    //   39: i2s
    //   40: istore_3
    //   41: iload_3
    //   42: istore_2
    //   43: iload_3
    //   44: aload_0
    //   45: getfield mEepromSize : I
    //   48: if_icmplt -> 27
    //   51: aload #8
    //   53: iconst_0
    //   54: iconst_0
    //   55: iastore
    //   56: aload #9
    //   58: getfield BCDEnable : Z
    //   61: ifeq -> 74
    //   64: aload #8
    //   66: iconst_0
    //   67: aload #8
    //   69: iconst_0
    //   70: iaload
    //   71: iconst_1
    //   72: ior
    //   73: iastore
    //   74: aload #9
    //   76: getfield BCDForceCBusPWREN : Z
    //   79: ifeq -> 92
    //   82: aload #8
    //   84: iconst_0
    //   85: aload #8
    //   87: iconst_0
    //   88: iaload
    //   89: iconst_2
    //   90: ior
    //   91: iastore
    //   92: aload #9
    //   94: getfield BCDDisableSleep : Z
    //   97: ifeq -> 110
    //   100: aload #8
    //   102: iconst_0
    //   103: aload #8
    //   105: iconst_0
    //   106: iaload
    //   107: iconst_4
    //   108: ior
    //   109: iastore
    //   110: aload #9
    //   112: getfield RS485EchoSuppress : Z
    //   115: ifeq -> 129
    //   118: aload #8
    //   120: iconst_0
    //   121: aload #8
    //   123: iconst_0
    //   124: iaload
    //   125: bipush #8
    //   127: ior
    //   128: iastore
    //   129: aload #9
    //   131: getfield A_LoadVCP : Z
    //   134: ifeq -> 149
    //   137: aload #8
    //   139: iconst_0
    //   140: aload #8
    //   142: iconst_0
    //   143: iaload
    //   144: sipush #128
    //   147: ior
    //   148: iastore
    //   149: aload #9
    //   151: getfield PowerSaveEnable : Z
    //   154: ifeq -> 254
    //   157: aload #9
    //   159: getfield CBus0 : B
    //   162: bipush #17
    //   164: if_icmpne -> 1022
    //   167: iconst_1
    //   168: istore #4
    //   170: goto -> 173
    //   173: aload #9
    //   175: getfield CBus1 : B
    //   178: bipush #17
    //   180: if_icmpne -> 186
    //   183: iconst_1
    //   184: istore #4
    //   186: aload #9
    //   188: getfield CBus2 : B
    //   191: bipush #17
    //   193: if_icmpne -> 199
    //   196: iconst_1
    //   197: istore #4
    //   199: aload #9
    //   201: getfield CBus3 : B
    //   204: bipush #17
    //   206: if_icmpne -> 212
    //   209: iconst_1
    //   210: istore #4
    //   212: aload #9
    //   214: getfield CBus4 : B
    //   217: bipush #17
    //   219: if_icmpne -> 225
    //   222: iconst_1
    //   223: istore #4
    //   225: aload #9
    //   227: getfield CBus5 : B
    //   230: bipush #17
    //   232: if_icmpne -> 238
    //   235: iconst_1
    //   236: istore #4
    //   238: aload #9
    //   240: getfield CBus6 : B
    //   243: bipush #17
    //   245: if_icmpne -> 1028
    //   248: iconst_1
    //   249: istore #4
    //   251: goto -> 1028
    //   254: aload #8
    //   256: iconst_1
    //   257: aload #9
    //   259: getfield VendorId : S
    //   262: iastore
    //   263: aload #8
    //   265: iconst_2
    //   266: aload #9
    //   268: getfield ProductId : S
    //   271: iastore
    //   272: aload #8
    //   274: iconst_3
    //   275: sipush #4096
    //   278: iastore
    //   279: aload #8
    //   281: iconst_4
    //   282: aload_0
    //   283: aload_1
    //   284: invokevirtual setUSBConfig : (Ljava/lang/Object;)I
    //   287: iastore
    //   288: aload #8
    //   290: iconst_5
    //   291: aload_0
    //   292: aload_1
    //   293: invokevirtual setDeviceControl : (Ljava/lang/Object;)I
    //   296: iastore
    //   297: aload #9
    //   299: getfield FT1248ClockPolarity : Z
    //   302: ifeq -> 316
    //   305: aload #8
    //   307: iconst_5
    //   308: aload #8
    //   310: iconst_5
    //   311: iaload
    //   312: bipush #16
    //   314: ior
    //   315: iastore
    //   316: aload #9
    //   318: getfield FT1248LSB : Z
    //   321: ifeq -> 335
    //   324: aload #8
    //   326: iconst_5
    //   327: aload #8
    //   329: iconst_5
    //   330: iaload
    //   331: bipush #32
    //   333: ior
    //   334: iastore
    //   335: aload #9
    //   337: getfield FT1248FlowControl : Z
    //   340: ifeq -> 354
    //   343: aload #8
    //   345: iconst_5
    //   346: aload #8
    //   348: iconst_5
    //   349: iaload
    //   350: bipush #64
    //   352: ior
    //   353: iastore
    //   354: aload #9
    //   356: getfield I2CDisableSchmitt : Z
    //   359: ifeq -> 374
    //   362: aload #8
    //   364: iconst_5
    //   365: aload #8
    //   367: iconst_5
    //   368: iaload
    //   369: sipush #128
    //   372: ior
    //   373: iastore
    //   374: aload #9
    //   376: getfield InvertTXD : Z
    //   379: ifeq -> 394
    //   382: aload #8
    //   384: iconst_5
    //   385: aload #8
    //   387: iconst_5
    //   388: iaload
    //   389: sipush #256
    //   392: ior
    //   393: iastore
    //   394: aload #9
    //   396: getfield InvertRXD : Z
    //   399: ifeq -> 414
    //   402: aload #8
    //   404: iconst_5
    //   405: aload #8
    //   407: iconst_5
    //   408: iaload
    //   409: sipush #512
    //   412: ior
    //   413: iastore
    //   414: aload #9
    //   416: getfield InvertRTS : Z
    //   419: ifeq -> 434
    //   422: aload #8
    //   424: iconst_5
    //   425: aload #8
    //   427: iconst_5
    //   428: iaload
    //   429: sipush #1024
    //   432: ior
    //   433: iastore
    //   434: aload #9
    //   436: getfield InvertCTS : Z
    //   439: ifeq -> 454
    //   442: aload #8
    //   444: iconst_5
    //   445: aload #8
    //   447: iconst_5
    //   448: iaload
    //   449: sipush #2048
    //   452: ior
    //   453: iastore
    //   454: aload #9
    //   456: getfield InvertDTR : Z
    //   459: ifeq -> 474
    //   462: aload #8
    //   464: iconst_5
    //   465: aload #8
    //   467: iconst_5
    //   468: iaload
    //   469: sipush #4096
    //   472: ior
    //   473: iastore
    //   474: aload #9
    //   476: getfield InvertDSR : Z
    //   479: ifeq -> 494
    //   482: aload #8
    //   484: iconst_5
    //   485: aload #8
    //   487: iconst_5
    //   488: iaload
    //   489: sipush #8192
    //   492: ior
    //   493: iastore
    //   494: aload #9
    //   496: getfield InvertDCD : Z
    //   499: ifeq -> 514
    //   502: aload #8
    //   504: iconst_5
    //   505: aload #8
    //   507: iconst_5
    //   508: iaload
    //   509: sipush #16384
    //   512: ior
    //   513: iastore
    //   514: aload #9
    //   516: getfield InvertRI : Z
    //   519: ifeq -> 533
    //   522: aload #8
    //   524: iconst_5
    //   525: aload #8
    //   527: iconst_5
    //   528: iaload
    //   529: ldc 32768
    //   531: ior
    //   532: iastore
    //   533: aload #8
    //   535: bipush #6
    //   537: iconst_0
    //   538: iastore
    //   539: aload #9
    //   541: getfield AD_DriveCurrent : B
    //   544: istore #5
    //   546: iload #5
    //   548: istore #4
    //   550: iload #5
    //   552: iconst_m1
    //   553: if_icmpne -> 559
    //   556: iconst_0
    //   557: istore #4
    //   559: aload #8
    //   561: bipush #6
    //   563: iload #4
    //   565: aload #8
    //   567: bipush #6
    //   569: iaload
    //   570: ior
    //   571: iastore
    //   572: aload #9
    //   574: getfield AD_SlowSlew : Z
    //   577: ifeq -> 592
    //   580: aload #8
    //   582: bipush #6
    //   584: aload #8
    //   586: bipush #6
    //   588: iaload
    //   589: iconst_4
    //   590: ior
    //   591: iastore
    //   592: aload #9
    //   594: getfield AD_SchmittInput : Z
    //   597: ifeq -> 613
    //   600: aload #8
    //   602: bipush #6
    //   604: aload #8
    //   606: bipush #6
    //   608: iaload
    //   609: bipush #8
    //   611: ior
    //   612: iastore
    //   613: aload #9
    //   615: getfield AC_DriveCurrent : B
    //   618: istore #5
    //   620: iload #5
    //   622: istore #4
    //   624: iload #5
    //   626: iconst_m1
    //   627: if_icmpne -> 633
    //   630: iconst_0
    //   631: istore #4
    //   633: aload #8
    //   635: bipush #6
    //   637: iload #4
    //   639: iconst_4
    //   640: ishl
    //   641: i2s
    //   642: aload #8
    //   644: bipush #6
    //   646: iaload
    //   647: ior
    //   648: iastore
    //   649: aload #9
    //   651: getfield AC_SlowSlew : Z
    //   654: ifeq -> 670
    //   657: aload #8
    //   659: bipush #6
    //   661: aload #8
    //   663: bipush #6
    //   665: iaload
    //   666: bipush #64
    //   668: ior
    //   669: iastore
    //   670: aload #9
    //   672: getfield AC_SchmittInput : Z
    //   675: ifeq -> 692
    //   678: aload #8
    //   680: bipush #6
    //   682: aload #8
    //   684: bipush #6
    //   686: iaload
    //   687: sipush #128
    //   690: ior
    //   691: iastore
    //   692: aload_0
    //   693: aload #9
    //   695: getfield Manufacturer : Ljava/lang/String;
    //   698: aload #8
    //   700: bipush #80
    //   702: bipush #7
    //   704: iconst_0
    //   705: invokevirtual setStringDescriptor : (Ljava/lang/String;[IIIZ)I
    //   708: istore #4
    //   710: aload_0
    //   711: aload #9
    //   713: getfield Product : Ljava/lang/String;
    //   716: aload #8
    //   718: iload #4
    //   720: bipush #8
    //   722: iconst_0
    //   723: invokevirtual setStringDescriptor : (Ljava/lang/String;[IIIZ)I
    //   726: istore #4
    //   728: aload #9
    //   730: getfield SerNumEnable : Z
    //   733: ifeq -> 753
    //   736: aload_0
    //   737: aload #9
    //   739: getfield SerialNumber : Ljava/lang/String;
    //   742: aload #8
    //   744: iload #4
    //   746: bipush #9
    //   748: iconst_0
    //   749: invokevirtual setStringDescriptor : (Ljava/lang/String;[IIIZ)I
    //   752: pop
    //   753: aload #8
    //   755: bipush #10
    //   757: aload #9
    //   759: getfield I2CSlaveAddress : I
    //   762: iastore
    //   763: aload #8
    //   765: bipush #11
    //   767: aload #9
    //   769: getfield I2CDeviceID : I
    //   772: ldc 65535
    //   774: iand
    //   775: iastore
    //   776: aload #8
    //   778: bipush #12
    //   780: aload #9
    //   782: getfield I2CDeviceID : I
    //   785: bipush #16
    //   787: ishr
    //   788: iastore
    //   789: aload #9
    //   791: getfield CBus0 : B
    //   794: istore #5
    //   796: iload #5
    //   798: istore #4
    //   800: iload #5
    //   802: iconst_m1
    //   803: if_icmpne -> 809
    //   806: iconst_0
    //   807: istore #4
    //   809: aload #9
    //   811: getfield CBus1 : B
    //   814: istore #6
    //   816: iload #6
    //   818: istore #5
    //   820: iload #6
    //   822: iconst_m1
    //   823: if_icmpne -> 829
    //   826: iconst_0
    //   827: istore #5
    //   829: aload #8
    //   831: bipush #13
    //   833: iload #4
    //   835: iload #5
    //   837: bipush #8
    //   839: ishl
    //   840: ior
    //   841: i2s
    //   842: iastore
    //   843: aload #9
    //   845: getfield CBus2 : B
    //   848: istore #5
    //   850: iload #5
    //   852: istore #4
    //   854: iload #5
    //   856: iconst_m1
    //   857: if_icmpne -> 863
    //   860: iconst_0
    //   861: istore #4
    //   863: aload #9
    //   865: getfield CBus3 : B
    //   868: istore #6
    //   870: iload #6
    //   872: istore #5
    //   874: iload #6
    //   876: iconst_m1
    //   877: if_icmpne -> 883
    //   880: iconst_0
    //   881: istore #5
    //   883: aload #8
    //   885: bipush #14
    //   887: iload #4
    //   889: iload #5
    //   891: bipush #8
    //   893: ishl
    //   894: ior
    //   895: i2s
    //   896: iastore
    //   897: aload #9
    //   899: getfield CBus4 : B
    //   902: istore #5
    //   904: iload #5
    //   906: istore #4
    //   908: iload #5
    //   910: iconst_m1
    //   911: if_icmpne -> 917
    //   914: iconst_0
    //   915: istore #4
    //   917: aload #9
    //   919: getfield CBus5 : B
    //   922: istore #6
    //   924: iload #6
    //   926: istore #5
    //   928: iload #6
    //   930: iconst_m1
    //   931: if_icmpne -> 937
    //   934: iconst_0
    //   935: istore #5
    //   937: aload #8
    //   939: bipush #15
    //   941: iload #4
    //   943: iload #5
    //   945: bipush #8
    //   947: ishl
    //   948: ior
    //   949: i2s
    //   950: iastore
    //   951: aload #9
    //   953: getfield CBus6 : B
    //   956: istore #5
    //   958: iload #5
    //   960: istore #4
    //   962: iload #5
    //   964: iconst_m1
    //   965: if_icmpne -> 971
    //   968: iconst_0
    //   969: istore #4
    //   971: aload #8
    //   973: bipush #16
    //   975: iload #4
    //   977: i2s
    //   978: iastore
    //   979: aload #8
    //   981: iconst_1
    //   982: iaload
    //   983: ifeq -> 1013
    //   986: aload #8
    //   988: iconst_2
    //   989: iaload
    //   990: ifeq -> 1013
    //   993: aload_0
    //   994: aload #8
    //   996: aload_0
    //   997: getfield mEepromSize : I
    //   1000: iconst_1
    //   1001: isub
    //   1002: invokevirtual programXeeprom : ([II)Z
    //   1005: istore #7
    //   1007: iload #7
    //   1009: iconst_1
    //   1010: ixor
    //   1011: i2s
    //   1012: ireturn
    //   1013: iconst_2
    //   1014: ireturn
    //   1015: astore_1
    //   1016: aload_1
    //   1017: invokevirtual printStackTrace : ()V
    //   1020: iconst_0
    //   1021: ireturn
    //   1022: iconst_0
    //   1023: istore #4
    //   1025: goto -> 173
    //   1028: iload #4
    //   1030: ifne -> 1035
    //   1033: iconst_1
    //   1034: ireturn
    //   1035: aload #8
    //   1037: iconst_0
    //   1038: aload #8
    //   1040: iconst_0
    //   1041: iaload
    //   1042: bipush #64
    //   1044: ior
    //   1045: iastore
    //   1046: goto -> 254
    // Exception table:
    //   from	to	target	type
    //   56	64	1015	java/lang/Exception
    //   74	82	1015	java/lang/Exception
    //   92	100	1015	java/lang/Exception
    //   110	118	1015	java/lang/Exception
    //   129	137	1015	java/lang/Exception
    //   149	167	1015	java/lang/Exception
    //   173	183	1015	java/lang/Exception
    //   186	196	1015	java/lang/Exception
    //   199	209	1015	java/lang/Exception
    //   212	222	1015	java/lang/Exception
    //   225	235	1015	java/lang/Exception
    //   238	248	1015	java/lang/Exception
    //   254	272	1015	java/lang/Exception
    //   279	305	1015	java/lang/Exception
    //   316	324	1015	java/lang/Exception
    //   335	343	1015	java/lang/Exception
    //   354	362	1015	java/lang/Exception
    //   374	382	1015	java/lang/Exception
    //   394	402	1015	java/lang/Exception
    //   414	422	1015	java/lang/Exception
    //   434	442	1015	java/lang/Exception
    //   454	462	1015	java/lang/Exception
    //   474	482	1015	java/lang/Exception
    //   494	502	1015	java/lang/Exception
    //   514	522	1015	java/lang/Exception
    //   539	546	1015	java/lang/Exception
    //   572	580	1015	java/lang/Exception
    //   592	600	1015	java/lang/Exception
    //   613	620	1015	java/lang/Exception
    //   649	657	1015	java/lang/Exception
    //   670	678	1015	java/lang/Exception
    //   692	753	1015	java/lang/Exception
    //   753	796	1015	java/lang/Exception
    //   809	816	1015	java/lang/Exception
    //   843	850	1015	java/lang/Exception
    //   863	870	1015	java/lang/Exception
    //   897	904	1015	java/lang/Exception
    //   917	924	1015	java/lang/Exception
    //   951	958	1015	java/lang/Exception
    //   993	1007	1015	java/lang/Exception
  }
  
  boolean programXeeprom(int[] paramArrayOfint, int paramInt) throws RobotUsbException {
    int j = 43690;
    int i = 0;
    while (true) {
      int k = paramArrayOfint[i] & 0xFFFF;
      writeWord((short)i, (short)k);
      j = (j ^ k) & 0xFFFF;
      if ((j & 0x8000) > 0) {
        k = 1;
      } else {
        k = 0;
      } 
      int m = (k | j << 1 & 0xFFFF) & 0xFFFF;
      k = ++i;
      if (i == 18)
        k = 64; 
      j = m;
      i = k;
      if (k == paramInt) {
        writeWord((short)paramInt, (short)m);
        return true;
      } 
    } 
  }
  
  public FT_EEPROM readEeprom() {
    FT_EEPROM_X_Series fT_EEPROM_X_Series = new FT_EEPROM_X_Series();
    int[] arrayOfInt = new int[this.mEepromSize];
    short s = 0;
    try {
      while (s < this.mEepromSize) {
        arrayOfInt[s] = readWord(s);
        s = (short)(s + 1);
      } 
      if ((arrayOfInt[0] & 0x1) > 0) {
        fT_EEPROM_X_Series.BCDEnable = true;
      } else {
        fT_EEPROM_X_Series.BCDEnable = false;
      } 
      if ((arrayOfInt[0] & 0x2) > 0) {
        fT_EEPROM_X_Series.BCDForceCBusPWREN = true;
      } else {
        fT_EEPROM_X_Series.BCDForceCBusPWREN = false;
      } 
      if ((arrayOfInt[0] & 0x4) > 0) {
        fT_EEPROM_X_Series.BCDDisableSleep = true;
      } else {
        fT_EEPROM_X_Series.BCDDisableSleep = false;
      } 
      if ((arrayOfInt[0] & 0x8) > 0) {
        fT_EEPROM_X_Series.RS485EchoSuppress = true;
      } else {
        fT_EEPROM_X_Series.RS485EchoSuppress = false;
      } 
      if ((arrayOfInt[0] & 0x40) > 0) {
        fT_EEPROM_X_Series.PowerSaveEnable = true;
      } else {
        fT_EEPROM_X_Series.PowerSaveEnable = false;
      } 
      if ((arrayOfInt[0] & 0x80) > 0) {
        fT_EEPROM_X_Series.A_LoadVCP = true;
        fT_EEPROM_X_Series.A_LoadD2XX = false;
      } else {
        fT_EEPROM_X_Series.A_LoadVCP = false;
        fT_EEPROM_X_Series.A_LoadD2XX = true;
      } 
      fT_EEPROM_X_Series.VendorId = (short)arrayOfInt[1];
      fT_EEPROM_X_Series.ProductId = (short)arrayOfInt[2];
      getUSBConfig(fT_EEPROM_X_Series, arrayOfInt[4]);
      getDeviceControl(fT_EEPROM_X_Series, arrayOfInt[5]);
      if ((arrayOfInt[5] & 0x10) > 0) {
        fT_EEPROM_X_Series.FT1248ClockPolarity = true;
      } else {
        fT_EEPROM_X_Series.FT1248ClockPolarity = false;
      } 
      if ((arrayOfInt[5] & 0x20) > 0) {
        fT_EEPROM_X_Series.FT1248LSB = true;
      } else {
        fT_EEPROM_X_Series.FT1248LSB = false;
      } 
      if ((arrayOfInt[5] & 0x40) > 0) {
        fT_EEPROM_X_Series.FT1248FlowControl = true;
      } else {
        fT_EEPROM_X_Series.FT1248FlowControl = false;
      } 
      if ((arrayOfInt[5] & 0x80) > 0) {
        fT_EEPROM_X_Series.I2CDisableSchmitt = true;
      } else {
        fT_EEPROM_X_Series.I2CDisableSchmitt = false;
      } 
      if ((arrayOfInt[5] & 0x100) == 256) {
        fT_EEPROM_X_Series.InvertTXD = true;
      } else {
        fT_EEPROM_X_Series.InvertTXD = false;
      } 
      if ((arrayOfInt[5] & 0x200) == 512) {
        fT_EEPROM_X_Series.InvertRXD = true;
      } else {
        fT_EEPROM_X_Series.InvertRXD = false;
      } 
      if ((arrayOfInt[5] & 0x400) == 1024) {
        fT_EEPROM_X_Series.InvertRTS = true;
      } else {
        fT_EEPROM_X_Series.InvertRTS = false;
      } 
      if ((arrayOfInt[5] & 0x800) == 2048) {
        fT_EEPROM_X_Series.InvertCTS = true;
      } else {
        fT_EEPROM_X_Series.InvertCTS = false;
      } 
      if ((arrayOfInt[5] & 0x1000) == 4096) {
        fT_EEPROM_X_Series.InvertDTR = true;
      } else {
        fT_EEPROM_X_Series.InvertDTR = false;
      } 
      if ((arrayOfInt[5] & 0x2000) == 8192) {
        fT_EEPROM_X_Series.InvertDSR = true;
      } else {
        fT_EEPROM_X_Series.InvertDSR = false;
      } 
      if ((arrayOfInt[5] & 0x4000) == 16384) {
        fT_EEPROM_X_Series.InvertDCD = true;
      } else {
        fT_EEPROM_X_Series.InvertDCD = false;
      } 
      if ((arrayOfInt[5] & 0x8000) == 32768) {
        fT_EEPROM_X_Series.InvertRI = true;
      } else {
        fT_EEPROM_X_Series.InvertRI = false;
      } 
    } catch (Exception exception) {
      return null;
    } 
    short s1 = (short)(arrayOfInt[6] & 0x3);
    if (s1 != 0) {
      if (s1 != 1) {
        if (s1 != 2) {
          if (s1 == 3)
            ((FT_EEPROM_X_Series)exception).AD_DriveCurrent = 3; 
        } else {
          ((FT_EEPROM_X_Series)exception).AD_DriveCurrent = 2;
        } 
      } else {
        ((FT_EEPROM_X_Series)exception).AD_DriveCurrent = 1;
      } 
    } else {
      ((FT_EEPROM_X_Series)exception).AD_DriveCurrent = 0;
    } 
    if ((short)(arrayOfInt[6] & 0x4) == 4) {
      ((FT_EEPROM_X_Series)exception).AD_SlowSlew = true;
    } else {
      ((FT_EEPROM_X_Series)exception).AD_SlowSlew = false;
    } 
    if ((short)(arrayOfInt[6] & 0x8) == 8) {
      ((FT_EEPROM_X_Series)exception).AD_SchmittInput = true;
    } else {
      ((FT_EEPROM_X_Series)exception).AD_SchmittInput = false;
    } 
    s1 = (short)((arrayOfInt[6] & 0x30) >> 4);
    if (s1 != 0) {
      if (s1 != 1) {
        if (s1 != 2) {
          if (s1 == 3)
            ((FT_EEPROM_X_Series)exception).AC_DriveCurrent = 3; 
        } else {
          ((FT_EEPROM_X_Series)exception).AC_DriveCurrent = 2;
        } 
      } else {
        ((FT_EEPROM_X_Series)exception).AC_DriveCurrent = 1;
      } 
    } else {
      ((FT_EEPROM_X_Series)exception).AC_DriveCurrent = 0;
    } 
    if ((short)(arrayOfInt[6] & 0x40) == 64) {
      ((FT_EEPROM_X_Series)exception).AC_SlowSlew = true;
    } else {
      ((FT_EEPROM_X_Series)exception).AC_SlowSlew = false;
    } 
    if ((short)(arrayOfInt[6] & 0x80) == 128) {
      ((FT_EEPROM_X_Series)exception).AC_SchmittInput = true;
    } else {
      ((FT_EEPROM_X_Series)exception).AC_SchmittInput = false;
    } 
    ((FT_EEPROM_X_Series)exception).I2CSlaveAddress = arrayOfInt[10];
    ((FT_EEPROM_X_Series)exception).I2CDeviceID = arrayOfInt[11];
    ((FT_EEPROM_X_Series)exception).I2CDeviceID |= (arrayOfInt[12] & 0xFF) << 16;
    ((FT_EEPROM_X_Series)exception).CBus0 = (byte)(arrayOfInt[13] & 0xFF);
    ((FT_EEPROM_X_Series)exception).CBus1 = (byte)(arrayOfInt[13] >> 8 & 0xFF);
    ((FT_EEPROM_X_Series)exception).CBus2 = (byte)(arrayOfInt[14] & 0xFF);
    ((FT_EEPROM_X_Series)exception).CBus3 = (byte)(arrayOfInt[14] >> 8 & 0xFF);
    ((FT_EEPROM_X_Series)exception).CBus4 = (byte)(arrayOfInt[15] & 0xFF);
    ((FT_EEPROM_X_Series)exception).CBus5 = (byte)(arrayOfInt[15] >> 8 & 0xFF);
    ((FT_EEPROM_X_Series)exception).CBus6 = (byte)(arrayOfInt[16] & 0xFF);
    this.mEepromType = (short)(arrayOfInt[73] >> 8);
    ((FT_EEPROM_X_Series)exception).Manufacturer = getStringDescriptor((arrayOfInt[7] & 0xFF) / 2, arrayOfInt);
    ((FT_EEPROM_X_Series)exception).Product = getStringDescriptor((arrayOfInt[8] & 0xFF) / 2, arrayOfInt);
    ((FT_EEPROM_X_Series)exception).SerialNumber = getStringDescriptor((arrayOfInt[9] & 0xFF) / 2, arrayOfInt);
    return (FT_EEPROM)exception;
  }
  
  public byte[] readUserData(int paramInt) throws RobotUsbException {
    byte[] arrayOfByte = new byte[paramInt];
    if (paramInt != 0 && paramInt <= getUserSize()) {
      short s = (short)(this.mEepromSize - getUserSize() / 2 - 1 - 1);
      int i = 0;
      while (i < paramInt) {
        short s1 = (short)(s + 1);
        int j = readWord(s);
        int k = i + 1;
        if (k < paramInt)
          arrayOfByte[k] = (byte)(j & 0xFF); 
        arrayOfByte[i] = (byte)((j & 0xFF00) >> 8);
        i += 2;
        s = s1;
      } 
      return arrayOfByte;
    } 
    return null;
  }
  
  public int writeUserData(byte[] paramArrayOfbyte) throws RobotUsbException {
    if (paramArrayOfbyte.length > getUserSize())
      return 0; 
    int[] arrayOfInt = new int[this.mEepromSize];
    for (short s = 0; s < this.mEepromSize; s = (short)(s + 1))
      arrayOfInt[s] = readWord(s); 
    short s1 = (short)(this.mEepromSize - getUserSize() / 2 - 1 - 1);
    int i = 0;
    while (i < paramArrayOfbyte.length) {
      int j = i + 1;
      if (j < paramArrayOfbyte.length) {
        j = paramArrayOfbyte[j] & 0xFF;
      } else {
        j = 0;
      } 
      byte b = paramArrayOfbyte[i];
      short s2 = (short)(s1 + 1);
      arrayOfInt[s1] = j << 8 | b & 0xFF;
      i += 2;
      s1 = s2;
    } 
    return (arrayOfInt[1] != 0 && arrayOfInt[2] != 0) ? (!programXeeprom(arrayOfInt, this.mEepromSize - 1) ? 0 : paramArrayOfbyte.length) : 0;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\eeprom\FT_EE_X_Ctrl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */