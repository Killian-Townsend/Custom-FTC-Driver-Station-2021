package org.firstinspires.ftc.robotcore.internal.tfod;

import com.vuforia.Image;
import java.util.concurrent.BlockingQueue;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;

public class VuforiaFrameGenerator implements FrameGenerator {
  private static final String TAG = "VuforiaFrameGenerator";
  
  private final CameraInformation cameraInformation;
  
  private final ClippingMargins clippingMargins;
  
  private final BlockingQueue<VuforiaLocalizer.CloseableFrame> frameQueue;
  
  public VuforiaFrameGenerator(VuforiaLocalizer paramVuforiaLocalizer, int paramInt, ClippingMargins paramClippingMargins) {
    if (paramVuforiaLocalizer.enableConvertFrameToFormat(new int[] { 1, 8 })[0]) {
      paramVuforiaLocalizer.setFrameQueueCapacity(1);
      this.frameQueue = paramVuforiaLocalizer.getFrameQueue();
      CameraCalibration cameraCalibration = paramVuforiaLocalizer.getCameraCalibration();
      this.cameraInformation = new CameraInformation(paramInt, cameraCalibration.focalLengthX, cameraCalibration.focalLengthY, cameraCalibration.getSize().getWidth(), cameraCalibration.getSize().getHeight());
      this.clippingMargins = paramClippingMargins;
      return;
    } 
    throw new RuntimeException("Unable to convince Vuforia to generate RGB565 frames!");
  }
  
  public CameraInformation getCameraInformation() {
    return this.cameraInformation;
  }
  
  public YuvRgbFrame getFrame() throws InterruptedException {
    VuforiaLocalizer.CloseableFrame closeableFrame = this.frameQueue.take();
    long l = System.nanoTime();
    for (int i = 0; i < closeableFrame.getNumImages(); i++) {
      Image image = closeableFrame.getImage(i);
      if (image.getFormat() == 1)
        return new YuvRgbFrame(l, this.cameraInformation.size, image.getPixels(), this.clippingMargins); 
    } 
    throw new IllegalStateException("Didn't find an RGB565 image from Vuforia!");
  }
  
  public void shutdown() {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\VuforiaFrameGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */