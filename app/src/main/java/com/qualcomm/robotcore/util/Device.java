package com.qualcomm.robotcore.util;

import android.app.UiModeManager;
import android.os.Build;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import org.firstinspires.ftc.robotcore.internal.network.WifiUtil;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.SystemProperties;

public final class Device {
  private static final boolean DISABLE_FALLBACK_SERIAL_NUMBER_RETRIEVAL = false;
  
  public static final String MANUFACTURER_MOTOROLA = "motorola";
  
  public static final String MANUFACTURER_REV = "REV Robotics";
  
  public static final String MODEL_E4 = "Moto E (4)";
  
  public static final String MODEL_E5_PLAY = "moto e5 play";
  
  private static final String SERIAL_NUMBER_PROPERTY = "ro.serialno";
  
  private static final String SERIAL_NUMBER_RETRIEVAL_COMMAND = "getprop ro.serialno";
  
  public static final String TAG = "Device";
  
  private static String cachedSerialNumberOrUnknown;
  
  public static boolean deviceHasBackButton() {
    return (((UiModeManager)AppUtil.getDefContext().getSystemService("uimode")).getCurrentModeType() == 1);
  }
  
  public static String getSerialNumber() throws AndroidSerialNumberNotFoundException {
    if (cachedSerialNumberOrUnknown == null)
      try {
        cachedSerialNumberOrUnknown = internalGetSerialNumber();
      } catch (AndroidSerialNumberNotFoundException androidSerialNumberNotFoundException) {
        cachedSerialNumberOrUnknown = "unknown";
      }  
    if (!cachedSerialNumberOrUnknown.isEmpty() && !cachedSerialNumberOrUnknown.equals("unknown"))
      return cachedSerialNumberOrUnknown; 
    throw new AndroidSerialNumberNotFoundException();
  }
  
  public static String getSerialNumberOrUnknown() {
    try {
      return getSerialNumber();
    } catch (AndroidSerialNumberNotFoundException androidSerialNumberNotFoundException) {
      return "unknown";
    } 
  }
  
  private static String internalGetSerialNumber() throws AndroidSerialNumberNotFoundException {
    if (Build.VERSION.SDK_INT < 26) {
      str1 = Build.SERIAL;
    } else {
      try {
        str1 = Build.getSerial();
      } catch (SecurityException securityException) {
        str1 = "unknown";
      } 
    } 
    if (!str1.isEmpty() && !str1.equals("unknown"))
      return str1; 
    String str1 = SystemProperties.get("ro.serialno", "unknown");
    if (!str1.equals("unknown"))
      return str1; 
    RobotLog.ww("Device", "Failed to find Android serial number through Android API. Using fallback method.");
    RunShellCommand.ProcessResult processResult = (new RunShellCommand()).run("getprop ro.serialno");
    String str2 = processResult.getOutput().trim();
    if (processResult.getReturnCode() == 0 && !str2.isEmpty() && !str2.equals("unknown"))
      return str2; 
    throw new AndroidSerialNumberNotFoundException();
  }
  
  public static boolean isMotorola() {
    return Build.MANUFACTURER.equalsIgnoreCase("motorola");
  }
  
  public static boolean isMotorolaE4() {
    return (Build.MANUFACTURER.equalsIgnoreCase("motorola") && Build.MODEL.equalsIgnoreCase("Moto E (4)"));
  }
  
  public static boolean isMotorolaE5Play() {
    return (Build.MANUFACTURER.equalsIgnoreCase("motorola") && Build.MODEL.equalsIgnoreCase("moto e5 play"));
  }
  
  public static boolean isRevControlHub() {
    return LynxConstants.isRevControlHub();
  }
  
  public static boolean phoneImplementsAggressiveWifiScanning() {
    return (isMotorola() && WifiUtil.is5GHzAvailable());
  }
  
  public static boolean wifiP2pRemoteChannelChangeWorks() {
    return isRevControlHub() ^ true;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\Device.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */