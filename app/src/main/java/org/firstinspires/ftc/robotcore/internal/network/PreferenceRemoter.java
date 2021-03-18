package org.firstinspires.ftc.robotcore.internal.network;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Map;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;

public abstract class PreferenceRemoter extends WifiStartStoppable {
  protected Context context;
  
  protected PreferencesHelper preferencesHelper;
  
  private SharedPreferences sharedPreferences;
  
  protected SharedPreferences.OnSharedPreferenceChangeListener sharedPreferencesListener;
  
  public PreferenceRemoter() {
    super(WifiDirectAgent.getInstance());
    Application application = AppUtil.getInstance().getApplication();
    this.context = (Context)application;
    this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)application);
    this.preferencesHelper = new PreferencesHelper(getTag(), this.sharedPreferences);
    this.sharedPreferencesListener = makeSharedPrefListener();
    dumpAllPrefs();
  }
  
  protected boolean doStart() throws InterruptedException {
    this.sharedPreferences.registerOnSharedPreferenceChangeListener(this.sharedPreferencesListener);
    return true;
  }
  
  protected void doStop() throws InterruptedException {
    this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this.sharedPreferencesListener);
  }
  
  protected void dumpAllPrefs() {
    RobotLog.vv(getTag(), "----- all preferences -----");
    for (Map.Entry entry : this.sharedPreferences.getAll().entrySet()) {
      RobotLog.vv(getTag(), "name='%s' value=%s", new Object[] { entry.getKey(), entry.getValue() });
    } 
  }
  
  public abstract CallbackResult handleCommandRobotControllerPreference(String paramString);
  
  protected abstract SharedPreferences.OnSharedPreferenceChangeListener makeSharedPrefListener();
  
  protected void sendPreference(RobotControllerPreference paramRobotControllerPreference) {
    RobotLog.vv(getTag(), "sending RC pref name=%s value=%s", new Object[] { paramRobotControllerPreference.getPrefName(), paramRobotControllerPreference.getValue() });
    Command command = new Command("CMD_ROBOT_CONTROLLER_PREFERENCE", paramRobotControllerPreference.serialize());
    NetworkConnectionHandler.getInstance().sendCommand(command);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\PreferenceRemoter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */