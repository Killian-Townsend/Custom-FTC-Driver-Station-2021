package com.qualcomm.hardware.lynx;

import com.qualcomm.robotcore.util.RobotLog;

public class EmbeddedControlHubModule {
  public static final String TAG = "EmbeddedControlHubModule";
  
  protected static volatile LynxModule embeddedLynxModule;
  
  public static void clear() {
    RobotLog.vv("EmbeddedControlHubModule", "clearing module");
    embeddedLynxModule = null;
  }
  
  public static LynxModule get() {
    return embeddedLynxModule;
  }
  
  public static void set(LynxModule paramLynxModule) {
    embeddedLynxModule = paramLynxModule;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\EmbeddedControlHubModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */