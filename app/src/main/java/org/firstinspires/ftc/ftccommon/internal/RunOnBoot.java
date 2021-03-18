package org.firstinspires.ftc.ftccommon.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.internal.hardware.android.AndroidBoard;
import org.firstinspires.ftc.robotcore.internal.network.PreferenceRemoterDS;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;

public class RunOnBoot extends BroadcastReceiver {
  public static final String TAG = "RunOnBoot";
  
  protected Context context = null;
  
  protected PreferencesHelper preferencesHelper = null;
  
  protected boolean isRobotControllerRunningInThisProcess() {
    return FtcRobotControllerWatchdogService.isFtcRobotControllerActivity(AppUtil.getInstance().getRootActivity());
  }
  
  protected void launchRobotController() {
    RobotLog.vv("RunOnBoot", "launchRobotController()");
    FtcRobotControllerWatchdogService.launchRobotController(this.context);
    this.preferencesHelper.writeBooleanPrefIfDifferent(this.context.getString(R.string.pref_autostarted_robot_controller), true);
  }
  
  protected void noteAndroidBoardPresenceAndExitIfNoRC() {
    RobotLog.vv("RunOnBoot", "noteDragonboardPresenceAndExitIfNoRC()");
    if (LynxConstants.isRevControlHub())
      AndroidBoard.getInstance().getAndroidBoardIsPresentPin().setState(true); 
    if (!isRobotControllerRunningInThisProcess())
      AppUtil.getInstance().exitApplication(); 
  }
  
  protected void onDriverStationBoot() {
    PreferenceRemoterDS.getInstance().onPhoneBoot();
  }
  
  protected void onDriverStationPostBoot() {}
  
  public void onReceive(Context paramContext, Intent paramIntent) {
    this.context = paramContext;
    if (this.preferencesHelper == null)
      this.preferencesHelper = new PreferencesHelper("RunOnBoot", paramContext); 
    String str = paramIntent.getAction();
    RobotLog.vv("RunOnBoot", "onReceive() action=%s", new Object[] { str });
    if (str.equals("android.intent.action.BOOT_COMPLETED")) {
      if (AppUtil.getInstance().isRobotController()) {
        onRobotControllerBoot();
        return;
      } 
      if (AppUtil.getInstance().isDriverStation()) {
        onDriverStationBoot();
        return;
      } 
    } else {
      if (AppUtil.getInstance().isRobotController()) {
        onRobotControllerPostBoot();
        return;
      } 
      if (AppUtil.getInstance().isDriverStation())
        onDriverStationPostBoot(); 
    } 
  }
  
  protected void onRobotControllerBoot() {
    this.preferencesHelper.remove(this.context.getString(R.string.pref_autostarted_robot_controller));
    if (shouldAutoLaunchRobotController()) {
      launchRobotController();
      return;
    } 
    noteAndroidBoardPresenceAndExitIfNoRC();
  }
  
  protected void onRobotControllerPostBoot() {
    noteAndroidBoardPresenceAndExitIfNoRC();
  }
  
  protected boolean shouldAutoLaunchRobotController() {
    int i;
    boolean bool2 = FtcRobotControllerWatchdogService.shouldAutoLaunchRobotController();
    boolean bool1 = bool2;
    if (bool2)
      i = this.preferencesHelper.readBoolean(this.context.getString(R.string.pref_autostarted_robot_controller), false) ^ true; 
    RobotLog.vv("RunOnBoot", "shouldAutoLauchRobotController() result=%s", new Object[] { Boolean.valueOf(i) });
    return i;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\ftccommon\internal\RunOnBoot.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */