package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.LineNumberList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.MutabilityException;

public final class AttLineNumberTable extends BaseAttribute {
  public static final String ATTRIBUTE_NAME = "LineNumberTable";
  
  private final LineNumberList lineNumbers;
  
  public AttLineNumberTable(LineNumberList paramLineNumberList) {
    super("LineNumberTable");
    try {
      boolean bool = paramLineNumberList.isMutable();
      if (!bool) {
        this.lineNumbers = paramLineNumberList;
        return;
      } 
      throw new MutabilityException("lineNumbers.isMutable()");
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("lineNumbers == null");
    } 
  }
  
  public int byteLength() {
    return this.lineNumbers.size() * 4 + 8;
  }
  
  public LineNumberList getLineNumbers() {
    return this.lineNumbers;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\AttLineNumberTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */