package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Insn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.LocalItem;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.PlainCstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.PlainInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rop;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rops;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.ThrowingCstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.TypedConstant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;

public class ConstCollector {
  private static boolean COLLECT_ONE_LOCAL = false;
  
  private static boolean COLLECT_STRINGS = false;
  
  private static final int MAX_COLLECTED_CONSTANTS = 5;
  
  private final SsaMethod ssaMeth;
  
  private ConstCollector(SsaMethod paramSsaMethod) {
    this.ssaMeth = paramSsaMethod;
  }
  
  private void fixLocalAssignment(RegisterSpec paramRegisterSpec1, RegisterSpec paramRegisterSpec2) {
    for (SsaInsn ssaInsn1 : this.ssaMeth.getUseListForRegister(paramRegisterSpec1.getReg())) {
      RegisterSpec registerSpec = ssaInsn1.getLocalAssignment();
      if (registerSpec == null || ssaInsn1.getResult() == null)
        continue; 
      LocalItem localItem = registerSpec.getLocalItem();
      ssaInsn1.setResultLocal(null);
      paramRegisterSpec2 = paramRegisterSpec2.withLocalItem(localItem);
      SsaInsn ssaInsn2 = SsaInsn.makeFromRop((Insn)new PlainInsn(Rops.opMarkLocal((TypeBearer)paramRegisterSpec2), SourcePosition.NO_INFO, null, RegisterSpecList.make(paramRegisterSpec2)), ssaInsn1.getBlock());
      ArrayList<SsaInsn> arrayList = ssaInsn1.getBlock().getInsns();
      arrayList.add(arrayList.indexOf(ssaInsn1) + 1, ssaInsn2);
    } 
  }
  
  private ArrayList<TypedConstant> getConstsSortedByCountUse() {
    int j = this.ssaMeth.getRegCount();
    final HashMap<Object, Object> countUses = new HashMap<Object, Object>();
    HashSet<TypedConstant> hashSet = new HashSet();
    for (int i = 0; i < j; i++) {
      SsaInsn ssaInsn2 = this.ssaMeth.getDefinitionForRegister(i);
      if (ssaInsn2 == null || ssaInsn2.getOpcode() == null)
        continue; 
      RegisterSpec registerSpec = ssaInsn2.getResult();
      TypeBearer typeBearer = registerSpec.getTypeBearer();
      if (!typeBearer.isConstant())
        continue; 
      TypedConstant typedConstant = (TypedConstant)typeBearer;
      SsaInsn ssaInsn1 = ssaInsn2;
      if (ssaInsn2.getOpcode().getOpcode() == 56) {
        int k = ssaInsn2.getBlock().getPredecessors().nextSetBit(0);
        ArrayList<SsaInsn> arrayList1 = ((SsaBasicBlock)this.ssaMeth.getBlocks().get(k)).getInsns();
        ssaInsn1 = arrayList1.get(arrayList1.size() - 1);
      } 
      if (ssaInsn1.canThrow() && (!(typedConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString) || !COLLECT_STRINGS || ssaInsn1.getBlock().getSuccessors().cardinality() > 1))
        continue; 
      if (this.ssaMeth.isRegALocal(registerSpec)) {
        if (!COLLECT_ONE_LOCAL || hashSet.contains(typedConstant))
          continue; 
        hashSet.add(typedConstant);
      } 
      Integer integer = (Integer)hashMap.get(typedConstant);
      if (integer == null) {
        hashMap.put(typedConstant, Integer.valueOf(1));
      } else {
        hashMap.put(typedConstant, Integer.valueOf(integer.intValue() + 1));
      } 
      continue;
    } 
    ArrayList<?> arrayList = new ArrayList();
    for (Map.Entry<Object, Object> entry : hashMap.entrySet()) {
      if (((Integer)entry.getValue()).intValue() > 1)
        arrayList.add(entry.getKey()); 
    } 
    Collections.sort(arrayList, new Comparator<Constant>() {
          public int compare(Constant param1Constant1, Constant param1Constant2) {
            int j = ((Integer)countUses.get(param1Constant2)).intValue() - ((Integer)countUses.get(param1Constant1)).intValue();
            int i = j;
            if (j == 0)
              i = param1Constant1.compareTo(param1Constant2); 
            return i;
          }
          
          public boolean equals(Object param1Object) {
            return (param1Object == this);
          }
        });
    return (ArrayList)arrayList;
  }
  
  public static void process(SsaMethod paramSsaMethod) {
    (new ConstCollector(paramSsaMethod)).run();
  }
  
  private void run() {
    int k = this.ssaMeth.getRegCount();
    ArrayList<TypedConstant> arrayList = getConstsSortedByCountUse();
    int i = Math.min(arrayList.size(), 5);
    SsaBasicBlock ssaBasicBlock = this.ssaMeth.getEntryBlock();
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>(i);
    for (int j = 0; j < i; j++) {
      TypedConstant typedConstant = arrayList.get(j);
      RegisterSpec registerSpec = RegisterSpec.make(this.ssaMeth.makeNewSsaReg(), (TypeBearer)typedConstant);
      Rop rop = Rops.opConst((TypeBearer)typedConstant);
      if (rop.getBranchingness() == 1) {
        ssaBasicBlock.addInsnToHead((Insn)new PlainCstInsn(Rops.opConst((TypeBearer)typedConstant), SourcePosition.NO_INFO, registerSpec, RegisterSpecList.EMPTY, (Constant)typedConstant));
      } else {
        SsaBasicBlock ssaBasicBlock2 = this.ssaMeth.getEntryBlock();
        SsaBasicBlock ssaBasicBlock1 = ssaBasicBlock2.getPrimarySuccessor();
        ssaBasicBlock2 = ssaBasicBlock2.insertNewSuccessor(ssaBasicBlock1);
        ssaBasicBlock2.replaceLastInsn((Insn)new ThrowingCstInsn(rop, SourcePosition.NO_INFO, RegisterSpecList.EMPTY, (TypeList)StdTypeList.EMPTY, (Constant)typedConstant));
        ssaBasicBlock2.insertNewSuccessor(ssaBasicBlock1).addInsnToHead((Insn)new PlainInsn(Rops.opMoveResultPseudo(registerSpec.getTypeBearer()), SourcePosition.NO_INFO, registerSpec, RegisterSpecList.EMPTY));
      } 
      hashMap.put(typedConstant, registerSpec);
    } 
    updateConstUses((HashMap)hashMap, k);
  }
  
  private void updateConstUses(HashMap<TypedConstant, RegisterSpec> paramHashMap, int paramInt) {
    HashSet<TypedConstant> hashSet = new HashSet();
    ArrayList[] arrayOfArrayList = (ArrayList[])this.ssaMeth.getUseListCopy();
    for (int i = 0; i < paramInt; i++) {
      SsaInsn ssaInsn = this.ssaMeth.getDefinitionForRegister(i);
      if (ssaInsn == null)
        continue; 
      final RegisterSpec origReg = ssaInsn.getResult();
      TypeBearer typeBearer = ssaInsn.getResult().getTypeBearer();
      if (!typeBearer.isConstant())
        continue; 
      TypedConstant typedConstant = (TypedConstant)typeBearer;
      final RegisterSpec newReg = paramHashMap.get(typedConstant);
      if (registerSpec2 == null)
        continue; 
      if (this.ssaMeth.isRegALocal(registerSpec1)) {
        if (!COLLECT_ONE_LOCAL || hashSet.contains(typedConstant))
          continue; 
        hashSet.add(typedConstant);
        fixLocalAssignment(registerSpec1, paramHashMap.get(typedConstant));
      } 
      RegisterMapper registerMapper = new RegisterMapper() {
          public int getNewRegisterCount() {
            return ConstCollector.this.ssaMeth.getRegCount();
          }
          
          public RegisterSpec map(RegisterSpec param1RegisterSpec) {
            RegisterSpec registerSpec = param1RegisterSpec;
            if (param1RegisterSpec.getReg() == origReg.getReg())
              registerSpec = newReg.withLocalItem(param1RegisterSpec.getLocalItem()); 
            return registerSpec;
          }
        };
      for (SsaInsn ssaInsn1 : arrayOfArrayList[registerSpec1.getReg()]) {
        if (ssaInsn1.canThrow() && ssaInsn1.getBlock().getSuccessors().cardinality() > 1)
          continue; 
        ssaInsn1.mapSourceRegisters(registerMapper);
      } 
      continue;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\ConstCollector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */