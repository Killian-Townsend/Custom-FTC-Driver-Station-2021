package com.qualcomm.robotcore.hardware.configuration;

import com.google.gson.annotations.Expose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.annotations.ExpansionHubPIDFPositionParams;
import com.qualcomm.robotcore.hardware.configuration.annotations.ExpansionHubPIDFVelocityParams;
import java.io.Serializable;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class ExpansionHubMotorControllerParamsState implements Serializable, Cloneable {
  @Expose
  public MotorControlAlgorithm algorithm;
  
  @Expose
  public double d = 0.0D;
  
  @Expose
  public double f = 0.0D;
  
  @Expose
  public double i = 0.0D;
  
  @Expose
  public DcMotor.RunMode mode = null;
  
  @Expose
  public double p = 0.0D;
  
  public ExpansionHubMotorControllerParamsState() {
    Assert.assertTrue(isDefault());
  }
  
  public ExpansionHubMotorControllerParamsState(DcMotor.RunMode paramRunMode, PIDFCoefficients paramPIDFCoefficients) {
    this.mode = paramRunMode;
    this.p = paramPIDFCoefficients.p;
    this.i = paramPIDFCoefficients.i;
    this.d = paramPIDFCoefficients.d;
    this.f = paramPIDFCoefficients.f;
    this.algorithm = paramPIDFCoefficients.algorithm;
  }
  
  public ExpansionHubMotorControllerParamsState(ExpansionHubMotorControllerPositionParams paramExpansionHubMotorControllerPositionParams) {
    this.mode = DcMotor.RunMode.RUN_TO_POSITION;
    this.p = paramExpansionHubMotorControllerPositionParams.P();
    this.i = paramExpansionHubMotorControllerPositionParams.I();
    this.d = paramExpansionHubMotorControllerPositionParams.D();
    this.f = 0.0D;
    this.algorithm = MotorControlAlgorithm.LegacyPID;
  }
  
  public ExpansionHubMotorControllerParamsState(ExpansionHubMotorControllerVelocityParams paramExpansionHubMotorControllerVelocityParams) {
    this.mode = DcMotor.RunMode.RUN_USING_ENCODER;
    this.p = paramExpansionHubMotorControllerVelocityParams.P();
    this.i = paramExpansionHubMotorControllerVelocityParams.I();
    this.d = paramExpansionHubMotorControllerVelocityParams.D();
    this.f = 0.0D;
    this.algorithm = MotorControlAlgorithm.LegacyPID;
  }
  
  public ExpansionHubMotorControllerParamsState(ExpansionHubPIDFPositionParams paramExpansionHubPIDFPositionParams) {
    this.mode = DcMotor.RunMode.RUN_TO_POSITION;
    this.p = paramExpansionHubPIDFPositionParams.P();
    this.i = 0.0D;
    this.d = 0.0D;
    this.f = 0.0D;
    this.algorithm = paramExpansionHubPIDFPositionParams.algorithm();
  }
  
  public ExpansionHubMotorControllerParamsState(ExpansionHubPIDFVelocityParams paramExpansionHubPIDFVelocityParams) {
    this.mode = DcMotor.RunMode.RUN_USING_ENCODER;
    this.p = paramExpansionHubPIDFVelocityParams.P();
    this.i = paramExpansionHubPIDFVelocityParams.I();
    this.d = paramExpansionHubPIDFVelocityParams.D();
    this.f = paramExpansionHubPIDFVelocityParams.F();
    this.algorithm = paramExpansionHubPIDFVelocityParams.algorithm();
  }
  
  public ExpansionHubMotorControllerParamsState clone() {
    try {
      return (ExpansionHubMotorControllerParamsState)super.clone();
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      throw new RuntimeException("internal error: Parameters not cloneable");
    } 
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof ExpansionHubMotorControllerParamsState;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      paramObject = paramObject;
      bool1 = bool2;
      if (this.mode == ((ExpansionHubMotorControllerParamsState)paramObject).mode) {
        bool1 = bool2;
        if (this.p == ((ExpansionHubMotorControllerParamsState)paramObject).p) {
          bool1 = bool2;
          if (this.i == ((ExpansionHubMotorControllerParamsState)paramObject).i) {
            bool1 = bool2;
            if (this.d == ((ExpansionHubMotorControllerParamsState)paramObject).d) {
              bool1 = bool2;
              if (this.f == ((ExpansionHubMotorControllerParamsState)paramObject).f) {
                bool1 = bool2;
                if (this.algorithm == ((ExpansionHubMotorControllerParamsState)paramObject).algorithm)
                  bool1 = true; 
              } 
            } 
          } 
        } 
      } 
    } 
    return bool1;
  }
  
  public PIDFCoefficients getPidfCoefficients() {
    return new PIDFCoefficients(this.p, this.i, this.d, this.f, this.algorithm);
  }
  
  protected int hash(double paramDouble) {
    return Double.valueOf(paramDouble).hashCode();
  }
  
  public int hashCode() {
    return this.mode.hashCode() ^ hash(this.p) << 3 ^ hash(this.i) << 6 ^ hash(this.d) << 9 ^ hash(this.f) << 12 ^ 0xCCAE348C;
  }
  
  public boolean isDefault() {
    return (this.mode == null);
  }
  
  public String toString() {
    return Misc.formatForUser("mode=%s,p=%f,i=%f,d=%f,f=%f", new Object[] { this.mode, Double.valueOf(this.p), Double.valueOf(this.i), Double.valueOf(this.d), Double.valueOf(this.f) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\ExpansionHubMotorControllerParamsState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */