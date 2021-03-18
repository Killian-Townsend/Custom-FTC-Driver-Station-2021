package com.qualcomm.robotcore.hardware;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public interface DcMotorControllerEx extends DcMotorController {
  double getMotorCurrent(int paramInt, CurrentUnit paramCurrentUnit);
  
  double getMotorCurrentAlert(int paramInt, CurrentUnit paramCurrentUnit);
  
  double getMotorVelocity(int paramInt);
  
  double getMotorVelocity(int paramInt, AngleUnit paramAngleUnit);
  
  @Deprecated
  PIDCoefficients getPIDCoefficients(int paramInt, DcMotor.RunMode paramRunMode);
  
  PIDFCoefficients getPIDFCoefficients(int paramInt, DcMotor.RunMode paramRunMode);
  
  boolean isMotorEnabled(int paramInt);
  
  boolean isMotorOverCurrent(int paramInt);
  
  void setMotorCurrentAlert(int paramInt, double paramDouble, CurrentUnit paramCurrentUnit);
  
  void setMotorDisable(int paramInt);
  
  void setMotorEnable(int paramInt);
  
  void setMotorTargetPosition(int paramInt1, int paramInt2, int paramInt3);
  
  void setMotorVelocity(int paramInt, double paramDouble);
  
  void setMotorVelocity(int paramInt, double paramDouble, AngleUnit paramAngleUnit);
  
  @Deprecated
  void setPIDCoefficients(int paramInt, DcMotor.RunMode paramRunMode, PIDCoefficients paramPIDCoefficients);
  
  void setPIDFCoefficients(int paramInt, DcMotor.RunMode paramRunMode, PIDFCoefficients paramPIDFCoefficients) throws UnsupportedOperationException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\DcMotorControllerEx.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */