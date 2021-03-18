package com.qualcomm.robotcore.hardware.configuration;

import com.google.gson.annotations.Expose;
import com.qualcomm.robotcore.util.TypeConversion;
import java.io.Serializable;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class ModernRoboticsMotorControllerParamsState implements Serializable, Cloneable {
  @Expose
  public int d = 0;
  
  @Expose
  public int i = 0;
  
  @Expose
  public int p = 0;
  
  @Expose
  public int ratio = 0;
  
  public ModernRoboticsMotorControllerParamsState() {
    Assert.assertTrue(isDefault());
  }
  
  public ModernRoboticsMotorControllerParamsState(ModernRoboticsMotorControllerParams paramModernRoboticsMotorControllerParams) {
    this.ratio = paramModernRoboticsMotorControllerParams.ratio();
    this.p = paramModernRoboticsMotorControllerParams.P();
    this.i = paramModernRoboticsMotorControllerParams.I();
    this.d = paramModernRoboticsMotorControllerParams.D();
  }
  
  public static ModernRoboticsMotorControllerParamsState fromByteArray(byte[] paramArrayOfbyte) {
    ModernRoboticsMotorControllerParamsState modernRoboticsMotorControllerParamsState = new ModernRoboticsMotorControllerParamsState();
    modernRoboticsMotorControllerParamsState.ratio = TypeConversion.unsignedByteToInt(paramArrayOfbyte[0]);
    modernRoboticsMotorControllerParamsState.p = TypeConversion.unsignedByteToInt(paramArrayOfbyte[1]);
    modernRoboticsMotorControllerParamsState.i = TypeConversion.unsignedByteToInt(paramArrayOfbyte[2]);
    modernRoboticsMotorControllerParamsState.d = TypeConversion.unsignedByteToInt(paramArrayOfbyte[3]);
    return modernRoboticsMotorControllerParamsState;
  }
  
  public ModernRoboticsMotorControllerParamsState clone() {
    try {
      return (ModernRoboticsMotorControllerParamsState)super.clone();
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      throw new RuntimeException("internal error: Parameters not cloneable");
    } 
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof ModernRoboticsMotorControllerParamsState;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      paramObject = paramObject;
      bool1 = bool2;
      if (this.ratio == ((ModernRoboticsMotorControllerParamsState)paramObject).ratio) {
        bool1 = bool2;
        if (this.p == ((ModernRoboticsMotorControllerParamsState)paramObject).p) {
          bool1 = bool2;
          if (this.i == ((ModernRoboticsMotorControllerParamsState)paramObject).i) {
            bool1 = bool2;
            if (this.d == ((ModernRoboticsMotorControllerParamsState)paramObject).d)
              bool1 = true; 
          } 
        } 
      } 
    } 
    return bool1;
  }
  
  public int hashCode() {
    return this.ratio ^ this.p << 3 ^ this.i << 6 ^ this.d << 9 ^ 0xFAD11234;
  }
  
  public boolean isDefault() {
    return (this.ratio == 0 && this.p == 0 && this.i == 0 && this.d == 0);
  }
  
  public byte[] toByteArray() {
    return new byte[] { (byte)this.ratio, (byte)this.p, (byte)this.i, (byte)this.d };
  }
  
  public String toString() {
    return String.format("ratio=%d,p=%d,i=%d,d=%d", new Object[] { Integer.valueOf(this.ratio), Integer.valueOf(this.p), Integer.valueOf(this.i), Integer.valueOf(this.d) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\ModernRoboticsMotorControllerParamsState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */