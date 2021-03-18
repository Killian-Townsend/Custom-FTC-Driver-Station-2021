package org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider;

public enum FrameFormat {
  UNKNOWN, YUYV;
  
  static {
    FrameFormat frameFormat = new FrameFormat("YUYV", 1);
    YUYV = frameFormat;
    $VALUES = new FrameFormat[] { UNKNOWN, frameFormat };
  }
  
  public static FrameFormat from(int paramInt) {
    return (paramInt >= 0 && paramInt < (values()).length) ? values()[paramInt] : UNKNOWN;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\externalprovider\FrameFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */