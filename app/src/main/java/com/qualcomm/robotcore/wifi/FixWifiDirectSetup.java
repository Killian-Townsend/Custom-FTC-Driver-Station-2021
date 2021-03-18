package com.qualcomm.robotcore.wifi;

import android.net.wifi.WifiManager;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.internal.network.PreferenceRemoterDS;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class FixWifiDirectSetup {
  public static final int WIFI_TOGGLE_DELAY = 2000;
  
  public static void disableWifiDirect(WifiManager paramWifiManager) throws InterruptedException {
    toggleWifi(false, paramWifiManager);
  }
  
  public static void fixWifiDirectSetup(WifiManager paramWifiManager) throws InterruptedException {
    toggleWifi(false, paramWifiManager);
    toggleWifi(true, paramWifiManager);
  }
  
  private static void toggleWifi(boolean paramBoolean, WifiManager paramWifiManager) throws InterruptedException {
    String str;
    if (paramBoolean) {
      str = "on";
    } else {
      str = "off";
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Toggling Wifi ");
    stringBuilder.append(str);
    RobotLog.i(stringBuilder.toString());
    paramWifiManager.setWifiEnabled(paramBoolean);
    if (AppUtil.getInstance().isDriverStation())
      PreferenceRemoterDS.getInstance().onWifiToggled(paramBoolean); 
    Thread.sleep(2000L);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\wifi\FixWifiDirectSetup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */