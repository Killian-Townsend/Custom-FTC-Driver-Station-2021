package org.firstinspires.ftc.robotcore.internal.system;

import java.lang.ref.WeakReference;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.internal.camera.CameraManagerImpl;
import org.firstinspires.ftc.robotcore.internal.tfod.TFObjectDetectorImpl;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;

public class ClassFactoryImpl extends ClassFactory {
  public static final String TAG = "ClassFactory";
  
  protected WeakReference<CameraManagerImpl> cameraManagerHolder = new WeakReference<CameraManagerImpl>(null);
  
  protected final Object lock = new Object();
  
  public static void onApplicationStart() {
    ClassFactory.InstanceHolder.theInstance = new ClassFactoryImpl();
  }
  
  public TFObjectDetector createTFObjectDetector(TFObjectDetector.Parameters paramParameters, VuforiaLocalizer paramVuforiaLocalizer) {
    return (TFObjectDetector)new TFObjectDetectorImpl(paramParameters, paramVuforiaLocalizer);
  }
  
  public VuforiaLocalizer createVuforia(VuforiaLocalizer.Parameters paramParameters) {
    return (VuforiaLocalizer)new VuforiaLocalizerImpl(paramParameters);
  }
  
  public CameraManager getCameraManager() {
    synchronized (this.lock) {
      CameraManagerImpl cameraManagerImpl2 = this.cameraManagerHolder.get();
      CameraManagerImpl cameraManagerImpl1 = cameraManagerImpl2;
      if (cameraManagerImpl2 == null) {
        cameraManagerImpl1 = new CameraManagerImpl();
        this.cameraManagerHolder = new WeakReference<CameraManagerImpl>(cameraManagerImpl1);
      } 
      return (CameraManager)cameraManagerImpl1;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\ClassFactoryImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */