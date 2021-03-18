package com.qualcomm.robotcore.util;

import java.io.File;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class IncludedFirmwareFileInfo {
  private static final String FILENAME = "REVHubFirmware_1_08_02.bin";
  
  public static final RobotCoreCommandList.FWImage FW_IMAGE;
  
  public static final String HUMAN_READABLE_FW_VERSION = "1.8.2";
  
  private static final File assetLocation;
  
  private static final File assetParentLocation = new File(AppUtil.UPDATES_DIR.getName(), AppUtil.LYNX_FIRMWARE_UPDATE_DIR.getName());
  
  static {
    assetLocation = new File(assetParentLocation, "REVHubFirmware_1_08_02.bin");
    FW_IMAGE = new RobotCoreCommandList.FWImage(assetLocation, true);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\IncludedFirmwareFileInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */