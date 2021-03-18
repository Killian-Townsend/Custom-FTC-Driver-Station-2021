package org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.ConstantPool;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;

public interface ClassFile extends HasAttribute {
  int getAccessFlags();
  
  AttributeList getAttributes();
  
  ConstantPool getConstantPool();
  
  FieldList getFields();
  
  TypeList getInterfaces();
  
  int getMagic();
  
  int getMajorVersion();
  
  MethodList getMethods();
  
  int getMinorVersion();
  
  CstString getSourceFile();
  
  CstType getSuperclass();
  
  CstType getThisClass();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\iface\ClassFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */