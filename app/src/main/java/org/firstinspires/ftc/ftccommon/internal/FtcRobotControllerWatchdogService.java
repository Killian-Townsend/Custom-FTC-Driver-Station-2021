package org.firstinspires.ftc.ftccommon.internal;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class FtcRobotControllerWatchdogService extends Service {
  public static final String TAG = "FtcRobotControllerWatchdogService";
  
  public static boolean isFtcRobotControllerActivity(Activity paramActivity) {
    try {
      Class<?> clazz2 = Class.forName("org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity");
      Class<?> clazz1 = paramActivity.getClass();
      return (clazz2 == clazz1);
    } catch (ClassNotFoundException|RuntimeException classNotFoundException) {
      return false;
    } 
  }
  
  public static boolean isLaunchActivity(Activity paramActivity) {
    return (paramActivity != null && isLaunchActivity(paramActivity.getClass()));
  }
  
  public static boolean isLaunchActivity(Class paramClass) {
    return (paramClass == ActivityFinder.launchActivityClass);
  }
  
  public static Class launchActivity() {
    return ActivityFinder.launchActivityClass;
  }
  
  public static void launchRobotController(Context paramContext) {
    RobotLog.vv("FtcRobotControllerWatchdogService", "launchRobotController()");
    Intent intent = new Intent(paramContext, ActivityFinder.launchActivityClass);
    intent.addFlags(268435456);
    paramContext.startActivity(intent);
  }
  
  public static boolean shouldAutoLaunchRobotController() {
    boolean bool;
    if (AppUtil.getInstance().isRobotController() && LynxConstants.isRevControlHub() && LynxConstants.autorunRobotController()) {
      bool = true;
    } else {
      bool = false;
    } 
    RobotLog.vv("FtcRobotControllerWatchdogService", "shouldAutoLauchRobotController() result=%s", new Object[] { Boolean.valueOf(bool) });
    return bool;
  }
  
  public IBinder onBind(Intent paramIntent) {
    return null;
  }
  
  public void onCreate() {
    super.onCreate();
    RobotLog.vv("FtcRobotControllerWatchdogService", "onCreate()");
  }
  
  public void onDestroy() {
    super.onDestroy();
    RobotLog.vv("FtcRobotControllerWatchdogService", "onDestroy()");
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
    RobotLog.vv("FtcRobotControllerWatchdogService", "onStartCommand() intent=%s flags=0x%x startId=%d", new Object[] { paramIntent, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    if (AppUtil.getInstance().isRobotController()) {
      boolean bool = shouldAutoLaunchRobotController();
      if (paramIntent == null && bool)
        launchRobotController((Context)this); 
      return bool ? 1 : 2;
    } 
    RobotLog.dd("FtcRobotControllerWatchdogService", "onStartCommand(): running on DS: shutting down");
    stopSelf();
    return 2;
  }
  
  protected static class ActivityFinder {
    protected static final Class launchActivityClass = findLaunchActivityClass();
    
    protected static Class findLaunchActivityClass() {
      try {
        Context context = AppUtil.getDefContext();
        Class<?> clazz = Class.forName(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName());
      } catch (ClassNotFoundException|RuntimeException classNotFoundException1) {
        classNotFoundException1 = null;
      } 
      classNotFoundException2 = classNotFoundException1;
      if (classNotFoundException1 == null)
        try {
          Class<?> clazz = Class.forName("org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity");
        } catch (ClassNotFoundException|RuntimeException classNotFoundException2) {
          classNotFoundException2 = classNotFoundException1;
        }  
      if (classNotFoundException2 != null)
        return (Class)classNotFoundException2; 
      throw AppUtil.getInstance().unreachable("FtcRobotControllerWatchdogService");
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\ftccommon\internal\FtcRobotControllerWatchdogService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */