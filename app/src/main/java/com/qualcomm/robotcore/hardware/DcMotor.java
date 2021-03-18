package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public interface DcMotor extends DcMotorSimple {
  DcMotorController getController();
  
  int getCurrentPosition();
  
  RunMode getMode();
  
  MotorConfigurationType getMotorType();
  
  int getPortNumber();
  
  boolean getPowerFloat();
  
  int getTargetPosition();
  
  ZeroPowerBehavior getZeroPowerBehavior();
  
  boolean isBusy();
  
  void setMode(RunMode paramRunMode);
  
  void setMotorType(MotorConfigurationType paramMotorConfigurationType);
  
  @Deprecated
  void setPowerFloat();
  
  void setTargetPosition(int paramInt);
  
  void setZeroPowerBehavior(ZeroPowerBehavior paramZeroPowerBehavior);
  
  public enum RunMode {
    RESET_ENCODERS, RUN_TO_POSITION, RUN_USING_ENCODER, RUN_USING_ENCODERS, RUN_WITHOUT_ENCODER, RUN_WITHOUT_ENCODERS, STOP_AND_RESET_ENCODER;
    
    static {
      RUN_TO_POSITION = new RunMode("RUN_TO_POSITION", 2);
      STOP_AND_RESET_ENCODER = new RunMode("STOP_AND_RESET_ENCODER", 3);
      RUN_WITHOUT_ENCODERS = new RunMode("RUN_WITHOUT_ENCODERS", 4);
      RUN_USING_ENCODERS = new RunMode("RUN_USING_ENCODERS", 5);
      RunMode runMode = new RunMode("RESET_ENCODERS", 6);
      RESET_ENCODERS = runMode;
      $VALUES = new RunMode[] { RUN_WITHOUT_ENCODER, RUN_USING_ENCODER, RUN_TO_POSITION, STOP_AND_RESET_ENCODER, RUN_WITHOUT_ENCODERS, RUN_USING_ENCODERS, runMode };
    }
    
    public boolean isPIDMode() {
      return (this == RUN_USING_ENCODER || this == RUN_USING_ENCODERS || this == RUN_TO_POSITION);
    }
    
    @Deprecated
    public RunMode migrate() {
      int i = DcMotor.null.$SwitchMap$com$qualcomm$robotcore$hardware$DcMotor$RunMode[ordinal()];
      return (i != 1) ? ((i != 2) ? ((i != 3) ? this : STOP_AND_RESET_ENCODER) : RUN_USING_ENCODER) : RUN_WITHOUT_ENCODER;
    }
  }
  
  public enum ZeroPowerBehavior {
    BRAKE, FLOAT, UNKNOWN;
    
    static {
      ZeroPowerBehavior zeroPowerBehavior = new ZeroPowerBehavior("FLOAT", 2);
      FLOAT = zeroPowerBehavior;
      $VALUES = new ZeroPowerBehavior[] { UNKNOWN, BRAKE, zeroPowerBehavior };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\DcMotor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */