package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;

public class SwitchableExposureControl extends CachingExposureControl {
  protected final RefCountedSwitchableCameraImpl switchableCamera;
  
  public SwitchableExposureControl(RefCountedSwitchableCameraImpl paramRefCountedSwitchableCameraImpl) {
    this.switchableCamera = paramRefCountedSwitchableCameraImpl;
  }
  
  protected boolean aggregatedIsExposureSupported() {
    synchronized (this.lock) {
      Iterator<SwitchableMemberInfo> iterator = this.switchableCamera.cameraInfos.values().iterator();
      while (iterator.hasNext()) {
        ExposureControl exposureControl = ((SwitchableMemberInfo)iterator.next()).<ExposureControl>getControl(ExposureControl.class);
        if (exposureControl != null && !exposureControl.isExposureSupported())
          return false; 
      } 
      return true;
    } 
  }
  
  protected boolean aggregatedIsModeSupported(ExposureControl.Mode paramMode) {
    synchronized (this.lock) {
      Iterator<SwitchableMemberInfo> iterator = this.switchableCamera.cameraInfos.values().iterator();
      while (iterator.hasNext()) {
        ExposureControl exposureControl = ((SwitchableMemberInfo)iterator.next()).<ExposureControl>getControl(ExposureControl.class);
        if (exposureControl != null && !exposureControl.isModeSupported(paramMode))
          return false; 
      } 
      return true;
    } 
  }
  
  protected long aggregatedMaxExposure(TimeUnit paramTimeUnit) {
    long l1;
    synchronized (this.lock) {
      Iterator<SwitchableMemberInfo> iterator = this.switchableCamera.cameraInfos.values().iterator();
      l1 = Long.MAX_VALUE;
      while (iterator.hasNext()) {
        ExposureControl exposureControl = ((SwitchableMemberInfo)iterator.next()).<ExposureControl>getControl(ExposureControl.class);
        if (exposureControl != null) {
          long l = exposureControl.getMaxExposure(paramTimeUnit);
          if (!isUnknownExposure(l))
            l1 = Math.min(l1, l); 
        } 
      } 
    } 
    long l2 = l1;
    if (l1 == Long.MAX_VALUE)
      l2 = 0L; 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_6} */
    return l2;
  }
  
  protected long aggregatedMinExposure(TimeUnit paramTimeUnit) {
    long l1;
    synchronized (this.lock) {
      Iterator<SwitchableMemberInfo> iterator = this.switchableCamera.cameraInfos.values().iterator();
      l1 = Long.MIN_VALUE;
      while (iterator.hasNext()) {
        ExposureControl exposureControl = ((SwitchableMemberInfo)iterator.next()).<ExposureControl>getControl(ExposureControl.class);
        if (exposureControl != null) {
          long l = exposureControl.getMinExposure(paramTimeUnit);
          if (!isUnknownExposure(l))
            l1 = Math.max(l1, l); 
        } 
      } 
    } 
    long l2 = l1;
    if (l1 == Long.MIN_VALUE)
      l2 = 0L; 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_6} */
    return l2;
  }
  
  protected void initializeLimits() {
    for (ExposureControl.Mode mode : ExposureControl.Mode.values()) {
      if (mode != ExposureControl.Mode.Unknown)
        this.supportedModes.put(mode, Boolean.valueOf(aggregatedIsModeSupported(mode))); 
    } 
    this.nsMinExposure = aggregatedMinExposure(TimeUnit.NANOSECONDS);
    this.nsMaxExposure = aggregatedMaxExposure(TimeUnit.NANOSECONDS);
    this.isExposureSupported = aggregatedIsExposureSupported();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\SwitchableExposureControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */