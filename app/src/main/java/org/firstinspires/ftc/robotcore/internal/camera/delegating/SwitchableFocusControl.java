package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import java.util.Iterator;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.FocusControl;

public class SwitchableFocusControl extends CachingFocusControl {
  protected final RefCountedSwitchableCameraImpl switchableCamera;
  
  public SwitchableFocusControl(RefCountedSwitchableCameraImpl paramRefCountedSwitchableCameraImpl) {
    this.switchableCamera = paramRefCountedSwitchableCameraImpl;
  }
  
  protected boolean aggregatedIsFocusSupported() {
    synchronized (this.lock) {
      Iterator<SwitchableMemberInfo> iterator = this.switchableCamera.cameraInfos.values().iterator();
      while (iterator.hasNext()) {
        FocusControl focusControl = ((SwitchableMemberInfo)iterator.next()).<FocusControl>getControl(FocusControl.class);
        if (focusControl != null && !focusControl.isFocusLengthSupported())
          return false; 
      } 
      return true;
    } 
  }
  
  protected boolean aggregatedIsModeSupported(FocusControl.Mode paramMode) {
    synchronized (this.lock) {
      Iterator<SwitchableMemberInfo> iterator = this.switchableCamera.cameraInfos.values().iterator();
      while (iterator.hasNext()) {
        FocusControl focusControl = ((SwitchableMemberInfo)iterator.next()).<FocusControl>getControl(FocusControl.class);
        if (focusControl != null && !focusControl.isModeSupported(paramMode))
          return false; 
      } 
      return true;
    } 
  }
  
  protected double aggregatedMaxFocus() {
    double d1;
    synchronized (this.lock) {
      Iterator<SwitchableMemberInfo> iterator = this.switchableCamera.cameraInfos.values().iterator();
      d1 = Double.MAX_VALUE;
      while (iterator.hasNext()) {
        FocusControl focusControl = ((SwitchableMemberInfo)iterator.next()).<FocusControl>getControl(FocusControl.class);
        if (focusControl != null) {
          double d = focusControl.getMaxFocusLength();
          if (!isUnknownFocusLength(d))
            d1 = Math.min(d1, d); 
        } 
      } 
    } 
    double d2 = d1;
    if (d1 == Double.MAX_VALUE)
      d2 = -1.0D; 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
    return d2;
  }
  
  protected double aggregatedMinFocus() {
    double d1;
    synchronized (this.lock) {
      Iterator<SwitchableMemberInfo> iterator = this.switchableCamera.cameraInfos.values().iterator();
      d1 = Double.MIN_VALUE;
      while (iterator.hasNext()) {
        FocusControl focusControl = ((SwitchableMemberInfo)iterator.next()).<FocusControl>getControl(FocusControl.class);
        if (focusControl != null) {
          double d = focusControl.getMinFocusLength();
          if (!isUnknownFocusLength(d))
            d1 = Math.max(d1, d); 
        } 
      } 
    } 
    double d2 = d1;
    if (d1 == Double.MIN_VALUE)
      d2 = -1.0D; 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
    return d2;
  }
  
  protected void initializeLimits() {
    for (FocusControl.Mode mode : FocusControl.Mode.values()) {
      if (mode != FocusControl.Mode.Unknown)
        this.supportedModes.put(mode, Boolean.valueOf(aggregatedIsModeSupported(mode))); 
    } 
    this.minFocusLength = aggregatedMinFocus();
    this.maxFocusLength = aggregatedMaxFocus();
    this.isFocusLengthSupported = aggregatedIsFocusSupported();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\SwitchableFocusControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */