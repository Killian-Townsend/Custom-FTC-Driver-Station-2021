package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import android.hardware.usb.UsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.CameraManagerInternal;
import org.firstinspires.ftc.robotcore.internal.camera.CameraState;
import org.firstinspires.ftc.robotcore.internal.camera.RefCountedCamera;
import org.firstinspires.ftc.robotcore.internal.collections.MutableReference;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public class UsbResiliantWebcam extends DelegatingCamera implements RefCountedCamera {
  public static final String TAG = "UsbResiliantWebcam";
  
  protected final InterveningStateCallback interveningStateCallback;
  
  protected long reopenDuration;
  
  protected TimeUnit reopenTimeUnit;
  
  protected String selfUsbDeviceName = null;
  
  protected final WebcamName selfWebcamName;
  
  protected final UsbAttachmentMonitor usbAttachmentMonitor;
  
  protected Semaphore usbAttachmentSemaphore = new Semaphore(1);
  
  protected UsbMonitoringState usbMonitoringState = UsbMonitoringState.Unknown;
  
  public UsbResiliantWebcam(CameraManagerInternal paramCameraManagerInternal, WebcamName paramWebcamName, Continuation<? extends Camera.StateCallback> paramContinuation) {
    super(paramCameraManagerInternal, (CameraName)paramWebcamName, paramContinuation);
    this.selfWebcamName = paramWebcamName;
    this.interveningStateCallback = new InterveningStateCallback(paramContinuation);
    UsbAttachmentMonitor usbAttachmentMonitor = new UsbAttachmentMonitor();
    this.usbAttachmentMonitor = usbAttachmentMonitor;
    paramCameraManagerInternal.registerReceiver(usbAttachmentMonitor);
  }
  
  protected void asyncRequestPermissionAndOpenCamera(final Runnable runOnCompletion) {
    this.tracer.trace("asyncRequestPermissionAndOpenCamera()", new Runnable() {
          public void run() {
            synchronized (UsbResiliantWebcam.this.outerLock) {
              Assert.assertNull(UsbResiliantWebcam.this.delegatedCamera);
              UsbResiliantWebcam.this.selfWebcamName.asyncRequestCameraPermission(UsbResiliantWebcam.this.context, new Deadline(UsbResiliantWebcam.this.reopenDuration, UsbResiliantWebcam.this.reopenTimeUnit), Continuation.create(UsbResiliantWebcam.this.serialThreadPool, new Consumer<Boolean>() {
                      public void accept(Boolean param2Boolean) {
                        if (param2Boolean.booleanValue()) {
                          UsbResiliantWebcam.this.openAssumingPermission();
                        } else {
                          UsbResiliantWebcam.this.tracer.traceError("permission declined for cameara: %s", new Object[] { this.this$1.this$0.selfWebcamName });
                        } 
                        if (runOnCompletion != null)
                          runOnCompletion.run(); 
                      }
                    }));
              return;
            } 
          }
        });
  }
  
  protected void constructControls() {
    this.delegatingCameraControls.add(new CachingFocusControl());
    this.delegatingCameraControls.add(new CachingExposureControl());
  }
  
  protected void destructor() {
    this.tracer.trace("destructor()", new Runnable() {
          public void run() {
            UsbResiliantWebcam.this.cameraManager.unregisterReceiver(UsbResiliantWebcam.this.usbAttachmentMonitor);
            UsbResiliantWebcam.this.destructor();
          }
        });
  }
  
  public String getTag() {
    return "UsbResiliantWebcam";
  }
  
  protected void openAssumingPermission() {
    this.tracer.trace("openAssumingPermission()", new Runnable() {
          public void run() {
            // Byte code:
            //   0: aload_0
            //   1: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   4: getfield outerLock : Ljava/lang/Object;
            //   7: astore #5
            //   9: aload #5
            //   11: monitorenter
            //   12: getstatic org/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$4.$SwitchMap$org$firstinspires$ftc$robotcore$internal$camera$CameraState : [I
            //   15: aload_0
            //   16: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   19: getfield selfState : Lorg/firstinspires/ftc/robotcore/internal/camera/CameraState;
            //   22: invokevirtual ordinal : ()I
            //   25: iaload
            //   26: tableswitch default -> 290, 1 -> 273, 2 -> 273, 3 -> 71, 4 -> 71, 5 -> 71, 6 -> 71
            //   64: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
            //   67: astore_3
            //   68: goto -> 277
            //   71: aload_0
            //   72: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   75: getfield delegatedCamera : Lorg/firstinspires/ftc/robotcore/external/hardware/camera/Camera;
            //   78: invokestatic assertNull : (Ljava/lang/Object;)V
            //   81: getstatic org/firstinspires/ftc/robotcore/external/hardware/camera/Camera$OpenFailure.InternalError : Lorg/firstinspires/ftc/robotcore/external/hardware/camera/Camera$OpenFailure;
            //   84: astore #4
            //   86: iconst_0
            //   87: istore_1
            //   88: aload_0
            //   89: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   92: getfield cameraManager : Lorg/firstinspires/ftc/robotcore/internal/camera/CameraManagerInternal;
            //   95: aload_0
            //   96: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   99: getfield selfWebcamName : Lorg/firstinspires/ftc/robotcore/external/hardware/camera/WebcamName;
            //   102: invokeinterface findUvcDevice : (Lorg/firstinspires/ftc/robotcore/external/hardware/camera/WebcamName;)Lorg/firstinspires/ftc/robotcore/internal/camera/libuvc/nativeobject/UvcDevice;
            //   107: astore #6
            //   109: aload #6
            //   111: ifnull -> 203
            //   114: aload_0
            //   115: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   118: aload_0
            //   119: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   122: getfield selfWebcamName : Lorg/firstinspires/ftc/robotcore/external/hardware/camera/WebcamName;
            //   125: invokeinterface getUsbDeviceNameIfAttached : ()Ljava/lang/String;
            //   130: putfield selfUsbDeviceName : Ljava/lang/String;
            //   133: aload #6
            //   135: aload_0
            //   136: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   139: getfield selfWebcamName : Lorg/firstinspires/ftc/robotcore/external/hardware/camera/WebcamName;
            //   142: aload_0
            //   143: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   146: getfield interveningStateCallback : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$InterveningStateCallback;
            //   149: invokevirtual open : (Lorg/firstinspires/ftc/robotcore/external/hardware/camera/WebcamName;Lorg/firstinspires/ftc/robotcore/external/hardware/camera/Camera$StateCallback;)Lorg/firstinspires/ftc/robotcore/internal/camera/libuvc/nativeobject/UvcDeviceHandle;
            //   152: astore_3
            //   153: iconst_1
            //   154: istore_2
            //   155: iconst_1
            //   156: istore_1
            //   157: aload_3
            //   158: ifnull -> 173
            //   161: aload_3
            //   162: invokevirtual releaseRef : ()I
            //   165: pop
            //   166: goto -> 173
            //   169: astore_3
            //   170: goto -> 187
            //   173: aload #6
            //   175: invokevirtual releaseRef : ()I
            //   178: pop
            //   179: iconst_1
            //   180: istore_1
            //   181: aload #4
            //   183: astore_3
            //   184: goto -> 223
            //   187: iload_1
            //   188: istore_2
            //   189: aload #6
            //   191: invokevirtual releaseRef : ()I
            //   194: pop
            //   195: iload_1
            //   196: istore_2
            //   197: aload_3
            //   198: athrow
            //   199: astore_3
            //   200: goto -> 248
            //   203: aload_0
            //   204: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   207: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
            //   210: ldc 'can't find uvcDevice'
            //   212: iconst_0
            //   213: anewarray java/lang/Object
            //   216: invokevirtual trace : (Ljava/lang/String;[Ljava/lang/Object;)V
            //   219: getstatic org/firstinspires/ftc/robotcore/external/hardware/camera/Camera$OpenFailure.Disconnected : Lorg/firstinspires/ftc/robotcore/external/hardware/camera/Camera$OpenFailure;
            //   222: astore_3
            //   223: iload_1
            //   224: ifne -> 273
            //   227: aload_0
            //   228: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   231: getfield interveningStateCallback : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$InterveningStateCallback;
            //   234: aload_0
            //   235: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   238: getfield selfCameraName : Lorg/firstinspires/ftc/robotcore/external/hardware/camera/CameraName;
            //   241: aload_3
            //   242: invokevirtual onOpenFailed : (Lorg/firstinspires/ftc/robotcore/external/hardware/camera/CameraName;Lorg/firstinspires/ftc/robotcore/external/hardware/camera/Camera$OpenFailure;)V
            //   245: goto -> 273
            //   248: iload_2
            //   249: ifne -> 271
            //   252: aload_0
            //   253: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   256: getfield interveningStateCallback : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$InterveningStateCallback;
            //   259: aload_0
            //   260: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
            //   263: getfield selfCameraName : Lorg/firstinspires/ftc/robotcore/external/hardware/camera/CameraName;
            //   266: aload #4
            //   268: invokevirtual onOpenFailed : (Lorg/firstinspires/ftc/robotcore/external/hardware/camera/CameraName;Lorg/firstinspires/ftc/robotcore/external/hardware/camera/Camera$OpenFailure;)V
            //   271: aload_3
            //   272: athrow
            //   273: aload #5
            //   275: monitorexit
            //   276: return
            //   277: aload_3
            //   278: ldc 'UsbResiliantWebcam'
            //   280: invokevirtual unreachable : (Ljava/lang/String;)Ljava/lang/RuntimeException;
            //   283: athrow
            //   284: astore_3
            //   285: aload #5
            //   287: monitorexit
            //   288: aload_3
            //   289: athrow
            //   290: goto -> 64
            //   293: astore_3
            //   294: iconst_0
            //   295: istore_1
            //   296: goto -> 187
            //   299: astore_3
            //   300: iconst_0
            //   301: istore_2
            //   302: goto -> 248
            // Exception table:
            //   from	to	target	type
            //   12	64	284	finally
            //   64	68	284	finally
            //   71	86	284	finally
            //   88	109	299	finally
            //   114	153	293	finally
            //   161	166	169	finally
            //   173	179	199	finally
            //   189	195	199	finally
            //   197	199	199	finally
            //   203	223	299	finally
            //   227	245	284	finally
            //   252	271	284	finally
            //   271	273	284	finally
            //   273	276	284	finally
            //   277	284	284	finally
            //   285	288	284	finally
          }
        });
  }
  
  public void openAssumingPermission(long paramLong, TimeUnit paramTimeUnit) {
    synchronized (this.outerLock) {
      this.reopenDuration = paramLong;
      this.reopenTimeUnit = paramTimeUnit;
      openAssumingPermission();
      return;
    } 
  }
  
  class InterveningStateCallback implements Camera.StateCallback {
    protected final Continuation<? extends Camera.StateCallback> interveningContinuation;
    
    protected final Tracer tracer;
    
    InterveningStateCallback(Continuation<? extends Camera.StateCallback> param1Continuation) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(UsbResiliantWebcam.this.getTag());
      stringBuilder.append(".InterveningStateCallback");
      this.tracer = Tracer.create(stringBuilder.toString(), DelegatingCamera.TRACE);
      this.interveningContinuation = Continuation.create(UsbResiliantWebcam.this.serialThreadPool, this);
    }
    
    public void onClosed(Camera param1Camera) {
      synchronized (UsbResiliantWebcam.this.outerLock) {
        if (UsbResiliantWebcam.this.selfState != CameraState.Closed && UsbResiliantWebcam.this.delegatedCameraState != CameraState.Disconnected) {
          this.tracer.traceError("unexpected closing internal camera W/O going through disconnection path", new Object[0]);
          UsbResiliantWebcam.this.reportSelfClosed();
        } 
        UsbResiliantWebcam.this.delegatedCameraState = CameraState.Closed;
        UsbResiliantWebcam.this.changeDelegatedCamera(null);
        return;
      } 
    }
    
    public void onError(Camera param1Camera, Camera.Error param1Error) {
      synchronized (UsbResiliantWebcam.this.outerLock) {
        UsbResiliantWebcam.this.dispatchingCallback.onError(UsbResiliantWebcam.this.selfCamera, param1Error);
        return;
      } 
    }
    
    public void onOpenFailed(CameraName param1CameraName, Camera.OpenFailure param1OpenFailure) {
      synchronized (UsbResiliantWebcam.this.outerLock) {
        Assert.assertTrue(UsbResiliantWebcam.this.selfWebcamName.equals(param1CameraName));
        UsbResiliantWebcam.this.delegatedCameraState = CameraState.FailedOpen;
        UsbResiliantWebcam.this.changeDelegatedCamera(null);
        UsbResiliantWebcam.this.reportOpenFailed(param1OpenFailure);
        return;
      } 
    }
    
    public void onOpened(final Camera cameraOpened) {
      Tracer tracer = this.tracer;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("onOpened() camera=");
      stringBuilder.append(cameraOpened);
      tracer.trace(stringBuilder.toString(), new Runnable() {
            public void run() {
              synchronized (UsbResiliantWebcam.this.outerLock) {
                UsbResiliantWebcam.this.usbMonitoringState = UsbResiliantWebcam.UsbMonitoringState.Connected;
                UsbResiliantWebcam.this.delegatedCameraState = CameraState.OpenNotStarted;
                UsbResiliantWebcam.this.changeDelegatedCamera(cameraOpened);
                UsbResiliantWebcam.this.openSelfAndReport();
                return;
              } 
            }
          });
    }
  }
  
  class null implements Runnable {
    public void run() {
      synchronized (UsbResiliantWebcam.this.outerLock) {
        UsbResiliantWebcam.this.usbMonitoringState = UsbResiliantWebcam.UsbMonitoringState.Connected;
        UsbResiliantWebcam.this.delegatedCameraState = CameraState.OpenNotStarted;
        UsbResiliantWebcam.this.changeDelegatedCamera(cameraOpened);
        UsbResiliantWebcam.this.openSelfAndReport();
        return;
      } 
    }
  }
  
  class UsbAttachmentMonitor implements CameraManagerInternal.UsbAttachmentCallback {
    public void onAttached(UsbDevice param1UsbDevice, SerialNumber param1SerialNumber, MutableReference<Boolean> param1MutableReference) {
      synchronized (UsbResiliantWebcam.this.outerLock) {
        boolean bool;
        if (!((Boolean)param1MutableReference.getValue()).booleanValue() && param1SerialNumber.matches(UsbResiliantWebcam.this.selfWebcamName.getSerialNumber())) {
          bool = true;
        } else {
          bool = false;
        } 
        UsbResiliantWebcam.this.tracer.trace("webcam=%s serialNumberJustAttached=%s reclaim=%s", new Object[] { this.this$0.selfWebcamName.getSerialNumber(), param1SerialNumber, Boolean.valueOf(bool) });
        if (bool) {
          UsbResiliantWebcam.this.tracer.trace("ACTION_USB_DEVICE_ATTACHED: camera attached: scheduling reopen: %s", new Object[] { this.this$0.selfWebcamName });
          param1MutableReference.setValue(Boolean.valueOf(true));
          UsbResiliantWebcam.this.usbMonitoringState = UsbResiliantWebcam.UsbMonitoringState.Connected;
          UsbResiliantWebcam.this.openClosePool.execute(new Runnable() {
                public void run() {
                  UsbResiliantWebcam.this.tracer.trace("doingReopen()", new Runnable() {
                        public void run() {
                          // Byte code:
                          //   0: iconst_0
                          //   1: istore #4
                          //   3: iconst_0
                          //   4: istore_1
                          //   5: iconst_0
                          //   6: istore_2
                          //   7: iconst_0
                          //   8: istore_3
                          //   9: aload_0
                          //   10: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
                          //   13: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
                          //   16: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
                          //   19: getfield usbAttachmentSemaphore : Ljava/util/concurrent/Semaphore;
                          //   22: invokevirtual acquire : ()V
                          //   25: aload_0
                          //   26: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
                          //   29: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
                          //   32: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
                          //   35: getfield outerLock : Ljava/lang/Object;
                          //   38: astore #6
                          //   40: aload #6
                          //   42: monitorenter
                          //   43: aload_0
                          //   44: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
                          //   47: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
                          //   50: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
                          //   53: getfield usbMonitoringState : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbMonitoringState;
                          //   56: getstatic org/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbMonitoringState.Connected : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbMonitoringState;
                          //   59: if_acmpne -> 219
                          //   62: aload_0
                          //   63: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
                          //   66: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
                          //   69: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
                          //   72: getfield delegatedCamera : Lorg/firstinspires/ftc/robotcore/external/hardware/camera/Camera;
                          //   75: astore #5
                          //   77: aload #5
                          //   79: ifnonnull -> 219
                          //   82: iload #4
                          //   84: istore_1
                          //   85: aload_0
                          //   86: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
                          //   89: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
                          //   92: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
                          //   95: new org/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2$1$1
                          //   98: dup
                          //   99: aload_0
                          //   100: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2$1;)V
                          //   103: invokevirtual asyncRequestPermissionAndOpenCamera : (Ljava/lang/Runnable;)V
                          //   106: iload_3
                          //   107: istore_2
                          //   108: goto -> 111
                          //   111: iload_2
                          //   112: istore_1
                          //   113: aload #6
                          //   115: monitorexit
                          //   116: iload_2
                          //   117: ifeq -> 185
                          //   120: goto -> 169
                          //   123: iload_2
                          //   124: istore_1
                          //   125: aload #6
                          //   127: monitorexit
                          //   128: iload_2
                          //   129: istore_1
                          //   130: aload #5
                          //   132: athrow
                          //   133: astore #5
                          //   135: iload_1
                          //   136: istore_2
                          //   137: goto -> 123
                          //   140: astore #5
                          //   142: iconst_1
                          //   143: istore_1
                          //   144: goto -> 186
                          //   147: iconst_1
                          //   148: istore_2
                          //   149: goto -> 157
                          //   152: astore #5
                          //   154: goto -> 186
                          //   157: iload_2
                          //   158: istore_1
                          //   159: invokestatic currentThread : ()Ljava/lang/Thread;
                          //   162: invokevirtual interrupt : ()V
                          //   165: iload_2
                          //   166: ifeq -> 185
                          //   169: aload_0
                          //   170: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
                          //   173: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
                          //   176: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
                          //   179: getfield usbAttachmentSemaphore : Ljava/util/concurrent/Semaphore;
                          //   182: invokevirtual release : ()V
                          //   185: return
                          //   186: iload_1
                          //   187: ifeq -> 206
                          //   190: aload_0
                          //   191: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
                          //   194: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
                          //   197: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
                          //   200: getfield usbAttachmentSemaphore : Ljava/util/concurrent/Semaphore;
                          //   203: invokevirtual release : ()V
                          //   206: aload #5
                          //   208: athrow
                          //   209: astore #5
                          //   211: goto -> 157
                          //   214: astore #5
                          //   216: goto -> 147
                          //   219: iconst_1
                          //   220: istore_2
                          //   221: goto -> 111
                          //   224: astore #5
                          //   226: iconst_1
                          //   227: istore_2
                          //   228: goto -> 123
                          // Exception table:
                          //   from	to	target	type
                          //   9	25	209	java/lang/InterruptedException
                          //   9	25	152	finally
                          //   25	43	214	java/lang/InterruptedException
                          //   25	43	140	finally
                          //   43	77	224	finally
                          //   85	106	133	finally
                          //   113	116	133	finally
                          //   125	128	133	finally
                          //   130	133	209	java/lang/InterruptedException
                          //   130	133	152	finally
                          //   159	165	152	finally
                        }
                      });
                }
              });
        } 
        return;
      } 
    }
    
    public void onDetached(UsbDevice param1UsbDevice) {
      synchronized (UsbResiliantWebcam.this.outerLock) {
        if (param1UsbDevice.getDeviceName().equals(UsbResiliantWebcam.this.selfUsbDeviceName)) {
          UsbResiliantWebcam.this.tracer.trace("ACTION_USB_DEVICE_DETACHED: camera detached: scheduling disconnect: %s(%s)", new Object[] { this.this$0.selfWebcamName, this.this$0.selfUsbDeviceName });
          UsbResiliantWebcam.this.usbMonitoringState = UsbResiliantWebcam.UsbMonitoringState.Disconnected;
          UsbResiliantWebcam.this.openClosePool.execute(new Runnable() {
                public void run() {
                  UsbResiliantWebcam.this.tracer.trace("doingDisconnect()", new Runnable() {
                        public void run() {
                          boolean bool2 = false;
                          boolean bool1 = false;
                          try {
                            UsbResiliantWebcam.this.usbAttachmentSemaphore.acquire();
                            boolean bool4 = true;
                            boolean bool3 = true;
                            bool1 = bool3;
                            bool2 = bool4;
                            Object object = UsbResiliantWebcam.this.outerLock;
                            bool1 = bool3;
                            synchronized (bool4) {
                              if (UsbResiliantWebcam.this.usbMonitoringState == UsbResiliantWebcam.UsbMonitoringState.Disconnected && UsbResiliantWebcam.this.delegatedCamera != null) {
                                UsbResiliantWebcam.this.delegatedCameraState = CameraState.Disconnected;
                                UsbResiliantWebcam.this.closeDelegatedCameras();
                              } 
                              /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
                            } 
                          } catch (InterruptedException interruptedException) {
                            bool1 = bool2;
                            Thread.currentThread().interrupt();
                          } finally {
                            if (bool1)
                              UsbResiliantWebcam.this.usbAttachmentSemaphore.release(); 
                          } 
                        }
                      });
                }
              });
        } 
        return;
      } 
    }
  }
  
  class null implements Runnable {
    public void run() {
      UsbResiliantWebcam.this.tracer.trace("doingDisconnect()", new Runnable() {
            public void run() {
              boolean bool2 = false;
              boolean bool1 = false;
              try {
                UsbResiliantWebcam.this.usbAttachmentSemaphore.acquire();
                boolean bool4 = true;
                boolean bool3 = true;
                bool1 = bool3;
                bool2 = bool4;
                Object object = UsbResiliantWebcam.this.outerLock;
                bool1 = bool3;
                synchronized (bool4) {
                  if (UsbResiliantWebcam.this.usbMonitoringState == UsbResiliantWebcam.UsbMonitoringState.Disconnected && UsbResiliantWebcam.this.delegatedCamera != null) {
                    UsbResiliantWebcam.this.delegatedCameraState = CameraState.Disconnected;
                    UsbResiliantWebcam.this.closeDelegatedCameras();
                  } 
                  /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
                } 
              } catch (InterruptedException interruptedException) {
                bool1 = bool2;
                Thread.currentThread().interrupt();
              } finally {
                if (bool1)
                  UsbResiliantWebcam.this.usbAttachmentSemaphore.release(); 
              } 
            }
          });
    }
  }
  
  class null implements Runnable {
    public void run() {
      boolean bool2 = false;
      boolean bool1 = false;
      try {
        UsbResiliantWebcam.this.usbAttachmentSemaphore.acquire();
        boolean bool4 = true;
        boolean bool3 = true;
        bool1 = bool3;
        bool2 = bool4;
        Object object = UsbResiliantWebcam.this.outerLock;
        bool1 = bool3;
        synchronized (bool4) {
          if (UsbResiliantWebcam.this.usbMonitoringState == UsbResiliantWebcam.UsbMonitoringState.Disconnected && UsbResiliantWebcam.this.delegatedCamera != null) {
            UsbResiliantWebcam.this.delegatedCameraState = CameraState.Disconnected;
            UsbResiliantWebcam.this.closeDelegatedCameras();
          } 
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
        } 
      } catch (InterruptedException interruptedException) {
        bool1 = bool2;
        Thread.currentThread().interrupt();
      } finally {
        if (bool1)
          UsbResiliantWebcam.this.usbAttachmentSemaphore.release(); 
      } 
    }
  }
  
  class null implements Runnable {
    public void run() {
      UsbResiliantWebcam.this.tracer.trace("doingReopen()", new Runnable() {
            public void run() {
              // Byte code:
              //   0: iconst_0
              //   1: istore #4
              //   3: iconst_0
              //   4: istore_1
              //   5: iconst_0
              //   6: istore_2
              //   7: iconst_0
              //   8: istore_3
              //   9: aload_0
              //   10: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
              //   13: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
              //   16: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
              //   19: getfield usbAttachmentSemaphore : Ljava/util/concurrent/Semaphore;
              //   22: invokevirtual acquire : ()V
              //   25: aload_0
              //   26: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
              //   29: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
              //   32: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
              //   35: getfield outerLock : Ljava/lang/Object;
              //   38: astore #6
              //   40: aload #6
              //   42: monitorenter
              //   43: aload_0
              //   44: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
              //   47: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
              //   50: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
              //   53: getfield usbMonitoringState : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbMonitoringState;
              //   56: getstatic org/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbMonitoringState.Connected : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbMonitoringState;
              //   59: if_acmpne -> 219
              //   62: aload_0
              //   63: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
              //   66: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
              //   69: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
              //   72: getfield delegatedCamera : Lorg/firstinspires/ftc/robotcore/external/hardware/camera/Camera;
              //   75: astore #5
              //   77: aload #5
              //   79: ifnonnull -> 219
              //   82: iload #4
              //   84: istore_1
              //   85: aload_0
              //   86: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
              //   89: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
              //   92: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
              //   95: new org/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2$1$1
              //   98: dup
              //   99: aload_0
              //   100: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2$1;)V
              //   103: invokevirtual asyncRequestPermissionAndOpenCamera : (Ljava/lang/Runnable;)V
              //   106: iload_3
              //   107: istore_2
              //   108: goto -> 111
              //   111: iload_2
              //   112: istore_1
              //   113: aload #6
              //   115: monitorexit
              //   116: iload_2
              //   117: ifeq -> 185
              //   120: goto -> 169
              //   123: iload_2
              //   124: istore_1
              //   125: aload #6
              //   127: monitorexit
              //   128: iload_2
              //   129: istore_1
              //   130: aload #5
              //   132: athrow
              //   133: astore #5
              //   135: iload_1
              //   136: istore_2
              //   137: goto -> 123
              //   140: astore #5
              //   142: iconst_1
              //   143: istore_1
              //   144: goto -> 186
              //   147: iconst_1
              //   148: istore_2
              //   149: goto -> 157
              //   152: astore #5
              //   154: goto -> 186
              //   157: iload_2
              //   158: istore_1
              //   159: invokestatic currentThread : ()Ljava/lang/Thread;
              //   162: invokevirtual interrupt : ()V
              //   165: iload_2
              //   166: ifeq -> 185
              //   169: aload_0
              //   170: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
              //   173: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
              //   176: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
              //   179: getfield usbAttachmentSemaphore : Ljava/util/concurrent/Semaphore;
              //   182: invokevirtual release : ()V
              //   185: return
              //   186: iload_1
              //   187: ifeq -> 206
              //   190: aload_0
              //   191: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
              //   194: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
              //   197: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
              //   200: getfield usbAttachmentSemaphore : Ljava/util/concurrent/Semaphore;
              //   203: invokevirtual release : ()V
              //   206: aload #5
              //   208: athrow
              //   209: astore #5
              //   211: goto -> 157
              //   214: astore #5
              //   216: goto -> 147
              //   219: iconst_1
              //   220: istore_2
              //   221: goto -> 111
              //   224: astore #5
              //   226: iconst_1
              //   227: istore_2
              //   228: goto -> 123
              // Exception table:
              //   from	to	target	type
              //   9	25	209	java/lang/InterruptedException
              //   9	25	152	finally
              //   25	43	214	java/lang/InterruptedException
              //   25	43	140	finally
              //   43	77	224	finally
              //   85	106	133	finally
              //   113	116	133	finally
              //   125	128	133	finally
              //   130	133	209	java/lang/InterruptedException
              //   130	133	152	finally
              //   159	165	152	finally
            }
          });
    }
  }
  
  class null implements Runnable {
    public void run() {
      // Byte code:
      //   0: iconst_0
      //   1: istore #4
      //   3: iconst_0
      //   4: istore_1
      //   5: iconst_0
      //   6: istore_2
      //   7: iconst_0
      //   8: istore_3
      //   9: aload_0
      //   10: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
      //   13: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
      //   16: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
      //   19: getfield usbAttachmentSemaphore : Ljava/util/concurrent/Semaphore;
      //   22: invokevirtual acquire : ()V
      //   25: aload_0
      //   26: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
      //   29: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
      //   32: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
      //   35: getfield outerLock : Ljava/lang/Object;
      //   38: astore #6
      //   40: aload #6
      //   42: monitorenter
      //   43: aload_0
      //   44: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
      //   47: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
      //   50: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
      //   53: getfield usbMonitoringState : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbMonitoringState;
      //   56: getstatic org/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbMonitoringState.Connected : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbMonitoringState;
      //   59: if_acmpne -> 219
      //   62: aload_0
      //   63: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
      //   66: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
      //   69: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
      //   72: getfield delegatedCamera : Lorg/firstinspires/ftc/robotcore/external/hardware/camera/Camera;
      //   75: astore #5
      //   77: aload #5
      //   79: ifnonnull -> 219
      //   82: iload #4
      //   84: istore_1
      //   85: aload_0
      //   86: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
      //   89: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
      //   92: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
      //   95: new org/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2$1$1
      //   98: dup
      //   99: aload_0
      //   100: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2$1;)V
      //   103: invokevirtual asyncRequestPermissionAndOpenCamera : (Ljava/lang/Runnable;)V
      //   106: iload_3
      //   107: istore_2
      //   108: goto -> 111
      //   111: iload_2
      //   112: istore_1
      //   113: aload #6
      //   115: monitorexit
      //   116: iload_2
      //   117: ifeq -> 185
      //   120: goto -> 169
      //   123: iload_2
      //   124: istore_1
      //   125: aload #6
      //   127: monitorexit
      //   128: iload_2
      //   129: istore_1
      //   130: aload #5
      //   132: athrow
      //   133: astore #5
      //   135: iload_1
      //   136: istore_2
      //   137: goto -> 123
      //   140: astore #5
      //   142: iconst_1
      //   143: istore_1
      //   144: goto -> 186
      //   147: iconst_1
      //   148: istore_2
      //   149: goto -> 157
      //   152: astore #5
      //   154: goto -> 186
      //   157: iload_2
      //   158: istore_1
      //   159: invokestatic currentThread : ()Ljava/lang/Thread;
      //   162: invokevirtual interrupt : ()V
      //   165: iload_2
      //   166: ifeq -> 185
      //   169: aload_0
      //   170: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
      //   173: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
      //   176: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
      //   179: getfield usbAttachmentSemaphore : Ljava/util/concurrent/Semaphore;
      //   182: invokevirtual release : ()V
      //   185: return
      //   186: iload_1
      //   187: ifeq -> 206
      //   190: aload_0
      //   191: getfield this$2 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor$2;
      //   194: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam$UsbAttachmentMonitor;
      //   197: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/UsbResiliantWebcam;
      //   200: getfield usbAttachmentSemaphore : Ljava/util/concurrent/Semaphore;
      //   203: invokevirtual release : ()V
      //   206: aload #5
      //   208: athrow
      //   209: astore #5
      //   211: goto -> 157
      //   214: astore #5
      //   216: goto -> 147
      //   219: iconst_1
      //   220: istore_2
      //   221: goto -> 111
      //   224: astore #5
      //   226: iconst_1
      //   227: istore_2
      //   228: goto -> 123
      // Exception table:
      //   from	to	target	type
      //   9	25	209	java/lang/InterruptedException
      //   9	25	152	finally
      //   25	43	214	java/lang/InterruptedException
      //   25	43	140	finally
      //   43	77	224	finally
      //   85	106	133	finally
      //   113	116	133	finally
      //   125	128	133	finally
      //   130	133	209	java/lang/InterruptedException
      //   130	133	152	finally
      //   159	165	152	finally
    }
  }
  
  class null implements Runnable {
    public void run() {
      UsbResiliantWebcam.this.tracer.trace("doingReopen(): complete", new Object[0]);
      UsbResiliantWebcam.this.usbAttachmentSemaphore.release();
    }
  }
  
  protected enum UsbMonitoringState {
    Connected, Disconnected, Unknown;
    
    static {
      UsbMonitoringState usbMonitoringState = new UsbMonitoringState("Disconnected", 2);
      Disconnected = usbMonitoringState;
      $VALUES = new UsbMonitoringState[] { Unknown, Connected, usbMonitoringState };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\UsbResiliantWebcam.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */