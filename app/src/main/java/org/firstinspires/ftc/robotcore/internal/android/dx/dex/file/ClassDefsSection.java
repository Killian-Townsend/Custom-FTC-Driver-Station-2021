package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class ClassDefsSection extends UniformItemSection {
  private final TreeMap<Type, ClassDefItem> classDefs = new TreeMap<Type, ClassDefItem>();
  
  private ArrayList<ClassDefItem> orderedDefs = null;
  
  public ClassDefsSection(DexFile paramDexFile) {
    super("class_defs", paramDexFile, 4);
  }
  
  private int orderItems0(Type paramType, int paramInt1, int paramInt2) {
    ClassDefItem classDefItem = this.classDefs.get(paramType);
    if (classDefItem != null) {
      TypeList typeList;
      if (classDefItem.hasIndex())
        return paramInt1; 
      if (paramInt2 >= 0) {
        int i = paramInt2 - 1;
        CstType cstType = classDefItem.getSuperclass();
        paramInt2 = paramInt1;
        if (cstType != null)
          paramInt2 = orderItems0(cstType.getClassType(), paramInt1, i); 
        typeList = classDefItem.getInterfaces();
        int j = typeList.size();
        for (paramInt1 = 0; paramInt1 < j; paramInt1++)
          paramInt2 = orderItems0(typeList.getType(paramInt1), paramInt2, i); 
        classDefItem.setIndex(paramInt2);
        this.orderedDefs.add(classDefItem);
        return paramInt2 + 1;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("class circularity with ");
      stringBuilder.append(typeList);
      throw new RuntimeException(stringBuilder.toString());
    } 
    return paramInt1;
  }
  
  public void add(ClassDefItem paramClassDefItem) {
    try {
      Type type = paramClassDefItem.getThisClass().getClassType();
      throwIfPrepared();
      if (this.classDefs.get(type) == null) {
        this.classDefs.put(type, paramClassDefItem);
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("already added: ");
      stringBuilder.append(type);
      throw new IllegalArgumentException(stringBuilder.toString());
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("clazz == null");
    } 
  }
  
  public IndexedItem get(Constant paramConstant) {
    if (paramConstant != null) {
      throwIfNotPrepared();
      Type type = ((CstType)paramConstant).getClassType();
      IndexedItem indexedItem = this.classDefs.get(type);
      if (indexedItem != null)
        return indexedItem; 
      throw new IllegalArgumentException("not found");
    } 
    throw new NullPointerException("cst == null");
  }
  
  public Collection<? extends Item> items() {
    ArrayList<ClassDefItem> arrayList = this.orderedDefs;
    return (Collection<? extends Item>)((arrayList != null) ? arrayList : this.classDefs.values());
  }
  
  protected void orderItems() {
    int j = this.classDefs.size();
    this.orderedDefs = new ArrayList<ClassDefItem>(j);
    Iterator<Type> iterator = this.classDefs.keySet().iterator();
    for (int i = 0; iterator.hasNext(); i = orderItems0(iterator.next(), i, j - i));
  }
  
  public void writeHeaderPart(AnnotatedOutput paramAnnotatedOutput) {
    int i;
    throwIfNotPrepared();
    int j = this.classDefs.size();
    if (j == 0) {
      i = 0;
    } else {
      i = getFileOffset();
    } 
    if (paramAnnotatedOutput.annotates()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("class_defs_size: ");
      stringBuilder.append(Hex.u4(j));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("class_defs_off:  ");
      stringBuilder.append(Hex.u4(i));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
    } 
    paramAnnotatedOutput.writeInt(j);
    paramAnnotatedOutput.writeInt(i);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\ClassDefsSection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */