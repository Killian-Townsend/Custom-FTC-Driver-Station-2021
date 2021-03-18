package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.configuration.ServoFlavor;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.ServoType;

@DeviceProperties(builtIn = true, name = "@string/configTypeContinuousRotationServo", xmlTag = "ContinuousRotationServo")
@ServoType(flavor = ServoFlavor.CONTINUOUS)
public interface CRServo extends DcMotorSimple {
  ServoController getController();
  
  int getPortNumber();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\CRServo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */