package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.internal.camera.CameraCaptureRequestImpl;

public class DelegatingCaptureRequest extends CameraCaptureRequestImpl {
  protected final DelegatingCamera camera;
  
  public DelegatingCaptureRequest(DelegatingCamera paramDelegatingCamera, int paramInt1, Size paramSize, int paramInt2) {
    super(paramInt1, paramSize, paramInt2);
    this.camera = paramDelegatingCamera;
  }
  
  public static boolean isForCamera(DelegatingCamera paramDelegatingCamera, CameraCaptureRequest paramCameraCaptureRequest) {
    boolean bool = paramCameraCaptureRequest instanceof DelegatingCaptureRequest;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((DelegatingCaptureRequest)paramCameraCaptureRequest).camera == paramDelegatingCamera)
        bool1 = true; 
    } 
    return bool1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\DelegatingCaptureRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */