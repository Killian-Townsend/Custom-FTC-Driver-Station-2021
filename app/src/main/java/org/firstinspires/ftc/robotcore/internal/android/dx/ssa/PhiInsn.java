package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Insn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.LocalItem;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rop;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class PhiInsn extends SsaInsn {
  private final ArrayList<Operand> operands = new ArrayList<Operand>();
  
  private final int ropResultReg;
  
  private RegisterSpecList sources;
  
  public PhiInsn(int paramInt, SsaBasicBlock paramSsaBasicBlock) {
    super(RegisterSpec.make(paramInt, (TypeBearer)Type.VOID), paramSsaBasicBlock);
    this.ropResultReg = paramInt;
  }
  
  public PhiInsn(RegisterSpec paramRegisterSpec, SsaBasicBlock paramSsaBasicBlock) {
    super(paramRegisterSpec, paramSsaBasicBlock);
    this.ropResultReg = paramRegisterSpec.getReg();
  }
  
  public void accept(SsaInsn.Visitor paramVisitor) {
    paramVisitor.visitPhiInsn(this);
  }
  
  public void addPhiOperand(RegisterSpec paramRegisterSpec, SsaBasicBlock paramSsaBasicBlock) {
    this.operands.add(new Operand(paramRegisterSpec, paramSsaBasicBlock.getIndex(), paramSsaBasicBlock.getRopLabel()));
    this.sources = null;
  }
  
  public boolean areAllOperandsEqual() {
    if (this.operands.size() == 0)
      return true; 
    int i = ((Operand)this.operands.get(0)).regSpec.getReg();
    Iterator<Operand> iterator = this.operands.iterator();
    while (iterator.hasNext()) {
      if (i != ((Operand)iterator.next()).regSpec.getReg())
        return false; 
    } 
    return true;
  }
  
  public boolean canThrow() {
    return false;
  }
  
  public void changeResultType(TypeBearer paramTypeBearer, LocalItem paramLocalItem) {
    setResult(RegisterSpec.makeLocalOptional(getResult().getReg(), paramTypeBearer, paramLocalItem));
  }
  
  public PhiInsn clone() {
    throw new UnsupportedOperationException("can't clone phi");
  }
  
  public Rop getOpcode() {
    return null;
  }
  
  public Insn getOriginalRopInsn() {
    return null;
  }
  
  public int getRopResultReg() {
    return this.ropResultReg;
  }
  
  public RegisterSpecList getSources() {
    RegisterSpecList registerSpecList = this.sources;
    if (registerSpecList != null)
      return registerSpecList; 
    if (this.operands.size() == 0)
      return RegisterSpecList.EMPTY; 
    int j = this.operands.size();
    this.sources = new RegisterSpecList(j);
    for (int i = 0; i < j; i++) {
      Operand operand = this.operands.get(i);
      this.sources.set(i, operand.regSpec);
    } 
    this.sources.setImmutable();
    return this.sources;
  }
  
  public boolean hasSideEffect() {
    return (Optimizer.getPreserveLocals() && getLocalAssignment() != null);
  }
  
  public boolean isPhiOrMove() {
    return true;
  }
  
  public boolean isRegASource(int paramInt) {
    Iterator<Operand> iterator = this.operands.iterator();
    while (iterator.hasNext()) {
      if (((Operand)iterator.next()).regSpec.getReg() == paramInt)
        return true; 
    } 
    return false;
  }
  
  public final void mapSourceRegisters(RegisterMapper paramRegisterMapper) {
    for (Operand operand : this.operands) {
      RegisterSpec registerSpec = operand.regSpec;
      operand.regSpec = paramRegisterMapper.map(registerSpec);
      if (registerSpec != operand.regSpec)
        getBlock().getParent().onSourceChanged(this, registerSpec, operand.regSpec); 
    } 
    this.sources = null;
  }
  
  public int predBlockIndexForSourcesIndex(int paramInt) {
    return ((Operand)this.operands.get(paramInt)).blockIndex;
  }
  
  public List<SsaBasicBlock> predBlocksForReg(int paramInt, SsaMethod paramSsaMethod) {
    ArrayList<SsaBasicBlock> arrayList = new ArrayList();
    for (Operand operand : this.operands) {
      if (operand.regSpec.getReg() == paramInt)
        arrayList.add(paramSsaMethod.getBlocks().get(operand.blockIndex)); 
    } 
    return arrayList;
  }
  
  public void removePhiRegister(RegisterSpec paramRegisterSpec) {
    ArrayList<Operand> arrayList = new ArrayList();
    for (Operand operand : this.operands) {
      if (operand.regSpec.getReg() == paramRegisterSpec.getReg())
        arrayList.add(operand); 
    } 
    this.operands.removeAll(arrayList);
    this.sources = null;
  }
  
  public String toHuman() {
    return toHumanWithInline((String)null);
  }
  
  protected final String toHumanWithInline(String paramString) {
    StringBuffer stringBuffer = new StringBuffer(80);
    stringBuffer.append(SourcePosition.NO_INFO);
    stringBuffer.append(": phi");
    if (paramString != null) {
      stringBuffer.append("(");
      stringBuffer.append(paramString);
      stringBuffer.append(")");
    } 
    RegisterSpec registerSpec = getResult();
    if (registerSpec == null) {
      stringBuffer.append(" .");
    } else {
      stringBuffer.append(" ");
      stringBuffer.append(registerSpec.toHuman());
    } 
    stringBuffer.append(" <-");
    int i = getSources().size();
    if (i == 0) {
      stringBuffer.append(" .");
    } else {
      for (int j = 0; j < i; j++) {
        stringBuffer.append(" ");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.sources.get(j).toHuman());
        stringBuilder.append("[b=");
        stringBuilder.append(Hex.u2(((Operand)this.operands.get(j)).ropLabel));
        stringBuilder.append("]");
        stringBuffer.append(stringBuilder.toString());
      } 
    } 
    return stringBuffer.toString();
  }
  
  public Insn toRopInsn() {
    throw new IllegalArgumentException("Cannot convert phi insns to rop form");
  }
  
  public void updateSourcesToDefinitions(SsaMethod paramSsaMethod) {
    for (Operand operand : this.operands) {
      RegisterSpec registerSpec = paramSsaMethod.getDefinitionForRegister(operand.regSpec.getReg()).getResult();
      operand.regSpec = operand.regSpec.withType((TypeBearer)registerSpec.getType());
    } 
    this.sources = null;
  }
  
  private static class Operand {
    public final int blockIndex;
    
    public RegisterSpec regSpec;
    
    public final int ropLabel;
    
    public Operand(RegisterSpec param1RegisterSpec, int param1Int1, int param1Int2) {
      this.regSpec = param1RegisterSpec;
      this.blockIndex = param1Int1;
      this.ropLabel = param1Int2;
    }
  }
  
  public static interface Visitor {
    void visitPhiInsn(PhiInsn param1PhiInsn);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\PhiInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */