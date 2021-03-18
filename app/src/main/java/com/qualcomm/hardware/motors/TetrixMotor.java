package com.qualcomm.hardware.motors;

import com.qualcomm.robotcore.hardware.configuration.DistributorInfo;
import com.qualcomm.robotcore.hardware.configuration.ModernRoboticsMotorControllerParams;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;

@DistributorInfo(distributor = "Pitsco", model = "W39530", url = "http://www.pitsco.com/TETRIX_DC_Gear_Motor")
@ModernRoboticsMotorControllerParams(D = 128, I = 56, P = 160, ratio = 19)
@DeviceProperties(builtIn = true, name = "Tetrix Motor", xmlTag = "TetrixMotor")
@MotorType(gearing = 52.0D, maxRPM = 165.0D, orientation = Rotation.CW, ticksPerRev = 1440.0D)
public interface TetrixMotor {}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\motors\TetrixMotor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */