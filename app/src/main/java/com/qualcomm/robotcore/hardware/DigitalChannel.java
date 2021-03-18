package com.qualcomm.robotcore.hardware;

public interface DigitalChannel extends HardwareDevice {
  Mode getMode();
  
  boolean getState();
  
  void setMode(Mode paramMode);
  
  @Deprecated
  void setMode(DigitalChannelController.Mode paramMode);
  
  void setState(boolean paramBoolean);
  
  public enum Mode {
    INPUT, OUTPUT;
    
    static {
      Mode mode = new Mode("OUTPUT", 1);
      OUTPUT = mode;
      $VALUES = new Mode[] { INPUT, mode };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\DigitalChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */