package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Insn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.PlainCstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.PlainInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegOps;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rop;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rops;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.TranslationAdvice;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstLiteralBits;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;

public class LiteralOpUpgrader {
  private final SsaMethod ssaMeth;
  
  private LiteralOpUpgrader(SsaMethod paramSsaMethod) {
    this.ssaMeth = paramSsaMethod;
  }
  
  private static boolean isConstIntZeroOrKnownNull(RegisterSpec paramRegisterSpec) {
    TypeBearer typeBearer = paramRegisterSpec.getTypeBearer();
    boolean bool = typeBearer instanceof CstLiteralBits;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((CstLiteralBits)typeBearer).getLongBits() == 0L)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public static void process(SsaMethod paramSsaMethod) {
    (new LiteralOpUpgrader(paramSsaMethod)).run();
  }
  
  private void replacePlainInsn(NormalSsaInsn paramNormalSsaInsn, RegisterSpecList paramRegisterSpecList, int paramInt, Constant paramConstant) {
    PlainInsn plainInsn;
    PlainCstInsn plainCstInsn;
    Insn insn = paramNormalSsaInsn.getOriginalRopInsn();
    Rop rop = Rops.ropFor(paramInt, (TypeBearer)paramNormalSsaInsn.getResult(), (TypeList)paramRegisterSpecList, paramConstant);
    if (paramConstant == null) {
      plainInsn = new PlainInsn(rop, insn.getPosition(), paramNormalSsaInsn.getResult(), paramRegisterSpecList);
    } else {
      plainCstInsn = new PlainCstInsn(rop, insn.getPosition(), paramNormalSsaInsn.getResult(), (RegisterSpecList)plainInsn, paramConstant);
    } 
    NormalSsaInsn normalSsaInsn = new NormalSsaInsn((Insn)plainCstInsn, paramNormalSsaInsn.getBlock());
    ArrayList<SsaInsn> arrayList = paramNormalSsaInsn.getBlock().getInsns();
    this.ssaMeth.onInsnRemoved(paramNormalSsaInsn);
    arrayList.set(arrayList.lastIndexOf(paramNormalSsaInsn), normalSsaInsn);
    this.ssaMeth.onInsnAdded(normalSsaInsn);
  }
  
  private void run() {
    final TranslationAdvice advice = Optimizer.getAdvice();
    this.ssaMeth.forEachInsn(new SsaInsn.Visitor() {
          public void visitMoveInsn(NormalSsaInsn param1NormalSsaInsn) {}
          
          public void visitNonMoveInsn(NormalSsaInsn param1NormalSsaInsn) {
            Rop rop = param1NormalSsaInsn.getOriginalRopInsn().getOpcode();
            RegisterSpecList registerSpecList = param1NormalSsaInsn.getSources();
            if (LiteralOpUpgrader.this.tryReplacingWithConstant(param1NormalSsaInsn))
              return; 
            if (registerSpecList.size() != 2)
              return; 
            if (rop.getBranchingness() == 4) {
              if (LiteralOpUpgrader.isConstIntZeroOrKnownNull(registerSpecList.get(0))) {
                LiteralOpUpgrader.this.replacePlainInsn(param1NormalSsaInsn, registerSpecList.withoutFirst(), RegOps.flippedIfOpcode(rop.getOpcode()), null);
                return;
              } 
              if (LiteralOpUpgrader.isConstIntZeroOrKnownNull(registerSpecList.get(1))) {
                LiteralOpUpgrader.this.replacePlainInsn(param1NormalSsaInsn, registerSpecList.withoutLast(), rop.getOpcode(), null);
                return;
              } 
            } else {
              if (advice.hasConstantOperation(rop, registerSpecList.get(0), registerSpecList.get(1))) {
                param1NormalSsaInsn.upgradeToLiteral();
                return;
              } 
              if (rop.isCommutative() && advice.hasConstantOperation(rop, registerSpecList.get(1), registerSpecList.get(0))) {
                param1NormalSsaInsn.setNewSources(RegisterSpecList.make(registerSpecList.get(1), registerSpecList.get(0)));
                param1NormalSsaInsn.upgradeToLiteral();
              } 
            } 
          }
          
          public void visitPhiInsn(PhiInsn param1PhiInsn) {}
        });
  }
  
  private boolean tryReplacingWithConstant(NormalSsaInsn paramNormalSsaInsn) {
    Rop rop = paramNormalSsaInsn.getOriginalRopInsn().getOpcode();
    RegisterSpec registerSpec = paramNormalSsaInsn.getResult();
    if (registerSpec != null && !this.ssaMeth.isRegALocal(registerSpec) && rop.getOpcode() != 5) {
      TypeBearer typeBearer = paramNormalSsaInsn.getResult().getTypeBearer();
      if (typeBearer.isConstant() && typeBearer.getBasicType() == 6) {
        replacePlainInsn(paramNormalSsaInsn, RegisterSpecList.EMPTY, 5, (Constant)typeBearer);
        if (rop.getOpcode() == 56) {
          int i = paramNormalSsaInsn.getBlock().getPredecessors().nextSetBit(0);
          ArrayList<SsaInsn> arrayList = ((SsaBasicBlock)this.ssaMeth.getBlocks().get(i)).getInsns();
          replacePlainInsn((NormalSsaInsn)arrayList.get(arrayList.size() - 1), RegisterSpecList.EMPTY, 6, null);
        } 
        return true;
      } 
    } 
    return false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\LiteralOpUpgrader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */