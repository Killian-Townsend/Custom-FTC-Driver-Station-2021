package com.qualcomm.hardware.motors;

import com.qualcomm.robotcore.hardware.configuration.DistributorInfo;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;

@DistributorInfo(distributor = "Modern Robotics", model = "50-0001", url = "http://modernroboticsinc.com/standard-motor-kit-50-0001-2")
@DeviceProperties(builtIn = true, name = "Matrix Legacy 9.6v Motor", xmlTag = "MatrixLegacyMotor")
@MotorType(gearing = 27.04D, maxRPM = 195.0D, orientation = Rotation.CW, ticksPerRev = 757.12D)
public interface MatrixLegacyMotor {}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\motors\MatrixLegacyMotor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */