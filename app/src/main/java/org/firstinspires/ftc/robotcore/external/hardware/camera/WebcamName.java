package org.firstinspires.ftc.robotcore.external.hardware.camera;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.util.SerialNumber;

public interface WebcamName extends CameraName, HardwareDevice {
  SerialNumber getSerialNumber();
  
  String getUsbDeviceNameIfAttached();
  
  boolean isAttached();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\hardware\camera\WebcamName.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */