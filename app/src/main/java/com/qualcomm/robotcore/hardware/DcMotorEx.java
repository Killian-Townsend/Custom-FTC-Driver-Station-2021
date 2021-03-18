package com.qualcomm.robotcore.hardware;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public interface DcMotorEx extends DcMotor {
  double getCurrent(CurrentUnit paramCurrentUnit);
  
  double getCurrentAlert(CurrentUnit paramCurrentUnit);
  
  @Deprecated
  PIDCoefficients getPIDCoefficients(DcMotor.RunMode paramRunMode);
  
  PIDFCoefficients getPIDFCoefficients(DcMotor.RunMode paramRunMode);
  
  int getTargetPositionTolerance();
  
  double getVelocity();
  
  double getVelocity(AngleUnit paramAngleUnit);
  
  boolean isMotorEnabled();
  
  boolean isOverCurrent();
  
  void setCurrentAlert(double paramDouble, CurrentUnit paramCurrentUnit);
  
  void setMotorDisable();
  
  void setMotorEnable();
  
  @Deprecated
  void setPIDCoefficients(DcMotor.RunMode paramRunMode, PIDCoefficients paramPIDCoefficients);
  
  void setPIDFCoefficients(DcMotor.RunMode paramRunMode, PIDFCoefficients paramPIDFCoefficients) throws UnsupportedOperationException;
  
  void setPositionPIDFCoefficients(double paramDouble);
  
  void setTargetPositionTolerance(int paramInt);
  
  void setVelocity(double paramDouble);
  
  void setVelocity(double paramDouble, AngleUnit paramAngleUnit);
  
  void setVelocityPIDFCoefficients(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\DcMotorEx.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */