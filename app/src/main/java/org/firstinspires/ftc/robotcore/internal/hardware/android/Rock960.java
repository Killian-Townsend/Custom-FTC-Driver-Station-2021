package org.firstinspires.ftc.robotcore.internal.hardware.android;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;

public class Rock960 extends AndroidBoard {
  private static final DigitalChannel ANDROID_BOARD_IS_PRESENT_PIN = new GpioPin(1102, true, GpioPin.Active.LOW, "AndroidBoardIsPresentPin");
  
  private static final DigitalChannel LYNX_MODULE_RESET_PIN = new GpioPin(1041, false, GpioPin.Active.LOW, "LynxModuleResetPin");
  
  private static final DigitalChannel PROGRAMMING_PIN = new GpioPin(1006, false, GpioPin.Active.LOW, "ProgrammingPin");
  
  private static final String TAG = "Rock960";
  
  private static final File UART_FILE;
  
  private static final DigitalChannel USER_BUTTON_PIN = new GpioPin(1100, "UserButtonPin");
  
  static {
    UART_FILE = new File("/dev/ttyS3");
  }
  
  public DigitalChannel getAndroidBoardIsPresentPin() {
    return ANDROID_BOARD_IS_PRESENT_PIN;
  }
  
  public String getDeviceType() {
    return "Rock960";
  }
  
  public DigitalChannel getLynxModuleResetPin() {
    return LYNX_MODULE_RESET_PIN;
  }
  
  public DigitalChannel getProgrammingPin() {
    return PROGRAMMING_PIN;
  }
  
  public File getUartLocation() {
    return UART_FILE;
  }
  
  public DigitalChannel getUserButtonPin() {
    return USER_BUTTON_PIN;
  }
  
  public AndroidBoard.WifiDataRate getWifiApBeaconRate() {
    return AndroidBoard.WifiDataRate.UNKNOWN;
  }
  
  public boolean hasControlHubUpdater() {
    return false;
  }
  
  public void setWifiApBeaconRate(AndroidBoard.WifiDataRate paramWifiDataRate) {
    RobotLog.ww("Rock960", "Unable to set the WiFi AP beacon rate on a Rock960");
  }
  
  public boolean supports5GhzAp() {
    return true;
  }
  
  public boolean supports5GhzAutoSelection() {
    return false;
  }
  
  public boolean supportsBulkNetworkSettings() {
    return false;
  }
  
  public boolean supportsGetChannelInfoIntent() {
    return false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\hardware\android\Rock960.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */