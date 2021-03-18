package com.qualcomm.robotcore.eventloop.opmode;

public interface OpModeManagerNotifier {
  OpMode registerListener(Notifications paramNotifications);
  
  void unregisterListener(Notifications paramNotifications);
  
  public static interface Notifications {
    void onOpModePostStop(OpMode param1OpMode);
    
    void onOpModePreInit(OpMode param1OpMode);
    
    void onOpModePreStart(OpMode param1OpMode);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\eventloop\opmode\OpModeManagerNotifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */