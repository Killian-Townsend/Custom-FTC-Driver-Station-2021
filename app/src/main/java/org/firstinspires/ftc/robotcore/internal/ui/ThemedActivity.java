package org.firstinspires.ftc.robotcore.internal.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;

public abstract class ThemedActivity extends BaseActivity {
  public static void appAppThemeToActivity(String paramString, Activity paramActivity) {
    PreferencesHelper preferencesHelper = new PreferencesHelper(paramString, (Context)paramActivity);
    String str = paramActivity.getString(R.string.pref_app_theme);
    paramString = preferencesHelper.readString(str, paramActivity.getString(R.string.tokenThemeRed));
    preferencesHelper.writePrefIfDifferent(str, paramString);
    String[] arrayOfString = paramActivity.getResources().getStringArray(R.array.app_theme_tokens);
    TypedArray typedArray = paramActivity.getResources().obtainTypedArray(R.array.app_theme_ids);
    int i = 0;
    while (true) {
      if (i < arrayOfString.length) {
        if (arrayOfString[i].equals(paramString)) {
          paramActivity.setTheme(typedArray.getResourceId(i, 0));
          i = 1;
          break;
        } 
        i++;
        continue;
      } 
      i = 0;
      break;
    } 
    if (i == 0)
      paramActivity.setTheme(typedArray.getResourceId(0, 0)); 
    typedArray.recycle();
  }
  
  public static void restartForAppThemeChange(String paramString1, final String toast) {
    final AppUtil appUtil = AppUtil.getInstance();
    RobotLog.vv(paramString1, "app theme changed: restarting app: %s", new Object[] { toast });
    appUtil.runOnUiThread(new Runnable() {
          public void run() {
            appUtil.showToast(UILocation.BOTH, toast);
            (new Handler()).postDelayed(new Runnable() {
                  public void run() {
                    appUtil.restartApp(0);
                  }
                },  1250L);
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    appAppThemeToActivity(getTag(), this);
    super.onCreate(paramBundle);
  }
  
  public void restartForAppThemeChange(int paramInt) {
    restartForAppThemeChange(getTag(), getString(paramInt));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\ui\ThemedActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */