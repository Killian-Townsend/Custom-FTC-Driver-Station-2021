package org.firstinspires.ftc.robotcore.internal.system;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ServiceController {
  public static final String TAG = "ServiceStarter";
  
  protected static final String metaDataAutoStartPrefix = "autoStartService.";
  
  protected static void autoStartServices() {
    Iterator<AutoStartableService> iterator = getAutoStartableServices().iterator();
    while (iterator.hasNext()) {
      AutoStartableService autoStartableService = iterator.next();
      try {
        startService(Class.forName(autoStartableService.className));
      } catch (ClassNotFoundException classNotFoundException) {
        throw AppUtil.getInstance().failFast("ServiceStarter", classNotFoundException, "configured service not found");
      } 
    } 
  }
  
  protected static List<AutoStartableService> getAutoStartableServices() {
    ArrayList<AutoStartableService> arrayList = new ArrayList();
    try {
      Bundle bundle = (AppUtil.getDefContext().getPackageManager().getApplicationInfo(AppUtil.getInstance().getApplication().getPackageName(), 128)).metaData;
      for (String str : bundle.keySet()) {
        if (str.startsWith("autoStartService.")) {
          String[] arrayOfString = bundle.getString(str).split("\\|");
          if (arrayOfString.length == 2) {
            if (("RC".equalsIgnoreCase(arrayOfString[0]) && AppUtil.getInstance().isRobotController()) || ("DS".equalsIgnoreCase(arrayOfString[0]) && AppUtil.getInstance().isDriverStation()) || "BOTH".equalsIgnoreCase(arrayOfString[0]))
              arrayList.add(new AutoStartableService(str.substring(17), Integer.parseInt(arrayOfString[1]))); 
            continue;
          } 
          throw AppUtil.getInstance().failFast("ServiceStarter", "incorrect manifest construction");
        } 
      } 
      Collections.sort(arrayList, new Comparator<AutoStartableService>() {
            public int compare(ServiceController.AutoStartableService param1AutoStartableService1, ServiceController.AutoStartableService param1AutoStartableService2) {
              int j = param1AutoStartableService1.launchOrder - param1AutoStartableService2.launchOrder;
              int i = j;
              if (j == 0)
                i = param1AutoStartableService1.className.compareTo(param1AutoStartableService2.className); 
              return i;
            }
          });
      return arrayList;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      throw AppUtil.getInstance().unreachable("ServiceStarter", nameNotFoundException);
    } 
  }
  
  public static void onApplicationStart() {
    autoStartServices();
  }
  
  public static boolean startService(Class paramClass) {
    RobotLog.vv("ServiceStarter", "attempting to start service %s", new Object[] { paramClass.getSimpleName() });
    Context context = AppUtil.getDefContext();
    Intent intent = new Intent(context, paramClass);
    try {
      if (context.startService(intent) == null) {
        RobotLog.ee("ServiceStarter", "unable to start service %s", new Object[] { paramClass.getSimpleName() });
        return false;
      } 
      RobotLog.vv("ServiceStarter", "started service %s", new Object[] { paramClass.getSimpleName() });
      return true;
    } catch (SecurityException securityException) {
      RobotLog.ee("ServiceStarter", securityException, "unable to start service %s", new Object[] { paramClass.getSimpleName() });
      return false;
    } 
  }
  
  public static boolean stopService(Class paramClass) {
    RobotLog.vv("ServiceStarter", "attempting to stop service %s", new Object[] { paramClass.getSimpleName() });
    Context context = AppUtil.getDefContext();
    Intent intent = new Intent(context, paramClass);
    try {
      context.stopService(intent);
      return true;
    } catch (SecurityException securityException) {
      RobotLog.ee("ServiceStarter", securityException, "unable to stop service %s", new Object[] { paramClass.getSimpleName() });
      return false;
    } 
  }
  
  protected static class AutoStartableService {
    public String className;
    
    public int launchOrder;
    
    public AutoStartableService(String param1String, int param1Int) {
      this.className = param1String;
      this.launchOrder = param1Int;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\ServiceController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */