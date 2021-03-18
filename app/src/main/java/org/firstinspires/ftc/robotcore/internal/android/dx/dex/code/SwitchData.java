package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public final class SwitchData extends VariableSizeInsn {
  private final IntList cases;
  
  private final boolean packed;
  
  private final CodeAddress[] targets;
  
  private final CodeAddress user;
  
  public SwitchData(SourcePosition paramSourcePosition, CodeAddress paramCodeAddress, IntList paramIntList, CodeAddress[] paramArrayOfCodeAddress) {
    super(paramSourcePosition, RegisterSpecList.EMPTY);
    if (paramCodeAddress != null) {
      if (paramIntList != null) {
        if (paramArrayOfCodeAddress != null) {
          int i = paramIntList.size();
          if (i == paramArrayOfCodeAddress.length) {
            if (i <= 65535) {
              this.user = paramCodeAddress;
              this.cases = paramIntList;
              this.targets = paramArrayOfCodeAddress;
              this.packed = shouldPack(paramIntList);
              return;
            } 
            throw new IllegalArgumentException("too many cases");
          } 
          throw new IllegalArgumentException("cases / targets mismatch");
        } 
        throw new NullPointerException("targets == null");
      } 
      throw new NullPointerException("cases == null");
    } 
    throw new NullPointerException("user == null");
  }
  
  private static long packedCodeSize(IntList paramIntList) {
    int i = paramIntList.size();
    long l = paramIntList.get(0);
    l = (paramIntList.get(i - 1) - l + 1L) * 2L + 4L;
    return (l <= 2147483647L) ? l : -1L;
  }
  
  private static boolean shouldPack(IntList paramIntList) {
    if (paramIntList.size() < 2)
      return true; 
    long l1 = packedCodeSize(paramIntList);
    long l2 = sparseCodeSize(paramIntList);
    return (l1 >= 0L && l1 <= l2 * 5L / 4L);
  }
  
  private static long sparseCodeSize(IntList paramIntList) {
    return paramIntList.size() * 4L + 2L;
  }
  
  protected String argString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    int j = this.targets.length;
    for (int i = 0; i < j; i++) {
      stringBuffer.append("\n    ");
      stringBuffer.append(this.cases.get(i));
      stringBuffer.append(": ");
      stringBuffer.append(this.targets[i]);
    } 
    return stringBuffer.toString();
  }
  
  public int codeSize() {
    long l;
    if (this.packed) {
      l = packedCodeSize(this.cases);
    } else {
      l = sparseCodeSize(this.cases);
    } 
    return (int)l;
  }
  
  public boolean isPacked() {
    return this.packed;
  }
  
  protected String listingString0(boolean paramBoolean) {
    String str;
    int j = this.user.getAddress();
    StringBuffer stringBuffer = new StringBuffer(100);
    int k = this.targets.length;
    if (this.packed) {
      str = "packed";
    } else {
      str = "sparse";
    } 
    stringBuffer.append(str);
    stringBuffer.append("-switch-payload // for switch @ ");
    stringBuffer.append(Hex.u2(j));
    for (int i = 0; i < k; i++) {
      int m = this.targets[i].getAddress();
      stringBuffer.append("\n  ");
      stringBuffer.append(this.cases.get(i));
      stringBuffer.append(": ");
      stringBuffer.append(Hex.u4(m));
      stringBuffer.append(" // ");
      stringBuffer.append(Hex.s4(m - j));
    } 
    return stringBuffer.toString();
  }
  
  public DalvInsn withRegisters(RegisterSpecList paramRegisterSpecList) {
    return new SwitchData(getPosition(), this.user, this.cases, this.targets);
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput) {
    int m = this.user.getAddress();
    int k = Dops.PACKED_SWITCH.getFormat().codeSize();
    int n = this.targets.length;
    boolean bool = this.packed;
    int i = 0;
    int j = 0;
    if (bool) {
      int i1;
      if (n == 0) {
        i1 = 0;
      } else {
        i1 = this.cases.get(0);
      } 
      if (n == 0) {
        i2 = 0;
      } else {
        i2 = this.cases.get(n - 1);
      } 
      n = i2 - i1 + 1;
      paramAnnotatedOutput.writeShort(256);
      paramAnnotatedOutput.writeShort(n);
      paramAnnotatedOutput.writeInt(i1);
      i = 0;
      for (int i2 = j; i2 < n; i2++) {
        if (this.cases.get(i) > i1 + i2) {
          j = k;
        } else {
          j = this.targets[i].getAddress() - m;
          i++;
        } 
        paramAnnotatedOutput.writeInt(j);
      } 
    } else {
      int i2;
      paramAnnotatedOutput.writeShort(512);
      paramAnnotatedOutput.writeShort(n);
      int i1 = 0;
      while (true) {
        i2 = i;
        if (i1 < n) {
          paramAnnotatedOutput.writeInt(this.cases.get(i1));
          i1++;
          continue;
        } 
        break;
      } 
      while (i2 < n) {
        paramAnnotatedOutput.writeInt(this.targets[i2].getAddress() - m);
        i2++;
      } 
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\SwitchData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */