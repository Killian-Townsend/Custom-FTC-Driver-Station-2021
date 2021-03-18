package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttCode;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttLineNumberTable;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttLocalVariableTable;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttLocalVariableTypeTable;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Attribute;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.AttributeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Method;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Prototype;

public final class ConcreteMethod implements Method {
  private final boolean accSuper;
  
  private final AttCode attCode;
  
  private final LineNumberList lineNumbers;
  
  private final LocalVariableList localVariables;
  
  private final Method method;
  
  private final CstString sourceFile;
  
  public ConcreteMethod(Method paramMethod, int paramInt, CstString paramCstString, boolean paramBoolean1, boolean paramBoolean2) {
    boolean bool;
    this.method = paramMethod;
    if ((paramInt & 0x20) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.accSuper = bool;
    this.sourceFile = paramCstString;
    AttCode attCode = (AttCode)paramMethod.getAttributes().findFirst("Code");
    this.attCode = attCode;
    AttributeList attributeList = attCode.getAttributes();
    LineNumberList lineNumberList1 = LineNumberList.EMPTY;
    LineNumberList lineNumberList2 = lineNumberList1;
    if (paramBoolean1) {
      AttLineNumberTable attLineNumberTable = (AttLineNumberTable)attributeList.findFirst("LineNumberTable");
      while (true) {
        lineNumberList2 = lineNumberList1;
        if (attLineNumberTable != null) {
          lineNumberList1 = LineNumberList.concat(lineNumberList1, attLineNumberTable.getLineNumbers());
          attLineNumberTable = (AttLineNumberTable)attributeList.findNext((Attribute)attLineNumberTable);
          continue;
        } 
        break;
      } 
    } 
    this.lineNumbers = lineNumberList2;
    LocalVariableList localVariableList1 = LocalVariableList.EMPTY;
    LocalVariableList localVariableList2 = localVariableList1;
    if (paramBoolean2) {
      for (AttLocalVariableTable attLocalVariableTable = (AttLocalVariableTable)attributeList.findFirst("LocalVariableTable"); attLocalVariableTable != null; attLocalVariableTable = (AttLocalVariableTable)attributeList.findNext((Attribute)attLocalVariableTable))
        localVariableList1 = LocalVariableList.concat(localVariableList1, attLocalVariableTable.getLocalVariables()); 
      LocalVariableList localVariableList = LocalVariableList.EMPTY;
      for (AttLocalVariableTypeTable attLocalVariableTypeTable = (AttLocalVariableTypeTable)attributeList.findFirst("LocalVariableTypeTable"); attLocalVariableTypeTable != null; attLocalVariableTypeTable = (AttLocalVariableTypeTable)attributeList.findNext((Attribute)attLocalVariableTypeTable))
        localVariableList = LocalVariableList.concat(localVariableList, attLocalVariableTypeTable.getLocalVariables()); 
      localVariableList2 = localVariableList1;
      if (localVariableList.size() != 0)
        localVariableList2 = LocalVariableList.mergeDescriptorsAndSignatures(localVariableList1, localVariableList); 
    } 
    this.localVariables = localVariableList2;
  }
  
  public ConcreteMethod(Method paramMethod, ClassFile paramClassFile, boolean paramBoolean1, boolean paramBoolean2) {
    this(paramMethod, paramClassFile.getAccessFlags(), paramClassFile.getSourceFile(), paramBoolean1, paramBoolean2);
  }
  
  public boolean getAccSuper() {
    return this.accSuper;
  }
  
  public int getAccessFlags() {
    return this.method.getAccessFlags();
  }
  
  public AttributeList getAttributes() {
    return this.method.getAttributes();
  }
  
  public ByteCatchList getCatches() {
    return this.attCode.getCatches();
  }
  
  public BytecodeArray getCode() {
    return this.attCode.getCode();
  }
  
  public CstType getDefiningClass() {
    return this.method.getDefiningClass();
  }
  
  public CstString getDescriptor() {
    return this.method.getDescriptor();
  }
  
  public Prototype getEffectiveDescriptor() {
    return this.method.getEffectiveDescriptor();
  }
  
  public LineNumberList getLineNumbers() {
    return this.lineNumbers;
  }
  
  public LocalVariableList getLocalVariables() {
    return this.localVariables;
  }
  
  public int getMaxLocals() {
    return this.attCode.getMaxLocals();
  }
  
  public int getMaxStack() {
    return this.attCode.getMaxStack();
  }
  
  public CstString getName() {
    return this.method.getName();
  }
  
  public CstNat getNat() {
    return this.method.getNat();
  }
  
  public SourcePosition makeSourcePosistion(int paramInt) {
    return new SourcePosition(this.sourceFile, paramInt, this.lineNumbers.pcToLine(paramInt));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\ConcreteMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */