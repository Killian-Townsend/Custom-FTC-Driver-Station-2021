package org.firstinspires.ftc.robotcore.internal.android.dx.dex.cf;

import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.ConcreteMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.Ropper;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.DirectClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Field;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.FieldList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Method;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.MethodList;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.DexOptions;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvCode;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.RopTranslator;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.file.ClassDefItem;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.file.DexFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.file.EncodedField;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.file.EncodedMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.file.FieldIdsSection;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.file.MethodIdsSection;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.Annotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.AnnotationsList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.DexTranslationAdvice;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.LocalVariableExtractor;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.LocalVariableInfo;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RopMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.TranslationAdvice;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.ConstantPool;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstBaseMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstBoolean;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstByte;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstChar;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstEnumRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFieldRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInterfaceMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstShort;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.TypedConstant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.Optimizer;

public class CfTranslator {
  private static final boolean DEBUG = false;
  
  private static TypedConstant coerceConstant(TypedConstant paramTypedConstant, Type paramType) {
    if (paramTypedConstant.getType().equals(paramType))
      return paramTypedConstant; 
    int i = paramType.getBasicType();
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          if (i == 8)
            return (TypedConstant)CstShort.make(((CstInteger)paramTypedConstant).getValue()); 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("can't coerce ");
          stringBuilder.append(paramTypedConstant);
          stringBuilder.append(" to ");
          stringBuilder.append(paramType);
          throw new UnsupportedOperationException(stringBuilder.toString());
        } 
        return (TypedConstant)CstChar.make(((CstInteger)paramTypedConstant).getValue());
      } 
      return (TypedConstant)CstByte.make(((CstInteger)paramTypedConstant).getValue());
    } 
    return (TypedConstant)CstBoolean.make(((CstInteger)paramTypedConstant).getValue());
  }
  
  private static void processFields(DirectClassFile paramDirectClassFile, ClassDefItem paramClassDefItem, DexFile paramDexFile) {
    CstType cstType = paramDirectClassFile.getThisClass();
    FieldList fieldList = paramDirectClassFile.getFields();
    int j = fieldList.size();
    int i = 0;
    while (i < j) {
      Field field = fieldList.get(i);
      try {
        CstFieldRef cstFieldRef = new CstFieldRef(cstType, field.getNat());
        int k = field.getAccessFlags();
        if (AccessFlags.isStatic(k)) {
          TypedConstant typedConstant2 = field.getConstantValue();
          EncodedField encodedField = new EncodedField(cstFieldRef, k);
          TypedConstant typedConstant1 = typedConstant2;
          if (typedConstant2 != null)
            typedConstant1 = coerceConstant(typedConstant2, cstFieldRef.getType()); 
          paramClassDefItem.addStaticField(encodedField, (Constant)typedConstant1);
        } else {
          paramClassDefItem.addInstanceField(new EncodedField(cstFieldRef, k));
        } 
        Annotations annotations = AttributeTranslator.getAnnotations(field.getAttributes());
        if (annotations.size() != 0)
          paramClassDefItem.addFieldAnnotations(cstFieldRef, annotations, paramDexFile); 
        paramDexFile.getFieldIds().intern(cstFieldRef);
        i++;
      } catch (RuntimeException runtimeException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("...while processing ");
        stringBuilder.append(field.getName().toHuman());
        stringBuilder.append(" ");
        stringBuilder.append(field.getDescriptor().toHuman());
        throw ExceptionWithContext.withContext(runtimeException, stringBuilder.toString());
      } 
    } 
  }
  
  private static void processMethods(DirectClassFile paramDirectClassFile, CfOptions paramCfOptions, DexOptions paramDexOptions, ClassDefItem paramClassDefItem, DexFile paramDexFile) {
    CstType cstType = paramDirectClassFile.getThisClass();
    MethodList methodList = paramDirectClassFile.getMethods();
    int j = methodList.size();
    for (int i = 0;; i++) {
      StringBuilder stringBuilder;
      boolean bool;
      boolean bool1;
      boolean bool2;
      boolean bool3;
      boolean bool4;
      DalvCode dalvCode;
      CstMethodRef cstMethodRef;
      Method method;
      if (i < j) {
        method = methodList.get(i);
        try {
          cstMethodRef = new CstMethodRef(cstType, method.getNat());
          m = method.getAccessFlags();
          bool1 = AccessFlags.isStatic(m);
          bool2 = AccessFlags.isPrivate(m);
          bool3 = AccessFlags.isNative(m);
          bool4 = AccessFlags.isAbstract(m);
          boolean bool6 = cstMethodRef.isInstanceInit();
          boolean bool5 = true;
          if (bool6 || cstMethodRef.isClassInit()) {
            bool = true;
          } else {
            bool = false;
          } 
        } catch (RuntimeException runtimeException) {
          stringBuilder = new StringBuilder();
          stringBuilder.append("...while processing ");
          stringBuilder.append(method.getName().toHuman());
          stringBuilder.append(" ");
          stringBuilder.append(method.getDescriptor().toHuman());
          throw ExceptionWithContext.withContext(runtimeException, stringBuilder.toString());
        } 
      } else {
        return;
      } 
      if (bool3 || bool4) {
        dalvCode = null;
      } else {
        boolean bool5;
        RopMethod ropMethod1;
        if (((CfOptions)stringBuilder).positionInfo == 1)
          bool5 = false; 
        ConcreteMethod concreteMethod = new ConcreteMethod(method, (ClassFile)runtimeException, bool5, ((CfOptions)stringBuilder).localInfo);
        DexTranslationAdvice dexTranslationAdvice = DexTranslationAdvice.THE_ONE;
        RopMethod ropMethod2 = Ropper.convert(concreteMethod, (TranslationAdvice)dexTranslationAdvice, methodList);
        int n = cstMethodRef.getParameterWordCount(bool1);
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(cstType.getClassType().getDescriptor());
        stringBuilder1.append(".");
        stringBuilder1.append(method.getName().getString());
        String str = stringBuilder1.toString();
        if (((CfOptions)stringBuilder).optimize && OptimizerOptions.shouldOptimize(str)) {
          ropMethod1 = Optimizer.optimize(ropMethod2, n, bool1, ((CfOptions)stringBuilder).localInfo, (TranslationAdvice)dexTranslationAdvice);
          if (((CfOptions)stringBuilder).statistics)
            CodeStatistics.updateRopStatistics(ropMethod2, ropMethod1); 
        } else {
          ropMethod1 = ropMethod2;
          ropMethod2 = null;
        } 
        if (((CfOptions)stringBuilder).localInfo) {
          LocalVariableInfo localVariableInfo = LocalVariableExtractor.extract(ropMethod1);
        } else {
          str = null;
        } 
        DalvCode dalvCode1 = RopTranslator.translate(ropMethod1, ((CfOptions)stringBuilder).positionInfo, (LocalVariableInfo)str, n, paramDexOptions);
        if (((CfOptions)stringBuilder).statistics && ropMethod2 != null)
          updateDexStatistics((CfOptions)stringBuilder, paramDexOptions, ropMethod1, ropMethod2, (LocalVariableInfo)str, n, concreteMethod.getCode().size()); 
        dalvCode = dalvCode1;
      } 
      int k = m;
      if (AccessFlags.isSynchronized(m)) {
        m |= 0x20000;
        k = m;
        if (!bool3)
          k = m & 0xFFFFFFDF; 
      } 
      int m = k;
      if (bool)
        m = k | 0x10000; 
      EncodedMethod encodedMethod = new EncodedMethod(cstMethodRef, m, dalvCode, AttributeTranslator.getExceptions(method));
      if (cstMethodRef.isInstanceInit() || cstMethodRef.isClassInit() || bool1 || bool2) {
        paramClassDefItem.addDirectMethod(encodedMethod);
      } else {
        paramClassDefItem.addVirtualMethod(encodedMethod);
      } 
      Annotations annotations = AttributeTranslator.getMethodAnnotations(method);
      if (annotations.size() != 0)
        paramClassDefItem.addMethodAnnotations(cstMethodRef, annotations, paramDexFile); 
      AnnotationsList annotationsList = AttributeTranslator.getParameterAnnotations(method);
      if (annotationsList.size() != 0)
        paramClassDefItem.addParameterAnnotations(cstMethodRef, annotationsList, paramDexFile); 
      paramDexFile.getMethodIds().intern((CstBaseMethodRef)cstMethodRef);
    } 
  }
  
  public static ClassDefItem translate(DirectClassFile paramDirectClassFile, byte[] paramArrayOfbyte, CfOptions paramCfOptions, DexOptions paramDexOptions, DexFile paramDexFile) {
    try {
      return translate0(paramDirectClassFile, paramArrayOfbyte, paramCfOptions, paramDexOptions, paramDexFile);
    } catch (RuntimeException runtimeException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("...while processing ");
      stringBuilder.append(paramDirectClassFile.getFilePath());
      throw ExceptionWithContext.withContext(runtimeException, stringBuilder.toString());
    } 
  }
  
  private static ClassDefItem translate0(DirectClassFile paramDirectClassFile, byte[] paramArrayOfbyte, CfOptions paramCfOptions, DexOptions paramDexOptions, DexFile paramDexFile) {
    CstString cstString;
    OptimizerOptions.loadOptimizeLists(paramCfOptions.optimizeListFile, paramCfOptions.dontOptimizeListFile);
    CstType cstType = paramDirectClassFile.getThisClass();
    int i = paramDirectClassFile.getAccessFlags();
    if (paramCfOptions.positionInfo == 1) {
      paramArrayOfbyte = null;
    } else {
      cstString = paramDirectClassFile.getSourceFile();
    } 
    ClassDefItem classDefItem = new ClassDefItem(cstType, i & 0xFFFFFFDF, paramDirectClassFile.getSuperclass(), paramDirectClassFile.getInterfaces(), cstString);
    Annotations annotations = AttributeTranslator.getClassAnnotations(paramDirectClassFile, paramCfOptions);
    if (annotations.size() != 0)
      classDefItem.setClassAnnotations(annotations, paramDexFile); 
    FieldIdsSection fieldIdsSection = paramDexFile.getFieldIds();
    MethodIdsSection methodIdsSection = paramDexFile.getMethodIds();
    processFields(paramDirectClassFile, classDefItem, paramDexFile);
    processMethods(paramDirectClassFile, paramCfOptions, paramDexOptions, classDefItem, paramDexFile);
    ConstantPool constantPool = paramDirectClassFile.getConstantPool();
    int j = constantPool.size();
    for (i = 0; i < j; i++) {
      Constant constant = constantPool.getOrNull(i);
      if (constant instanceof CstMethodRef) {
        methodIdsSection.intern((CstBaseMethodRef)constant);
      } else if (constant instanceof CstInterfaceMethodRef) {
        methodIdsSection.intern((CstBaseMethodRef)((CstInterfaceMethodRef)constant).toMethodRef());
      } else if (constant instanceof CstFieldRef) {
        fieldIdsSection.intern((CstFieldRef)constant);
      } else if (constant instanceof CstEnumRef) {
        fieldIdsSection.intern(((CstEnumRef)constant).getFieldRef());
      } 
    } 
    return classDefItem;
  }
  
  private static void updateDexStatistics(CfOptions paramCfOptions, DexOptions paramDexOptions, RopMethod paramRopMethod1, RopMethod paramRopMethod2, LocalVariableInfo paramLocalVariableInfo, int paramInt1, int paramInt2) {
    DalvCode dalvCode2 = RopTranslator.translate(paramRopMethod1, paramCfOptions.positionInfo, paramLocalVariableInfo, paramInt1, paramDexOptions);
    DalvCode dalvCode1 = RopTranslator.translate(paramRopMethod2, paramCfOptions.positionInfo, paramLocalVariableInfo, paramInt1, paramDexOptions);
    DalvCode.AssignIndicesCallback assignIndicesCallback = new DalvCode.AssignIndicesCallback() {
        public int getIndex(Constant param1Constant) {
          return 0;
        }
      };
    dalvCode2.assignIndices(assignIndicesCallback);
    dalvCode1.assignIndices(assignIndicesCallback);
    CodeStatistics.updateDexStatistics(dalvCode1, dalvCode2);
    CodeStatistics.updateOriginalByteCount(paramInt2);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\cf\CfTranslator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */