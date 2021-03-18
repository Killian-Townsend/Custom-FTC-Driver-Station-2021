package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import java.util.concurrent.atomic.AtomicInteger;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.ContinuationResult;
import org.firstinspires.ftc.robotcore.external.function.ThrowingRunnable;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSequenceId;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraFrame;
import org.firstinspires.ftc.robotcore.internal.camera.RenumberedCameraFrame;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public class DelegatingCaptureSequence extends RefCounted {
  public static final String TAG = "DelCaptureSequence";
  
  protected static final AtomicInteger nextFrameNumber = new AtomicInteger(1);
  
  protected Camera camera = null;
  
  protected CameraCaptureRequest cameraCaptureRequest = null;
  
  protected CameraCaptureSequenceId cameraCaptureSequenceId = null;
  
  protected CameraCaptureSession cameraCaptureSession = null;
  
  protected boolean closeReported = false;
  
  protected final DelegatingCamera delegatingCamera;
  
  protected final DelegatingCaptureRequest delegatingCaptureRequest;
  
  protected final DelegatingCaptureSession delegatingCaptureSession;
  
  protected final DelegatingCaptureSequenceId delegatingSequenceId;
  
  protected long lastFrameNumber = -1L;
  
  protected boolean reportOnClose = false;
  
  protected StreamingState streamingState = StreamingState.Stopped;
  
  protected final Tracer tracer;
  
  protected final Continuation<? extends CameraCaptureSession.CaptureCallback> userCaptureContinuation;
  
  protected final Continuation<? extends CameraCaptureSession.StatusCallback> userStatusContinuation;
  
  DelegatingCaptureSequence(DelegatingCamera paramDelegatingCamera, DelegatingCaptureSession paramDelegatingCaptureSession, DelegatingCaptureSequenceId paramDelegatingCaptureSequenceId, DelegatingCaptureRequest paramDelegatingCaptureRequest, Continuation<? extends CameraCaptureSession.CaptureCallback> paramContinuation, Continuation<? extends CameraCaptureSession.StatusCallback> paramContinuation1) {
    this.delegatingCaptureSession = paramDelegatingCaptureSession;
    this.delegatingCamera = paramDelegatingCamera;
    this.delegatingSequenceId = paramDelegatingCaptureSequenceId;
    this.delegatingCaptureRequest = paramDelegatingCaptureRequest;
    this.userCaptureContinuation = paramContinuation;
    this.userStatusContinuation = paramContinuation1;
    this.tracer = Tracer.create(getTag(), DelegatingCamera.TRACE);
  }
  
  protected void destructor() {
    stopStreamingAndReportClosedIfNeeded();
    super.destructor();
  }
  
  protected void doStreaming() throws CameraException {
    this.tracer.trace("doStreaming()", new ThrowingRunnable<CameraException>() {
          public void run() throws CameraException {
            synchronized (DelegatingCaptureSequence.this.lock) {
              Assert.assertNotNull(DelegatingCaptureSequence.this.camera);
              Assert.assertNull(DelegatingCaptureSequence.this.cameraCaptureSession);
              Assert.assertNull(DelegatingCaptureSequence.this.cameraCaptureRequest);
              Continuation<DelegatingCaptureSession.InterveningStateCallback> continuation = DelegatingCaptureSequence.this.delegatingCaptureSession.newInterveningStateCallback();
              DelegatingCaptureSequence.this.cameraCaptureSession = DelegatingCaptureSequence.this.camera.createCaptureSession(continuation);
              if (((DelegatingCaptureSession.InterveningStateCallback)continuation.getTarget()).awaitConfiguredOrClosed()) {
                DelegatingCaptureSequence.this.cameraCaptureRequest = DelegatingCaptureSequence.this.camera.createCaptureRequest(DelegatingCaptureSequence.this.delegatingCaptureRequest.androidFormat, DelegatingCaptureSequence.this.delegatingCaptureRequest.size, DelegatingCaptureSequence.this.delegatingCaptureRequest.fps);
                try {
                  DelegatingCaptureSequence.this.cameraCaptureSequenceId = DelegatingCaptureSequence.this.cameraCaptureSession.startCapture(DelegatingCaptureSequence.this.cameraCaptureRequest, DelegatingCaptureSequence.this.userCaptureContinuation.createForNewTarget(new DelegatingCaptureSequence.InterveningCaptureCallback()), DelegatingCaptureSequence.this.userStatusContinuation.createForNewTarget(new DelegatingCaptureSequence.InterveningStatusCallback()));
                  return;
                } catch (CameraException cameraException) {
                  DelegatingCaptureSequence.this.tracer.traceError((Throwable)cameraException, "exception starting capture", new Object[0]);
                  DelegatingCaptureSequence.this.undoStreaming();
                  throw cameraException;
                } catch (RuntimeException runtimeException) {
                  DelegatingCaptureSequence.this.tracer.traceError(runtimeException, "exception starting capture", new Object[0]);
                  DelegatingCaptureSequence.this.undoStreaming();
                  throw new CameraException(Camera.Error.InternalError, runtimeException);
                } 
              } 
              DelegatingCaptureSequence.this.tracer.traceError("awaitConfiguredOrClosed(): unable to open capture session", new Object[0]);
              DelegatingCaptureSequence.this.cameraCaptureSession.close();
              DelegatingCaptureSequence.this.cameraCaptureSession = null;
              throw new CameraException(Camera.Error.Timeout);
            } 
          }
        });
  }
  
  public String getTag() {
    DelegatingCamera delegatingCamera = this.delegatingCamera;
    String str = "DelCaptureSequence";
    if (delegatingCamera != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.delegatingCamera.getTag());
      stringBuilder.append("|");
      stringBuilder.append("DelCaptureSequence");
      str = stringBuilder.toString();
    } 
    return str;
  }
  
  public void onCameraChanged(final Camera newCamera) {
    synchronized (this.lock) {
      if (this.camera != newCamera)
        this.tracer.trace(this.tracer.format("onCameraChange(%s->%s)", new Object[] { this.camera, newCamera }), new Runnable() {
              public void run() {
                DelegatingCaptureSequence.this.pauseStreaming();
                DelegatingCaptureSequence.this.camera = newCamera;
                if (DelegatingCaptureSequence.this.camera != null)
                  DelegatingCaptureSequence.this.resumeStreaming(); 
              }
            }); 
      return;
    } 
  }
  
  public void pauseStreaming() {
    synchronized (this.lock) {
      this.tracer.trace(this.tracer.format("pauseStreaming(%s)", new Object[] { this.streamingState }), new Runnable() {
            public void run() {
              if (DelegatingCaptureSequence.null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$camera$delegating$DelegatingCaptureSequence$StreamingState[DelegatingCaptureSequence.this.streamingState.ordinal()] != 1)
                return; 
              DelegatingCaptureSequence.this.undoStreaming();
              DelegatingCaptureSequence.this.streamingState = DelegatingCaptureSequence.StreamingState.Paused;
            }
          });
      return;
    } 
  }
  
  protected void reportClosedIfNeeded() {
    synchronized (this.lock) {
      if (this.reportOnClose && !this.closeReported) {
        this.closeReported = true;
        this.userStatusContinuation.dispatch(new ContinuationResult<CameraCaptureSession.StatusCallback>() {
              public void handle(CameraCaptureSession.StatusCallback param1StatusCallback) {
                param1StatusCallback.onCaptureSequenceCompleted(DelegatingCaptureSequence.this.delegatingCaptureSession, (CameraCaptureSequenceId)DelegatingCaptureSequence.this.delegatingSequenceId, DelegatingCaptureSequence.this.lastFrameNumber);
              }
            });
      } 
      return;
    } 
  }
  
  protected void reportError(RuntimeException paramRuntimeException) {
    this.delegatingCamera.reportError(Camera.Error.InternalError);
  }
  
  protected void reportError(CameraException paramCameraException) {
    this.delegatingCamera.reportError(paramCameraException.error);
  }
  
  public void resumeStreaming() {
    synchronized (this.lock) {
      this.tracer.trace(this.tracer.format("resumeStreaming(%s)", new Object[] { this.streamingState }), new Runnable() {
            public void run() {
              // Byte code:
              //   0: getstatic org/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence$8.$SwitchMap$org$firstinspires$ftc$robotcore$internal$camera$delegating$DelegatingCaptureSequence$StreamingState : [I
              //   3: aload_0
              //   4: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence;
              //   7: getfield streamingState : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence$StreamingState;
              //   10: invokevirtual ordinal : ()I
              //   13: iaload
              //   14: iconst_2
              //   15: if_icmpeq -> 19
              //   18: return
              //   19: iconst_0
              //   20: istore #4
              //   22: iconst_0
              //   23: istore_3
              //   24: aload_0
              //   25: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence;
              //   28: invokevirtual doStreaming : ()V
              //   31: aload_0
              //   32: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence;
              //   35: getstatic org/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence$StreamingState.Started : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence$StreamingState;
              //   38: putfield streamingState : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence$StreamingState;
              //   41: aload_0
              //   42: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence;
              //   45: astore #5
              //   47: aload #5
              //   49: getfield reportOnClose : Z
              //   52: istore_3
              //   53: aload #5
              //   55: iconst_1
              //   56: putfield reportOnClose : Z
              //   59: return
              //   60: astore #5
              //   62: iconst_1
              //   63: istore_1
              //   64: goto -> 192
              //   67: astore #5
              //   69: iconst_1
              //   70: istore_2
              //   71: goto -> 92
              //   74: astore #5
              //   76: iconst_1
              //   77: istore_2
              //   78: goto -> 140
              //   81: astore #5
              //   83: iconst_0
              //   84: istore_1
              //   85: goto -> 192
              //   88: astore #5
              //   90: iconst_0
              //   91: istore_2
              //   92: iload_2
              //   93: istore_1
              //   94: aload_0
              //   95: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence;
              //   98: aload #5
              //   100: invokevirtual reportError : (Lorg/firstinspires/ftc/robotcore/external/hardware/camera/CameraException;)V
              //   103: aload_0
              //   104: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence;
              //   107: astore #6
              //   109: aload #6
              //   111: astore #5
              //   113: aload #6
              //   115: getfield reportOnClose : Z
              //   118: ifne -> 181
              //   121: aload #6
              //   123: astore #5
              //   125: iload_2
              //   126: ifeq -> 183
              //   129: aload #6
              //   131: astore #5
              //   133: goto -> 181
              //   136: astore #5
              //   138: iconst_0
              //   139: istore_2
              //   140: iload_2
              //   141: istore_1
              //   142: aload_0
              //   143: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence;
              //   146: aload #5
              //   148: invokevirtual reportError : (Ljava/lang/RuntimeException;)V
              //   151: aload_0
              //   152: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence;
              //   155: astore #6
              //   157: aload #6
              //   159: astore #5
              //   161: aload #6
              //   163: getfield reportOnClose : Z
              //   166: ifne -> 181
              //   169: aload #6
              //   171: astore #5
              //   173: iload_2
              //   174: ifeq -> 183
              //   177: aload #6
              //   179: astore #5
              //   181: iconst_1
              //   182: istore_3
              //   183: aload #5
              //   185: iload_3
              //   186: putfield reportOnClose : Z
              //   189: return
              //   190: astore #5
              //   192: aload_0
              //   193: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/delegating/DelegatingCaptureSequence;
              //   196: astore #6
              //   198: aload #6
              //   200: getfield reportOnClose : Z
              //   203: ifne -> 213
              //   206: iload #4
              //   208: istore_3
              //   209: iload_1
              //   210: ifeq -> 215
              //   213: iconst_1
              //   214: istore_3
              //   215: aload #6
              //   217: iload_3
              //   218: putfield reportOnClose : Z
              //   221: aload #5
              //   223: athrow
              // Exception table:
              //   from	to	target	type
              //   24	31	136	java/lang/RuntimeException
              //   24	31	88	org/firstinspires/ftc/robotcore/external/hardware/camera/CameraException
              //   24	31	81	finally
              //   31	41	74	java/lang/RuntimeException
              //   31	41	67	org/firstinspires/ftc/robotcore/external/hardware/camera/CameraException
              //   31	41	60	finally
              //   94	103	190	finally
              //   142	151	190	finally
            }
          });
      return;
    } 
  }
  
  public void startStreaming() throws CameraException {
    synchronized (this.lock) {
      this.tracer.trace(this.tracer.format("startStreaming(%s)", new Object[] { this.streamingState }), new ThrowingRunnable<CameraException>() {
            public void run() throws CameraException {
              int i = DelegatingCaptureSequence.null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$camera$delegating$DelegatingCaptureSequence$StreamingState[DelegatingCaptureSequence.this.streamingState.ordinal()];
              if (i != 2) {
                if (i != 3)
                  return; 
                Assert.assertFalse(DelegatingCaptureSequence.this.reportOnClose);
                DelegatingCaptureSequence.this.reportOnClose = true;
                try {
                  DelegatingCaptureSequence.this.doStreaming();
                  DelegatingCaptureSequence.this.streamingState = DelegatingCaptureSequence.StreamingState.Started;
                  return;
                } catch (RuntimeException runtimeException) {
                  DelegatingCaptureSequence.this.reportError(runtimeException);
                  DelegatingCaptureSequence.this.reportOnClose = false;
                  throw runtimeException;
                } catch (CameraException cameraException) {
                  DelegatingCaptureSequence.this.reportError(cameraException);
                  DelegatingCaptureSequence.this.reportOnClose = false;
                  throw cameraException;
                } 
              } 
              DelegatingCaptureSequence.this.tracer.traceError("starting paused stream", new Object[0]);
            }
          });
      return;
    } 
  }
  
  public void stopStreamingAndReportClosedIfNeeded() {
    synchronized (this.lock) {
      undoStreaming();
      reportClosedIfNeeded();
      this.streamingState = StreamingState.Stopped;
      return;
    } 
  }
  
  protected void undoStreaming() {
    synchronized (this.lock) {
      if (this.cameraCaptureSession != null)
        this.tracer.trace("undoStreaming()", new Runnable() {
              public void run() {
                DelegatingCaptureSequence.this.cameraCaptureSession.stopCapture();
                DelegatingCaptureSequence.this.cameraCaptureSession.close();
                DelegatingCaptureSequence.this.cameraCaptureSession = null;
                DelegatingCaptureSequence.this.cameraCaptureRequest = null;
              }
            }); 
      return;
    } 
  }
  
  class InterveningCaptureCallback implements CameraCaptureSession.CaptureCallback {
    public void onNewFrame(CameraCaptureSession param1CameraCaptureSession, final CameraCaptureRequest request, final CameraFrame cameraFrame) {
      DelegatingCaptureSequence.this.userCaptureContinuation.dispatchHere(new ContinuationResult<CameraCaptureSession.CaptureCallback>() {
            public void handle(CameraCaptureSession.CaptureCallback param2CaptureCallback) {
              RenumberedCameraFrame renumberedCameraFrame = new RenumberedCameraFrame((CameraCaptureRequest)DelegatingCaptureSequence.this.delegatingCaptureRequest, (CameraCaptureSequenceId)DelegatingCaptureSequence.this.delegatingSequenceId, cameraFrame, DelegatingCaptureSequence.nextFrameNumber.getAndIncrement());
              DelegatingCaptureSequence.this.lastFrameNumber = renumberedCameraFrame.getFrameNumber();
              param2CaptureCallback.onNewFrame(DelegatingCaptureSequence.this.delegatingCaptureSession, request, (CameraFrame)renumberedCameraFrame);
              renumberedCameraFrame.releaseRef();
            }
          });
    }
  }
  
  class null implements ContinuationResult<CameraCaptureSession.CaptureCallback> {
    public void handle(CameraCaptureSession.CaptureCallback param1CaptureCallback) {
      RenumberedCameraFrame renumberedCameraFrame = new RenumberedCameraFrame((CameraCaptureRequest)DelegatingCaptureSequence.this.delegatingCaptureRequest, (CameraCaptureSequenceId)DelegatingCaptureSequence.this.delegatingSequenceId, cameraFrame, DelegatingCaptureSequence.nextFrameNumber.getAndIncrement());
      DelegatingCaptureSequence.this.lastFrameNumber = renumberedCameraFrame.getFrameNumber();
      param1CaptureCallback.onNewFrame(DelegatingCaptureSequence.this.delegatingCaptureSession, request, (CameraFrame)renumberedCameraFrame);
      renumberedCameraFrame.releaseRef();
    }
  }
  
  class InterveningStatusCallback implements CameraCaptureSession.StatusCallback {
    public void onCaptureSequenceCompleted(CameraCaptureSession param1CameraCaptureSession, CameraCaptureSequenceId param1CameraCaptureSequenceId, long param1Long) {
      DelegatingCaptureSequence.this.userStatusContinuation.dispatchHere(new ContinuationResult<CameraCaptureSession.StatusCallback>() {
            public void handle(CameraCaptureSession.StatusCallback param2StatusCallback) {
              param2StatusCallback.onCaptureSequenceCompleted(DelegatingCaptureSequence.this.delegatingCaptureSession, (CameraCaptureSequenceId)DelegatingCaptureSequence.this.delegatingSequenceId, DelegatingCaptureSequence.this.lastFrameNumber);
            }
          });
    }
  }
  
  class null implements ContinuationResult<CameraCaptureSession.StatusCallback> {
    public void handle(CameraCaptureSession.StatusCallback param1StatusCallback) {
      param1StatusCallback.onCaptureSequenceCompleted(DelegatingCaptureSequence.this.delegatingCaptureSession, (CameraCaptureSequenceId)DelegatingCaptureSequence.this.delegatingSequenceId, DelegatingCaptureSequence.this.lastFrameNumber);
    }
  }
  
  protected enum StreamingState {
    Paused, Started, Stopped;
    
    static {
      StreamingState streamingState = new StreamingState("Stopped", 2);
      Stopped = streamingState;
      $VALUES = new StreamingState[] { Started, Paused, streamingState };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\DelegatingCaptureSequence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */