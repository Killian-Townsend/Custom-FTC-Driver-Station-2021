package org.firstinspires.ftc.robotcore.internal.tfod;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

public class AnnotatedYuvRgbFrame {
  private final YuvRgbFrame frame;
  
  private final List<Recognition> recognitions;
  
  public AnnotatedYuvRgbFrame(YuvRgbFrame paramYuvRgbFrame, List<Recognition> paramList) {
    this.frame = paramYuvRgbFrame;
    this.recognitions = paramList;
  }
  
  public YuvRgbFrame getFrame() {
    return this.frame;
  }
  
  public long getFrameTimeNanos() {
    return this.frame.getFrameTimeNanos();
  }
  
  public List<Recognition> getRecognitions() {
    return this.recognitions;
  }
  
  public String getTag() {
    return this.frame.getTag();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\AnnotatedYuvRgbFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */