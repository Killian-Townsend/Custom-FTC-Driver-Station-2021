package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.Annotation;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.AnnotationVisibility;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.NameValuePair;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstAnnotation;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstKnownNull;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;

public final class AnnotationUtils {
  private static final CstString ACCESS_FLAGS_STRING;
  
  private static final CstType ANNOTATION_DEFAULT_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/AnnotationDefault;"));
  
  private static final CstType ENCLOSING_CLASS_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/EnclosingClass;"));
  
  private static final CstType ENCLOSING_METHOD_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/EnclosingMethod;"));
  
  private static final CstType INNER_CLASS_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/InnerClass;"));
  
  private static final CstType MEMBER_CLASSES_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/MemberClasses;"));
  
  private static final CstString NAME_STRING;
  
  private static final CstType SIGNATURE_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/Signature;"));
  
  private static final CstType THROWS_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/Throws;"));
  
  private static final CstString VALUE_STRING;
  
  static {
    ACCESS_FLAGS_STRING = new CstString("accessFlags");
    NAME_STRING = new CstString("name");
    VALUE_STRING = new CstString("value");
  }
  
  public static Annotation makeAnnotationDefault(Annotation paramAnnotation) {
    Annotation annotation = new Annotation(ANNOTATION_DEFAULT_TYPE, AnnotationVisibility.SYSTEM);
    annotation.put(new NameValuePair(VALUE_STRING, (Constant)new CstAnnotation(paramAnnotation)));
    annotation.setImmutable();
    return annotation;
  }
  
  private static CstArray makeCstArray(TypeList paramTypeList) {
    int j = paramTypeList.size();
    CstArray.List list = new CstArray.List(j);
    for (int i = 0; i < j; i++)
      list.set(i, (Constant)CstType.intern(paramTypeList.getType(i))); 
    list.setImmutable();
    return new CstArray(list);
  }
  
  public static Annotation makeEnclosingClass(CstType paramCstType) {
    Annotation annotation = new Annotation(ENCLOSING_CLASS_TYPE, AnnotationVisibility.SYSTEM);
    annotation.put(new NameValuePair(VALUE_STRING, (Constant)paramCstType));
    annotation.setImmutable();
    return annotation;
  }
  
  public static Annotation makeEnclosingMethod(CstMethodRef paramCstMethodRef) {
    Annotation annotation = new Annotation(ENCLOSING_METHOD_TYPE, AnnotationVisibility.SYSTEM);
    annotation.put(new NameValuePair(VALUE_STRING, (Constant)paramCstMethodRef));
    annotation.setImmutable();
    return annotation;
  }
  
  public static Annotation makeInnerClass(CstString paramCstString, int paramInt) {
    CstKnownNull cstKnownNull;
    Annotation annotation = new Annotation(INNER_CLASS_TYPE, AnnotationVisibility.SYSTEM);
    if (paramCstString == null)
      cstKnownNull = CstKnownNull.THE_ONE; 
    annotation.put(new NameValuePair(NAME_STRING, (Constant)cstKnownNull));
    annotation.put(new NameValuePair(ACCESS_FLAGS_STRING, (Constant)CstInteger.make(paramInt)));
    annotation.setImmutable();
    return annotation;
  }
  
  public static Annotation makeMemberClasses(TypeList paramTypeList) {
    CstArray cstArray = makeCstArray(paramTypeList);
    Annotation annotation = new Annotation(MEMBER_CLASSES_TYPE, AnnotationVisibility.SYSTEM);
    annotation.put(new NameValuePair(VALUE_STRING, (Constant)cstArray));
    annotation.setImmutable();
    return annotation;
  }
  
  public static Annotation makeSignature(CstString paramCstString) {
    Annotation annotation = new Annotation(SIGNATURE_TYPE, AnnotationVisibility.SYSTEM);
    String str = paramCstString.getString();
    int m = str.length();
    ArrayList<String> arrayList = new ArrayList(20);
    byte b = 0;
    for (int k = 0; k < m; k = n) {
      char c = str.charAt(k);
      int n = k + 1;
      int i1 = n;
      if (c == 'L') {
        i1 = n;
        while (true) {
          n = i1;
          if (i1 < m) {
            n = str.charAt(i1);
            if (n == 59) {
              n = i1 + 1;
              break;
            } 
            if (n == 60) {
              n = i1;
              break;
            } 
            i1++;
            continue;
          } 
          break;
        } 
      } else {
        while (true) {
          n = i1;
          if (i1 < m) {
            if (str.charAt(i1) == 'L') {
              n = i1;
              break;
            } 
            i1++;
            continue;
          } 
          break;
        } 
      } 
      arrayList.add(str.substring(k, n));
    } 
    int j = arrayList.size();
    CstArray.List list = new CstArray.List(j);
    for (int i = b; i < j; i++)
      list.set(i, (Constant)new CstString(arrayList.get(i))); 
    list.setImmutable();
    annotation.put(new NameValuePair(VALUE_STRING, (Constant)new CstArray(list)));
    annotation.setImmutable();
    return annotation;
  }
  
  public static Annotation makeThrows(TypeList paramTypeList) {
    CstArray cstArray = makeCstArray(paramTypeList);
    Annotation annotation = new Annotation(THROWS_TYPE, AnnotationVisibility.SYSTEM);
    annotation.put(new NameValuePair(VALUE_STRING, (Constant)cstArray));
    annotation.setImmutable();
    return annotation;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\AnnotationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */