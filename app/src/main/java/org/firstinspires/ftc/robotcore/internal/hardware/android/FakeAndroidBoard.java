package org.firstinspires.ftc.robotcore.internal.hardware.android;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;

public class FakeAndroidBoard extends AndroidBoard {
  private static final String TAG = "FakeAndroidBoard";
  
  public DigitalChannel getAndroidBoardIsPresentPin() {
    return new FakeDigitalChannel(DigitalChannel.Mode.OUTPUT);
  }
  
  public String getDeviceType() {
    return "Fake Android board";
  }
  
  public DigitalChannel getLynxModuleResetPin() {
    return new FakeDigitalChannel(DigitalChannel.Mode.OUTPUT);
  }
  
  public DigitalChannel getProgrammingPin() {
    return new FakeDigitalChannel(DigitalChannel.Mode.OUTPUT);
  }
  
  public File getUartLocation() {
    return new File("/dev/null");
  }
  
  public DigitalChannel getUserButtonPin() {
    return new FakeDigitalChannel(DigitalChannel.Mode.INPUT);
  }
  
  public AndroidBoard.WifiDataRate getWifiApBeaconRate() {
    return AndroidBoard.WifiDataRate.UNKNOWN;
  }
  
  public boolean hasControlHubUpdater() {
    return false;
  }
  
  public void setWifiApBeaconRate(AndroidBoard.WifiDataRate paramWifiDataRate) {
    RobotLog.ww("FakeAndroidBoard", "This is not a known type of Control Hub; unable to set the WiFi AP beacon rate");
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
  
  private static class FakeDigitalChannel implements DigitalChannel {
    DigitalChannel.Mode mode;
    
    public FakeDigitalChannel(DigitalChannel.Mode param1Mode) {
      this.mode = param1Mode;
    }
    
    public void close() {}
    
    public String getConnectionInfo() {
      return "";
    }
    
    public String getDeviceName() {
      return "Fake Digital Channel";
    }
    
    public HardwareDevice.Manufacturer getManufacturer() {
      return HardwareDevice.Manufacturer.Other;
    }
    
    public DigitalChannel.Mode getMode() {
      return this.mode;
    }
    
    public boolean getState() {
      return false;
    }
    
    public int getVersion() {
      return 0;
    }
    
    public void resetDeviceConfigurationForOpMode() {}
    
    public void setMode(DigitalChannel.Mode param1Mode) {}
    
    public void setMode(DigitalChannelController.Mode param1Mode) {}
    
    public void setState(boolean param1Boolean) {}
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\hardware\android\FakeAndroidBoard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */