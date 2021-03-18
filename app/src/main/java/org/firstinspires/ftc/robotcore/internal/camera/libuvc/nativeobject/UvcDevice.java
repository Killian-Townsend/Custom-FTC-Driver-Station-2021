package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.function.Supplier;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.CameraManagerImpl;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants.UvcError;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.NativeObject;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public class UvcDevice extends NativeObject<UvcContext> {
  public static final String TAG = UvcDevice.class.getSimpleName();
  
  public static boolean TRACE = true;
  
  protected LibUsbDevice libUsbDevice;
  
  protected Tracer tracer = Tracer.create(TAG, TRACE);
  
  protected final UsbDevice usbDevice;
  
  protected UsbDeviceConnection usbDeviceConnection;
  
  protected UsbInterfaceManager usbInterfaceManager = new UsbInterfaceMangerImpl();
  
  protected WebcamName webcamName;
  
  public UvcDevice(long paramLong, UvcContext paramUvcContext, UsbDevice paramUsbDevice) throws IOException {
    super(paramLong);
    try {
      setParent((RefCounted)paramUvcContext);
      this.libUsbDevice = new LibUsbDevice(nativeGetLibUsbDevice(this.pointer), paramUsbDevice);
      UsbDevice usbDevice = paramUsbDevice;
      if (paramUsbDevice == null)
        usbDevice = findUsbDevice(); 
      this.usbDevice = usbDevice;
      this.webcamName = null;
      this.usbDeviceConnection = null;
      return;
    } catch (IOException iOException) {
    
    } catch (RuntimeException runtimeException) {}
    releaseRef();
    throw runtimeException;
  }
  
  protected static native long nativeGetContext(long paramLong);
  
  protected static native long nativeGetDeviceDescriptor(long paramLong);
  
  protected static native long nativeGetDeviceInfo(long paramLong);
  
  protected static native long nativeGetLibUsbDevice(long paramLong);
  
  protected static native boolean nativeIsUvcCompatible(long paramLong);
  
  protected static native long nativeOpenDeviceHandle(long paramLong, UsbInterfaceManager paramUsbInterfaceManager);
  
  protected static native void nativeReleaseRefDevice(long paramLong);
  
  protected static native boolean nativeSetUsbDeviceInfo(long paramLong, int paramInt, String paramString);
  
  public void cacheWebcamName() {
    synchronized (this.lock) {
      if (this.webcamName == null)
        this.webcamName = internalGetWebcamName(); 
      return;
    } 
  }
  
  protected void destructor() {
    UsbDeviceConnection usbDeviceConnection = this.usbDeviceConnection;
    if (usbDeviceConnection != null) {
      usbDeviceConnection.close();
      this.usbDeviceConnection = null;
    } 
    if (this.pointer != 0L) {
      nativeReleaseRefDevice(this.pointer);
      clearPointer();
    } 
    LibUsbDevice libUsbDevice = this.libUsbDevice;
    if (libUsbDevice != null) {
      libUsbDevice.releaseRef();
      this.libUsbDevice = null;
    } 
    super.destructor();
  }
  
  protected UsbDevice findUsbDevice() throws FileNotFoundException {
    return getCameraManagerImpl().findUsbDevice(getUsbDeviceName());
  }
  
  public CameraManagerImpl getCameraManagerImpl() {
    return ((UvcContext)getParent()).getCameraManagerImpl();
  }
  
  public UvcDeviceDescriptor getDeviceDescriptor() {
    return new UvcDeviceDescriptor(nativeGetDeviceDescriptor(this.pointer));
  }
  
  public UvcDeviceInfo getDeviceInfo() {
    return new UvcDeviceInfo(nativeGetDeviceInfo(this.pointer));
  }
  
  public int getProductId() {
    return this.usbDevice.getProductId();
  }
  
  public List<UvcStreamingInterface> getStreamingInterfaces() {
    UvcDeviceInfo uvcDeviceInfo = getDeviceInfo();
    try {
      return uvcDeviceInfo.getStreamingInterfaces();
    } finally {
      uvcDeviceInfo.releaseRef();
    } 
  }
  
  public String getTag() {
    return TAG;
  }
  
  public String getTraceIdentifier() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(super.getTraceIdentifier());
    stringBuilder.append("|");
    stringBuilder.append(internalGetUsbDeviceName());
    return stringBuilder.toString();
  }
  
  public UsbDevice getUsbDevice() {
    return this.usbDevice;
  }
  
  protected String getUsbDeviceName() {
    String str = internalGetUsbDeviceName();
    if (str != null)
      return str; 
    throw Misc.internalError("internal error: getUsbDeviceName with both usbDevice and libUsbDevice null");
  }
  
  protected UsbManager getUsbManager() {
    return getCameraManagerImpl().getUsbManager();
  }
  
  public UvcContext getUvcContext() {
    return (UvcContext)getParent();
  }
  
  public int getVendorId() {
    return this.usbDevice.getVendorId();
  }
  
  public WebcamName getWebcamName() {
    synchronized (this.lock) {
      cacheWebcamName();
      return this.webcamName;
    } 
  }
  
  protected String internalGetUsbDeviceName() {
    synchronized (this.lock) {
      if (this.usbDevice != null)
        return this.usbDevice.getDeviceName(); 
      if (this.libUsbDevice != null)
        return this.libUsbDevice.getUsbDeviceName(); 
      return null;
    } 
  }
  
  protected WebcamName internalGetWebcamName() {
    try {
      synchronized (this.lock) {
        if (this.usbDevice != null)
          return getCameraManagerImpl().webcamNameFromDevice(this.usbDevice); 
        if (this.libUsbDevice != null)
          return getCameraManagerImpl().webcamNameFromDevice(this.libUsbDevice); 
      } 
    } catch (RuntimeException runtimeException) {}
    return null;
  }
  
  public boolean isUvcCompatible() {
    return nativeIsUvcCompatible(this.pointer);
  }
  
  public UvcDeviceHandle open(final WebcamName cameraName, final Camera.StateCallback stateCallback) {
    return (UvcDeviceHandle)this.tracer.trace("open()", new Supplier<UvcDeviceHandle>() {
          public UvcDeviceHandle get() {
            synchronized (UvcDevice.this.lock) {
              Camera.OpenFailure openFailure = Camera.OpenFailure.InternalError;
              try {
                Assert.assertNull(UvcDevice.this.usbDeviceConnection);
                UvcDevice.this.usbDeviceConnection = UvcDevice.this.getUsbManager().openDevice(UvcDevice.this.usbDevice);
                if (UvcDevice.this.usbDeviceConnection != null) {
                  if (UvcDevice.nativeSetUsbDeviceInfo(UvcDevice.this.pointer, UvcDevice.this.usbDeviceConnection.getFileDescriptor(), UvcDevice.this.usbDevice.getDeviceName())) {
                    long l = UvcDevice.nativeOpenDeviceHandle(UvcDevice.this.pointer, UvcDevice.this.usbInterfaceManager);
                    if (l != 0L) {
                      UvcDeviceHandle uvcDeviceHandle = new UvcDeviceHandle(l, UvcDevice.this, stateCallback);
                      uvcDeviceHandle.openSelfAndReport();
                      return uvcDeviceHandle;
                    } 
                    openFailure = Camera.OpenFailure.OtherFailure;
                  } else {
                    openFailure = Camera.OpenFailure.InternalError;
                  } 
                } else {
                  openFailure = Camera.OpenFailure.InUseOrAccessDenied;
                } 
              } catch (RuntimeException runtimeException) {
                UvcDevice.this.tracer.traceError(runtimeException, "exception opening UvcDevice %s", new Object[] { this.val$cameraName });
                openFailure = Camera.OpenFailure.InternalError;
              } 
              stateCallback.onOpenFailed((CameraName)cameraName, openFailure);
              return null;
            } 
          }
        });
  }
  
  public String toString() {
    return Misc.formatInvariant("%s(%s)", new Object[] { getTag(), internalGetUsbDeviceName() });
  }
  
  static interface UsbInterfaceManager {
    int claimInterface(int param1Int);
    
    int releaseInterface(int param1Int);
    
    int setInterfaceAltSetting(int param1Int1, int param1Int2);
  }
  
  protected class UsbInterfaceMangerImpl implements UsbInterfaceManager {
    public int claimInterface(int param1Int) {
      UvcDevice.this.tracer.trace("claimInterface(%d)", new Object[] { Integer.valueOf(param1Int) });
      int j = UvcDevice.this.usbDevice.getInterfaceCount();
      for (int i = 0; i < j; i++) {
        UsbInterface usbInterface = UvcDevice.this.usbDevice.getInterface(i);
        if (usbInterface.getId() == param1Int) {
          if (UvcDevice.this.usbDeviceConnection.claimInterface(usbInterface, true)) {
            UvcDevice.this.tracer.trace("claimInterface(%d) succeeded", new Object[] { Integer.valueOf(param1Int) });
            return UvcError.SUCCESS.getValue();
          } 
          UvcDevice.this.tracer.traceError("claimInterface(%d) failed", new Object[] { Integer.valueOf(param1Int) });
          return UvcError.IO.getValue();
        } 
      } 
      UvcDevice.this.tracer.traceError("claimInterface(%d) failed: not found", new Object[] { Integer.valueOf(param1Int) });
      return UvcError.NOT_FOUND.getValue();
    }
    
    public int releaseInterface(int param1Int) {
      UvcDevice.this.tracer.trace("releaseInterface(%d)", new Object[] { Integer.valueOf(param1Int) });
      int j = UvcDevice.this.usbDevice.getInterfaceCount();
      for (int i = 0; i < j; i++) {
        UsbInterface usbInterface = UvcDevice.this.usbDevice.getInterface(i);
        if (usbInterface.getId() == param1Int) {
          if (UvcDevice.this.usbDeviceConnection.releaseInterface(usbInterface)) {
            UvcDevice.this.tracer.trace("releaseInterface(%d) succeeded", new Object[] { Integer.valueOf(param1Int) });
            return UvcError.SUCCESS.getValue();
          } 
          UvcDevice.this.tracer.traceError("releaseInterface(%d) failed", new Object[] { Integer.valueOf(param1Int) });
          return UvcError.IO.getValue();
        } 
      } 
      UvcDevice.this.tracer.traceError("releaseInterface(%d) failed: not found", new Object[] { Integer.valueOf(param1Int) });
      return UvcError.NOT_FOUND.getValue();
    }
    
    public int setInterfaceAltSetting(int param1Int1, int param1Int2) {
      UvcDevice.this.tracer.trace("setInterfaceAltSetting(%d,%d)", new Object[] { Integer.valueOf(param1Int1), Integer.valueOf(param1Int2) });
      int j = UvcDevice.this.usbDevice.getInterfaceCount();
      for (int i = 0; i < j; i++) {
        UsbInterface usbInterface = UvcDevice.this.usbDevice.getInterface(i);
        if (usbInterface.getId() == param1Int1 && usbInterface.getAlternateSetting() == param1Int2) {
          if (UvcDevice.this.usbDeviceConnection.setInterface(usbInterface)) {
            UvcDevice.this.tracer.trace("setInterfaceAltSetting(%d,%d) succeeded", new Object[] { Integer.valueOf(param1Int1), Integer.valueOf(param1Int2) });
            return UvcError.SUCCESS.getValue();
          } 
          UvcDevice.this.tracer.traceError("setInterfaceAltSetting(%d, %d) failed", new Object[] { Integer.valueOf(param1Int1), Integer.valueOf(param1Int2) });
          return UvcError.IO.getValue();
        } 
      } 
      UvcDevice.this.tracer.traceError("setInterfaceAltSetting(%d, %d) failed: not found", new Object[] { Integer.valueOf(param1Int1), Integer.valueOf(param1Int2) });
      return UvcError.NOT_FOUND.getValue();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\UvcDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */