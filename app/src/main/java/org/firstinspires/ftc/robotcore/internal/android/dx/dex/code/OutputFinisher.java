package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dex.DexException;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.DexOptions;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.LocalItem;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMemberRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.BasicRegisterMapper;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.RegisterMapper;

public final class OutputFinisher {
  private final DexOptions dexOptions;
  
  private boolean hasAnyLocalInfo;
  
  private boolean hasAnyPositionInfo;
  
  private ArrayList<DalvInsn> insns;
  
  private final int paramSize;
  
  private int reservedCount;
  
  private int reservedParameterCount;
  
  private final int unreservedRegCount;
  
  public OutputFinisher(DexOptions paramDexOptions, int paramInt1, int paramInt2, int paramInt3) {
    this.dexOptions = paramDexOptions;
    this.unreservedRegCount = paramInt2;
    this.insns = new ArrayList<DalvInsn>(paramInt1);
    this.reservedCount = -1;
    this.hasAnyPositionInfo = false;
    this.hasAnyLocalInfo = false;
    this.paramSize = paramInt3;
  }
  
  private static void addConstants(HashSet<Constant> paramHashSet, DalvInsn paramDalvInsn) {
    RegisterSpecSet registerSpecSet;
    if (paramDalvInsn instanceof CstInsn) {
      paramHashSet.add(((CstInsn)paramDalvInsn).getConstant());
      return;
    } 
    if (paramDalvInsn instanceof LocalSnapshot) {
      registerSpecSet = ((LocalSnapshot)paramDalvInsn).getLocals();
      int j = registerSpecSet.size();
      for (int i = 0; i < j; i++)
        addConstants(paramHashSet, registerSpecSet.get(i)); 
    } else if (registerSpecSet instanceof LocalStart) {
      addConstants(paramHashSet, ((LocalStart)registerSpecSet).getLocal());
    } 
  }
  
  private static void addConstants(HashSet<Constant> paramHashSet, RegisterSpec paramRegisterSpec) {
    if (paramRegisterSpec == null)
      return; 
    LocalItem localItem = paramRegisterSpec.getLocalItem();
    CstString cstString1 = localItem.getName();
    CstString cstString2 = localItem.getSignature();
    Type type = paramRegisterSpec.getType();
    if (type != Type.KNOWN_NULL)
      paramHashSet.add(CstType.intern(type)); 
    if (cstString1 != null)
      paramHashSet.add(cstString1); 
    if (cstString2 != null)
      paramHashSet.add(cstString2); 
  }
  
  private void addReservedParameters(int paramInt) {
    shiftParameters(paramInt);
    this.reservedParameterCount += paramInt;
  }
  
  private void addReservedRegisters(int paramInt) {
    shiftAllRegisters(paramInt);
    this.reservedCount += paramInt;
  }
  
  private void align64bits(Dop[] paramArrayOfDop) {
    do {
      int i2 = this.unreservedRegCount;
      int i3 = this.reservedCount;
      int i4 = this.reservedParameterCount;
      int i5 = this.paramSize;
      Iterator<DalvInsn> iterator = this.insns.iterator();
      int i1 = 0;
      int i = i1;
      int j = i;
      int k = j;
      int m = j;
      int n = i;
      label40: while (iterator.hasNext()) {
        RegisterSpecList registerSpecList = ((DalvInsn)iterator.next()).getRegisters();
        int i8 = 0;
        int i7 = k;
        int i6 = m;
        j = n;
        i = i1;
        while (true) {
          i1 = i;
          n = j;
          m = i6;
          k = i7;
          if (i8 < registerSpecList.size()) {
            RegisterSpec registerSpec = registerSpecList.get(i8);
            k = i;
            m = j;
            n = i6;
            i1 = i7;
            if (registerSpec.isCategory2()) {
              if (registerSpec.getReg() >= i2 + i3 + i4 - i5) {
                k = 1;
              } else {
                k = 0;
              } 
              if (registerSpec.isEvenRegister()) {
                if (k) {
                  m = j + 1;
                  k = i;
                  n = i6;
                  i1 = i7;
                } else {
                  i1 = i7 + 1;
                  k = i;
                  m = j;
                  n = i6;
                } 
              } else if (k != 0) {
                k = i + 1;
                m = j;
                n = i6;
                i1 = i7;
              } else {
                n = i6 + 1;
                i1 = i7;
                m = j;
                k = i;
              } 
            } 
            i8++;
            i = k;
            j = m;
            i6 = n;
            i7 = i1;
            continue;
          } 
          continue label40;
        } 
      } 
      if (i1 > n && m > k) {
        addReservedRegisters(1);
      } else if (i1 > n) {
        addReservedParameters(1);
      } else if (m > k) {
        addReservedRegisters(1);
        if (this.paramSize != 0 && n > i1)
          addReservedParameters(1); 
      } else {
        break;
      } 
    } while (reserveRegisters(paramArrayOfDop));
  }
  
  private void assignAddresses() {
    int k = this.insns.size();
    int i = 0;
    int j = 0;
    while (i < k) {
      DalvInsn dalvInsn = this.insns.get(i);
      dalvInsn.setAddress(j);
      j += dalvInsn.codeSize();
      i++;
    } 
  }
  
  private void assignAddressesAndFixBranches() {
    do {
      assignAddresses();
    } while (fixBranches());
  }
  
  private static void assignIndices(CstInsn paramCstInsn, DalvCode.AssignIndicesCallback paramAssignIndicesCallback) {
    Constant constant = paramCstInsn.getConstant();
    int i = paramAssignIndicesCallback.getIndex(constant);
    if (i >= 0)
      paramCstInsn.setIndex(i); 
    if (constant instanceof CstMemberRef) {
      i = paramAssignIndicesCallback.getIndex((Constant)((CstMemberRef)constant).getDefiningClass());
      if (i >= 0)
        paramCstInsn.setClassIndex(i); 
    } 
  }
  
  private int calculateReservedCount(Dop[] paramArrayOfDop) {
    int k = this.insns.size();
    int i = this.reservedCount;
    int j;
    for (j = 0; j < k; j++) {
      int m;
      DalvInsn dalvInsn = this.insns.get(j);
      Dop dop1 = paramArrayOfDop[j];
      Dop dop2 = findOpcodeForInsn(dalvInsn, dop1);
      if (dop2 == null) {
        int n = dalvInsn.getMinimumRegisterRequirement(findExpandedOpcodeForInsn(dalvInsn).getFormat().compatibleRegs(dalvInsn));
        m = i;
        if (n > i)
          m = n; 
      } else {
        m = i;
        if (dop1 == dop2)
          continue; 
      } 
      paramArrayOfDop[j] = dop2;
      i = m;
      continue;
    } 
    return i;
  }
  
  private Dop findExpandedOpcodeForInsn(DalvInsn paramDalvInsn) {
    Dop dop = findOpcodeForInsn(paramDalvInsn.getLowRegVersion(), paramDalvInsn.getOpcode());
    if (dop != null)
      return dop; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No expanded opcode for ");
    stringBuilder.append(paramDalvInsn);
    throw new DexException(stringBuilder.toString());
  }
  
  private Dop findOpcodeForInsn(DalvInsn paramDalvInsn, Dop paramDop) {
    while (paramDop != null) {
      if (paramDop.getFormat().isCompatible(paramDalvInsn))
        if (this.dexOptions.forceJumbo) {
          if (paramDop.getOpcode() != 26)
            return paramDop; 
        } else {
          break;
        }  
      paramDop = Dops.getNextOrNull(paramDop, this.dexOptions);
    } 
    return paramDop;
  }
  
  private boolean fixBranches() {
    int j = this.insns.size();
    int i = 0;
    boolean bool = false;
    while (true) {
      if (i < j) {
        Dop dop1;
        DalvInsn dalvInsn = this.insns.get(i);
        if (!(dalvInsn instanceof TargetInsn))
          continue; 
        Dop dop2 = dalvInsn.getOpcode();
        TargetInsn targetInsn = (TargetInsn)dalvInsn;
        if (dop2.getFormat().branchFits(targetInsn))
          continue; 
        if (dop2.getFamily() == 40) {
          dop1 = findOpcodeForInsn(dalvInsn, dop2);
          if (dop1 != null) {
            this.insns.set(i, dalvInsn.withOpcode(dop1));
          } else {
            throw new UnsupportedOperationException("method too long");
          } 
        } else {
          try {
            ArrayList<DalvInsn> arrayList = this.insns;
            int k = i + 1;
            CodeAddress codeAddress = (CodeAddress)arrayList.get(k);
            TargetInsn targetInsn1 = new TargetInsn(Dops.GOTO, dop1.getPosition(), RegisterSpecList.EMPTY, dop1.getTarget());
            this.insns.set(i, targetInsn1);
            this.insns.add(i, dop1.withNewTargetAndReversed(codeAddress));
            j++;
            i = k;
            bool = true;
            continue;
          } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw new IllegalStateException("unpaired TargetInsn (dangling)");
          } catch (ClassCastException classCastException) {
            throw new IllegalStateException("unpaired TargetInsn");
          } 
        } 
      } else {
        return bool;
      } 
      bool = true;
      continue;
      i++;
    } 
  }
  
  private static boolean hasLocalInfo(DalvInsn paramDalvInsn) {
    RegisterSpecSet registerSpecSet;
    if (paramDalvInsn instanceof LocalSnapshot) {
      registerSpecSet = ((LocalSnapshot)paramDalvInsn).getLocals();
      int j = registerSpecSet.size();
      for (int i = 0; i < j; i++) {
        if (hasLocalInfo(registerSpecSet.get(i)))
          return true; 
      } 
    } else if (registerSpecSet instanceof LocalStart && hasLocalInfo(((LocalStart)registerSpecSet).getLocal())) {
      return true;
    } 
    return false;
  }
  
  private static boolean hasLocalInfo(RegisterSpec paramRegisterSpec) {
    return (paramRegisterSpec != null && paramRegisterSpec.getLocalItem().getName() != null);
  }
  
  private Dop[] makeOpcodesArray() {
    int j = this.insns.size();
    Dop[] arrayOfDop = new Dop[j];
    for (int i = 0; i < j; i++)
      arrayOfDop[i] = ((DalvInsn)this.insns.get(i)).getOpcode(); 
    return arrayOfDop;
  }
  
  private void massageInstructions(Dop[] paramArrayOfDop) {
    if (this.reservedCount == 0) {
      int j = this.insns.size();
      for (int i = 0; i < j; i++) {
        DalvInsn dalvInsn = this.insns.get(i);
        Dop dop1 = dalvInsn.getOpcode();
        Dop dop2 = paramArrayOfDop[i];
        if (dop1 != dop2)
          this.insns.set(i, dalvInsn.withOpcode(dop2)); 
      } 
    } else {
      this.insns = performExpansion(paramArrayOfDop);
    } 
  }
  
  private ArrayList<DalvInsn> performExpansion(Dop[] paramArrayOfDop) {
    int j = this.insns.size();
    ArrayList<DalvInsn> arrayList = new ArrayList(j * 2);
    ArrayList<CodeAddress> arrayList1 = new ArrayList();
    for (int i = 0; i < j; i++) {
      DalvInsn dalvInsn2;
      DalvInsn dalvInsn1 = this.insns.get(i);
      Dop dop2 = dalvInsn1.getOpcode();
      Dop dop1 = paramArrayOfDop[i];
      DalvInsn dalvInsn3 = null;
      if (dop1 != null) {
        dalvInsn2 = null;
      } else {
        dop1 = findExpandedOpcodeForInsn(dalvInsn1);
        BitSet bitSet = dop1.getFormat().compatibleRegs(dalvInsn1);
        dalvInsn3 = dalvInsn1.expandedPrefix(bitSet);
        dalvInsn2 = dalvInsn1.expandedSuffix(bitSet);
        dalvInsn1 = dalvInsn1.expandedVersion(bitSet);
      } 
      if (dalvInsn1 instanceof CodeAddress) {
        CodeAddress codeAddress = (CodeAddress)dalvInsn1;
        if (codeAddress.getBindsClosely()) {
          arrayList1.add(codeAddress);
          continue;
        } 
      } 
      if (dalvInsn3 != null)
        arrayList.add(dalvInsn3); 
      if (!(dalvInsn1 instanceof ZeroSizeInsn) && arrayList1.size() > 0) {
        Iterator<CodeAddress> iterator = arrayList1.iterator();
        while (iterator.hasNext())
          arrayList.add(iterator.next()); 
        arrayList1.clear();
      } 
      dalvInsn3 = dalvInsn1;
      if (dop1 != dop2)
        dalvInsn3 = dalvInsn1.withOpcode(dop1); 
      arrayList.add(dalvInsn3);
      if (dalvInsn2 != null)
        arrayList.add(dalvInsn2); 
      continue;
    } 
    return arrayList;
  }
  
  private boolean reserveRegisters(Dop[] paramArrayOfDop) {
    int j = this.reservedCount;
    int i = j;
    if (j < 0)
      i = 0; 
    boolean bool = false;
    while (true) {
      int k = calculateReservedCount(paramArrayOfDop);
      if (i >= k) {
        this.reservedCount = i;
        return bool;
      } 
      int m = this.insns.size();
      for (j = 0; j < m; j++) {
        DalvInsn dalvInsn = this.insns.get(j);
        if (!(dalvInsn instanceof CodeAddress))
          this.insns.set(j, dalvInsn.withRegisterOffset(k - i)); 
      } 
      bool = true;
      i = k;
    } 
  }
  
  private void shiftAllRegisters(int paramInt) {
    int j = this.insns.size();
    for (int i = 0; i < j; i++) {
      DalvInsn dalvInsn = this.insns.get(i);
      if (!(dalvInsn instanceof CodeAddress))
        this.insns.set(i, dalvInsn.withRegisterOffset(paramInt)); 
    } 
  }
  
  private void shiftParameters(int paramInt) {
    int j;
    int k = this.insns.size();
    int m = this.unreservedRegCount + this.reservedCount + this.reservedParameterCount;
    int n = this.paramSize;
    BasicRegisterMapper basicRegisterMapper = new BasicRegisterMapper(m);
    byte b = 0;
    int i = 0;
    while (true) {
      j = b;
      if (i < m) {
        if (i >= m - n) {
          basicRegisterMapper.addMapping(i, i + paramInt, 1);
        } else {
          basicRegisterMapper.addMapping(i, i, 1);
        } 
        i++;
        continue;
      } 
      break;
    } 
    while (j < k) {
      DalvInsn dalvInsn = this.insns.get(j);
      if (!(dalvInsn instanceof CodeAddress))
        this.insns.set(j, dalvInsn.withMapper((RegisterMapper)basicRegisterMapper)); 
      j++;
    } 
  }
  
  private void updateInfo(DalvInsn paramDalvInsn) {
    if (!this.hasAnyPositionInfo && paramDalvInsn.getPosition().getLine() >= 0)
      this.hasAnyPositionInfo = true; 
    if (!this.hasAnyLocalInfo && hasLocalInfo(paramDalvInsn))
      this.hasAnyLocalInfo = true; 
  }
  
  public void add(DalvInsn paramDalvInsn) {
    this.insns.add(paramDalvInsn);
    updateInfo(paramDalvInsn);
  }
  
  public void assignIndices(DalvCode.AssignIndicesCallback paramAssignIndicesCallback) {
    for (DalvInsn dalvInsn : this.insns) {
      if (dalvInsn instanceof CstInsn)
        assignIndices((CstInsn)dalvInsn, paramAssignIndicesCallback); 
    } 
  }
  
  public DalvInsnList finishProcessingAndGetList() {
    if (this.reservedCount < 0) {
      Dop[] arrayOfDop = makeOpcodesArray();
      reserveRegisters(arrayOfDop);
      if (this.dexOptions.ALIGN_64BIT_REGS_IN_OUTPUT_FINISHER)
        align64bits(arrayOfDop); 
      massageInstructions(arrayOfDop);
      assignAddressesAndFixBranches();
      return DalvInsnList.makeImmutable(this.insns, this.reservedCount + this.unreservedRegCount + this.reservedParameterCount);
    } 
    throw new UnsupportedOperationException("already processed");
  }
  
  public HashSet<Constant> getAllConstants() {
    HashSet<Constant> hashSet = new HashSet(20);
    Iterator<DalvInsn> iterator = this.insns.iterator();
    while (iterator.hasNext())
      addConstants(hashSet, iterator.next()); 
    return hashSet;
  }
  
  public boolean hasAnyLocalInfo() {
    return this.hasAnyLocalInfo;
  }
  
  public boolean hasAnyPositionInfo() {
    return this.hasAnyPositionInfo;
  }
  
  public void insert(int paramInt, DalvInsn paramDalvInsn) {
    this.insns.add(paramInt, paramDalvInsn);
    updateInfo(paramDalvInsn);
  }
  
  public void reverseBranch(int paramInt, CodeAddress paramCodeAddress) {
    paramInt = this.insns.size() - paramInt - 1;
    try {
      TargetInsn targetInsn = (TargetInsn)this.insns.get(paramInt);
      this.insns.set(paramInt, targetInsn.withNewTargetAndReversed(paramCodeAddress));
      return;
    } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
      throw new IllegalArgumentException("too few instructions");
    } catch (ClassCastException classCastException) {
      throw new IllegalArgumentException("non-reversible instruction");
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\OutputFinisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */