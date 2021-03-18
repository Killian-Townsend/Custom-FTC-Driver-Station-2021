package org.firstinspires.ftc.robotcore.internal.android.dx.dex.cf;

import java.io.PrintStream;
import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttAnnotationDefault;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttEnclosingMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttExceptions;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttInnerClasses;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttRuntimeInvisibleAnnotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttRuntimeInvisibleParameterAnnotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttRuntimeVisibleAnnotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttRuntimeVisibleParameterAnnotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttSignature;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.InnerClassList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.DirectClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.AttributeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Method;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.MethodList;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.file.AnnotationUtils;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.Annotation;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.AnnotationVisibility;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.Annotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.AnnotationsList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.NameValuePair;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Warning;

class AttributeTranslator {
  public static Annotations getAnnotations(AttributeList paramAttributeList) {
    Annotations annotations2 = getAnnotations0(paramAttributeList);
    Annotation annotation = getSignature(paramAttributeList);
    Annotations annotations1 = annotations2;
    if (annotation != null)
      annotations1 = Annotations.combine(annotations2, annotation); 
    return annotations1;
  }
  
  private static Annotations getAnnotations0(AttributeList paramAttributeList) {
    AttRuntimeVisibleAnnotations attRuntimeVisibleAnnotations = (AttRuntimeVisibleAnnotations)paramAttributeList.findFirst("RuntimeVisibleAnnotations");
    AttRuntimeInvisibleAnnotations attRuntimeInvisibleAnnotations = (AttRuntimeInvisibleAnnotations)paramAttributeList.findFirst("RuntimeInvisibleAnnotations");
    return (attRuntimeVisibleAnnotations == null) ? ((attRuntimeInvisibleAnnotations == null) ? Annotations.EMPTY : attRuntimeInvisibleAnnotations.getAnnotations()) : ((attRuntimeInvisibleAnnotations == null) ? attRuntimeVisibleAnnotations.getAnnotations() : Annotations.combine(attRuntimeVisibleAnnotations.getAnnotations(), attRuntimeInvisibleAnnotations.getAnnotations()));
  }
  
  public static Annotations getClassAnnotations(DirectClassFile paramDirectClassFile, CfOptions paramCfOptions) {
    boolean bool;
    CstType cstType = paramDirectClassFile.getThisClass();
    AttributeList attributeList = paramDirectClassFile.getAttributes();
    Annotations annotations3 = getAnnotations(attributeList);
    Annotation annotation = translateEnclosingMethod(attributeList);
    if (annotation == null) {
      bool = true;
    } else {
      bool = false;
    } 
    try {
      Annotations annotations = translateInnerClasses(cstType, attributeList, bool);
      annotations2 = annotations3;
      if (annotations != null)
        annotations2 = Annotations.combine(annotations3, annotations); 
    } catch (Warning warning) {
      PrintStream printStream = paramCfOptions.warn;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("warning: ");
      stringBuilder.append(warning.getMessage());
      printStream.println(stringBuilder.toString());
      annotations2 = annotations3;
    } 
    Annotations annotations1 = annotations2;
    if (annotation != null)
      annotations1 = Annotations.combine(annotations2, annotation); 
    Annotations annotations2 = annotations1;
    if (AccessFlags.isAnnotation(paramDirectClassFile.getAccessFlags())) {
      Annotation annotation1 = translateAnnotationDefaults(paramDirectClassFile);
      annotations2 = annotations1;
      if (annotation1 != null)
        annotations2 = Annotations.combine(annotations1, annotation1); 
    } 
    return annotations2;
  }
  
  public static TypeList getExceptions(Method paramMethod) {
    AttExceptions attExceptions = (AttExceptions)paramMethod.getAttributes().findFirst("Exceptions");
    return (TypeList)((attExceptions == null) ? StdTypeList.EMPTY : attExceptions.getExceptions());
  }
  
  public static Annotations getMethodAnnotations(Method paramMethod) {
    Annotations annotations2 = getAnnotations(paramMethod.getAttributes());
    TypeList typeList = getExceptions(paramMethod);
    Annotations annotations1 = annotations2;
    if (typeList.size() != 0)
      annotations1 = Annotations.combine(annotations2, AnnotationUtils.makeThrows(typeList)); 
    return annotations1;
  }
  
  public static AnnotationsList getParameterAnnotations(Method paramMethod) {
    AttributeList attributeList = paramMethod.getAttributes();
    AttRuntimeVisibleParameterAnnotations attRuntimeVisibleParameterAnnotations = (AttRuntimeVisibleParameterAnnotations)attributeList.findFirst("RuntimeVisibleParameterAnnotations");
    AttRuntimeInvisibleParameterAnnotations attRuntimeInvisibleParameterAnnotations = (AttRuntimeInvisibleParameterAnnotations)attributeList.findFirst("RuntimeInvisibleParameterAnnotations");
    return (attRuntimeVisibleParameterAnnotations == null) ? ((attRuntimeInvisibleParameterAnnotations == null) ? AnnotationsList.EMPTY : attRuntimeInvisibleParameterAnnotations.getParameterAnnotations()) : ((attRuntimeInvisibleParameterAnnotations == null) ? attRuntimeVisibleParameterAnnotations.getParameterAnnotations() : AnnotationsList.combine(attRuntimeVisibleParameterAnnotations.getParameterAnnotations(), attRuntimeInvisibleParameterAnnotations.getParameterAnnotations()));
  }
  
  private static Annotation getSignature(AttributeList paramAttributeList) {
    AttSignature attSignature = (AttSignature)paramAttributeList.findFirst("Signature");
    return (attSignature == null) ? null : AnnotationUtils.makeSignature(attSignature.getSignature());
  }
  
  private static Annotation translateAnnotationDefaults(DirectClassFile paramDirectClassFile) {
    CstType cstType = paramDirectClassFile.getThisClass();
    MethodList methodList = paramDirectClassFile.getMethods();
    int j = methodList.size();
    Annotation annotation = new Annotation(cstType, AnnotationVisibility.EMBEDDED);
    int i = 0;
    boolean bool = false;
    while (i < j) {
      Method method = methodList.get(i);
      AttAnnotationDefault attAnnotationDefault = (AttAnnotationDefault)method.getAttributes().findFirst("AnnotationDefault");
      if (attAnnotationDefault != null) {
        annotation.add(new NameValuePair(method.getNat().getName(), attAnnotationDefault.getValue()));
        bool = true;
      } 
      i++;
    } 
    if (!bool)
      return null; 
    annotation.setImmutable();
    return AnnotationUtils.makeAnnotationDefault(annotation);
  }
  
  private static Annotation translateEnclosingMethod(AttributeList paramAttributeList) {
    AttEnclosingMethod attEnclosingMethod = (AttEnclosingMethod)paramAttributeList.findFirst("EnclosingMethod");
    if (attEnclosingMethod == null)
      return null; 
    CstType cstType = attEnclosingMethod.getEnclosingClass();
    CstNat cstNat = attEnclosingMethod.getMethod();
    return (cstNat == null) ? AnnotationUtils.makeEnclosingClass(cstType) : AnnotationUtils.makeEnclosingMethod(new CstMethodRef(cstType, cstNat));
  }
  
  private static Annotations translateInnerClasses(CstType paramCstType, AttributeList paramAttributeList, boolean paramBoolean) {
    AttInnerClasses attInnerClasses = (AttInnerClasses)paramAttributeList.findFirst("InnerClasses");
    if (attInnerClasses == null)
      return null; 
    InnerClassList innerClassList = attInnerClasses.getInnerClasses();
    int j = innerClassList.size();
    ArrayList<Type> arrayList = new ArrayList();
    boolean bool = false;
    attInnerClasses = null;
    int i = 0;
    while (i < j) {
      AttInnerClasses attInnerClasses1;
      InnerClassList.Item item = innerClassList.get(i);
      CstType cstType = item.getInnerClass();
      if (cstType.equals(paramCstType)) {
        InnerClassList.Item item1 = item;
      } else {
        attInnerClasses1 = attInnerClasses;
        if (paramCstType.equals(item.getOuterClass())) {
          arrayList.add(cstType.getClassType());
          attInnerClasses1 = attInnerClasses;
        } 
      } 
      i++;
      attInnerClasses = attInnerClasses1;
    } 
    j = arrayList.size();
    if (attInnerClasses == null && j == 0)
      return null; 
    Annotations annotations = new Annotations();
    if (attInnerClasses != null) {
      annotations.add(AnnotationUtils.makeInnerClass(attInnerClasses.getInnerName(), attInnerClasses.getAccessFlags()));
      if (paramBoolean)
        if (attInnerClasses.getOuterClass() != null) {
          annotations.add(AnnotationUtils.makeEnclosingClass(attInnerClasses.getOuterClass()));
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Ignoring InnerClasses attribute for an anonymous inner class\n(");
          stringBuilder.append(paramCstType.toHuman());
          stringBuilder.append(") that doesn't come with an\nassociated EnclosingMethod attribute. This class was probably produced by a\ncompiler that did not target the modern .class file format. The recommended\nsolution is to recompile the class from source, using an up-to-date compiler\nand without specifying any \"-target\" type options. The consequence of ignoring\nthis warning is that reflective operations on this class will incorrectly\nindicate that it is *not* an inner class.");
          throw new Warning(stringBuilder.toString());
        }  
    } 
    if (j != 0) {
      StdTypeList stdTypeList = new StdTypeList(j);
      for (i = bool; i < j; i++)
        stdTypeList.set(i, arrayList.get(i)); 
      stdTypeList.setImmutable();
      annotations.add(AnnotationUtils.makeMemberClasses((TypeList)stdTypeList));
    } 
    annotations.setImmutable();
    return annotations;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\cf\AttributeTranslator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */