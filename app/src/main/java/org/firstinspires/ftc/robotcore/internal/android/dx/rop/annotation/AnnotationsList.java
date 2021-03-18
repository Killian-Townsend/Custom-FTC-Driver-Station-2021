package org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.FixedSizeList;

public final class AnnotationsList extends FixedSizeList {
  public static final AnnotationsList EMPTY = new AnnotationsList(0);
  
  public AnnotationsList(int paramInt) {
    super(paramInt);
  }
  
  public static AnnotationsList combine(AnnotationsList paramAnnotationsList1, AnnotationsList paramAnnotationsList2) {
    int i = paramAnnotationsList1.size();
    if (i == paramAnnotationsList2.size()) {
      AnnotationsList annotationsList = new AnnotationsList(i);
      for (int j = 0; j < i; j++)
        annotationsList.set(j, Annotations.combine(paramAnnotationsList1.get(j), paramAnnotationsList2.get(j))); 
      annotationsList.setImmutable();
      return annotationsList;
    } 
    throw new IllegalArgumentException("list1.size() != list2.size()");
  }
  
  public Annotations get(int paramInt) {
    return (Annotations)get0(paramInt);
  }
  
  public void set(int paramInt, Annotations paramAnnotations) {
    paramAnnotations.throwIfMutable();
    set0(paramInt, paramAnnotations);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\annotation\AnnotationsList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */