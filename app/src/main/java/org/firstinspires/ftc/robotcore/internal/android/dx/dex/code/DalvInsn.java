package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import java.util.BitSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.RegisterMapper;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.TwoColumnOutput;

public abstract class DalvInsn {
  private int address;
  
  private final Dop opcode;
  
  private final SourcePosition position;
  
  private final RegisterSpecList registers;
  
  public DalvInsn(Dop paramDop, SourcePosition paramSourcePosition, RegisterSpecList paramRegisterSpecList) {
    if (paramDop != null) {
      if (paramSourcePosition != null) {
        if (paramRegisterSpecList != null) {
          this.address = -1;
          this.opcode = paramDop;
          this.position = paramSourcePosition;
          this.registers = paramRegisterSpecList;
          return;
        } 
        throw new NullPointerException("registers == null");
      } 
      throw new NullPointerException("position == null");
    } 
    throw new NullPointerException("opcode == null");
  }
  
  public static SimpleInsn makeMove(SourcePosition paramSourcePosition, RegisterSpec paramRegisterSpec1, RegisterSpec paramRegisterSpec2) {
    Dop dop;
    int i = paramRegisterSpec1.getCategory();
    boolean bool = true;
    if (i != 1)
      bool = false; 
    boolean bool1 = paramRegisterSpec1.getType().isReference();
    i = paramRegisterSpec1.getReg();
    if ((paramRegisterSpec2.getReg() | i) < 16) {
      if (bool1) {
        dop = Dops.MOVE_OBJECT;
      } else if (bool) {
        dop = Dops.MOVE;
      } else {
        dop = Dops.MOVE_WIDE;
      } 
    } else if (i < 256) {
      if (bool1) {
        dop = Dops.MOVE_OBJECT_FROM16;
      } else if (bool) {
        dop = Dops.MOVE_FROM16;
      } else {
        dop = Dops.MOVE_WIDE_FROM16;
      } 
    } else if (bool1) {
      dop = Dops.MOVE_OBJECT_16;
    } else if (bool) {
      dop = Dops.MOVE_16;
    } else {
      dop = Dops.MOVE_WIDE_16;
    } 
    return new SimpleInsn(dop, paramSourcePosition, RegisterSpecList.make(paramRegisterSpec1, paramRegisterSpec2));
  }
  
  protected abstract String argString();
  
  public abstract int codeSize();
  
  public DalvInsn expandedPrefix(BitSet paramBitSet) {
    RegisterSpecList registerSpecList = this.registers;
    boolean bool = paramBitSet.get(0);
    if (hasResult())
      paramBitSet.set(0); 
    registerSpecList = registerSpecList.subset(paramBitSet);
    if (hasResult())
      paramBitSet.set(0, bool); 
    return (registerSpecList.size() == 0) ? null : new HighRegisterPrefix(this.position, registerSpecList);
  }
  
  public DalvInsn expandedSuffix(BitSet paramBitSet) {
    if (hasResult() && !paramBitSet.get(0)) {
      RegisterSpec registerSpec = this.registers.get(0);
      return makeMove(this.position, registerSpec, registerSpec.withReg(0));
    } 
    return null;
  }
  
  public DalvInsn expandedVersion(BitSet paramBitSet) {
    return withRegisters(this.registers.withExpandedRegisters(0, hasResult(), paramBitSet));
  }
  
  public final int getAddress() {
    int i = this.address;
    if (i >= 0)
      return i; 
    throw new RuntimeException("address not yet known");
  }
  
  public DalvInsn getLowRegVersion() {
    return withRegisters(this.registers.withExpandedRegisters(0, hasResult(), null));
  }
  
  public final int getMinimumRegisterRequirement(BitSet paramBitSet) {
    throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
  }
  
  public final int getNextAddress() {
    return getAddress() + codeSize();
  }
  
  public final Dop getOpcode() {
    return this.opcode;
  }
  
  public final SourcePosition getPosition() {
    return this.position;
  }
  
  public final RegisterSpecList getRegisters() {
    return this.registers;
  }
  
  public final boolean hasAddress() {
    return (this.address >= 0);
  }
  
  public final boolean hasResult() {
    return this.opcode.hasResult();
  }
  
  public final String identifierString() {
    int i = this.address;
    return (i != -1) ? String.format("%04x", new Object[] { Integer.valueOf(i) }) : Hex.u4(System.identityHashCode(this));
  }
  
  public final String listingString(String paramString, int paramInt, boolean paramBoolean) {
    String str = listingString0(paramBoolean);
    if (str == null)
      return null; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(identifierString());
    stringBuilder.append(": ");
    paramString = stringBuilder.toString();
    int i = paramString.length();
    if (paramInt == 0) {
      paramInt = str.length();
    } else {
      paramInt -= i;
    } 
    return TwoColumnOutput.toString(paramString, i, "", str, paramInt);
  }
  
  protected abstract String listingString0(boolean paramBoolean);
  
  public final void setAddress(int paramInt) {
    if (paramInt >= 0) {
      this.address = paramInt;
      return;
    } 
    throw new IllegalArgumentException("address < 0");
  }
  
  public final String toString() {
    boolean bool;
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append(identifierString());
    stringBuffer.append(' ');
    stringBuffer.append(this.position);
    stringBuffer.append(": ");
    stringBuffer.append(this.opcode.getName());
    if (this.registers.size() != 0) {
      stringBuffer.append(this.registers.toHuman(" ", ", ", null));
      bool = true;
    } else {
      bool = false;
    } 
    String str = argString();
    if (str != null) {
      if (bool)
        stringBuffer.append(','); 
      stringBuffer.append(' ');
      stringBuffer.append(str);
    } 
    return stringBuffer.toString();
  }
  
  public DalvInsn withMapper(RegisterMapper paramRegisterMapper) {
    return withRegisters(paramRegisterMapper.map(getRegisters()));
  }
  
  public abstract DalvInsn withOpcode(Dop paramDop);
  
  public abstract DalvInsn withRegisterOffset(int paramInt);
  
  public abstract DalvInsn withRegisters(RegisterSpecList paramRegisterSpecList);
  
  public abstract void writeTo(AnnotatedOutput paramAnnotatedOutput);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\DalvInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */