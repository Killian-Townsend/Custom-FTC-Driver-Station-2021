package org.firstinspires.ftc.robotcore.internal.camera;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;
import java.util.concurrent.Executor;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Function;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.LibUsbDevice;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcDevice;
import org.firstinspires.ftc.robotcore.internal.collections.MutableReference;
import org.firstinspires.ftc.robotcore.internal.hardware.usb.ArmableUsbDevice;

public interface CameraManagerInternal extends CameraManager {
  public static final boolean forceJavaUsbEnumerationKitKat = true;
  
  void enumerateAttachedSerialNumbers(Consumer<SerialNumber> paramConsumer);
  
  UvcDevice findUvcDevice(WebcamName paramWebcamName);
  
  List<LibUsbDevice> getMatchingLibUsbDevices(Function<SerialNumber, Boolean> paramFunction);
  
  SerialNumber getRealOrVendorProductSerialNumber(UsbDevice paramUsbDevice);
  
  Executor getSerialThreadPool();
  
  UsbManager getUsbManager();
  
  boolean isWebcamAttached(SerialNumber paramSerialNumber);
  
  BuiltinCameraName nameFromCameraDirection(VuforiaLocalizer.CameraDirection paramCameraDirection);
  
  void registerReceiver(UsbAttachmentCallback paramUsbAttachmentCallback);
  
  void unregisterReceiver(UsbAttachmentCallback paramUsbAttachmentCallback);
  
  WebcamName webcamNameFromDevice(UsbDevice paramUsbDevice);
  
  WebcamName webcamNameFromDevice(LibUsbDevice paramLibUsbDevice);
  
  WebcamName webcamNameFromSerialNumber(SerialNumber paramSerialNumber, ArmableUsbDevice.OpenRobotUsbDevice paramOpenRobotUsbDevice, SyncdDevice.Manager paramManager);
  
  public static interface UsbAttachmentCallback {
    void onAttached(UsbDevice param1UsbDevice, SerialNumber param1SerialNumber, MutableReference<Boolean> param1MutableReference);
    
    void onDetached(UsbDevice param1UsbDevice);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\CameraManagerInternal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */