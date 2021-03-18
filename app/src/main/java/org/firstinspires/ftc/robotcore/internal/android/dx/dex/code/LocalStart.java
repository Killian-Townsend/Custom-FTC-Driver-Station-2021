package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.RegisterMapper;

public final class LocalStart extends ZeroSizeInsn {
  private final RegisterSpec local;
  
  public LocalStart(SourcePosition paramSourcePosition, RegisterSpec paramRegisterSpec) {
    super(paramSourcePosition);
    if (paramRegisterSpec != null) {
      this.local = paramRegisterSpec;
      return;
    } 
    throw new NullPointerException("local == null");
  }
  
  public static String localString(RegisterSpec paramRegisterSpec) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramRegisterSpec.regString());
    stringBuilder.append(' ');
    stringBuilder.append(paramRegisterSpec.getLocalItem().toString());
    stringBuilder.append(": ");
    stringBuilder.append(paramRegisterSpec.getTypeBearer().toHuman());
    return stringBuilder.toString();
  }
  
  protected String argString() {
    return this.local.toString();
  }
  
  public RegisterSpec getLocal() {
    return this.local;
  }
  
  protected String listingString0(boolean paramBoolean) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("local-start ");
    stringBuilder.append(localString(this.local));
    return stringBuilder.toString();
  }
  
  public DalvInsn withMapper(RegisterMapper paramRegisterMapper) {
    return new LocalStart(getPosition(), paramRegisterMapper.map(this.local));
  }
  
  public DalvInsn withRegisterOffset(int paramInt) {
    return new LocalStart(getPosition(), this.local.withOffset(paramInt));
  }
  
  public DalvInsn withRegisters(RegisterSpecList paramRegisterSpecList) {
    return new LocalStart(getPosition(), this.local);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\LocalStart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */