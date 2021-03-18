package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.io.PrintWriter;
import org.firstinspires.ftc.robotcore.internal.android.dex.Leb128;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvCode;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstBaseMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class EncodedMethod extends EncodedMember implements Comparable<EncodedMethod> {
  private final CodeItem code;
  
  private final CstMethodRef method;
  
  public EncodedMethod(CstMethodRef paramCstMethodRef, int paramInt, DalvCode paramDalvCode, TypeList paramTypeList) {
    super(paramInt);
    if (paramCstMethodRef != null) {
      boolean bool;
      this.method = paramCstMethodRef;
      if (paramDalvCode == null) {
        this.code = null;
        return;
      } 
      if ((paramInt & 0x8) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.code = new CodeItem(paramCstMethodRef, paramDalvCode, bool, paramTypeList);
      return;
    } 
    throw new NullPointerException("method == null");
  }
  
  public void addContents(DexFile paramDexFile) {
    MethodIdsSection methodIdsSection = paramDexFile.getMethodIds();
    MixedItemSection mixedItemSection = paramDexFile.getWordData();
    methodIdsSection.intern((CstBaseMethodRef)this.method);
    CodeItem codeItem = this.code;
    if (codeItem != null)
      mixedItemSection.add(codeItem); 
  }
  
  public int compareTo(EncodedMethod paramEncodedMethod) {
    return this.method.compareTo((Constant)paramEncodedMethod.method);
  }
  
  public void debugPrint(PrintWriter paramPrintWriter, boolean paramBoolean) {
    StringBuilder stringBuilder;
    CodeItem codeItem = this.code;
    if (codeItem == null) {
      stringBuilder = new StringBuilder();
      stringBuilder.append(getRef().toHuman());
      stringBuilder.append(": abstract or native");
      paramPrintWriter.println(stringBuilder.toString());
      return;
    } 
    stringBuilder.debugPrint(paramPrintWriter, "  ", paramBoolean);
  }
  
  public int encode(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput, int paramInt1, int paramInt2) {
    byte b;
    int i = paramDexFile.getMethodIds().indexOf((CstBaseMethodRef)this.method);
    int j = i - paramInt1;
    int k = getAccessFlags();
    int m = OffsettedItem.getAbsoluteOffsetOr0(this.code);
    if (m != 0) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if ((k & 0x500) == 0) {
      b = 1;
    } else {
      b = 0;
    } 
    if (paramInt1 == b) {
      if (paramAnnotatedOutput.annotates()) {
        paramAnnotatedOutput.annotate(0, String.format("  [%x] %s", new Object[] { Integer.valueOf(paramInt2), this.method.toHuman() }));
        paramInt1 = Leb128.unsignedLeb128Size(j);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("    method_idx:   ");
        stringBuilder.append(Hex.u4(i));
        paramAnnotatedOutput.annotate(paramInt1, stringBuilder.toString());
        paramInt1 = Leb128.unsignedLeb128Size(k);
        stringBuilder = new StringBuilder();
        stringBuilder.append("    access_flags: ");
        stringBuilder.append(AccessFlags.methodString(k));
        paramAnnotatedOutput.annotate(paramInt1, stringBuilder.toString());
        paramInt1 = Leb128.unsignedLeb128Size(m);
        stringBuilder = new StringBuilder();
        stringBuilder.append("    code_off:     ");
        stringBuilder.append(Hex.u4(m));
        paramAnnotatedOutput.annotate(paramInt1, stringBuilder.toString());
      } 
      paramAnnotatedOutput.writeUleb128(j);
      paramAnnotatedOutput.writeUleb128(k);
      paramAnnotatedOutput.writeUleb128(m);
      return i;
    } 
    throw new UnsupportedOperationException("code vs. access_flags mismatch");
  }
  
  public boolean equals(Object paramObject) {
    boolean bool1 = paramObject instanceof EncodedMethod;
    boolean bool = false;
    if (!bool1)
      return false; 
    if (compareTo((EncodedMethod)paramObject) == 0)
      bool = true; 
    return bool;
  }
  
  public final CstString getName() {
    return this.method.getNat().getName();
  }
  
  public final CstMethodRef getRef() {
    return this.method;
  }
  
  public final String toHuman() {
    return this.method.toHuman();
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append(getClass().getName());
    stringBuffer.append('{');
    stringBuffer.append(Hex.u2(getAccessFlags()));
    stringBuffer.append(' ');
    stringBuffer.append(this.method);
    if (this.code != null) {
      stringBuffer.append(' ');
      stringBuffer.append(this.code);
    } 
    stringBuffer.append('}');
    return stringBuffer.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\EncodedMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */