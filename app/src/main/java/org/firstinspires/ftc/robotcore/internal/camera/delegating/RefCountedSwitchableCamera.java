package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.internal.camera.RefCountedCamera;

public interface RefCountedSwitchableCamera extends RefCountedCamera {
  CameraName getActiveCamera();
  
  CameraName[] getMembers();
  
  void setActiveCamera(CameraName paramCameraName);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\RefCountedSwitchableCamera.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */