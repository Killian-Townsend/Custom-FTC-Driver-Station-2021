package com.qualcomm.robotcore.hardware;

public interface IrSeekerSensor extends HardwareDevice {
  double getAngle();
  
  I2cAddr getI2cAddress();
  
  IrSeekerIndividualSensor[] getIndividualSensors();
  
  Mode getMode();
  
  double getSignalDetectedThreshold();
  
  double getStrength();
  
  void setI2cAddress(I2cAddr paramI2cAddr);
  
  void setMode(Mode paramMode);
  
  void setSignalDetectedThreshold(double paramDouble);
  
  boolean signalDetected();
  
  public static class IrSeekerIndividualSensor {
    private double angle = 0.0D;
    
    private double strength = 0.0D;
    
    public IrSeekerIndividualSensor() {
      this(0.0D, 0.0D);
    }
    
    public IrSeekerIndividualSensor(double param1Double1, double param1Double2) {
      this.angle = param1Double1;
      this.strength = param1Double2;
    }
    
    public double getSensorAngle() {
      return this.angle;
    }
    
    public double getSensorStrength() {
      return this.strength;
    }
    
    public String toString() {
      return String.format("IR Sensor: %3.1f degrees at %3.1f%% power", new Object[] { Double.valueOf(this.angle), Double.valueOf(this.strength * 100.0D) });
    }
  }
  
  public enum Mode {
    MODE_1200HZ, MODE_600HZ;
    
    static {
      Mode mode = new Mode("MODE_1200HZ", 1);
      MODE_1200HZ = mode;
      $VALUES = new Mode[] { MODE_600HZ, mode };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\IrSeekerSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */