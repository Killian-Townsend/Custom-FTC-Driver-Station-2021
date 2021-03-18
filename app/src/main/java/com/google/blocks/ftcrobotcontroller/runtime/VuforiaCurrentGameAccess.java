package com.google.blocks.ftcrobotcontroller.runtime;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;

final class VuforiaCurrentGameAccess extends VuforiaBaseAccess<VuforiaCurrentGame> {
  VuforiaCurrentGameAccess(BlocksOpMode paramBlocksOpMode, String paramString, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramString, paramHardwareMap, "VuforiaUltimateGoal");
  }
  
  protected VuforiaCurrentGame createVuforia() {
    return new VuforiaCurrentGame();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\VuforiaCurrentGameAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */