package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.RegisterMapper;

public final class LocalSnapshot extends ZeroSizeInsn {
  private final RegisterSpecSet locals;
  
  public LocalSnapshot(SourcePosition paramSourcePosition, RegisterSpecSet paramRegisterSpecSet) {
    super(paramSourcePosition);
    if (paramRegisterSpecSet != null) {
      this.locals = paramRegisterSpecSet;
      return;
    } 
    throw new NullPointerException("locals == null");
  }
  
  protected String argString() {
    return this.locals.toString();
  }
  
  public RegisterSpecSet getLocals() {
    return this.locals;
  }
  
  protected String listingString0(boolean paramBoolean) {
    int i = this.locals.size();
    int j = this.locals.getMaxSize();
    StringBuffer stringBuffer = new StringBuffer(i * 40 + 100);
    stringBuffer.append("local-snapshot");
    for (i = 0; i < j; i++) {
      RegisterSpec registerSpec = this.locals.get(i);
      if (registerSpec != null) {
        stringBuffer.append("\n  ");
        stringBuffer.append(LocalStart.localString(registerSpec));
      } 
    } 
    return stringBuffer.toString();
  }
  
  public DalvInsn withMapper(RegisterMapper paramRegisterMapper) {
    return new LocalSnapshot(getPosition(), paramRegisterMapper.map(this.locals));
  }
  
  public DalvInsn withRegisterOffset(int paramInt) {
    return new LocalSnapshot(getPosition(), this.locals.withOffset(paramInt));
  }
  
  public DalvInsn withRegisters(RegisterSpecList paramRegisterSpecList) {
    return new LocalSnapshot(getPosition(), this.locals);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\LocalSnapshot.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */