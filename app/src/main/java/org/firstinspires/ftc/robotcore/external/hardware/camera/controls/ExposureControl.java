package org.firstinspires.ftc.robotcore.external.hardware.camera.controls;

import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.collections.MutableReference;

public interface ExposureControl extends CameraControl {
  public static final long unknownExposure = 0L;
  
  long getCachedExposure(TimeUnit paramTimeUnit1, MutableReference<Boolean> paramMutableReference, long paramLong, TimeUnit paramTimeUnit2);
  
  long getExposure(TimeUnit paramTimeUnit);
  
  long getMaxExposure(TimeUnit paramTimeUnit);
  
  long getMinExposure(TimeUnit paramTimeUnit);
  
  Mode getMode();
  
  boolean isExposureSupported();
  
  boolean isModeSupported(Mode paramMode);
  
  boolean setExposure(long paramLong, TimeUnit paramTimeUnit);
  
  boolean setMode(Mode paramMode);
  
  public enum Mode {
    AperturePriority, Auto, ContinuousAuto, Manual, ShutterPriority, Unknown;
    
    static {
      Mode mode = new Mode("AperturePriority", 5);
      AperturePriority = mode;
      $VALUES = new Mode[] { Unknown, Auto, ContinuousAuto, Manual, ShutterPriority, mode };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\hardware\camera\controls\ExposureControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */