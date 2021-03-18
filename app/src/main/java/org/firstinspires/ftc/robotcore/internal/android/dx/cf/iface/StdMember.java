package org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;

public abstract class StdMember implements Member {
  private final int accessFlags;
  
  private final AttributeList attributes;
  
  private final CstType definingClass;
  
  private final CstNat nat;
  
  public StdMember(CstType paramCstType, int paramInt, CstNat paramCstNat, AttributeList paramAttributeList) {
    if (paramCstType != null) {
      if (paramCstNat != null) {
        if (paramAttributeList != null) {
          this.definingClass = paramCstType;
          this.accessFlags = paramInt;
          this.nat = paramCstNat;
          this.attributes = paramAttributeList;
          return;
        } 
        throw new NullPointerException("attributes == null");
      } 
      throw new NullPointerException("nat == null");
    } 
    throw new NullPointerException("definingClass == null");
  }
  
  public final int getAccessFlags() {
    return this.accessFlags;
  }
  
  public final AttributeList getAttributes() {
    return this.attributes;
  }
  
  public final CstType getDefiningClass() {
    return this.definingClass;
  }
  
  public final CstString getDescriptor() {
    return this.nat.getDescriptor();
  }
  
  public final CstString getName() {
    return this.nat.getName();
  }
  
  public final CstNat getNat() {
    return this.nat;
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append(getClass().getName());
    stringBuffer.append('{');
    stringBuffer.append(this.nat.toHuman());
    stringBuffer.append('}');
    return stringBuffer.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\iface\StdMember.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */