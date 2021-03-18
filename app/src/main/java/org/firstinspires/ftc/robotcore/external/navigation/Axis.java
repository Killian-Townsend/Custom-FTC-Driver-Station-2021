package org.firstinspires.ftc.robotcore.external.navigation;

public enum Axis {
  UNKNOWN,
  X(0),
  Y(1),
  Z(2);
  
  public int index;
  
  static {
    Axis axis = new Axis("UNKNOWN", 3, -1);
    UNKNOWN = axis;
    $VALUES = new Axis[] { X, Y, Z, axis };
  }
  
  Axis(int paramInt1) {
    this.index = paramInt1;
  }
  
  public static Axis fromIndex(int paramInt) {
    return (paramInt != 0) ? ((paramInt != 1) ? ((paramInt != 2) ? UNKNOWN : Z) : Y) : X;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\Axis.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */