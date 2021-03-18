package com.qualcomm.robotcore.hardware;

import java.util.Set;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public interface OrientationSensor {
  Orientation getAngularOrientation(AxesReference paramAxesReference, AxesOrder paramAxesOrder, AngleUnit paramAngleUnit);
  
  Set<Axis> getAngularOrientationAxes();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\OrientationSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */