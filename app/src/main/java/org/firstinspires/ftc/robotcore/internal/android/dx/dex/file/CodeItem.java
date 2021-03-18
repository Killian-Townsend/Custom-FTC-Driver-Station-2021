package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.io.PrintWriter;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvCode;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsnList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class CodeItem extends OffsettedItem {
  private static final int ALIGNMENT = 4;
  
  private static final int HEADER_SIZE = 16;
  
  private CatchStructs catches;
  
  private final DalvCode code;
  
  private DebugInfoItem debugInfo;
  
  private final boolean isStatic;
  
  private final CstMethodRef ref;
  
  private final TypeList throwsList;
  
  public CodeItem(CstMethodRef paramCstMethodRef, DalvCode paramDalvCode, boolean paramBoolean, TypeList paramTypeList) {
    super(4, -1);
    if (paramCstMethodRef != null) {
      if (paramDalvCode != null) {
        if (paramTypeList != null) {
          this.ref = paramCstMethodRef;
          this.code = paramDalvCode;
          this.isStatic = paramBoolean;
          this.throwsList = paramTypeList;
          this.catches = null;
          this.debugInfo = null;
          return;
        } 
        throw new NullPointerException("throwsList == null");
      } 
      throw new NullPointerException("code == null");
    } 
    throw new NullPointerException("ref == null");
  }
  
  private int getInsSize() {
    return this.ref.getParameterWordCount(this.isStatic);
  }
  
  private int getOutsSize() {
    return this.code.getInsns().getOutsSize();
  }
  
  private int getRegistersSize() {
    return this.code.getInsns().getRegistersSize();
  }
  
  private void writeCodes(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    DalvInsnList dalvInsnList = this.code.getInsns();
    try {
      dalvInsnList.writeTo(paramAnnotatedOutput);
      return;
    } catch (RuntimeException runtimeException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("...while writing instructions for ");
      stringBuilder.append(this.ref.toHuman());
      throw ExceptionWithContext.withContext(runtimeException, stringBuilder.toString());
    } 
  }
  
  public void addContents(DexFile paramDexFile) {
    MixedItemSection mixedItemSection = paramDexFile.getByteData();
    TypeIdsSection typeIdsSection = paramDexFile.getTypeIds();
    if (this.code.hasPositions() || this.code.hasLocals()) {
      DebugInfoItem debugInfoItem = new DebugInfoItem(this.code, this.isStatic, this.ref);
      this.debugInfo = debugInfoItem;
      mixedItemSection.add(debugInfoItem);
    } 
    if (this.code.hasAnyCatches()) {
      Iterator<Type> iterator1 = this.code.getCatchTypes().iterator();
      while (iterator1.hasNext())
        typeIdsSection.intern(iterator1.next()); 
      this.catches = new CatchStructs(this.code);
    } 
    Iterator<Constant> iterator = this.code.getInsnConstants().iterator();
    while (iterator.hasNext())
      paramDexFile.internIfAppropriate(iterator.next()); 
  }
  
  public void debugPrint(PrintWriter paramPrintWriter, String paramString, boolean paramBoolean) {
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(this.ref.toHuman());
    stringBuilder2.append(":");
    paramPrintWriter.println(stringBuilder2.toString());
    DalvInsnList dalvInsnList = this.code.getInsns();
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append("regs: ");
    stringBuilder3.append(Hex.u2(getRegistersSize()));
    stringBuilder3.append("; ins: ");
    stringBuilder3.append(Hex.u2(getInsSize()));
    stringBuilder3.append("; outs: ");
    stringBuilder3.append(Hex.u2(getOutsSize()));
    paramPrintWriter.println(stringBuilder3.toString());
    dalvInsnList.debugPrint(paramPrintWriter, paramString, paramBoolean);
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(paramString);
    stringBuilder1.append("  ");
    String str = stringBuilder1.toString();
    if (this.catches != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("catches");
      this.catches.debugPrint(paramPrintWriter, str);
    } 
    if (this.debugInfo != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("debug info");
      this.debugInfo.debugPrint(paramPrintWriter, str);
    } 
  }
  
  public CstMethodRef getRef() {
    return this.ref;
  }
  
  public ItemType itemType() {
    return ItemType.TYPE_CODE_ITEM;
  }
  
  protected void place0(Section paramSection, int paramInt) {
    final DexFile file = paramSection.getFile();
    this.code.assignIndices(new DalvCode.AssignIndicesCallback() {
          public int getIndex(Constant param1Constant) {
            IndexedItem indexedItem = file.findItemOrNull(param1Constant);
            return (indexedItem == null) ? -1 : indexedItem.getIndex();
          }
        });
    CatchStructs catchStructs = this.catches;
    if (catchStructs != null) {
      catchStructs.encode(dexFile);
      paramInt = this.catches.writeSize();
    } else {
      paramInt = 0;
    } 
    int j = this.code.getInsns().codeSize();
    int i = j;
    if ((j & 0x1) != 0)
      i = j + 1; 
    setWriteSize(i * 2 + 16 + paramInt);
  }
  
  public String toHuman() {
    return this.ref.toHuman();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("CodeItem{");
    stringBuilder.append(toHuman());
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  protected void writeTo0(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    boolean bool;
    int i;
    int j;
    boolean bool1 = paramAnnotatedOutput.annotates();
    int k = getRegistersSize();
    int m = getOutsSize();
    int n = getInsSize();
    int i1 = this.code.getInsns().codeSize();
    if ((i1 & 0x1) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    CatchStructs catchStructs = this.catches;
    if (catchStructs == null) {
      i = 0;
    } else {
      i = catchStructs.triesSize();
    } 
    DebugInfoItem debugInfoItem = this.debugInfo;
    if (debugInfoItem == null) {
      j = 0;
    } else {
      j = debugInfoItem.getAbsoluteOffset();
    } 
    if (bool1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(offsetString());
      stringBuilder.append(' ');
      stringBuilder.append(this.ref.toHuman());
      paramAnnotatedOutput.annotate(0, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("  registers_size: ");
      stringBuilder.append(Hex.u2(k));
      paramAnnotatedOutput.annotate(2, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("  ins_size:       ");
      stringBuilder.append(Hex.u2(n));
      paramAnnotatedOutput.annotate(2, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("  outs_size:      ");
      stringBuilder.append(Hex.u2(m));
      paramAnnotatedOutput.annotate(2, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("  tries_size:     ");
      stringBuilder.append(Hex.u2(i));
      paramAnnotatedOutput.annotate(2, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("  debug_off:      ");
      stringBuilder.append(Hex.u4(j));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("  insns_size:     ");
      stringBuilder.append(Hex.u4(i1));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
      if (this.throwsList.size() != 0) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("  throws ");
        stringBuilder.append(StdTypeList.toHuman(this.throwsList));
        paramAnnotatedOutput.annotate(0, stringBuilder.toString());
      } 
    } 
    paramAnnotatedOutput.writeShort(k);
    paramAnnotatedOutput.writeShort(n);
    paramAnnotatedOutput.writeShort(m);
    paramAnnotatedOutput.writeShort(i);
    paramAnnotatedOutput.writeInt(j);
    paramAnnotatedOutput.writeInt(i1);
    writeCodes(paramDexFile, paramAnnotatedOutput);
    if (this.catches != null) {
      if (bool) {
        if (bool1)
          paramAnnotatedOutput.annotate(2, "  padding: 0"); 
        paramAnnotatedOutput.writeShort(0);
      } 
      this.catches.writeTo(paramDexFile, paramAnnotatedOutput);
    } 
    if (bool1 && this.debugInfo != null) {
      paramAnnotatedOutput.annotate(0, "  debug info");
      this.debugInfo.annotateTo(paramDexFile, paramAnnotatedOutput, "    ");
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\CodeItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */