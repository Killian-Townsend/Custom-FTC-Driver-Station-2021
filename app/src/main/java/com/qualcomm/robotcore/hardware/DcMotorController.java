package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public interface DcMotorController extends HardwareDevice {
  int getMotorCurrentPosition(int paramInt);
  
  DcMotor.RunMode getMotorMode(int paramInt);
  
  double getMotorPower(int paramInt);
  
  boolean getMotorPowerFloat(int paramInt);
  
  int getMotorTargetPosition(int paramInt);
  
  MotorConfigurationType getMotorType(int paramInt);
  
  DcMotor.ZeroPowerBehavior getMotorZeroPowerBehavior(int paramInt);
  
  boolean isBusy(int paramInt);
  
  void resetDeviceConfigurationForOpMode(int paramInt);
  
  void setMotorMode(int paramInt, DcMotor.RunMode paramRunMode);
  
  void setMotorPower(int paramInt, double paramDouble);
  
  void setMotorTargetPosition(int paramInt1, int paramInt2);
  
  void setMotorType(int paramInt, MotorConfigurationType paramMotorConfigurationType);
  
  void setMotorZeroPowerBehavior(int paramInt, DcMotor.ZeroPowerBehavior paramZeroPowerBehavior);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\DcMotorController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */