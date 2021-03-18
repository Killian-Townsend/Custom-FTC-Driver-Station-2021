package org.firstinspires.ftc.robotcore.internal.camera;

import android.graphics.Bitmap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.MovingStatistics;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import java.io.File;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.Supplier;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSequenceId;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraFrame;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;

public class WebcamExample {
  public static final String TAG = "WebcamExample";
  
  public void example(Supplier<Boolean> paramSupplier) {
    CameraManager cameraManager = ClassFactory.getInstance().getCameraManager();
    Iterator<CameraName> iterator = cameraManager.getAllWebcams().iterator();
    while (iterator.hasNext())
      (new OneCameraExample(cameraManager, iterator.next(), paramSupplier)).example(); 
  }
  
  protected class OneCameraExample {
    File bitmapOutputDir = new File(AppUtil.FIRST_FOLDER, "webcam");
    
    Camera camera;
    
    final CameraManager cameraManager;
    
    final CameraName cameraName;
    
    CameraCaptureSession.StateCallback captureStateCallback = (CameraCaptureSession.StateCallback)new CameraCaptureSession.StateCallbackDefault() {
        public void onClosed(CameraCaptureSession param2CameraCaptureSession) {
          RobotLog.vv("WebcamExample", "capture session reports closed: %s", new Object[] { param2CameraCaptureSession });
        }
        
        public void onConfigured(CameraCaptureSession param2CameraCaptureSession) {
          try {
            CameraCaptureRequest cameraCaptureRequest = WebcamExample.OneCameraExample.this.camera.createCaptureRequest(WebcamExample.OneCameraExample.this.imageFormatWanted, WebcamExample.OneCameraExample.this.sizeWanted, WebcamExample.OneCameraExample.this.characteristics.getMaxFramesPerSecond(WebcamExample.OneCameraExample.this.imageFormatWanted, WebcamExample.OneCameraExample.this.sizeWanted));
            param2CameraCaptureSession.startCapture(cameraCaptureRequest, new WebcamExample.OneCameraExample.WebcamExampleCaptureCallback(cameraCaptureRequest), Continuation.create(WebcamExample.OneCameraExample.this.threadPool, new CameraCaptureSession.StatusCallback() {
                    public void onCaptureSequenceCompleted(CameraCaptureSession param3CameraCaptureSession, CameraCaptureSequenceId param3CameraCaptureSequenceId, long param3Long) {
                      RobotLog.vv("WebcamExample", "capture sequence %s reports completed: lastFrame=%d", new Object[] { param3CameraCaptureSequenceId, Long.valueOf(param3Long) });
                    }
                  }));
            null = new AppUtil.DialogParams(UILocation.ONLY_LOCAL, "Streaming Active", "Press OK to stop");
            try {
              null = new CountDownLatch(1);
              AppUtil.DialogContext dialogContext = AppUtil.getInstance().showDialog(null, ThreadPool.getDefault(), new Consumer<AppUtil.DialogContext>() {
                    public void accept(AppUtil.DialogContext param3DialogContext) {
                      latch.countDown();
                    }
                  });
              try {
                do {
                
                } while (((Boolean)WebcamExample.OneCameraExample.this.exampleShouldContinue.get()).booleanValue() && !null.await(100L, TimeUnit.MILLISECONDS));
              } catch (InterruptedException interruptedException) {
              
              } finally {
                dialogContext.dismissDialog();
              } 
              dialogContext.dismissDialog();
              return;
            } finally {
              param2CameraCaptureSession.stopCapture();
              param2CameraCaptureSession.close();
              WebcamExample.OneCameraExample.this.camera.close();
            } 
          } catch (CameraException cameraException) {
            RobotLog.ee("WebcamExample", (Throwable)cameraException, "error setting repeat capture request");
            return;
          } 
        }
      };
    
    CameraCharacteristics characteristics;
    
    Supplier<Boolean> exampleShouldContinue;
    
    int imageFormatWanted = 20;
    
    Size sizeWanted;
    
    final Executor threadPool = ThreadPool.newSingleThreadExecutor("OneCameraExample");
    
    public OneCameraExample(CameraManager param1CameraManager, CameraName param1CameraName, Supplier<Boolean> param1Supplier) {
      this.cameraManager = param1CameraManager;
      this.cameraName = param1CameraName;
      AppUtil.getInstance().ensureDirectoryExists(this.bitmapOutputDir);
      if (param1Supplier == null)
        param1Supplier = new Supplier<Boolean>() {
            public Boolean get() {
              return Boolean.valueOf(true);
            }
          }; 
      this.exampleShouldContinue = param1Supplier;
    }
    
    public void example() {
      Deadline deadline = new Deadline(10L, TimeUnit.SECONDS);
      this.cameraName.asyncRequestCameraPermission(AppUtil.getDefContext(), deadline, Continuation.create(this.threadPool, new Consumer<Boolean>() {
              public void accept(Boolean param2Boolean) {
                if (param2Boolean.booleanValue()) {
                  WebcamExample.OneCameraExample oneCameraExample = WebcamExample.OneCameraExample.this;
                  oneCameraExample.characteristics = oneCameraExample.cameraName.getCameraCharacteristics();
                  String[] arrayOfString = WebcamExample.OneCameraExample.this.characteristics.toString().split("\\n");
                  int j = arrayOfString.length;
                  for (int i = 0; i < j; i++)
                    RobotLog.vv("WebcamExample", arrayOfString[i]); 
                  WebcamExample.OneCameraExample.this.cameraManager.asyncOpenCameraAssumingPermission(WebcamExample.OneCameraExample.this.cameraName, Continuation.create(WebcamExample.OneCameraExample.this.threadPool, new Camera.StateCallbackDefault() {
                          public void onClosed(Camera param3Camera) {
                            RobotLog.vv("WebcamExample", "camera reports closed: %s", new Object[] { param3Camera });
                          }
                          
                          public void onOpened(Camera param3Camera) {
                            RobotLog.vv("WebcamExample", "camera opened: %s", new Object[] { param3Camera });
                            WebcamExample.OneCameraExample.this.camera = param3Camera;
                            if (Misc.contains(WebcamExample.OneCameraExample.this.characteristics.getAndroidFormats(), WebcamExample.OneCameraExample.this.imageFormatWanted)) {
                              WebcamExample.OneCameraExample.this.sizeWanted = WebcamExample.OneCameraExample.this.characteristics.getDefaultSize(WebcamExample.OneCameraExample.this.imageFormatWanted);
                              try {
                                param3Camera.createCaptureSession(Continuation.create(WebcamExample.OneCameraExample.this.threadPool, WebcamExample.OneCameraExample.this.captureStateCallback));
                                return;
                              } catch (CameraException cameraException) {
                                RobotLog.ee("WebcamExample", (Throwable)cameraException, "error creating capture session");
                                return;
                              } 
                            } 
                            RobotLog.ee("WebcamExample", "camera doesn't support desired format: 0x%02x", new Object[] { Integer.valueOf(this.this$2.this$1.imageFormatWanted) });
                          }
                        }), 10L, TimeUnit.SECONDS);
                } 
              }
            }));
    }
    
    public void saveBitmap(long param1Long, Bitmap param1Bitmap, Bitmap.CompressFormat param1CompressFormat) {
      // Byte code:
      //   0: aload #4
      //   2: ifnull -> 58
      //   5: getstatic org/firstinspires/ftc/robotcore/internal/camera/WebcamExample$1.$SwitchMap$android$graphics$Bitmap$CompressFormat : [I
      //   8: aload #4
      //   10: invokevirtual ordinal : ()I
      //   13: iaload
      //   14: istore #5
      //   16: iload #5
      //   18: iconst_1
      //   19: if_icmpeq -> 51
      //   22: iload #5
      //   24: iconst_2
      //   25: if_icmpeq -> 44
      //   28: iload #5
      //   30: iconst_3
      //   31: if_icmpeq -> 37
      //   34: goto -> 58
      //   37: ldc '.webp'
      //   39: astore #6
      //   41: goto -> 62
      //   44: ldc '.png'
      //   46: astore #6
      //   48: goto -> 62
      //   51: ldc '.jpg'
      //   53: astore #6
      //   55: goto -> 62
      //   58: ldc '.bmp'
      //   60: astore #6
      //   62: aload_0
      //   63: getfield bitmapOutputDir : Ljava/io/File;
      //   66: astore #7
      //   68: new java/lang/StringBuilder
      //   71: dup
      //   72: invokespecial <init> : ()V
      //   75: astore #8
      //   77: aload #8
      //   79: ldc 'uvc-%d'
      //   81: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   84: pop
      //   85: aload #8
      //   87: aload #6
      //   89: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   92: pop
      //   93: new java/io/File
      //   96: dup
      //   97: aload #7
      //   99: aload #8
      //   101: invokevirtual toString : ()Ljava/lang/String;
      //   104: iconst_1
      //   105: anewarray java/lang/Object
      //   108: dup
      //   109: iconst_0
      //   110: lload_1
      //   111: invokestatic valueOf : (J)Ljava/lang/Long;
      //   114: aastore
      //   115: invokestatic formatInvariant : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   118: invokespecial <init> : (Ljava/io/File;Ljava/lang/String;)V
      //   121: astore #6
      //   123: aload #4
      //   125: ifnull -> 172
      //   128: new java/io/FileOutputStream
      //   131: dup
      //   132: aload #6
      //   134: invokespecial <init> : (Ljava/io/File;)V
      //   137: astore #7
      //   139: new java/io/BufferedOutputStream
      //   142: dup
      //   143: aload #7
      //   145: invokespecial <init> : (Ljava/io/OutputStream;)V
      //   148: astore #8
      //   150: aload_3
      //   151: aload #4
      //   153: bipush #100
      //   155: aload #8
      //   157: invokevirtual compress : (Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
      //   160: pop
      //   161: aload #8
      //   163: invokevirtual close : ()V
      //   166: aload #7
      //   168: invokevirtual close : ()V
      //   171: return
      //   172: new org/firstinspires/ftc/robotcore/internal/system/BmpFileWriter
      //   175: dup
      //   176: aload_3
      //   177: invokespecial <init> : (Landroid/graphics/Bitmap;)V
      //   180: aload #6
      //   182: invokevirtual save : (Ljava/io/File;)V
      //   185: return
      //   186: astore_3
      //   187: ldc 'WebcamExample'
      //   189: aload_3
      //   190: ldc 'failed to save bitmap to %s'
      //   192: iconst_1
      //   193: anewarray java/lang/Object
      //   196: dup
      //   197: iconst_0
      //   198: aload #6
      //   200: aastore
      //   201: invokestatic ee : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V
      //   204: return
      // Exception table:
      //   from	to	target	type
      //   128	171	186	java/io/IOException
      //   172	185	186	java/io/IOException
    }
    
    class WebcamExampleCaptureCallback implements CameraCaptureSession.CaptureCallback {
      final Bitmap bitmap;
      
      MovingStatistics nsIntervalRollingAverage = new MovingStatistics(90);
      
      ElapsedTime timer = null;
      
      WebcamExampleCaptureCallback(CameraCaptureRequest param2CameraCaptureRequest) {
        this.bitmap = param2CameraCaptureRequest.createEmptyBitmap();
      }
      
      public void onNewFrame(CameraCaptureSession param2CameraCaptureSession, CameraCaptureRequest param2CameraCaptureRequest, CameraFrame param2CameraFrame) {
        boolean bool;
        ElapsedTime elapsedTime = this.timer;
        if (elapsedTime != null) {
          long l = elapsedTime.nanoseconds();
          this.timer.reset();
          this.nsIntervalRollingAverage.add(l);
          bool = (int)Math.round(1.0D / this.nsIntervalRollingAverage.getMean() / 1.0E9D);
        } else {
          this.timer = new ElapsedTime();
          bool = false;
        } 
        RobotLog.vv("WebcamExample", "captured frame#=%d size=%s cb=%d fps=%d", new Object[] { Long.valueOf(param2CameraFrame.getFrameNumber()), param2CameraFrame.getSize(), Integer.valueOf(param2CameraFrame.getImageSize()), Integer.valueOf(bool) });
        param2CameraFrame.copyToBitmap(this.bitmap);
        WebcamExample.OneCameraExample.this.saveBitmap(param2CameraFrame.getFrameNumber(), this.bitmap, null);
      }
    }
  }
  
  class null implements Supplier<Boolean> {
    public Boolean get() {
      return Boolean.valueOf(true);
    }
  }
  
  class null implements Consumer<Boolean> {
    public void accept(Boolean param1Boolean) {
      if (param1Boolean.booleanValue()) {
        WebcamExample.OneCameraExample oneCameraExample = this.this$1;
        oneCameraExample.characteristics = oneCameraExample.cameraName.getCameraCharacteristics();
        String[] arrayOfString = this.this$1.characteristics.toString().split("\\n");
        int j = arrayOfString.length;
        for (int i = 0; i < j; i++)
          RobotLog.vv("WebcamExample", arrayOfString[i]); 
        this.this$1.cameraManager.asyncOpenCameraAssumingPermission(this.this$1.cameraName, Continuation.create(this.this$1.threadPool, new Camera.StateCallbackDefault() {
                public void onClosed(Camera param3Camera) {
                  RobotLog.vv("WebcamExample", "camera reports closed: %s", new Object[] { param3Camera });
                }
                
                public void onOpened(Camera param3Camera) {
                  RobotLog.vv("WebcamExample", "camera opened: %s", new Object[] { param3Camera });
                  this.this$2.this$1.camera = param3Camera;
                  if (Misc.contains(this.this$2.this$1.characteristics.getAndroidFormats(), this.this$2.this$1.imageFormatWanted)) {
                    this.this$2.this$1.sizeWanted = this.this$2.this$1.characteristics.getDefaultSize(this.this$2.this$1.imageFormatWanted);
                    try {
                      param3Camera.createCaptureSession(Continuation.create(this.this$2.this$1.threadPool, this.this$2.this$1.captureStateCallback));
                      return;
                    } catch (CameraException cameraException) {
                      RobotLog.ee("WebcamExample", (Throwable)cameraException, "error creating capture session");
                      return;
                    } 
                  } 
                  RobotLog.ee("WebcamExample", "camera doesn't support desired format: 0x%02x", new Object[] { Integer.valueOf(this.this$2.this$1.imageFormatWanted) });
                }
              }), 10L, TimeUnit.SECONDS);
      } 
    }
  }
  
  class null extends Camera.StateCallbackDefault {
    public void onClosed(Camera param1Camera) {
      RobotLog.vv("WebcamExample", "camera reports closed: %s", new Object[] { param1Camera });
    }
    
    public void onOpened(Camera param1Camera) {
      RobotLog.vv("WebcamExample", "camera opened: %s", new Object[] { param1Camera });
      this.this$2.this$1.camera = param1Camera;
      if (Misc.contains(this.this$2.this$1.characteristics.getAndroidFormats(), this.this$2.this$1.imageFormatWanted)) {
        this.this$2.this$1.sizeWanted = this.this$2.this$1.characteristics.getDefaultSize(this.this$2.this$1.imageFormatWanted);
        try {
          param1Camera.createCaptureSession(Continuation.create(this.this$2.this$1.threadPool, this.this$2.this$1.captureStateCallback));
          return;
        } catch (CameraException cameraException) {
          RobotLog.ee("WebcamExample", (Throwable)cameraException, "error creating capture session");
          return;
        } 
      } 
      RobotLog.ee("WebcamExample", "camera doesn't support desired format: 0x%02x", new Object[] { Integer.valueOf(this.this$2.this$1.imageFormatWanted) });
    }
  }
  
  class null extends CameraCaptureSession.StateCallbackDefault {
    public void onClosed(CameraCaptureSession param1CameraCaptureSession) {
      RobotLog.vv("WebcamExample", "capture session reports closed: %s", new Object[] { param1CameraCaptureSession });
    }
    
    public void onConfigured(CameraCaptureSession param1CameraCaptureSession) {
      try {
        CameraCaptureRequest cameraCaptureRequest = this.this$1.camera.createCaptureRequest(this.this$1.imageFormatWanted, this.this$1.sizeWanted, this.this$1.characteristics.getMaxFramesPerSecond(this.this$1.imageFormatWanted, this.this$1.sizeWanted));
        param1CameraCaptureSession.startCapture(cameraCaptureRequest, new WebcamExample.OneCameraExample.WebcamExampleCaptureCallback(cameraCaptureRequest), Continuation.create(this.this$1.threadPool, new CameraCaptureSession.StatusCallback() {
                public void onCaptureSequenceCompleted(CameraCaptureSession param3CameraCaptureSession, CameraCaptureSequenceId param3CameraCaptureSequenceId, long param3Long) {
                  RobotLog.vv("WebcamExample", "capture sequence %s reports completed: lastFrame=%d", new Object[] { param3CameraCaptureSequenceId, Long.valueOf(param3Long) });
                }
              }));
        null = new AppUtil.DialogParams(UILocation.ONLY_LOCAL, "Streaming Active", "Press OK to stop");
        try {
          null = new CountDownLatch(1);
          AppUtil.DialogContext dialogContext = AppUtil.getInstance().showDialog(null, ThreadPool.getDefault(), new Consumer<AppUtil.DialogContext>() {
                public void accept(AppUtil.DialogContext param3DialogContext) {
                  latch.countDown();
                }
              });
          try {
            do {
            
            } while (((Boolean)this.this$1.exampleShouldContinue.get()).booleanValue() && !null.await(100L, TimeUnit.MILLISECONDS));
          } catch (InterruptedException interruptedException) {
          
          } finally {
            dialogContext.dismissDialog();
          } 
          dialogContext.dismissDialog();
          return;
        } finally {
          param1CameraCaptureSession.stopCapture();
          param1CameraCaptureSession.close();
          this.this$1.camera.close();
        } 
      } catch (CameraException cameraException) {
        RobotLog.ee("WebcamExample", (Throwable)cameraException, "error setting repeat capture request");
        return;
      } 
    }
  }
  
  class null implements CameraCaptureSession.StatusCallback {
    public void onCaptureSequenceCompleted(CameraCaptureSession param1CameraCaptureSession, CameraCaptureSequenceId param1CameraCaptureSequenceId, long param1Long) {
      RobotLog.vv("WebcamExample", "capture sequence %s reports completed: lastFrame=%d", new Object[] { param1CameraCaptureSequenceId, Long.valueOf(param1Long) });
    }
  }
  
  class null implements Consumer<AppUtil.DialogContext> {
    public void accept(AppUtil.DialogContext param1DialogContext) {
      latch.countDown();
    }
  }
  
  class WebcamExampleCaptureCallback implements CameraCaptureSession.CaptureCallback {
    final Bitmap bitmap;
    
    MovingStatistics nsIntervalRollingAverage = new MovingStatistics(90);
    
    ElapsedTime timer = null;
    
    WebcamExampleCaptureCallback(CameraCaptureRequest param1CameraCaptureRequest) {
      this.bitmap = param1CameraCaptureRequest.createEmptyBitmap();
    }
    
    public void onNewFrame(CameraCaptureSession param1CameraCaptureSession, CameraCaptureRequest param1CameraCaptureRequest, CameraFrame param1CameraFrame) {
      boolean bool;
      ElapsedTime elapsedTime = this.timer;
      if (elapsedTime != null) {
        long l = elapsedTime.nanoseconds();
        this.timer.reset();
        this.nsIntervalRollingAverage.add(l);
        bool = (int)Math.round(1.0D / this.nsIntervalRollingAverage.getMean() / 1.0E9D);
      } else {
        this.timer = new ElapsedTime();
        bool = false;
      } 
      RobotLog.vv("WebcamExample", "captured frame#=%d size=%s cb=%d fps=%d", new Object[] { Long.valueOf(param1CameraFrame.getFrameNumber()), param1CameraFrame.getSize(), Integer.valueOf(param1CameraFrame.getImageSize()), Integer.valueOf(bool) });
      param1CameraFrame.copyToBitmap(this.bitmap);
      this.this$1.saveBitmap(param1CameraFrame.getFrameNumber(), this.bitmap, null);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\WebcamExample.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */