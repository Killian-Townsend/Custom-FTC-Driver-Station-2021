package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.Annotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstBaseMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ToHuman;

public final class MethodAnnotationStruct implements ToHuman, Comparable<MethodAnnotationStruct> {
  private AnnotationSetItem annotations;
  
  private final CstMethodRef method;
  
  public MethodAnnotationStruct(CstMethodRef paramCstMethodRef, AnnotationSetItem paramAnnotationSetItem) {
    if (paramCstMethodRef != null) {
      if (paramAnnotationSetItem != null) {
        this.method = paramCstMethodRef;
        this.annotations = paramAnnotationSetItem;
        return;
      } 
      throw new NullPointerException("annotations == null");
    } 
    throw new NullPointerException("method == null");
  }
  
  public void addContents(DexFile paramDexFile) {
    MethodIdsSection methodIdsSection = paramDexFile.getMethodIds();
    MixedItemSection mixedItemSection = paramDexFile.getWordData();
    methodIdsSection.intern((CstBaseMethodRef)this.method);
    this.annotations = mixedItemSection.<AnnotationSetItem>intern(this.annotations);
  }
  
  public int compareTo(MethodAnnotationStruct paramMethodAnnotationStruct) {
    return this.method.compareTo((Constant)paramMethodAnnotationStruct.method);
  }
  
  public boolean equals(Object paramObject) {
    return !(paramObject instanceof MethodAnnotationStruct) ? false : this.method.equals(((MethodAnnotationStruct)paramObject).method);
  }
  
  public Annotations getAnnotations() {
    return this.annotations.getAnnotations();
  }
  
  public CstMethodRef getMethod() {
    return this.method;
  }
  
  public int hashCode() {
    return this.method.hashCode();
  }
  
  public String toHuman() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.method.toHuman());
    stringBuilder.append(": ");
    stringBuilder.append(this.annotations);
    return stringBuilder.toString();
  }
  
  public void writeTo(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    int i = paramDexFile.getMethodIds().indexOf((CstBaseMethodRef)this.method);
    int j = this.annotations.getAbsoluteOffset();
    if (paramAnnotatedOutput.annotates()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("    ");
      stringBuilder.append(this.method.toHuman());
      paramAnnotatedOutput.annotate(0, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("      method_idx:      ");
      stringBuilder.append(Hex.u4(i));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("      annotations_off: ");
      stringBuilder.append(Hex.u4(j));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
    } 
    paramAnnotatedOutput.writeInt(i);
    paramAnnotatedOutput.writeInt(j);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\MethodAnnotationStruct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */