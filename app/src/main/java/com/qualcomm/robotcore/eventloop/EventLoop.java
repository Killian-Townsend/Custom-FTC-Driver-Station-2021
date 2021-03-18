package com.qualcomm.robotcore.eventloop;

import android.hardware.usb.UsbDevice;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

public interface EventLoop {
  public static final double TELEMETRY_DEFAULT_INTERVAL = NaND;
  
  OpModeManagerImpl getOpModeManager();
  
  void handleUsbModuleAttach(RobotUsbModule paramRobotUsbModule) throws RobotCoreException, InterruptedException;
  
  void handleUsbModuleDetach(RobotUsbModule paramRobotUsbModule) throws RobotCoreException, InterruptedException;
  
  void init(EventLoopManager paramEventLoopManager) throws RobotCoreException, InterruptedException;
  
  void loop() throws RobotCoreException, InterruptedException;
  
  void onUsbDeviceAttached(UsbDevice paramUsbDevice);
  
  void pendUsbDeviceAttachment(SerialNumber paramSerialNumber, long paramLong, TimeUnit paramTimeUnit);
  
  CallbackResult processCommand(Command paramCommand) throws InterruptedException, RobotCoreException;
  
  void processedRecentlyAttachedUsbDevices() throws RobotCoreException, InterruptedException;
  
  void refreshUserTelemetry(TelemetryMessage paramTelemetryMessage, double paramDouble);
  
  void requestOpModeStop(OpMode paramOpMode);
  
  void teardown() throws RobotCoreException, InterruptedException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\eventloop\EventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */