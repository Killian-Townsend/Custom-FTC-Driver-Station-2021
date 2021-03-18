package org.firstinspires.ftc.robotcore.internal.android.dx.ssa.back;

import java.util.BitSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.CstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.BasicRegisterMapper;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.NormalSsaInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.RegisterMapper;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.BitIntSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntSet;

public class FirstFitAllocator extends RegisterAllocator {
  private static final boolean PRESLOT_PARAMS = true;
  
  private final BitSet mapped;
  
  public FirstFitAllocator(SsaMethod paramSsaMethod, InterferenceGraph paramInterferenceGraph) {
    super(paramSsaMethod, paramInterferenceGraph);
    this.mapped = new BitSet(paramSsaMethod.getRegCount());
  }
  
  private int paramNumberFromMoveParam(NormalSsaInsn paramNormalSsaInsn) {
    return ((CstInteger)((CstInsn)paramNormalSsaInsn.getOriginalRopInsn()).getConstant()).getValue();
  }
  
  public RegisterMapper allocateRegisters() {
    Object object;
    int k = this.ssaMeth.getRegCount();
    BasicRegisterMapper basicRegisterMapper = new BasicRegisterMapper(k);
    int i = this.ssaMeth.getParamWidth();
    int j = 0;
    while (j < k) {
      boolean bool;
      Object object2;
      if (this.mapped.get(j)) {
        Object object3 = object;
        continue;
      } 
      int m = getCategoryForSsaReg(j);
      BitIntSet bitIntSet = new BitIntSet(k);
      this.interference.mergeInterferenceSet(j, (IntSet)bitIntSet);
      if (isDefinitionMoveParam(j)) {
        int i1 = paramNumberFromMoveParam((NormalSsaInsn)this.ssaMeth.getDefinitionForRegister(j));
        basicRegisterMapper.addMapping(j, i1, m);
        bool = true;
      } else {
        basicRegisterMapper.addMapping(j, object, m);
        Object object3 = object;
        bool = false;
      } 
      int n = j + 1;
      while (true) {
        n++;
        object2 = SYNTHETIC_LOCAL_VARIABLE_7;
      } 
      this.mapped.set(j);
      Object object1 = object;
      if (!bool)
        int i1 = object + object2; 
      continue;
      j++;
      object = SYNTHETIC_LOCAL_VARIABLE_4;
    } 
    return (RegisterMapper)basicRegisterMapper;
  }
  
  public boolean wantsParamsMovedHigh() {
    return true;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\back\FirstFitAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */