package org.firstinspires.ftc.robotcore.external.hardware.camera;

public class CameraException extends Exception {
  public final Camera.Error error;
  
  public CameraException(Camera.Error paramError) {
    this.error = paramError;
  }
  
  public CameraException(Camera.Error paramError, String paramString, Object... paramVarArgs) {
    super(String.format(paramString, paramVarArgs));
    this.error = paramError;
  }
  
  public CameraException(Camera.Error paramError, Throwable paramThrowable) {
    super(paramThrowable);
    this.error = paramError;
  }
  
  public CameraException(Camera.Error paramError, Throwable paramThrowable, String paramString, Object... paramVarArgs) {
    super(String.format(paramString, paramVarArgs), paramThrowable);
    this.error = paramError;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\hardware\camera\CameraException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */