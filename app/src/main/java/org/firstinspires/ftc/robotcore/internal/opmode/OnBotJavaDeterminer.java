package org.firstinspires.ftc.robotcore.internal.opmode;

import com.qualcomm.robotcore.util.RobotLog;

public class OnBotJavaDeterminer {
  private static String TAG = "OnBotJavaDeterminer";
  
  private static String onBotJavaConstructorName = "org.firstinspires.ftc.onbotjava.OnBotJavaClassLoader";
  
  public static boolean isOnBotJava(Class paramClass) {
    ClassLoader classLoader = paramClass.getClassLoader();
    Class<?> clazz = classLoader.getClass();
    try {
      boolean bool = clazz.getConstructor(new Class[0]).getName().equals(onBotJavaConstructorName);
      RobotLog.vv(TAG, "isOnBotJava: class=%s loader=%s: %s", new Object[] { paramClass.getSimpleName(), classLoader.getClass().getSimpleName(), Boolean.valueOf(bool) });
      return bool;
    } catch (NoSuchMethodException noSuchMethodException) {
      RobotLog.vv(TAG, "isOnBotJava: class=%s loader=%s: %s", new Object[] { paramClass.getSimpleName(), classLoader.getClass().getSimpleName(), Boolean.valueOf(false) });
      return false;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opmode\OnBotJavaDeterminer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */