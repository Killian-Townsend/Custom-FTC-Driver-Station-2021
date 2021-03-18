package org.firstinspires.ftc.robotcore.internal.android.dx.ssa.back;

import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.BasicRegisterMapper;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.RegisterMapper;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaMethod;

public class NullRegisterAllocator extends RegisterAllocator {
  public NullRegisterAllocator(SsaMethod paramSsaMethod, InterferenceGraph paramInterferenceGraph) {
    super(paramSsaMethod, paramInterferenceGraph);
  }
  
  public RegisterMapper allocateRegisters() {
    int j = this.ssaMeth.getRegCount();
    BasicRegisterMapper basicRegisterMapper = new BasicRegisterMapper(j);
    for (int i = 0; i < j; i++)
      basicRegisterMapper.addMapping(i, i * 2, 2); 
    return (RegisterMapper)basicRegisterMapper;
  }
  
  public boolean wantsParamsMovedHigh() {
    return false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\back\NullRegisterAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */