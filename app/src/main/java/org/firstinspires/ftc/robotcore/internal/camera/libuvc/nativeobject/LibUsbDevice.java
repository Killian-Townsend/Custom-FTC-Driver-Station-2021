package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.text.TextUtils;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.TypeConversion;
import java.io.File;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.NativeObject;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;
import org.firstinspires.ftc.robotcore.internal.usb.UsbSerialNumber;

public class LibUsbDevice extends NativeObject {
  public static final String TAG = LibUsbDevice.class.getSimpleName();
  
  private UsbDevice javaUsbDevice;
  
  protected final boolean traceEnabled;
  
  public LibUsbDevice(long paramLong, UsbDevice paramUsbDevice) {
    this(paramLong, paramUsbDevice, true);
  }
  
  public LibUsbDevice(long paramLong, UsbDevice paramUsbDevice, boolean paramBoolean) {
    super(paramLong, traceLevel);
    RefCounted.TraceLevel traceLevel;
    this.traceEnabled = paramBoolean;
    this.javaUsbDevice = paramUsbDevice;
  }
  
  protected static native void nativeAddRefDevice(long paramLong);
  
  protected static native byte nativeGetBusNumber(long paramLong);
  
  protected static native byte nativeGetDeviceAddress(long paramLong);
  
  protected static native byte nativeGetPortNumber(long paramLong);
  
  protected static native int nativeGetProductId(long paramLong);
  
  protected static native String nativeGetSerialNumber(long paramLong, boolean paramBoolean);
  
  protected static native String nativeGetSysfs(long paramLong);
  
  protected static native int nativeGetVendorId(long paramLong);
  
  protected static native void nativeReleaseRefDevice(long paramLong, boolean paramBoolean);
  
  protected void destructor() {
    if (this.pointer != 0L) {
      nativeReleaseRefDevice(this.pointer, this.traceEnabled);
      clearPointer();
    } 
    super.destructor();
  }
  
  public int getBusNumber() {
    return TypeConversion.unsignedByteToInt(nativeGetBusNumber(this.pointer));
  }
  
  public int getDeviceAddress() {
    return TypeConversion.unsignedByteToInt(nativeGetDeviceAddress(this.pointer));
  }
  
  public long getPointer() {
    return this.pointer;
  }
  
  public int getPortNumber() {
    return TypeConversion.unsignedByteToInt(nativeGetPortNumber(this.pointer));
  }
  
  public SerialNumber getRealOrVendorProductSerialNumber() {
    String str = nativeGetSerialNumber(this.pointer, this.traceEnabled);
    return UsbSerialNumber.isValidUsbSerialNumber(str) ? SerialNumber.fromString(str) : SerialNumber.fromVidPid(this.javaUsbDevice.getVendorId(), this.javaUsbDevice.getProductId(), getUsbConnectionPath());
  }
  
  public String getTag() {
    return TAG;
  }
  
  public String getUsbConnectionPath() {
    String str = nativeGetSysfs(this.pointer);
    if (!TextUtils.isEmpty(str))
      return (new File(str)).getName(); 
    if (Build.VERSION.SDK_INT < 24)
      RobotLog.ee(TAG, "unable to find USB connection path for %s", new Object[] { getUsbDeviceName() }); 
    return "";
  }
  
  public String getUsbDeviceName() {
    String str = AppUtil.getInstance().getUsbFileSystemRoot();
    if (str != null)
      return Misc.formatInvariant("%s/%03d/%03d", new Object[] { str, Integer.valueOf(getBusNumber()), Integer.valueOf(getDeviceAddress()) }); 
    throw new IllegalStateException("root of usbfs not known");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\LibUsbDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */