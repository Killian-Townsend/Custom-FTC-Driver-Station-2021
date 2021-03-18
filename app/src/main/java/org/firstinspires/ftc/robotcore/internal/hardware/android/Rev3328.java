package org.firstinspires.ftc.robotcore.internal.hardware.android;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.RunShellCommand;
import java.io.File;

public class Rev3328 extends AndroidBoard {
  private static final DigitalChannel ANDROID_BOARD_IS_PRESENT_PIN = new GpioPin(50, true, GpioPin.Active.LOW, "AndroidBoardIsPresentPin");
  
  private static final DigitalChannel LYNX_MODULE_RESET_PIN;
  
  private static final int OS_1_1_0_VERSION_NUM = 3;
  
  private static final int OS_1_1_1_VERSION_NUM = 4;
  
  private static final int OS_1_1_2_VERSION_NUM = 5;
  
  private static final DigitalChannel PROGRAMMING_PIN;
  
  private static final String TAG = "Rev3328";
  
  private static final File UART_FILE;
  
  private static final DigitalChannel USER_BUTTON_PIN = new GpioPin(51, "UserButtonPin");
  
  static {
    PROGRAMMING_PIN = new GpioPin(66, false, GpioPin.Active.LOW, "ProgrammingPin");
    LYNX_MODULE_RESET_PIN = new GpioPin(87, false, GpioPin.Active.LOW, "LynxModuleResetPin");
    UART_FILE = new File("/dev/ttyS1");
  }
  
  public DigitalChannel getAndroidBoardIsPresentPin() {
    return ANDROID_BOARD_IS_PRESENT_PIN;
  }
  
  public String getDeviceType() {
    return "REV3328";
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
    if (LynxConstants.getControlHubOsVersionNum() < 3)
      return AndroidBoard.WifiDataRate.CCK_1Mb; 
    String str = (new RunShellCommand()).run("cat /sys/module/wlan/parameters/rev_beacon_rate").getOutput().trim();
    RealtekWifiDataRate realtekWifiDataRate = null;
    try {
      RealtekWifiDataRate realtekWifiDataRate1 = RealtekWifiDataRate.fromRawValue(Integer.parseInt(str));
      realtekWifiDataRate = realtekWifiDataRate1;
    } catch (RuntimeException runtimeException) {
      RobotLog.ee("Rev3328", runtimeException, "Error obtaining WiFi AP beacon rate");
    } 
    return (realtekWifiDataRate == null) ? AndroidBoard.WifiDataRate.UNKNOWN : realtekWifiDataRate.wifiDataRate;
  }
  
  public boolean hasControlHubUpdater() {
    return true;
  }
  
  public void setWifiApBeaconRate(AndroidBoard.WifiDataRate paramWifiDataRate) {
    StringBuilder stringBuilder1;
    if (LynxConstants.getControlHubOsVersionNum() < 4) {
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Unable to set the WiFi AP beacon rate on Control Hub OS version");
      stringBuilder1.append(LynxConstants.getControlHubOsVersion());
      RobotLog.ww("Rev3328", stringBuilder1.toString());
      RobotLog.ww("Rev3328", "Control Hub OS version 1.1.1 or higher is required for this feature.");
      return;
    } 
    int i = (RealtekWifiDataRate.fromWifiDataRate((AndroidBoard.WifiDataRate)stringBuilder1)).rawValue;
    RunShellCommand runShellCommand = new RunShellCommand();
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("echo ");
    stringBuilder2.append(i);
    stringBuilder2.append(" > /sys/module/wlan/parameters/rev_beacon_rate");
    runShellCommand.run(stringBuilder2.toString());
  }
  
  public boolean supports5GhzAp() {
    return true;
  }
  
  public boolean supports5GhzAutoSelection() {
    return (LynxConstants.getControlHubOsVersionNum() >= 5);
  }
  
  public boolean supportsBulkNetworkSettings() {
    return (LynxConstants.getControlHubOsVersionNum() >= 5);
  }
  
  public boolean supportsGetChannelInfoIntent() {
    return (LynxConstants.getControlHubOsVersionNum() >= 5);
  }
  
  private enum RealtekWifiDataRate {
    RTK_CCK_11Mb,
    RTK_CCK_1Mb(2, AndroidBoard.WifiDataRate.CCK_1Mb),
    RTK_CCK_2Mb(4, AndroidBoard.WifiDataRate.CCK_2Mb),
    RTK_CCK_5Mb(11, AndroidBoard.WifiDataRate.CCK_5Mb),
    RTK_OFDM_12Mb(11, AndroidBoard.WifiDataRate.CCK_5Mb),
    RTK_OFDM_18Mb(11, AndroidBoard.WifiDataRate.CCK_5Mb),
    RTK_OFDM_24Mb(11, AndroidBoard.WifiDataRate.CCK_5Mb),
    RTK_OFDM_36Mb(11, AndroidBoard.WifiDataRate.CCK_5Mb),
    RTK_OFDM_48Mb(11, AndroidBoard.WifiDataRate.CCK_5Mb),
    RTK_OFDM_54Mb(11, AndroidBoard.WifiDataRate.CCK_5Mb),
    RTK_OFDM_6Mb(11, AndroidBoard.WifiDataRate.CCK_5Mb),
    RTK_OFDM_9Mb(11, AndroidBoard.WifiDataRate.CCK_5Mb);
    
    private final int rawValue;
    
    private final AndroidBoard.WifiDataRate wifiDataRate;
    
    static {
      RTK_OFDM_12Mb = new RealtekWifiDataRate("RTK_OFDM_12Mb", 6, 24, AndroidBoard.WifiDataRate.OFDM_12Mb);
      RTK_OFDM_18Mb = new RealtekWifiDataRate("RTK_OFDM_18Mb", 7, 36, AndroidBoard.WifiDataRate.OFDM_18Mb);
      RTK_OFDM_24Mb = new RealtekWifiDataRate("RTK_OFDM_24Mb", 8, 48, AndroidBoard.WifiDataRate.OFDM_24Mb);
      RTK_OFDM_36Mb = new RealtekWifiDataRate("RTK_OFDM_36Mb", 9, 72, AndroidBoard.WifiDataRate.OFDM_36Mb);
      RTK_OFDM_48Mb = new RealtekWifiDataRate("RTK_OFDM_48Mb", 10, 96, AndroidBoard.WifiDataRate.OFDM_48Mb);
      RealtekWifiDataRate realtekWifiDataRate = new RealtekWifiDataRate("RTK_OFDM_54Mb", 11, 108, AndroidBoard.WifiDataRate.OFDM_54Mb);
      RTK_OFDM_54Mb = realtekWifiDataRate;
      $VALUES = new RealtekWifiDataRate[] { 
          RTK_CCK_1Mb, RTK_CCK_2Mb, RTK_CCK_5Mb, RTK_CCK_11Mb, RTK_OFDM_6Mb, RTK_OFDM_9Mb, RTK_OFDM_12Mb, RTK_OFDM_18Mb, RTK_OFDM_24Mb, RTK_OFDM_36Mb, 
          RTK_OFDM_48Mb, realtekWifiDataRate };
    }
    
    RealtekWifiDataRate(int param1Int1, AndroidBoard.WifiDataRate param1WifiDataRate) {
      this.rawValue = param1Int1;
      this.wifiDataRate = param1WifiDataRate;
    }
    
    public static RealtekWifiDataRate fromRawValue(int param1Int) {
      for (RealtekWifiDataRate realtekWifiDataRate : values()) {
        if (realtekWifiDataRate.rawValue == param1Int)
          return realtekWifiDataRate; 
      } 
      return null;
    }
    
    public static RealtekWifiDataRate fromWifiDataRate(AndroidBoard.WifiDataRate param1WifiDataRate) {
      for (RealtekWifiDataRate realtekWifiDataRate : values()) {
        if (realtekWifiDataRate.wifiDataRate == param1WifiDataRate)
          return realtekWifiDataRate; 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unsupported data rate for Realtek WiFi: ");
      stringBuilder.append(param1WifiDataRate);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\hardware\android\Rev3328.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */