package org.firstinspires.ftc.robotcore.external.tfod;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public interface Recognition {
  double estimateAngleToObject(AngleUnit paramAngleUnit);
  
  float getBottom();
  
  float getConfidence();
  
  float getHeight();
  
  int getImageHeight();
  
  int getImageWidth();
  
  String getLabel();
  
  float getLeft();
  
  float getRight();
  
  float getTop();
  
  float getWidth();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\tfod\Recognition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */