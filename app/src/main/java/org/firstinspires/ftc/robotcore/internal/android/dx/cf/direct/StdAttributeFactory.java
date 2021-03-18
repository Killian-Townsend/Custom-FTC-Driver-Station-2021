package org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct;

import java.io.IOException;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttAnnotationDefault;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttCode;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttConstantValue;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttDeprecated;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttEnclosingMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttExceptions;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttInnerClasses;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttLineNumberTable;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttLocalVariableTable;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttLocalVariableTypeTable;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttRuntimeInvisibleAnnotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttRuntimeInvisibleParameterAnnotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttRuntimeVisibleAnnotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttRuntimeVisibleParameterAnnotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttSignature;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttSourceFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttSynthetic;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.InnerClassList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.ByteCatchList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.BytecodeArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.LineNumberList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.LocalVariableList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Attribute;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.AttributeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseException;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseObserver;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.StdAttributeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.AnnotationVisibility;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.ConstantPool;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.TypedConstant;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public class StdAttributeFactory extends AttributeFactory {
  public static final StdAttributeFactory THE_ONE = new StdAttributeFactory();
  
  private Attribute annotationDefault(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 < 2)
      throwSeverelyTruncated(); 
    return (Attribute)new AttAnnotationDefault((new AnnotationParser(paramDirectClassFile, paramInt1, paramInt2, paramParseObserver)).parseValueAttribute(), paramInt2);
  }
  
  private Attribute code(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    ByteCatchList byteCatchList;
    if (paramInt2 < 12)
      return throwSeverelyTruncated(); 
    ByteArray byteArray = paramDirectClassFile.getBytes();
    ConstantPool constantPool = paramDirectClassFile.getConstantPool();
    int k = byteArray.getUnsignedShort(paramInt1);
    int j = paramInt1 + 2;
    int m = byteArray.getUnsignedShort(j);
    int n = paramInt1 + 4;
    int i = byteArray.getInt(n);
    if (paramParseObserver != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("max_stack: ");
      stringBuilder.append(Hex.u2(k));
      paramParseObserver.parsed(byteArray, paramInt1, 2, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("max_locals: ");
      stringBuilder.append(Hex.u2(m));
      paramParseObserver.parsed(byteArray, j, 2, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("code_length: ");
      stringBuilder.append(Hex.u4(i));
      paramParseObserver.parsed(byteArray, n, 4, stringBuilder.toString());
    } 
    j = paramInt1 + 8;
    paramInt2 -= 8;
    if (paramInt2 < i + 4)
      return throwTruncated(); 
    int i1 = j + i;
    BytecodeArray bytecodeArray = new BytecodeArray(byteArray.slice(j, i1), constantPool);
    if (paramParseObserver != null)
      bytecodeArray.forEach(new CodeObserver(bytecodeArray.getBytes(), paramParseObserver)); 
    n = byteArray.getUnsignedShort(i1);
    if (n == 0) {
      byteCatchList = ByteCatchList.EMPTY;
    } else {
      byteCatchList = new ByteCatchList(n);
    } 
    if (paramParseObserver != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("exception_table_length: ");
      stringBuilder.append(Hex.u2(n));
      paramParseObserver.parsed(byteArray, i1, 2, stringBuilder.toString());
    } 
    j = i1 + 2;
    i = paramInt2 - i - 2;
    if (i < n * 8 + 2)
      return throwTruncated(); 
    for (paramInt2 = 0; paramInt2 < n; paramInt2++) {
      if (paramParseObserver != null)
        paramParseObserver.changeIndent(1); 
      i1 = byteArray.getUnsignedShort(j);
      int i2 = byteArray.getUnsignedShort(j + 2);
      int i3 = byteArray.getUnsignedShort(j + 4);
      CstType cstType = (CstType)constantPool.get0Ok(byteArray.getUnsignedShort(j + 6));
      byteCatchList.set(paramInt2, i1, i2, i3, cstType);
      if (paramParseObserver != null) {
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Hex.u2(i1));
        stringBuilder.append("build/generated/source/aidl");
        stringBuilder.append(Hex.u2(i2));
        stringBuilder.append(" -> ");
        stringBuilder.append(Hex.u2(i3));
        stringBuilder.append(" ");
        if (cstType == null) {
          str = "<any>";
        } else {
          str = str.toHuman();
        } 
        stringBuilder.append(str);
        paramParseObserver.parsed(byteArray, j, 8, stringBuilder.toString());
      } 
      j += 8;
      i -= 8;
      if (paramParseObserver != null)
        paramParseObserver.changeIndent(-1); 
    } 
    byteCatchList.setImmutable();
    AttributeListParser attributeListParser = new AttributeListParser(paramDirectClassFile, 3, j, this);
    attributeListParser.setObserver(paramParseObserver);
    StdAttributeList stdAttributeList = attributeListParser.getList();
    stdAttributeList.setImmutable();
    paramInt2 = attributeListParser.getEndOffset() - j;
    return (Attribute)((paramInt2 != i) ? throwBadLength(paramInt2 + j - paramInt1) : new AttCode(k, m, bytecodeArray, byteCatchList, (AttributeList)stdAttributeList));
  }
  
  private Attribute constantValue(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 != 2)
      return throwBadLength(2); 
    ByteArray byteArray = paramDirectClassFile.getBytes();
    TypedConstant typedConstant = (TypedConstant)paramDirectClassFile.getConstantPool().get(byteArray.getUnsignedShort(paramInt1));
    AttConstantValue attConstantValue = new AttConstantValue(typedConstant);
    if (paramParseObserver != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("value: ");
      stringBuilder.append(typedConstant);
      paramParseObserver.parsed(byteArray, paramInt1, 2, stringBuilder.toString());
    } 
    return (Attribute)attConstantValue;
  }
  
  private Attribute deprecated(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    return (Attribute)((paramInt2 != 0) ? throwBadLength(0) : new AttDeprecated());
  }
  
  private Attribute enclosingMethod(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 != 4)
      throwBadLength(4); 
    ByteArray byteArray = paramDirectClassFile.getBytes();
    ConstantPool constantPool = paramDirectClassFile.getConstantPool();
    CstType cstType = (CstType)constantPool.get(byteArray.getUnsignedShort(paramInt1));
    paramInt2 = paramInt1 + 2;
    CstNat cstNat = (CstNat)constantPool.get0Ok(byteArray.getUnsignedShort(paramInt2));
    AttEnclosingMethod attEnclosingMethod = new AttEnclosingMethod(cstType, cstNat);
    if (paramParseObserver != null) {
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("class: ");
      stringBuilder2.append(cstType);
      paramParseObserver.parsed(byteArray, paramInt1, 2, stringBuilder2.toString());
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("method: ");
      stringBuilder1.append(DirectClassFile.stringOrNone(cstNat));
      paramParseObserver.parsed(byteArray, paramInt2, 2, stringBuilder1.toString());
    } 
    return (Attribute)attEnclosingMethod;
  }
  
  private Attribute exceptions(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 < 2)
      return throwSeverelyTruncated(); 
    ByteArray byteArray = paramDirectClassFile.getBytes();
    int i = byteArray.getUnsignedShort(paramInt1);
    if (paramParseObserver != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("number_of_exceptions: ");
      stringBuilder.append(Hex.u2(i));
      paramParseObserver.parsed(byteArray, paramInt1, 2, stringBuilder.toString());
    } 
    int j = i * 2;
    if (paramInt2 - 2 != j)
      throwBadLength(j + 2); 
    return (Attribute)new AttExceptions(paramDirectClassFile.makeTypeList(paramInt1 + 2, i));
  }
  
  private Attribute innerClasses(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 < 2)
      return throwSeverelyTruncated(); 
    ByteArray byteArray = paramDirectClassFile.getBytes();
    ConstantPool constantPool = paramDirectClassFile.getConstantPool();
    int j = byteArray.getUnsignedShort(paramInt1);
    if (paramParseObserver != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("number_of_classes: ");
      stringBuilder.append(Hex.u2(j));
      paramParseObserver.parsed(byteArray, paramInt1, 2, stringBuilder.toString());
    } 
    int i = paramInt1 + 2;
    paramInt1 = j * 8;
    if (paramInt2 - 2 != paramInt1)
      throwBadLength(paramInt1 + 2); 
    InnerClassList innerClassList = new InnerClassList(j);
    paramInt1 = 0;
    paramInt2 = i;
    while (paramInt1 < j) {
      i = byteArray.getUnsignedShort(paramInt2);
      int k = paramInt2 + 2;
      int m = byteArray.getUnsignedShort(k);
      int n = paramInt2 + 4;
      int i1 = byteArray.getUnsignedShort(n);
      int i2 = paramInt2 + 6;
      int i3 = byteArray.getUnsignedShort(i2);
      CstType cstType2 = (CstType)constantPool.get(i);
      CstType cstType1 = (CstType)constantPool.get0Ok(m);
      CstString cstString = (CstString)constantPool.get0Ok(i1);
      innerClassList.set(paramInt1, cstType2, cstType1, cstString, i3);
      if (paramParseObserver != null) {
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("inner_class: ");
        stringBuilder4.append(DirectClassFile.stringOrNone(cstType2));
        paramParseObserver.parsed(byteArray, paramInt2, 2, stringBuilder4.toString());
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("  outer_class: ");
        stringBuilder3.append(DirectClassFile.stringOrNone(cstType1));
        paramParseObserver.parsed(byteArray, k, 2, stringBuilder3.toString());
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("  name: ");
        stringBuilder2.append(DirectClassFile.stringOrNone(cstString));
        paramParseObserver.parsed(byteArray, n, 2, stringBuilder2.toString());
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("  access_flags: ");
        stringBuilder1.append(AccessFlags.innerClassString(i3));
        paramParseObserver.parsed(byteArray, i2, 2, stringBuilder1.toString());
      } 
      paramInt2 += 8;
      paramInt1++;
    } 
    innerClassList.setImmutable();
    return (Attribute)new AttInnerClasses(innerClassList);
  }
  
  private Attribute lineNumberTable(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 < 2)
      return throwSeverelyTruncated(); 
    ByteArray byteArray = paramDirectClassFile.getBytes();
    int j = byteArray.getUnsignedShort(paramInt1);
    if (paramParseObserver != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("line_number_table_length: ");
      stringBuilder.append(Hex.u2(j));
      paramParseObserver.parsed(byteArray, paramInt1, 2, stringBuilder.toString());
    } 
    paramInt1 += 2;
    int i = j * 4;
    if (paramInt2 - 2 != i)
      throwBadLength(i + 2); 
    LineNumberList lineNumberList = new LineNumberList(j);
    i = 0;
    paramInt2 = paramInt1;
    for (paramInt1 = i; paramInt1 < j; paramInt1++) {
      i = byteArray.getUnsignedShort(paramInt2);
      int k = byteArray.getUnsignedShort(paramInt2 + 2);
      lineNumberList.set(paramInt1, i, k);
      if (paramParseObserver != null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Hex.u2(i));
        stringBuilder.append(" ");
        stringBuilder.append(k);
        paramParseObserver.parsed(byteArray, paramInt2, 4, stringBuilder.toString());
      } 
      paramInt2 += 4;
    } 
    lineNumberList.setImmutable();
    return (Attribute)new AttLineNumberTable(lineNumberList);
  }
  
  private Attribute localVariableTable(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 < 2)
      return throwSeverelyTruncated(); 
    ByteArray byteArray = paramDirectClassFile.getBytes();
    int i = byteArray.getUnsignedShort(paramInt1);
    if (paramParseObserver != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("local_variable_table_length: ");
      stringBuilder.append(Hex.u2(i));
      paramParseObserver.parsed(byteArray, paramInt1, 2, stringBuilder.toString());
    } 
    return (Attribute)new AttLocalVariableTable(parseLocalVariables(byteArray.slice(paramInt1 + 2, paramInt1 + paramInt2), paramDirectClassFile.getConstantPool(), paramParseObserver, i, false));
  }
  
  private Attribute localVariableTypeTable(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 < 2)
      return throwSeverelyTruncated(); 
    ByteArray byteArray = paramDirectClassFile.getBytes();
    int i = byteArray.getUnsignedShort(paramInt1);
    if (paramParseObserver != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("local_variable_type_table_length: ");
      stringBuilder.append(Hex.u2(i));
      paramParseObserver.parsed(byteArray, paramInt1, 2, stringBuilder.toString());
    } 
    return (Attribute)new AttLocalVariableTypeTable(parseLocalVariables(byteArray.slice(paramInt1 + 2, paramInt1 + paramInt2), paramDirectClassFile.getConstantPool(), paramParseObserver, i, true));
  }
  
  private LocalVariableList parseLocalVariables(ByteArray paramByteArray, ConstantPool paramConstantPool, ParseObserver paramParseObserver, int paramInt, boolean paramBoolean) {
    int i = paramByteArray.size();
    int j = paramInt * 10;
    if (i != j)
      throwBadLength(j + 2); 
    ByteArray.MyDataInputStream myDataInputStream = paramByteArray.makeDataInputStream();
    LocalVariableList localVariableList = new LocalVariableList(paramInt);
    i = 0;
    while (true) {
      if (i < paramInt) {
        try {
          CstString cstString2;
          CstString cstString3;
          j = myDataInputStream.readUnsignedShort();
          int k = myDataInputStream.readUnsignedShort();
          int m = myDataInputStream.readUnsignedShort();
          int n = myDataInputStream.readUnsignedShort();
          int i1 = myDataInputStream.readUnsignedShort();
          CstString cstString4 = (CstString)paramConstantPool.get(m);
          CstString cstString1 = (CstString)paramConstantPool.get(n);
          if (paramBoolean) {
            cstString2 = null;
            cstString3 = cstString1;
          } else {
            cstString3 = null;
            cstString2 = cstString1;
          } 
          localVariableList.set(i, j, k, cstString4, cstString2, cstString3, i1);
          if (paramParseObserver != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Hex.u2(j));
            stringBuilder.append("build/generated/source/aidl");
            stringBuilder.append(Hex.u2(j + k));
            stringBuilder.append(" ");
            stringBuilder.append(Hex.u2(i1));
            stringBuilder.append(" ");
            stringBuilder.append(cstString4.toHuman());
            stringBuilder.append(" ");
            stringBuilder.append(cstString1.toHuman());
            paramParseObserver.parsed(paramByteArray, i * 10, 10, stringBuilder.toString());
          } 
          i++;
        } catch (IOException iOException) {
          throw new RuntimeException("shouldn't happen", iOException);
        } 
        continue;
      } 
      localVariableList.setImmutable();
      return localVariableList;
    } 
  }
  
  private Attribute runtimeInvisibleAnnotations(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 < 2)
      throwSeverelyTruncated(); 
    return (Attribute)new AttRuntimeInvisibleAnnotations((new AnnotationParser(paramDirectClassFile, paramInt1, paramInt2, paramParseObserver)).parseAnnotationAttribute(AnnotationVisibility.BUILD), paramInt2);
  }
  
  private Attribute runtimeInvisibleParameterAnnotations(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 < 2)
      throwSeverelyTruncated(); 
    return (Attribute)new AttRuntimeInvisibleParameterAnnotations((new AnnotationParser(paramDirectClassFile, paramInt1, paramInt2, paramParseObserver)).parseParameterAttribute(AnnotationVisibility.BUILD), paramInt2);
  }
  
  private Attribute runtimeVisibleAnnotations(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 < 2)
      throwSeverelyTruncated(); 
    return (Attribute)new AttRuntimeVisibleAnnotations((new AnnotationParser(paramDirectClassFile, paramInt1, paramInt2, paramParseObserver)).parseAnnotationAttribute(AnnotationVisibility.RUNTIME), paramInt2);
  }
  
  private Attribute runtimeVisibleParameterAnnotations(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 < 2)
      throwSeverelyTruncated(); 
    return (Attribute)new AttRuntimeVisibleParameterAnnotations((new AnnotationParser(paramDirectClassFile, paramInt1, paramInt2, paramParseObserver)).parseParameterAttribute(AnnotationVisibility.RUNTIME), paramInt2);
  }
  
  private Attribute signature(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 != 2)
      throwBadLength(2); 
    ByteArray byteArray = paramDirectClassFile.getBytes();
    CstString cstString = (CstString)paramDirectClassFile.getConstantPool().get(byteArray.getUnsignedShort(paramInt1));
    AttSignature attSignature = new AttSignature(cstString);
    if (paramParseObserver != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("signature: ");
      stringBuilder.append(cstString);
      paramParseObserver.parsed(byteArray, paramInt1, 2, stringBuilder.toString());
    } 
    return (Attribute)attSignature;
  }
  
  private Attribute sourceFile(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramInt2 != 2)
      throwBadLength(2); 
    ByteArray byteArray = paramDirectClassFile.getBytes();
    CstString cstString = (CstString)paramDirectClassFile.getConstantPool().get(byteArray.getUnsignedShort(paramInt1));
    AttSourceFile attSourceFile = new AttSourceFile(cstString);
    if (paramParseObserver != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("source: ");
      stringBuilder.append(cstString);
      paramParseObserver.parsed(byteArray, paramInt1, 2, stringBuilder.toString());
    } 
    return (Attribute)attSourceFile;
  }
  
  private Attribute synthetic(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    return (Attribute)((paramInt2 != 0) ? throwBadLength(0) : new AttSynthetic());
  }
  
  private static Attribute throwBadLength(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("bad attribute length; expected length ");
    stringBuilder.append(Hex.u4(paramInt));
    throw new ParseException(stringBuilder.toString());
  }
  
  private static Attribute throwSeverelyTruncated() {
    throw new ParseException("severely truncated attribute");
  }
  
  private static Attribute throwTruncated() {
    throw new ParseException("truncated attribute");
  }
  
  protected Attribute parse0(DirectClassFile paramDirectClassFile, int paramInt1, String paramString, int paramInt2, int paramInt3, ParseObserver paramParseObserver) {
    if (paramInt1 != 0) {
      if (paramInt1 != 1) {
        if (paramInt1 != 2) {
          if (paramInt1 == 3) {
            if (paramString == "LineNumberTable")
              return lineNumberTable(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
            if (paramString == "LocalVariableTable")
              return localVariableTable(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
            if (paramString == "LocalVariableTypeTable")
              return localVariableTypeTable(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
          } 
        } else {
          if (paramString == "AnnotationDefault")
            return annotationDefault(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
          if (paramString == "Code")
            return code(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
          if (paramString == "Deprecated")
            return deprecated(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
          if (paramString == "Exceptions")
            return exceptions(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
          if (paramString == "RuntimeInvisibleAnnotations")
            return runtimeInvisibleAnnotations(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
          if (paramString == "RuntimeVisibleAnnotations")
            return runtimeVisibleAnnotations(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
          if (paramString == "RuntimeInvisibleParameterAnnotations")
            return runtimeInvisibleParameterAnnotations(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
          if (paramString == "RuntimeVisibleParameterAnnotations")
            return runtimeVisibleParameterAnnotations(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
          if (paramString == "Signature")
            return signature(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
          if (paramString == "Synthetic")
            return synthetic(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
        } 
      } else {
        if (paramString == "ConstantValue")
          return constantValue(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
        if (paramString == "Deprecated")
          return deprecated(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
        if (paramString == "RuntimeInvisibleAnnotations")
          return runtimeInvisibleAnnotations(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
        if (paramString == "RuntimeVisibleAnnotations")
          return runtimeVisibleAnnotations(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
        if (paramString == "Signature")
          return signature(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
        if (paramString == "Synthetic")
          return synthetic(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
      } 
    } else {
      if (paramString == "Deprecated")
        return deprecated(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
      if (paramString == "EnclosingMethod")
        return enclosingMethod(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
      if (paramString == "InnerClasses")
        return innerClasses(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
      if (paramString == "RuntimeInvisibleAnnotations")
        return runtimeInvisibleAnnotations(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
      if (paramString == "RuntimeVisibleAnnotations")
        return runtimeVisibleAnnotations(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
      if (paramString == "Synthetic")
        return synthetic(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
      if (paramString == "Signature")
        return signature(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
      if (paramString == "SourceFile")
        return sourceFile(paramDirectClassFile, paramInt2, paramInt3, paramParseObserver); 
    } 
    return super.parse0(paramDirectClassFile, paramInt1, paramString, paramInt2, paramInt3, paramParseObserver);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\direct\StdAttributeFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */