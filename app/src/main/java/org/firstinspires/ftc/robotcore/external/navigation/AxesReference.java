package org.firstinspires.ftc.robotcore.external.navigation;

public enum AxesReference {
  EXTRINSIC, INTRINSIC;
  
  static {
    AxesReference axesReference = new AxesReference("INTRINSIC", 1);
    INTRINSIC = axesReference;
    $VALUES = new AxesReference[] { EXTRINSIC, axesReference };
  }
  
  public AxesReference reverse() {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AxesReference[ordinal()] != 2) ? INTRINSIC : EXTRINSIC;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\AxesReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */