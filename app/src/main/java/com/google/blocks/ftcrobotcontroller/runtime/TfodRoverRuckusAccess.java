package com.google.blocks.ftcrobotcontroller.runtime;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.tfod.TfodBase;
import org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus;

final class TfodRoverRuckusAccess extends TfodBaseAccess<TfodRoverRuckus> {
  TfodRoverRuckusAccess(BlocksOpMode paramBlocksOpMode, String paramString, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramString, paramHardwareMap, "TensorFlowObjectDetectionRoverRuckus");
  }
  
  protected TfodRoverRuckus createTfod() {
    return new TfodRoverRuckus();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\TfodRoverRuckusAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */