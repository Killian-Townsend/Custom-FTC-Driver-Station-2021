package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.LocalVariableList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.MutabilityException;

public abstract class BaseLocalVariables extends BaseAttribute {
  private final LocalVariableList localVariables;
  
  public BaseLocalVariables(String paramString, LocalVariableList paramLocalVariableList) {
    super(paramString);
    try {
      boolean bool = paramLocalVariableList.isMutable();
      if (!bool) {
        this.localVariables = paramLocalVariableList;
        return;
      } 
      throw new MutabilityException("localVariables.isMutable()");
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("localVariables == null");
    } 
  }
  
  public final int byteLength() {
    return this.localVariables.size() * 10 + 8;
  }
  
  public final LocalVariableList getLocalVariables() {
    return this.localVariables;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\BaseLocalVariables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */