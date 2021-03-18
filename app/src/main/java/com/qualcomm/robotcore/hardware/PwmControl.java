package com.qualcomm.robotcore.hardware;

public interface PwmControl {
  PwmRange getPwmRange();
  
  boolean isPwmEnabled();
  
  void setPwmDisable();
  
  void setPwmEnable();
  
  void setPwmRange(PwmRange paramPwmRange);
  
  public static class PwmRange {
    public static final PwmRange defaultRange = new PwmRange(600.0D, 2400.0D);
    
    public static final double usFrameDefault = 20000.0D;
    
    public static final double usPulseLowerDefault = 600.0D;
    
    public static final double usPulseUpperDefault = 2400.0D;
    
    public final double usFrame;
    
    public final double usPulseLower;
    
    public final double usPulseUpper;
    
    public PwmRange(double param1Double1, double param1Double2) {
      this(param1Double1, param1Double2, 20000.0D);
    }
    
    public PwmRange(double param1Double1, double param1Double2, double param1Double3) {
      this.usPulseLower = param1Double1;
      this.usPulseUpper = param1Double2;
      this.usFrame = param1Double3;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = param1Object instanceof PwmRange;
      boolean bool2 = false;
      boolean bool1 = bool2;
      if (bool) {
        param1Object = param1Object;
        bool1 = bool2;
        if (this.usPulseLower == ((PwmRange)param1Object).usPulseLower) {
          bool1 = bool2;
          if (this.usPulseUpper == ((PwmRange)param1Object).usPulseUpper) {
            bool1 = bool2;
            if (this.usFrame == ((PwmRange)param1Object).usFrame)
              bool1 = true; 
          } 
        } 
      } 
      return bool1;
    }
    
    public int hashCode() {
      return Double.valueOf(this.usPulseLower).hashCode() ^ Double.valueOf(this.usPulseUpper).hashCode() ^ Double.valueOf(this.usFrame).hashCode();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\PwmControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */