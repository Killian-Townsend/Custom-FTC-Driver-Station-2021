package org.firstinspires.ftc.robotcore.internal.hardware.android;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;

public class Dragonboard extends AndroidBoard {
  private static final DigitalChannel ANDROID_BOARD_IS_PRESENT_PIN = new GpioPin(921, true, GpioPin.Active.LOW, "AndroidBoardIsPresentPin");
  
  private static final DigitalChannel LYNX_MODULE_RESET_PIN = new GpioPin(915, false, GpioPin.Active.LOW, "LynxModuleResetPin");
  
  private static final DigitalChannel PROGRAMMING_PIN = new GpioPin(938, false, GpioPin.Active.LOW, "ProgrammingPin");
  
  private static final String TAG = "Dragonboard";
  
  private static final File UART_FILE;
  
  private static final DigitalChannel USER_BUTTON_PIN = new GpioPin(919, "UserButtonPin");
  
  static {
    UART_FILE = findSerialDevTty();
  }
  
  private static File findSerialDevTty() {
    File file = new File("/dev/ttyHS4");
    if (file.exists())
      return file; 
    for (int i = 0; i <= 255; i++) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("/dev/ttyHS");
      stringBuilder.append(i);
      File file1 = new File(stringBuilder.toString());
      if (file1.exists())
        return file1; 
    } 
    throw new RuntimeException("unable to locate UART communication file for Dragonboard /dev/ttyHSx");
  }
  
  public DigitalChannel getAndroidBoardIsPresentPin() {
    return ANDROID_BOARD_IS_PRESENT_PIN;
  }
  
  public String getDeviceType() {
    return "Dragonboard 410c";
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
    RobotLog.ww("Dragonboard", "Unable to set the WiFi AP beacon rate on a Dragonboard");
  }
  
  public boolean supports5GhzAp() {
    return false;
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


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\hardware\android\Dragonboard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */