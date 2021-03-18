package com.qualcomm.ftccommon;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.FrameLayout;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.NetworkType;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;
import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;
import org.firstinspires.inspection.R;

public class FtcAdvancedRCSettingsActivity extends ThemedActivity {
  protected static final String CLIENT_CONNECTED = "CLIENT_CONNECTED";
  
  public static final String TAG = "FtcAdvancedRCSettingsActivity";
  
  protected FrameLayout getBackBar() {
    return (FrameLayout)findViewById(R.id.backbar);
  }
  
  public String getTag() {
    return "FtcAdvancedRCSettingsActivity";
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_generic_settings);
    DeviceNameManagerFactory.getInstance().initializeDeviceNameIfNecessary();
    SettingsFragment settingsFragment = new SettingsFragment();
    Bundle bundle = new Bundle();
    bundle.putBoolean("CLIENT_CONNECTED", (new PreferencesHelper("FtcAdvancedRCSettingsActivity", (Context)this)).readBoolean(getString(R.string.pref_rc_connected), false));
    settingsFragment.setArguments(bundle);
    getFragmentManager().beginTransaction().replace(R.id.container, (Fragment)settingsFragment).commit();
  }
  
  public static class SettingsFragment extends PreferenceFragment {
    protected boolean clientConnected;
    
    protected boolean controlHubConnectionMode;
    
    protected PreferencesHelper preferencesHelper;
    
    protected boolean remoteConfigure;
    
    public SettingsFragment() {
      boolean bool = false;
      this.clientConnected = false;
      this.remoteConfigure = AppUtil.getInstance().isDriverStation();
      if (NetworkConnectionHandler.getDefaultNetworkType(AppUtil.getDefContext()) == NetworkType.WIRELESSAP)
        bool = true; 
      this.controlHubConnectionMode = bool;
      this.preferencesHelper = new PreferencesHelper("FtcAdvancedRCSettingsActivity");
    }
    
    public void onCreate(Bundle param1Bundle) {
      boolean bool;
      super.onCreate(param1Bundle);
      this.clientConnected = getArguments().getBoolean("CLIENT_CONNECTED");
      addPreferencesFromResource(R.xml.advanced_rc_settings);
      Preference preference1 = findPreference(getString(R.string.pref_launch_wifi_remembered_groups_edit));
      Preference preference2 = findPreference(getString(R.string.pref_launch_wifi_channel_edit));
      Preference preference3 = findPreference(getString(R.string.pref_launch_lynx_firmware_update));
      if (LynxConstants.isRevControlHub() || this.controlHubConnectionMode)
        preference3.setSummary(R.string.summaryLynxFirmwareUpdateCH); 
      if (!this.clientConnected)
        for (bool = false; bool < getPreferenceScreen().getPreferenceCount(); bool++)
          getPreferenceScreen().getPreference(bool).setEnabled(false);  
      if ((this.remoteConfigure && LynxConstants.isRevControlHub()) || (this.clientConnected && !this.preferencesHelper.readBoolean(getString(R.string.pref_wifip2p_remote_channel_change_works), false))) {
        bool = true;
      } else {
        bool = false;
      } 
      preference2.setEnabled(bool ^ true);
      preference1.setEnabled(bool ^ true);
      RobotLog.vv("FtcAdvancedRCSettingsActivity", "clientConnected=%s", new Object[] { Boolean.valueOf(this.clientConnected) });
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\FtcAdvancedRCSettingsActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */