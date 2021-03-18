package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

class DistanceSensorAccess extends HardwareAccess<DistanceSensor> {
  private final DistanceSensor distanceSensor = this.hardwareDevice;
  
  DistanceSensorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, DistanceSensor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {DistanceSensor.class}, methodName = {"getDistance"})
  public double getDistance(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".getDistance");
    DistanceUnit distanceUnit = checkDistanceUnit(paramString);
    return (distanceUnit != null) ? this.distanceSensor.getDistance(distanceUnit) : 0.0D;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\DistanceSensorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */