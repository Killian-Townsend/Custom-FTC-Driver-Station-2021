package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.Collection;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dex.EncodedValueCodec;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ByteOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.Annotation;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.NameValuePair;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstAnnotation;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstBaseMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstBoolean;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstDouble;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstEnumRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFieldRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFloat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstLiteralBits;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class ValueEncoder {
  private static final int VALUE_ANNOTATION = 29;
  
  private static final int VALUE_ARRAY = 28;
  
  private static final int VALUE_BOOLEAN = 31;
  
  private static final int VALUE_BYTE = 0;
  
  private static final int VALUE_CHAR = 3;
  
  private static final int VALUE_DOUBLE = 17;
  
  private static final int VALUE_ENUM = 27;
  
  private static final int VALUE_FIELD = 25;
  
  private static final int VALUE_FLOAT = 16;
  
  private static final int VALUE_INT = 4;
  
  private static final int VALUE_LONG = 6;
  
  private static final int VALUE_METHOD = 26;
  
  private static final int VALUE_NULL = 30;
  
  private static final int VALUE_SHORT = 2;
  
  private static final int VALUE_STRING = 23;
  
  private static final int VALUE_TYPE = 24;
  
  private final DexFile file;
  
  private final AnnotatedOutput out;
  
  public ValueEncoder(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    if (paramDexFile != null) {
      if (paramAnnotatedOutput != null) {
        this.file = paramDexFile;
        this.out = paramAnnotatedOutput;
        return;
      } 
      throw new NullPointerException("out == null");
    } 
    throw new NullPointerException("file == null");
  }
  
  public static void addContents(DexFile paramDexFile, Annotation paramAnnotation) {
    TypeIdsSection typeIdsSection = paramDexFile.getTypeIds();
    StringIdsSection stringIdsSection = paramDexFile.getStringIds();
    typeIdsSection.intern(paramAnnotation.getType());
    for (NameValuePair nameValuePair : paramAnnotation.getNameValuePairs()) {
      stringIdsSection.intern(nameValuePair.getName());
      addContents(paramDexFile, nameValuePair.getValue());
    } 
  }
  
  public static void addContents(DexFile paramDexFile, Constant paramConstant) {
    CstArray.List list;
    if (paramConstant instanceof CstAnnotation) {
      addContents(paramDexFile, ((CstAnnotation)paramConstant).getAnnotation());
      return;
    } 
    if (paramConstant instanceof CstArray) {
      list = ((CstArray)paramConstant).getList();
      int j = list.size();
      for (int i = 0; i < j; i++)
        addContents(paramDexFile, list.get(i)); 
    } else {
      paramDexFile.internIfAppropriate((Constant)list);
    } 
  }
  
  public static String constantToHuman(Constant paramConstant) {
    if (constantToValueType(paramConstant) == 30)
      return "null"; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramConstant.typeName());
    stringBuilder.append(' ');
    stringBuilder.append(paramConstant.toHuman());
    return stringBuilder.toString();
  }
  
  private static int constantToValueType(Constant paramConstant) {
    if (paramConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstByte)
      return 0; 
    if (paramConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstShort)
      return 2; 
    if (paramConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstChar)
      return 3; 
    if (paramConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger)
      return 4; 
    if (paramConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstLong)
      return 6; 
    if (paramConstant instanceof CstFloat)
      return 16; 
    if (paramConstant instanceof CstDouble)
      return 17; 
    if (paramConstant instanceof CstString)
      return 23; 
    if (paramConstant instanceof CstType)
      return 24; 
    if (paramConstant instanceof CstFieldRef)
      return 25; 
    if (paramConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef)
      return 26; 
    if (paramConstant instanceof CstEnumRef)
      return 27; 
    if (paramConstant instanceof CstArray)
      return 28; 
    if (paramConstant instanceof CstAnnotation)
      return 29; 
    if (paramConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstKnownNull)
      return 30; 
    if (paramConstant instanceof CstBoolean)
      return 31; 
    throw new RuntimeException("Shouldn't happen");
  }
  
  public void writeAnnotation(Annotation paramAnnotation, boolean paramBoolean) {
    boolean bool;
    if (paramBoolean && this.out.annotates()) {
      bool = true;
    } else {
      bool = false;
    } 
    StringIdsSection stringIdsSection = this.file.getStringIds();
    TypeIdsSection typeIdsSection = this.file.getTypeIds();
    CstType cstType = paramAnnotation.getType();
    int i = typeIdsSection.indexOf(cstType);
    if (bool) {
      AnnotatedOutput annotatedOutput = this.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("  type_idx: ");
      stringBuilder.append(Hex.u4(i));
      stringBuilder.append(" // ");
      stringBuilder.append(cstType.toHuman());
      annotatedOutput.annotate(stringBuilder.toString());
    } 
    this.out.writeUleb128(typeIdsSection.indexOf(paramAnnotation.getType()));
    Collection collection = paramAnnotation.getNameValuePairs();
    i = collection.size();
    if (bool) {
      AnnotatedOutput annotatedOutput = this.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("  size: ");
      stringBuilder.append(Hex.u4(i));
      annotatedOutput.annotate(stringBuilder.toString());
    } 
    this.out.writeUleb128(i);
    Iterator<NameValuePair> iterator = collection.iterator();
    for (i = 0; iterator.hasNext(); i = j) {
      NameValuePair nameValuePair = iterator.next();
      CstString cstString = nameValuePair.getName();
      int k = stringIdsSection.indexOf(cstString);
      Constant constant = nameValuePair.getValue();
      int j = i;
      if (bool) {
        AnnotatedOutput annotatedOutput = this.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("  elements[");
        stringBuilder.append(i);
        stringBuilder.append("]:");
        annotatedOutput.annotate(0, stringBuilder.toString());
        j = i + 1;
        annotatedOutput = this.out;
        stringBuilder = new StringBuilder();
        stringBuilder.append("    name_idx: ");
        stringBuilder.append(Hex.u4(k));
        stringBuilder.append(" // ");
        stringBuilder.append(cstString.toHuman());
        annotatedOutput.annotate(stringBuilder.toString());
      } 
      this.out.writeUleb128(k);
      if (bool) {
        AnnotatedOutput annotatedOutput = this.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("    value: ");
        stringBuilder.append(constantToHuman(constant));
        annotatedOutput.annotate(stringBuilder.toString());
      } 
      writeConstant(constant);
    } 
    if (bool)
      this.out.endAnnotation(); 
  }
  
  public void writeArray(CstArray paramCstArray, boolean paramBoolean) {
    boolean bool;
    int i = 0;
    if (paramBoolean && this.out.annotates()) {
      bool = true;
    } else {
      bool = false;
    } 
    CstArray.List list = paramCstArray.getList();
    int j = list.size();
    if (bool) {
      AnnotatedOutput annotatedOutput = this.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("  size: ");
      stringBuilder.append(Hex.u4(j));
      annotatedOutput.annotate(stringBuilder.toString());
    } 
    this.out.writeUleb128(j);
    while (i < j) {
      Constant constant = list.get(i);
      if (bool) {
        AnnotatedOutput annotatedOutput = this.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("  [");
        stringBuilder.append(Integer.toHexString(i));
        stringBuilder.append("] ");
        stringBuilder.append(constantToHuman(constant));
        annotatedOutput.annotate(stringBuilder.toString());
      } 
      writeConstant(constant);
      i++;
    } 
    if (bool)
      this.out.endAnnotation(); 
  }
  
  public void writeConstant(Constant paramConstant) {
    CstFieldRef cstFieldRef;
    int i = constantToValueType(paramConstant);
    if (i != 0 && i != 6 && i != 2)
      if (i != 3) {
        if (i != 4) {
          if (i != 16) {
            if (i != 17) {
              switch (i) {
                default:
                  throw new RuntimeException("Shouldn't happen");
                case 31:
                  j = ((CstBoolean)paramConstant).getIntBits();
                  this.out.writeByte(j << 5 | i);
                  return;
                case 30:
                  this.out.writeByte(i);
                  return;
                case 29:
                  this.out.writeByte(i);
                  writeAnnotation(((CstAnnotation)paramConstant).getAnnotation(), false);
                  return;
                case 28:
                  this.out.writeByte(i);
                  writeArray((CstArray)paramConstant, false);
                  return;
                case 27:
                  cstFieldRef = ((CstEnumRef)paramConstant).getFieldRef();
                  j = this.file.getFieldIds().indexOf(cstFieldRef);
                  EncodedValueCodec.writeUnsignedIntegralValue((ByteOutput)this.out, i, j);
                  return;
                case 26:
                  j = this.file.getMethodIds().indexOf((CstBaseMethodRef)cstFieldRef);
                  EncodedValueCodec.writeUnsignedIntegralValue((ByteOutput)this.out, i, j);
                  return;
                case 25:
                  j = this.file.getFieldIds().indexOf(cstFieldRef);
                  EncodedValueCodec.writeUnsignedIntegralValue((ByteOutput)this.out, i, j);
                  return;
                case 24:
                  j = this.file.getTypeIds().indexOf((CstType)cstFieldRef);
                  EncodedValueCodec.writeUnsignedIntegralValue((ByteOutput)this.out, i, j);
                  return;
                case 23:
                  break;
              } 
              int j = this.file.getStringIds().indexOf((CstString)cstFieldRef);
              EncodedValueCodec.writeUnsignedIntegralValue((ByteOutput)this.out, i, j);
              return;
            } 
            long l2 = ((CstDouble)cstFieldRef).getLongBits();
            EncodedValueCodec.writeRightZeroExtendedValue((ByteOutput)this.out, i, l2);
            return;
          } 
          long l1 = ((CstFloat)cstFieldRef).getLongBits();
          EncodedValueCodec.writeRightZeroExtendedValue((ByteOutput)this.out, i, l1 << 32L);
          return;
        } 
      } else {
        long l1 = ((CstLiteralBits)cstFieldRef).getLongBits();
        EncodedValueCodec.writeUnsignedIntegralValue((ByteOutput)this.out, i, l1);
        return;
      }  
    long l = ((CstLiteralBits)cstFieldRef).getLongBits();
    EncodedValueCodec.writeSignedIntegralValue((ByteOutput)this.out, i, l);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\ValueEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */