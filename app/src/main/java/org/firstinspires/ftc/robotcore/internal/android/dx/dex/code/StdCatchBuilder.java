package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import java.util.ArrayList;
import java.util.HashSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlockList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RopMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public final class StdCatchBuilder implements CatchBuilder {
  private static final int MAX_CATCH_RANGE = 65535;
  
  private final BlockAddresses addresses;
  
  private final RopMethod method;
  
  private final int[] order;
  
  public StdCatchBuilder(RopMethod paramRopMethod, int[] paramArrayOfint, BlockAddresses paramBlockAddresses) {
    if (paramRopMethod != null) {
      if (paramArrayOfint != null) {
        if (paramBlockAddresses != null) {
          this.method = paramRopMethod;
          this.order = paramArrayOfint;
          this.addresses = paramBlockAddresses;
          return;
        } 
        throw new NullPointerException("addresses == null");
      } 
      throw new NullPointerException("order == null");
    } 
    throw new NullPointerException("method == null");
  }
  
  public static CatchTable build(RopMethod paramRopMethod, int[] paramArrayOfint, BlockAddresses paramBlockAddresses) {
    BasicBlock basicBlock1;
    int j = paramArrayOfint.length;
    BasicBlockList basicBlockList = paramRopMethod.getBlocks();
    ArrayList<CatchTable.Entry> arrayList = new ArrayList(j);
    CatchHandlerList catchHandlerList = CatchHandlerList.EMPTY;
    boolean bool = false;
    BasicBlock basicBlock2 = null;
    int i = 0;
    paramRopMethod = null;
    while (true) {
      BasicBlock basicBlock;
      CatchHandlerList catchHandlerList1;
      if (i < j) {
        basicBlock = basicBlockList.labelToBlock(paramArrayOfint[i]);
        if (!basicBlock.canThrow())
          continue; 
        catchHandlerList1 = handlersFor(basicBlock, paramBlockAddresses);
        if (catchHandlerList.size() != 0) {
          BasicBlock basicBlock3;
          if (catchHandlerList.equals(catchHandlerList1) && rangeIsValid(basicBlock2, basicBlock, paramBlockAddresses)) {
            basicBlock3 = basicBlock;
          } else {
            if (catchHandlerList.size() != 0)
              arrayList.add(makeEntry(basicBlock2, basicBlock3, catchHandlerList, paramBlockAddresses)); 
            basicBlock2 = basicBlock;
            basicBlock3 = basicBlock2;
            catchHandlerList = catchHandlerList1;
          } 
          continue;
        } 
      } else {
        break;
      } 
      basicBlock2 = basicBlock;
      basicBlock1 = basicBlock2;
      catchHandlerList = catchHandlerList1;
      i++;
    } 
    if (catchHandlerList.size() != 0)
      arrayList.add(makeEntry(basicBlock2, basicBlock1, catchHandlerList, paramBlockAddresses)); 
    j = arrayList.size();
    if (j == 0)
      return CatchTable.EMPTY; 
    CatchTable catchTable = new CatchTable(j);
    for (i = bool; i < j; i++)
      catchTable.set(i, arrayList.get(i)); 
    catchTable.setImmutable();
    return catchTable;
  }
  
  private static CatchHandlerList handlersFor(BasicBlock paramBasicBlock, BlockAddresses paramBlockAddresses) {
    IntList intList = paramBasicBlock.getSuccessors();
    int i = intList.size();
    int j = paramBasicBlock.getPrimarySuccessor();
    TypeList typeList = paramBasicBlock.getLastInsn().getCatches();
    int k = typeList.size();
    if (k == 0)
      return CatchHandlerList.EMPTY; 
    if ((j != -1 || i == k) && (j == -1 || (i == k + 1 && j == intList.get(k)))) {
      boolean bool = false;
      j = 0;
      while (true) {
        i = k;
        if (j < k) {
          if (typeList.getType(j).equals(Type.OBJECT)) {
            i = j + 1;
            break;
          } 
          j++;
          continue;
        } 
        break;
      } 
      CatchHandlerList catchHandlerList = new CatchHandlerList(i);
      for (j = bool; j < i; j++)
        catchHandlerList.set(j, new CstType(typeList.getType(j)), paramBlockAddresses.getStart(intList.get(j)).getAddress()); 
      catchHandlerList.setImmutable();
      return catchHandlerList;
    } 
    throw new RuntimeException("shouldn't happen: weird successors list");
  }
  
  private static CatchTable.Entry makeEntry(BasicBlock paramBasicBlock1, BasicBlock paramBasicBlock2, CatchHandlerList paramCatchHandlerList, BlockAddresses paramBlockAddresses) {
    CodeAddress codeAddress1 = paramBlockAddresses.getLast(paramBasicBlock1);
    CodeAddress codeAddress2 = paramBlockAddresses.getEnd(paramBasicBlock2);
    return new CatchTable.Entry(codeAddress1.getAddress(), codeAddress2.getAddress(), paramCatchHandlerList);
  }
  
  private static boolean rangeIsValid(BasicBlock paramBasicBlock1, BasicBlock paramBasicBlock2, BlockAddresses paramBlockAddresses) {
    if (paramBasicBlock1 != null) {
      if (paramBasicBlock2 != null) {
        int i = paramBlockAddresses.getLast(paramBasicBlock1).getAddress();
        return (paramBlockAddresses.getEnd(paramBasicBlock2).getAddress() - i <= 65535);
      } 
      throw new NullPointerException("end == null");
    } 
    throw new NullPointerException("start == null");
  }
  
  public CatchTable build() {
    return build(this.method, this.order, this.addresses);
  }
  
  public HashSet<Type> getCatchTypes() {
    HashSet<Type> hashSet = new HashSet(20);
    BasicBlockList basicBlockList = this.method.getBlocks();
    int j = basicBlockList.size();
    for (int i = 0; i < j; i++) {
      TypeList typeList = basicBlockList.get(i).getLastInsn().getCatches();
      int m = typeList.size();
      for (int k = 0; k < m; k++)
        hashSet.add(typeList.getType(k)); 
    } 
    return hashSet;
  }
  
  public boolean hasAnyCatches() {
    BasicBlockList basicBlockList = this.method.getBlocks();
    int j = basicBlockList.size();
    for (int i = 0; i < j; i++) {
      if (basicBlockList.get(i).getLastInsn().getCatches().size() != 0)
        return true; 
    } 
    return false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\StdCatchBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */