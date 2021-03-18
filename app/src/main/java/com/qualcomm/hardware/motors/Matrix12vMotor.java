package com.qualcomm.hardware.motors;

import com.qualcomm.robotcore.hardware.configuration.DistributorInfo;
import com.qualcomm.robotcore.hardware.configuration.ModernRoboticsMotorControllerParams;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;

@DistributorInfo(distributor = "Modern Robotics", model = "50-0120", url = "http://www.modernroboticsinc.com/12v-6mm-motor-kit-2")
@ModernRoboticsMotorControllerParams(D = 136, I = 44, P = 192, ratio = 16)
@DeviceProperties(builtIn = true, name = "Matrix 12v Motor", xmlTag = "Matrix12vMotor")
@MotorType(gearing = 52.8D, maxRPM = 190.0D, orientation = Rotation.CW, ticksPerRev = 1478.4D)
public interface Matrix12vMotor {}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\motors\Matrix12vMotor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */