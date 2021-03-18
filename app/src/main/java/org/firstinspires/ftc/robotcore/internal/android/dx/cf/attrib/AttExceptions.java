package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.MutabilityException;

public final class AttExceptions extends BaseAttribute {
  public static final String ATTRIBUTE_NAME = "Exceptions";
  
  private final TypeList exceptions;
  
  public AttExceptions(TypeList paramTypeList) {
    super("Exceptions");
    try {
      boolean bool = paramTypeList.isMutable();
      if (!bool) {
        this.exceptions = paramTypeList;
        return;
      } 
      throw new MutabilityException("exceptions.isMutable()");
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("exceptions == null");
    } 
  }
  
  public int byteLength() {
    return this.exceptions.size() * 2 + 8;
  }
  
  public TypeList getExceptions() {
    return this.exceptions;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\AttExceptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */