package org.firstinspires.ftc.robotcore.internal.network;

import com.qualcomm.robotcore.util.Device;

public class DeviceNameManagerFactory {
  public static DeviceNameManager getInstance() {
    return InstanceHolder.theInstance;
  }
  
  protected static class InstanceHolder {
    public static final DeviceNameManager theInstance;
    
    static {
      WifiDirectDeviceNameManager wifiDirectDeviceNameManager;
      if (Device.isRevControlHub()) {
        ControlHubDeviceNameManager controlHubDeviceNameManager = new ControlHubDeviceNameManager();
      } else {
        wifiDirectDeviceNameManager = new WifiDirectDeviceNameManager();
      } 
      theInstance = wifiDirectDeviceNameManager;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\DeviceNameManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */