package org.firstinspires.ftc.robotcore.external.hardware.camera.controls;

public interface FocusControl extends CameraControl {
  public static final double unknownFocusLength = -1.0D;
  
  double getFocusLength();
  
  double getMaxFocusLength();
  
  double getMinFocusLength();
  
  Mode getMode();
  
  boolean isFocusLengthSupported();
  
  boolean isModeSupported(Mode paramMode);
  
  boolean setFocusLength(double paramDouble);
  
  boolean setMode(Mode paramMode);
  
  public enum Mode {
    Auto, ContinuousAuto, Fixed, Infinity, Macro, Unknown;
    
    static {
      Infinity = new Mode("Infinity", 4);
      Mode mode = new Mode("Fixed", 5);
      Fixed = mode;
      $VALUES = new Mode[] { Unknown, Auto, ContinuousAuto, Macro, Infinity, mode };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\hardware\camera\controls\FocusControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */