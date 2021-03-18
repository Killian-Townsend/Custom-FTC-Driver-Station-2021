package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.LocalList;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.PositionList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Prototype;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArrayAnnotatedOutput;

public final class DebugInfoEncoder {
  private static final boolean DEBUG = false;
  
  private int address = 0;
  
  private AnnotatedOutput annotateTo;
  
  private final int codeSize;
  
  private PrintWriter debugPrint;
  
  private final Prototype desc;
  
  private final DexFile file;
  
  private final boolean isStatic;
  
  private final LocalList.Entry[] lastEntryForReg;
  
  private int line = 1;
  
  private final LocalList locals;
  
  private final ByteArrayAnnotatedOutput output;
  
  private final PositionList positions;
  
  private String prefix;
  
  private final int regSize;
  
  private boolean shouldConsume;
  
  public DebugInfoEncoder(PositionList paramPositionList, LocalList paramLocalList, DexFile paramDexFile, int paramInt1, int paramInt2, boolean paramBoolean, CstMethodRef paramCstMethodRef) {
    this.positions = paramPositionList;
    this.locals = paramLocalList;
    this.file = paramDexFile;
    this.desc = paramCstMethodRef.getPrototype();
    this.isStatic = paramBoolean;
    this.codeSize = paramInt1;
    this.regSize = paramInt2;
    this.output = new ByteArrayAnnotatedOutput();
    this.lastEntryForReg = new LocalList.Entry[paramInt2];
  }
  
  private void annotate(int paramInt, String paramString) {
    String str = paramString;
    if (this.prefix != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.prefix);
      stringBuilder.append(paramString);
      str = stringBuilder.toString();
    } 
    AnnotatedOutput annotatedOutput = this.annotateTo;
    if (annotatedOutput != null) {
      if (!this.shouldConsume)
        paramInt = 0; 
      annotatedOutput.annotate(paramInt, str);
    } 
    PrintWriter printWriter = this.debugPrint;
    if (printWriter != null)
      printWriter.println(str); 
  }
  
  private ArrayList<PositionList.Entry> buildSortedPositions() {
    int i;
    PositionList positionList = this.positions;
    int j = 0;
    if (positionList == null) {
      i = 0;
    } else {
      i = positionList.size();
    } 
    ArrayList<PositionList.Entry> arrayList = new ArrayList(i);
    while (j < i) {
      arrayList.add(this.positions.get(j));
      j++;
    } 
    Collections.sort(arrayList, new Comparator<PositionList.Entry>() {
          public int compare(PositionList.Entry param1Entry1, PositionList.Entry param1Entry2) {
            return param1Entry1.getAddress() - param1Entry2.getAddress();
          }
          
          public boolean equals(Object param1Object) {
            return (param1Object == this);
          }
        });
    return arrayList;
  }
  
  private static int computeOpcode(int paramInt1, int paramInt2) {
    if (paramInt1 >= -4 && paramInt1 <= 10)
      return paramInt1 + 4 + paramInt2 * 15 + 10; 
    throw new RuntimeException("Parameter out of range");
  }
  
  private byte[] convert0() throws IOException {
    ArrayList<PositionList.Entry> arrayList = buildSortedPositions();
    emitHeader(arrayList, extractMethodArguments());
    this.output.writeByte(7);
    AnnotatedOutput annotatedOutput = this.annotateTo;
    int j = 0;
    if (annotatedOutput != null || this.debugPrint != null)
      annotate(1, String.format("%04x: prologue end", new Object[] { Integer.valueOf(this.address) })); 
    int k = arrayList.size();
    int m = this.locals.size();
    for (int i = 0;; i = i1) {
      int n = emitLocalsAtAddress(j);
      int i1 = emitPositionsAtAddress(i, arrayList);
      if (n < m) {
        i = this.locals.get(n).getAddress();
      } else {
        i = Integer.MAX_VALUE;
      } 
      if (i1 < k) {
        j = ((PositionList.Entry)arrayList.get(i1)).getAddress();
      } else {
        j = Integer.MAX_VALUE;
      } 
      int i2 = Math.min(j, i);
      if (i2 == Integer.MAX_VALUE || (i2 == this.codeSize && i == Integer.MAX_VALUE && j == Integer.MAX_VALUE)) {
        emitEndSequence();
        return this.output.toByteArray();
      } 
      if (i2 == j) {
        emitPosition(arrayList.get(i1));
        i = i1 + 1;
        j = n;
        continue;
      } 
      emitAdvancePc(i2 - this.address);
      j = n;
    } 
  }
  
  private void emitAdvanceLine(int paramInt) throws IOException {
    int i = this.output.getCursor();
    this.output.writeByte(2);
    this.output.writeSleb128(paramInt);
    this.line += paramInt;
    if (this.annotateTo != null || this.debugPrint != null)
      annotate(this.output.getCursor() - i, String.format("line = %d", new Object[] { Integer.valueOf(this.line) })); 
  }
  
  private void emitAdvancePc(int paramInt) throws IOException {
    int i = this.output.getCursor();
    this.output.writeByte(1);
    this.output.writeUleb128(paramInt);
    this.address += paramInt;
    if (this.annotateTo != null || this.debugPrint != null)
      annotate(this.output.getCursor() - i, String.format("%04x: advance pc", new Object[] { Integer.valueOf(this.address) })); 
  }
  
  private void emitEndSequence() {
    this.output.writeByte(0);
    if (this.annotateTo != null || this.debugPrint != null)
      annotate(1, "end sequence"); 
  }
  
  private void emitHeader(ArrayList<PositionList.Entry> paramArrayList, ArrayList<LocalList.Entry> paramArrayList1) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield annotateTo : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/AnnotatedOutput;
    //   4: astore #10
    //   6: iconst_0
    //   7: istore #6
    //   9: aload #10
    //   11: ifnonnull -> 30
    //   14: aload_0
    //   15: getfield debugPrint : Ljava/io/PrintWriter;
    //   18: ifnull -> 24
    //   21: goto -> 30
    //   24: iconst_0
    //   25: istore #4
    //   27: goto -> 33
    //   30: iconst_1
    //   31: istore #4
    //   33: aload_0
    //   34: getfield output : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArrayAnnotatedOutput;
    //   37: invokevirtual getCursor : ()I
    //   40: istore_3
    //   41: aload_1
    //   42: invokevirtual size : ()I
    //   45: ifle -> 66
    //   48: aload_0
    //   49: aload_1
    //   50: iconst_0
    //   51: invokevirtual get : (I)Ljava/lang/Object;
    //   54: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/dex/code/PositionList$Entry
    //   57: invokevirtual getPosition : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;
    //   60: invokevirtual getLine : ()I
    //   63: putfield line : I
    //   66: aload_0
    //   67: getfield output : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArrayAnnotatedOutput;
    //   70: aload_0
    //   71: getfield line : I
    //   74: invokevirtual writeUleb128 : (I)I
    //   77: pop
    //   78: iload #4
    //   80: ifeq -> 128
    //   83: aload_0
    //   84: getfield output : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArrayAnnotatedOutput;
    //   87: invokevirtual getCursor : ()I
    //   90: istore #5
    //   92: new java/lang/StringBuilder
    //   95: dup
    //   96: invokespecial <init> : ()V
    //   99: astore_1
    //   100: aload_1
    //   101: ldc 'line_start: '
    //   103: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: pop
    //   107: aload_1
    //   108: aload_0
    //   109: getfield line : I
    //   112: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   115: pop
    //   116: aload_0
    //   117: iload #5
    //   119: iload_3
    //   120: isub
    //   121: aload_1
    //   122: invokevirtual toString : ()Ljava/lang/String;
    //   125: invokespecial annotate : (ILjava/lang/String;)V
    //   128: aload_0
    //   129: invokespecial getParamBase : ()I
    //   132: istore #5
    //   134: aload_0
    //   135: getfield desc : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Prototype;
    //   138: invokevirtual getParameterTypes : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/StdTypeList;
    //   141: astore #10
    //   143: aload #10
    //   145: invokevirtual size : ()I
    //   148: istore #8
    //   150: iload #5
    //   152: istore_3
    //   153: aload_0
    //   154: getfield isStatic : Z
    //   157: ifne -> 209
    //   160: aload_2
    //   161: invokevirtual iterator : ()Ljava/util/Iterator;
    //   164: astore_1
    //   165: aload_1
    //   166: invokeinterface hasNext : ()Z
    //   171: ifeq -> 204
    //   174: aload_1
    //   175: invokeinterface next : ()Ljava/lang/Object;
    //   180: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/dex/code/LocalList$Entry
    //   183: astore #11
    //   185: iload #5
    //   187: aload #11
    //   189: invokevirtual getRegister : ()I
    //   192: if_icmpne -> 165
    //   195: aload_0
    //   196: getfield lastEntryForReg : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/code/LocalList$Entry;
    //   199: iload #5
    //   201: aload #11
    //   203: aastore
    //   204: iload #5
    //   206: iconst_1
    //   207: iadd
    //   208: istore_3
    //   209: aload_0
    //   210: getfield output : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArrayAnnotatedOutput;
    //   213: invokevirtual getCursor : ()I
    //   216: istore #5
    //   218: aload_0
    //   219: getfield output : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArrayAnnotatedOutput;
    //   222: iload #8
    //   224: invokevirtual writeUleb128 : (I)I
    //   227: pop
    //   228: iload #4
    //   230: ifeq -> 265
    //   233: aload_0
    //   234: aload_0
    //   235: getfield output : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArrayAnnotatedOutput;
    //   238: invokevirtual getCursor : ()I
    //   241: iload #5
    //   243: isub
    //   244: ldc_w 'parameters_size: %04x'
    //   247: iconst_1
    //   248: anewarray java/lang/Object
    //   251: dup
    //   252: iconst_0
    //   253: iload #8
    //   255: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   258: aastore
    //   259: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   262: invokespecial annotate : (ILjava/lang/String;)V
    //   265: iconst_0
    //   266: istore #7
    //   268: iload_3
    //   269: istore #5
    //   271: iload #7
    //   273: istore_3
    //   274: iload_3
    //   275: iload #8
    //   277: if_icmpge -> 503
    //   280: aload #10
    //   282: iload_3
    //   283: invokevirtual get : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   286: astore #11
    //   288: aload_0
    //   289: getfield output : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArrayAnnotatedOutput;
    //   292: invokevirtual getCursor : ()I
    //   295: istore #7
    //   297: aload_2
    //   298: invokevirtual iterator : ()Ljava/util/Iterator;
    //   301: astore #12
    //   303: aload #12
    //   305: invokeinterface hasNext : ()Z
    //   310: ifeq -> 367
    //   313: aload #12
    //   315: invokeinterface next : ()Ljava/lang/Object;
    //   320: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/dex/code/LocalList$Entry
    //   323: astore_1
    //   324: iload #5
    //   326: aload_1
    //   327: invokevirtual getRegister : ()I
    //   330: if_icmpne -> 303
    //   333: aload_1
    //   334: invokevirtual getSignature : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstString;
    //   337: ifnull -> 348
    //   340: aload_0
    //   341: aconst_null
    //   342: invokespecial emitStringIndex : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstString;)V
    //   345: goto -> 356
    //   348: aload_0
    //   349: aload_1
    //   350: invokevirtual getName : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstString;
    //   353: invokespecial emitStringIndex : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstString;)V
    //   356: aload_0
    //   357: getfield lastEntryForReg : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/code/LocalList$Entry;
    //   360: iload #5
    //   362: aload_1
    //   363: aastore
    //   364: goto -> 369
    //   367: aconst_null
    //   368: astore_1
    //   369: aload_1
    //   370: ifnonnull -> 378
    //   373: aload_0
    //   374: aconst_null
    //   375: invokespecial emitStringIndex : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstString;)V
    //   378: iload #4
    //   380: ifeq -> 486
    //   383: aload_1
    //   384: ifnull -> 408
    //   387: aload_1
    //   388: invokevirtual getSignature : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstString;
    //   391: ifnull -> 397
    //   394: goto -> 408
    //   397: aload_1
    //   398: invokevirtual getName : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstString;
    //   401: invokevirtual toHuman : ()Ljava/lang/String;
    //   404: astore_1
    //   405: goto -> 412
    //   408: ldc_w '<unnamed>'
    //   411: astore_1
    //   412: aload_0
    //   413: getfield output : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArrayAnnotatedOutput;
    //   416: invokevirtual getCursor : ()I
    //   419: istore #9
    //   421: new java/lang/StringBuilder
    //   424: dup
    //   425: invokespecial <init> : ()V
    //   428: astore #12
    //   430: aload #12
    //   432: ldc_w 'parameter '
    //   435: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   438: pop
    //   439: aload #12
    //   441: aload_1
    //   442: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   445: pop
    //   446: aload #12
    //   448: ldc_w ' '
    //   451: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   454: pop
    //   455: aload #12
    //   457: ldc_w 'v'
    //   460: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   463: pop
    //   464: aload #12
    //   466: iload #5
    //   468: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   471: pop
    //   472: aload_0
    //   473: iload #9
    //   475: iload #7
    //   477: isub
    //   478: aload #12
    //   480: invokevirtual toString : ()Ljava/lang/String;
    //   483: invokespecial annotate : (ILjava/lang/String;)V
    //   486: iload #5
    //   488: aload #11
    //   490: invokevirtual getCategory : ()I
    //   493: iadd
    //   494: istore #5
    //   496: iload_3
    //   497: iconst_1
    //   498: iadd
    //   499: istore_3
    //   500: goto -> 274
    //   503: aload_0
    //   504: getfield lastEntryForReg : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/code/LocalList$Entry;
    //   507: astore_1
    //   508: aload_1
    //   509: arraylength
    //   510: istore #4
    //   512: iload #6
    //   514: istore_3
    //   515: iload_3
    //   516: iload #4
    //   518: if_icmpge -> 551
    //   521: aload_1
    //   522: iload_3
    //   523: aaload
    //   524: astore_2
    //   525: aload_2
    //   526: ifnonnull -> 532
    //   529: goto -> 544
    //   532: aload_2
    //   533: invokevirtual getSignature : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstString;
    //   536: ifnull -> 544
    //   539: aload_0
    //   540: aload_2
    //   541: invokespecial emitLocalStartExtended : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/code/LocalList$Entry;)V
    //   544: iload_3
    //   545: iconst_1
    //   546: iadd
    //   547: istore_3
    //   548: goto -> 515
    //   551: return
  }
  
  private void emitLocalEnd(LocalList.Entry paramEntry) throws IOException {
    int i = this.output.getCursor();
    this.output.writeByte(5);
    this.output.writeUleb128(paramEntry.getRegister());
    if (this.annotateTo != null || this.debugPrint != null)
      annotate(this.output.getCursor() - i, String.format("%04x: -local %s", new Object[] { Integer.valueOf(this.address), entryAnnotationString(paramEntry) })); 
  }
  
  private void emitLocalRestart(LocalList.Entry paramEntry) throws IOException {
    int i = this.output.getCursor();
    this.output.writeByte(6);
    emitUnsignedLeb128(paramEntry.getRegister());
    if (this.annotateTo != null || this.debugPrint != null)
      annotate(this.output.getCursor() - i, String.format("%04x: +local restart %s", new Object[] { Integer.valueOf(this.address), entryAnnotationString(paramEntry) })); 
  }
  
  private void emitLocalStart(LocalList.Entry paramEntry) throws IOException {
    if (paramEntry.getSignature() != null) {
      emitLocalStartExtended(paramEntry);
      return;
    } 
    int i = this.output.getCursor();
    this.output.writeByte(3);
    emitUnsignedLeb128(paramEntry.getRegister());
    emitStringIndex(paramEntry.getName());
    emitTypeIndex(paramEntry.getType());
    if (this.annotateTo != null || this.debugPrint != null)
      annotate(this.output.getCursor() - i, String.format("%04x: +local %s", new Object[] { Integer.valueOf(this.address), entryAnnotationString(paramEntry) })); 
  }
  
  private void emitLocalStartExtended(LocalList.Entry paramEntry) throws IOException {
    int i = this.output.getCursor();
    this.output.writeByte(4);
    emitUnsignedLeb128(paramEntry.getRegister());
    emitStringIndex(paramEntry.getName());
    emitTypeIndex(paramEntry.getType());
    emitStringIndex(paramEntry.getSignature());
    if (this.annotateTo != null || this.debugPrint != null)
      annotate(this.output.getCursor() - i, String.format("%04x: +localx %s", new Object[] { Integer.valueOf(this.address), entryAnnotationString(paramEntry) })); 
  }
  
  private int emitLocalsAtAddress(int paramInt) throws IOException {
    int i = this.locals.size();
    while (paramInt < i && this.locals.get(paramInt).getAddress() == this.address) {
      LocalList.Entry entry1 = this.locals.get(paramInt);
      int j = entry1.getRegister();
      LocalList.Entry[] arrayOfEntry = this.lastEntryForReg;
      LocalList.Entry entry2 = arrayOfEntry[j];
      if (entry1 != entry2) {
        arrayOfEntry[j] = entry1;
        if (entry1.isStart()) {
          if (entry2 != null && entry1.matches(entry2)) {
            if (!entry2.isStart()) {
              emitLocalRestart(entry1);
            } else {
              throw new RuntimeException("shouldn't happen");
            } 
          } else {
            emitLocalStart(entry1);
          } 
        } else if (entry1.getDisposition() != LocalList.Disposition.END_REPLACED) {
          emitLocalEnd(entry1);
        } 
      } 
      paramInt++;
    } 
    return paramInt;
  }
  
  private void emitPosition(PositionList.Entry paramEntry) throws IOException {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getPosition : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;
    //   4: invokevirtual getLine : ()I
    //   7: istore_3
    //   8: aload_1
    //   9: invokevirtual getAddress : ()I
    //   12: istore_2
    //   13: iload_3
    //   14: aload_0
    //   15: getfield line : I
    //   18: isub
    //   19: istore_3
    //   20: iload_2
    //   21: aload_0
    //   22: getfield address : I
    //   25: isub
    //   26: istore #6
    //   28: iload #6
    //   30: iflt -> 208
    //   33: iload_3
    //   34: bipush #-4
    //   36: if_icmplt -> 47
    //   39: iload_3
    //   40: istore_2
    //   41: iload_3
    //   42: bipush #10
    //   44: if_icmple -> 54
    //   47: aload_0
    //   48: iload_3
    //   49: invokespecial emitAdvanceLine : (I)V
    //   52: iconst_0
    //   53: istore_2
    //   54: iload_2
    //   55: iload #6
    //   57: invokestatic computeOpcode : (II)I
    //   60: istore #7
    //   62: iload_2
    //   63: istore #5
    //   65: iload #7
    //   67: istore_3
    //   68: iload #6
    //   70: istore #4
    //   72: iload #7
    //   74: sipush #-256
    //   77: iand
    //   78: ifle -> 128
    //   81: aload_0
    //   82: iload #6
    //   84: invokespecial emitAdvancePc : (I)V
    //   87: iload_2
    //   88: iconst_0
    //   89: invokestatic computeOpcode : (II)I
    //   92: istore_3
    //   93: iload_3
    //   94: sipush #-256
    //   97: iand
    //   98: ifle -> 122
    //   101: aload_0
    //   102: iload_2
    //   103: invokespecial emitAdvanceLine : (I)V
    //   106: iconst_0
    //   107: iconst_0
    //   108: invokestatic computeOpcode : (II)I
    //   111: istore_3
    //   112: iconst_0
    //   113: istore #4
    //   115: iload #4
    //   117: istore #5
    //   119: goto -> 128
    //   122: iconst_0
    //   123: istore #4
    //   125: iload_2
    //   126: istore #5
    //   128: aload_0
    //   129: getfield output : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArrayAnnotatedOutput;
    //   132: iload_3
    //   133: invokevirtual writeByte : (I)V
    //   136: aload_0
    //   137: aload_0
    //   138: getfield line : I
    //   141: iload #5
    //   143: iadd
    //   144: putfield line : I
    //   147: aload_0
    //   148: aload_0
    //   149: getfield address : I
    //   152: iload #4
    //   154: iadd
    //   155: putfield address : I
    //   158: aload_0
    //   159: getfield annotateTo : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/AnnotatedOutput;
    //   162: ifnonnull -> 172
    //   165: aload_0
    //   166: getfield debugPrint : Ljava/io/PrintWriter;
    //   169: ifnull -> 207
    //   172: aload_0
    //   173: iconst_1
    //   174: ldc_w '%04x: line %d'
    //   177: iconst_2
    //   178: anewarray java/lang/Object
    //   181: dup
    //   182: iconst_0
    //   183: aload_0
    //   184: getfield address : I
    //   187: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   190: aastore
    //   191: dup
    //   192: iconst_1
    //   193: aload_0
    //   194: getfield line : I
    //   197: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   200: aastore
    //   201: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   204: invokespecial annotate : (ILjava/lang/String;)V
    //   207: return
    //   208: new java/lang/RuntimeException
    //   211: dup
    //   212: ldc_w 'Position entries must be in ascending address order'
    //   215: invokespecial <init> : (Ljava/lang/String;)V
    //   218: athrow
  }
  
  private int emitPositionsAtAddress(int paramInt, ArrayList<PositionList.Entry> paramArrayList) throws IOException {
    int i = paramArrayList.size();
    while (paramInt < i && ((PositionList.Entry)paramArrayList.get(paramInt)).getAddress() == this.address) {
      emitPosition(paramArrayList.get(paramInt));
      paramInt++;
    } 
    return paramInt;
  }
  
  private void emitStringIndex(CstString paramCstString) throws IOException {
    if (paramCstString != null) {
      DexFile dexFile = this.file;
      if (dexFile != null) {
        this.output.writeUleb128(dexFile.getStringIds().indexOf(paramCstString) + 1);
        return;
      } 
    } 
    this.output.writeUleb128(0);
  }
  
  private void emitTypeIndex(CstType paramCstType) throws IOException {
    if (paramCstType != null) {
      DexFile dexFile = this.file;
      if (dexFile != null) {
        this.output.writeUleb128(dexFile.getTypeIds().indexOf(paramCstType) + 1);
        return;
      } 
    } 
    this.output.writeUleb128(0);
  }
  
  private void emitUnsignedLeb128(int paramInt) throws IOException {
    if (paramInt >= 0) {
      this.output.writeUleb128(paramInt);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Signed value where unsigned required: ");
    stringBuilder.append(paramInt);
    throw new RuntimeException(stringBuilder.toString());
  }
  
  private String entryAnnotationString(LocalList.Entry paramEntry) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("v");
    stringBuilder.append(paramEntry.getRegister());
    stringBuilder.append(' ');
    CstString cstString2 = paramEntry.getName();
    if (cstString2 == null) {
      stringBuilder.append("null");
    } else {
      stringBuilder.append(cstString2.toHuman());
    } 
    stringBuilder.append(' ');
    CstType cstType = paramEntry.getType();
    if (cstType == null) {
      stringBuilder.append("null");
    } else {
      stringBuilder.append(cstType.toHuman());
    } 
    CstString cstString1 = paramEntry.getSignature();
    if (cstString1 != null) {
      stringBuilder.append(' ');
      stringBuilder.append(cstString1.toHuman());
    } 
    return stringBuilder.toString();
  }
  
  private ArrayList<LocalList.Entry> extractMethodArguments() {
    ArrayList<LocalList.Entry> arrayList = new ArrayList(this.desc.getParameterTypes().size());
    int j = getParamBase();
    BitSet bitSet = new BitSet(this.regSize - j);
    int k = this.locals.size();
    for (int i = 0; i < k; i++) {
      LocalList.Entry entry = this.locals.get(i);
      int m = entry.getRegister();
      if (m >= j) {
        m -= j;
        if (!bitSet.get(m)) {
          bitSet.set(m);
          arrayList.add(entry);
        } 
      } 
    } 
    Collections.sort(arrayList, new Comparator<LocalList.Entry>() {
          public int compare(LocalList.Entry param1Entry1, LocalList.Entry param1Entry2) {
            return param1Entry1.getRegister() - param1Entry2.getRegister();
          }
          
          public boolean equals(Object param1Object) {
            return (param1Object == this);
          }
        });
    return arrayList;
  }
  
  private int getParamBase() {
    return this.regSize - this.desc.getParameterTypes().getWordCount() - (this.isStatic ^ true);
  }
  
  public byte[] convert() {
    try {
      return convert0();
    } catch (IOException iOException) {
      throw ExceptionWithContext.withContext(iOException, "...while encoding debug info");
    } 
  }
  
  public byte[] convertAndAnnotate(String paramString, PrintWriter paramPrintWriter, AnnotatedOutput paramAnnotatedOutput, boolean paramBoolean) {
    this.prefix = paramString;
    this.debugPrint = paramPrintWriter;
    this.annotateTo = paramAnnotatedOutput;
    this.shouldConsume = paramBoolean;
    return convert();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\DebugInfoEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */