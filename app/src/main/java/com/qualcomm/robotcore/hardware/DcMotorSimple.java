package com.qualcomm.robotcore.hardware;

public interface DcMotorSimple extends HardwareDevice {
  Direction getDirection();
  
  double getPower();
  
  void setDirection(Direction paramDirection);
  
  void setPower(double paramDouble);
  
  public enum Direction {
    FORWARD, REVERSE;
    
    static {
      Direction direction = new Direction("REVERSE", 1);
      REVERSE = direction;
      $VALUES = new Direction[] { FORWARD, direction };
    }
    
    public Direction inverted() {
      Direction direction2 = FORWARD;
      Direction direction1 = direction2;
      if (this == direction2)
        direction1 = REVERSE; 
      return direction1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\DcMotorSimple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */