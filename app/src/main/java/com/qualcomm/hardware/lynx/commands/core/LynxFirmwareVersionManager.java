package com.qualcomm.hardware.lynx.commands.core;

import android.content.Context;
import com.qualcomm.hardware.lynx.LynxI2cDeviceSynch;
import com.qualcomm.hardware.lynx.LynxI2cDeviceSynchV1;
import com.qualcomm.hardware.lynx.LynxI2cDeviceSynchV2;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.util.RobotLog;

public class LynxFirmwareVersionManager {
  public static LynxI2cDeviceSynch createLynxI2cDeviceSynch(Context paramContext, LynxModule paramLynxModule, int paramInt) {
    if (paramLynxModule.isCommandSupported(LynxI2cWriteReadMultipleBytesCommand.class)) {
      RobotLog.i("LynxFirmwareVersionManager: LynxI2cDeviceSynchV2");
      return (LynxI2cDeviceSynch)new LynxI2cDeviceSynchV2(paramContext, paramLynxModule, paramInt);
    } 
    RobotLog.i("LynxFirmwareVersionManager: LynxI2cDeviceSynchV1");
    return (LynxI2cDeviceSynch)new LynxI2cDeviceSynchV1(paramContext, paramLynxModule, paramInt);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxFirmwareVersionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */