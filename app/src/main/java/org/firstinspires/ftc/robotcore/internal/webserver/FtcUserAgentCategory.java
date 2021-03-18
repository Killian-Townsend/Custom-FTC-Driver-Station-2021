package org.firstinspires.ftc.robotcore.internal.webserver;

import android.content.Context;
import android.content.pm.PackageManager;
import com.qualcomm.robotcore.util.Version;
import java.util.regex.Pattern;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public enum FtcUserAgentCategory {
  DRIVER_STATION, OTHER, ROBOT_CONTROLLER;
  
  public static final String TAG;
  
  private static final Pattern patternDriverStation;
  
  private static final Pattern patternRobotController;
  
  private static final String uaDriverStation = "FtcDriverStation";
  
  private static final String uaRobotController = "FtcRobotController";
  
  static {
    FtcUserAgentCategory ftcUserAgentCategory = new FtcUserAgentCategory("OTHER", 2);
    OTHER = ftcUserAgentCategory;
    $VALUES = new FtcUserAgentCategory[] { DRIVER_STATION, ROBOT_CONTROLLER, ftcUserAgentCategory };
    TAG = FtcUserAgentCategory.class.getSimpleName();
    patternRobotController = Pattern.compile("[\\s]*FtcRobotController/", 0);
    patternDriverStation = Pattern.compile("[\\s]*FtcDriverStation/", 0);
  }
  
  public static String addToUserAgent(String paramString) {
    String str;
    Context context = AppUtil.getDefContext();
    try {
      str = (context.getPackageManager().getPackageInfo(context.getPackageName(), 0)).versionName;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      str = "3.1";
    } 
    if (AppUtil.getInstance().isRobotController()) {
      str = String.format(" %s/%s (library:%s)", new Object[] { "FtcRobotController", str, Version.getLibraryVersion() });
    } else if (AppUtil.getInstance().isDriverStation()) {
      str = String.format(" %s/%s (library:%s)", new Object[] { "FtcDriverStation", str, Version.getLibraryVersion() });
    } else {
      str = "";
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(str);
    return stringBuilder.toString();
  }
  
  public static FtcUserAgentCategory fromUserAgent(String paramString) {
    return patternRobotController.matcher(paramString).find() ? ROBOT_CONTROLLER : (patternDriverStation.matcher(paramString).find() ? DRIVER_STATION : OTHER);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\webserver\FtcUserAgentCategory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */