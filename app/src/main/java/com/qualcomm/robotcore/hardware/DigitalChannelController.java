package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public interface DigitalChannelController extends HardwareDevice {
  DigitalChannel.Mode getDigitalChannelMode(int paramInt);
  
  boolean getDigitalChannelState(int paramInt);
  
  SerialNumber getSerialNumber();
  
  void setDigitalChannelMode(int paramInt, DigitalChannel.Mode paramMode);
  
  @Deprecated
  void setDigitalChannelMode(int paramInt, Mode paramMode);
  
  void setDigitalChannelState(int paramInt, boolean paramBoolean);
  
  @Deprecated
  public enum Mode {
    INPUT, OUTPUT;
    
    static {
      Mode mode = new Mode("OUTPUT", 1);
      OUTPUT = mode;
      $VALUES = new Mode[] { INPUT, mode };
    }
    
    public DigitalChannel.Mode migrate() {
      return (DigitalChannelController.null.$SwitchMap$com$qualcomm$robotcore$hardware$DigitalChannelController$Mode[ordinal()] != 1) ? DigitalChannel.Mode.OUTPUT : DigitalChannel.Mode.INPUT;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\DigitalChannelController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */