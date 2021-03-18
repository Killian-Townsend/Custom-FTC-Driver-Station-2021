package org.firstinspires.ftc.robotcore.external.navigation;

public enum VoltageUnit {
  MILLIVOLTS,
  VOLTS(1.0D) {
    public double convert(double param1Double, VoltageUnit param1VoltageUnit) {
      return param1VoltageUnit.toVolts(param1Double);
    }
  };
  
  private double voltsPerUnit;
  
  static {
    null  = new null("MILLIVOLTS", 1, 0.001D);
    MILLIVOLTS = ;
    $VALUES = new VoltageUnit[] { VOLTS,  };
  }
  
  VoltageUnit(double paramDouble) {
    this.voltsPerUnit = paramDouble;
  }
  
  public double convert(double paramDouble, VoltageUnit paramVoltageUnit) {
    throw new AbstractMethodError();
  }
  
  public double toMilliVolts(double paramDouble) {
    return paramDouble * this.voltsPerUnit / MILLIVOLTS.voltsPerUnit;
  }
  
  public double toVolts(double paramDouble) {
    return paramDouble * this.voltsPerUnit / VOLTS.voltsPerUnit;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\VoltageUnit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */