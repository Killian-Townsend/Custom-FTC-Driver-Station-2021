package org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;

public interface Member extends HasAttribute {
  int getAccessFlags();
  
  AttributeList getAttributes();
  
  CstType getDefiningClass();
  
  CstString getDescriptor();
  
  CstString getName();
  
  CstNat getNat();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\iface\Member.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */