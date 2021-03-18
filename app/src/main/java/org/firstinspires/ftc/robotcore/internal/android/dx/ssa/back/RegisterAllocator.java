package org.firstinspires.ftc.robotcore.internal.android.dx.ssa.back;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Insn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.PlainInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rops;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.NormalSsaInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.RegisterMapper;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaBasicBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntIterator;

public abstract class RegisterAllocator {
  protected final InterferenceGraph interference;
  
  protected final SsaMethod ssaMeth;
  
  public RegisterAllocator(SsaMethod paramSsaMethod, InterferenceGraph paramInterferenceGraph) {
    this.ssaMeth = paramSsaMethod;
    this.interference = paramInterferenceGraph;
  }
  
  public abstract RegisterMapper allocateRegisters();
  
  protected final int getCategoryForSsaReg(int paramInt) {
    SsaInsn ssaInsn = this.ssaMeth.getDefinitionForRegister(paramInt);
    return (ssaInsn == null) ? 1 : ssaInsn.getResult().getCategory();
  }
  
  protected final RegisterSpec getDefinitionSpecForSsaReg(int paramInt) {
    SsaInsn ssaInsn = this.ssaMeth.getDefinitionForRegister(paramInt);
    return (ssaInsn == null) ? null : ssaInsn.getResult();
  }
  
  protected final RegisterSpec insertMoveBefore(SsaInsn paramSsaInsn, RegisterSpec paramRegisterSpec) {
    SsaBasicBlock ssaBasicBlock = paramSsaInsn.getBlock();
    ArrayList<SsaInsn> arrayList = ssaBasicBlock.getInsns();
    int i = arrayList.indexOf(paramSsaInsn);
    if (i >= 0) {
      RegisterSpecList registerSpecList;
      if (i == arrayList.size() - 1) {
        RegisterSpec registerSpec = RegisterSpec.make(this.ssaMeth.makeNewSsaReg(), paramRegisterSpec.getTypeBearer());
        arrayList.add(i, SsaInsn.makeFromRop((Insn)new PlainInsn(Rops.opMove((TypeBearer)registerSpec.getType()), SourcePosition.NO_INFO, registerSpec, RegisterSpecList.make(paramRegisterSpec)), ssaBasicBlock));
        int j = registerSpec.getReg();
        IntIterator intIterator = ssaBasicBlock.getLiveOutRegs().iterator();
        while (intIterator.hasNext())
          this.interference.add(j, intIterator.next()); 
        registerSpecList = paramSsaInsn.getSources();
        int k = registerSpecList.size();
        for (i = 0; i < k; i++)
          this.interference.add(j, registerSpecList.get(i).getReg()); 
        this.ssaMeth.onInsnsChanged();
        return registerSpec;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Adding move here not supported:");
      stringBuilder.append(registerSpecList.toHuman());
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("specified insn is not in this block");
  }
  
  protected boolean isDefinitionMoveParam(int paramInt) {
    SsaInsn ssaInsn = this.ssaMeth.getDefinitionForRegister(paramInt);
    boolean bool = ssaInsn instanceof NormalSsaInsn;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((NormalSsaInsn)ssaInsn).getOpcode().getOpcode() == 3)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public abstract boolean wantsParamsMovedHigh();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\back\RegisterAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */