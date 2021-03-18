package com.qualcomm.ftcdriverstation;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.FrameLayout;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManagerFactory;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;
import org.firstinspires.ftc.robotcore.internal.ui.BaseActivity;

public class FtcDriverStationInspectionReportsActivity extends BaseActivity {
  protected static final String CLIENT_CONNECTED = "CLIENT_CONNECTED";
  
  public static final String TAG = "FtcDriverStationInspectionReportsActivity";
  
  protected FrameLayout getBackBar() {
    return (FrameLayout)findViewById(2131230804);
  }
  
  public String getTag() {
    return "FtcDriverStationInspectionReportsActivity";
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131427371);
    DeviceNameManagerFactory.getInstance().initializeDeviceNameIfNecessary();
    SettingsFragment settingsFragment = new SettingsFragment();
    Bundle bundle = new Bundle();
    bundle.putBoolean("CLIENT_CONNECTED", (new PreferencesHelper("FtcDriverStationInspectionReportsActivity", (Context)this)).readBoolean(getString(2131624448), false));
    settingsFragment.setArguments(bundle);
    getFragmentManager().beginTransaction().replace(2131230855, (Fragment)settingsFragment).commit();
  }
  
  public static class SettingsFragment extends PreferenceFragment {
    protected boolean clientConnected = false;
    
    public void onCreate(Bundle param1Bundle) {
      super.onCreate(param1Bundle);
      this.clientConnected = getArguments().getBoolean("CLIENT_CONNECTED");
      addPreferencesFromResource(2131820550);
      if (!this.clientConnected)
        findPreference(getString(2131624426)).setEnabled(false); 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\FtcDriverStationInspectionReportsActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */