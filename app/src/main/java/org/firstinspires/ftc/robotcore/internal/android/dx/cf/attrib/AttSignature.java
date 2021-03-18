package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;

public final class AttSignature extends BaseAttribute {
  public static final String ATTRIBUTE_NAME = "Signature";
  
  private final CstString signature;
  
  public AttSignature(CstString paramCstString) {
    super("Signature");
    if (paramCstString != null) {
      this.signature = paramCstString;
      return;
    } 
    throw new NullPointerException("signature == null");
  }
  
  public int byteLength() {
    return 8;
  }
  
  public CstString getSignature() {
    return this.signature;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\AttSignature.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */