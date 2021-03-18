package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.Annotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.AnnotationsList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFieldRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Writers;

public final class ClassDefItem extends IndexedItem {
  private final int accessFlags;
  
  private AnnotationsDirectoryItem annotationsDirectory;
  
  private final ClassDataItem classData;
  
  private TypeListItem interfaces;
  
  private final CstString sourceFile;
  
  private EncodedArrayItem staticValuesItem;
  
  private final CstType superclass;
  
  private final CstType thisClass;
  
  public ClassDefItem(CstType paramCstType1, int paramInt, CstType paramCstType2, TypeList paramTypeList, CstString paramCstString) {
    if (paramCstType1 != null) {
      if (paramTypeList != null) {
        TypeListItem typeListItem;
        this.thisClass = paramCstType1;
        this.accessFlags = paramInt;
        this.superclass = paramCstType2;
        if (paramTypeList.size() == 0) {
          paramCstType2 = null;
        } else {
          typeListItem = new TypeListItem(paramTypeList);
        } 
        this.interfaces = typeListItem;
        this.sourceFile = paramCstString;
        this.classData = new ClassDataItem(paramCstType1);
        this.staticValuesItem = null;
        this.annotationsDirectory = new AnnotationsDirectoryItem();
        return;
      } 
      throw new NullPointerException("interfaces == null");
    } 
    throw new NullPointerException("thisClass == null");
  }
  
  public void addContents(DexFile paramDexFile) {
    TypeIdsSection typeIdsSection = paramDexFile.getTypeIds();
    MixedItemSection mixedItemSection3 = paramDexFile.getByteData();
    MixedItemSection mixedItemSection1 = paramDexFile.getWordData();
    MixedItemSection mixedItemSection2 = paramDexFile.getTypeLists();
    StringIdsSection stringIdsSection = paramDexFile.getStringIds();
    typeIdsSection.intern(this.thisClass);
    if (!this.classData.isEmpty()) {
      paramDexFile.getClassData().add(this.classData);
      CstArray cstArray = this.classData.getStaticValuesConstant();
      if (cstArray != null)
        this.staticValuesItem = mixedItemSection3.<EncodedArrayItem>intern(new EncodedArrayItem(cstArray)); 
    } 
    CstType cstType = this.superclass;
    if (cstType != null)
      typeIdsSection.intern(cstType); 
    TypeListItem typeListItem = this.interfaces;
    if (typeListItem != null)
      this.interfaces = mixedItemSection2.<TypeListItem>intern(typeListItem); 
    CstString cstString = this.sourceFile;
    if (cstString != null)
      stringIdsSection.intern(cstString); 
    if (!this.annotationsDirectory.isEmpty()) {
      if (this.annotationsDirectory.isInternable()) {
        this.annotationsDirectory = mixedItemSection1.<AnnotationsDirectoryItem>intern(this.annotationsDirectory);
        return;
      } 
      mixedItemSection1.add(this.annotationsDirectory);
    } 
  }
  
  public void addDirectMethod(EncodedMethod paramEncodedMethod) {
    this.classData.addDirectMethod(paramEncodedMethod);
  }
  
  public void addFieldAnnotations(CstFieldRef paramCstFieldRef, Annotations paramAnnotations, DexFile paramDexFile) {
    this.annotationsDirectory.addFieldAnnotations(paramCstFieldRef, paramAnnotations, paramDexFile);
  }
  
  public void addInstanceField(EncodedField paramEncodedField) {
    this.classData.addInstanceField(paramEncodedField);
  }
  
  public void addMethodAnnotations(CstMethodRef paramCstMethodRef, Annotations paramAnnotations, DexFile paramDexFile) {
    this.annotationsDirectory.addMethodAnnotations(paramCstMethodRef, paramAnnotations, paramDexFile);
  }
  
  public void addParameterAnnotations(CstMethodRef paramCstMethodRef, AnnotationsList paramAnnotationsList, DexFile paramDexFile) {
    this.annotationsDirectory.addParameterAnnotations(paramCstMethodRef, paramAnnotationsList, paramDexFile);
  }
  
  public void addStaticField(EncodedField paramEncodedField, Constant paramConstant) {
    this.classData.addStaticField(paramEncodedField, paramConstant);
  }
  
  public void addVirtualMethod(EncodedMethod paramEncodedMethod) {
    this.classData.addVirtualMethod(paramEncodedMethod);
  }
  
  public void debugPrint(Writer paramWriter, boolean paramBoolean) {
    String str2;
    String str1;
    PrintWriter printWriter = Writers.printWriterFor(paramWriter);
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(getClass().getName());
    stringBuilder1.append(" {");
    printWriter.println(stringBuilder1.toString());
    stringBuilder1 = new StringBuilder();
    stringBuilder1.append("  accessFlags: ");
    stringBuilder1.append(Hex.u2(this.accessFlags));
    printWriter.println(stringBuilder1.toString());
    stringBuilder1 = new StringBuilder();
    stringBuilder1.append("  superclass: ");
    stringBuilder1.append(this.superclass);
    printWriter.println(stringBuilder1.toString());
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append("  interfaces: ");
    TypeListItem typeListItem2 = this.interfaces;
    String str3 = "<none>";
    TypeListItem typeListItem1 = typeListItem2;
    if (typeListItem2 == null)
      str2 = "<none>"; 
    stringBuilder3.append(str2);
    printWriter.println(stringBuilder3.toString());
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("  sourceFile: ");
    CstString cstString = this.sourceFile;
    if (cstString == null) {
      str1 = str3;
    } else {
      str1 = str1.toQuoted();
    } 
    stringBuilder2.append(str1);
    printWriter.println(stringBuilder2.toString());
    this.classData.debugPrint(paramWriter, paramBoolean);
    this.annotationsDirectory.debugPrint(printWriter);
    printWriter.println("}");
  }
  
  public int getAccessFlags() {
    return this.accessFlags;
  }
  
  public TypeList getInterfaces() {
    TypeListItem typeListItem = this.interfaces;
    return (TypeList)((typeListItem == null) ? StdTypeList.EMPTY : typeListItem.getList());
  }
  
  public Annotations getMethodAnnotations(CstMethodRef paramCstMethodRef) {
    return this.annotationsDirectory.getMethodAnnotations(paramCstMethodRef);
  }
  
  public ArrayList<EncodedMethod> getMethods() {
    return this.classData.getMethods();
  }
  
  public AnnotationsList getParameterAnnotations(CstMethodRef paramCstMethodRef) {
    return this.annotationsDirectory.getParameterAnnotations(paramCstMethodRef);
  }
  
  public CstString getSourceFile() {
    return this.sourceFile;
  }
  
  public CstType getSuperclass() {
    return this.superclass;
  }
  
  public CstType getThisClass() {
    return this.thisClass;
  }
  
  public ItemType itemType() {
    return ItemType.TYPE_CLASS_DEF_ITEM;
  }
  
  public void setClassAnnotations(Annotations paramAnnotations, DexFile paramDexFile) {
    this.annotationsDirectory.setClassAnnotations(paramAnnotations, paramDexFile);
  }
  
  public int writeSize() {
    return 32;
  }
  
  public void writeTo(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    int i;
    int j;
    int m;
    boolean bool = paramAnnotatedOutput.annotates();
    TypeIdsSection typeIdsSection = paramDexFile.getTypeIds();
    int n = typeIdsSection.indexOf(this.thisClass);
    CstType cstType = this.superclass;
    int k = -1;
    if (cstType == null) {
      i = -1;
    } else {
      i = typeIdsSection.indexOf(cstType);
    } 
    int i1 = OffsettedItem.getAbsoluteOffsetOr0(this.interfaces);
    if (this.annotationsDirectory.isEmpty()) {
      j = 0;
    } else {
      j = this.annotationsDirectory.getAbsoluteOffset();
    } 
    if (this.sourceFile != null)
      k = paramDexFile.getStringIds().indexOf(this.sourceFile); 
    if (this.classData.isEmpty()) {
      m = 0;
    } else {
      m = this.classData.getAbsoluteOffset();
    } 
    int i2 = OffsettedItem.getAbsoluteOffsetOr0(this.staticValuesItem);
    if (bool) {
      String str2;
      String str1;
      StringBuilder stringBuilder3 = new StringBuilder();
      stringBuilder3.append(indexString());
      stringBuilder3.append(' ');
      stringBuilder3.append(this.thisClass.toHuman());
      paramAnnotatedOutput.annotate(0, stringBuilder3.toString());
      stringBuilder3 = new StringBuilder();
      stringBuilder3.append("  class_idx:           ");
      stringBuilder3.append(Hex.u4(n));
      paramAnnotatedOutput.annotate(4, stringBuilder3.toString());
      stringBuilder3 = new StringBuilder();
      stringBuilder3.append("  access_flags:        ");
      stringBuilder3.append(AccessFlags.classString(this.accessFlags));
      paramAnnotatedOutput.annotate(4, stringBuilder3.toString());
      StringBuilder stringBuilder4 = new StringBuilder();
      stringBuilder4.append("  superclass_idx:      ");
      stringBuilder4.append(Hex.u4(i));
      stringBuilder4.append(" // ");
      CstType cstType1 = this.superclass;
      String str3 = "<none>";
      if (cstType1 == null) {
        str2 = "<none>";
      } else {
        str2 = str2.toHuman();
      } 
      stringBuilder4.append(str2);
      paramAnnotatedOutput.annotate(4, stringBuilder4.toString());
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("  interfaces_off:      ");
      stringBuilder2.append(Hex.u4(i1));
      paramAnnotatedOutput.annotate(4, stringBuilder2.toString());
      if (i1 != 0) {
        TypeList typeList = this.interfaces.getList();
        int i4 = typeList.size();
        int i3;
        for (i3 = 0; i3 < i4; i3++) {
          stringBuilder4 = new StringBuilder();
          stringBuilder4.append("    ");
          stringBuilder4.append(typeList.getType(i3).toHuman());
          paramAnnotatedOutput.annotate(0, stringBuilder4.toString());
        } 
      } 
      stringBuilder4 = new StringBuilder();
      stringBuilder4.append("  source_file_idx:     ");
      stringBuilder4.append(Hex.u4(k));
      stringBuilder4.append(" // ");
      CstString cstString = this.sourceFile;
      if (cstString == null) {
        str1 = str3;
      } else {
        str1 = str1.toHuman();
      } 
      stringBuilder4.append(str1);
      paramAnnotatedOutput.annotate(4, stringBuilder4.toString());
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("  annotations_off:     ");
      stringBuilder1.append(Hex.u4(j));
      paramAnnotatedOutput.annotate(4, stringBuilder1.toString());
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("  class_data_off:      ");
      stringBuilder1.append(Hex.u4(m));
      paramAnnotatedOutput.annotate(4, stringBuilder1.toString());
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("  static_values_off:   ");
      stringBuilder1.append(Hex.u4(i2));
      paramAnnotatedOutput.annotate(4, stringBuilder1.toString());
    } 
    paramAnnotatedOutput.writeInt(n);
    paramAnnotatedOutput.writeInt(this.accessFlags);
    paramAnnotatedOutput.writeInt(i);
    paramAnnotatedOutput.writeInt(i1);
    paramAnnotatedOutput.writeInt(k);
    paramAnnotatedOutput.writeInt(j);
    paramAnnotatedOutput.writeInt(m);
    paramAnnotatedOutput.writeInt(i2);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\ClassDefItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */