package org.firstinspires.ftc.robotcore.internal.camera.libuvc.api;

import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.internal.camera.CameraCaptureRequestImpl;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcDeviceHandle;

public class UvcApiCameraCaptureRequest extends CameraCaptureRequestImpl {
  protected final UvcDeviceHandle uvcDeviceHandle;
  
  public UvcApiCameraCaptureRequest(UvcDeviceHandle paramUvcDeviceHandle, int paramInt1, Size paramSize, int paramInt2) {
    super(paramInt1, paramSize, paramInt2);
    this.uvcDeviceHandle = paramUvcDeviceHandle;
  }
  
  public static boolean isForDeviceHandle(UvcDeviceHandle paramUvcDeviceHandle, CameraCaptureRequest paramCameraCaptureRequest) {
    boolean bool = paramCameraCaptureRequest instanceof UvcApiCameraCaptureRequest;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((UvcApiCameraCaptureRequest)paramCameraCaptureRequest).uvcDeviceHandle == paramUvcDeviceHandle)
        bool1 = true; 
    } 
    return bool1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\api\UvcApiCameraCaptureRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */