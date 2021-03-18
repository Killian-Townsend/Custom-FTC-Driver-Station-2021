package com.qualcomm.ftcdriverstation;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.FrameLayout;
import com.google.gson.Gson;
import com.qualcomm.ftccommon.configuration.EditActivity;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.NetworkType;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectDeviceNameManager;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;

public class FtcDriverStationSettingsActivity extends EditActivity {
  protected static final String CLIENT_CONNECTED = "CLIENT_CONNECTED";
  
  protected static final String HAS_SPEAKER = "HAS_SPEAKER";
  
  protected static final String RESULT = "RESULT";
  
  public static final String TAG = "FtcDriverStationSettingsActivity";
  
  protected boolean clientConnected = false;
  
  protected boolean hasSpeaker = false;
  
  protected NetworkType lastNetworkType;
  
  protected PreferencesHelper prefHelper;
  
  protected Result result = new Result();
  
  protected void checkForPairingMethodChange() {
    if (NetworkType.fromString(this.prefHelper.readString(getString(2131624445), NetworkType.globalDefaultAsString())) != this.lastNetworkType)
      this.result.prefPairingMethodChanged = true; 
  }
  
  protected void finishOk() {
    checkForPairingMethodChange();
    Bundle bundle = new Bundle();
    bundle.putString("RESULT", this.result.serialize());
    Intent intent = new Intent();
    intent.putExtras(bundle);
    finishOk(intent);
  }
  
  protected FrameLayout getBackBar() {
    return (FrameLayout)findViewById(2131230804);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131427371);
    PreferencesHelper preferencesHelper = new PreferencesHelper("FtcDriverStationSettingsActivity", (Context)this);
    this.prefHelper = preferencesHelper;
    this.lastNetworkType = NetworkType.fromString(preferencesHelper.readString(getString(2131624445), NetworkType.globalDefaultAsString()));
    this.clientConnected = this.prefHelper.readBoolean(getString(2131624448), false);
    this.hasSpeaker = this.prefHelper.readBoolean(getString(2131624421), true);
    DeviceNameManagerFactory.getInstance().initializeDeviceNameIfNecessary();
    SettingsFragment settingsFragment = new SettingsFragment();
    Bundle bundle = new Bundle();
    bundle.putBoolean("CLIENT_CONNECTED", this.clientConnected);
    bundle.putBoolean("HAS_SPEAKER", this.hasSpeaker);
    settingsFragment.setArguments(bundle);
    settingsFragment.setProperties(this, this.prefHelper);
    getFragmentManager().beginTransaction().replace(2131230855, (Fragment)settingsFragment).commit();
  }
  
  public static class Result {
    public boolean prefAdvancedClicked = false;
    
    public boolean prefLogsClicked = false;
    
    public boolean prefPairClicked = false;
    
    public boolean prefPairingMethodChanged = false;
    
    public static Result deserialize(String param1String) {
      return (Result)(new Gson()).fromJson(param1String, Result.class);
    }
    
    public String serialize() {
      return (new Gson()).toJson(this);
    }
  }
  
  public static class SettingsFragment extends PreferenceFragment {
    protected FtcDriverStationSettingsActivity activity;
    
    protected PreferencesHelper prefHelper;
    
    public void onCreate(Bundle param1Bundle) {
      super.onCreate(param1Bundle);
      boolean bool1 = getArguments().getBoolean("CLIENT_CONNECTED");
      boolean bool2 = getArguments().getBoolean("HAS_SPEAKER");
      addPreferencesFromResource(2131820545);
      if (!bool1) {
        findPreference(getString(2131624410)).setEnabled(false);
        findPreference(getString(2131624398)).setEnabled(false);
        findPreference(getString(2131624450)).setEnabled(false);
      } 
      if (!bool2)
        findPreference(getString(2131624450)).setEnabled(false); 
      findPreference(getString(2131624444)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference param2Preference) {
              NetworkType networkType = NetworkType.fromString(FtcDriverStationSettingsActivity.SettingsFragment.this.prefHelper.readString(FtcDriverStationSettingsActivity.SettingsFragment.this.getString(2131624445), NetworkType.globalDefaultAsString()));
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("prefPair clicked ");
              stringBuilder.append(networkType);
              RobotLog.vv("FtcDriverStationSettingsActivity", stringBuilder.toString());
              FtcDriverStationSettingsActivity.SettingsFragment.this.activity.result.prefPairClicked = true;
              if (networkType == NetworkType.WIFIDIRECT) {
                FtcDriverStationSettingsActivity.SettingsFragment.this.startActivity(new Intent((Context)FtcDriverStationSettingsActivity.SettingsFragment.this.activity, FtcPairNetworkConnectionActivity.class));
              } else {
                FtcDriverStationSettingsActivity.SettingsFragment.this.startActivity(new Intent((Context)FtcDriverStationSettingsActivity.SettingsFragment.this.activity, FtcWirelessApNetworkConnectionActivity.class));
              } 
              return false;
            }
          });
      findPreference(getString(2131624424)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference param2Preference) {
              RobotLog.vv("FtcDriverStationSettingsActivity", "prefAdvanced clicked");
              FtcDriverStationSettingsActivity.SettingsFragment.this.activity.result.prefAdvancedClicked = true;
              return false;
            }
          });
      findPreference(getString(2131624406)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference param2Preference) {
              RobotLog.vv("FtcDriverStationSettingsActivity", "prefLogs clicked");
              FtcDriverStationSettingsActivity.SettingsFragment.this.activity.result.prefLogsClicked = true;
              return false;
            }
          });
      findPreference(getString(2131624407)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference param2Preference, Object param2Object) {
              if (param2Object instanceof String && WifiDirectDeviceNameManager.validDeviceName((String)param2Object))
                return true; 
              AlertDialog.Builder builder = new AlertDialog.Builder((Context)FtcDriverStationSettingsActivity.SettingsFragment.this.getActivity());
              builder.setTitle(FtcDriverStationSettingsActivity.SettingsFragment.this.getString(2131624479));
              builder.setMessage(FtcDriverStationSettingsActivity.SettingsFragment.this.getString(2131624478));
              builder.setPositiveButton(17039370, null);
              builder.show();
              return false;
            }
          });
    }
    
    public void setProperties(FtcDriverStationSettingsActivity param1FtcDriverStationSettingsActivity, PreferencesHelper param1PreferencesHelper) {
      this.activity = param1FtcDriverStationSettingsActivity;
      this.prefHelper = param1PreferencesHelper;
    }
  }
  
  class null implements Preference.OnPreferenceClickListener {
    public boolean onPreferenceClick(Preference param1Preference) {
      NetworkType networkType = NetworkType.fromString(this.this$0.prefHelper.readString(this.this$0.getString(2131624445), NetworkType.globalDefaultAsString()));
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("prefPair clicked ");
      stringBuilder.append(networkType);
      RobotLog.vv("FtcDriverStationSettingsActivity", stringBuilder.toString());
      this.this$0.activity.result.prefPairClicked = true;
      if (networkType == NetworkType.WIFIDIRECT) {
        this.this$0.startActivity(new Intent((Context)this.this$0.activity, FtcPairNetworkConnectionActivity.class));
      } else {
        this.this$0.startActivity(new Intent((Context)this.this$0.activity, FtcWirelessApNetworkConnectionActivity.class));
      } 
      return false;
    }
  }
  
  class null implements Preference.OnPreferenceClickListener {
    public boolean onPreferenceClick(Preference param1Preference) {
      RobotLog.vv("FtcDriverStationSettingsActivity", "prefAdvanced clicked");
      this.this$0.activity.result.prefAdvancedClicked = true;
      return false;
    }
  }
  
  class null implements Preference.OnPreferenceClickListener {
    public boolean onPreferenceClick(Preference param1Preference) {
      RobotLog.vv("FtcDriverStationSettingsActivity", "prefLogs clicked");
      this.this$0.activity.result.prefLogsClicked = true;
      return false;
    }
  }
  
  class null implements Preference.OnPreferenceChangeListener {
    public boolean onPreferenceChange(Preference param1Preference, Object param1Object) {
      if (param1Object instanceof String && WifiDirectDeviceNameManager.validDeviceName((String)param1Object))
        return true; 
      AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.this$0.getActivity());
      builder.setTitle(this.this$0.getString(2131624479));
      builder.setMessage(this.this$0.getString(2131624478));
      builder.setPositiveButton(17039370, null);
      builder.show();
      return false;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\FtcDriverStationSettingsActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */