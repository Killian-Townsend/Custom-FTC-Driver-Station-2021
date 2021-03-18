package org.firstinspires.ftc.robotcore.internal.network;

public interface DeviceNameManager {
  String getDeviceName();
  
  void initializeDeviceNameIfNecessary();
  
  void registerCallback(DeviceNameListener paramDeviceNameListener);
  
  String resetDeviceName(boolean paramBoolean);
  
  void setDeviceName(String paramString, boolean paramBoolean) throws InvalidNetworkSettingException;
  
  boolean start(StartResult paramStartResult);
  
  void stop(StartResult paramStartResult);
  
  void unregisterCallback(DeviceNameListener paramDeviceNameListener);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\DeviceNameManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */