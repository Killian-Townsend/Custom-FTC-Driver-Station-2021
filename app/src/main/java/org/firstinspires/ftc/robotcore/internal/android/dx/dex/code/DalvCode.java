package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import java.util.HashSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;

public final class DalvCode {
  private CatchTable catches;
  
  private DalvInsnList insns;
  
  private LocalList locals;
  
  private final int positionInfo;
  
  private PositionList positions;
  
  private CatchBuilder unprocessedCatches;
  
  private OutputFinisher unprocessedInsns;
  
  public DalvCode(int paramInt, OutputFinisher paramOutputFinisher, CatchBuilder paramCatchBuilder) {
    if (paramOutputFinisher != null) {
      if (paramCatchBuilder != null) {
        this.positionInfo = paramInt;
        this.unprocessedInsns = paramOutputFinisher;
        this.unprocessedCatches = paramCatchBuilder;
        this.catches = null;
        this.positions = null;
        this.locals = null;
        this.insns = null;
        return;
      } 
      throw new NullPointerException("unprocessedCatches == null");
    } 
    throw new NullPointerException("unprocessedInsns == null");
  }
  
  private void finishProcessingIfNecessary() {
    if (this.insns != null)
      return; 
    DalvInsnList dalvInsnList = this.unprocessedInsns.finishProcessingAndGetList();
    this.insns = dalvInsnList;
    this.positions = PositionList.make(dalvInsnList, this.positionInfo);
    this.locals = LocalList.make(this.insns);
    this.catches = this.unprocessedCatches.build();
    this.unprocessedInsns = null;
    this.unprocessedCatches = null;
  }
  
  public void assignIndices(AssignIndicesCallback paramAssignIndicesCallback) {
    this.unprocessedInsns.assignIndices(paramAssignIndicesCallback);
  }
  
  public HashSet<Type> getCatchTypes() {
    return this.unprocessedCatches.getCatchTypes();
  }
  
  public CatchTable getCatches() {
    finishProcessingIfNecessary();
    return this.catches;
  }
  
  public HashSet<Constant> getInsnConstants() {
    return this.unprocessedInsns.getAllConstants();
  }
  
  public DalvInsnList getInsns() {
    finishProcessingIfNecessary();
    return this.insns;
  }
  
  public LocalList getLocals() {
    finishProcessingIfNecessary();
    return this.locals;
  }
  
  public PositionList getPositions() {
    finishProcessingIfNecessary();
    return this.positions;
  }
  
  public boolean hasAnyCatches() {
    return this.unprocessedCatches.hasAnyCatches();
  }
  
  public boolean hasLocals() {
    return this.unprocessedInsns.hasAnyLocalInfo();
  }
  
  public boolean hasPositions() {
    return (this.positionInfo != 1 && this.unprocessedInsns.hasAnyPositionInfo());
  }
  
  public static interface AssignIndicesCallback {
    int getIndex(Constant param1Constant);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\DalvCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */