package com.qualcomm.robotcore.hardware;

import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class LynxModuleMeta {
  protected boolean isParent;
  
  protected int moduleAddress;
  
  public LynxModuleMeta(int paramInt, boolean paramBoolean) {
    this.moduleAddress = paramInt;
    this.isParent = paramBoolean;
  }
  
  public LynxModuleMeta(LynxModuleMeta paramLynxModuleMeta) {
    this.moduleAddress = paramLynxModuleMeta.getModuleAddress();
    this.isParent = paramLynxModuleMeta.isParent();
  }
  
  public LynxModuleMeta(RobotCoreLynxModule paramRobotCoreLynxModule) {
    this.moduleAddress = paramRobotCoreLynxModule.getModuleAddress();
    this.isParent = paramRobotCoreLynxModule.isParent();
  }
  
  public int getModuleAddress() {
    return this.moduleAddress;
  }
  
  public boolean isParent() {
    return this.isParent;
  }
  
  public String toString() {
    return Misc.formatForUser("LynxModuleMeta(#%d,%s)", new Object[] { Integer.valueOf(this.moduleAddress), Boolean.valueOf(this.isParent) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\LynxModuleMeta.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */