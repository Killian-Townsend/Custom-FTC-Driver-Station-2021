package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;

public final class CodeAddress extends ZeroSizeInsn {
  private final boolean bindsClosely;
  
  public CodeAddress(SourcePosition paramSourcePosition) {
    this(paramSourcePosition, false);
  }
  
  public CodeAddress(SourcePosition paramSourcePosition, boolean paramBoolean) {
    super(paramSourcePosition);
    this.bindsClosely = paramBoolean;
  }
  
  protected String argString() {
    return null;
  }
  
  public boolean getBindsClosely() {
    return this.bindsClosely;
  }
  
  protected String listingString0(boolean paramBoolean) {
    return "code-address";
  }
  
  public final DalvInsn withRegisters(RegisterSpecList paramRegisterSpecList) {
    return new CodeAddress(getPosition());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\CodeAddress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */