package org.firstinspires.ftc.robotcore.internal.camera.names;

import android.content.Context;
import com.qualcomm.robotcore.util.ThreadPool;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.ContinuationResult;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiCameraCharacteristics;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.ContinuationSynchronizer;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

public abstract class CameraNameImplBase implements CameraName {
  public static boolean TRACE = true;
  
  public void asyncRequestCameraPermission(Context paramContext, Deadline paramDeadline, Continuation<? extends Consumer<Boolean>> paramContinuation) {
    paramContinuation.dispatch(new ContinuationResult<Consumer<Boolean>>() {
          public void handle(Consumer<Boolean> param1Consumer) {
            param1Consumer.accept(Boolean.valueOf(true));
          }
        });
  }
  
  public CameraCharacteristics getCameraCharacteristics() {
    return (CameraCharacteristics)new UvcApiCameraCharacteristics();
  }
  
  public boolean isCameraDirection() {
    return false;
  }
  
  public boolean isSwitchable() {
    return false;
  }
  
  public boolean isUnknown() {
    return false;
  }
  
  public boolean isWebcam() {
    return false;
  }
  
  public boolean requestCameraPermission(Deadline paramDeadline) {
    final ContinuationSynchronizer synchronizer = new ContinuationSynchronizer(paramDeadline, TRACE, Boolean.valueOf(false));
    asyncRequestCameraPermission(AppUtil.getDefContext(), paramDeadline, Continuation.create(ThreadPool.getDefault(), new Consumer<Boolean>() {
            public void accept(Boolean param1Boolean) {
              synchronizer.finish("permission request complete", param1Boolean);
            }
          }));
    try {
      continuationSynchronizer.await();
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
    } 
    return ((Boolean)continuationSynchronizer.getValue()).booleanValue();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\names\CameraNameImplBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */