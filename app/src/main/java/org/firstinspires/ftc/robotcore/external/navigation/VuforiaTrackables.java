package org.firstinspires.ftc.robotcore.external.navigation;

import java.util.List;

public interface VuforiaTrackables extends List<VuforiaTrackable> {
  void activate();
  
  void deactivate();
  
  VuforiaLocalizer getLocalizer();
  
  String getName();
  
  void setName(String paramString);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuforiaTrackables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */