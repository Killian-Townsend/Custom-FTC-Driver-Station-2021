package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import android.hardware.usb.UsbConfiguration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import androidx.renderscript.RenderScript;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.firstinspires.ftc.robotcore.internal.camera.CameraManagerImpl;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.NativeObject;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;
import org.firstinspires.ftc.robotcore.internal.system.SystemProperties;

public class UvcContext extends NativeObject {
  public static boolean DEBUG_RS;
  
  public static final String TAG = UvcContext.class.getSimpleName();
  
  protected static final AtomicInteger instanceCounter;
  
  protected CameraManagerImpl cameraManagerImpl;
  
  protected final int instanceNumber;
  
  protected RenderScript renderScript;
  
  protected final RenderScript.ContextType renderScriptContextType;
  
  protected final int renderScriptCreateFlags;
  
  protected boolean renderScriptInitialized;
  
  protected final Lock renderscriptAccessLock;
  
  protected final String usbFileSystemRoot;
  
  static {
    DEBUG_RS = false;
    instanceCounter = new AtomicInteger(0);
  }
  
  public UvcContext(CameraManagerImpl paramCameraManagerImpl, String paramString) {
    super(nativeInitContext(paramString, Build.VERSION.SDK_INT, AppUtil.FIRST_FOLDER.getAbsolutePath(), true), RefCounted.TraceLevel.None);
    RenderScript.ContextType contextType;
    this.instanceNumber = instanceCounter.getAndIncrement();
    this.renderscriptAccessLock = new ReentrantLock();
    this.renderScript = null;
    if (DEBUG_RS) {
      contextType = RenderScript.ContextType.DEBUG;
    } else {
      contextType = RenderScript.ContextType.NORMAL;
    } 
    this.renderScriptContextType = contextType;
    this.renderScriptCreateFlags = 0;
    this.cameraManagerImpl = null;
    this.renderScriptInitialized = false;
    this.traceLevel = defaultTraceLevel;
    if (paramString == null)
      RobotLog.ww(TAG, "creating UvcContext with null usbFileSystemRoot"); 
    this.usbFileSystemRoot = paramString;
    this.cameraManagerImpl = paramCameraManagerImpl;
    if (traceCtor())
      RobotLog.vv(getTag(), "construct(%s)", new Object[] { getTraceIdentifier() }); 
  }
  
  protected static native long nativeCreateUvcDevice(long paramLong, String paramString);
  
  protected static native void nativeExitContext(long paramLong);
  
  protected static native long nativeGetLibUsbDeviceFromUsbDeviceName(long paramLong, String paramString);
  
  protected static native long nativeInitContext(String paramString1, int paramInt, String paramString2, boolean paramBoolean);
  
  protected void destructor() {
    RenderScript renderScript = this.renderScript;
    if (renderScript != null) {
      renderScript.destroy();
      this.renderScript = null;
    } 
    if (this.pointer != 0L) {
      nativeExitContext(this.pointer);
      clearPointer();
    } 
    this.cameraManagerImpl = null;
    super.destructor();
  }
  
  public CameraManagerImpl getCameraManagerImpl() {
    return this.cameraManagerImpl;
  }
  
  public List<UvcDevice> getDeviceList() {
    return getUvcDeviceListUsingJava();
  }
  
  public LibUsbDevice getLibUsbDeviceFromUsbDevice(UsbDevice paramUsbDevice, boolean paramBoolean) {
    synchronized (this.lock) {
      long l = nativeGetLibUsbDeviceFromUsbDeviceName(this.pointer, paramUsbDevice.getDeviceName());
      if (l != 0L)
        return new LibUsbDevice(l, paramUsbDevice, paramBoolean); 
      return null;
    } 
  }
  
  public long getPointer() {
    return this.pointer;
  }
  
  public SerialNumber getRealOrVendorProductSerialNumber(UsbDevice paramUsbDevice) {
    SerialNumber serialNumber2 = SerialNumber.fromUsbOrNull(paramUsbDevice.getSerialNumber());
    SerialNumber serialNumber1 = serialNumber2;
    if (serialNumber2 == null) {
      LibUsbDevice libUsbDevice = getLibUsbDeviceFromUsbDevice(paramUsbDevice, false);
      serialNumber1 = serialNumber2;
      if (libUsbDevice != null) {
        serialNumber1 = libUsbDevice.getRealOrVendorProductSerialNumber();
        libUsbDevice.releaseRef();
      } 
    } 
    return serialNumber1;
  }
  
  public RenderScript getRenderScript() {
    synchronized (this.lock) {
      initRenderScriptParametersIfNeeded();
      if (this.renderScript == null)
        this.renderScript = RenderScript.create(AppUtil.getDefContext(), 28, this.renderScriptContextType, 0); 
      this.renderScript.setErrorHandler(new RenderScript.RSErrorHandler() {
            public void run() {
              RobotLog.ee(UvcContext.this.getTag(), "RenderScript error(%d): %s", new Object[] { Integer.valueOf(this.mErrorNum), this.mErrorMessage });
            }
          });
      return this.renderScript;
    } 
  }
  
  protected File getRenderscriptCacheDir() {
    return new File(AppUtil.getDefContext().getCacheDir(), "org.firstinspires.ftc.renderscript.cache");
  }
  
  public String getTag() {
    return TAG;
  }
  
  public String getTraceIdentifier() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(super.getTraceIdentifier());
    stringBuilder.append(Misc.formatInvariant("|inst#=%d", new Object[] { Integer.valueOf(this.instanceNumber) }));
    return stringBuilder.toString();
  }
  
  public String getUsbFileSystemRoot() {
    return this.usbFileSystemRoot;
  }
  
  protected List<UvcDevice> getUvcDeviceListUsingJava() {
    synchronized (this.lock) {
      ArrayList<UvcDevice> arrayList = new ArrayList();
      for (UsbDevice usbDevice : ((UsbManager)AppUtil.getDefContext().getSystemService("usb")).getDeviceList().values()) {
        try {
          if (isUvcCompatible(usbDevice)) {
            RobotLog.dd(TAG, "found webcam: usbPath=%s vid=%d pid=%d serial=%s product=%s", new Object[] { usbDevice.getDeviceName(), Integer.valueOf(usbDevice.getVendorId()), Integer.valueOf(usbDevice.getProductId()), usbDevice.getSerialNumber(), usbDevice.getProductName() });
            UvcDevice uvcDevice = uvcDeviceFrom(usbDevice);
            if (uvcDevice != null)
              arrayList.add(uvcDevice); 
            continue;
          } 
          RobotLog.dd(TAG, "usb device is *not* UVC compatible, %s", new Object[] { usbDevice.getDeviceName() });
        } catch (RuntimeException runtimeException) {
          RobotLog.ee(TAG, runtimeException, "exception processing %s; ignoring", new Object[] { usbDevice.getDeviceName() });
        } 
      } 
      return arrayList;
    } 
  }
  
  protected void initRenderScriptParametersIfNeeded() {
    synchronized (this.lock) {
      if (!this.renderScriptInitialized) {
        File file = getRenderscriptCacheDir();
        AppUtil.getInstance().ensureDirectoryExists(file, false);
        try {
          String str;
          if (this.renderScriptContextType == RenderScript.ContextType.DEBUG) {
            str = "1";
          } else {
            str = "0";
          } 
          SystemProperties.set("debug.rs.debug", str);
        } finally {}
        this.renderScriptInitialized = true;
      } 
      return;
    } 
  }
  
  protected boolean isUvcCompatible(UsbDevice paramUsbDevice) {
    UsbConfiguration usbConfiguration = paramUsbDevice.getConfiguration(0);
    int j = usbConfiguration.getInterfaceCount();
    for (int i = 0; i < j; i++) {
      UsbInterface usbInterface = usbConfiguration.getInterface(i);
      if (usbInterface.getInterfaceClass() == 14 && usbInterface.getInterfaceSubclass() == 2)
        return true; 
    } 
    return false;
  }
  
  public boolean lockRenderScriptWhile(long paramLong, TimeUnit paramTimeUnit, Runnable paramRunnable) {
    try {
      initRenderScriptParametersIfNeeded();
      boolean bool = this.renderscriptAccessLock.tryLock(paramLong, paramTimeUnit);
      if (bool)
        try {
          paramRunnable.run();
          return true;
        } finally {
          this.renderscriptAccessLock.unlock();
        }  
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
    } 
    return false;
  }
  
  protected UvcDevice uvcDeviceFrom(UsbDevice paramUsbDevice) {
    long l = nativeCreateUvcDevice(this.pointer, paramUsbDevice.getDeviceName());
    if (l != 0L) {
      try {
        return new UvcDevice(l, this, paramUsbDevice);
      } catch (IOException iOException) {
        RobotLog.ee(TAG, iOException, "exception processing %s; ignoring", new Object[] { paramUsbDevice.getDeviceName() });
      } 
    } else {
      RobotLog.ee(TAG, "nativeCreateUvcDevice() failed");
    } 
    return null;
  }
  
  protected static interface LongConsumer {
    void accept(long param1Long);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\UvcContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */