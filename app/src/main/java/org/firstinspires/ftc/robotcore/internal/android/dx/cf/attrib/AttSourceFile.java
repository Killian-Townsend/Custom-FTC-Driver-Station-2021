package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;

public final class AttSourceFile extends BaseAttribute {
  public static final String ATTRIBUTE_NAME = "SourceFile";
  
  private final CstString sourceFile;
  
  public AttSourceFile(CstString paramCstString) {
    super("SourceFile");
    if (paramCstString != null) {
      this.sourceFile = paramCstString;
      return;
    } 
    throw new NullPointerException("sourceFile == null");
  }
  
  public int byteLength() {
    return 8;
  }
  
  public CstString getSourceFile() {
    return this.sourceFile;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\AttSourceFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */