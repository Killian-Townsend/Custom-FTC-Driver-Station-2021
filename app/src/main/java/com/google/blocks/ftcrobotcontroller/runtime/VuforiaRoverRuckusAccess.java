package com.google.blocks.ftcrobotcontroller.runtime;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaRoverRuckus;

final class VuforiaRoverRuckusAccess extends VuforiaBaseAccess<VuforiaRoverRuckus> {
  VuforiaRoverRuckusAccess(BlocksOpMode paramBlocksOpMode, String paramString, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramString, paramHardwareMap, "VuforiaRoverRuckus");
  }
  
  protected VuforiaRoverRuckus createVuforia() {
    return new VuforiaRoverRuckus();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\VuforiaRoverRuckusAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */