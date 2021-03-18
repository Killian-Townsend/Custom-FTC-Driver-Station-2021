package org.firstinspires.ftc.robotcore.internal.network;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.GlobalWarningSource;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class PreferenceRemoterRC extends PreferenceRemoter {
  public static final String TAG = "NetDiscover_prefremrc";
  
  protected static PreferenceRemoterRC theInstance;
  
  protected Set<String> rcPrefsOfInterestToDS;
  
  protected WarningSource warningSource;
  
  public PreferenceRemoterRC() {
    HashSet<String> hashSet = new HashSet();
    this.rcPrefsOfInterestToDS = hashSet;
    hashSet.add(this.context.getString(R.string.pref_device_name));
    this.rcPrefsOfInterestToDS.add(this.context.getString(R.string.pref_app_theme));
    this.rcPrefsOfInterestToDS.add(this.context.getString(R.string.pref_sound_on_off));
    this.rcPrefsOfInterestToDS.add(this.context.getString(R.string.pref_wifip2p_remote_channel_change_works));
    this.rcPrefsOfInterestToDS.add(this.context.getString(R.string.pref_wifip2p_channel));
    this.rcPrefsOfInterestToDS.add(this.context.getString(R.string.pref_has_independent_phone_battery));
    this.rcPrefsOfInterestToDS.add(this.context.getString(R.string.pref_has_speaker));
    this.rcPrefsOfInterestToDS.add(this.context.getString(R.string.pref_warn_about_outdated_firmware));
    this.rcPrefsOfInterestToDS.add(this.context.getString(R.string.pref_warn_about_mismatched_app_versions));
    this.rcPrefsOfInterestToDS.add(this.context.getString(R.string.pref_warn_about_2_4_ghz_band));
    this.warningSource = new WarningSource();
  }
  
  public static PreferenceRemoterRC getInstance() {
    // Byte code:
    //   0: ldc org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterRC
    //   2: monitorenter
    //   3: getstatic org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterRC.theInstance : Lorg/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterRC;
    //   6: ifnonnull -> 19
    //   9: new org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterRC
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: putstatic org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterRC.theInstance : Lorg/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterRC;
    //   19: getstatic org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterRC.theInstance : Lorg/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterRC;
    //   22: astore_0
    //   23: ldc org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterRC
    //   25: monitorexit
    //   26: aload_0
    //   27: areturn
    //   28: astore_0
    //   29: ldc org/firstinspires/ftc/robotcore/internal/network/PreferenceRemoterRC
    //   31: monitorexit
    //   32: aload_0
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   3	19	28	finally
    //   19	23	28	finally
  }
  
  public String getTag() {
    return "NetDiscover_prefremrc";
  }
  
  public CallbackResult handleCommandRobotControllerPreference(String paramString) {
    RobotControllerPreference robotControllerPreference = RobotControllerPreference.deserialize(paramString);
    if (robotControllerPreference.getPrefName().equals(AppUtil.getDefContext().getString(R.string.pref_wifip2p_channel))) {
      if (robotControllerPreference.getValue() != null && robotControllerPreference.getValue() instanceof Integer) {
        (new WifiDirectChannelChanger()).changeToChannel(((Integer)robotControllerPreference.getValue()).intValue());
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("incorrect preference value type: ");
        stringBuilder.append(robotControllerPreference.getValue());
        RobotLog.ee("NetDiscover_prefremrc", stringBuilder.toString());
      } 
    } else {
      Integer integer;
      if (robotControllerPreference.getPrefName().equals(this.context.getString(R.string.pref_ds_version_code))) {
        integer = (Integer)robotControllerPreference.getValue();
        this.warningSource.checkForMismatchedAppVersions(integer.intValue());
      } else {
        Boolean bool;
        if (integer.getPrefName().equals(this.context.getString(R.string.pref_ds_supports_5_ghz))) {
          bool = (Boolean)integer.getValue();
          this.warningSource.checkForUnnecessary2_4GhzUsage(bool.booleanValue());
        } else {
          this.preferencesHelper.writePrefIfDifferent(bool.getPrefName(), bool.getValue());
        } 
      } 
    } 
    return CallbackResult.HANDLED;
  }
  
  protected SharedPreferences.OnSharedPreferenceChangeListener makeSharedPrefListener() {
    return new SharedPreferencesListenerRC();
  }
  
  public void sendAllPreferences() {
    RobotLog.vv("NetDiscover_prefremrc", "sendAllPreferences()");
    Iterator<String> iterator = this.rcPrefsOfInterestToDS.iterator();
    while (iterator.hasNext())
      sendPreference(iterator.next()); 
  }
  
  protected void sendPreference(String paramString) {
    Object object = this.preferencesHelper.readPref(paramString);
    if (object != null)
      sendPreference(new RobotControllerPreference(paramString, object)); 
  }
  
  protected class SharedPreferencesListenerRC implements SharedPreferences.OnSharedPreferenceChangeListener {
    public void onSharedPreferenceChanged(SharedPreferences param1SharedPreferences, String param1String) {
      RobotLog.vv("NetDiscover_prefremrc", "onSharedPreferenceChanged(name=%s, value=%s)", new Object[] { param1String, this.this$0.preferencesHelper.readPref(param1String) });
      if (PreferenceRemoterRC.this.rcPrefsOfInterestToDS.contains(param1String))
        PreferenceRemoterRC.this.sendPreference(param1String); 
    }
  }
  
  protected class WarningSource implements GlobalWarningSource, PeerStatusCallback {
    private volatile boolean currentlyUsing2_4Ghz_cache;
    
    private volatile String mismatchedAppVersionsWarning;
    
    private final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AppUtil.getDefContext());
    
    private final ElapsedTime timeSinceLastChannelCheck = new ElapsedTime(0L);
    
    private volatile String unnecessary2_4GhzUsageWarning;
    
    public WarningSource() {
      RobotLog.registerGlobalWarningSource(this);
      NetworkConnectionHandler.getInstance().registerPeerStatusCallback(this);
    }
    
    private boolean currentlyUsing2_4Ghz() {
      if (this.timeSinceLastChannelCheck.seconds() > 5.0D) {
        boolean bool;
        ApChannel apChannel = ApChannelManagerFactory.getInstance().getCurrentChannel();
        if (apChannel != ApChannel.UNKNOWN && apChannel.band == ApChannel.Band.BAND_2_4_GHZ) {
          bool = true;
        } else {
          bool = false;
        } 
        this.currentlyUsing2_4Ghz_cache = bool;
        this.timeSinceLastChannelCheck.reset();
      } 
      return this.currentlyUsing2_4Ghz_cache;
    }
    
    public void checkForMismatchedAppVersions(int param1Int) {
      int i;
      String str;
      try {
        i = (PreferenceRemoterRC.this.context.getPackageManager().getPackageInfo(PreferenceRemoterRC.this.context.getPackageName(), 0)).versionCode;
        if (i == param1Int) {
          this.mismatchedAppVersionsWarning = null;
          return;
        } 
      } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
        nameNotFoundException.printStackTrace();
        return;
      } 
      if (i < param1Int) {
        str = "Robot Controller";
      } else {
        str = "Driver Station";
      } 
      this.mismatchedAppVersionsWarning = PreferenceRemoterRC.this.context.getString(R.string.warningMismatchedAppVersions, new Object[] { str });
    }
    
    public void checkForUnnecessary2_4GhzUsage(boolean param1Boolean) {
      boolean bool = WifiUtil.is5GHzAvailable();
      if (param1Boolean && bool && currentlyUsing2_4Ghz()) {
        this.unnecessary2_4GhzUsageWarning = PreferenceRemoterRC.this.context.getString(R.string.warning2_4GhzUnnecessaryUsage);
        return;
      } 
      this.unnecessary2_4GhzUsageWarning = null;
    }
    
    public void clearGlobalWarning() {
      this.mismatchedAppVersionsWarning = null;
      this.unnecessary2_4GhzUsageWarning = null;
    }
    
    public String getGlobalWarning() {
      ArrayList<String> arrayList = new ArrayList();
      if (this.sharedPrefs.getBoolean(PreferenceRemoterRC.this.context.getString(R.string.pref_warn_about_mismatched_app_versions), true))
        arrayList.add(this.mismatchedAppVersionsWarning); 
      if (this.sharedPrefs.getBoolean(PreferenceRemoterRC.this.context.getString(R.string.pref_warn_about_2_4_ghz_band), true))
        if (currentlyUsing2_4Ghz()) {
          arrayList.add(this.unnecessary2_4GhzUsageWarning);
        } else {
          this.unnecessary2_4GhzUsageWarning = null;
        }  
      return RobotLog.combineGlobalWarnings(arrayList);
    }
    
    public void onPeerConnected() {}
    
    public void onPeerDisconnected() {
      clearGlobalWarning();
    }
    
    public void setGlobalWarning(String param1String) {}
    
    public void suppressGlobalWarning(boolean param1Boolean) {}
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\PreferenceRemoterRC.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */