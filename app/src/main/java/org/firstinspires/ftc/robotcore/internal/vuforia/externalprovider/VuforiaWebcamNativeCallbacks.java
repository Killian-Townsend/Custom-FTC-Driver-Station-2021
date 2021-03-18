package org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider;

public interface VuforiaWebcamNativeCallbacks {
  boolean nativeCallbackClose();
  
  long nativeCallbackGetExposure();
  
  int nativeCallbackGetExposureMode();
  
  double nativeCallbackGetFocusLength();
  
  int nativeCallbackGetFocusMode();
  
  long nativeCallbackGetMaxExposure();
  
  double nativeCallbackGetMaxFocusLength();
  
  long nativeCallbackGetMinExposure();
  
  double nativeCallbackGetMinFocusLength();
  
  int nativeCallbackGetNumSupportedCameraModes();
  
  int[] nativeCallbackGetSupportedCameraMode(int paramInt);
  
  boolean nativeCallbackIsExposureModeSupported(int paramInt);
  
  boolean nativeCallbackIsExposureSupported();
  
  boolean nativeCallbackIsFocusLengthSupported();
  
  boolean nativeCallbackIsFocusModeSupported(int paramInt);
  
  boolean nativeCallbackOpen();
  
  boolean nativeCallbackSetExposure(long paramLong);
  
  boolean nativeCallbackSetExposureMode(int paramInt);
  
  boolean nativeCallbackSetFocusLength(double paramDouble);
  
  boolean nativeCallbackSetFocusMode(int paramInt);
  
  boolean nativeCallbackStart(int[] paramArrayOfint, long paramLong);
  
  boolean nativeCallbackStop();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\externalprovider\VuforiaWebcamNativeCallbacks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */