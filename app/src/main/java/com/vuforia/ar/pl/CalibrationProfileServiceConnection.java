package com.vuforia.ar.pl;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.vuforia.eyewear.Calibration.service.ICalibrationProfileService;
import java.nio.charset.Charset;

public class CalibrationProfileServiceConnection {
  private static final ComponentName CPS_COMPONENT_NAME = new ComponentName("com.vuforia.eyewear.Calibration", "com.vuforia.eyewear.Calibration.service.CalibrationProfileService");
  
  private static final String SUBTAG = "CalibrationProfileServiceConn";
  
  private VuforiaServiceConnection mConnection = new VuforiaServiceConnection();
  
  public boolean bind(Context paramContext) {
    if (paramContext == null) {
      DebugLog.LOGD("CalibrationProfileServiceConn", "Activity is null");
      return false;
    } 
    return this.mConnection.bindService(paramContext, CPS_COMPONENT_NAME);
  }
  
  boolean clearProfile(int paramInt) {
    try {
      return getCalibrationProfileClient().clearProfile(paramInt);
    } catch (RemoteException remoteException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("clearProfile; Remote Exception");
      stringBuilder.append(remoteException.getCause());
      DebugLog.LOGD("CalibrationProfileServiceConn", stringBuilder.toString());
      return false;
    } 
  }
  
  int getActiveProfile() {
    try {
      return getCalibrationProfileClient().getActiveProfile();
    } catch (RemoteException remoteException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("getActiveProfile; Remote Exception");
      stringBuilder.append(remoteException.getCause());
      DebugLog.LOGD("CalibrationProfileServiceConn", stringBuilder.toString());
      return 0;
    } 
  }
  
  public ICalibrationProfileService getCalibrationProfileClient() {
    IBinder iBinder = this.mConnection.awaitService();
    if (iBinder == null) {
      DebugLog.LOGD("CalibrationProfileServiceConn", "getCalibrationProfileClient IBinder is null; returning null");
      return null;
    } 
    return ICalibrationProfileService.Stub.asInterface(iBinder);
  }
  
  float[] getCameraToEyePose(int paramInt1, int paramInt2) {
    try {
      return getCalibrationProfileClient().getCameraToEyePose(paramInt1, paramInt2);
    } catch (RemoteException remoteException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("getCameraToEyePose; Remote Exception");
      stringBuilder.append(remoteException.getCause());
      DebugLog.LOGD("CalibrationProfileServiceConn", stringBuilder.toString());
      return null;
    } 
  }
  
  float[] getEyeProjection(int paramInt1, int paramInt2) {
    try {
      return getCalibrationProfileClient().getEyeProjection(paramInt1, paramInt2);
    } catch (RemoteException remoteException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("getEyeProjection; Remote Exception");
      stringBuilder.append(remoteException.getCause());
      DebugLog.LOGD("CalibrationProfileServiceConn", stringBuilder.toString());
      return null;
    } 
  }
  
  int getMaxProfileCount() {
    try {
      return getCalibrationProfileClient().getMaxProfileCount();
    } catch (RemoteException remoteException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("getMaxProfileCount; Remote Exception");
      stringBuilder.append(remoteException.getCause());
      DebugLog.LOGD("CalibrationProfileServiceConn", stringBuilder.toString());
      return 0;
    } 
  }
  
  byte[] getProfileName(int paramInt) {
    try {
      return getCalibrationProfileClient().getProfileName(paramInt).getBytes(Charset.forName("UTF-16LE"));
    } catch (RemoteException remoteException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("getProfileName; Remote Exception");
      stringBuilder.append(remoteException.getCause());
      DebugLog.LOGD("CalibrationProfileServiceConn", stringBuilder.toString());
      return null;
    } 
  }
  
  int getUsedProfileCount() {
    try {
      return getCalibrationProfileClient().getUsedProfileCount();
    } catch (RemoteException remoteException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("getUsedProfileCount; Remote Exception");
      stringBuilder.append(remoteException.getCause());
      DebugLog.LOGD("CalibrationProfileServiceConn", stringBuilder.toString());
      return 0;
    } 
  }
  
  boolean isProfileUsed(int paramInt) {
    try {
      return getCalibrationProfileClient().isProfileUsed(paramInt);
    } catch (RemoteException remoteException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("isProfileUsed; Remote Exception");
      stringBuilder.append(remoteException.getCause());
      DebugLog.LOGD("CalibrationProfileServiceConn", stringBuilder.toString());
      return false;
    } 
  }
  
  boolean setActiveProfile(int paramInt) {
    try {
      return getCalibrationProfileClient().setActiveProfile(paramInt);
    } catch (RemoteException remoteException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("setActiveProfile; Remote Exception");
      stringBuilder.append(remoteException.getCause());
      DebugLog.LOGD("CalibrationProfileServiceConn", stringBuilder.toString());
      return false;
    } 
  }
  
  boolean setCameraToEyePose(int paramInt1, int paramInt2, float[] paramArrayOffloat) {
    try {
      return getCalibrationProfileClient().setCameraToEyePose(paramInt1, paramInt2, paramArrayOffloat);
    } catch (RemoteException remoteException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("setCameraToEyePose; Remote Exception");
      stringBuilder.append(remoteException.getCause());
      DebugLog.LOGD("CalibrationProfileServiceConn", stringBuilder.toString());
      return false;
    } 
  }
  
  boolean setEyeProjection(int paramInt1, int paramInt2, float[] paramArrayOffloat) {
    try {
      return getCalibrationProfileClient().setEyeProjection(paramInt1, paramInt2, paramArrayOffloat);
    } catch (RemoteException remoteException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("setEyeProjection; Remote Exception");
      stringBuilder.append(remoteException.getCause());
      DebugLog.LOGD("CalibrationProfileServiceConn", stringBuilder.toString());
      return false;
    } 
  }
  
  boolean setProfileName(int paramInt, byte[] paramArrayOfbyte) {
    try {
      return getCalibrationProfileClient().setProfileName(paramInt, new String(paramArrayOfbyte, Charset.forName("UTF-16LE")));
    } catch (RemoteException remoteException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("setProfileName; Remote Exception");
      stringBuilder.append(remoteException.getCause());
      DebugLog.LOGD("CalibrationProfileServiceConn", stringBuilder.toString());
      return false;
    } 
  }
  
  public boolean unbind(Context paramContext) {
    if (paramContext == null) {
      DebugLog.LOGD("CalibrationProfileServiceConn", "Activity is null");
      return false;
    } 
    return this.mConnection.unbindService(paramContext);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\CalibrationProfileServiceConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */