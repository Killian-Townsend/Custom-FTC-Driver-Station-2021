package com.qualcomm.robotcore.eventloop.opmode;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;

public interface OpModeManager {
  public static final String DEFAULT_OP_MODE_NAME = "$Stop$Robot$";
  
  void register(String paramString, OpMode paramOpMode);
  
  void register(String paramString, Class<? extends OpMode> paramClass);
  
  void register(OpModeMeta paramOpModeMeta, OpMode paramOpMode);
  
  void register(OpModeMeta paramOpModeMeta, Class<? extends OpMode> paramClass);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\eventloop\opmode\OpModeManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */