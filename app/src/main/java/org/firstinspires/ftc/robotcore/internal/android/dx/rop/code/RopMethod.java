package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public final class RopMethod {
  private final BasicBlockList blocks;
  
  private IntList exitPredecessors;
  
  private final int firstLabel;
  
  private IntList[] predecessors;
  
  public RopMethod(BasicBlockList paramBasicBlockList, int paramInt) {
    if (paramBasicBlockList != null) {
      if (paramInt >= 0) {
        this.blocks = paramBasicBlockList;
        this.firstLabel = paramInt;
        this.predecessors = null;
        this.exitPredecessors = null;
        return;
      } 
      throw new IllegalArgumentException("firstLabel < 0");
    } 
    throw new NullPointerException("blocks == null");
  }
  
  private void calcPredecessors() {
    int j;
    int k = this.blocks.getMaxLabel();
    IntList[] arrayOfIntList = new IntList[k];
    IntList intList = new IntList(10);
    int m = this.blocks.size();
    byte b = 0;
    int i = 0;
    while (true) {
      j = b;
      if (i < m) {
        BasicBlock basicBlock = this.blocks.get(i);
        int n = basicBlock.getLabel();
        IntList intList1 = basicBlock.getSuccessors();
        int i1 = intList1.size();
        if (i1 == 0) {
          intList.add(n);
        } else {
          for (j = 0; j < i1; j++) {
            int i2 = intList1.get(j);
            IntList intList3 = arrayOfIntList[i2];
            IntList intList2 = intList3;
            if (intList3 == null) {
              intList2 = new IntList(10);
              arrayOfIntList[i2] = intList2;
            } 
            intList2.add(n);
          } 
        } 
        i++;
        continue;
      } 
      break;
    } 
    while (j < k) {
      IntList intList1 = arrayOfIntList[j];
      if (intList1 != null) {
        intList1.sort();
        intList1.setImmutable();
      } 
      j++;
    } 
    intList.sort();
    intList.setImmutable();
    i = this.firstLabel;
    if (arrayOfIntList[i] == null)
      arrayOfIntList[i] = IntList.EMPTY; 
    this.predecessors = arrayOfIntList;
    this.exitPredecessors = intList;
  }
  
  public BasicBlockList getBlocks() {
    return this.blocks;
  }
  
  public IntList getExitPredecessors() {
    if (this.exitPredecessors == null)
      calcPredecessors(); 
    return this.exitPredecessors;
  }
  
  public int getFirstLabel() {
    return this.firstLabel;
  }
  
  public IntList labelToPredecessors(int paramInt) {
    if (this.exitPredecessors == null)
      calcPredecessors(); 
    IntList intList = this.predecessors[paramInt];
    if (intList != null)
      return intList; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("no such block: ");
    stringBuilder.append(Hex.u2(paramInt));
    throw new RuntimeException(stringBuilder.toString());
  }
  
  public RopMethod withRegisterOffset(int paramInt) {
    RopMethod ropMethod = new RopMethod(this.blocks.withRegisterOffset(paramInt), this.firstLabel);
    IntList intList = this.exitPredecessors;
    if (intList != null) {
      ropMethod.exitPredecessors = intList;
      ropMethod.predecessors = this.predecessors;
    } 
    return ropMethod;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\RopMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */