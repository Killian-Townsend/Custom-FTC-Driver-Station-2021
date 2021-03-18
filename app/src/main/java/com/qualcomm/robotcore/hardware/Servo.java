package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.configuration.ServoFlavor;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.ServoType;

@DeviceProperties(builtIn = true, name = "@string/configTypeServo", xmlTag = "Servo")
@ServoType(flavor = ServoFlavor.STANDARD)
public interface Servo extends HardwareDevice {
  public static final double MAX_POSITION = 1.0D;
  
  public static final double MIN_POSITION = 0.0D;
  
  ServoController getController();
  
  Direction getDirection();
  
  int getPortNumber();
  
  double getPosition();
  
  void scaleRange(double paramDouble1, double paramDouble2);
  
  void setDirection(Direction paramDirection);
  
  void setPosition(double paramDouble);
  
  public enum Direction {
    FORWARD, REVERSE;
    
    static {
      Direction direction = new Direction("REVERSE", 1);
      REVERSE = direction;
      $VALUES = new Direction[] { FORWARD, direction };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\Servo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */