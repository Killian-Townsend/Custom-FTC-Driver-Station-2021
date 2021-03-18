package org.firstinspires.ftc.robotcore.internal.opmode;

import java.io.File;
import java.util.Set;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public interface OnBotJavaHelper {
  public static final File buildSuccessfulFile;
  
  public static final File controlDir;
  
  public static final File jarDir;
  
  public static final File javaRoot = new File(AppUtil.FIRST_FOLDER, "/java/");
  
  public static final File srcDir = new File(javaRoot, "/src/");
  
  public static final File statusDir;
  
  static {
    jarDir = new File(srcDir, "/jars/");
    statusDir = new File(javaRoot, "/status/");
    buildSuccessfulFile = new File(statusDir, "buildSuccessful.txt");
    controlDir = new File(javaRoot, "/control/");
  }
  
  void close(ClassLoader paramClassLoader);
  
  ClassLoader getOnBotJavaClassLoader();
  
  Set<String> getOnBotJavaClassNames();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opmode\OnBotJavaHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */