package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import org.firstinspires.ftc.robotcore.internal.camera.CameraCaptureSequenceIdImpl;
import org.firstinspires.ftc.robotcore.internal.camera.RefCountedCamera;

public class DelegatingCaptureSequenceId extends CameraCaptureSequenceIdImpl {
  public DelegatingCaptureSequenceId(RefCountedCamera paramRefCountedCamera, int paramInt) {
    super(paramRefCountedCamera, paramInt, DelegatingCaptureSequenceId.class);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\DelegatingCaptureSequenceId.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */