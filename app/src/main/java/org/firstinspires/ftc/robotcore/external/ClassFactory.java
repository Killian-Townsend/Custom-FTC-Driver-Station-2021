package org.firstinspires.ftc.robotcore.external;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public abstract class ClassFactory {
  @Deprecated
  public static VuforiaLocalizer createVuforiaLocalizer(VuforiaLocalizer.Parameters paramParameters) {
    return getInstance().createVuforia(paramParameters);
  }
  
  public static ClassFactory getInstance() {
    return InstanceHolder.theInstance;
  }
  
  public abstract TFObjectDetector createTFObjectDetector(TFObjectDetector.Parameters paramParameters, VuforiaLocalizer paramVuforiaLocalizer);
  
  public abstract VuforiaLocalizer createVuforia(VuforiaLocalizer.Parameters paramParameters);
  
  public abstract CameraManager getCameraManager();
  
  protected static class InstanceHolder {
    public static ClassFactory theInstance;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\ClassFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */