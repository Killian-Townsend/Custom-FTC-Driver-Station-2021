package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.DexOptions;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlockList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.FillArrayDataInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Insn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.LocalVariableInfo;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.PlainCstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.PlainInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rop;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RopMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SwitchInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.ThrowingCstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.ThrowingInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Bits;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public final class RopTranslator {
  private final BlockAddresses addresses;
  
  private final DexOptions dexOptions;
  
  private final LocalVariableInfo locals;
  
  private final RopMethod method;
  
  private int[] order;
  
  private final OutputCollector output;
  
  private final int paramSize;
  
  private boolean paramsAreInOrder;
  
  private final int positionInfo;
  
  private final int regCount;
  
  private final TranslationVisitor translationVisitor;
  
  private RopTranslator(RopMethod paramRopMethod, int paramInt1, LocalVariableInfo paramLocalVariableInfo, int paramInt2, DexOptions paramDexOptions) {
    this.dexOptions = paramDexOptions;
    this.method = paramRopMethod;
    this.positionInfo = paramInt1;
    this.locals = paramLocalVariableInfo;
    this.addresses = new BlockAddresses(paramRopMethod);
    this.paramSize = paramInt2;
    this.order = null;
    this.paramsAreInOrder = calculateParamsAreInOrder(paramRopMethod, paramInt2);
    BasicBlockList basicBlockList = paramRopMethod.getBlocks();
    int k = basicBlockList.size();
    int j = k * 3;
    int i = basicBlockList.getInstructionCount() + j;
    paramInt1 = i;
    if (paramLocalVariableInfo != null)
      paramInt1 = i + k + paramLocalVariableInfo.getAssignmentCount(); 
    k = basicBlockList.getRegCount();
    if (this.paramsAreInOrder) {
      i = 0;
    } else {
      i = this.paramSize;
    } 
    this.regCount = k + i;
    this.output = new OutputCollector(paramDexOptions, paramInt1, j, this.regCount, paramInt2);
    if (paramLocalVariableInfo != null) {
      this.translationVisitor = new LocalVariableAwareTranslationVisitor(this.output, paramLocalVariableInfo);
      return;
    } 
    this.translationVisitor = new TranslationVisitor(this.output);
  }
  
  private static boolean calculateParamsAreInOrder(RopMethod paramRopMethod, final int paramSize) {
    final boolean[] paramsAreInOrder = new boolean[1];
    arrayOfBoolean[0] = true;
    final int initialRegCount = paramRopMethod.getBlocks().getRegCount();
    paramRopMethod.getBlocks().forEachInsn((Insn.Visitor)new Insn.BaseVisitor() {
          public void visitPlainCstInsn(PlainCstInsn param1PlainCstInsn) {
            if (param1PlainCstInsn.getOpcode().getOpcode() == 3) {
              boolean bool;
              int i = ((CstInteger)param1PlainCstInsn.getConstant()).getValue();
              boolean[] arrayOfBoolean = paramsAreInOrder;
              if (arrayOfBoolean[0] && initialRegCount - paramSize + i == param1PlainCstInsn.getResult().getReg()) {
                bool = true;
              } else {
                bool = false;
              } 
              arrayOfBoolean[0] = bool;
            } 
          }
        });
    return arrayOfBoolean[0];
  }
  
  private static RegisterSpecList getRegs(Insn paramInsn) {
    return getRegs(paramInsn, paramInsn.getResult());
  }
  
  private static RegisterSpecList getRegs(Insn paramInsn, RegisterSpec paramRegisterSpec) {
    RegisterSpecList registerSpecList2 = paramInsn.getSources();
    RegisterSpecList registerSpecList1 = registerSpecList2;
    if (paramInsn.getOpcode().isCommutative()) {
      registerSpecList1 = registerSpecList2;
      if (registerSpecList2.size() == 2) {
        registerSpecList1 = registerSpecList2;
        if (paramRegisterSpec.getReg() == registerSpecList2.get(1).getReg())
          registerSpecList1 = RegisterSpecList.make(registerSpecList2.get(1), registerSpecList2.get(0)); 
      } 
    } 
    return (paramRegisterSpec == null) ? registerSpecList1 : registerSpecList1.withFirst(paramRegisterSpec);
  }
  
  private void outputBlock(BasicBlock paramBasicBlock, int paramInt) {
    CodeAddress codeAddress = this.addresses.getStart(paramBasicBlock);
    this.output.add(codeAddress);
    LocalVariableInfo localVariableInfo = this.locals;
    if (localVariableInfo != null) {
      RegisterSpecSet registerSpecSet = localVariableInfo.getStarts(paramBasicBlock);
      this.output.add(new LocalSnapshot(codeAddress.getPosition(), registerSpecSet));
    } 
    this.translationVisitor.setBlock(paramBasicBlock, this.addresses.getLast(paramBasicBlock));
    paramBasicBlock.getInsns().forEach(this.translationVisitor);
    this.output.add(this.addresses.getEnd(paramBasicBlock));
    int i = paramBasicBlock.getPrimarySuccessor();
    Insn insn = paramBasicBlock.getLastInsn();
    if (i >= 0 && i != paramInt) {
      if (insn.getOpcode().getBranchingness() == 4 && paramBasicBlock.getSecondarySuccessor() == paramInt) {
        this.output.reverseBranch(1, this.addresses.getStart(i));
        return;
      } 
      TargetInsn targetInsn = new TargetInsn(Dops.GOTO, insn.getPosition(), RegisterSpecList.EMPTY, this.addresses.getStart(i));
      this.output.add(targetInsn);
    } 
  }
  
  private void outputInstructions() {
    BasicBlockList basicBlockList = this.method.getBlocks();
    int[] arrayOfInt = this.order;
    int j = arrayOfInt.length;
    for (int i = 0; i < j; i = m) {
      int k;
      int m = i + 1;
      if (m == arrayOfInt.length) {
        k = -1;
      } else {
        k = arrayOfInt[m];
      } 
      outputBlock(basicBlockList.labelToBlock(arrayOfInt[i]), k);
    } 
  }
  
  private void pickOrder() {
    BasicBlockList basicBlockList = this.method.getBlocks();
    int m = basicBlockList.size();
    int i = basicBlockList.getMaxLabel();
    int[] arrayOfInt1 = Bits.makeBitSet(i);
    int[] arrayOfInt2 = Bits.makeBitSet(i);
    for (i = 0; i < m; i++)
      Bits.set(arrayOfInt1, basicBlockList.get(i).getLabel()); 
    int[] arrayOfInt3 = new int[m];
    int k = this.method.getFirstLabel();
    int j = 0;
    while (k != -1) {
      int n;
      label56: while (true) {
        IntList intList = this.method.labelToPredecessors(k);
        int i2 = intList.size();
        int i1 = 0;
        while (true) {
          i = k;
          n = j;
          if (i1 < i2) {
            i = intList.get(i1);
            if (Bits.get(arrayOfInt2, i)) {
              i = k;
              n = j;
              break;
            } 
            if (Bits.get(arrayOfInt1, i) && basicBlockList.labelToBlock(i).getPrimarySuccessor() == k) {
              Bits.set(arrayOfInt2, i);
              k = i;
              continue label56;
            } 
            i1++;
            continue;
          } 
          break;
        } 
        break;
      } 
      label57: while (true) {
        j = n;
        if (i != -1) {
          Bits.clear(arrayOfInt1, i);
          Bits.clear(arrayOfInt2, i);
          arrayOfInt3[n] = i;
          j = n + 1;
          BasicBlock basicBlock1 = basicBlockList.labelToBlock(i);
          BasicBlock basicBlock2 = basicBlockList.preferredSuccessorOf(basicBlock1);
          if (basicBlock2 == null)
            break; 
          i = basicBlock2.getLabel();
          k = basicBlock1.getPrimarySuccessor();
          if (Bits.get(arrayOfInt1, i)) {
            n = j;
            continue;
          } 
          if (k != i && k >= 0 && Bits.get(arrayOfInt1, k)) {
            i = k;
            n = j;
            continue;
          } 
          IntList intList = basicBlock1.getSuccessors();
          n = intList.size();
          for (i = 0; i < n; i++) {
            k = intList.get(i);
            if (Bits.get(arrayOfInt1, k)) {
              i = k;
              n = j;
              continue label57;
            } 
          } 
          i = -1;
          n = j;
          continue;
        } 
        break;
      } 
      k = Bits.findFirst(arrayOfInt1, 0);
    } 
    if (j == m) {
      this.order = arrayOfInt3;
      return;
    } 
    throw new RuntimeException("shouldn't happen");
  }
  
  public static DalvCode translate(RopMethod paramRopMethod, int paramInt1, LocalVariableInfo paramLocalVariableInfo, int paramInt2, DexOptions paramDexOptions) {
    return (new RopTranslator(paramRopMethod, paramInt1, paramLocalVariableInfo, paramInt2, paramDexOptions)).translateAndGetResult();
  }
  
  private DalvCode translateAndGetResult() {
    pickOrder();
    outputInstructions();
    StdCatchBuilder stdCatchBuilder = new StdCatchBuilder(this.method, this.order, this.addresses);
    return new DalvCode(this.positionInfo, this.output.getFinisher(), stdCatchBuilder);
  }
  
  private class LocalVariableAwareTranslationVisitor extends TranslationVisitor {
    private LocalVariableInfo locals;
    
    public LocalVariableAwareTranslationVisitor(OutputCollector param1OutputCollector, LocalVariableInfo param1LocalVariableInfo) {
      super(param1OutputCollector);
      this.locals = param1LocalVariableInfo;
    }
    
    public void addIntroductionIfNecessary(Insn param1Insn) {
      RegisterSpec registerSpec = this.locals.getAssignment(param1Insn);
      if (registerSpec != null)
        addOutput(new LocalStart(param1Insn.getPosition(), registerSpec)); 
    }
    
    public void visitPlainCstInsn(PlainCstInsn param1PlainCstInsn) {
      super.visitPlainCstInsn(param1PlainCstInsn);
      addIntroductionIfNecessary((Insn)param1PlainCstInsn);
    }
    
    public void visitPlainInsn(PlainInsn param1PlainInsn) {
      super.visitPlainInsn(param1PlainInsn);
      addIntroductionIfNecessary((Insn)param1PlainInsn);
    }
    
    public void visitSwitchInsn(SwitchInsn param1SwitchInsn) {
      super.visitSwitchInsn(param1SwitchInsn);
      addIntroductionIfNecessary((Insn)param1SwitchInsn);
    }
    
    public void visitThrowingCstInsn(ThrowingCstInsn param1ThrowingCstInsn) {
      super.visitThrowingCstInsn(param1ThrowingCstInsn);
      addIntroductionIfNecessary((Insn)param1ThrowingCstInsn);
    }
    
    public void visitThrowingInsn(ThrowingInsn param1ThrowingInsn) {
      super.visitThrowingInsn(param1ThrowingInsn);
      addIntroductionIfNecessary((Insn)param1ThrowingInsn);
    }
  }
  
  private class TranslationVisitor implements Insn.Visitor {
    private BasicBlock block;
    
    private CodeAddress lastAddress;
    
    private final OutputCollector output;
    
    public TranslationVisitor(OutputCollector param1OutputCollector) {
      this.output = param1OutputCollector;
    }
    
    private RegisterSpec getNextMoveResultPseudo() {
      int i = this.block.getPrimarySuccessor();
      if (i < 0)
        return null; 
      Insn insn = RopTranslator.this.method.getBlocks().labelToBlock(i).getInsns().get(0);
      return (insn.getOpcode().getOpcode() != 56) ? null : insn.getResult();
    }
    
    protected void addOutput(DalvInsn param1DalvInsn) {
      this.output.add(param1DalvInsn);
    }
    
    protected void addOutputSuffix(DalvInsn param1DalvInsn) {
      this.output.addSuffix(param1DalvInsn);
    }
    
    public void setBlock(BasicBlock param1BasicBlock, CodeAddress param1CodeAddress) {
      this.block = param1BasicBlock;
      this.lastAddress = param1CodeAddress;
    }
    
    public void visitFillArrayDataInsn(FillArrayDataInsn param1FillArrayDataInsn) {
      SourcePosition sourcePosition = param1FillArrayDataInsn.getPosition();
      Constant constant = param1FillArrayDataInsn.getConstant();
      ArrayList<Constant> arrayList = param1FillArrayDataInsn.getInitValues();
      if (param1FillArrayDataInsn.getOpcode().getBranchingness() == 1) {
        CodeAddress codeAddress = new CodeAddress(sourcePosition);
        ArrayData arrayData = new ArrayData(sourcePosition, this.lastAddress, arrayList, constant);
        TargetInsn targetInsn = new TargetInsn(Dops.FILL_ARRAY_DATA, sourcePosition, RopTranslator.getRegs((Insn)param1FillArrayDataInsn), codeAddress);
        addOutput(this.lastAddress);
        addOutput(targetInsn);
        addOutputSuffix(new OddSpacer(sourcePosition));
        addOutputSuffix(codeAddress);
        addOutputSuffix(arrayData);
        return;
      } 
      throw new RuntimeException("shouldn't happen");
    }
    
    public void visitPlainCstInsn(PlainCstInsn param1PlainCstInsn) {
      SourcePosition sourcePosition = param1PlainCstInsn.getPosition();
      Dop dop = RopToDop.dopFor((Insn)param1PlainCstInsn);
      Rop rop = param1PlainCstInsn.getOpcode();
      int i = rop.getOpcode();
      if (rop.getBranchingness() == 1) {
        if (i == 3) {
          if (!RopTranslator.this.paramsAreInOrder) {
            RegisterSpec registerSpec = param1PlainCstInsn.getResult();
            i = ((CstInteger)param1PlainCstInsn.getConstant()).getValue();
            addOutput(new SimpleInsn(dop, sourcePosition, RegisterSpecList.make(registerSpec, RegisterSpec.make(RopTranslator.this.regCount - RopTranslator.this.paramSize + i, (TypeBearer)registerSpec.getType()))));
            return;
          } 
        } else {
          addOutput(new CstInsn(dop, sourcePosition, RopTranslator.getRegs((Insn)param1PlainCstInsn), param1PlainCstInsn.getConstant()));
        } 
        return;
      } 
      throw new RuntimeException("shouldn't happen");
    }
    
    public void visitPlainInsn(PlainInsn param1PlainInsn) {
      TargetInsn targetInsn;
      Rop rop = param1PlainInsn.getOpcode();
      if (rop.getOpcode() == 54)
        return; 
      if (rop.getOpcode() == 56)
        return; 
      SourcePosition sourcePosition = param1PlainInsn.getPosition();
      Dop dop = RopToDop.dopFor((Insn)param1PlainInsn);
      int i = rop.getBranchingness();
      if (i != 1 && i != 2)
        if (i != 3) {
          if (i != 4) {
            if (i != 6)
              throw new RuntimeException("shouldn't happen"); 
          } else {
            i = this.block.getSuccessors().get(1);
            targetInsn = new TargetInsn(dop, sourcePosition, RopTranslator.getRegs((Insn)param1PlainInsn), RopTranslator.this.addresses.getStart(i));
            addOutput(targetInsn);
          } 
        } else {
          return;
        }  
      SimpleInsn simpleInsn = new SimpleInsn(dop, sourcePosition, RopTranslator.getRegs((Insn)targetInsn));
      addOutput(simpleInsn);
    }
    
    public void visitSwitchInsn(SwitchInsn param1SwitchInsn) {
      SourcePosition sourcePosition = param1SwitchInsn.getPosition();
      IntList intList1 = param1SwitchInsn.getCases();
      IntList intList2 = this.block.getSuccessors();
      int j = intList1.size();
      int i = intList2.size();
      int k = this.block.getPrimarySuccessor();
      if (j == i - 1 && k == intList2.get(j)) {
        Dop dop;
        CodeAddress[] arrayOfCodeAddress = new CodeAddress[j];
        for (i = 0; i < j; i++) {
          k = intList2.get(i);
          arrayOfCodeAddress[i] = RopTranslator.this.addresses.getStart(k);
        } 
        CodeAddress codeAddress1 = new CodeAddress(sourcePosition);
        CodeAddress codeAddress2 = new CodeAddress(this.lastAddress.getPosition(), true);
        SwitchData switchData = new SwitchData(sourcePosition, codeAddress2, intList1, arrayOfCodeAddress);
        if (switchData.isPacked()) {
          dop = Dops.PACKED_SWITCH;
        } else {
          dop = Dops.SPARSE_SWITCH;
        } 
        TargetInsn targetInsn = new TargetInsn(dop, sourcePosition, RopTranslator.getRegs((Insn)param1SwitchInsn), codeAddress1);
        addOutput(codeAddress2);
        addOutput(targetInsn);
        addOutputSuffix(new OddSpacer(sourcePosition));
        addOutputSuffix(codeAddress1);
        addOutputSuffix(switchData);
        return;
      } 
      throw new RuntimeException("shouldn't happen");
    }
    
    public void visitThrowingCstInsn(ThrowingCstInsn param1ThrowingCstInsn) {
      SourcePosition sourcePosition = param1ThrowingCstInsn.getPosition();
      Dop dop = RopToDop.dopFor((Insn)param1ThrowingCstInsn);
      Rop rop = param1ThrowingCstInsn.getOpcode();
      Constant constant = param1ThrowingCstInsn.getConstant();
      if (rop.getBranchingness() == 6) {
        CstInsn cstInsn;
        boolean bool1;
        addOutput(this.lastAddress);
        if (rop.isCallLike()) {
          addOutput(new CstInsn(dop, sourcePosition, param1ThrowingCstInsn.getSources(), constant));
          return;
        } 
        RegisterSpec registerSpec = getNextMoveResultPseudo();
        RegisterSpecList registerSpecList = RopTranslator.getRegs((Insn)param1ThrowingCstInsn, registerSpec);
        boolean bool = dop.hasResult();
        boolean bool2 = false;
        if (bool || rop.getOpcode() == 43) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (registerSpec != null)
          bool2 = true; 
        if (bool1 == bool2) {
          if (rop.getOpcode() == 41 && dop.getOpcode() != 35) {
            SimpleInsn simpleInsn = new SimpleInsn(dop, sourcePosition, registerSpecList);
          } else {
            cstInsn = new CstInsn(dop, sourcePosition, registerSpecList, constant);
          } 
          addOutput(cstInsn);
          return;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Insn with result/move-result-pseudo mismatch ");
        stringBuilder.append(cstInsn);
        throw new RuntimeException(stringBuilder.toString());
      } 
      throw new RuntimeException("shouldn't happen");
    }
    
    public void visitThrowingInsn(ThrowingInsn param1ThrowingInsn) {
      SourcePosition sourcePosition = param1ThrowingInsn.getPosition();
      Dop dop = RopToDop.dopFor((Insn)param1ThrowingInsn);
      if (param1ThrowingInsn.getOpcode().getBranchingness() == 6) {
        boolean bool1;
        RegisterSpec registerSpec = getNextMoveResultPseudo();
        boolean bool2 = dop.hasResult();
        if (registerSpec != null) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (bool2 == bool1) {
          addOutput(this.lastAddress);
          addOutput(new SimpleInsn(dop, sourcePosition, RopTranslator.getRegs((Insn)param1ThrowingInsn, registerSpec)));
          return;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Insn with result/move-result-pseudo mismatch");
        stringBuilder.append(param1ThrowingInsn);
        throw new RuntimeException(stringBuilder.toString());
      } 
      throw new RuntimeException("shouldn't happen");
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\RopTranslator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */