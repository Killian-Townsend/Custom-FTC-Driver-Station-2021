package org.firstinspires.inspection;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.Device;
import com.qualcomm.robotcore.wifi.NetworkType;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.hardware.CachedLynxFirmwareVersions;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManager;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.PasswordManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.StartResult;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectAgent;
import org.firstinspires.ftc.robotcore.internal.network.WifiUtil;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class InspectionState {
  public static final String NO_VERSION = "";
  
  public static final int NO_VERSION_CODE = 0;
  
  public static final String driverStationPackage = "com.qualcomm.ftcdriverstation";
  
  public static final String robotControllerPackage = "com.qualcomm.ftcrobotcontroller";
  
  public boolean airplaneModeOn;
  
  public double batteryFraction;
  
  public boolean bluetoothOn;
  
  public long bytesPerSecond;
  
  public boolean channelChangerRequired = false;
  
  public String controlHubOsVersion = "";
  
  public int controlHubOsVersionNum;
  
  public String deviceName;
  
  public String driverStationVersion;
  
  public int driverStationVersionCode;
  
  public String firmwareVersion;
  
  public boolean isAppInventorInstalled = false;
  
  public boolean isDefaultPassword;
  
  public String manufacturer;
  
  public String model;
  
  public String osVersion;
  
  public String robotControllerVersion;
  
  public int robotControllerVersionCode;
  
  public long rxDataCount;
  
  public int sdkInt;
  
  public long txDataCount;
  
  public boolean wifiConnected;
  
  public boolean wifiDirectConnected;
  
  public boolean wifiDirectEnabled;
  
  public boolean wifiEnabled;
  
  public int ztcChannelChangeVersionCode = 0;
  
  public String zteChannelChangeVersion = "";
  
  public static InspectionState deserialize(String paramString) {
    return (InspectionState)SimpleGson.getInstance().fromJson(paramString, InspectionState.class);
  }
  
  private static String getFirmwareInspectionVersions() {
    List list = CachedLynxFirmwareVersions.getFormattedVersions();
    if (list == null || list.isEmpty())
      return "N/A"; 
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < list.size(); i++) {
      stringBuilder.append(String.format("[%s] %s", new Object[] { ((CachedLynxFirmwareVersions.LynxModuleInfo)list.get(i)).name, ((CachedLynxFirmwareVersions.LynxModuleInfo)list.get(i)).firmwareVersion }));
      if (i < list.size() - 1)
        stringBuilder.append("\n"); 
    } 
    return stringBuilder.toString();
  }
  
  public static boolean isPackageInstalled(String paramString) {
    return paramString.equals("") ^ true;
  }
  
  protected double getLocalBatteryFraction() {
    IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
    Intent intent = AppUtil.getInstance().getApplication().registerReceiver(null, intentFilter);
    int i = intent.getIntExtra("level", -1);
    int j = intent.getIntExtra("scale", -1);
    return i / j;
  }
  
  protected String getPackageVersion(String paramString) {
    PackageManager packageManager = AppUtil.getDefContext().getPackageManager();
    try {
      return (packageManager.getPackageInfo(paramString, 128)).versionName;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      return "";
    } 
  }
  
  protected int getPackageVersionCode(String paramString) {
    PackageManager packageManager = AppUtil.getDefContext().getPackageManager();
    try {
      return (packageManager.getPackageInfo(paramString, 128)).versionCode;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      return 0;
    } 
  }
  
  public void initializeLocal() {
    DeviceNameManager deviceNameManager = DeviceNameManagerFactory.getInstance();
    StartResult startResult = new StartResult();
    deviceNameManager.start(startResult);
    initializeLocal(deviceNameManager);
    deviceNameManager.stop(startResult);
  }
  
  public void initializeLocal(DeviceNameManager paramDeviceNameManager) {
    this.manufacturer = Build.MANUFACTURER;
    this.model = Build.MODEL;
    this.osVersion = Build.VERSION.RELEASE;
    this.firmwareVersion = getFirmwareInspectionVersions();
    this.sdkInt = Build.VERSION.SDK_INT;
    this.airplaneModeOn = WifiUtil.isAirplaneModeOn();
    this.bluetoothOn = WifiUtil.isBluetoothOn();
    this.wifiEnabled = WifiUtil.isWifiEnabled();
    this.batteryFraction = getLocalBatteryFraction();
    this.robotControllerVersion = getPackageVersion("com.qualcomm.ftcrobotcontroller");
    this.robotControllerVersionCode = getPackageVersionCode("com.qualcomm.ftcrobotcontroller");
    this.driverStationVersion = getPackageVersion("com.qualcomm.ftcdriverstation");
    this.driverStationVersionCode = getPackageVersionCode("com.qualcomm.ftcdriverstation");
    this.deviceName = paramDeviceNameManager.getDeviceName();
    NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
    NetworkType networkType = networkConnectionHandler.getNetworkType();
    if (networkType == NetworkType.WIRELESSAP || networkType == NetworkType.RCWIRELESSAP) {
      if (Device.isRevControlHub()) {
        String str = LynxConstants.getControlHubOsVersion();
        this.controlHubOsVersion = str;
        if (str == null)
          this.controlHubOsVersion = "unknown"; 
        this.controlHubOsVersionNum = LynxConstants.getControlHubOsVersionNum();
        this.isDefaultPassword = PasswordManagerFactory.getInstance().isDefault();
        boolean bool = WifiUtil.isWifiApEnabled();
        this.wifiEnabled = bool;
        if (bool)
          this.wifiConnected = true; 
      } else {
        this.controlHubOsVersion = "";
        this.controlHubOsVersionNum = 0;
        this.deviceName = WifiUtil.getConnectedSsid();
        this.wifiDirectEnabled = WifiUtil.isWifiEnabled();
        this.wifiConnected = WifiUtil.isWifiConnected();
      } 
      this.wifiDirectConnected = false;
    } else {
      this.wifiConnected = WifiDirectAgent.getInstance().isWifiConnected();
      this.wifiDirectEnabled = WifiDirectAgent.getInstance().isWifiDirectEnabled();
      this.wifiDirectConnected = WifiDirectAgent.getInstance().isWifiDirectConnected();
    } 
    this.rxDataCount = networkConnectionHandler.getRxDataCount();
    this.txDataCount = networkConnectionHandler.getTxDataCount();
    this.bytesPerSecond = networkConnectionHandler.getBytesPerSecond();
  }
  
  public boolean isDriverStationInstalled() {
    return isPackageInstalled(this.driverStationVersion);
  }
  
  public boolean isRobotControllerInstalled() {
    return isPackageInstalled(this.robotControllerVersion);
  }
  
  public String serialize() {
    return SimpleGson.getInstance().toJson(this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\inspection\InspectionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */