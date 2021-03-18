package org.firstinspires.ftc.robotcore.internal.tfod;

interface FrameGenerator {
  CameraInformation getCameraInformation();
  
  YuvRgbFrame getFrame() throws InterruptedException;
  
  void shutdown();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\FrameGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */