package org.firstinspires.ftc.robotcore.external.navigation;

public enum CurrentUnit {
  AMPS(1.0D) {
    public double convert(double param1Double, CurrentUnit param1CurrentUnit) {
      return param1CurrentUnit.toAmps(param1Double);
    }
  },
  MILLIAMPS(1.0D);
  
  private double ampsPerUnit;
  
  static {
    null  = new null("MILLIAMPS", 1, 0.001D);
    MILLIAMPS = ;
    $VALUES = new CurrentUnit[] { AMPS,  };
  }
  
  CurrentUnit(double paramDouble) {
    this.ampsPerUnit = paramDouble;
  }
  
  public double convert(double paramDouble, CurrentUnit paramCurrentUnit) {
    throw new AbstractMethodError();
  }
  
  public double toAmps(double paramDouble) {
    return paramDouble * this.ampsPerUnit / AMPS.ampsPerUnit;
  }
  
  public double toMilliAmps(double paramDouble) {
    return paramDouble * this.ampsPerUnit / MILLIAMPS.ampsPerUnit;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\CurrentUnit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */