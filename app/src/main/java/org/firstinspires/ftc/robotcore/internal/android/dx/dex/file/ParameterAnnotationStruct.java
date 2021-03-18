package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.ArrayList;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.AnnotationsList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstBaseMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ToHuman;

public final class ParameterAnnotationStruct implements ToHuman, Comparable<ParameterAnnotationStruct> {
  private final UniformListItem<AnnotationSetRefItem> annotationsItem;
  
  private final AnnotationsList annotationsList;
  
  private final CstMethodRef method;
  
  public ParameterAnnotationStruct(CstMethodRef paramCstMethodRef, AnnotationsList paramAnnotationsList, DexFile paramDexFile) {
    if (paramCstMethodRef != null) {
      if (paramAnnotationsList != null) {
        this.method = paramCstMethodRef;
        this.annotationsList = paramAnnotationsList;
        int j = paramAnnotationsList.size();
        ArrayList<AnnotationSetRefItem> arrayList = new ArrayList(j);
        int i;
        for (i = 0; i < j; i++)
          arrayList.add(new AnnotationSetRefItem(new AnnotationSetItem(paramAnnotationsList.get(i), paramDexFile))); 
        this.annotationsItem = new UniformListItem<AnnotationSetRefItem>(ItemType.TYPE_ANNOTATION_SET_REF_LIST, arrayList);
        return;
      } 
      throw new NullPointerException("annotationsList == null");
    } 
    throw new NullPointerException("method == null");
  }
  
  public void addContents(DexFile paramDexFile) {
    MethodIdsSection methodIdsSection = paramDexFile.getMethodIds();
    MixedItemSection mixedItemSection = paramDexFile.getWordData();
    methodIdsSection.intern((CstBaseMethodRef)this.method);
    mixedItemSection.add(this.annotationsItem);
  }
  
  public int compareTo(ParameterAnnotationStruct paramParameterAnnotationStruct) {
    return this.method.compareTo((Constant)paramParameterAnnotationStruct.method);
  }
  
  public boolean equals(Object paramObject) {
    return !(paramObject instanceof ParameterAnnotationStruct) ? false : this.method.equals(((ParameterAnnotationStruct)paramObject).method);
  }
  
  public AnnotationsList getAnnotationsList() {
    return this.annotationsList;
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
    Iterator<AnnotationSetRefItem> iterator = this.annotationsItem.getItems().iterator();
    boolean bool = true;
    while (iterator.hasNext()) {
      AnnotationSetRefItem annotationSetRefItem = iterator.next();
      if (bool) {
        bool = false;
      } else {
        stringBuilder.append(", ");
      } 
      stringBuilder.append(annotationSetRefItem.toHuman());
    } 
    return stringBuilder.toString();
  }
  
  public void writeTo(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    int i = paramDexFile.getMethodIds().indexOf((CstBaseMethodRef)this.method);
    int j = this.annotationsItem.getAbsoluteOffset();
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


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\ParameterAnnotationStruct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */