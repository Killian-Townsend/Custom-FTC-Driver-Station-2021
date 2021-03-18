package org.firstinspires.ftc.robotcore.internal.network;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;

public class PreferenceRemoterDS extends PreferenceRemoter {
  public static final String TAG = "NetDiscover_prefremds";
  
  protected static PreferenceRemoterDS theInstance;
  
  protected PreferencesHelper.StringMap mapGroupOwnerToDeviceName;
  
  public PreferenceRemoterDS() {
    loadRenameMap();
    this.preferencesHelper.remove(this.context.getString(R.string.pref_wifip2p_groupowner_lastconnectedto));
    this.preferencesHelper.remove(this.context.getString(R.string.pref_wifip2p_channel));
    this.preferencesHelper.remove(this.context.getString(R.string.pref_has_independent_phone_battery_rc));
  }
  
  public static PreferenceRemoterDS getInstance() {
    // Byte code:
    //   0: ldc org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterDS
    //   2: monitorenter
    //   3: getstatic org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterDS.theInstance : Lorg/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterDS;
    //   6: ifnonnull -> 19
    //   9: new org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterDS
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: putstatic org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterDS.theInstance : Lorg/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterDS;
    //   19: getstatic org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterDS.theInstance : Lorg/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterDS;
    //   22: astore_0
    //   23: ldc org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterDS
    //   25: monitorexit
    //   26: aload_0
    //   27: areturn
    //   28: astore_0
    //   29: ldc org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterDS
    //   31: monitorexit
    //   32: aload_0
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   3	19	28	finally
    //   19	23	28	finally
  }
  
  protected void clearRenameMap() {
    RobotLog.vv("NetDiscover_prefremds", "clearRenameMap()");
    this.mapGroupOwnerToDeviceName = new PreferencesHelper.StringMap();
    saveRenameMap();
  }
  
  public String getDeviceNameForWifiP2pGroupOwner(String paramString) {
    String str = (String)this.mapGroupOwnerToDeviceName.get(paramString);
    if (str != null)
      paramString = str; 
    return paramString;
  }
  
  public String getTag() {
    return "NetDiscover_prefremds";
  }
  
  public CallbackResult handleCommandRobotControllerPreference(String paramString) {
    RobotControllerPreference robotControllerPreference = RobotControllerPreference.deserialize(paramString);
    RobotLog.vv(getTag(), "handleRobotControllerPreference() pref=%s", new Object[] { robotControllerPreference.getPrefName() });
    if (robotControllerPreference.getPrefName().equals(this.context.getString(R.string.pref_sound_on_off))) {
      if (this.preferencesHelper.readBoolean(this.context.getString(R.string.pref_has_speaker_rc), true)) {
        this.preferencesHelper.writePrefIfDifferent(this.context.getString(R.string.pref_sound_on_off_rc), robotControllerPreference.getValue());
      } else {
        this.preferencesHelper.writeBooleanPrefIfDifferent(this.context.getString(R.string.pref_sound_on_off_rc), false);
      } 
    } else if (robotControllerPreference.getPrefName().equals(this.context.getString(R.string.pref_wifip2p_channel))) {
      int i = ((Integer)robotControllerPreference.getValue()).intValue();
      RobotLog.vv("NetDiscover_prefremds", "pref_wifip2p_channel: prefChannel = %d", new Object[] { Integer.valueOf(i) });
      this.preferencesHelper.writeIntPrefIfDifferent(this.context.getString(R.string.pref_wifip2p_channel), i);
    } else if (robotControllerPreference.getPrefName().equals(this.context.getString(R.string.pref_has_speaker))) {
      this.preferencesHelper.writePrefIfDifferent(this.context.getString(R.string.pref_has_speaker_rc), robotControllerPreference.getValue());
      if (!this.preferencesHelper.readBoolean(this.context.getString(R.string.pref_has_speaker_rc), true))
        this.preferencesHelper.writeBooleanPrefIfDifferent(this.context.getString(R.string.pref_sound_on_off_rc), false); 
    } else if (robotControllerPreference.getPrefName().equals(this.context.getString(R.string.pref_app_theme))) {
      this.preferencesHelper.writePrefIfDifferent(this.context.getString(R.string.pref_app_theme_rc), robotControllerPreference.getValue());
    } else if (robotControllerPreference.getPrefName().equals(this.context.getString(R.string.pref_device_name))) {
      this.preferencesHelper.writePrefIfDifferent(this.context.getString(R.string.pref_device_name_rc), robotControllerPreference.getValue());
      this.preferencesHelper.writePrefIfDifferent(this.context.getString(R.string.pref_device_name_rc_display), robotControllerPreference.getValue());
      String str = this.preferencesHelper.readString(this.context.getString(R.string.pref_wifip2p_groupowner_connectedto), "");
      paramString = str;
      if (str.isEmpty())
        paramString = this.preferencesHelper.readString(this.context.getString(R.string.pref_wifip2p_groupowner_lastconnectedto), ""); 
      if (!paramString.isEmpty()) {
        str = (String)robotControllerPreference.getValue();
        this.mapGroupOwnerToDeviceName.put(paramString, str);
        saveRenameMap();
      } else {
        RobotLog.ee("NetDiscover_prefremds", "odd: we got a name change from an RC we're not actually connected to");
      } 
    } else if (robotControllerPreference.getPrefName().equals(this.context.getString(R.string.pref_wifip2p_remote_channel_change_works))) {
      this.preferencesHelper.writePrefIfDifferent(robotControllerPreference.getPrefName(), robotControllerPreference.getValue());
    } else if (robotControllerPreference.getPrefName().equals(this.context.getString(R.string.pref_has_independent_phone_battery))) {
      this.preferencesHelper.writePrefIfDifferent(this.context.getString(R.string.pref_has_independent_phone_battery_rc), robotControllerPreference.getValue());
    } else if (robotControllerPreference.getPrefName().equals(this.context.getString(R.string.pref_warn_about_outdated_firmware))) {
      this.preferencesHelper.writePrefIfDifferent(robotControllerPreference.getPrefName(), robotControllerPreference.getValue());
    } else if (robotControllerPreference.getPrefName().equals(this.context.getString(R.string.pref_warn_about_mismatched_app_versions))) {
      this.preferencesHelper.writePrefIfDifferent(robotControllerPreference.getPrefName(), robotControllerPreference.getValue());
    } else if (robotControllerPreference.getPrefName().equals(this.context.getString(R.string.pref_warn_about_2_4_ghz_band))) {
      this.preferencesHelper.writePrefIfDifferent(robotControllerPreference.getPrefName(), robotControllerPreference.getValue());
    } 
    return CallbackResult.HANDLED;
  }
  
  protected void loadRenameMap() {
    this.mapGroupOwnerToDeviceName = this.preferencesHelper.readStringMap(this.context.getString(R.string.pref_wifip2p_groupowner_map), new PreferencesHelper.StringMap());
  }
  
  protected SharedPreferences.OnSharedPreferenceChangeListener makeSharedPrefListener() {
    return new SharedPreferencesListenerDS();
  }
  
  public void onPhoneBoot() {
    RobotLog.vv("NetDiscover_prefremds", "onPhoneBoot()");
    clearRenameMap();
  }
  
  public void onWifiToggled(boolean paramBoolean) {
    RobotLog.vv("NetDiscover_prefremds", "onWifiToggled(%s)", new Object[] { Boolean.valueOf(paramBoolean) });
    if (!paramBoolean)
      clearRenameMap(); 
  }
  
  protected void saveRenameMap() {
    this.preferencesHelper.writeStringMapPrefIfDifferent(this.context.getString(R.string.pref_wifip2p_groupowner_map), this.mapGroupOwnerToDeviceName);
  }
  
  public void sendInformationalPrefsToRc() {
    int i = 0;
    try {
      int j = (this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0)).versionCode;
      i = j;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
    } 
    sendPreference(new RobotControllerPreference(this.context.getString(R.string.pref_ds_version_code), Integer.valueOf(i)));
    sendPreference(new RobotControllerPreference(this.context.getString(R.string.pref_ds_supports_5_ghz), Boolean.valueOf(WifiUtil.is5GHzAvailable())));
  }
  
  protected class SharedPreferencesListenerDS implements SharedPreferences.OnSharedPreferenceChangeListener {
    public void onSharedPreferenceChanged(SharedPreferences param1SharedPreferences, String param1String) {
      RobotLog.vv("NetDiscover_prefremds", "onSharedPreferenceChanged(name=%s, value=%s)", new Object[] { param1String, this.this$0.preferencesHelper.readPref(param1String) });
      if (param1String.equals(PreferenceRemoterDS.this.context.getString(R.string.pref_sound_on_off_rc))) {
        String str = PreferenceRemoterDS.this.context.getString(R.string.pref_sound_on_off);
      } else if (param1String.equals(PreferenceRemoterDS.this.context.getString(R.string.pref_device_name_rc))) {
        String str = PreferenceRemoterDS.this.context.getString(R.string.pref_device_name);
      } else if (param1String.equals(PreferenceRemoterDS.this.context.getString(R.string.pref_app_theme_rc))) {
        String str = PreferenceRemoterDS.this.context.getString(R.string.pref_app_theme);
      } else if (param1String.equals(PreferenceRemoterDS.this.context.getString(R.string.pref_wifip2p_channel))) {
        String str = PreferenceRemoterDS.this.context.getString(R.string.pref_wifip2p_channel);
      } else if (param1String.equals(PreferenceRemoterDS.this.context.getString(R.string.pref_warn_about_outdated_firmware)) || param1String.equals(PreferenceRemoterDS.this.context.getString(R.string.pref_warn_about_mismatched_app_versions)) || param1String.equals(PreferenceRemoterDS.this.context.getString(R.string.pref_warn_about_2_4_ghz_band))) {
        String str = param1String;
      } else {
        param1SharedPreferences = null;
      } 
      if (param1SharedPreferences != null) {
        Object object = PreferenceRemoterDS.this.preferencesHelper.readPref(param1String);
        if (object != null)
          PreferenceRemoterDS.this.sendPreference(new RobotControllerPreference((String)param1SharedPreferences, object)); 
      } 
      if (param1String.equals(PreferenceRemoterDS.this.context.getString(R.string.pref_wifip2p_groupowner_connectedto))) {
        String str = PreferenceRemoterDS.this.preferencesHelper.readString(param1String, "");
        if (!str.isEmpty()) {
          PreferenceRemoterDS.this.preferencesHelper.writePrefIfDifferent(PreferenceRemoterDS.this.context.getString(R.string.pref_wifip2p_groupowner_lastconnectedto), str);
          return;
        } 
        RobotLog.vv("NetDiscover_prefremds", "%s has been removed", new Object[] { param1String });
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\PreferenceRemoterDS.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */