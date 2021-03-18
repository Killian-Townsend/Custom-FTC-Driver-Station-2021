package org.firstinspires.ftc.robotcore.internal.android.dx.ssa.back;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.CstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.LocalItem;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rop;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.InterferenceRegisterMapper;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.NormalSsaInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.Optimizer;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.PhiInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.RegisterMapper;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaBasicBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntIterator;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntSet;

public class FirstFitLocalCombiningAllocator extends RegisterAllocator {
  private static final boolean DEBUG = false;
  
  private final ArrayList<NormalSsaInsn> invokeRangeInsns;
  
  private final Map<LocalItem, ArrayList<RegisterSpec>> localVariables;
  
  private final InterferenceRegisterMapper mapper;
  
  private final boolean minimizeRegisters;
  
  private final ArrayList<NormalSsaInsn> moveResultPseudoInsns;
  
  private final int paramRangeEnd;
  
  private final ArrayList<PhiInsn> phiInsns;
  
  private final BitSet reservedRopRegs;
  
  private final BitSet ssaRegsMapped;
  
  private final BitSet usedRopRegs;
  
  public FirstFitLocalCombiningAllocator(SsaMethod paramSsaMethod, InterferenceGraph paramInterferenceGraph, boolean paramBoolean) {
    super(paramSsaMethod, paramInterferenceGraph);
    this.ssaRegsMapped = new BitSet(paramSsaMethod.getRegCount());
    this.mapper = new InterferenceRegisterMapper(paramInterferenceGraph, paramSsaMethod.getRegCount());
    this.minimizeRegisters = paramBoolean;
    this.paramRangeEnd = paramSsaMethod.getParamWidth();
    BitSet bitSet = new BitSet(this.paramRangeEnd * 2);
    this.reservedRopRegs = bitSet;
    bitSet.set(0, this.paramRangeEnd);
    this.usedRopRegs = new BitSet(this.paramRangeEnd * 2);
    this.localVariables = new TreeMap<LocalItem, ArrayList<RegisterSpec>>();
    this.moveResultPseudoInsns = new ArrayList<NormalSsaInsn>();
    this.invokeRangeInsns = new ArrayList<NormalSsaInsn>();
    this.phiInsns = new ArrayList<PhiInsn>();
  }
  
  private void addMapping(RegisterSpec paramRegisterSpec, int paramInt) {
    int i = paramRegisterSpec.getReg();
    if (!this.ssaRegsMapped.get(i) && canMapReg(paramRegisterSpec, paramInt)) {
      int j = paramRegisterSpec.getCategory();
      this.mapper.addMapping(paramRegisterSpec.getReg(), paramInt, j);
      this.ssaRegsMapped.set(i);
      this.usedRopRegs.set(paramInt, j + paramInt);
      return;
    } 
    throw new RuntimeException("attempt to add invalid register mapping");
  }
  
  private void adjustAndMapSourceRangeRange(NormalSsaInsn paramNormalSsaInsn) {
    int i = findRangeAndAdjust(paramNormalSsaInsn);
    RegisterSpecList registerSpecList = paramNormalSsaInsn.getSources();
    int k = registerSpecList.size();
    int j = 0;
    while (j < k) {
      RegisterSpec registerSpec = registerSpecList.get(j);
      int m = registerSpec.getReg();
      int n = registerSpec.getCategory();
      if (!this.ssaRegsMapped.get(m)) {
        LocalItem localItem = getLocalItemForReg(m);
        addMapping(registerSpec, i);
        if (localItem != null) {
          markReserved(i, n);
          ArrayList<RegisterSpec> arrayList = this.localVariables.get(localItem);
          int i1 = arrayList.size();
          for (m = 0; m < i1; m++) {
            RegisterSpec registerSpec1 = arrayList.get(m);
            if (-1 == registerSpecList.indexOfRegister(registerSpec1.getReg()))
              tryMapReg(registerSpec1, i, n); 
          } 
        } 
      } 
      j++;
      i += n;
    } 
  }
  
  private void analyzeInstructions() {
    this.ssaMeth.forEachInsn(new SsaInsn.Visitor() {
          private void processInsn(SsaInsn param1SsaInsn) {
            RegisterSpec registerSpec = param1SsaInsn.getLocalAssignment();
            if (registerSpec != null) {
              LocalItem localItem = registerSpec.getLocalItem();
              ArrayList<RegisterSpec> arrayList2 = (ArrayList)FirstFitLocalCombiningAllocator.this.localVariables.get(localItem);
              ArrayList<RegisterSpec> arrayList1 = arrayList2;
              if (arrayList2 == null) {
                arrayList1 = new ArrayList();
                FirstFitLocalCombiningAllocator.this.localVariables.put(localItem, arrayList1);
              } 
              arrayList1.add(registerSpec);
            } 
            if (param1SsaInsn instanceof NormalSsaInsn) {
              if (param1SsaInsn.getOpcode().getOpcode() == 56) {
                FirstFitLocalCombiningAllocator.this.moveResultPseudoInsns.add((NormalSsaInsn)param1SsaInsn);
                return;
              } 
              if (Optimizer.getAdvice().requiresSourcesInOrder(param1SsaInsn.getOriginalRopInsn().getOpcode(), param1SsaInsn.getSources())) {
                FirstFitLocalCombiningAllocator.this.invokeRangeInsns.add((NormalSsaInsn)param1SsaInsn);
                return;
              } 
            } else if (param1SsaInsn instanceof PhiInsn) {
              FirstFitLocalCombiningAllocator.this.phiInsns.add((PhiInsn)param1SsaInsn);
            } 
          }
          
          public void visitMoveInsn(NormalSsaInsn param1NormalSsaInsn) {
            processInsn((SsaInsn)param1NormalSsaInsn);
          }
          
          public void visitNonMoveInsn(NormalSsaInsn param1NormalSsaInsn) {
            processInsn((SsaInsn)param1NormalSsaInsn);
          }
          
          public void visitPhiInsn(PhiInsn param1PhiInsn) {
            processInsn((SsaInsn)param1PhiInsn);
          }
        });
  }
  
  private boolean canMapReg(RegisterSpec paramRegisterSpec, int paramInt) {
    return (!spansParamRange(paramInt, paramRegisterSpec.getCategory()) && !this.mapper.interferes(paramRegisterSpec, paramInt));
  }
  
  private boolean canMapRegs(ArrayList<RegisterSpec> paramArrayList, int paramInt) {
    for (RegisterSpec registerSpec : paramArrayList) {
      if (!this.ssaRegsMapped.get(registerSpec.getReg()) && !canMapReg(registerSpec, paramInt))
        return false; 
    } 
    return true;
  }
  
  private int findAnyFittingRange(NormalSsaInsn paramNormalSsaInsn, int paramInt, int[] paramArrayOfint, BitSet paramBitSet) {
    Alignment alignment = Alignment.UNSPECIFIED;
    int n = paramArrayOfint.length;
    int m = 0;
    int i = 0;
    int j = i;
    int k = j;
    while (m < n) {
      if (paramArrayOfint[m] == 2) {
        if (isEven(k)) {
          j++;
        } else {
          i++;
        } 
        k += 2;
      } else {
        k++;
      } 
      m++;
    } 
    if (i > j) {
      if (isEven(this.paramRangeEnd)) {
        alignment = Alignment.ODD;
      } else {
        alignment = Alignment.EVEN;
      } 
    } else if (j > 0) {
      if (isEven(this.paramRangeEnd)) {
        alignment = Alignment.EVEN;
      } else {
        alignment = Alignment.ODD;
      } 
    } 
    i = this.paramRangeEnd;
    while (true) {
      i = findNextUnreservedRopReg(i, paramInt, alignment);
      if (fitPlanForRange(i, paramNormalSsaInsn, paramArrayOfint, paramBitSet) >= 0)
        return i; 
      i++;
      paramBitSet.clear();
    } 
  }
  
  private int findNextUnreservedRopReg(int paramInt1, int paramInt2) {
    return findNextUnreservedRopReg(paramInt1, paramInt2, getAlignment(paramInt2));
  }
  
  private int findNextUnreservedRopReg(int paramInt1, int paramInt2, Alignment paramAlignment) {
    for (paramInt1 = paramAlignment.nextClearBit(this.reservedRopRegs, paramInt1);; paramInt1 = paramAlignment.nextClearBit(this.reservedRopRegs, paramInt1 + i)) {
      int i;
      for (i = 1; i < paramInt2 && !this.reservedRopRegs.get(paramInt1 + i); i++);
      if (i == paramInt2)
        return paramInt1; 
    } 
  }
  
  private int findRangeAndAdjust(NormalSsaInsn paramNormalSsaInsn) {
    int k;
    BitSet bitSet2;
    RegisterSpecList registerSpecList = paramNormalSsaInsn.getSources();
    int i2 = registerSpecList.size();
    int[] arrayOfInt = new int[i2];
    int i = 0;
    int m = 0;
    while (i < i2) {
      arrayOfInt[i] = registerSpecList.get(i).getCategory();
      m += arrayOfInt[i];
      i++;
    } 
    int j = Integer.MIN_VALUE;
    BitSet bitSet1 = null;
    i = -1;
    int n = 0;
    int i1 = 0;
    while (true) {
      bitSet2 = bitSet1;
      k = i;
      if (n < i2) {
        k = registerSpecList.get(n).getReg();
        int i3 = i1;
        if (n != 0)
          i3 = i1 - arrayOfInt[n - 1]; 
        if (!this.ssaRegsMapped.get(k)) {
          i1 = j;
          bitSet2 = bitSet1;
          k = i;
        } else {
          int i4 = this.mapper.oldToNew(k) + i3;
          i1 = j;
          bitSet2 = bitSet1;
          k = i;
          if (i4 >= 0)
            if (spansParamRange(i4, m)) {
              i1 = j;
              bitSet2 = bitSet1;
              k = i;
            } else {
              bitSet2 = new BitSet(i2);
              int i5 = fitPlanForRange(i4, paramNormalSsaInsn, arrayOfInt, bitSet2);
              if (i5 < 0) {
                i1 = j;
                bitSet2 = bitSet1;
                k = i;
              } else {
                i1 = i5 - bitSet2.cardinality();
                k = j;
                if (i1 > j) {
                  k = i1;
                  i = i4;
                  bitSet1 = bitSet2;
                } 
                i1 = k;
                bitSet2 = bitSet1;
                k = i;
                if (i5 == m) {
                  bitSet2 = bitSet1;
                  k = i;
                  break;
                } 
              } 
            }  
        } 
        n++;
        j = i1;
        bitSet1 = bitSet2;
        i1 = i3;
        i = k;
        continue;
      } 
      break;
    } 
    i = k;
    if (k == -1) {
      bitSet2 = new BitSet(i2);
      i = findAnyFittingRange(paramNormalSsaInsn, m, arrayOfInt, bitSet2);
    } 
    for (j = bitSet2.nextSetBit(0); j >= 0; j = bitSet2.nextSetBit(j + 1))
      paramNormalSsaInsn.changeOneSource(j, insertMoveBefore((SsaInsn)paramNormalSsaInsn, registerSpecList.get(j))); 
    return i;
  }
  
  private int findRopRegForLocal(int paramInt1, int paramInt2) {
    Alignment alignment = getAlignment(paramInt2);
    for (paramInt1 = alignment.nextClearBit(this.usedRopRegs, paramInt1);; paramInt1 = alignment.nextClearBit(this.usedRopRegs, paramInt1 + i)) {
      int i;
      for (i = 1; i < paramInt2 && !this.usedRopRegs.get(paramInt1 + i); i++);
      if (i == paramInt2)
        return paramInt1; 
    } 
  }
  
  private int fitPlanForRange(int paramInt, NormalSsaInsn paramNormalSsaInsn, int[] paramArrayOfint, BitSet paramBitSet) {
    RegisterSpecList registerSpecList2 = paramNormalSsaInsn.getSources();
    int m = registerSpecList2.size();
    RegisterSpecList registerSpecList1 = ssaSetToSpecs(paramNormalSsaInsn.getBlock().getLiveOutRegs());
    BitSet bitSet = new BitSet(this.ssaMeth.getRegCount());
    int i = 0;
    int j = 0;
    int k = paramInt;
    paramInt = i;
    while (paramInt < m) {
      RegisterSpec registerSpec = registerSpecList2.get(paramInt);
      int n = registerSpec.getReg();
      int i1 = paramArrayOfint[paramInt];
      i = k;
      if (paramInt != 0)
        i = k + paramArrayOfint[paramInt - 1]; 
      if (!this.ssaRegsMapped.get(n) || this.mapper.oldToNew(n) != i) {
        if (rangeContainsReserved(i, i1))
          return -1; 
        if (this.ssaRegsMapped.get(n) || !canMapReg(registerSpec, i) || bitSet.get(n)) {
          if (!this.mapper.areAnyPinned(registerSpecList1, i, i1) && !this.mapper.areAnyPinned(registerSpecList2, i, i1)) {
            paramBitSet.set(paramInt);
          } else {
            return -1;
          } 
          continue;
        } 
      } 
      j += i1;
      continue;
      bitSet.set(SYNTHETIC_LOCAL_VARIABLE_9);
      paramInt++;
      k = i;
    } 
    return j;
  }
  
  private Alignment getAlignment(int paramInt) {
    Alignment alignment = Alignment.UNSPECIFIED;
    if (paramInt == 2) {
      if (isEven(this.paramRangeEnd))
        return Alignment.EVEN; 
      alignment = Alignment.ODD;
    } 
    return alignment;
  }
  
  private LocalItem getLocalItemForReg(int paramInt) {
    for (Map.Entry<LocalItem, ArrayList<RegisterSpec>> entry : this.localVariables.entrySet()) {
      Iterator<RegisterSpec> iterator = ((ArrayList)entry.getValue()).iterator();
      while (iterator.hasNext()) {
        if (((RegisterSpec)iterator.next()).getReg() == paramInt)
          return (LocalItem)entry.getKey(); 
      } 
    } 
    return null;
  }
  
  private int getParameterIndexForReg(int paramInt) {
    SsaInsn ssaInsn = this.ssaMeth.getDefinitionForRegister(paramInt);
    if (ssaInsn == null)
      return -1; 
    Rop rop = ssaInsn.getOpcode();
    return (rop != null && rop.getOpcode() == 3) ? ((CstInteger)((CstInsn)ssaInsn.getOriginalRopInsn()).getConstant()).getValue() : -1;
  }
  
  private void handleCheckCastResults() {
    for (NormalSsaInsn normalSsaInsn : this.moveResultPseudoInsns) {
      RegisterSpec registerSpec1 = normalSsaInsn.getResult();
      int k = registerSpec1.getReg();
      BitSet bitSet = normalSsaInsn.getBlock().getPredecessors();
      int i = bitSet.cardinality();
      int j = 1;
      if (i != 1)
        continue; 
      ArrayList<SsaInsn> arrayList = ((SsaBasicBlock)this.ssaMeth.getBlocks().get(bitSet.nextSetBit(0))).getInsns();
      SsaInsn ssaInsn = arrayList.get(arrayList.size() - 1);
      if (ssaInsn.getOpcode().getOpcode() != 43)
        continue; 
      RegisterSpec registerSpec2 = ssaInsn.getSources().get(0);
      int m = registerSpec2.getReg();
      int n = registerSpec2.getCategory();
      boolean bool3 = this.ssaRegsMapped.get(k);
      boolean bool2 = this.ssaRegsMapped.get(m);
      boolean bool1 = bool2;
      if (((bool2 ^ true) & bool3) != 0)
        bool1 = tryMapReg(registerSpec2, this.mapper.oldToNew(k), n); 
      bool2 = bool3;
      if (((bool3 ^ true) & bool1) != 0)
        bool2 = tryMapReg(registerSpec1, this.mapper.oldToNew(m), n); 
      if (!bool2 || !bool1) {
        i = findNextUnreservedRopReg(this.paramRangeEnd, n);
        ArrayList<RegisterSpec> arrayList1 = new ArrayList(2);
        arrayList1.add(registerSpec1);
        arrayList1.add(registerSpec2);
        while (!tryMapRegs(arrayList1, i, n, false))
          i = findNextUnreservedRopReg(i + 1, n); 
      } 
      if (ssaInsn.getOriginalRopInsn().getCatches().size() != 0) {
        i = j;
      } else {
        i = 0;
      } 
      j = this.mapper.oldToNew(k);
      if (j != this.mapper.oldToNew(m) && i == 0) {
        ((NormalSsaInsn)ssaInsn).changeOneSource(0, insertMoveBefore(ssaInsn, registerSpec2));
        addMapping(ssaInsn.getSources().get(0), j);
      } 
    } 
  }
  
  private void handleInvokeRangeInsns() {
    Iterator<NormalSsaInsn> iterator = this.invokeRangeInsns.iterator();
    while (iterator.hasNext())
      adjustAndMapSourceRangeRange(iterator.next()); 
  }
  
  private void handleLocalAssociatedOther() {
    label23: for (ArrayList<RegisterSpec> arrayList : this.localVariables.values()) {
      int i = this.paramRangeEnd;
      boolean bool = false;
      while (true) {
        int m = arrayList.size();
        int k = 0;
        int j;
        for (j = 1; k < m; j = n) {
          RegisterSpec registerSpec = arrayList.get(k);
          int i1 = registerSpec.getCategory();
          int n = j;
          if (!this.ssaRegsMapped.get(registerSpec.getReg())) {
            n = j;
            if (i1 > j)
              n = i1; 
          } 
          k++;
        } 
        i = findRopRegForLocal(i, j);
        boolean bool1 = bool;
        if (canMapRegs(arrayList, i))
          bool1 = tryMapRegs(arrayList, i, j, true); 
        i++;
        bool = bool1;
        if (bool1)
          continue label23; 
      } 
    } 
  }
  
  private void handleLocalAssociatedParams() {
    for (ArrayList<RegisterSpec> arrayList : this.localVariables.values()) {
      int k;
      int m = arrayList.size();
      int i = -1;
      byte b = 0;
      int j = 0;
      while (true) {
        k = b;
        if (j < m) {
          RegisterSpec registerSpec = arrayList.get(j);
          i = getParameterIndexForReg(registerSpec.getReg());
          if (i >= 0) {
            k = registerSpec.getCategory();
            addMapping(registerSpec, i);
            break;
          } 
          j++;
          continue;
        } 
        break;
      } 
      if (i < 0)
        continue; 
      tryMapRegs(arrayList, i, k, true);
    } 
  }
  
  private void handleNormalUnassociated() {
    int j = this.ssaMeth.getRegCount();
    for (int i = 0; i < j; i++) {
      if (!this.ssaRegsMapped.get(i)) {
        RegisterSpec registerSpec = getDefinitionSpecForSsaReg(i);
        if (registerSpec != null) {
          int m = registerSpec.getCategory();
          int k;
          for (k = findNextUnreservedRopReg(this.paramRangeEnd, m); !canMapReg(registerSpec, k); k = findNextUnreservedRopReg(k + 1, m));
          addMapping(registerSpec, k);
        } 
      } 
    } 
  }
  
  private void handlePhiInsns() {
    Iterator<PhiInsn> iterator = this.phiInsns.iterator();
    while (iterator.hasNext())
      processPhiInsn(iterator.next()); 
  }
  
  private void handleUnassociatedParameters() {
    int j = this.ssaMeth.getRegCount();
    for (int i = 0; i < j; i++) {
      if (!this.ssaRegsMapped.get(i)) {
        int k = getParameterIndexForReg(i);
        RegisterSpec registerSpec = getDefinitionSpecForSsaReg(i);
        if (k >= 0)
          addMapping(registerSpec, k); 
      } 
    } 
  }
  
  private static boolean isEven(int paramInt) {
    return ((paramInt & 0x1) == 0);
  }
  
  private boolean isThisPointerReg(int paramInt) {
    return (paramInt == 0 && !this.ssaMeth.isStatic());
  }
  
  private void markReserved(int paramInt1, int paramInt2) {
    this.reservedRopRegs.set(paramInt1, paramInt2 + paramInt1, true);
  }
  
  private void printLocalVars() {
    System.out.println("Printing local vars");
    for (Map.Entry<LocalItem, ArrayList<RegisterSpec>> entry : this.localVariables.entrySet()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append('{');
      stringBuilder.append(' ');
      for (RegisterSpec registerSpec : entry.getValue()) {
        stringBuilder.append('v');
        stringBuilder.append(registerSpec.getReg());
        stringBuilder.append(' ');
      } 
      stringBuilder.append('}');
      System.out.printf("Local: %s Registers: %s\n", new Object[] { entry.getKey(), stringBuilder });
    } 
  }
  
  private void processPhiInsn(PhiInsn paramPhiInsn) {
    RegisterSpec registerSpec = paramPhiInsn.getResult();
    int i = registerSpec.getReg();
    int j = registerSpec.getCategory();
    RegisterSpecList registerSpecList = paramPhiInsn.getSources();
    int k = registerSpecList.size();
    ArrayList<RegisterSpec> arrayList = new ArrayList();
    Multiset multiset = new Multiset(k + 1);
    if (this.ssaRegsMapped.get(i)) {
      multiset.add(this.mapper.oldToNew(i));
    } else {
      arrayList.add(registerSpec);
    } 
    for (i = 0; i < k; i++) {
      registerSpec = registerSpecList.get(i);
      registerSpec = this.ssaMeth.getDefinitionForRegister(registerSpec.getReg()).getResult();
      int m = registerSpec.getReg();
      if (this.ssaRegsMapped.get(m)) {
        multiset.add(this.mapper.oldToNew(m));
      } else {
        arrayList.add(registerSpec);
      } 
    } 
    for (i = 0; i < multiset.getSize(); i++)
      tryMapRegs(arrayList, multiset.getAndRemoveHighestCount(), j, false); 
    for (i = findNextUnreservedRopReg(this.paramRangeEnd, j); !tryMapRegs(arrayList, i, j, false); i = findNextUnreservedRopReg(i + 1, j));
  }
  
  private boolean rangeContainsReserved(int paramInt1, int paramInt2) {
    for (int i = paramInt1; i < paramInt1 + paramInt2; i++) {
      if (this.reservedRopRegs.get(i))
        return true; 
    } 
    return false;
  }
  
  private boolean spansParamRange(int paramInt1, int paramInt2) {
    int i = this.paramRangeEnd;
    return (paramInt1 < i && paramInt1 + paramInt2 > i);
  }
  
  private boolean tryMapReg(RegisterSpec paramRegisterSpec, int paramInt1, int paramInt2) {
    if (paramRegisterSpec.getCategory() <= paramInt2 && !this.ssaRegsMapped.get(paramRegisterSpec.getReg()) && canMapReg(paramRegisterSpec, paramInt1)) {
      addMapping(paramRegisterSpec, paramInt1);
      return true;
    } 
    return false;
  }
  
  private boolean tryMapRegs(ArrayList<RegisterSpec> paramArrayList, int paramInt1, int paramInt2, boolean paramBoolean) {
    Iterator<RegisterSpec> iterator = paramArrayList.iterator();
    boolean bool = false;
    while (iterator.hasNext()) {
      boolean bool1;
      RegisterSpec registerSpec = iterator.next();
      if (this.ssaRegsMapped.get(registerSpec.getReg()))
        continue; 
      boolean bool2 = tryMapReg(registerSpec, paramInt1, paramInt2);
      if (!bool2 || bool) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      bool = bool1;
      if (bool2) {
        bool = bool1;
        if (paramBoolean) {
          markReserved(paramInt1, registerSpec.getCategory());
          bool = bool1;
        } 
      } 
    } 
    return bool ^ true;
  }
  
  public RegisterMapper allocateRegisters() {
    analyzeInstructions();
    handleLocalAssociatedParams();
    handleUnassociatedParameters();
    handleInvokeRangeInsns();
    handleLocalAssociatedOther();
    handleCheckCastResults();
    handlePhiInsns();
    handleNormalUnassociated();
    return (RegisterMapper)this.mapper;
  }
  
  RegisterSpecList ssaSetToSpecs(IntSet paramIntSet) {
    RegisterSpecList registerSpecList = new RegisterSpecList(paramIntSet.elements());
    IntIterator intIterator = paramIntSet.iterator();
    for (int i = 0; intIterator.hasNext(); i++)
      registerSpecList.set(i, getDefinitionSpecForSsaReg(intIterator.next())); 
    return registerSpecList;
  }
  
  public boolean wantsParamsMovedHigh() {
    return true;
  }
  
  private enum Alignment {
    EVEN {
      int nextClearBit(BitSet param2BitSet, int param2Int) {
        for (param2Int = param2BitSet.nextClearBit(param2Int); !FirstFitLocalCombiningAllocator.isEven(param2Int); param2Int = param2BitSet.nextClearBit(param2Int + 1));
        return param2Int;
      }
    },
    ODD {
      int nextClearBit(BitSet param2BitSet, int param2Int) {
        for (param2Int = param2BitSet.nextClearBit(param2Int); FirstFitLocalCombiningAllocator.isEven(param2Int); param2Int = param2BitSet.nextClearBit(param2Int + 1));
        return param2Int;
      }
    },
    UNSPECIFIED;
    
    static {
      null  = new null("UNSPECIFIED", 2);
      UNSPECIFIED = ;
      $VALUES = new Alignment[] { EVEN, ODD,  };
    }
    
    abstract int nextClearBit(BitSet param1BitSet, int param1Int);
  }
  
  enum null {
    int nextClearBit(BitSet param1BitSet, int param1Int) {
      for (param1Int = param1BitSet.nextClearBit(param1Int); !FirstFitLocalCombiningAllocator.isEven(param1Int); param1Int = param1BitSet.nextClearBit(param1Int + 1));
      return param1Int;
    }
  }
  
  enum null {
    int nextClearBit(BitSet param1BitSet, int param1Int) {
      for (param1Int = param1BitSet.nextClearBit(param1Int); FirstFitLocalCombiningAllocator.isEven(param1Int); param1Int = param1BitSet.nextClearBit(param1Int + 1));
      return param1Int;
    }
  }
  
  enum null {
    int nextClearBit(BitSet param1BitSet, int param1Int) {
      return param1BitSet.nextClearBit(param1Int);
    }
  }
  
  private static class Multiset {
    private final int[] count;
    
    private final int[] reg;
    
    private int size;
    
    public Multiset(int param1Int) {
      this.reg = new int[param1Int];
      this.count = new int[param1Int];
      this.size = 0;
    }
    
    public void add(int param1Int) {
      int i = 0;
      while (true) {
        int j = this.size;
        if (i < j) {
          if (this.reg[i] == param1Int) {
            int[] arrayOfInt = this.count;
            arrayOfInt[i] = arrayOfInt[i] + 1;
            return;
          } 
          i++;
          continue;
        } 
        this.reg[j] = param1Int;
        this.count[j] = 1;
        this.size = j + 1;
        return;
      } 
    }
    
    public int getAndRemoveHighestCount() {
      int m = -1;
      int k = -1;
      int i = 0;
      for (int j = i; i < this.size; j = n) {
        int[] arrayOfInt = this.count;
        int n = j;
        if (j < arrayOfInt[i]) {
          k = this.reg[i];
          n = arrayOfInt[i];
          m = i;
        } 
        i++;
      } 
      this.count[m] = 0;
      return k;
    }
    
    public int getSize() {
      return this.size;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\back\FirstFitLocalCombiningAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */