package org.firstinspires.ftc.robotcore.internal.system;

import android.app.Application;
import com.qualcomm.robotcore.util.RobotLog;

public class RobotApplication extends Application {
  public void onCreate() {
    super.onCreate();
    AppUtil.onApplicationStart(this);
    RobotLog.onApplicationStart();
    ClassFactoryImpl.onApplicationStart();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\RobotApplication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */