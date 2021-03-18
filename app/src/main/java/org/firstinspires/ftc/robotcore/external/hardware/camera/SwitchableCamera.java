package org.firstinspires.ftc.robotcore.external.hardware.camera;

public interface SwitchableCamera extends Camera {
  CameraName getActiveCamera();
  
  CameraName[] getMembers();
  
  void setActiveCamera(CameraName paramCameraName);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\hardware\camera\SwitchableCamera.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */