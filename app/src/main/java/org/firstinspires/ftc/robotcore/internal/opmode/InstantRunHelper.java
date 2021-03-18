package org.firstinspires.ftc.robotcore.internal.opmode;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import com.qualcomm.robotcore.util.RobotLog;
import dalvik.system.DexFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InstantRunHelper {
  public static final String TAG = "InstantRunHelper";
  
  public static List<String> getAllClassNames(Context paramContext) {
    ArrayList<String> arrayList = new ArrayList();
    try {
      PackageManager packageManager = paramContext.getPackageManager();
      String str = paramContext.getPackageName();
      int i = 0;
      ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
      String[] arrayOfString = applicationInfo.splitSourceDirs;
      if (arrayOfString != null) {
        int j = arrayOfString.length;
        while (i < j) {
          String str1 = arrayOfString[i];
          try {
            DexFile dexFile = new DexFile(str1);
            try {
              arrayList.addAll(Collections.list(dexFile.entries()));
            } finally {
              dexFile.close();
            } 
          } catch (IOException iOException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error accessing apk file: ");
            stringBuilder.append(str1);
            stringBuilder.append(", IOException: ");
            stringBuilder.append(iOException.toString());
            RobotLog.ee("InstantRunHelper", iOException, stringBuilder.toString());
          } 
          i++;
        } 
      } 
      return arrayList;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      RobotLog.ee("InstantRunHelper", (Throwable)nameNotFoundException, "Could not obtain application info for class scanning");
      return arrayList;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opmode\InstantRunHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */