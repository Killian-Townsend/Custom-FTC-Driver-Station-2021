package org.firstinspires.ftc.robotcore.internal.camera;

public enum CameraState {
  Closed, Disconnected, FailedOpen, Nascent, OpenAndStarted, OpenNotStarted;
  
  static {
    FailedOpen = new CameraState("FailedOpen", 1);
    OpenNotStarted = new CameraState("OpenNotStarted", 2);
    OpenAndStarted = new CameraState("OpenAndStarted", 3);
    Disconnected = new CameraState("Disconnected", 4);
    CameraState cameraState = new CameraState("Closed", 5);
    Closed = cameraState;
    $VALUES = new CameraState[] { Nascent, FailedOpen, OpenNotStarted, OpenAndStarted, Disconnected, cameraState };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\CameraState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */