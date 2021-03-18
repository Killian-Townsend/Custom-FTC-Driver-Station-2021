package com.qualcomm.ftccommon;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

public class FtcEventLoopIdle extends FtcEventLoopBase {
  public static final String TAG = "FtcEventLoopIdle";
  
  public FtcEventLoopIdle(HardwareFactory paramHardwareFactory, OpModeRegister paramOpModeRegister, UpdateUI.Callback paramCallback, Activity paramActivity) {
    super(paramHardwareFactory, paramOpModeRegister, paramCallback, paramActivity);
  }
  
  public OpModeManagerImpl getOpModeManager() {
    return null;
  }
  
  public void handleUsbModuleAttach(RobotUsbModule paramRobotUsbModule) throws RobotCoreException, InterruptedException {}
  
  public void handleUsbModuleDetach(RobotUsbModule paramRobotUsbModule) throws RobotCoreException, InterruptedException {}
  
  public void init(EventLoopManager paramEventLoopManager) throws RobotCoreException, InterruptedException {
    RobotLog.ii("FtcEventLoopIdle", "------- idle init --------");
    try {
      super.init(paramEventLoopManager);
      return;
    } catch (Exception exception) {
      RobotLog.vv("FtcEventLoopIdle", exception, "exception in idle event loop init; ignored");
      return;
    } 
  }
  
  public void loop() throws RobotCoreException, InterruptedException {
    try {
      checkForChangedOpModes();
      return;
    } catch (Exception exception) {
      RobotLog.vv("FtcEventLoopIdle", exception, "exception in idle event loop loop; ignored");
      return;
    } 
  }
  
  public void onUsbDeviceAttached(UsbDevice paramUsbDevice) {}
  
  public void pendUsbDeviceAttachment(SerialNumber paramSerialNumber, long paramLong, TimeUnit paramTimeUnit) {}
  
  public void processedRecentlyAttachedUsbDevices() throws RobotCoreException, InterruptedException {}
  
  public void refreshUserTelemetry(TelemetryMessage paramTelemetryMessage, double paramDouble) {}
  
  public void requestOpModeStop(OpMode paramOpMode) {}
  
  public void teardown() throws RobotCoreException, InterruptedException {
    RobotLog.ii("FtcEventLoopIdle", "------- idle teardown ----");
    try {
      super.teardown();
      return;
    } catch (Exception exception) {
      RobotLog.vv("FtcEventLoopIdle", exception, "exception in idle event loop teardown; ignored");
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\FtcEventLoopIdle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */