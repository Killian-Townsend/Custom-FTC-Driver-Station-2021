package com.qualcomm.ftccommon;

public class LaunchActivityConstantsList {
  public static final String RC_WEB_INFO = "RC_WEB_INFO";
  
  public static final String VIEW_LOGS_ACTIVITY_FILENAME = "org.firstinspires.ftc.ftccommon.logFilename";
  
  public enum RequestCode {
    CONFIGURE_DRIVER_STATION, CONFIGURE_ROBOT_CONTROLLER, INSPECTIONS, PROGRAM_AND_MANAGE, SETTINGS_DRIVER_STATION, SETTINGS_ROBOT_CONTROLLER, UNKNOWN;
    
    static {
      CONFIGURE_DRIVER_STATION = new RequestCode("CONFIGURE_DRIVER_STATION", 2);
      PROGRAM_AND_MANAGE = new RequestCode("PROGRAM_AND_MANAGE", 3);
      SETTINGS_DRIVER_STATION = new RequestCode("SETTINGS_DRIVER_STATION", 4);
      SETTINGS_ROBOT_CONTROLLER = new RequestCode("SETTINGS_ROBOT_CONTROLLER", 5);
      RequestCode requestCode = new RequestCode("INSPECTIONS", 6);
      INSPECTIONS = requestCode;
      $VALUES = new RequestCode[] { UNKNOWN, CONFIGURE_ROBOT_CONTROLLER, CONFIGURE_DRIVER_STATION, PROGRAM_AND_MANAGE, SETTINGS_DRIVER_STATION, SETTINGS_ROBOT_CONTROLLER, requestCode };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\LaunchActivityConstantsList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */