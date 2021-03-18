package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlockList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Insn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RopMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;

public final class BlockAddresses {
  private final CodeAddress[] ends;
  
  private final CodeAddress[] lasts;
  
  private final CodeAddress[] starts;
  
  public BlockAddresses(RopMethod paramRopMethod) {
    int i = paramRopMethod.getBlocks().getMaxLabel();
    this.starts = new CodeAddress[i];
    this.lasts = new CodeAddress[i];
    this.ends = new CodeAddress[i];
    setupArrays(paramRopMethod);
  }
  
  private void setupArrays(RopMethod paramRopMethod) {
    BasicBlockList basicBlockList = paramRopMethod.getBlocks();
    int j = basicBlockList.size();
    for (int i = 0; i < j; i++) {
      BasicBlock basicBlock = basicBlockList.get(i);
      int k = basicBlock.getLabel();
      Insn insn = basicBlock.getInsns().get(0);
      this.starts[k] = new CodeAddress(insn.getPosition());
      SourcePosition sourcePosition = basicBlock.getLastInsn().getPosition();
      this.lasts[k] = new CodeAddress(sourcePosition);
      this.ends[k] = new CodeAddress(sourcePosition);
    } 
  }
  
  public CodeAddress getEnd(int paramInt) {
    return this.ends[paramInt];
  }
  
  public CodeAddress getEnd(BasicBlock paramBasicBlock) {
    return this.ends[paramBasicBlock.getLabel()];
  }
  
  public CodeAddress getLast(int paramInt) {
    return this.lasts[paramInt];
  }
  
  public CodeAddress getLast(BasicBlock paramBasicBlock) {
    return this.lasts[paramBasicBlock.getLabel()];
  }
  
  public CodeAddress getStart(int paramInt) {
    return this.starts[paramInt];
  }
  
  public CodeAddress getStart(BasicBlock paramBasicBlock) {
    return this.starts[paramBasicBlock.getLabel()];
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\BlockAddresses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */