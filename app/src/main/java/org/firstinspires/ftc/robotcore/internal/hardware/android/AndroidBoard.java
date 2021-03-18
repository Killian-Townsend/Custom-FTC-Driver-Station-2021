package org.firstinspires.ftc.robotcore.internal.hardware.android;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.Device;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public abstract class AndroidBoard {
  protected static final String ANDROID_BOARD_IS_PRESENT_PIN_NAME = "AndroidBoardIsPresentPin";
  
  protected static final String LYNX_MODULE_RESET_PIN_NAME = "LynxModuleResetPin";
  
  protected static final String PROGRAMMING_PIN_NAME = "ProgrammingPin";
  
  private static final String TAG = "AndroidBoard";
  
  protected static final String USER_BUTTON_PIN_NAME = "UserButtonPin";
  
  private static AndroidBoard androidBoard;
  
  public static AndroidBoard getInstance() {
    if (androidBoard == null)
      if (isRevControlHubv1()) {
        androidBoard = new Rev3328();
      } else if (isDragonboard()) {
        androidBoard = new Dragonboard();
      } else {
        androidBoard = new FakeAndroidBoard();
      }  
    return androidBoard;
  }
  
  private static boolean isDragonboard() {
    return (LynxConstants.getControlHubVersion() == 0);
  }
  
  private static boolean isRevControlHubv1() {
    return (LynxConstants.getControlHubVersion() == 1);
  }
  
  public static void showErrorIfUnknownControlHub() {
    if (Device.isRevControlHub()) {
      if (androidBoard == null)
        getInstance(); 
      if (androidBoard instanceof FakeAndroidBoard)
        RobotLog.setGlobalErrorMsg(AppUtil.getDefContext().getString(R.string.unknown_control_hub_error)); 
    } 
  }
  
  public abstract DigitalChannel getAndroidBoardIsPresentPin();
  
  public abstract String getDeviceType();
  
  public abstract DigitalChannel getLynxModuleResetPin();
  
  public abstract DigitalChannel getProgrammingPin();
  
  public abstract File getUartLocation();
  
  public abstract DigitalChannel getUserButtonPin();
  
  public abstract WifiDataRate getWifiApBeaconRate();
  
  public abstract boolean hasControlHubUpdater();
  
  public void logAndroidBoardInfo() {
    if (Device.isRevControlHub()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("REV Control Hub contains ");
      stringBuilder.append(getDeviceType());
      RobotLog.vv("AndroidBoard", stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("Communicating with embedded REV hub via ");
      stringBuilder.append(getUartLocation().getAbsolutePath());
      RobotLog.vv("AndroidBoard", stringBuilder.toString());
    } 
  }
  
  public abstract void setWifiApBeaconRate(WifiDataRate paramWifiDataRate);
  
  public abstract boolean supports5GhzAp();
  
  public abstract boolean supports5GhzAutoSelection();
  
  public abstract boolean supportsBulkNetworkSettings();
  
  public abstract boolean supportsGetChannelInfoIntent();
  
  public enum WifiDataRate {
    CCK_11Mb, CCK_1Mb, CCK_2Mb, CCK_5Mb, OFDM_12Mb, OFDM_18Mb, OFDM_24Mb, OFDM_36Mb, OFDM_48Mb, OFDM_54Mb, OFDM_6Mb, OFDM_9Mb, UNKNOWN;
    
    static {
      CCK_11Mb = new WifiDataRate("CCK_11Mb", 4);
      OFDM_6Mb = new WifiDataRate("OFDM_6Mb", 5);
      OFDM_9Mb = new WifiDataRate("OFDM_9Mb", 6);
      OFDM_12Mb = new WifiDataRate("OFDM_12Mb", 7);
      OFDM_18Mb = new WifiDataRate("OFDM_18Mb", 8);
      OFDM_24Mb = new WifiDataRate("OFDM_24Mb", 9);
      OFDM_36Mb = new WifiDataRate("OFDM_36Mb", 10);
      OFDM_48Mb = new WifiDataRate("OFDM_48Mb", 11);
      WifiDataRate wifiDataRate = new WifiDataRate("OFDM_54Mb", 12);
      OFDM_54Mb = wifiDataRate;
      $VALUES = new WifiDataRate[] { 
          UNKNOWN, CCK_1Mb, CCK_2Mb, CCK_5Mb, CCK_11Mb, OFDM_6Mb, OFDM_9Mb, OFDM_12Mb, OFDM_18Mb, OFDM_24Mb, 
          OFDM_36Mb, OFDM_48Mb, wifiDataRate };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\hardware\android\AndroidBoard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */