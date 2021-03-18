package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.android.dex.Leb128;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ByteInput;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvCode;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsnList;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.LocalList;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.PositionList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Prototype;

public class DebugInfoDecoder {
  private int address = 0;
  
  private final int codesize;
  
  private final Prototype desc;
  
  private final byte[] encoded;
  
  private final DexFile file;
  
  private final boolean isStatic;
  
  private final LocalEntry[] lastEntryForReg;
  
  private int line = 1;
  
  private final ArrayList<LocalEntry> locals;
  
  private final ArrayList<PositionEntry> positions;
  
  private final int regSize;
  
  private final int thisStringIdx;
  
  DebugInfoDecoder(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, boolean paramBoolean, CstMethodRef paramCstMethodRef, DexFile paramDexFile) {
    if (paramArrayOfbyte != null) {
      this.encoded = paramArrayOfbyte;
      this.isStatic = paramBoolean;
      this.desc = paramCstMethodRef.getPrototype();
      this.file = paramDexFile;
      this.regSize = paramInt2;
      this.positions = new ArrayList<PositionEntry>();
      this.locals = new ArrayList<LocalEntry>();
      this.codesize = paramInt1;
      this.lastEntryForReg = new LocalEntry[paramInt2];
      paramInt1 = -1;
      try {
        paramInt2 = paramDexFile.getStringIds().indexOf(new CstString("this"));
        paramInt1 = paramInt2;
      } catch (IllegalArgumentException illegalArgumentException) {}
      this.thisStringIdx = paramInt1;
      return;
    } 
    throw new NullPointerException("encoded == null");
  }
  
  private void decode0() throws IOException {
    // Byte code:
    //   0: new org/firstinspires/ftc/robotcore/internal/android/dex/util/ByteArrayByteInput
    //   3: dup
    //   4: aload_0
    //   5: getfield encoded : [B
    //   8: invokespecial <init> : ([B)V
    //   11: astore #6
    //   13: aload_0
    //   14: aload #6
    //   16: invokestatic readUnsignedLeb128 : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   19: putfield line : I
    //   22: aload #6
    //   24: invokestatic readUnsignedLeb128 : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   27: istore #4
    //   29: aload_0
    //   30: getfield desc : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Prototype;
    //   33: invokevirtual getParameterTypes : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/StdTypeList;
    //   36: astore #7
    //   38: aload_0
    //   39: invokespecial getParamBase : ()I
    //   42: istore_2
    //   43: iload #4
    //   45: aload #7
    //   47: invokevirtual size : ()I
    //   50: if_icmpne -> 822
    //   53: iload_2
    //   54: istore_1
    //   55: aload_0
    //   56: getfield isStatic : Z
    //   59: ifne -> 102
    //   62: new org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry
    //   65: dup
    //   66: iconst_0
    //   67: iconst_1
    //   68: iload_2
    //   69: aload_0
    //   70: getfield thisStringIdx : I
    //   73: iconst_0
    //   74: iconst_0
    //   75: invokespecial <init> : (IZIIII)V
    //   78: astore #5
    //   80: aload_0
    //   81: getfield locals : Ljava/util/ArrayList;
    //   84: aload #5
    //   86: invokevirtual add : (Ljava/lang/Object;)Z
    //   89: pop
    //   90: aload_0
    //   91: getfield lastEntryForReg : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry;
    //   94: iload_2
    //   95: aload #5
    //   97: aastore
    //   98: iload_2
    //   99: iconst_1
    //   100: iadd
    //   101: istore_1
    //   102: iconst_0
    //   103: istore_3
    //   104: iload_1
    //   105: istore_2
    //   106: iload_3
    //   107: istore_1
    //   108: iload_1
    //   109: iload #4
    //   111: if_icmpge -> 200
    //   114: aload #7
    //   116: iload_1
    //   117: invokevirtual getType : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   120: astore #8
    //   122: aload_0
    //   123: aload #6
    //   125: invokespecial readStringIndex : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   128: istore_3
    //   129: iload_3
    //   130: iconst_m1
    //   131: if_icmpne -> 152
    //   134: new org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry
    //   137: dup
    //   138: iconst_0
    //   139: iconst_1
    //   140: iload_2
    //   141: iconst_m1
    //   142: iconst_0
    //   143: iconst_0
    //   144: invokespecial <init> : (IZIIII)V
    //   147: astore #5
    //   149: goto -> 167
    //   152: new org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry
    //   155: dup
    //   156: iconst_0
    //   157: iconst_1
    //   158: iload_2
    //   159: iload_3
    //   160: iconst_0
    //   161: iconst_0
    //   162: invokespecial <init> : (IZIIII)V
    //   165: astore #5
    //   167: aload_0
    //   168: getfield locals : Ljava/util/ArrayList;
    //   171: aload #5
    //   173: invokevirtual add : (Ljava/lang/Object;)Z
    //   176: pop
    //   177: aload_0
    //   178: getfield lastEntryForReg : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry;
    //   181: iload_2
    //   182: aload #5
    //   184: aastore
    //   185: iload_2
    //   186: aload #8
    //   188: invokevirtual getCategory : ()I
    //   191: iadd
    //   192: istore_2
    //   193: iload_1
    //   194: iconst_1
    //   195: iadd
    //   196: istore_1
    //   197: goto -> 108
    //   200: aload #6
    //   202: invokeinterface readByte : ()B
    //   207: sipush #255
    //   210: iand
    //   211: istore_1
    //   212: iload_1
    //   213: tableswitch default -> 268, 0 -> 784, 1 -> 767, 2 -> 750, 3 -> 691, 4 -> 623, 5 -> 476, 6 -> 333, 7 -> 200, 8 -> 200, 9 -> 200
    //   268: iload_1
    //   269: bipush #10
    //   271: if_icmplt -> 785
    //   274: iload_1
    //   275: bipush #10
    //   277: isub
    //   278: istore_1
    //   279: aload_0
    //   280: aload_0
    //   281: getfield address : I
    //   284: iload_1
    //   285: bipush #15
    //   287: idiv
    //   288: iadd
    //   289: putfield address : I
    //   292: aload_0
    //   293: aload_0
    //   294: getfield line : I
    //   297: iload_1
    //   298: bipush #15
    //   300: irem
    //   301: iconst_4
    //   302: isub
    //   303: iadd
    //   304: putfield line : I
    //   307: aload_0
    //   308: getfield positions : Ljava/util/ArrayList;
    //   311: new org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$PositionEntry
    //   314: dup
    //   315: aload_0
    //   316: getfield address : I
    //   319: aload_0
    //   320: getfield line : I
    //   323: invokespecial <init> : (II)V
    //   326: invokevirtual add : (Ljava/lang/Object;)Z
    //   329: pop
    //   330: goto -> 200
    //   333: aload #6
    //   335: invokestatic readUnsignedLeb128 : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   338: istore_1
    //   339: aload_0
    //   340: getfield lastEntryForReg : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry;
    //   343: iload_1
    //   344: aaload
    //   345: astore #5
    //   347: aload #5
    //   349: getfield isStart : Z
    //   352: ifne -> 402
    //   355: new org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry
    //   358: dup
    //   359: aload_0
    //   360: getfield address : I
    //   363: iconst_1
    //   364: iload_1
    //   365: aload #5
    //   367: getfield nameIndex : I
    //   370: aload #5
    //   372: getfield typeIndex : I
    //   375: iconst_0
    //   376: invokespecial <init> : (IZIIII)V
    //   379: astore #5
    //   381: aload_0
    //   382: getfield locals : Ljava/util/ArrayList;
    //   385: aload #5
    //   387: invokevirtual add : (Ljava/lang/Object;)Z
    //   390: pop
    //   391: aload_0
    //   392: getfield lastEntryForReg : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry;
    //   395: iload_1
    //   396: aload #5
    //   398: aastore
    //   399: goto -> 200
    //   402: new java/lang/StringBuilder
    //   405: dup
    //   406: invokespecial <init> : ()V
    //   409: astore #5
    //   411: aload #5
    //   413: ldc 'nonsensical RESTART_LOCAL on live register v'
    //   415: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   418: pop
    //   419: aload #5
    //   421: iload_1
    //   422: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   425: pop
    //   426: new java/lang/RuntimeException
    //   429: dup
    //   430: aload #5
    //   432: invokevirtual toString : ()Ljava/lang/String;
    //   435: invokespecial <init> : (Ljava/lang/String;)V
    //   438: athrow
    //   439: new java/lang/StringBuilder
    //   442: dup
    //   443: invokespecial <init> : ()V
    //   446: astore #5
    //   448: aload #5
    //   450: ldc 'Encountered RESTART_LOCAL on new v'
    //   452: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   455: pop
    //   456: aload #5
    //   458: iload_1
    //   459: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   462: pop
    //   463: new java/lang/RuntimeException
    //   466: dup
    //   467: aload #5
    //   469: invokevirtual toString : ()Ljava/lang/String;
    //   472: invokespecial <init> : (Ljava/lang/String;)V
    //   475: athrow
    //   476: aload #6
    //   478: invokestatic readUnsignedLeb128 : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   481: istore_1
    //   482: aload_0
    //   483: getfield lastEntryForReg : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry;
    //   486: iload_1
    //   487: aaload
    //   488: astore #5
    //   490: aload #5
    //   492: getfield isStart : Z
    //   495: ifeq -> 549
    //   498: new org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry
    //   501: dup
    //   502: aload_0
    //   503: getfield address : I
    //   506: iconst_0
    //   507: iload_1
    //   508: aload #5
    //   510: getfield nameIndex : I
    //   513: aload #5
    //   515: getfield typeIndex : I
    //   518: aload #5
    //   520: getfield signatureIndex : I
    //   523: invokespecial <init> : (IZIIII)V
    //   526: astore #5
    //   528: aload_0
    //   529: getfield locals : Ljava/util/ArrayList;
    //   532: aload #5
    //   534: invokevirtual add : (Ljava/lang/Object;)Z
    //   537: pop
    //   538: aload_0
    //   539: getfield lastEntryForReg : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry;
    //   542: iload_1
    //   543: aload #5
    //   545: aastore
    //   546: goto -> 200
    //   549: new java/lang/StringBuilder
    //   552: dup
    //   553: invokespecial <init> : ()V
    //   556: astore #5
    //   558: aload #5
    //   560: ldc 'nonsensical END_LOCAL on dead register v'
    //   562: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   565: pop
    //   566: aload #5
    //   568: iload_1
    //   569: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   572: pop
    //   573: new java/lang/RuntimeException
    //   576: dup
    //   577: aload #5
    //   579: invokevirtual toString : ()Ljava/lang/String;
    //   582: invokespecial <init> : (Ljava/lang/String;)V
    //   585: athrow
    //   586: new java/lang/StringBuilder
    //   589: dup
    //   590: invokespecial <init> : ()V
    //   593: astore #5
    //   595: aload #5
    //   597: ldc 'Encountered END_LOCAL on new v'
    //   599: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   602: pop
    //   603: aload #5
    //   605: iload_1
    //   606: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   609: pop
    //   610: new java/lang/RuntimeException
    //   613: dup
    //   614: aload #5
    //   616: invokevirtual toString : ()Ljava/lang/String;
    //   619: invokespecial <init> : (Ljava/lang/String;)V
    //   622: athrow
    //   623: aload #6
    //   625: invokestatic readUnsignedLeb128 : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   628: istore_1
    //   629: aload_0
    //   630: aload #6
    //   632: invokespecial readStringIndex : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   635: istore_2
    //   636: aload_0
    //   637: aload #6
    //   639: invokespecial readStringIndex : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   642: istore_3
    //   643: aload_0
    //   644: aload #6
    //   646: invokespecial readStringIndex : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   649: istore #4
    //   651: new org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry
    //   654: dup
    //   655: aload_0
    //   656: getfield address : I
    //   659: iconst_1
    //   660: iload_1
    //   661: iload_2
    //   662: iload_3
    //   663: iload #4
    //   665: invokespecial <init> : (IZIIII)V
    //   668: astore #5
    //   670: aload_0
    //   671: getfield locals : Ljava/util/ArrayList;
    //   674: aload #5
    //   676: invokevirtual add : (Ljava/lang/Object;)Z
    //   679: pop
    //   680: aload_0
    //   681: getfield lastEntryForReg : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry;
    //   684: iload_1
    //   685: aload #5
    //   687: aastore
    //   688: goto -> 200
    //   691: aload #6
    //   693: invokestatic readUnsignedLeb128 : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   696: istore_1
    //   697: aload_0
    //   698: aload #6
    //   700: invokespecial readStringIndex : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   703: istore_2
    //   704: aload_0
    //   705: aload #6
    //   707: invokespecial readStringIndex : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   710: istore_3
    //   711: new org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry
    //   714: dup
    //   715: aload_0
    //   716: getfield address : I
    //   719: iconst_1
    //   720: iload_1
    //   721: iload_2
    //   722: iload_3
    //   723: iconst_0
    //   724: invokespecial <init> : (IZIIII)V
    //   727: astore #5
    //   729: aload_0
    //   730: getfield locals : Ljava/util/ArrayList;
    //   733: aload #5
    //   735: invokevirtual add : (Ljava/lang/Object;)Z
    //   738: pop
    //   739: aload_0
    //   740: getfield lastEntryForReg : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DebugInfoDecoder$LocalEntry;
    //   743: iload_1
    //   744: aload #5
    //   746: aastore
    //   747: goto -> 200
    //   750: aload_0
    //   751: aload_0
    //   752: getfield line : I
    //   755: aload #6
    //   757: invokestatic readSignedLeb128 : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   760: iadd
    //   761: putfield line : I
    //   764: goto -> 200
    //   767: aload_0
    //   768: aload_0
    //   769: getfield address : I
    //   772: aload #6
    //   774: invokestatic readUnsignedLeb128 : (Lorg/firstinspires/ftc/robotcore/internal/android/dex/util/ByteInput;)I
    //   777: iadd
    //   778: putfield address : I
    //   781: goto -> 200
    //   784: return
    //   785: new java/lang/StringBuilder
    //   788: dup
    //   789: invokespecial <init> : ()V
    //   792: astore #5
    //   794: aload #5
    //   796: ldc 'Invalid extended opcode encountered '
    //   798: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   801: pop
    //   802: aload #5
    //   804: iload_1
    //   805: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   808: pop
    //   809: new java/lang/RuntimeException
    //   812: dup
    //   813: aload #5
    //   815: invokevirtual toString : ()Ljava/lang/String;
    //   818: invokespecial <init> : (Ljava/lang/String;)V
    //   821: athrow
    //   822: new java/lang/RuntimeException
    //   825: dup
    //   826: ldc 'Mismatch between parameters_size and prototype'
    //   828: invokespecial <init> : (Ljava/lang/String;)V
    //   831: athrow
    //   832: astore #5
    //   834: goto -> 439
    //   837: astore #5
    //   839: goto -> 586
    // Exception table:
    //   from	to	target	type
    //   339	381	832	java/lang/NullPointerException
    //   402	439	832	java/lang/NullPointerException
    //   482	528	837	java/lang/NullPointerException
    //   549	586	837	java/lang/NullPointerException
  }
  
  private int getParamBase() {
    return this.regSize - this.desc.getParameterTypes().getWordCount() - (this.isStatic ^ true);
  }
  
  private int readStringIndex(ByteInput paramByteInput) throws IOException {
    return Leb128.readUnsignedLeb128(paramByteInput) - 1;
  }
  
  public static void validateEncode(byte[] paramArrayOfbyte, DexFile paramDexFile, CstMethodRef paramCstMethodRef, DalvCode paramDalvCode, boolean paramBoolean) {
    PositionList positionList = paramDalvCode.getPositions();
    LocalList localList = paramDalvCode.getLocals();
    DalvInsnList dalvInsnList = paramDalvCode.getInsns();
    int i = dalvInsnList.codeSize();
    int j = dalvInsnList.getRegistersSize();
    try {
      validateEncode0(paramArrayOfbyte, i, j, paramBoolean, paramCstMethodRef, paramDexFile, positionList, localList);
      return;
    } catch (RuntimeException runtimeException) {
      System.err.println("instructions:");
      dalvInsnList.debugPrint(System.err, "  ", true);
      System.err.println("local list:");
      localList.debugPrint(System.err, "  ");
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("while processing ");
      stringBuilder.append(paramCstMethodRef.toHuman());
      throw ExceptionWithContext.withContext(runtimeException, stringBuilder.toString());
    } 
  }
  
  private static void validateEncode0(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, boolean paramBoolean, CstMethodRef paramCstMethodRef, DexFile paramDexFile, PositionList paramPositionList, LocalList paramLocalList) {
    StringBuilder stringBuilder2;
    DebugInfoDecoder debugInfoDecoder = new DebugInfoDecoder(paramArrayOfbyte, paramInt1, paramInt2, paramBoolean, paramCstMethodRef, paramDexFile);
    debugInfoDecoder.decode();
    List<PositionEntry> list = debugInfoDecoder.getPositionList();
    if (list.size() == paramPositionList.size()) {
      Iterator<PositionEntry> iterator = list.iterator();
      while (true) {
        StringBuilder stringBuilder;
        paramBoolean = iterator.hasNext();
        boolean bool = false;
        int i = 0;
        if (paramBoolean) {
          PositionEntry positionEntry = iterator.next();
          paramInt1 = paramPositionList.size() - 1;
          while (true) {
            paramInt2 = i;
            if (paramInt1 >= 0) {
              PositionList.Entry entry = paramPositionList.get(paramInt1);
              if (positionEntry.line == entry.getPosition().getLine() && positionEntry.address == entry.getAddress()) {
                paramInt2 = 1;
                break;
              } 
              paramInt1--;
              continue;
            } 
            break;
          } 
          if (paramInt2 != 0)
            continue; 
          stringBuilder = new StringBuilder();
          stringBuilder.append("Could not match position entry: ");
          stringBuilder.append(positionEntry.address);
          stringBuilder.append(", ");
          stringBuilder.append(positionEntry.line);
          throw new RuntimeException(stringBuilder.toString());
        } 
        list = (List)stringBuilder.getLocals();
        int m = ((DebugInfoDecoder)stringBuilder).thisStringIdx;
        i = list.size();
        int k = stringBuilder.getParamBase();
        paramInt1 = 0;
        while (true) {
          paramInt1++;
          i = paramInt2;
        } 
        m = paramLocalList.size();
        int j = 0;
        paramInt1 = j;
        while (true) {
          paramInt2 = bool;
          if (j < m) {
            LocalList.Entry entry = paramLocalList.get(j);
            paramInt2 = paramInt1;
            if (entry.getDisposition() != LocalList.Disposition.END_REPLACED) {
              PrintStream printStream;
              LocalEntry localEntry;
              StringBuilder stringBuilder3;
              do {
                localEntry = (LocalEntry)list.get(paramInt2);
                if (localEntry.nameIndex >= 0) {
                  paramInt1 = paramInt2;
                  break;
                } 
                paramInt1 = paramInt2 + 1;
                paramInt2 = paramInt1;
              } while (paramInt1 < i);
              paramInt2 = localEntry.address;
              if (localEntry.reg != entry.getRegister()) {
                printStream = System.err;
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("local register mismatch at orig ");
                stringBuilder3.append(j);
                stringBuilder3.append(" / decoded ");
                stringBuilder3.append(paramInt1);
                printStream.println(stringBuilder3.toString());
              } else if (((LocalEntry)stringBuilder3).isStart != printStream.isStart()) {
                printStream = System.err;
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("local start/end mismatch at orig ");
                stringBuilder3.append(j);
                stringBuilder3.append(" / decoded ");
                stringBuilder3.append(paramInt1);
                printStream.println(stringBuilder3.toString());
              } else if (paramInt2 != printStream.getAddress() && (paramInt2 != 0 || ((LocalEntry)stringBuilder3).reg < k)) {
                printStream = System.err;
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("local address mismatch at orig ");
                stringBuilder3.append(j);
                stringBuilder3.append(" / decoded ");
                stringBuilder3.append(paramInt1);
                printStream.println(stringBuilder3.toString());
              } else {
                paramInt1++;
                j++;
              } 
              paramInt2 = 1;
              break;
            } 
          } else {
            break;
          } 
          j++;
        } 
        if (paramInt2 != 0) {
          System.err.println("decoded locals:");
          for (LocalEntry localEntry : list) {
            PrintStream printStream = System.err;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("  ");
            stringBuilder2.append(localEntry);
            printStream.println(stringBuilder2.toString());
          } 
          throw new RuntimeException("local table problem");
        } 
        return;
      } 
    } 
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("Decoded positions table not same size was ");
    stringBuilder1.append(list.size());
    stringBuilder1.append(" expected ");
    stringBuilder1.append(stringBuilder2.size());
    throw new RuntimeException(stringBuilder1.toString());
  }
  
  public void decode() {
    try {
      decode0();
      return;
    } catch (Exception exception) {
      throw ExceptionWithContext.withContext(exception, "...while decoding debug info");
    } 
  }
  
  public List<LocalEntry> getLocals() {
    return this.locals;
  }
  
  public List<PositionEntry> getPositionList() {
    return this.positions;
  }
  
  private static class LocalEntry {
    public int address;
    
    public boolean isStart;
    
    public int nameIndex;
    
    public int reg;
    
    public int signatureIndex;
    
    public int typeIndex;
    
    public LocalEntry(int param1Int1, boolean param1Boolean, int param1Int2, int param1Int3, int param1Int4, int param1Int5) {
      this.address = param1Int1;
      this.isStart = param1Boolean;
      this.reg = param1Int2;
      this.nameIndex = param1Int3;
      this.typeIndex = param1Int4;
      this.signatureIndex = param1Int5;
    }
    
    public String toString() {
      String str;
      int i = this.address;
      if (this.isStart) {
        str = "start";
      } else {
        str = "end";
      } 
      return String.format("[%x %s v%d %04x %04x %04x]", new Object[] { Integer.valueOf(i), str, Integer.valueOf(this.reg), Integer.valueOf(this.nameIndex), Integer.valueOf(this.typeIndex), Integer.valueOf(this.signatureIndex) });
    }
  }
  
  private static class PositionEntry {
    public int address;
    
    public int line;
    
    public PositionEntry(int param1Int1, int param1Int2) {
      this.address = param1Int1;
      this.line = param1Int2;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\DebugInfoDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */