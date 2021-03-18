package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;

public final class AttAnnotationDefault extends BaseAttribute {
  public static final String ATTRIBUTE_NAME = "AnnotationDefault";
  
  private final int byteLength;
  
  private final Constant value;
  
  public AttAnnotationDefault(Constant paramConstant, int paramInt) {
    super("AnnotationDefault");
    if (paramConstant != null) {
      this.value = paramConstant;
      this.byteLength = paramInt;
      return;
    } 
    throw new NullPointerException("value == null");
  }
  
  public int byteLength() {
    return this.byteLength + 6;
  }
  
  public Constant getValue() {
    return this.value;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\AttAnnotationDefault.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */