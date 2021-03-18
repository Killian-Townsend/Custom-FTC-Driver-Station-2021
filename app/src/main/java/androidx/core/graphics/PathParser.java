package androidx.core.graphics;

import android.graphics.Path;
import android.util.Log;
import java.util.ArrayList;

public class PathParser {
  private static final String LOGTAG = "PathParser";
  
  private static void addNode(ArrayList<PathDataNode> paramArrayList, char paramChar, float[] paramArrayOffloat) {
    paramArrayList.add(new PathDataNode(paramChar, paramArrayOffloat));
  }
  
  public static boolean canMorph(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    if (paramArrayOfPathDataNode1 != null) {
      if (paramArrayOfPathDataNode2 == null)
        return false; 
      if (paramArrayOfPathDataNode1.length != paramArrayOfPathDataNode2.length)
        return false; 
      int i = 0;
      while (i < paramArrayOfPathDataNode1.length) {
        if ((paramArrayOfPathDataNode1[i]).mType == (paramArrayOfPathDataNode2[i]).mType) {
          if ((paramArrayOfPathDataNode1[i]).mParams.length != (paramArrayOfPathDataNode2[i]).mParams.length)
            return false; 
          i++;
          continue;
        } 
        return false;
      } 
      return true;
    } 
    return false;
  }
  
  static float[] copyOfRange(float[] paramArrayOffloat, int paramInt1, int paramInt2) {
    if (paramInt1 <= paramInt2) {
      int i = paramArrayOffloat.length;
      if (paramInt1 >= 0 && paramInt1 <= i) {
        paramInt2 -= paramInt1;
        i = Math.min(paramInt2, i - paramInt1);
        float[] arrayOfFloat = new float[paramInt2];
        System.arraycopy(paramArrayOffloat, paramInt1, arrayOfFloat, 0, i);
        return arrayOfFloat;
      } 
      throw new ArrayIndexOutOfBoundsException();
    } 
    throw new IllegalArgumentException();
  }
  
  public static PathDataNode[] createNodesFromPathData(String paramString) {
    if (paramString == null)
      return null; 
    ArrayList<PathDataNode> arrayList = new ArrayList();
    int j = 1;
    int i = 0;
    while (j < paramString.length()) {
      j = nextStart(paramString, j);
      String str = paramString.substring(i, j).trim();
      if (str.length() > 0) {
        float[] arrayOfFloat = getFloats(str);
        addNode(arrayList, str.charAt(0), arrayOfFloat);
      } 
      i = j;
      j++;
    } 
    if (j - i == 1 && i < paramString.length())
      addNode(arrayList, paramString.charAt(i), new float[0]); 
    return arrayList.<PathDataNode>toArray(new PathDataNode[arrayList.size()]);
  }
  
  public static Path createPathFromPathData(String paramString) {
    Path path = new Path();
    PathDataNode[] arrayOfPathDataNode = createNodesFromPathData(paramString);
    if (arrayOfPathDataNode != null)
      try {
        PathDataNode.nodesToPath(arrayOfPathDataNode, path);
        return path;
      } catch (RuntimeException runtimeException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error in parsing ");
        stringBuilder.append(paramString);
        throw new RuntimeException(stringBuilder.toString(), runtimeException);
      }  
    return null;
  }
  
  public static PathDataNode[] deepCopyNodes(PathDataNode[] paramArrayOfPathDataNode) {
    if (paramArrayOfPathDataNode == null)
      return null; 
    PathDataNode[] arrayOfPathDataNode = new PathDataNode[paramArrayOfPathDataNode.length];
    for (int i = 0; i < paramArrayOfPathDataNode.length; i++)
      arrayOfPathDataNode[i] = new PathDataNode(paramArrayOfPathDataNode[i]); 
    return arrayOfPathDataNode;
  }
  
  private static void extract(String paramString, int paramInt, ExtractFloatResult paramExtractFloatResult) {
    paramExtractFloatResult.mEndWithNegOrDot = false;
    int i = paramInt;
    char c4 = Character.MIN_VALUE;
    char c1 = c4;
    char c2 = c1;
    char c3 = c1;
    c1 = c4;
    while (i < paramString.length()) {
      c4 = paramString.charAt(i);
      if (c4 != ' ') {
        if (c4 != 'E' && c4 != 'e') {
          switch (c4) {
            default:
              c1 = Character.MIN_VALUE;
              break;
            case '.':
              if (c3 == '\000') {
                c1 = Character.MIN_VALUE;
                c3 = '\001';
                break;
              } 
              paramExtractFloatResult.mEndWithNegOrDot = true;
            case '-':
            
            case ',':
              c1 = Character.MIN_VALUE;
              c2 = '\001';
              break;
          } 
        } else {
          c1 = '\001';
        } 
        if (c2 != '\000')
          break; 
        continue;
      } 
      i++;
    } 
    paramExtractFloatResult.mEndPosition = i;
  }
  
  private static float[] getFloats(String paramString) {
    if (paramString.charAt(0) == 'z' || paramString.charAt(0) == 'Z')
      return new float[0]; 
    try {
      float[] arrayOfFloat = new float[paramString.length()];
      ExtractFloatResult extractFloatResult = new ExtractFloatResult();
      int k = paramString.length();
      int i = 1;
      for (int j = 0;; j = m) {
        int m;
        int n;
        if (i < k) {
          extract(paramString, i, extractFloatResult);
          n = extractFloatResult.mEndPosition;
          m = j;
          if (i < n) {
            arrayOfFloat[j] = Float.parseFloat(paramString.substring(i, n));
            m = j + 1;
          } 
          if (extractFloatResult.mEndWithNegOrDot) {
            i = n;
            j = m;
            continue;
          } 
        } else {
          return copyOfRange(arrayOfFloat, 0, j);
        } 
        i = n + 1;
      } 
    } catch (NumberFormatException numberFormatException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("error in parsing \"");
      stringBuilder.append(paramString);
      stringBuilder.append("\"");
      throw new RuntimeException(stringBuilder.toString(), numberFormatException);
    } 
  }
  
  public static boolean interpolatePathDataNodes(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2, PathDataNode[] paramArrayOfPathDataNode3, float paramFloat) {
    if (paramArrayOfPathDataNode1 != null && paramArrayOfPathDataNode2 != null && paramArrayOfPathDataNode3 != null) {
      if (paramArrayOfPathDataNode1.length == paramArrayOfPathDataNode2.length && paramArrayOfPathDataNode2.length == paramArrayOfPathDataNode3.length) {
        boolean bool = canMorph(paramArrayOfPathDataNode2, paramArrayOfPathDataNode3);
        int i = 0;
        if (!bool)
          return false; 
        while (i < paramArrayOfPathDataNode1.length) {
          paramArrayOfPathDataNode1[i].interpolatePathDataNode(paramArrayOfPathDataNode2[i], paramArrayOfPathDataNode3[i], paramFloat);
          i++;
        } 
        return true;
      } 
      throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes must have the same length");
    } 
    throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes cannot be null");
  }
  
  private static int nextStart(String paramString, int paramInt) {
    while (paramInt < paramString.length()) {
      char c = paramString.charAt(paramInt);
      if (((c - 65) * (c - 90) <= 0 || (c - 97) * (c - 122) <= 0) && c != 'e' && c != 'E')
        return paramInt; 
      paramInt++;
    } 
    return paramInt;
  }
  
  public static void updateNodes(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    for (int i = 0; i < paramArrayOfPathDataNode2.length; i++) {
      (paramArrayOfPathDataNode1[i]).mType = (paramArrayOfPathDataNode2[i]).mType;
      for (int j = 0; j < (paramArrayOfPathDataNode2[i]).mParams.length; j++)
        (paramArrayOfPathDataNode1[i]).mParams[j] = (paramArrayOfPathDataNode2[i]).mParams[j]; 
    } 
  }
  
  private static class ExtractFloatResult {
    int mEndPosition;
    
    boolean mEndWithNegOrDot;
  }
  
  public static class PathDataNode {
    public float[] mParams;
    
    public char mType;
    
    PathDataNode(char param1Char, float[] param1ArrayOffloat) {
      this.mType = param1Char;
      this.mParams = param1ArrayOffloat;
    }
    
    PathDataNode(PathDataNode param1PathDataNode) {
      this.mType = param1PathDataNode.mType;
      float[] arrayOfFloat = param1PathDataNode.mParams;
      this.mParams = PathParser.copyOfRange(arrayOfFloat, 0, arrayOfFloat.length);
    }
    
    private static void addCommand(Path param1Path, float[] param1ArrayOffloat1, char param1Char1, char param1Char2, float[] param1ArrayOffloat2) {
      // Byte code:
      //   0: aload_1
      //   1: iconst_0
      //   2: faload
      //   3: fstore #11
      //   5: aload_1
      //   6: iconst_1
      //   7: faload
      //   8: fstore #12
      //   10: aload_1
      //   11: iconst_2
      //   12: faload
      //   13: fstore #13
      //   15: aload_1
      //   16: iconst_3
      //   17: faload
      //   18: fstore #14
      //   20: aload_1
      //   21: iconst_4
      //   22: faload
      //   23: fstore #10
      //   25: aload_1
      //   26: iconst_5
      //   27: faload
      //   28: fstore #9
      //   30: fload #11
      //   32: fstore #5
      //   34: fload #12
      //   36: fstore #6
      //   38: fload #13
      //   40: fstore #7
      //   42: fload #14
      //   44: fstore #8
      //   46: iload_3
      //   47: lookupswitch default -> 216, 65 -> 320, 67 -> 313, 72 -> 291, 76 -> 232, 77 -> 232, 81 -> 269, 83 -> 269, 84 -> 232, 86 -> 291, 90 -> 238, 97 -> 320, 99 -> 313, 104 -> 291, 108 -> 232, 109 -> 232, 113 -> 269, 115 -> 269, 116 -> 232, 118 -> 291, 122 -> 238
      //   216: fload #14
      //   218: fstore #8
      //   220: fload #13
      //   222: fstore #7
      //   224: fload #12
      //   226: fstore #6
      //   228: fload #11
      //   230: fstore #5
      //   232: iconst_2
      //   233: istore #15
      //   235: goto -> 340
      //   238: aload_0
      //   239: invokevirtual close : ()V
      //   242: aload_0
      //   243: fload #10
      //   245: fload #9
      //   247: invokevirtual moveTo : (FF)V
      //   250: fload #10
      //   252: fstore #5
      //   254: fload #5
      //   256: fstore #7
      //   258: fload #9
      //   260: fstore #6
      //   262: fload #6
      //   264: fstore #8
      //   266: goto -> 232
      //   269: iconst_4
      //   270: istore #15
      //   272: fload #11
      //   274: fstore #5
      //   276: fload #12
      //   278: fstore #6
      //   280: fload #13
      //   282: fstore #7
      //   284: fload #14
      //   286: fstore #8
      //   288: goto -> 340
      //   291: iconst_1
      //   292: istore #15
      //   294: fload #11
      //   296: fstore #5
      //   298: fload #12
      //   300: fstore #6
      //   302: fload #13
      //   304: fstore #7
      //   306: fload #14
      //   308: fstore #8
      //   310: goto -> 340
      //   313: bipush #6
      //   315: istore #15
      //   317: goto -> 324
      //   320: bipush #7
      //   322: istore #15
      //   324: fload #14
      //   326: fstore #8
      //   328: fload #13
      //   330: fstore #7
      //   332: fload #12
      //   334: fstore #6
      //   336: fload #11
      //   338: fstore #5
      //   340: iconst_0
      //   341: istore #17
      //   343: iload_2
      //   344: istore #16
      //   346: fload #9
      //   348: fstore #11
      //   350: fload #10
      //   352: fstore #12
      //   354: iload #17
      //   356: istore_2
      //   357: iload_3
      //   358: istore #17
      //   360: iload_2
      //   361: aload #4
      //   363: arraylength
      //   364: if_icmpge -> 2131
      //   367: iload #17
      //   369: bipush #65
      //   371: if_icmpeq -> 1977
      //   374: iload #17
      //   376: bipush #67
      //   378: if_icmpeq -> 1861
      //   381: iload #17
      //   383: bipush #72
      //   385: if_icmpeq -> 1835
      //   388: iload #17
      //   390: bipush #81
      //   392: if_icmpeq -> 1741
      //   395: iload #17
      //   397: bipush #86
      //   399: if_icmpeq -> 1715
      //   402: iload #17
      //   404: bipush #97
      //   406: if_icmpeq -> 1575
      //   409: iload #17
      //   411: bipush #99
      //   413: if_icmpeq -> 1432
      //   416: iload #17
      //   418: bipush #104
      //   420: if_icmpeq -> 1404
      //   423: iload #17
      //   425: bipush #113
      //   427: if_icmpeq -> 1304
      //   430: iload #17
      //   432: bipush #118
      //   434: if_icmpeq -> 1279
      //   437: iload #17
      //   439: bipush #76
      //   441: if_icmpeq -> 1234
      //   444: iload #17
      //   446: bipush #77
      //   448: if_icmpeq -> 1164
      //   451: iload #17
      //   453: bipush #83
      //   455: if_icmpeq -> 1019
      //   458: iload #17
      //   460: bipush #84
      //   462: if_icmpeq -> 908
      //   465: iload #17
      //   467: bipush #108
      //   469: if_icmpeq -> 853
      //   472: iload #17
      //   474: bipush #109
      //   476: if_icmpeq -> 785
      //   479: iload #17
      //   481: bipush #115
      //   483: if_icmpeq -> 627
      //   486: iload #17
      //   488: bipush #116
      //   490: if_icmpeq -> 496
      //   493: goto -> 2120
      //   496: iload #16
      //   498: bipush #113
      //   500: if_icmpeq -> 536
      //   503: iload #16
      //   505: bipush #116
      //   507: if_icmpeq -> 536
      //   510: iload #16
      //   512: bipush #81
      //   514: if_icmpeq -> 536
      //   517: iload #16
      //   519: bipush #84
      //   521: if_icmpne -> 527
      //   524: goto -> 536
      //   527: fconst_0
      //   528: fstore #8
      //   530: fconst_0
      //   531: fstore #7
      //   533: goto -> 550
      //   536: fload #5
      //   538: fload #7
      //   540: fsub
      //   541: fstore #7
      //   543: fload #6
      //   545: fload #8
      //   547: fsub
      //   548: fstore #8
      //   550: iload_2
      //   551: iconst_0
      //   552: iadd
      //   553: istore #16
      //   555: aload #4
      //   557: iload #16
      //   559: faload
      //   560: fstore #9
      //   562: iload_2
      //   563: iconst_1
      //   564: iadd
      //   565: istore #17
      //   567: aload_0
      //   568: fload #7
      //   570: fload #8
      //   572: fload #9
      //   574: aload #4
      //   576: iload #17
      //   578: faload
      //   579: invokevirtual rQuadTo : (FFFF)V
      //   582: fload #5
      //   584: aload #4
      //   586: iload #16
      //   588: faload
      //   589: fadd
      //   590: fstore #9
      //   592: fload #6
      //   594: aload #4
      //   596: iload #17
      //   598: faload
      //   599: fadd
      //   600: fstore #10
      //   602: fload #8
      //   604: fload #6
      //   606: fadd
      //   607: fstore #8
      //   609: fload #7
      //   611: fload #5
      //   613: fadd
      //   614: fstore #7
      //   616: fload #10
      //   618: fstore #6
      //   620: fload #9
      //   622: fstore #5
      //   624: goto -> 493
      //   627: iload #16
      //   629: bipush #99
      //   631: if_icmpeq -> 667
      //   634: iload #16
      //   636: bipush #115
      //   638: if_icmpeq -> 667
      //   641: iload #16
      //   643: bipush #67
      //   645: if_icmpeq -> 667
      //   648: iload #16
      //   650: bipush #83
      //   652: if_icmpne -> 658
      //   655: goto -> 667
      //   658: fconst_0
      //   659: fstore #7
      //   661: fconst_0
      //   662: fstore #8
      //   664: goto -> 681
      //   667: fload #6
      //   669: fload #8
      //   671: fsub
      //   672: fstore #8
      //   674: fload #5
      //   676: fload #7
      //   678: fsub
      //   679: fstore #7
      //   681: iload_2
      //   682: iconst_0
      //   683: iadd
      //   684: istore #16
      //   686: aload #4
      //   688: iload #16
      //   690: faload
      //   691: fstore #9
      //   693: iload_2
      //   694: iconst_1
      //   695: iadd
      //   696: istore #17
      //   698: aload #4
      //   700: iload #17
      //   702: faload
      //   703: fstore #10
      //   705: iload_2
      //   706: iconst_2
      //   707: iadd
      //   708: istore #18
      //   710: aload #4
      //   712: iload #18
      //   714: faload
      //   715: fstore #13
      //   717: iload_2
      //   718: iconst_3
      //   719: iadd
      //   720: istore #19
      //   722: aload_0
      //   723: fload #7
      //   725: fload #8
      //   727: fload #9
      //   729: fload #10
      //   731: fload #13
      //   733: aload #4
      //   735: iload #19
      //   737: faload
      //   738: invokevirtual rCubicTo : (FFFFFF)V
      //   741: aload #4
      //   743: iload #16
      //   745: faload
      //   746: fload #5
      //   748: fadd
      //   749: fstore #10
      //   751: aload #4
      //   753: iload #17
      //   755: faload
      //   756: fload #6
      //   758: fadd
      //   759: fstore #7
      //   761: fload #5
      //   763: aload #4
      //   765: iload #18
      //   767: faload
      //   768: fadd
      //   769: fstore #8
      //   771: aload #4
      //   773: iload #19
      //   775: faload
      //   776: fstore #9
      //   778: fload #10
      //   780: fstore #5
      //   782: goto -> 1549
      //   785: iload_2
      //   786: iconst_0
      //   787: iadd
      //   788: istore #16
      //   790: fload #5
      //   792: aload #4
      //   794: iload #16
      //   796: faload
      //   797: fadd
      //   798: fstore #5
      //   800: iload_2
      //   801: iconst_1
      //   802: iadd
      //   803: istore #17
      //   805: fload #6
      //   807: aload #4
      //   809: iload #17
      //   811: faload
      //   812: fadd
      //   813: fstore #6
      //   815: iload_2
      //   816: ifle -> 836
      //   819: aload_0
      //   820: aload #4
      //   822: iload #16
      //   824: faload
      //   825: aload #4
      //   827: iload #17
      //   829: faload
      //   830: invokevirtual rLineTo : (FF)V
      //   833: goto -> 493
      //   836: aload_0
      //   837: aload #4
      //   839: iload #16
      //   841: faload
      //   842: aload #4
      //   844: iload #17
      //   846: faload
      //   847: invokevirtual rMoveTo : (FF)V
      //   850: goto -> 1223
      //   853: iload_2
      //   854: iconst_0
      //   855: iadd
      //   856: istore #16
      //   858: aload #4
      //   860: iload #16
      //   862: faload
      //   863: fstore #9
      //   865: iload_2
      //   866: iconst_1
      //   867: iadd
      //   868: istore #17
      //   870: aload_0
      //   871: fload #9
      //   873: aload #4
      //   875: iload #17
      //   877: faload
      //   878: invokevirtual rLineTo : (FF)V
      //   881: fload #5
      //   883: aload #4
      //   885: iload #16
      //   887: faload
      //   888: fadd
      //   889: fstore #5
      //   891: aload #4
      //   893: iload #17
      //   895: faload
      //   896: fstore #9
      //   898: fload #6
      //   900: fload #9
      //   902: fadd
      //   903: fstore #6
      //   905: goto -> 493
      //   908: iload #16
      //   910: bipush #113
      //   912: if_icmpeq -> 944
      //   915: iload #16
      //   917: bipush #116
      //   919: if_icmpeq -> 944
      //   922: iload #16
      //   924: bipush #81
      //   926: if_icmpeq -> 944
      //   929: fload #6
      //   931: fstore #10
      //   933: fload #5
      //   935: fstore #9
      //   937: iload #16
      //   939: bipush #84
      //   941: if_icmpne -> 962
      //   944: fload #5
      //   946: fconst_2
      //   947: fmul
      //   948: fload #7
      //   950: fsub
      //   951: fstore #9
      //   953: fload #6
      //   955: fconst_2
      //   956: fmul
      //   957: fload #8
      //   959: fsub
      //   960: fstore #10
      //   962: iload_2
      //   963: iconst_0
      //   964: iadd
      //   965: istore #16
      //   967: aload #4
      //   969: iload #16
      //   971: faload
      //   972: fstore #5
      //   974: iload_2
      //   975: iconst_1
      //   976: iadd
      //   977: istore #17
      //   979: aload_0
      //   980: fload #9
      //   982: fload #10
      //   984: fload #5
      //   986: aload #4
      //   988: iload #17
      //   990: faload
      //   991: invokevirtual quadTo : (FFFF)V
      //   994: aload #4
      //   996: iload #16
      //   998: faload
      //   999: fstore #5
      //   1001: aload #4
      //   1003: iload #17
      //   1005: faload
      //   1006: fstore #6
      //   1008: fload #10
      //   1010: fstore #8
      //   1012: fload #9
      //   1014: fstore #7
      //   1016: goto -> 2120
      //   1019: iload #16
      //   1021: bipush #99
      //   1023: if_icmpeq -> 1055
      //   1026: iload #16
      //   1028: bipush #115
      //   1030: if_icmpeq -> 1055
      //   1033: iload #16
      //   1035: bipush #67
      //   1037: if_icmpeq -> 1055
      //   1040: fload #6
      //   1042: fstore #10
      //   1044: fload #5
      //   1046: fstore #9
      //   1048: iload #16
      //   1050: bipush #83
      //   1052: if_icmpne -> 1073
      //   1055: fload #5
      //   1057: fconst_2
      //   1058: fmul
      //   1059: fload #7
      //   1061: fsub
      //   1062: fstore #9
      //   1064: fload #6
      //   1066: fconst_2
      //   1067: fmul
      //   1068: fload #8
      //   1070: fsub
      //   1071: fstore #10
      //   1073: iload_2
      //   1074: iconst_0
      //   1075: iadd
      //   1076: istore #16
      //   1078: aload #4
      //   1080: iload #16
      //   1082: faload
      //   1083: fstore #5
      //   1085: iload_2
      //   1086: iconst_1
      //   1087: iadd
      //   1088: istore #17
      //   1090: aload #4
      //   1092: iload #17
      //   1094: faload
      //   1095: fstore #6
      //   1097: iload_2
      //   1098: iconst_2
      //   1099: iadd
      //   1100: istore #18
      //   1102: aload #4
      //   1104: iload #18
      //   1106: faload
      //   1107: fstore #7
      //   1109: iload_2
      //   1110: iconst_3
      //   1111: iadd
      //   1112: istore #19
      //   1114: aload_0
      //   1115: fload #9
      //   1117: fload #10
      //   1119: fload #5
      //   1121: fload #6
      //   1123: fload #7
      //   1125: aload #4
      //   1127: iload #19
      //   1129: faload
      //   1130: invokevirtual cubicTo : (FFFFFF)V
      //   1133: aload #4
      //   1135: iload #16
      //   1137: faload
      //   1138: fstore #5
      //   1140: aload #4
      //   1142: iload #17
      //   1144: faload
      //   1145: fstore #7
      //   1147: aload #4
      //   1149: iload #18
      //   1151: faload
      //   1152: fstore #9
      //   1154: aload #4
      //   1156: iload #19
      //   1158: faload
      //   1159: fstore #6
      //   1161: goto -> 1560
      //   1164: iload_2
      //   1165: iconst_0
      //   1166: iadd
      //   1167: istore #16
      //   1169: aload #4
      //   1171: iload #16
      //   1173: faload
      //   1174: fstore #5
      //   1176: iload_2
      //   1177: iconst_1
      //   1178: iadd
      //   1179: istore #17
      //   1181: aload #4
      //   1183: iload #17
      //   1185: faload
      //   1186: fstore #6
      //   1188: iload_2
      //   1189: ifle -> 1209
      //   1192: aload_0
      //   1193: aload #4
      //   1195: iload #16
      //   1197: faload
      //   1198: aload #4
      //   1200: iload #17
      //   1202: faload
      //   1203: invokevirtual lineTo : (FF)V
      //   1206: goto -> 493
      //   1209: aload_0
      //   1210: aload #4
      //   1212: iload #16
      //   1214: faload
      //   1215: aload #4
      //   1217: iload #17
      //   1219: faload
      //   1220: invokevirtual moveTo : (FF)V
      //   1223: fload #6
      //   1225: fstore #11
      //   1227: fload #5
      //   1229: fstore #12
      //   1231: goto -> 2120
      //   1234: iload_2
      //   1235: iconst_0
      //   1236: iadd
      //   1237: istore #16
      //   1239: aload #4
      //   1241: iload #16
      //   1243: faload
      //   1244: fstore #5
      //   1246: iload_2
      //   1247: iconst_1
      //   1248: iadd
      //   1249: istore #17
      //   1251: aload_0
      //   1252: fload #5
      //   1254: aload #4
      //   1256: iload #17
      //   1258: faload
      //   1259: invokevirtual lineTo : (FF)V
      //   1262: aload #4
      //   1264: iload #16
      //   1266: faload
      //   1267: fstore #5
      //   1269: aload #4
      //   1271: iload #17
      //   1273: faload
      //   1274: fstore #6
      //   1276: goto -> 493
      //   1279: iload_2
      //   1280: iconst_0
      //   1281: iadd
      //   1282: istore #16
      //   1284: aload_0
      //   1285: fconst_0
      //   1286: aload #4
      //   1288: iload #16
      //   1290: faload
      //   1291: invokevirtual rLineTo : (FF)V
      //   1294: aload #4
      //   1296: iload #16
      //   1298: faload
      //   1299: fstore #9
      //   1301: goto -> 898
      //   1304: iload_2
      //   1305: iconst_0
      //   1306: iadd
      //   1307: istore #16
      //   1309: aload #4
      //   1311: iload #16
      //   1313: faload
      //   1314: fstore #7
      //   1316: iload_2
      //   1317: iconst_1
      //   1318: iadd
      //   1319: istore #17
      //   1321: aload #4
      //   1323: iload #17
      //   1325: faload
      //   1326: fstore #8
      //   1328: iload_2
      //   1329: iconst_2
      //   1330: iadd
      //   1331: istore #18
      //   1333: aload #4
      //   1335: iload #18
      //   1337: faload
      //   1338: fstore #9
      //   1340: iload_2
      //   1341: iconst_3
      //   1342: iadd
      //   1343: istore #19
      //   1345: aload_0
      //   1346: fload #7
      //   1348: fload #8
      //   1350: fload #9
      //   1352: aload #4
      //   1354: iload #19
      //   1356: faload
      //   1357: invokevirtual rQuadTo : (FFFF)V
      //   1360: aload #4
      //   1362: iload #16
      //   1364: faload
      //   1365: fload #5
      //   1367: fadd
      //   1368: fstore #10
      //   1370: aload #4
      //   1372: iload #17
      //   1374: faload
      //   1375: fload #6
      //   1377: fadd
      //   1378: fstore #7
      //   1380: fload #5
      //   1382: aload #4
      //   1384: iload #18
      //   1386: faload
      //   1387: fadd
      //   1388: fstore #8
      //   1390: aload #4
      //   1392: iload #19
      //   1394: faload
      //   1395: fstore #9
      //   1397: fload #10
      //   1399: fstore #5
      //   1401: goto -> 1549
      //   1404: iload_2
      //   1405: iconst_0
      //   1406: iadd
      //   1407: istore #16
      //   1409: aload_0
      //   1410: aload #4
      //   1412: iload #16
      //   1414: faload
      //   1415: fconst_0
      //   1416: invokevirtual rLineTo : (FF)V
      //   1419: fload #5
      //   1421: aload #4
      //   1423: iload #16
      //   1425: faload
      //   1426: fadd
      //   1427: fstore #5
      //   1429: goto -> 493
      //   1432: aload #4
      //   1434: iload_2
      //   1435: iconst_0
      //   1436: iadd
      //   1437: faload
      //   1438: fstore #7
      //   1440: aload #4
      //   1442: iload_2
      //   1443: iconst_1
      //   1444: iadd
      //   1445: faload
      //   1446: fstore #8
      //   1448: iload_2
      //   1449: iconst_2
      //   1450: iadd
      //   1451: istore #16
      //   1453: aload #4
      //   1455: iload #16
      //   1457: faload
      //   1458: fstore #9
      //   1460: iload_2
      //   1461: iconst_3
      //   1462: iadd
      //   1463: istore #17
      //   1465: aload #4
      //   1467: iload #17
      //   1469: faload
      //   1470: fstore #10
      //   1472: iload_2
      //   1473: iconst_4
      //   1474: iadd
      //   1475: istore #18
      //   1477: aload #4
      //   1479: iload #18
      //   1481: faload
      //   1482: fstore #13
      //   1484: iload_2
      //   1485: iconst_5
      //   1486: iadd
      //   1487: istore #19
      //   1489: aload_0
      //   1490: fload #7
      //   1492: fload #8
      //   1494: fload #9
      //   1496: fload #10
      //   1498: fload #13
      //   1500: aload #4
      //   1502: iload #19
      //   1504: faload
      //   1505: invokevirtual rCubicTo : (FFFFFF)V
      //   1508: aload #4
      //   1510: iload #16
      //   1512: faload
      //   1513: fload #5
      //   1515: fadd
      //   1516: fstore #10
      //   1518: aload #4
      //   1520: iload #17
      //   1522: faload
      //   1523: fload #6
      //   1525: fadd
      //   1526: fstore #7
      //   1528: fload #5
      //   1530: aload #4
      //   1532: iload #18
      //   1534: faload
      //   1535: fadd
      //   1536: fstore #8
      //   1538: aload #4
      //   1540: iload #19
      //   1542: faload
      //   1543: fstore #9
      //   1545: fload #10
      //   1547: fstore #5
      //   1549: fload #6
      //   1551: fload #9
      //   1553: fadd
      //   1554: fstore #6
      //   1556: fload #8
      //   1558: fstore #9
      //   1560: fload #7
      //   1562: fstore #8
      //   1564: fload #5
      //   1566: fstore #7
      //   1568: fload #9
      //   1570: fstore #5
      //   1572: goto -> 493
      //   1575: iload_2
      //   1576: iconst_5
      //   1577: iadd
      //   1578: istore #16
      //   1580: aload #4
      //   1582: iload #16
      //   1584: faload
      //   1585: fstore #7
      //   1587: iload_2
      //   1588: bipush #6
      //   1590: iadd
      //   1591: istore #17
      //   1593: aload #4
      //   1595: iload #17
      //   1597: faload
      //   1598: fstore #8
      //   1600: aload #4
      //   1602: iload_2
      //   1603: iconst_0
      //   1604: iadd
      //   1605: faload
      //   1606: fstore #9
      //   1608: aload #4
      //   1610: iload_2
      //   1611: iconst_1
      //   1612: iadd
      //   1613: faload
      //   1614: fstore #10
      //   1616: aload #4
      //   1618: iload_2
      //   1619: iconst_2
      //   1620: iadd
      //   1621: faload
      //   1622: fstore #13
      //   1624: aload #4
      //   1626: iload_2
      //   1627: iconst_3
      //   1628: iadd
      //   1629: faload
      //   1630: fconst_0
      //   1631: fcmpl
      //   1632: ifeq -> 1641
      //   1635: iconst_1
      //   1636: istore #20
      //   1638: goto -> 1644
      //   1641: iconst_0
      //   1642: istore #20
      //   1644: aload #4
      //   1646: iload_2
      //   1647: iconst_4
      //   1648: iadd
      //   1649: faload
      //   1650: fconst_0
      //   1651: fcmpl
      //   1652: ifeq -> 1661
      //   1655: iconst_1
      //   1656: istore #21
      //   1658: goto -> 1664
      //   1661: iconst_0
      //   1662: istore #21
      //   1664: aload_0
      //   1665: fload #5
      //   1667: fload #6
      //   1669: fload #7
      //   1671: fload #5
      //   1673: fadd
      //   1674: fload #8
      //   1676: fload #6
      //   1678: fadd
      //   1679: fload #9
      //   1681: fload #10
      //   1683: fload #13
      //   1685: iload #20
      //   1687: iload #21
      //   1689: invokestatic drawArc : (Landroid/graphics/Path;FFFFFFFZZ)V
      //   1692: fload #5
      //   1694: aload #4
      //   1696: iload #16
      //   1698: faload
      //   1699: fadd
      //   1700: fstore #5
      //   1702: fload #6
      //   1704: aload #4
      //   1706: iload #17
      //   1708: faload
      //   1709: fadd
      //   1710: fstore #6
      //   1712: goto -> 2112
      //   1715: iload_2
      //   1716: iconst_0
      //   1717: iadd
      //   1718: istore #16
      //   1720: aload_0
      //   1721: fload #5
      //   1723: aload #4
      //   1725: iload #16
      //   1727: faload
      //   1728: invokevirtual lineTo : (FF)V
      //   1731: aload #4
      //   1733: iload #16
      //   1735: faload
      //   1736: fstore #6
      //   1738: goto -> 2120
      //   1741: iload_2
      //   1742: istore #16
      //   1744: iload #16
      //   1746: iconst_0
      //   1747: iadd
      //   1748: istore #17
      //   1750: aload #4
      //   1752: iload #17
      //   1754: faload
      //   1755: fstore #5
      //   1757: iload #16
      //   1759: iconst_1
      //   1760: iadd
      //   1761: istore #18
      //   1763: aload #4
      //   1765: iload #18
      //   1767: faload
      //   1768: fstore #6
      //   1770: iload #16
      //   1772: iconst_2
      //   1773: iadd
      //   1774: istore #19
      //   1776: aload #4
      //   1778: iload #19
      //   1780: faload
      //   1781: fstore #7
      //   1783: iload #16
      //   1785: iconst_3
      //   1786: iadd
      //   1787: istore #16
      //   1789: aload_0
      //   1790: fload #5
      //   1792: fload #6
      //   1794: fload #7
      //   1796: aload #4
      //   1798: iload #16
      //   1800: faload
      //   1801: invokevirtual quadTo : (FFFF)V
      //   1804: aload #4
      //   1806: iload #17
      //   1808: faload
      //   1809: fstore #7
      //   1811: aload #4
      //   1813: iload #18
      //   1815: faload
      //   1816: fstore #8
      //   1818: aload #4
      //   1820: iload #19
      //   1822: faload
      //   1823: fstore #5
      //   1825: aload #4
      //   1827: iload #16
      //   1829: faload
      //   1830: fstore #6
      //   1832: goto -> 2120
      //   1835: iload_2
      //   1836: iconst_0
      //   1837: iadd
      //   1838: istore #16
      //   1840: aload_0
      //   1841: aload #4
      //   1843: iload #16
      //   1845: faload
      //   1846: fload #6
      //   1848: invokevirtual lineTo : (FF)V
      //   1851: aload #4
      //   1853: iload #16
      //   1855: faload
      //   1856: fstore #5
      //   1858: goto -> 2120
      //   1861: iload_2
      //   1862: istore #16
      //   1864: aload #4
      //   1866: iload #16
      //   1868: iconst_0
      //   1869: iadd
      //   1870: faload
      //   1871: fstore #5
      //   1873: aload #4
      //   1875: iload #16
      //   1877: iconst_1
      //   1878: iadd
      //   1879: faload
      //   1880: fstore #6
      //   1882: iload #16
      //   1884: iconst_2
      //   1885: iadd
      //   1886: istore #17
      //   1888: aload #4
      //   1890: iload #17
      //   1892: faload
      //   1893: fstore #7
      //   1895: iload #16
      //   1897: iconst_3
      //   1898: iadd
      //   1899: istore #18
      //   1901: aload #4
      //   1903: iload #18
      //   1905: faload
      //   1906: fstore #8
      //   1908: iload #16
      //   1910: iconst_4
      //   1911: iadd
      //   1912: istore #19
      //   1914: aload #4
      //   1916: iload #19
      //   1918: faload
      //   1919: fstore #9
      //   1921: iload #16
      //   1923: iconst_5
      //   1924: iadd
      //   1925: istore #16
      //   1927: aload_0
      //   1928: fload #5
      //   1930: fload #6
      //   1932: fload #7
      //   1934: fload #8
      //   1936: fload #9
      //   1938: aload #4
      //   1940: iload #16
      //   1942: faload
      //   1943: invokevirtual cubicTo : (FFFFFF)V
      //   1946: aload #4
      //   1948: iload #19
      //   1950: faload
      //   1951: fstore #5
      //   1953: aload #4
      //   1955: iload #16
      //   1957: faload
      //   1958: fstore #6
      //   1960: aload #4
      //   1962: iload #17
      //   1964: faload
      //   1965: fstore #7
      //   1967: aload #4
      //   1969: iload #18
      //   1971: faload
      //   1972: fstore #8
      //   1974: goto -> 2120
      //   1977: iload_2
      //   1978: istore #16
      //   1980: iload #16
      //   1982: iconst_5
      //   1983: iadd
      //   1984: istore #17
      //   1986: aload #4
      //   1988: iload #17
      //   1990: faload
      //   1991: fstore #7
      //   1993: iload #16
      //   1995: bipush #6
      //   1997: iadd
      //   1998: istore #18
      //   2000: aload #4
      //   2002: iload #18
      //   2004: faload
      //   2005: fstore #8
      //   2007: aload #4
      //   2009: iload #16
      //   2011: iconst_0
      //   2012: iadd
      //   2013: faload
      //   2014: fstore #9
      //   2016: aload #4
      //   2018: iload #16
      //   2020: iconst_1
      //   2021: iadd
      //   2022: faload
      //   2023: fstore #10
      //   2025: aload #4
      //   2027: iload #16
      //   2029: iconst_2
      //   2030: iadd
      //   2031: faload
      //   2032: fstore #13
      //   2034: aload #4
      //   2036: iload #16
      //   2038: iconst_3
      //   2039: iadd
      //   2040: faload
      //   2041: fconst_0
      //   2042: fcmpl
      //   2043: ifeq -> 2052
      //   2046: iconst_1
      //   2047: istore #20
      //   2049: goto -> 2055
      //   2052: iconst_0
      //   2053: istore #20
      //   2055: aload #4
      //   2057: iload #16
      //   2059: iconst_4
      //   2060: iadd
      //   2061: faload
      //   2062: fconst_0
      //   2063: fcmpl
      //   2064: ifeq -> 2073
      //   2067: iconst_1
      //   2068: istore #21
      //   2070: goto -> 2076
      //   2073: iconst_0
      //   2074: istore #21
      //   2076: aload_0
      //   2077: fload #5
      //   2079: fload #6
      //   2081: fload #7
      //   2083: fload #8
      //   2085: fload #9
      //   2087: fload #10
      //   2089: fload #13
      //   2091: iload #20
      //   2093: iload #21
      //   2095: invokestatic drawArc : (Landroid/graphics/Path;FFFFFFFZZ)V
      //   2098: aload #4
      //   2100: iload #17
      //   2102: faload
      //   2103: fstore #5
      //   2105: aload #4
      //   2107: iload #18
      //   2109: faload
      //   2110: fstore #6
      //   2112: fload #6
      //   2114: fstore #8
      //   2116: fload #5
      //   2118: fstore #7
      //   2120: iload_2
      //   2121: iload #15
      //   2123: iadd
      //   2124: istore_2
      //   2125: iload_3
      //   2126: istore #16
      //   2128: goto -> 357
      //   2131: aload_1
      //   2132: iconst_0
      //   2133: fload #5
      //   2135: fastore
      //   2136: aload_1
      //   2137: iconst_1
      //   2138: fload #6
      //   2140: fastore
      //   2141: aload_1
      //   2142: iconst_2
      //   2143: fload #7
      //   2145: fastore
      //   2146: aload_1
      //   2147: iconst_3
      //   2148: fload #8
      //   2150: fastore
      //   2151: aload_1
      //   2152: iconst_4
      //   2153: fload #12
      //   2155: fastore
      //   2156: aload_1
      //   2157: iconst_5
      //   2158: fload #11
      //   2160: fastore
      //   2161: return
    }
    
    private static void arcToBezier(Path param1Path, double param1Double1, double param1Double2, double param1Double3, double param1Double4, double param1Double5, double param1Double6, double param1Double7, double param1Double8, double param1Double9) {
      int i = (int)Math.ceil(Math.abs(param1Double9 * 4.0D / Math.PI));
      double d4 = Math.cos(param1Double7);
      double d5 = Math.sin(param1Double7);
      double d2 = Math.cos(param1Double8);
      double d3 = Math.sin(param1Double8);
      param1Double7 = -param1Double3;
      double d7 = param1Double7 * d4;
      double d8 = param1Double4 * d5;
      param1Double7 *= d5;
      double d9 = param1Double4 * d4;
      double d6 = param1Double9 / i;
      double d1 = d3 * param1Double7 + d2 * d9;
      d2 = d7 * d3 - d8 * d2;
      int j = 0;
      d3 = param1Double8;
      param1Double9 = param1Double6;
      param1Double4 = param1Double7;
      param1Double8 = param1Double5;
      param1Double7 = d6;
      param1Double6 = d5;
      param1Double5 = d4;
      while (true) {
        d4 = param1Double3;
        if (j < i) {
          d6 = d3 + param1Double7;
          double d10 = Math.sin(d6);
          double d12 = Math.cos(d6);
          double d11 = param1Double1 + d4 * param1Double5 * d12 - d8 * d10;
          d4 = param1Double2 + d4 * param1Double6 * d12 + d9 * d10;
          d5 = d7 * d10 - d8 * d12;
          d10 = d10 * param1Double4 + d12 * d9;
          d3 = d6 - d3;
          d12 = Math.tan(d3 / 2.0D);
          d3 = Math.sin(d3) * (Math.sqrt(d12 * 3.0D * d12 + 4.0D) - 1.0D) / 3.0D;
          param1Path.rLineTo(0.0F, 0.0F);
          param1Path.cubicTo((float)(param1Double8 + d2 * d3), (float)(param1Double9 + d1 * d3), (float)(d11 - d3 * d5), (float)(d4 - d3 * d10), (float)d11, (float)d4);
          j++;
          param1Double8 = d11;
          d3 = d6;
          d1 = d10;
          d2 = d5;
          param1Double9 = d4;
          continue;
        } 
        break;
      } 
    }
    
    private static void drawArc(Path param1Path, float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6, float param1Float7, boolean param1Boolean1, boolean param1Boolean2) {
      double d5 = Math.toRadians(param1Float7);
      double d6 = Math.cos(d5);
      double d7 = Math.sin(d5);
      double d8 = param1Float1;
      double d9 = param1Float2;
      double d10 = param1Float5;
      double d1 = (d8 * d6 + d9 * d7) / d10;
      double d2 = -param1Float1;
      double d11 = param1Float6;
      double d4 = (d2 * d7 + d9 * d6) / d11;
      double d3 = param1Float3;
      d2 = param1Float4;
      double d12 = (d3 * d6 + d2 * d7) / d10;
      double d13 = (-param1Float3 * d7 + d2 * d6) / d11;
      double d15 = d1 - d12;
      double d14 = d4 - d13;
      d3 = (d1 + d12) / 2.0D;
      d2 = (d4 + d13) / 2.0D;
      double d16 = d15 * d15 + d14 * d14;
      if (d16 == 0.0D) {
        Log.w("PathParser", " Points are coincident");
        return;
      } 
      double d17 = 1.0D / d16 - 0.25D;
      if (d17 < 0.0D) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Points are too far apart ");
        stringBuilder.append(d16);
        Log.w("PathParser", stringBuilder.toString());
        float f = (float)(Math.sqrt(d16) / 1.99999D);
        drawArc(param1Path, param1Float1, param1Float2, param1Float3, param1Float4, param1Float5 * f, param1Float6 * f, param1Float7, param1Boolean1, param1Boolean2);
        return;
      } 
      d16 = Math.sqrt(d17);
      d15 *= d16;
      d14 = d16 * d14;
      if (param1Boolean1 == param1Boolean2) {
        d3 -= d14;
        d2 += d15;
      } else {
        d3 += d14;
        d2 -= d15;
      } 
      d14 = Math.atan2(d4 - d2, d1 - d3);
      d4 = Math.atan2(d13 - d2, d12 - d3) - d14;
      int i = d4 cmp 0.0D;
      if (i >= 0) {
        param1Boolean1 = true;
      } else {
        param1Boolean1 = false;
      } 
      d1 = d4;
      if (param1Boolean2 != param1Boolean1)
        if (i > 0) {
          d1 = d4 - 6.283185307179586D;
        } else {
          d1 = d4 + 6.283185307179586D;
        }  
      d3 *= d10;
      d2 *= d11;
      arcToBezier(param1Path, d3 * d6 - d2 * d7, d3 * d7 + d2 * d6, d10, d11, d8, d9, d5, d14, d1);
    }
    
    public static void nodesToPath(PathDataNode[] param1ArrayOfPathDataNode, Path param1Path) {
      float[] arrayOfFloat = new float[6];
      char c = 'm';
      for (int i = 0; i < param1ArrayOfPathDataNode.length; i++) {
        addCommand(param1Path, arrayOfFloat, c, (param1ArrayOfPathDataNode[i]).mType, (param1ArrayOfPathDataNode[i]).mParams);
        c = (param1ArrayOfPathDataNode[i]).mType;
      } 
    }
    
    public void interpolatePathDataNode(PathDataNode param1PathDataNode1, PathDataNode param1PathDataNode2, float param1Float) {
      this.mType = param1PathDataNode1.mType;
      int i = 0;
      while (true) {
        float[] arrayOfFloat = param1PathDataNode1.mParams;
        if (i < arrayOfFloat.length) {
          this.mParams[i] = arrayOfFloat[i] * (1.0F - param1Float) + param1PathDataNode2.mParams[i] * param1Float;
          i++;
          continue;
        } 
        break;
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\core\graphics\PathParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */