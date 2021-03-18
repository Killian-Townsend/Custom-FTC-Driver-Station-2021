package org.firstinspires.ftc.robotcore.internal.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ThreadPool;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.ContinuationResult;
import org.firstinspires.ftc.robotcore.external.function.Function;
import org.firstinspires.ftc.robotcore.external.function.Supplier;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.camera.delegating.RefCountedSwitchableCameraImpl;
import org.firstinspires.ftc.robotcore.internal.camera.delegating.SwitchableCameraName;
import org.firstinspires.ftc.robotcore.internal.camera.delegating.SwitchableCameraNameImpl;
import org.firstinspires.ftc.robotcore.internal.camera.delegating.UsbResiliantWebcam;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.LibUsbDevice;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcContext;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcDevice;
import org.firstinspires.ftc.robotcore.internal.camera.names.BuiltinCameraNameImpl;
import org.firstinspires.ftc.robotcore.internal.camera.names.UnknownCameraNameImpl;
import org.firstinspires.ftc.robotcore.internal.camera.names.WebcamNameImpl;
import org.firstinspires.ftc.robotcore.internal.collections.MutableReference;
import org.firstinspires.ftc.robotcore.internal.hardware.usb.ArmableUsbDevice;
import org.firstinspires.ftc.robotcore.internal.network.CallbackLooper;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.ContinuationSynchronizer;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.robotcore.internal.system.DestructOnFinalize;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public class CameraManagerImpl extends DestructOnFinalize implements CameraManager, CameraManagerInternal, AppUtil.UsbFileSystemRootListener {
  public static final String TAG = "CameraManager";
  
  public static boolean TRACE = true;
  
  protected static final AtomicInteger instanceCounter = new AtomicInteger(0);
  
  protected final List<CameraManagerInternal.UsbAttachmentCallback> callbacks = new ArrayList<CameraManagerInternal.UsbAttachmentCallback>();
  
  protected final int instanceNumber = instanceCounter.getAndIncrement();
  
  protected final Executor serialThreadPool = ThreadPool.newSingleThreadExecutor("CameraManager");
  
  protected Tracer tracer = Tracer.create("CameraManager", TRACE);
  
  protected final UsbAttachmentMonitor usbAttachmentMonitor;
  
  protected UsbManager usbManager = (UsbManager)AppUtil.getDefContext().getSystemService("usb");
  
  protected UvcContext uvcContext = null;
  
  public CameraManagerImpl() {
    super(RefCounted.TraceLevel.None);
    AppUtil.getInstance().addUsbfsListener(this);
    if (traceCtor())
      RobotLog.vv(getTag(), "construct(%s)", new Object[] { getTraceIdentifier() }); 
    getOrMakeUvcContext();
    this.usbAttachmentMonitor = new UsbAttachmentMonitor();
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
    intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
    AppUtil.getDefContext().registerReceiver(this.usbAttachmentMonitor, intentFilter, null, CallbackLooper.getDefault().getHandler());
  }
  
  public void asyncOpenCameraAssumingPermission(final CameraName cameraName, final Continuation<? extends Camera.StateCallback> tracingContinuation, final long reopenDuration, final TimeUnit reopenTimeUnit) {
    if (tracingContinuation != null) {
      tracingContinuation = tracingContinuation.createForNewTarget(new Camera.StateCallbackDefault() {
            public void onClosed(Camera param1Camera) {
              CameraManagerImpl.this.tracer.trace("camera reports closed: %s", new Object[] { param1Camera });
              ((Camera.StateCallback)userContinuation.getTarget()).onClosed(param1Camera);
            }
            
            public void onError(Camera param1Camera, Camera.Error param1Error) {
              CameraManagerImpl.this.tracer.traceError("camera reports error: %s: %s", new Object[] { param1Camera, param1Error });
              ((Camera.StateCallback)userContinuation.getTarget()).onError(param1Camera, param1Error);
            }
            
            public void onOpenFailed(CameraName param1CameraName, Camera.OpenFailure param1OpenFailure) {
              CameraManagerImpl.this.tracer.trace("camera reports failed to open: %s:", new Object[] { param1CameraName });
              ((Camera.StateCallback)userContinuation.getTarget()).onOpenFailed(param1CameraName, param1OpenFailure);
            }
            
            public void onOpened(Camera param1Camera) {
              CameraManagerImpl.this.tracer.trace("camera reports opened: %s", new Object[] { param1Camera });
              ((Camera.StateCallback)userContinuation.getTarget()).onOpened(param1Camera);
            }
          });
      Tracer tracer = this.tracer;
      tracer.trace(tracer.format("asyncOpenCamera(%s)", new Object[] { cameraName }), new Runnable() {
            public void run() {
              if (cameraName.isWebcam()) {
                CameraManagerImpl.this.asyncOpenWebcamAssumingPermission((WebcamName)cameraName, tracingContinuation, reopenDuration, reopenTimeUnit);
                return;
              } 
              CameraName cameraName = cameraName;
              if (cameraName instanceof SwitchableCameraName) {
                CameraManagerImpl.this.asyncOpenSwitchableAssumingPermission((SwitchableCameraName)cameraName, tracingContinuation, reopenDuration, reopenTimeUnit);
                return;
              } 
              CameraManagerImpl.this.tracer.traceError("asyncOpenCamera(): %s is not a kind of camera we can open", new Object[] { this.val$cameraName });
              tracingContinuation.dispatch(new ContinuationResult<Camera.StateCallback>() {
                    public void handle(Camera.StateCallback param2StateCallback) {
                      param2StateCallback.onOpenFailed(cameraName, Camera.OpenFailure.CameraTypeNotSupported);
                    }
                  });
            }
          });
      return;
    } 
    throw Misc.illegalArgumentException("Camera.StateCallback continuation must not be null");
  }
  
  protected void asyncOpenSwitchableAssumingPermission(final SwitchableCameraName switchableCameraName, final Continuation<? extends Camera.StateCallback> userContinuation, final long reopenDuration, final TimeUnit reopenTimeUnit) {
    final CameraName[] cameraNames = switchableCameraName.getMembers();
    Tracer tracer = this.tracer;
    tracer.trace(tracer.format("asyncOpenSwitchable(%s)", new Object[] { switchableCameraName }), new Runnable() {
          public void run() {
            RefCountedSwitchableCameraImpl refCountedSwitchableCameraImpl = new RefCountedSwitchableCameraImpl(CameraManagerImpl.this, switchableCameraName, cameraNames, userContinuation);
            try {
              refCountedSwitchableCameraImpl.openAssumingPermission(reopenDuration, reopenTimeUnit);
              return;
            } finally {
              refCountedSwitchableCameraImpl.releaseRef();
            } 
          }
        });
  }
  
  protected void asyncOpenWebcamAssumingPermission(final WebcamName webcamName, final Continuation<? extends Camera.StateCallback> userContinuation, final long reopenDuration, final TimeUnit reopenTimeUnit) {
    Tracer tracer = this.tracer;
    tracer.trace(tracer.format("asyncOpenWebcam(%s)", new Object[] { webcamName }), new Runnable() {
          public void run() {
            UsbResiliantWebcam usbResiliantWebcam = new UsbResiliantWebcam(CameraManagerImpl.this, webcamName, userContinuation);
            try {
              usbResiliantWebcam.openAssumingPermission(reopenDuration, reopenTimeUnit);
              return;
            } finally {
              usbResiliantWebcam.releaseRef();
            } 
          }
        });
  }
  
  protected void destructor() {
    this.uvcContext.releaseRef();
    AppUtil.getInstance().removeUsbfsListener(this);
    AppUtil.getDefContext().getApplicationContext().unregisterReceiver(this.usbAttachmentMonitor);
    super.destructor();
  }
  
  public void enumerateAttachedSerialNumbers(Consumer<SerialNumber> paramConsumer) {
    Iterator<UsbDevice> iterator = this.usbManager.getDeviceList().values().iterator();
    while (iterator.hasNext()) {
      SerialNumber serialNumber = getRealOrVendorProductSerialNumber(iterator.next());
      if (serialNumber != null)
        paramConsumer.accept(serialNumber); 
    } 
  }
  
  public UsbDevice findUsbDevice(String paramString) throws FileNotFoundException {
    try {
      return (UsbDevice)this.usbManager.getDeviceList().get(paramString);
    } catch (RuntimeException runtimeException) {
      throw new FileNotFoundException(paramString);
    } 
  }
  
  public UvcDevice findUvcDevice(WebcamName paramWebcamName) {
    SerialNumber serialNumber = paramWebcamName.getSerialNumber();
    Iterator<UvcDevice> iterator = getOrMakeUvcContext().getDeviceList().iterator();
    boolean bool = false;
    WebcamName webcamName = null;
    while (iterator.hasNext()) {
      UvcDevice uvcDevice = iterator.next();
      paramWebcamName = webcamName;
      boolean bool1 = bool;
      try {
        if (uvcDevice.getWebcamName().getSerialNumber().matches(serialNumber))
          if (!bool) {
            uvcDevice.addRef();
            UvcDevice uvcDevice1 = uvcDevice;
            bool1 = true;
          } else {
            this.tracer.traceError("more than one webcam attached matching serial number %s: ignoring them all", new Object[] { serialNumber });
            paramWebcamName = webcamName;
            bool1 = bool;
            if (webcamName != null) {
              webcamName.releaseRef();
              paramWebcamName = null;
              bool1 = bool;
            } 
          }  
        uvcDevice.releaseRef();
        webcamName = paramWebcamName;
      } finally {
        uvcDevice.releaseRef();
      } 
    } 
    if (webcamName != null)
      webcamName.cacheWebcamName(); 
    return (UvcDevice)webcamName;
  }
  
  public List<CameraName> getAllCameras() {
    ArrayList<WebcamName> arrayList = new ArrayList();
    Iterator<?> iterator = (new HashSet(getAllWebcams())).iterator();
    while (iterator.hasNext())
      arrayList.add((WebcamName)iterator.next()); 
    int j = Camera.getNumberOfCameras();
    for (int i = 0; i < j; i++) {
      Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
      Camera.getCameraInfo(i, cameraInfo);
      int k = cameraInfo.facing;
      if (k != 0) {
        if (k == 1)
          arrayList.add(nameFromCameraDirection(VuforiaLocalizer.CameraDirection.FRONT)); 
      } else {
        arrayList.add(nameFromCameraDirection(VuforiaLocalizer.CameraDirection.BACK));
      } 
    } 
    return (List)arrayList;
  }
  
  public List<WebcamName> getAllWebcams() {
    ArrayList<WebcamName> arrayList = new ArrayList();
    for (UvcDevice uvcDevice : getOrMakeUvcContext().getDeviceList()) {
      arrayList.add(uvcDevice.getWebcamName());
      uvcDevice.releaseRef();
    } 
    return arrayList;
  }
  
  public List<LibUsbDevice> getMatchingLibUsbDevices(Function<SerialNumber, Boolean> paramFunction) {
    ArrayList<LibUsbDevice> arrayList = new ArrayList();
    for (UsbDevice usbDevice : this.usbManager.getDeviceList().values()) {
      SerialNumber serialNumber = getRealOrVendorProductSerialNumber(usbDevice);
      if (serialNumber != null && ((Boolean)paramFunction.apply(serialNumber)).booleanValue())
        arrayList.add(getOrMakeUvcContext().getLibUsbDeviceFromUsbDevice(usbDevice, true)); 
    } 
    return arrayList;
  }
  
  protected UvcContext getOrMakeUvcContext() {
    synchronized (this.lock) {
      if (this.uvcContext == null) {
        this.uvcContext = new UvcContext(this, AppUtil.getInstance().getUsbFileSystemRoot());
      } else if (this.uvcContext.getUsbFileSystemRoot() == null) {
        String str = AppUtil.getInstance().getUsbFileSystemRoot();
        if (str != null) {
          this.uvcContext.releaseRef();
          this.uvcContext = new UvcContext(this, str);
        } 
      } 
      return this.uvcContext;
    } 
  }
  
  public SerialNumber getRealOrVendorProductSerialNumber(UsbDevice paramUsbDevice) {
    return getOrMakeUvcContext().getRealOrVendorProductSerialNumber(paramUsbDevice);
  }
  
  public Executor getSerialThreadPool() {
    return this.serialThreadPool;
  }
  
  public String getTag() {
    return "CameraManager";
  }
  
  public String getTraceIdentifier() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(super.getTraceIdentifier());
    stringBuilder.append(Misc.formatInvariant("|inst#=%d", new Object[] { Integer.valueOf(this.instanceNumber) }));
    return stringBuilder.toString();
  }
  
  public UsbManager getUsbManager() {
    return this.usbManager;
  }
  
  public boolean isWebcamAttached(SerialNumber paramSerialNumber) {
    return WebcamNameImpl.isAttached(this, paramSerialNumber);
  }
  
  public SwitchableCameraName nameForSwitchableCamera(CameraName... paramVarArgs) {
    return SwitchableCameraNameImpl.forSwitchable(paramVarArgs);
  }
  
  public CameraName nameForUnknownCamera() {
    return UnknownCameraNameImpl.forUnknown();
  }
  
  public BuiltinCameraName nameFromCameraDirection(VuforiaLocalizer.CameraDirection paramCameraDirection) {
    return BuiltinCameraNameImpl.forCameraDirection(paramCameraDirection);
  }
  
  public void onUsbFileSystemRootChanged(String paramString) {
    if (paramString != null) {
      RobotLog.ii("CameraManager", "found USB file system root: %s", new Object[] { paramString });
      getOrMakeUvcContext();
    } 
  }
  
  public void registerReceiver(CameraManagerInternal.UsbAttachmentCallback paramUsbAttachmentCallback) {
    synchronized (this.callbacks) {
      if (!this.callbacks.contains(paramUsbAttachmentCallback))
        this.callbacks.add(paramUsbAttachmentCallback); 
      return;
    } 
  }
  
  public Camera requestPermissionAndOpenCamera(final Deadline deadline, final CameraName cameraName, final Continuation<? extends Camera.StateCallback> userContinuation) {
    return (Camera)this.tracer.trace("doOpenCamera()", new Supplier<Camera>() {
          public Camera get() {
            final ContinuationSynchronizer synchronizer = new ContinuationSynchronizer(deadline, CameraManagerImpl.TRACE);
            CameraManagerImpl.this.tracer.trace("requesting permission for camera: %s", new Object[] { this.val$cameraName });
            cameraName.asyncRequestCameraPermission(AppUtil.getDefContext(), continuationSynchronizer.getDeadline(), Continuation.create(ThreadPool.getDefault(), new Consumer<Boolean>() {
                    public void accept(Boolean param2Boolean) {
                      if (param2Boolean.booleanValue()) {
                        Continuation<? extends Camera.StateCallback> continuation;
                        CameraManagerImpl.this.tracer.trace("permission granted for camera: %s", new Object[] { this.this$1.val$cameraName });
                        Camera.StateCallbackDefault stateCallbackDefault = new Camera.StateCallbackDefault() {
                            public void onClosed(Camera param3Camera) {
                              CameraImpl.addRefCamera(param3Camera);
                              if (userContinuation != null)
                                ((Camera.StateCallback)userContinuation.getTarget()).onClosed(param3Camera); 
                              CameraImpl.releaseRefCamera(param3Camera);
                            }
                            
                            public void onError(Camera param3Camera, Camera.Error param3Error) {
                              CameraImpl.addRefCamera(param3Camera);
                              if (userContinuation != null)
                                ((Camera.StateCallback)userContinuation.getTarget()).onError(param3Camera, param3Error); 
                              CameraImpl.releaseRefCamera(param3Camera);
                            }
                            
                            public void onOpenFailed(CameraName param3CameraName, Camera.OpenFailure param3OpenFailure) {
                              if (userContinuation != null)
                                ((Camera.StateCallback)userContinuation.getTarget()).onOpenFailed(param3CameraName, param3OpenFailure); 
                              synchronizer.finish(Misc.formatInvariant("camera failed to open: %s", new Object[] { param3CameraName }), null);
                            }
                            
                            public void onOpened(Camera param3Camera) {
                              CameraImpl.addRefCamera(param3Camera);
                              if (userContinuation != null)
                                ((Camera.StateCallback)userContinuation.getTarget()).onOpened(param3Camera); 
                              synchronizer.finish(CameraManagerImpl.this.tracer.format("camera reports opened: %s", new Object[] { this.this$2.this$1.val$cameraName }), param3Camera);
                              CameraImpl.releaseRefCamera(param3Camera);
                            }
                          };
                        if (userContinuation == null) {
                          continuation = Continuation.create(ThreadPool.getDefault(), stateCallbackDefault);
                        } else {
                          continuation = userContinuation.createForNewTarget(continuation);
                        } 
                        try {
                          CameraManagerImpl.this.asyncOpenCameraAssumingPermission(cameraName, continuation, deadline.getDuration(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
                          return;
                        } catch (RuntimeException runtimeException) {
                          CameraManagerImpl.this.tracer.traceError(runtimeException, "exception opening camera: %s", new Object[] { this.this$1.val$cameraName });
                          ContinuationSynchronizer continuationSynchronizer = synchronizer;
                          StringBuilder stringBuilder = new StringBuilder();
                          stringBuilder.append("exception opening camera: ");
                          stringBuilder.append(cameraName);
                          continuationSynchronizer.finish(stringBuilder.toString(), null);
                          return;
                        } 
                      } 
                      RobotLog.ee("CameraManager", "permission declined for camera: %s", new Object[] { this.this$1.val$cameraName });
                      synchronizer.finish("permission declined", null);
                    }
                  }));
            try {
              continuationSynchronizer.await("camera open");
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
            } 
            return (Camera)continuationSynchronizer.getValue();
          }
        });
  }
  
  public void unregisterReceiver(CameraManagerInternal.UsbAttachmentCallback paramUsbAttachmentCallback) {
    synchronized (this.callbacks) {
      this.callbacks.remove(paramUsbAttachmentCallback);
      return;
    } 
  }
  
  public WebcamName webcamNameFromDevice(UsbDevice paramUsbDevice) {
    SerialNumber serialNumber = getRealOrVendorProductSerialNumber(paramUsbDevice);
    if (serialNumber != null)
      return WebcamNameImpl.forSerialNumber(serialNumber); 
    this.tracer.traceError("unable to determine webcamName for %s", new Object[] { paramUsbDevice.getDeviceName() });
    return null;
  }
  
  public WebcamName webcamNameFromDevice(LibUsbDevice paramLibUsbDevice) {
    return WebcamNameImpl.forSerialNumber(paramLibUsbDevice.getRealOrVendorProductSerialNumber());
  }
  
  public WebcamName webcamNameFromSerialNumber(SerialNumber paramSerialNumber, ArmableUsbDevice.OpenRobotUsbDevice paramOpenRobotUsbDevice, SyncdDevice.Manager paramManager) {
    return WebcamNameImpl.forSerialNumber(paramSerialNumber, paramOpenRobotUsbDevice, paramManager);
  }
  
  class UsbAttachmentMonitor extends BroadcastReceiver {
    public void onReceive(Context param1Context, Intent param1Intent) {
      String str = param1Intent.getAction();
      if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(str)) {
        CameraManagerImpl.this.tracer.trace("---------------------------------------------- ACTION_USB_DEVICE_DETACHED", new Object[0]);
        null = (UsbDevice)param1Intent.getParcelableExtra("device");
        synchronized (CameraManagerImpl.this.callbacks) {
          Iterator<CameraManagerInternal.UsbAttachmentCallback> iterator = CameraManagerImpl.this.callbacks.iterator();
          while (iterator.hasNext())
            ((CameraManagerInternal.UsbAttachmentCallback)iterator.next()).onDetached(null); 
          return;
        } 
      } 
      if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(str)) {
        CameraManagerImpl.this.tracer.trace("---------------------------------------------- ACTION_USB_DEVICE_ATTACHED", new Object[0]);
        null = (UsbDevice)param1Intent.getParcelableExtra("device");
        synchronized (CameraManagerImpl.this.callbacks) {
          SerialNumber serialNumber = CameraManagerImpl.this.getRealOrVendorProductSerialNumber(null);
          if (serialNumber != null) {
            MutableReference<Boolean> mutableReference = new MutableReference(Boolean.valueOf(false));
            Iterator<CameraManagerInternal.UsbAttachmentCallback> iterator = CameraManagerImpl.this.callbacks.iterator();
            while (iterator.hasNext())
              ((CameraManagerInternal.UsbAttachmentCallback)iterator.next()).onAttached(null, serialNumber, mutableReference); 
          } else {
            CameraManagerImpl.this.tracer.traceError("unable to determine serial number of %s; ignoring", new Object[] { null.getDeviceName() });
          } 
          return;
        } 
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\CameraManagerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */