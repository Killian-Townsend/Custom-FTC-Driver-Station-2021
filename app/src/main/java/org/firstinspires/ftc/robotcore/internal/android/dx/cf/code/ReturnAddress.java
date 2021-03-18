package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class ReturnAddress implements TypeBearer {
  private final int subroutineAddress;
  
  public ReturnAddress(int paramInt) {
    if (paramInt >= 0) {
      this.subroutineAddress = paramInt;
      return;
    } 
    throw new IllegalArgumentException("subroutineAddress < 0");
  }
  
  public boolean equals(Object paramObject) {
    boolean bool1 = paramObject instanceof ReturnAddress;
    boolean bool = false;
    if (!bool1)
      return false; 
    if (this.subroutineAddress == ((ReturnAddress)paramObject).subroutineAddress)
      bool = true; 
    return bool;
  }
  
  public int getBasicFrameType() {
    return Type.RETURN_ADDRESS.getBasicFrameType();
  }
  
  public int getBasicType() {
    return Type.RETURN_ADDRESS.getBasicType();
  }
  
  public TypeBearer getFrameType() {
    return this;
  }
  
  public int getSubroutineAddress() {
    return this.subroutineAddress;
  }
  
  public Type getType() {
    return Type.RETURN_ADDRESS;
  }
  
  public int hashCode() {
    return this.subroutineAddress;
  }
  
  public boolean isConstant() {
    return false;
  }
  
  public String toHuman() {
    return toString();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<addr:");
    stringBuilder.append(Hex.u2(this.subroutineAddress));
    stringBuilder.append(">");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\ReturnAddress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */