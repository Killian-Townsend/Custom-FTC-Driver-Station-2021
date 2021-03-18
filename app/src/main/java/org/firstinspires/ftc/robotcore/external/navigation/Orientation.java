package org.firstinspires.ftc.robotcore.external.navigation;

import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class Orientation {
  public long acquisitionTime;
  
  public AngleUnit angleUnit;
  
  public AxesOrder axesOrder;
  
  public AxesReference axesReference;
  
  public float firstAngle;
  
  public float secondAngle;
  
  public float thirdAngle;
  
  public Orientation() {
    this(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS, 0.0F, 0.0F, 0.0F, 0L);
  }
  
  public Orientation(AxesReference paramAxesReference, AxesOrder paramAxesOrder, AngleUnit paramAngleUnit, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong) {
    this.axesReference = paramAxesReference;
    this.axesOrder = paramAxesOrder;
    this.angleUnit = paramAngleUnit;
    this.firstAngle = paramFloat1;
    this.secondAngle = paramFloat2;
    this.thirdAngle = paramFloat3;
    this.acquisitionTime = paramLong;
  }
  
  public static Orientation getOrientation(MatrixF paramMatrixF, AxesReference paramAxesReference, AxesOrder paramAxesOrder, AngleUnit paramAngleUnit) {
    Orientation orientation2 = getOrientation(paramMatrixF, paramAxesReference, paramAxesOrder, paramAngleUnit, AngleSet.THEONE);
    Orientation orientation1 = getOrientation(paramMatrixF, paramAxesReference, paramAxesOrder, paramAngleUnit, AngleSet.THEOTHER);
    VectorF vectorF1 = new VectorF(orientation2.firstAngle, orientation2.secondAngle, orientation2.thirdAngle);
    VectorF vectorF2 = new VectorF(orientation1.firstAngle, orientation1.secondAngle, orientation1.thirdAngle);
    return (vectorF1.magnitude() <= vectorF2.magnitude()) ? orientation2 : orientation1;
  }
  
  public static Orientation getOrientation(MatrixF paramMatrixF, AxesReference paramAxesReference, AxesOrder paramAxesOrder, AngleUnit paramAngleUnit, AngleSet paramAngleSet) {
    // Byte code:
    //   0: aload_1
    //   1: getstatic org/firstinspires/ftc/robotcore/external/navigation/AxesReference.INTRINSIC : Lorg/firstinspires/ftc/robotcore/external/navigation/AxesReference;
    //   4: if_acmpne -> 27
    //   7: aload_0
    //   8: aload_1
    //   9: invokevirtual reverse : ()Lorg/firstinspires/ftc/robotcore/external/navigation/AxesReference;
    //   12: aload_2
    //   13: invokevirtual reverse : ()Lorg/firstinspires/ftc/robotcore/external/navigation/AxesOrder;
    //   16: aload_3
    //   17: aload #4
    //   19: invokestatic getOrientation : (Lorg/firstinspires/ftc/robotcore/external/matrices/MatrixF;Lorg/firstinspires/ftc/robotcore/external/navigation/AxesReference;Lorg/firstinspires/ftc/robotcore/external/navigation/AxesOrder;Lorg/firstinspires/ftc/robotcore/external/navigation/AngleUnit;Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet;)Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation;
    //   22: aload_1
    //   23: invokevirtual toAxesReference : (Lorg/firstinspires/ftc/robotcore/external/navigation/AxesReference;)Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation;
    //   26: areturn
    //   27: getstatic org/firstinspires/ftc/robotcore/external/navigation/Orientation$1.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AxesOrder : [I
    //   30: aload_2
    //   31: invokevirtual ordinal : ()I
    //   34: iaload
    //   35: istore #13
    //   37: ldc -1.5707964
    //   39: fstore #9
    //   41: fconst_0
    //   42: fstore #10
    //   44: fconst_0
    //   45: fstore #11
    //   47: iload #13
    //   49: tableswitch default -> 108, 2 -> 2098, 3 -> 1910, 4 -> 1721, 5 -> 1533, 6 -> 1344, 7 -> 1120, 8 -> 924, 9 -> 732, 10 -> 540, 11 -> 348, 12 -> 156
    //   108: aload_0
    //   109: iconst_0
    //   110: iconst_0
    //   111: invokevirtual get : (II)F
    //   114: fstore #9
    //   116: fload #9
    //   118: fconst_1
    //   119: fcmpl
    //   120: ifne -> 2287
    //   123: aload_0
    //   124: iconst_2
    //   125: iconst_1
    //   126: invokevirtual get : (II)F
    //   129: f2d
    //   130: aload_0
    //   131: iconst_1
    //   132: iconst_1
    //   133: invokevirtual get : (II)F
    //   136: f2d
    //   137: invokestatic atan2 : (DD)D
    //   140: dstore #5
    //   142: dload #5
    //   144: fconst_0
    //   145: f2d
    //   146: dsub
    //   147: d2f
    //   148: fstore #11
    //   150: fconst_0
    //   151: fstore #9
    //   153: goto -> 2450
    //   156: aload_0
    //   157: iconst_1
    //   158: iconst_2
    //   159: invokevirtual get : (II)F
    //   162: fstore #12
    //   164: fload #12
    //   166: ldc -1.0
    //   168: fcmpl
    //   169: ifne -> 198
    //   172: fconst_0
    //   173: f2d
    //   174: dstore #5
    //   176: aload_0
    //   177: iconst_2
    //   178: iconst_0
    //   179: invokevirtual get : (II)F
    //   182: f2d
    //   183: aload_0
    //   184: iconst_0
    //   185: iconst_0
    //   186: invokevirtual get : (II)F
    //   189: f2d
    //   190: invokestatic atan2 : (DD)D
    //   193: dstore #7
    //   195: goto -> 1158
    //   198: fload #12
    //   200: fconst_1
    //   201: fcmpl
    //   202: ifne -> 228
    //   205: aload_0
    //   206: iconst_0
    //   207: iconst_1
    //   208: invokevirtual get : (II)F
    //   211: fneg
    //   212: f2d
    //   213: aload_0
    //   214: iconst_0
    //   215: iconst_0
    //   216: invokevirtual get : (II)F
    //   219: f2d
    //   220: invokestatic atan2 : (DD)D
    //   223: dstore #5
    //   225: goto -> 993
    //   228: aload #4
    //   230: getstatic org/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet.THEONE : Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet;
    //   233: if_acmpne -> 252
    //   236: aload_0
    //   237: iconst_1
    //   238: iconst_2
    //   239: invokevirtual get : (II)F
    //   242: f2d
    //   243: invokestatic asin : (D)D
    //   246: dneg
    //   247: dstore #5
    //   249: goto -> 268
    //   252: aload_0
    //   253: iconst_1
    //   254: iconst_2
    //   255: invokevirtual get : (II)F
    //   258: f2d
    //   259: invokestatic asin : (D)D
    //   262: ldc2_w 3.141592653589793
    //   265: dadd
    //   266: dstore #5
    //   268: dload #5
    //   270: d2f
    //   271: fstore #9
    //   273: aload_0
    //   274: iconst_1
    //   275: iconst_0
    //   276: invokevirtual get : (II)F
    //   279: f2d
    //   280: dstore #5
    //   282: fload #9
    //   284: f2d
    //   285: dstore #7
    //   287: dload #5
    //   289: dload #7
    //   291: invokestatic cos : (D)D
    //   294: ddiv
    //   295: aload_0
    //   296: iconst_1
    //   297: iconst_1
    //   298: invokevirtual get : (II)F
    //   301: f2d
    //   302: dload #7
    //   304: invokestatic cos : (D)D
    //   307: ddiv
    //   308: invokestatic atan2 : (DD)D
    //   311: d2f
    //   312: fstore #10
    //   314: aload_0
    //   315: iconst_0
    //   316: iconst_2
    //   317: invokevirtual get : (II)F
    //   320: f2d
    //   321: dload #7
    //   323: invokestatic cos : (D)D
    //   326: ddiv
    //   327: aload_0
    //   328: iconst_2
    //   329: iconst_2
    //   330: invokevirtual get : (II)F
    //   333: f2d
    //   334: dload #7
    //   336: invokestatic cos : (D)D
    //   339: ddiv
    //   340: invokestatic atan2 : (DD)D
    //   343: dstore #5
    //   345: goto -> 1215
    //   348: aload_0
    //   349: iconst_0
    //   350: iconst_2
    //   351: invokevirtual get : (II)F
    //   354: fstore #12
    //   356: fload #12
    //   358: fconst_1
    //   359: fcmpl
    //   360: ifne -> 385
    //   363: aload_0
    //   364: iconst_1
    //   365: iconst_0
    //   366: invokevirtual get : (II)F
    //   369: f2d
    //   370: aload_0
    //   371: iconst_1
    //   372: iconst_1
    //   373: invokevirtual get : (II)F
    //   376: f2d
    //   377: invokestatic atan2 : (DD)D
    //   380: dstore #5
    //   382: goto -> 1154
    //   385: fload #12
    //   387: ldc -1.0
    //   389: fcmpl
    //   390: ifne -> 419
    //   393: fconst_0
    //   394: f2d
    //   395: dstore #5
    //   397: aload_0
    //   398: iconst_1
    //   399: iconst_0
    //   400: invokevirtual get : (II)F
    //   403: f2d
    //   404: aload_0
    //   405: iconst_1
    //   406: iconst_1
    //   407: invokevirtual get : (II)F
    //   410: f2d
    //   411: invokestatic atan2 : (DD)D
    //   414: dstore #7
    //   416: goto -> 1204
    //   419: aload #4
    //   421: getstatic org/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet.THEONE : Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet;
    //   424: if_acmpne -> 442
    //   427: aload_0
    //   428: iconst_0
    //   429: iconst_2
    //   430: invokevirtual get : (II)F
    //   433: f2d
    //   434: invokestatic asin : (D)D
    //   437: dstore #5
    //   439: goto -> 458
    //   442: ldc2_w 3.141592653589793
    //   445: aload_0
    //   446: iconst_0
    //   447: iconst_2
    //   448: invokevirtual get : (II)F
    //   451: f2d
    //   452: invokestatic asin : (D)D
    //   455: dsub
    //   456: dstore #5
    //   458: dload #5
    //   460: d2f
    //   461: fstore #9
    //   463: aload_0
    //   464: iconst_0
    //   465: iconst_1
    //   466: invokevirtual get : (II)F
    //   469: fneg
    //   470: f2d
    //   471: dstore #5
    //   473: fload #9
    //   475: f2d
    //   476: dstore #7
    //   478: dload #5
    //   480: dload #7
    //   482: invokestatic cos : (D)D
    //   485: ddiv
    //   486: aload_0
    //   487: iconst_0
    //   488: iconst_0
    //   489: invokevirtual get : (II)F
    //   492: f2d
    //   493: dload #7
    //   495: invokestatic cos : (D)D
    //   498: ddiv
    //   499: invokestatic atan2 : (DD)D
    //   502: d2f
    //   503: fstore #10
    //   505: aload_0
    //   506: iconst_1
    //   507: iconst_2
    //   508: invokevirtual get : (II)F
    //   511: fneg
    //   512: f2d
    //   513: dload #7
    //   515: invokestatic cos : (D)D
    //   518: ddiv
    //   519: aload_0
    //   520: iconst_2
    //   521: iconst_2
    //   522: invokevirtual get : (II)F
    //   525: f2d
    //   526: dload #7
    //   528: invokestatic cos : (D)D
    //   531: ddiv
    //   532: invokestatic atan2 : (DD)D
    //   535: dstore #5
    //   537: goto -> 1215
    //   540: aload_0
    //   541: iconst_0
    //   542: iconst_1
    //   543: invokevirtual get : (II)F
    //   546: fstore #12
    //   548: fload #12
    //   550: ldc -1.0
    //   552: fcmpl
    //   553: ifne -> 582
    //   556: fconst_0
    //   557: f2d
    //   558: dstore #5
    //   560: aload_0
    //   561: iconst_1
    //   562: iconst_2
    //   563: invokevirtual get : (II)F
    //   566: f2d
    //   567: aload_0
    //   568: iconst_1
    //   569: iconst_0
    //   570: invokevirtual get : (II)F
    //   573: f2d
    //   574: invokestatic atan2 : (DD)D
    //   577: dstore #7
    //   579: goto -> 1158
    //   582: fload #12
    //   584: fconst_1
    //   585: fcmpl
    //   586: ifne -> 612
    //   589: aload_0
    //   590: iconst_1
    //   591: iconst_2
    //   592: invokevirtual get : (II)F
    //   595: fneg
    //   596: f2d
    //   597: aload_0
    //   598: iconst_2
    //   599: iconst_2
    //   600: invokevirtual get : (II)F
    //   603: f2d
    //   604: invokestatic atan2 : (DD)D
    //   607: dstore #5
    //   609: goto -> 993
    //   612: aload #4
    //   614: getstatic org/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet.THEONE : Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet;
    //   617: if_acmpne -> 636
    //   620: aload_0
    //   621: iconst_0
    //   622: iconst_1
    //   623: invokevirtual get : (II)F
    //   626: f2d
    //   627: invokestatic asin : (D)D
    //   630: dneg
    //   631: dstore #5
    //   633: goto -> 652
    //   636: aload_0
    //   637: iconst_0
    //   638: iconst_1
    //   639: invokevirtual get : (II)F
    //   642: f2d
    //   643: invokestatic asin : (D)D
    //   646: ldc2_w 3.141592653589793
    //   649: dadd
    //   650: dstore #5
    //   652: dload #5
    //   654: d2f
    //   655: fstore #9
    //   657: aload_0
    //   658: iconst_0
    //   659: iconst_2
    //   660: invokevirtual get : (II)F
    //   663: f2d
    //   664: dstore #5
    //   666: fload #9
    //   668: f2d
    //   669: dstore #7
    //   671: dload #5
    //   673: dload #7
    //   675: invokestatic cos : (D)D
    //   678: ddiv
    //   679: aload_0
    //   680: iconst_0
    //   681: iconst_0
    //   682: invokevirtual get : (II)F
    //   685: f2d
    //   686: dload #7
    //   688: invokestatic cos : (D)D
    //   691: ddiv
    //   692: invokestatic atan2 : (DD)D
    //   695: d2f
    //   696: fstore #10
    //   698: aload_0
    //   699: iconst_2
    //   700: iconst_1
    //   701: invokevirtual get : (II)F
    //   704: f2d
    //   705: dload #7
    //   707: invokestatic cos : (D)D
    //   710: ddiv
    //   711: aload_0
    //   712: iconst_1
    //   713: iconst_1
    //   714: invokevirtual get : (II)F
    //   717: f2d
    //   718: dload #7
    //   720: invokestatic cos : (D)D
    //   723: ddiv
    //   724: invokestatic atan2 : (DD)D
    //   727: dstore #5
    //   729: goto -> 1215
    //   732: aload_0
    //   733: iconst_2
    //   734: iconst_1
    //   735: invokevirtual get : (II)F
    //   738: fstore #12
    //   740: fload #12
    //   742: fconst_1
    //   743: fcmpl
    //   744: ifne -> 769
    //   747: aload_0
    //   748: iconst_0
    //   749: iconst_2
    //   750: invokevirtual get : (II)F
    //   753: f2d
    //   754: aload_0
    //   755: iconst_0
    //   756: iconst_0
    //   757: invokevirtual get : (II)F
    //   760: f2d
    //   761: invokestatic atan2 : (DD)D
    //   764: dstore #5
    //   766: goto -> 1154
    //   769: fload #12
    //   771: ldc -1.0
    //   773: fcmpl
    //   774: ifne -> 803
    //   777: fconst_0
    //   778: f2d
    //   779: dstore #5
    //   781: aload_0
    //   782: iconst_0
    //   783: iconst_2
    //   784: invokevirtual get : (II)F
    //   787: f2d
    //   788: aload_0
    //   789: iconst_0
    //   790: iconst_0
    //   791: invokevirtual get : (II)F
    //   794: f2d
    //   795: invokestatic atan2 : (DD)D
    //   798: dstore #7
    //   800: goto -> 1204
    //   803: aload #4
    //   805: getstatic org/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet.THEONE : Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet;
    //   808: if_acmpne -> 826
    //   811: aload_0
    //   812: iconst_2
    //   813: iconst_1
    //   814: invokevirtual get : (II)F
    //   817: f2d
    //   818: invokestatic asin : (D)D
    //   821: dstore #5
    //   823: goto -> 842
    //   826: ldc2_w 3.141592653589793
    //   829: aload_0
    //   830: iconst_2
    //   831: iconst_1
    //   832: invokevirtual get : (II)F
    //   835: f2d
    //   836: invokestatic asin : (D)D
    //   839: dsub
    //   840: dstore #5
    //   842: dload #5
    //   844: d2f
    //   845: fstore #9
    //   847: aload_0
    //   848: iconst_2
    //   849: iconst_0
    //   850: invokevirtual get : (II)F
    //   853: fneg
    //   854: f2d
    //   855: dstore #5
    //   857: fload #9
    //   859: f2d
    //   860: dstore #7
    //   862: dload #5
    //   864: dload #7
    //   866: invokestatic cos : (D)D
    //   869: ddiv
    //   870: aload_0
    //   871: iconst_2
    //   872: iconst_2
    //   873: invokevirtual get : (II)F
    //   876: f2d
    //   877: dload #7
    //   879: invokestatic cos : (D)D
    //   882: ddiv
    //   883: invokestatic atan2 : (DD)D
    //   886: d2f
    //   887: fstore #10
    //   889: aload_0
    //   890: iconst_0
    //   891: iconst_1
    //   892: invokevirtual get : (II)F
    //   895: fneg
    //   896: f2d
    //   897: dload #7
    //   899: invokestatic cos : (D)D
    //   902: ddiv
    //   903: aload_0
    //   904: iconst_1
    //   905: iconst_1
    //   906: invokevirtual get : (II)F
    //   909: f2d
    //   910: dload #7
    //   912: invokestatic cos : (D)D
    //   915: ddiv
    //   916: invokestatic atan2 : (DD)D
    //   919: dstore #5
    //   921: goto -> 1215
    //   924: aload_0
    //   925: iconst_2
    //   926: iconst_0
    //   927: invokevirtual get : (II)F
    //   930: fstore #12
    //   932: fload #12
    //   934: ldc -1.0
    //   936: fcmpl
    //   937: ifne -> 966
    //   940: fconst_0
    //   941: f2d
    //   942: dstore #5
    //   944: aload_0
    //   945: iconst_0
    //   946: iconst_1
    //   947: invokevirtual get : (II)F
    //   950: f2d
    //   951: aload_0
    //   952: iconst_0
    //   953: iconst_2
    //   954: invokevirtual get : (II)F
    //   957: f2d
    //   958: invokestatic atan2 : (DD)D
    //   961: dstore #7
    //   963: goto -> 1158
    //   966: fload #12
    //   968: fconst_1
    //   969: fcmpl
    //   970: ifne -> 1000
    //   973: aload_0
    //   974: iconst_0
    //   975: iconst_1
    //   976: invokevirtual get : (II)F
    //   979: fneg
    //   980: f2d
    //   981: aload_0
    //   982: iconst_1
    //   983: iconst_1
    //   984: invokevirtual get : (II)F
    //   987: f2d
    //   988: invokestatic atan2 : (DD)D
    //   991: dstore #5
    //   993: fconst_0
    //   994: f2d
    //   995: dstore #7
    //   997: goto -> 1204
    //   1000: aload #4
    //   1002: getstatic org/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet.THEONE : Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet;
    //   1005: if_acmpne -> 1024
    //   1008: aload_0
    //   1009: iconst_2
    //   1010: iconst_0
    //   1011: invokevirtual get : (II)F
    //   1014: f2d
    //   1015: invokestatic asin : (D)D
    //   1018: dneg
    //   1019: dstore #5
    //   1021: goto -> 1040
    //   1024: aload_0
    //   1025: iconst_2
    //   1026: iconst_0
    //   1027: invokevirtual get : (II)F
    //   1030: f2d
    //   1031: invokestatic asin : (D)D
    //   1034: ldc2_w 3.141592653589793
    //   1037: dadd
    //   1038: dstore #5
    //   1040: dload #5
    //   1042: d2f
    //   1043: fstore #9
    //   1045: aload_0
    //   1046: iconst_2
    //   1047: iconst_1
    //   1048: invokevirtual get : (II)F
    //   1051: f2d
    //   1052: dstore #5
    //   1054: fload #9
    //   1056: f2d
    //   1057: dstore #7
    //   1059: dload #5
    //   1061: dload #7
    //   1063: invokestatic cos : (D)D
    //   1066: ddiv
    //   1067: aload_0
    //   1068: iconst_2
    //   1069: iconst_2
    //   1070: invokevirtual get : (II)F
    //   1073: f2d
    //   1074: dload #7
    //   1076: invokestatic cos : (D)D
    //   1079: ddiv
    //   1080: invokestatic atan2 : (DD)D
    //   1083: d2f
    //   1084: fstore #10
    //   1086: aload_0
    //   1087: iconst_1
    //   1088: iconst_0
    //   1089: invokevirtual get : (II)F
    //   1092: f2d
    //   1093: dload #7
    //   1095: invokestatic cos : (D)D
    //   1098: ddiv
    //   1099: aload_0
    //   1100: iconst_0
    //   1101: iconst_0
    //   1102: invokevirtual get : (II)F
    //   1105: f2d
    //   1106: dload #7
    //   1108: invokestatic cos : (D)D
    //   1111: ddiv
    //   1112: invokestatic atan2 : (DD)D
    //   1115: dstore #5
    //   1117: goto -> 1215
    //   1120: aload_0
    //   1121: iconst_1
    //   1122: iconst_0
    //   1123: invokevirtual get : (II)F
    //   1126: fstore #12
    //   1128: fload #12
    //   1130: fconst_1
    //   1131: fcmpl
    //   1132: ifne -> 1173
    //   1135: aload_0
    //   1136: iconst_0
    //   1137: iconst_2
    //   1138: invokevirtual get : (II)F
    //   1141: f2d
    //   1142: aload_0
    //   1143: iconst_2
    //   1144: iconst_2
    //   1145: invokevirtual get : (II)F
    //   1148: f2d
    //   1149: invokestatic atan2 : (DD)D
    //   1152: dstore #5
    //   1154: fconst_0
    //   1155: f2d
    //   1156: dstore #7
    //   1158: dload #5
    //   1160: dload #7
    //   1162: dsub
    //   1163: d2f
    //   1164: fstore #11
    //   1166: ldc 1.5707964
    //   1168: fstore #9
    //   1170: goto -> 2450
    //   1173: fload #12
    //   1175: ldc -1.0
    //   1177: fcmpl
    //   1178: ifne -> 1223
    //   1181: fconst_0
    //   1182: f2d
    //   1183: dstore #5
    //   1185: aload_0
    //   1186: iconst_2
    //   1187: iconst_1
    //   1188: invokevirtual get : (II)F
    //   1191: f2d
    //   1192: aload_0
    //   1193: iconst_0
    //   1194: iconst_1
    //   1195: invokevirtual get : (II)F
    //   1198: f2d
    //   1199: invokestatic atan2 : (DD)D
    //   1202: dstore #7
    //   1204: dload #5
    //   1206: dload #7
    //   1208: dsub
    //   1209: dstore #5
    //   1211: fload #11
    //   1213: fstore #10
    //   1215: dload #5
    //   1217: d2f
    //   1218: fstore #11
    //   1220: goto -> 2450
    //   1223: aload #4
    //   1225: getstatic org/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet.THEONE : Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet;
    //   1228: if_acmpne -> 1246
    //   1231: aload_0
    //   1232: iconst_1
    //   1233: iconst_0
    //   1234: invokevirtual get : (II)F
    //   1237: f2d
    //   1238: invokestatic asin : (D)D
    //   1241: dstore #5
    //   1243: goto -> 1262
    //   1246: ldc2_w 3.141592653589793
    //   1249: aload_0
    //   1250: iconst_1
    //   1251: iconst_0
    //   1252: invokevirtual get : (II)F
    //   1255: f2d
    //   1256: invokestatic asin : (D)D
    //   1259: dsub
    //   1260: dstore #5
    //   1262: dload #5
    //   1264: d2f
    //   1265: fstore #9
    //   1267: aload_0
    //   1268: iconst_1
    //   1269: iconst_2
    //   1270: invokevirtual get : (II)F
    //   1273: fneg
    //   1274: f2d
    //   1275: dstore #5
    //   1277: fload #9
    //   1279: f2d
    //   1280: dstore #7
    //   1282: dload #5
    //   1284: dload #7
    //   1286: invokestatic cos : (D)D
    //   1289: ddiv
    //   1290: aload_0
    //   1291: iconst_1
    //   1292: iconst_1
    //   1293: invokevirtual get : (II)F
    //   1296: f2d
    //   1297: dload #7
    //   1299: invokestatic cos : (D)D
    //   1302: ddiv
    //   1303: invokestatic atan2 : (DD)D
    //   1306: d2f
    //   1307: fstore #10
    //   1309: aload_0
    //   1310: iconst_2
    //   1311: iconst_0
    //   1312: invokevirtual get : (II)F
    //   1315: fneg
    //   1316: f2d
    //   1317: dload #7
    //   1319: invokestatic cos : (D)D
    //   1322: ddiv
    //   1323: aload_0
    //   1324: iconst_0
    //   1325: iconst_0
    //   1326: invokevirtual get : (II)F
    //   1329: f2d
    //   1330: dload #7
    //   1332: invokestatic cos : (D)D
    //   1335: ddiv
    //   1336: invokestatic atan2 : (DD)D
    //   1339: dstore #5
    //   1341: goto -> 1215
    //   1344: aload_0
    //   1345: iconst_2
    //   1346: iconst_2
    //   1347: invokevirtual get : (II)F
    //   1350: fstore #9
    //   1352: fload #9
    //   1354: fconst_1
    //   1355: fcmpl
    //   1356: ifne -> 1381
    //   1359: aload_0
    //   1360: iconst_1
    //   1361: iconst_0
    //   1362: invokevirtual get : (II)F
    //   1365: f2d
    //   1366: aload_0
    //   1367: iconst_0
    //   1368: iconst_0
    //   1369: invokevirtual get : (II)F
    //   1372: f2d
    //   1373: invokestatic atan2 : (DD)D
    //   1376: dstore #5
    //   1378: goto -> 142
    //   1381: fload #9
    //   1383: ldc -1.0
    //   1385: fcmpl
    //   1386: ifne -> 1416
    //   1389: fconst_0
    //   1390: f2d
    //   1391: dstore #5
    //   1393: aload_0
    //   1394: iconst_0
    //   1395: iconst_1
    //   1396: invokevirtual get : (II)F
    //   1399: fneg
    //   1400: f2d
    //   1401: aload_0
    //   1402: iconst_0
    //   1403: iconst_0
    //   1404: invokevirtual get : (II)F
    //   1407: f2d
    //   1408: invokestatic atan2 : (DD)D
    //   1411: dstore #7
    //   1413: goto -> 2318
    //   1416: aload #4
    //   1418: getstatic org/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet.THEONE : Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet;
    //   1421: if_acmpne -> 1439
    //   1424: aload_0
    //   1425: iconst_2
    //   1426: iconst_2
    //   1427: invokevirtual get : (II)F
    //   1430: f2d
    //   1431: invokestatic acos : (D)D
    //   1434: dstore #5
    //   1436: goto -> 1452
    //   1439: aload_0
    //   1440: iconst_2
    //   1441: iconst_2
    //   1442: invokevirtual get : (II)F
    //   1445: f2d
    //   1446: invokestatic acos : (D)D
    //   1449: dneg
    //   1450: dstore #5
    //   1452: dload #5
    //   1454: d2f
    //   1455: fstore #9
    //   1457: aload_0
    //   1458: iconst_2
    //   1459: iconst_0
    //   1460: invokevirtual get : (II)F
    //   1463: f2d
    //   1464: dstore #5
    //   1466: fload #9
    //   1468: f2d
    //   1469: dstore #7
    //   1471: dload #5
    //   1473: dload #7
    //   1475: invokestatic sin : (D)D
    //   1478: ddiv
    //   1479: aload_0
    //   1480: iconst_2
    //   1481: iconst_1
    //   1482: invokevirtual get : (II)F
    //   1485: f2d
    //   1486: dload #7
    //   1488: invokestatic sin : (D)D
    //   1491: ddiv
    //   1492: invokestatic atan2 : (DD)D
    //   1495: d2f
    //   1496: fstore #10
    //   1498: aload_0
    //   1499: iconst_0
    //   1500: iconst_2
    //   1501: invokevirtual get : (II)F
    //   1504: f2d
    //   1505: dload #7
    //   1507: invokestatic sin : (D)D
    //   1510: ddiv
    //   1511: aload_0
    //   1512: iconst_1
    //   1513: iconst_2
    //   1514: invokevirtual get : (II)F
    //   1517: fneg
    //   1518: f2d
    //   1519: dload #7
    //   1521: invokestatic sin : (D)D
    //   1524: ddiv
    //   1525: invokestatic atan2 : (DD)D
    //   1528: dstore #5
    //   1530: goto -> 1215
    //   1533: aload_0
    //   1534: iconst_2
    //   1535: iconst_2
    //   1536: invokevirtual get : (II)F
    //   1539: fstore #9
    //   1541: fload #9
    //   1543: fconst_1
    //   1544: fcmpl
    //   1545: ifne -> 1570
    //   1548: aload_0
    //   1549: iconst_1
    //   1550: iconst_0
    //   1551: invokevirtual get : (II)F
    //   1554: f2d
    //   1555: aload_0
    //   1556: iconst_0
    //   1557: iconst_0
    //   1558: invokevirtual get : (II)F
    //   1561: f2d
    //   1562: invokestatic atan2 : (DD)D
    //   1565: dstore #5
    //   1567: goto -> 142
    //   1570: fload #9
    //   1572: ldc -1.0
    //   1574: fcmpl
    //   1575: ifne -> 1604
    //   1578: fconst_0
    //   1579: f2d
    //   1580: dstore #5
    //   1582: aload_0
    //   1583: iconst_0
    //   1584: iconst_1
    //   1585: invokevirtual get : (II)F
    //   1588: f2d
    //   1589: aload_0
    //   1590: iconst_1
    //   1591: iconst_1
    //   1592: invokevirtual get : (II)F
    //   1595: f2d
    //   1596: invokestatic atan2 : (DD)D
    //   1599: dstore #7
    //   1601: goto -> 2318
    //   1604: aload #4
    //   1606: getstatic org/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet.THEONE : Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet;
    //   1609: if_acmpne -> 1627
    //   1612: aload_0
    //   1613: iconst_2
    //   1614: iconst_2
    //   1615: invokevirtual get : (II)F
    //   1618: f2d
    //   1619: invokestatic acos : (D)D
    //   1622: dstore #5
    //   1624: goto -> 1640
    //   1627: aload_0
    //   1628: iconst_2
    //   1629: iconst_2
    //   1630: invokevirtual get : (II)F
    //   1633: f2d
    //   1634: invokestatic acos : (D)D
    //   1637: dneg
    //   1638: dstore #5
    //   1640: dload #5
    //   1642: d2f
    //   1643: fstore #9
    //   1645: aload_0
    //   1646: iconst_2
    //   1647: iconst_1
    //   1648: invokevirtual get : (II)F
    //   1651: f2d
    //   1652: dstore #5
    //   1654: fload #9
    //   1656: f2d
    //   1657: dstore #7
    //   1659: dload #5
    //   1661: dload #7
    //   1663: invokestatic sin : (D)D
    //   1666: ddiv
    //   1667: aload_0
    //   1668: iconst_2
    //   1669: iconst_0
    //   1670: invokevirtual get : (II)F
    //   1673: fneg
    //   1674: f2d
    //   1675: dload #7
    //   1677: invokestatic sin : (D)D
    //   1680: ddiv
    //   1681: invokestatic atan2 : (DD)D
    //   1684: d2f
    //   1685: fstore #10
    //   1687: aload_0
    //   1688: iconst_1
    //   1689: iconst_2
    //   1690: invokevirtual get : (II)F
    //   1693: f2d
    //   1694: dload #7
    //   1696: invokestatic sin : (D)D
    //   1699: ddiv
    //   1700: aload_0
    //   1701: iconst_0
    //   1702: iconst_2
    //   1703: invokevirtual get : (II)F
    //   1706: f2d
    //   1707: dload #7
    //   1709: invokestatic sin : (D)D
    //   1712: ddiv
    //   1713: invokestatic atan2 : (DD)D
    //   1716: dstore #5
    //   1718: goto -> 1215
    //   1721: aload_0
    //   1722: iconst_1
    //   1723: iconst_1
    //   1724: invokevirtual get : (II)F
    //   1727: fstore #9
    //   1729: fload #9
    //   1731: fconst_1
    //   1732: fcmpl
    //   1733: ifne -> 1758
    //   1736: aload_0
    //   1737: iconst_0
    //   1738: iconst_2
    //   1739: invokevirtual get : (II)F
    //   1742: f2d
    //   1743: aload_0
    //   1744: iconst_0
    //   1745: iconst_0
    //   1746: invokevirtual get : (II)F
    //   1749: f2d
    //   1750: invokestatic atan2 : (DD)D
    //   1753: dstore #5
    //   1755: goto -> 142
    //   1758: fload #9
    //   1760: ldc -1.0
    //   1762: fcmpl
    //   1763: ifne -> 1793
    //   1766: fconst_0
    //   1767: f2d
    //   1768: dstore #5
    //   1770: aload_0
    //   1771: iconst_0
    //   1772: iconst_2
    //   1773: invokevirtual get : (II)F
    //   1776: fneg
    //   1777: f2d
    //   1778: aload_0
    //   1779: iconst_2
    //   1780: iconst_2
    //   1781: invokevirtual get : (II)F
    //   1784: f2d
    //   1785: invokestatic atan2 : (DD)D
    //   1788: dstore #7
    //   1790: goto -> 2318
    //   1793: aload #4
    //   1795: getstatic org/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet.THEONE : Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet;
    //   1798: if_acmpne -> 1816
    //   1801: aload_0
    //   1802: iconst_1
    //   1803: iconst_1
    //   1804: invokevirtual get : (II)F
    //   1807: f2d
    //   1808: invokestatic acos : (D)D
    //   1811: dstore #5
    //   1813: goto -> 1829
    //   1816: aload_0
    //   1817: iconst_1
    //   1818: iconst_1
    //   1819: invokevirtual get : (II)F
    //   1822: f2d
    //   1823: invokestatic acos : (D)D
    //   1826: dneg
    //   1827: dstore #5
    //   1829: dload #5
    //   1831: d2f
    //   1832: fstore #9
    //   1834: aload_0
    //   1835: iconst_1
    //   1836: iconst_2
    //   1837: invokevirtual get : (II)F
    //   1840: f2d
    //   1841: dstore #5
    //   1843: fload #9
    //   1845: f2d
    //   1846: dstore #7
    //   1848: dload #5
    //   1850: dload #7
    //   1852: invokestatic sin : (D)D
    //   1855: ddiv
    //   1856: aload_0
    //   1857: iconst_1
    //   1858: iconst_0
    //   1859: invokevirtual get : (II)F
    //   1862: f2d
    //   1863: dload #7
    //   1865: invokestatic sin : (D)D
    //   1868: ddiv
    //   1869: invokestatic atan2 : (DD)D
    //   1872: d2f
    //   1873: fstore #10
    //   1875: aload_0
    //   1876: iconst_2
    //   1877: iconst_1
    //   1878: invokevirtual get : (II)F
    //   1881: f2d
    //   1882: dload #7
    //   1884: invokestatic sin : (D)D
    //   1887: ddiv
    //   1888: aload_0
    //   1889: iconst_0
    //   1890: iconst_1
    //   1891: invokevirtual get : (II)F
    //   1894: fneg
    //   1895: f2d
    //   1896: dload #7
    //   1898: invokestatic sin : (D)D
    //   1901: ddiv
    //   1902: invokestatic atan2 : (DD)D
    //   1905: dstore #5
    //   1907: goto -> 1215
    //   1910: aload_0
    //   1911: iconst_1
    //   1912: iconst_1
    //   1913: invokevirtual get : (II)F
    //   1916: fstore #9
    //   1918: fload #9
    //   1920: fconst_1
    //   1921: fcmpl
    //   1922: ifne -> 1947
    //   1925: aload_0
    //   1926: iconst_0
    //   1927: iconst_2
    //   1928: invokevirtual get : (II)F
    //   1931: f2d
    //   1932: aload_0
    //   1933: iconst_0
    //   1934: iconst_0
    //   1935: invokevirtual get : (II)F
    //   1938: f2d
    //   1939: invokestatic atan2 : (DD)D
    //   1942: dstore #5
    //   1944: goto -> 142
    //   1947: fload #9
    //   1949: ldc -1.0
    //   1951: fcmpl
    //   1952: ifne -> 1981
    //   1955: fconst_0
    //   1956: f2d
    //   1957: dstore #5
    //   1959: aload_0
    //   1960: iconst_0
    //   1961: iconst_2
    //   1962: invokevirtual get : (II)F
    //   1965: f2d
    //   1966: aload_0
    //   1967: iconst_0
    //   1968: iconst_0
    //   1969: invokevirtual get : (II)F
    //   1972: f2d
    //   1973: invokestatic atan2 : (DD)D
    //   1976: dstore #7
    //   1978: goto -> 2318
    //   1981: aload #4
    //   1983: getstatic org/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet.THEONE : Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet;
    //   1986: if_acmpne -> 2004
    //   1989: aload_0
    //   1990: iconst_1
    //   1991: iconst_1
    //   1992: invokevirtual get : (II)F
    //   1995: f2d
    //   1996: invokestatic acos : (D)D
    //   1999: dstore #5
    //   2001: goto -> 2017
    //   2004: aload_0
    //   2005: iconst_1
    //   2006: iconst_1
    //   2007: invokevirtual get : (II)F
    //   2010: f2d
    //   2011: invokestatic acos : (D)D
    //   2014: dneg
    //   2015: dstore #5
    //   2017: dload #5
    //   2019: d2f
    //   2020: fstore #9
    //   2022: aload_0
    //   2023: iconst_1
    //   2024: iconst_0
    //   2025: invokevirtual get : (II)F
    //   2028: f2d
    //   2029: dstore #5
    //   2031: fload #9
    //   2033: f2d
    //   2034: dstore #7
    //   2036: dload #5
    //   2038: dload #7
    //   2040: invokestatic sin : (D)D
    //   2043: ddiv
    //   2044: aload_0
    //   2045: iconst_1
    //   2046: iconst_2
    //   2047: invokevirtual get : (II)F
    //   2050: fneg
    //   2051: f2d
    //   2052: dload #7
    //   2054: invokestatic sin : (D)D
    //   2057: ddiv
    //   2058: invokestatic atan2 : (DD)D
    //   2061: d2f
    //   2062: fstore #10
    //   2064: aload_0
    //   2065: iconst_0
    //   2066: iconst_1
    //   2067: invokevirtual get : (II)F
    //   2070: f2d
    //   2071: dload #7
    //   2073: invokestatic sin : (D)D
    //   2076: ddiv
    //   2077: aload_0
    //   2078: iconst_2
    //   2079: iconst_1
    //   2080: invokevirtual get : (II)F
    //   2083: f2d
    //   2084: dload #7
    //   2086: invokestatic sin : (D)D
    //   2089: ddiv
    //   2090: invokestatic atan2 : (DD)D
    //   2093: dstore #5
    //   2095: goto -> 1215
    //   2098: aload_0
    //   2099: iconst_0
    //   2100: iconst_0
    //   2101: invokevirtual get : (II)F
    //   2104: fstore #9
    //   2106: fload #9
    //   2108: fconst_1
    //   2109: fcmpl
    //   2110: ifne -> 2135
    //   2113: aload_0
    //   2114: iconst_2
    //   2115: iconst_1
    //   2116: invokevirtual get : (II)F
    //   2119: f2d
    //   2120: aload_0
    //   2121: iconst_1
    //   2122: iconst_1
    //   2123: invokevirtual get : (II)F
    //   2126: f2d
    //   2127: invokestatic atan2 : (DD)D
    //   2130: dstore #5
    //   2132: goto -> 142
    //   2135: fload #9
    //   2137: ldc -1.0
    //   2139: fcmpl
    //   2140: ifne -> 2170
    //   2143: fconst_0
    //   2144: f2d
    //   2145: dstore #5
    //   2147: aload_0
    //   2148: iconst_1
    //   2149: iconst_2
    //   2150: invokevirtual get : (II)F
    //   2153: fneg
    //   2154: f2d
    //   2155: aload_0
    //   2156: iconst_1
    //   2157: iconst_1
    //   2158: invokevirtual get : (II)F
    //   2161: f2d
    //   2162: invokestatic atan2 : (DD)D
    //   2165: dstore #7
    //   2167: goto -> 2318
    //   2170: aload #4
    //   2172: getstatic org/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet.THEONE : Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet;
    //   2175: if_acmpne -> 2193
    //   2178: aload_0
    //   2179: iconst_0
    //   2180: iconst_0
    //   2181: invokevirtual get : (II)F
    //   2184: f2d
    //   2185: invokestatic acos : (D)D
    //   2188: dstore #5
    //   2190: goto -> 2206
    //   2193: aload_0
    //   2194: iconst_0
    //   2195: iconst_0
    //   2196: invokevirtual get : (II)F
    //   2199: f2d
    //   2200: invokestatic acos : (D)D
    //   2203: dneg
    //   2204: dstore #5
    //   2206: dload #5
    //   2208: d2f
    //   2209: fstore #9
    //   2211: aload_0
    //   2212: iconst_0
    //   2213: iconst_1
    //   2214: invokevirtual get : (II)F
    //   2217: f2d
    //   2218: dstore #5
    //   2220: fload #9
    //   2222: f2d
    //   2223: dstore #7
    //   2225: dload #5
    //   2227: dload #7
    //   2229: invokestatic sin : (D)D
    //   2232: ddiv
    //   2233: aload_0
    //   2234: iconst_0
    //   2235: iconst_2
    //   2236: invokevirtual get : (II)F
    //   2239: f2d
    //   2240: dload #7
    //   2242: invokestatic sin : (D)D
    //   2245: ddiv
    //   2246: invokestatic atan2 : (DD)D
    //   2249: d2f
    //   2250: fstore #10
    //   2252: aload_0
    //   2253: iconst_1
    //   2254: iconst_0
    //   2255: invokevirtual get : (II)F
    //   2258: f2d
    //   2259: dload #7
    //   2261: invokestatic sin : (D)D
    //   2264: ddiv
    //   2265: aload_0
    //   2266: iconst_2
    //   2267: iconst_0
    //   2268: invokevirtual get : (II)F
    //   2271: fneg
    //   2272: f2d
    //   2273: dload #7
    //   2275: invokestatic sin : (D)D
    //   2278: ddiv
    //   2279: invokestatic atan2 : (DD)D
    //   2282: dstore #5
    //   2284: goto -> 1215
    //   2287: fload #9
    //   2289: ldc -1.0
    //   2291: fcmpl
    //   2292: ifne -> 2333
    //   2295: fconst_0
    //   2296: f2d
    //   2297: dstore #5
    //   2299: aload_0
    //   2300: iconst_1
    //   2301: iconst_2
    //   2302: invokevirtual get : (II)F
    //   2305: f2d
    //   2306: aload_0
    //   2307: iconst_2
    //   2308: iconst_2
    //   2309: invokevirtual get : (II)F
    //   2312: f2d
    //   2313: invokestatic atan2 : (DD)D
    //   2316: dstore #7
    //   2318: dload #5
    //   2320: dload #7
    //   2322: dsub
    //   2323: d2f
    //   2324: fstore #11
    //   2326: ldc 3.1415927
    //   2328: fstore #9
    //   2330: goto -> 2450
    //   2333: aload #4
    //   2335: getstatic org/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet.THEONE : Lorg/firstinspires/ftc/robotcore/external/navigation/Orientation$AngleSet;
    //   2338: if_acmpne -> 2356
    //   2341: aload_0
    //   2342: iconst_0
    //   2343: iconst_0
    //   2344: invokevirtual get : (II)F
    //   2347: f2d
    //   2348: invokestatic acos : (D)D
    //   2351: dstore #5
    //   2353: goto -> 2369
    //   2356: aload_0
    //   2357: iconst_0
    //   2358: iconst_0
    //   2359: invokevirtual get : (II)F
    //   2362: f2d
    //   2363: invokestatic acos : (D)D
    //   2366: dneg
    //   2367: dstore #5
    //   2369: dload #5
    //   2371: d2f
    //   2372: fstore #9
    //   2374: aload_0
    //   2375: iconst_0
    //   2376: iconst_2
    //   2377: invokevirtual get : (II)F
    //   2380: f2d
    //   2381: dstore #5
    //   2383: fload #9
    //   2385: f2d
    //   2386: dstore #7
    //   2388: dload #5
    //   2390: dload #7
    //   2392: invokestatic sin : (D)D
    //   2395: ddiv
    //   2396: aload_0
    //   2397: iconst_0
    //   2398: iconst_1
    //   2399: invokevirtual get : (II)F
    //   2402: fneg
    //   2403: f2d
    //   2404: dload #7
    //   2406: invokestatic sin : (D)D
    //   2409: ddiv
    //   2410: invokestatic atan2 : (DD)D
    //   2413: d2f
    //   2414: fstore #10
    //   2416: aload_0
    //   2417: iconst_2
    //   2418: iconst_0
    //   2419: invokevirtual get : (II)F
    //   2422: f2d
    //   2423: dload #7
    //   2425: invokestatic sin : (D)D
    //   2428: ddiv
    //   2429: aload_0
    //   2430: iconst_1
    //   2431: iconst_0
    //   2432: invokevirtual get : (II)F
    //   2435: f2d
    //   2436: dload #7
    //   2438: invokestatic sin : (D)D
    //   2441: ddiv
    //   2442: invokestatic atan2 : (DD)D
    //   2445: dstore #5
    //   2447: goto -> 1215
    //   2450: new org/firstinspires/ftc/robotcore/external/navigation/Orientation
    //   2453: dup
    //   2454: aload_1
    //   2455: aload_2
    //   2456: aload_3
    //   2457: aload_3
    //   2458: fload #10
    //   2460: invokevirtual fromRadians : (F)F
    //   2463: aload_3
    //   2464: fload #9
    //   2466: invokevirtual fromRadians : (F)F
    //   2469: aload_3
    //   2470: fload #11
    //   2472: invokevirtual fromRadians : (F)F
    //   2475: lconst_0
    //   2476: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/external/navigation/AxesReference;Lorg/firstinspires/ftc/robotcore/external/navigation/AxesOrder;Lorg/firstinspires/ftc/robotcore/external/navigation/AngleUnit;FFFJ)V
    //   2479: areturn
  }
  
  public static OpenGLMatrix getRotationMatrix(AxesReference paramAxesReference, AxesOrder paramAxesOrder, AngleUnit paramAngleUnit, float paramFloat1, float paramFloat2, float paramFloat3) {
    double d5;
    double d6;
    float f7;
    if (paramAxesReference == AxesReference.INTRINSIC)
      return getRotationMatrix(paramAxesReference.reverse(), paramAxesOrder.reverse(), paramAngleUnit, paramFloat3, paramFloat2, paramFloat1); 
    float f1 = paramAngleUnit.toRadians(paramFloat1);
    paramFloat2 = paramAngleUnit.toRadians(paramFloat2);
    paramFloat1 = paramAngleUnit.toRadians(paramFloat3);
    switch (paramAxesOrder) {
      default:
        d1 = paramFloat2;
        paramFloat2 = (float)Math.cos(d1);
        d2 = f1;
        f1 = (float)-(Math.cos(d2) * Math.sin(d1));
        f2 = (float)(Math.sin(d2) * Math.sin(d1));
        d3 = paramFloat1;
        f3 = (float)(Math.cos(d3) * Math.sin(d1));
        f4 = (float)(Math.cos(d2) * Math.cos(d1) * Math.cos(d3) - Math.sin(d2) * Math.sin(d3));
        f5 = (float)(-(Math.cos(d2) * Math.sin(d3)) - Math.cos(d1) * Math.cos(d3) * Math.sin(d2));
        f6 = (float)(Math.sin(d1) * Math.sin(d3));
        paramFloat3 = (float)(Math.cos(d3) * Math.sin(d2) + Math.cos(d2) * Math.cos(d1) * Math.sin(d3));
        paramFloat1 = (float)(Math.cos(d2) * Math.cos(d3) - Math.cos(d1) * Math.sin(d2) * Math.sin(d3));
        openGLMatrix = new OpenGLMatrix();
        openGLMatrix.put(0, 0, paramFloat2);
        openGLMatrix.put(0, 1, f1);
        openGLMatrix.put(0, 2, f2);
        openGLMatrix.put(1, 0, f3);
        openGLMatrix.put(1, 1, f4);
        openGLMatrix.put(1, 2, f5);
        openGLMatrix.put(2, 0, f6);
        openGLMatrix.put(2, 1, paramFloat3);
        openGLMatrix.put(2, 2, paramFloat1);
        return openGLMatrix;
      case null:
        d1 = f1;
        d2 = Math.cos(d1);
        d3 = paramFloat1;
        d4 = Math.cos(d3);
        d5 = Math.sin(d1);
        d6 = paramFloat2;
        paramFloat2 = (float)(d2 * d4 + d5 * Math.sin(d6) * Math.sin(d3));
        f1 = (float)(Math.cos(d1) * Math.sin(d6) * Math.sin(d3) - Math.cos(d3) * Math.sin(d1));
        f2 = (float)(Math.cos(d6) * Math.sin(d3));
        f3 = (float)(Math.cos(d6) * Math.sin(d1));
        f4 = (float)(Math.cos(d1) * Math.cos(d6));
        f5 = (float)-Math.sin(d6);
        f6 = (float)(Math.cos(d3) * Math.sin(d1) * Math.sin(d6) - Math.cos(d1) * Math.sin(d3));
        paramFloat3 = (float)(Math.sin(d1) * Math.sin(d3) + Math.cos(d1) * Math.cos(d3) * Math.sin(d6));
        d1 = Math.cos(d6) * Math.cos(d3);
        paramFloat1 = (float)d1;
        openGLMatrix = new OpenGLMatrix();
        openGLMatrix.put(0, 0, paramFloat2);
        openGLMatrix.put(0, 1, f1);
        openGLMatrix.put(0, 2, f2);
        openGLMatrix.put(1, 0, f3);
        openGLMatrix.put(1, 1, f4);
        openGLMatrix.put(1, 2, f5);
        openGLMatrix.put(2, 0, f6);
        openGLMatrix.put(2, 1, paramFloat3);
        openGLMatrix.put(2, 2, paramFloat1);
        return openGLMatrix;
      case null:
        d1 = f1;
        d3 = Math.cos(d1);
        d2 = paramFloat2;
        paramFloat2 = (float)(d3 * Math.cos(d2));
        f1 = (float)-(Math.cos(d2) * Math.sin(d1));
        f2 = (float)Math.sin(d2);
        d3 = paramFloat1;
        f3 = (float)(Math.cos(d3) * Math.sin(d1) + Math.cos(d1) * Math.sin(d2) * Math.sin(d3));
        f4 = (float)(Math.cos(d1) * Math.cos(d3) - Math.sin(d1) * Math.sin(d2) * Math.sin(d3));
        f5 = (float)-(Math.cos(d2) * Math.sin(d3));
        f6 = (float)(Math.sin(d1) * Math.sin(d3) - Math.cos(d1) * Math.cos(d3) * Math.sin(d2));
        paramFloat3 = (float)(Math.cos(d1) * Math.sin(d3) + Math.cos(d3) * Math.sin(d1) * Math.sin(d2));
        paramFloat1 = (float)(Math.cos(d2) * Math.cos(d3));
        openGLMatrix = new OpenGLMatrix();
        openGLMatrix.put(0, 0, paramFloat2);
        openGLMatrix.put(0, 1, f1);
        openGLMatrix.put(0, 2, f2);
        openGLMatrix.put(1, 0, f3);
        openGLMatrix.put(1, 1, f4);
        openGLMatrix.put(1, 2, f5);
        openGLMatrix.put(2, 0, f6);
        openGLMatrix.put(2, 1, paramFloat3);
        openGLMatrix.put(2, 2, paramFloat1);
        return openGLMatrix;
      case null:
        d1 = f1;
        d3 = Math.cos(d1);
        d2 = paramFloat2;
        paramFloat2 = (float)(d3 * Math.cos(d2));
        f1 = (float)-Math.sin(d2);
        f2 = (float)(Math.cos(d2) * Math.sin(d1));
        d3 = Math.sin(d1);
        d4 = paramFloat1;
        f3 = (float)(d3 * Math.sin(d4) + Math.cos(d1) * Math.cos(d4) * Math.sin(d2));
        f4 = (float)(Math.cos(d2) * Math.cos(d4));
        f5 = (float)(Math.cos(d4) * Math.sin(d1) * Math.sin(d2) - Math.cos(d1) * Math.sin(d4));
        f6 = (float)(Math.cos(d1) * Math.sin(d2) * Math.sin(d4) - Math.cos(d4) * Math.sin(d1));
        paramFloat3 = (float)(Math.cos(d2) * Math.sin(d4));
        paramFloat1 = (float)(Math.cos(d1) * Math.cos(d4) + Math.sin(d1) * Math.sin(d2) * Math.sin(d4));
        openGLMatrix = new OpenGLMatrix();
        openGLMatrix.put(0, 0, paramFloat2);
        openGLMatrix.put(0, 1, f1);
        openGLMatrix.put(0, 2, f2);
        openGLMatrix.put(1, 0, f3);
        openGLMatrix.put(1, 1, f4);
        openGLMatrix.put(1, 2, f5);
        openGLMatrix.put(2, 0, f6);
        openGLMatrix.put(2, 1, paramFloat3);
        openGLMatrix.put(2, 2, paramFloat1);
        return openGLMatrix;
      case null:
        d1 = f1;
        d2 = Math.cos(d1);
        d3 = paramFloat1;
        d4 = Math.cos(d3);
        d5 = Math.sin(d1);
        d6 = paramFloat2;
        paramFloat2 = (float)(d2 * d4 - d5 * Math.sin(d6) * Math.sin(d3));
        f1 = (float)-(Math.cos(d6) * Math.sin(d3));
        f2 = (float)(Math.cos(d3) * Math.sin(d1) + Math.cos(d1) * Math.sin(d6) * Math.sin(d3));
        f3 = (float)(Math.cos(d1) * Math.sin(d3) + Math.cos(d3) * Math.sin(d1) * Math.sin(d6));
        f4 = (float)(Math.cos(d6) * Math.cos(d3));
        f5 = (float)(Math.sin(d1) * Math.sin(d3) - Math.cos(d1) * Math.cos(d3) * Math.sin(d6));
        f6 = (float)-(Math.cos(d6) * Math.sin(d1));
        paramFloat3 = (float)Math.sin(d6);
        paramFloat1 = (float)(Math.cos(d1) * Math.cos(d6));
        openGLMatrix = new OpenGLMatrix();
        openGLMatrix.put(0, 0, paramFloat2);
        openGLMatrix.put(0, 1, f1);
        openGLMatrix.put(0, 2, f2);
        openGLMatrix.put(1, 0, f3);
        openGLMatrix.put(1, 1, f4);
        openGLMatrix.put(1, 2, f5);
        openGLMatrix.put(2, 0, f6);
        openGLMatrix.put(2, 1, paramFloat3);
        openGLMatrix.put(2, 2, paramFloat1);
        return openGLMatrix;
      case null:
        d1 = paramFloat2;
        d3 = Math.cos(d1);
        d2 = paramFloat1;
        paramFloat2 = (float)(d3 * Math.cos(d2));
        d3 = Math.cos(d2);
        d4 = f1;
        f1 = (float)(d3 * Math.sin(d4) * Math.sin(d1) - Math.cos(d4) * Math.sin(d2));
        f2 = (float)(Math.sin(d4) * Math.sin(d2) + Math.cos(d4) * Math.cos(d2) * Math.sin(d1));
        f3 = (float)(Math.cos(d1) * Math.sin(d2));
        f4 = (float)(Math.cos(d4) * Math.cos(d2) + Math.sin(d4) * Math.sin(d1) * Math.sin(d2));
        f5 = (float)(Math.cos(d4) * Math.sin(d1) * Math.sin(d2) - Math.cos(d2) * Math.sin(d4));
        f6 = (float)-Math.sin(d1);
        paramFloat3 = (float)(Math.cos(d1) * Math.sin(d4));
        paramFloat1 = (float)(Math.cos(d4) * Math.cos(d1));
        openGLMatrix = new OpenGLMatrix();
        openGLMatrix.put(0, 0, paramFloat2);
        openGLMatrix.put(0, 1, f1);
        openGLMatrix.put(0, 2, f2);
        openGLMatrix.put(1, 0, f3);
        openGLMatrix.put(1, 1, f4);
        openGLMatrix.put(1, 2, f5);
        openGLMatrix.put(2, 0, f6);
        openGLMatrix.put(2, 1, paramFloat3);
        openGLMatrix.put(2, 2, paramFloat1);
        return openGLMatrix;
      case null:
        d1 = paramFloat2;
        d3 = Math.cos(d1);
        d2 = paramFloat1;
        paramFloat2 = (float)(d3 * Math.cos(d2));
        d3 = f1;
        f1 = (float)(Math.sin(d3) * Math.sin(d2) - Math.cos(d3) * Math.cos(d2) * Math.sin(d1));
        f2 = (float)(Math.cos(d3) * Math.sin(d2) + Math.cos(d2) * Math.sin(d3) * Math.sin(d1));
        f3 = (float)Math.sin(d1);
        f4 = (float)(Math.cos(d3) * Math.cos(d1));
        f5 = (float)-(Math.cos(d1) * Math.sin(d3));
        f6 = (float)-(Math.cos(d1) * Math.sin(d2));
        paramFloat3 = (float)(Math.cos(d2) * Math.sin(d3) + Math.cos(d3) * Math.sin(d1) * Math.sin(d2));
        paramFloat1 = (float)(Math.cos(d3) * Math.cos(d2) - Math.sin(d3) * Math.sin(d1) * Math.sin(d2));
        openGLMatrix = new OpenGLMatrix();
        openGLMatrix.put(0, 0, paramFloat2);
        openGLMatrix.put(0, 1, f1);
        openGLMatrix.put(0, 2, f2);
        openGLMatrix.put(1, 0, f3);
        openGLMatrix.put(1, 1, f4);
        openGLMatrix.put(1, 2, f5);
        openGLMatrix.put(2, 0, f6);
        openGLMatrix.put(2, 1, paramFloat3);
        openGLMatrix.put(2, 2, paramFloat1);
        return openGLMatrix;
      case null:
        d1 = f1;
        d2 = Math.cos(d1);
        d3 = paramFloat1;
        d4 = Math.cos(d3);
        d5 = paramFloat2;
        paramFloat2 = (float)(d2 * d4 - Math.cos(d5) * Math.sin(d1) * Math.sin(d3));
        f1 = (float)(-(Math.cos(d1) * Math.cos(d5) * Math.sin(d3)) - Math.cos(d3) * Math.sin(d1));
        f2 = (float)(Math.sin(d5) * Math.sin(d3));
        f3 = (float)(Math.cos(d1) * Math.sin(d3) + Math.cos(d5) * Math.cos(d3) * Math.sin(d1));
        f4 = (float)(Math.cos(d1) * Math.cos(d5) * Math.cos(d3) - Math.sin(d1) * Math.sin(d3));
        f5 = (float)-(Math.cos(d3) * Math.sin(d5));
        f6 = (float)(Math.sin(d1) * Math.sin(d5));
        paramFloat3 = (float)(Math.cos(d1) * Math.sin(d5));
        d1 = Math.cos(d5);
        paramFloat1 = (float)d1;
        openGLMatrix = new OpenGLMatrix();
        openGLMatrix.put(0, 0, paramFloat2);
        openGLMatrix.put(0, 1, f1);
        openGLMatrix.put(0, 2, f2);
        openGLMatrix.put(1, 0, f3);
        openGLMatrix.put(1, 1, f4);
        openGLMatrix.put(1, 2, f5);
        openGLMatrix.put(2, 0, f6);
        openGLMatrix.put(2, 1, paramFloat3);
        openGLMatrix.put(2, 2, paramFloat1);
        return openGLMatrix;
      case null:
        d1 = f1;
        d2 = Math.cos(d1);
        d3 = paramFloat2;
        d4 = Math.cos(d3);
        d5 = paramFloat1;
        paramFloat2 = (float)(d2 * d4 * Math.cos(d5) - Math.sin(d1) * Math.sin(d5));
        f1 = (float)(-(Math.cos(d1) * Math.sin(d5)) - Math.cos(d3) * Math.cos(d5) * Math.sin(d1));
        f2 = (float)(Math.cos(d5) * Math.sin(d3));
        f3 = (float)(Math.cos(d5) * Math.sin(d1) + Math.cos(d1) * Math.cos(d3) * Math.sin(d5));
        f4 = (float)(Math.cos(d1) * Math.cos(d5) - Math.cos(d3) * Math.sin(d1) * Math.sin(d5));
        f5 = (float)(Math.sin(d3) * Math.sin(d5));
        f6 = (float)-(Math.cos(d1) * Math.sin(d3));
        paramFloat3 = (float)(Math.sin(d1) * Math.sin(d3));
        d1 = Math.cos(d3);
        paramFloat1 = (float)d1;
        openGLMatrix = new OpenGLMatrix();
        openGLMatrix.put(0, 0, paramFloat2);
        openGLMatrix.put(0, 1, f1);
        openGLMatrix.put(0, 2, f2);
        openGLMatrix.put(1, 0, f3);
        openGLMatrix.put(1, 1, f4);
        openGLMatrix.put(1, 2, f5);
        openGLMatrix.put(2, 0, f6);
        openGLMatrix.put(2, 1, paramFloat3);
        openGLMatrix.put(2, 2, paramFloat1);
        return openGLMatrix;
      case null:
        d1 = f1;
        d3 = Math.cos(d1);
        d4 = paramFloat2;
        d5 = Math.cos(d4);
        d2 = paramFloat1;
        paramFloat1 = (float)(d3 * d5 * Math.cos(d2) - Math.sin(d1) * Math.sin(d2));
        paramFloat2 = (float)-(Math.cos(d2) * Math.sin(d4));
        f2 = (float)(Math.cos(d1) * Math.sin(d2) + Math.cos(d4) * Math.cos(d2) * Math.sin(d1));
        f3 = (float)(Math.cos(d1) * Math.sin(d4));
        f4 = (float)Math.cos(d4);
        f5 = (float)(Math.sin(d1) * Math.sin(d4));
        f6 = (float)(-(Math.cos(d1) * Math.cos(d4) * Math.sin(d2)) - Math.cos(d2) * Math.sin(d1));
        paramFloat3 = (float)(Math.sin(d4) * Math.sin(d2));
        d3 = Math.cos(d1) * Math.cos(d2);
        d1 = Math.cos(d4) * Math.sin(d1);
        d2 = Math.sin(d2);
        f7 = (float)(d3 - d1 * d2);
        f1 = paramFloat2;
        paramFloat2 = paramFloat1;
        paramFloat1 = f7;
        openGLMatrix = new OpenGLMatrix();
        openGLMatrix.put(0, 0, paramFloat2);
        openGLMatrix.put(0, 1, f1);
        openGLMatrix.put(0, 2, f2);
        openGLMatrix.put(1, 0, f3);
        openGLMatrix.put(1, 1, f4);
        openGLMatrix.put(1, 2, f5);
        openGLMatrix.put(2, 0, f6);
        openGLMatrix.put(2, 1, paramFloat3);
        openGLMatrix.put(2, 2, paramFloat1);
        return openGLMatrix;
      case null:
        d1 = f1;
        d3 = Math.cos(d1);
        d2 = paramFloat1;
        d4 = Math.cos(d2);
        d5 = paramFloat2;
        paramFloat1 = (float)(d3 * d4 - Math.cos(d5) * Math.sin(d1) * Math.sin(d2));
        paramFloat2 = (float)(Math.sin(d5) * Math.sin(d2));
        f2 = (float)(Math.cos(d2) * Math.sin(d1) + Math.cos(d1) * Math.cos(d5) * Math.sin(d2));
        f3 = (float)(Math.sin(d1) * Math.sin(d5));
        f4 = (float)Math.cos(d5);
        f5 = (float)-(Math.cos(d1) * Math.sin(d5));
        f6 = (float)(-(Math.cos(d1) * Math.sin(d2)) - Math.cos(d5) * Math.cos(d2) * Math.sin(d1));
        paramFloat3 = (float)(Math.cos(d2) * Math.sin(d5));
        d3 = Math.cos(d1) * Math.cos(d5) * Math.cos(d2);
        d1 = Math.sin(d1);
        d2 = Math.sin(d2);
        f7 = (float)(d3 - d1 * d2);
        f1 = paramFloat2;
        paramFloat2 = paramFloat1;
        paramFloat1 = f7;
        openGLMatrix = new OpenGLMatrix();
        openGLMatrix.put(0, 0, paramFloat2);
        openGLMatrix.put(0, 1, f1);
        openGLMatrix.put(0, 2, f2);
        openGLMatrix.put(1, 0, f3);
        openGLMatrix.put(1, 1, f4);
        openGLMatrix.put(1, 2, f5);
        openGLMatrix.put(2, 0, f6);
        openGLMatrix.put(2, 1, paramFloat3);
        openGLMatrix.put(2, 2, paramFloat1);
        return openGLMatrix;
      case null:
        break;
    } 
    double d1 = paramFloat2;
    paramFloat2 = (float)Math.cos(d1);
    double d2 = f1;
    f1 = (float)(Math.sin(d2) * Math.sin(d1));
    float f2 = (float)(Math.cos(d2) * Math.sin(d1));
    double d3 = Math.sin(d1);
    double d4 = paramFloat1;
    float f3 = (float)(d3 * Math.sin(d4));
    float f4 = (float)(Math.cos(d2) * Math.cos(d4) - Math.cos(d1) * Math.sin(d2) * Math.sin(d4));
    float f5 = (float)(-(Math.cos(d2) * Math.cos(d1) * Math.sin(d4)) - Math.cos(d4) * Math.sin(d2));
    float f6 = (float)-(Math.cos(d4) * Math.sin(d1));
    paramFloat3 = (float)(Math.cos(d2) * Math.sin(d4) + Math.cos(d1) * Math.cos(d4) * Math.sin(d2));
    paramFloat1 = (float)(Math.cos(d2) * Math.cos(d1) * Math.cos(d4) - Math.sin(d2) * Math.sin(d4));
    OpenGLMatrix openGLMatrix = new OpenGLMatrix();
    openGLMatrix.put(0, 0, paramFloat2);
    openGLMatrix.put(0, 1, f1);
    openGLMatrix.put(0, 2, f2);
    openGLMatrix.put(1, 0, f3);
    openGLMatrix.put(1, 1, f4);
    openGLMatrix.put(1, 2, f5);
    openGLMatrix.put(2, 0, f6);
    openGLMatrix.put(2, 1, paramFloat3);
    openGLMatrix.put(2, 2, paramFloat1);
    return openGLMatrix;
  }
  
  public OpenGLMatrix getRotationMatrix() {
    return getRotationMatrix(this.axesReference, this.axesOrder, this.angleUnit, this.firstAngle, this.secondAngle, this.thirdAngle);
  }
  
  public Orientation toAngleUnit(AngleUnit paramAngleUnit) {
    return (paramAngleUnit != this.angleUnit) ? new Orientation(this.axesReference, this.axesOrder, paramAngleUnit, paramAngleUnit.fromUnit(this.angleUnit, this.firstAngle), paramAngleUnit.fromUnit(this.angleUnit, this.secondAngle), paramAngleUnit.fromUnit(this.angleUnit, this.thirdAngle), this.acquisitionTime) : this;
  }
  
  public Orientation toAxesOrder(AxesOrder paramAxesOrder) {
    return (this.axesOrder != paramAxesOrder) ? getOrientation((MatrixF)getRotationMatrix(), this.axesReference, paramAxesOrder, this.angleUnit) : this;
  }
  
  public Orientation toAxesReference(AxesReference paramAxesReference) {
    AxesReference axesReference = this.axesReference;
    if (axesReference != paramAxesReference) {
      boolean bool;
      if (paramAxesReference == axesReference.reverse()) {
        bool = true;
      } else {
        bool = false;
      } 
      Assert.assertTrue(bool);
      return new Orientation(this.axesReference.reverse(), this.axesOrder.reverse(), this.angleUnit, this.thirdAngle, this.secondAngle, this.firstAngle, this.acquisitionTime);
    } 
    return this;
  }
  
  public String toString() {
    return (this.angleUnit == AngleUnit.DEGREES) ? String.format("{%s %s %.0f %.0f %.0f}", new Object[] { this.axesReference.toString(), this.axesOrder.toString(), Float.valueOf(this.firstAngle), Float.valueOf(this.secondAngle), Float.valueOf(this.thirdAngle) }) : String.format("{%s %s %.3f %.3f %.3f}", new Object[] { this.axesReference.toString(), this.axesOrder.toString(), Float.valueOf(this.firstAngle), Float.valueOf(this.secondAngle), Float.valueOf(this.thirdAngle) });
  }
  
  public enum AngleSet {
    THEONE, THEOTHER;
    
    static {
      AngleSet angleSet = new AngleSet("THEOTHER", 1);
      THEOTHER = angleSet;
      $VALUES = new AngleSet[] { THEONE, angleSet };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\Orientation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */