package org.firstinspires.ftc.robotcore.internal.camera.names;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.text.TextUtils;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareDeviceCloseOnTearDown;
import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.util.GlobalWarningSource;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.WeakReferenceSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.ContinuationResult;
import org.firstinspires.ftc.robotcore.external.function.Function;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.CameraManagerInternal;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiCameraCharacteristics;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiCameraCharacteristicsBuilder;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcDevice;
import org.firstinspires.ftc.robotcore.internal.collections.MutableReference;
import org.firstinspires.ftc.robotcore.internal.hardware.UserNameable;
import org.firstinspires.ftc.robotcore.internal.hardware.usb.ArmableUsbDevice;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;
import org.firstinspires.ftc.robotcore.internal.usb.UsbConstants;

public class WebcamNameImpl extends CameraNameImplBase implements WebcamNameInternal, RobotUsbModule, GlobalWarningSource, HardwareDeviceCloseOnTearDown, UserNameable {
  public static final String TAG = "WebcamNameImpl";
  
  protected static Semaphore requestPermissionSemaphore = new Semaphore(1);
  
  protected final CameraManagerInternal cameraManagerInternal;
  
  protected final WeakReferenceSet<DelegatingCallback> delegatingCallbacks = new WeakReferenceSet();
  
  protected final ArmableDeviceHelper helper;
  
  protected final Object lock = new Object();
  
  protected final SerialNumber serialNumberPattern;
  
  protected final Tracer tracer = Tracer.create("WebcamNameImpl", TRACE);
  
  protected String userName = null;
  
  private WebcamNameImpl(SerialNumber paramSerialNumber) {
    Assert.assertNotNull(paramSerialNumber);
    this.cameraManagerInternal = (CameraManagerInternal)ClassFactory.getInstance().getCameraManager();
    this.serialNumberPattern = paramSerialNumber;
    this.helper = null;
  }
  
  private WebcamNameImpl(SerialNumber paramSerialNumber, ArmableUsbDevice.OpenRobotUsbDevice paramOpenRobotUsbDevice, SyncdDevice.Manager paramManager) {
    Assert.assertNotNull(paramSerialNumber);
    this.cameraManagerInternal = (CameraManagerInternal)ClassFactory.getInstance().getCameraManager();
    this.serialNumberPattern = paramSerialNumber;
    ArmableDeviceHelper armableDeviceHelper = new ArmableDeviceHelper(paramSerialNumber, paramOpenRobotUsbDevice, paramManager);
    this.helper = armableDeviceHelper;
    armableDeviceHelper.finishConstruction();
  }
  
  public static WebcamName forSerialNumber(SerialNumber paramSerialNumber) {
    return new WebcamNameImpl(paramSerialNumber);
  }
  
  public static WebcamName forSerialNumber(SerialNumber paramSerialNumber, ArmableUsbDevice.OpenRobotUsbDevice paramOpenRobotUsbDevice, SyncdDevice.Manager paramManager) {
    return new WebcamNameImpl(paramSerialNumber, paramOpenRobotUsbDevice, paramManager);
  }
  
  protected static List<SerialNumber> getMatchingAttachedSerialNumbers(CameraManagerInternal paramCameraManagerInternal, final Function<SerialNumber, Boolean> matcher) {
    final ArrayList<SerialNumber> result = new ArrayList();
    paramCameraManagerInternal.enumerateAttachedSerialNumbers(new Consumer<SerialNumber>() {
          public void accept(SerialNumber param1SerialNumber) {
            if (((Boolean)matcher.apply(param1SerialNumber)).booleanValue())
              result.add(param1SerialNumber); 
          }
        });
    return arrayList;
  }
  
  static String getUsbDeviceNameIfAttached(CameraManagerInternal paramCameraManagerInternal, SerialNumber paramSerialNumber, MutableReference<Boolean> paramMutableReference) {
    if (paramMutableReference != null)
      paramMutableReference.setValue(Boolean.valueOf(false)); 
    Iterator<UsbDevice> iterator = getUsbManager().getDeviceList().values().iterator();
    boolean bool = false;
    while (true) {
      String str = null;
      boolean bool1 = bool;
      while (iterator.hasNext()) {
        UsbDevice usbDevice = iterator.next();
        SerialNumber serialNumber = paramCameraManagerInternal.getRealOrVendorProductSerialNumber(usbDevice);
        if (serialNumber != null && serialNumber.matches(paramSerialNumber)) {
          if (!bool1) {
            str = usbDevice.getDeviceName();
            bool1 = true;
            continue;
          } 
          RobotLog.ee("WebcamNameImpl", "more than one webcam attached matching serial number %s: ignoring them all", new Object[] { paramSerialNumber });
          bool = bool1;
          if (paramMutableReference != null) {
            paramMutableReference.setValue(Boolean.valueOf(true));
            bool = bool1;
          } 
        } 
      } 
      return str;
    } 
  }
  
  protected static UsbManager getUsbManager() {
    return (UsbManager)AppUtil.getDefContext().getSystemService("usb");
  }
  
  public static boolean isAttached(CameraManagerInternal paramCameraManagerInternal, SerialNumber paramSerialNumber) {
    return (getUsbDeviceNameIfAttached(paramCameraManagerInternal, paramSerialNumber, null) != null);
  }
  
  protected static void reportFalse(Continuation<? extends Consumer<Boolean>> paramContinuation) {
    paramContinuation.dispatch(new ContinuationResult<Consumer<Boolean>>() {
          public void handle(Consumer<Boolean> param1Consumer) {
            param1Consumer.accept(Boolean.valueOf(false));
          }
        });
  }
  
  public void arm() throws RobotCoreException, InterruptedException {
    ArmableDeviceHelper armableDeviceHelper = this.helper;
    if (armableDeviceHelper != null)
      armableDeviceHelper.arm(); 
  }
  
  public void armOrPretend() throws RobotCoreException, InterruptedException {
    ArmableDeviceHelper armableDeviceHelper = this.helper;
    if (armableDeviceHelper != null)
      armableDeviceHelper.armOrPretend(); 
  }
  
  public void asyncRequestCameraPermission(Context paramContext, Deadline paramDeadline, final Continuation<? extends Consumer<Boolean>> userContinuation) {
    final UsbDevice usbDevice = getUsbDevice();
    if (usbDevice != null)
      try {
        if (paramDeadline.tryAcquire(requestPermissionSemaphore)) {
          this.tracer.trace("requesting permission for %s", new Object[] { usbDevice.getDeviceName() });
          AppUtil.getInstance().asyncRequestUsbPermission("WebcamNameImpl", paramContext, usbDevice, paramDeadline, userContinuation.createForNewTarget(new Consumer<Boolean>() {
                  public void accept(final Boolean permissionGranted) {
                    WebcamNameImpl.this.tracer.trace("permission for %s=%s", new Object[] { this.val$usbDevice.getDeviceName(), permissionGranted });
                    WebcamNameImpl.requestPermissionSemaphore.release();
                    userContinuation.dispatchHere(new ContinuationResult<Consumer<Boolean>>() {
                          public void handle(Consumer<Boolean> param2Consumer) {
                            param2Consumer.accept(permissionGranted);
                          }
                        });
                  }
                }));
          return;
        } 
        this.tracer.trace("requestPermission(): timed out waiting on semaphore", new Object[0]);
        reportFalse(userContinuation);
        return;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        this.tracer.trace("requestPermission(): interrupted", new Object[0]);
        reportFalse(userContinuation);
        return;
      }  
    this.tracer.trace("unable to find usbDevice: %s", new Object[] { getSerialNumber() });
    reportFalse(userContinuation);
  }
  
  public void clearGlobalWarning() {
    ArmableDeviceHelper armableDeviceHelper = this.helper;
    if (armableDeviceHelper != null)
      armableDeviceHelper.clearGlobalWarning(); 
  }
  
  public void close() {
    ArmableDeviceHelper armableDeviceHelper = this.helper;
    if (armableDeviceHelper != null)
      armableDeviceHelper.close(); 
  }
  
  public void disarm() throws RobotCoreException, InterruptedException {
    ArmableDeviceHelper armableDeviceHelper = this.helper;
    if (armableDeviceHelper != null)
      armableDeviceHelper.disarm(); 
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof WebcamNameImpl) {
      paramObject = paramObject;
      return this.serialNumberPattern.equals(((WebcamNameImpl)paramObject).serialNumberPattern);
    } 
    return super.equals(paramObject);
  }
  
  protected UvcDevice findUvcDevice() {
    return ((CameraManagerInternal)ClassFactory.getInstance().getCameraManager()).findUvcDevice(this);
  }
  
  public RobotArmingStateNotifier.ARMINGSTATE getArmingState() {
    ArmableDeviceHelper armableDeviceHelper = this.helper;
    return (armableDeviceHelper != null) ? armableDeviceHelper.getArmingState() : RobotArmingStateNotifier.ARMINGSTATE.CLOSED;
  }
  
  public CameraCharacteristics getCameraCharacteristics() {
    UvcDevice uvcDevice = findUvcDevice();
    if (uvcDevice != null)
      try {
        UvcApiCameraCharacteristicsBuilder uvcApiCameraCharacteristicsBuilder = new UvcApiCameraCharacteristicsBuilder();
      } finally {
        uvcDevice.releaseRef();
      }  
    return (CameraCharacteristics)new UvcApiCameraCharacteristics();
  }
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("USB (");
    stringBuilder.append(getSerialNumber());
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    UsbDevice usbDevice = getUsbDevice();
    if (usbDevice != null) {
      String str3 = usbDevice.getManufacturerName();
      String str2 = usbDevice.getProductName();
      str3 = UsbConstants.getManufacturerName(str3, usbDevice.getVendorId());
      str2 = UsbConstants.getProductName(str2, usbDevice.getVendorId(), usbDevice.getProductId());
      String str1 = str2;
      if (TextUtils.isEmpty(str2))
        str1 = AppUtil.getDefContext().getString(R.string.moduleDisplayNameWebcam); 
      if (TextUtils.isEmpty(str3))
        return str1; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str3);
      stringBuilder.append(" ");
      stringBuilder.append(str1);
      return stringBuilder.toString();
    } 
    return AppUtil.getDefContext().getString(R.string.moduleDisplayNameWebcam);
  }
  
  public String getGlobalWarning() {
    ArmableDeviceHelper armableDeviceHelper = this.helper;
    return (armableDeviceHelper != null) ? armableDeviceHelper.getGlobalWarning() : "";
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Unknown;
  }
  
  public SerialNumber getSerialNumber() {
    return this.serialNumberPattern;
  }
  
  protected UsbDevice getUsbDevice() {
    return (UsbDevice)getUsbManager().getDeviceList().get(getUsbDeviceNameIfAttached());
  }
  
  public String getUsbDeviceNameIfAttached() {
    return getUsbDeviceNameIfAttached(this.cameraManagerInternal, this.serialNumberPattern, null);
  }
  
  public String getUserName() {
    return this.userName;
  }
  
  public int getVersion() {
    return 1;
  }
  
  public int hashCode() {
    return this.serialNumberPattern.hashCode();
  }
  
  public boolean isAttached() {
    return isAttached(null);
  }
  
  protected boolean isAttached(MutableReference<Boolean> paramMutableReference) {
    return (getUsbDeviceNameIfAttached(this.cameraManagerInternal, this.serialNumberPattern, paramMutableReference) != null);
  }
  
  public boolean isWebcam() {
    return true;
  }
  
  public void pretend() throws RobotCoreException, InterruptedException {
    ArmableDeviceHelper armableDeviceHelper = this.helper;
    if (armableDeviceHelper != null)
      armableDeviceHelper.pretend(); 
  }
  
  public void registerCallback(RobotArmingStateNotifier.Callback paramCallback, boolean paramBoolean) {
    synchronized (this.delegatingCallbacks) {
      Iterator iterator = this.delegatingCallbacks.iterator();
      while (iterator.hasNext()) {
        if (((DelegatingCallback)iterator.next()).userCallback == paramCallback)
          return; 
      } 
      paramCallback = new DelegatingCallback(paramCallback);
      this.delegatingCallbacks.add(paramCallback);
      if (this.helper != null)
        this.helper.registerCallback(paramCallback, paramBoolean); 
      return;
    } 
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public void setGlobalWarning(String paramString) {
    ArmableDeviceHelper armableDeviceHelper = this.helper;
    if (armableDeviceHelper != null)
      armableDeviceHelper.setGlobalWarning(paramString); 
  }
  
  public void setUserName(String paramString) {
    this.userName = paramString;
  }
  
  public void suppressGlobalWarning(boolean paramBoolean) {
    ArmableDeviceHelper armableDeviceHelper = this.helper;
    if (armableDeviceHelper != null)
      armableDeviceHelper.suppressGlobalWarning(paramBoolean); 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Webcam(");
    stringBuilder.append(this.serialNumberPattern);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void unregisterCallback(RobotArmingStateNotifier.Callback paramCallback) {
    // Byte code:
    //   0: aload_0
    //   1: getfield delegatingCallbacks : Lcom/qualcomm/robotcore/util/WeakReferenceSet;
    //   4: astore #4
    //   6: aload #4
    //   8: monitorenter
    //   9: aconst_null
    //   10: astore_3
    //   11: aload_0
    //   12: getfield delegatingCallbacks : Lcom/qualcomm/robotcore/util/WeakReferenceSet;
    //   15: invokevirtual iterator : ()Ljava/util/Iterator;
    //   18: astore #5
    //   20: aload_3
    //   21: astore_2
    //   22: aload #5
    //   24: invokeinterface hasNext : ()Z
    //   29: ifeq -> 51
    //   32: aload #5
    //   34: invokeinterface next : ()Ljava/lang/Object;
    //   39: checkcast org/firstinspires/ftc/robotcore/internal/camera/names/WebcamNameImpl$DelegatingCallback
    //   42: astore_2
    //   43: aload_2
    //   44: getfield userCallback : Lcom/qualcomm/robotcore/hardware/usb/RobotArmingStateNotifier$Callback;
    //   47: aload_1
    //   48: if_acmpne -> 20
    //   51: aload_2
    //   52: ifnull -> 82
    //   55: aload_0
    //   56: getfield delegatingCallbacks : Lcom/qualcomm/robotcore/util/WeakReferenceSet;
    //   59: aload_2
    //   60: invokevirtual remove : (Ljava/lang/Object;)Z
    //   63: pop
    //   64: aload_0
    //   65: getfield helper : Lorg/firstinspires/ftc/robotcore/internal/camera/names/WebcamNameImpl$ArmableDeviceHelper;
    //   68: ifnull -> 82
    //   71: aload_0
    //   72: getfield helper : Lorg/firstinspires/ftc/robotcore/internal/camera/names/WebcamNameImpl$ArmableDeviceHelper;
    //   75: aload_2
    //   76: getfield userCallback : Lcom/qualcomm/robotcore/hardware/usb/RobotArmingStateNotifier$Callback;
    //   79: invokevirtual unregisterCallback : (Lcom/qualcomm/robotcore/hardware/usb/RobotArmingStateNotifier$Callback;)V
    //   82: aload #4
    //   84: monitorexit
    //   85: return
    //   86: astore_1
    //   87: aload #4
    //   89: monitorexit
    //   90: aload_1
    //   91: athrow
    // Exception table:
    //   from	to	target	type
    //   11	20	86	finally
    //   22	51	86	finally
    //   55	82	86	finally
    //   82	85	86	finally
    //   87	90	86	finally
  }
  
  class ArmableDeviceHelper extends ArmableUsbDevice implements SyncdDevice, CameraManagerInternal.UsbAttachmentCallback {
    protected volatile boolean hasDetached = false;
    
    public ArmableDeviceHelper(SerialNumber param1SerialNumber, ArmableUsbDevice.OpenRobotUsbDevice param1OpenRobotUsbDevice, SyncdDevice.Manager param1Manager) {
      super(AppUtil.getDefContext(), param1SerialNumber, param1Manager, param1OpenRobotUsbDevice);
    }
    
    protected void armDevice(RobotUsbDevice param1RobotUsbDevice) throws RobotCoreException, InterruptedException {
      this.tracer.trace("armDevice()", new Runnable() {
            public void run() {
              WebcamNameImpl.ArmableDeviceHelper.this.hasDetached = false;
              if (WebcamNameImpl.ArmableDeviceHelper.this.syncdDeviceManager != null)
                WebcamNameImpl.ArmableDeviceHelper.this.syncdDeviceManager.registerSyncdDevice(WebcamNameImpl.ArmableDeviceHelper.this); 
              WebcamNameImpl.this.cameraManagerInternal.registerReceiver(WebcamNameImpl.ArmableDeviceHelper.this);
              WebcamNameImpl.ArmableDeviceHelper.this.checkAttached();
            }
          });
    }
    
    protected void checkAttached() {
      synchronized (this.armingLock) {
        if (getArmingState() == RobotArmingStateNotifier.ARMINGSTATE.ARMED && !this.hasDetached) {
          MutableReference<Boolean> mutableReference = new MutableReference(Boolean.valueOf(false));
          if (!WebcamNameImpl.this.isAttached(mutableReference)) {
            this.tracer.trace("detach detected", new Object[0]);
            this.hasDetached = true;
            setGlobalWarning(getUnableToFindMessage(((Boolean)mutableReference.getValue()).booleanValue()));
          } 
        } 
        return;
      } 
    }
    
    protected void disarmDevice() throws InterruptedException {
      this.tracer.trace("disarmDevice()", new Runnable() {
            public void run() {
              if (WebcamNameImpl.ArmableDeviceHelper.this.syncdDeviceManager != null)
                WebcamNameImpl.ArmableDeviceHelper.this.syncdDeviceManager.unregisterSyncdDevice(WebcamNameImpl.ArmableDeviceHelper.this); 
              WebcamNameImpl.this.cameraManagerInternal.unregisterReceiver(WebcamNameImpl.ArmableDeviceHelper.this);
            }
          });
    }
    
    public void finishConstruction() {
      super.finishConstruction();
    }
    
    public RobotUsbModule getOwner() {
      return WebcamNameImpl.this;
    }
    
    public SyncdDevice.ShutdownReason getShutdownReason() {
      return this.hasDetached ? SyncdDevice.ShutdownReason.ABNORMAL : SyncdDevice.ShutdownReason.NORMAL;
    }
    
    protected String getTag() {
      return "WebcamNameImpl";
    }
    
    protected String getUnableToFindMessage(boolean param1Boolean) {
      return param1Boolean ? ((WebcamNameImpl.this.userName == null) ? Misc.formatForUser(R.string.duplicateWebcam, new Object[] { this.serialNumber }) : Misc.formatForUser(R.string.duplicateWebcamWithName, new Object[] { this.this$0.userName, this.serialNumber })) : ((WebcamNameImpl.this.userName == null) ? Misc.formatForUser(R.string.webcamNotFound, new Object[] { this.serialNumber }) : Misc.formatForUser(R.string.webcamNotFoundWithName, new Object[] { this.this$0.userName, this.serialNumber }));
    }
    
    protected String getUnableToOpenMessage() {
      return getUnableToFindMessage(false);
    }
    
    public void onAttached(UsbDevice param1UsbDevice, SerialNumber param1SerialNumber, MutableReference<Boolean> param1MutableReference) {
      checkAttached();
    }
    
    public void onDetached(UsbDevice param1UsbDevice) {
      checkAttached();
    }
    
    protected void pretendDevice(RobotUsbDevice param1RobotUsbDevice) throws RobotCoreException, InterruptedException {
      this.tracer.trace("pretendDevice()", new Runnable() {
            public void run() {
              WebcamNameImpl.ArmableDeviceHelper.this.hasDetached = false;
              if (WebcamNameImpl.ArmableDeviceHelper.this.syncdDeviceManager != null)
                WebcamNameImpl.ArmableDeviceHelper.this.syncdDeviceManager.registerSyncdDevice(WebcamNameImpl.ArmableDeviceHelper.this); 
              WebcamNameImpl.this.cameraManagerInternal.registerReceiver(WebcamNameImpl.ArmableDeviceHelper.this);
            }
          });
    }
    
    protected void registerGlobalWarningSource() {
      this.tracer.trace("registerGlobalWarningSource(%s)", new Object[] { this.this$0 });
      RobotLog.registerGlobalWarningSource(WebcamNameImpl.this);
    }
    
    public void setOwner(RobotUsbModule param1RobotUsbModule) {}
    
    protected void unregisterGlobalWarningSource() {
      RobotLog.unregisterGlobalWarningSource(WebcamNameImpl.this);
      this.tracer.trace("unregisterGlobalWarningSource(%s)", new Object[] { this.this$0 });
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$1.hasDetached = false;
      if (this.this$1.syncdDeviceManager != null)
        this.this$1.syncdDeviceManager.registerSyncdDevice(this.this$1); 
      WebcamNameImpl.this.cameraManagerInternal.registerReceiver(this.this$1);
      this.this$1.checkAttached();
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$1.hasDetached = false;
      if (this.this$1.syncdDeviceManager != null)
        this.this$1.syncdDeviceManager.registerSyncdDevice(this.this$1); 
      WebcamNameImpl.this.cameraManagerInternal.registerReceiver(this.this$1);
    }
  }
  
  class null implements Runnable {
    public void run() {
      if (this.this$1.syncdDeviceManager != null)
        this.this$1.syncdDeviceManager.unregisterSyncdDevice(this.this$1); 
      WebcamNameImpl.this.cameraManagerInternal.unregisterReceiver(this.this$1);
    }
  }
  
  class DelegatingCallback implements RobotArmingStateNotifier.Callback {
    public final RobotArmingStateNotifier.Callback userCallback;
    
    DelegatingCallback(RobotArmingStateNotifier.Callback param1Callback) {
      this.userCallback = param1Callback;
    }
    
    public void onModuleStateChange(RobotArmingStateNotifier param1RobotArmingStateNotifier, RobotArmingStateNotifier.ARMINGSTATE param1ARMINGSTATE) {
      this.userCallback.onModuleStateChange((RobotArmingStateNotifier)WebcamNameImpl.this, param1ARMINGSTATE);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\names\WebcamNameImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */