package org.firstinspires.ftc.robotcore.internal.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class OpModeMetaAndInstance {
  public final OpMode instance;
  
  public final InstanceOpModeRegistrar instanceOpModeRegistrar;
  
  public final OpModeMeta meta;
  
  public OpModeMetaAndInstance(OpModeMeta paramOpModeMeta, OpMode paramOpMode, InstanceOpModeRegistrar paramInstanceOpModeRegistrar) {
    this.meta = paramOpModeMeta;
    this.instance = paramOpMode;
    this.instanceOpModeRegistrar = paramInstanceOpModeRegistrar;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opmode\OpModeMetaAndInstance.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */