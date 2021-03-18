package com.qualcomm.robotcore.hardware;

import java.util.Set;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;

public interface Gyroscope {
  AngularVelocity getAngularVelocity(AngleUnit paramAngleUnit);
  
  Set<Axis> getAngularVelocityAxes();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\Gyroscope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */