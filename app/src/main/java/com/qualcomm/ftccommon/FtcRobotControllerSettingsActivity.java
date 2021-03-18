package com.qualcomm.ftccommon;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.FrameLayout;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectDeviceNameManager;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;
import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;

public class FtcRobotControllerSettingsActivity extends ThemedActivity {
  protected FrameLayout getBackBar() {
    return (FrameLayout)findViewById(R.id.backbar);
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_generic_settings);
    DeviceNameManagerFactory.getInstance().initializeDeviceNameIfNecessary();
    getFragmentManager().beginTransaction().replace(R.id.container, (Fragment)new SettingsFragment()).commit();
  }
  
  public static class SettingsFragment extends PreferenceFragment {
    public void onActivityResult(int param1Int1, int param1Int2, Intent param1Intent) {
      if ((param1Int1 == LaunchActivityConstantsList.RequestCode.CONFIGURE_ROBOT_CONTROLLER.ordinal() || param1Int1 == LaunchActivityConstantsList.RequestCode.SETTINGS_ROBOT_CONTROLLER.ordinal()) && param1Int2 == -1)
        getActivity().setResult(-1, param1Intent); 
    }
    
    public void onCreate(Bundle param1Bundle) {
      super.onCreate(param1Bundle);
      addPreferencesFromResource(R.xml.app_settings);
      findPreference(getString(R.string.pref_device_name)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference param2Preference, Object param2Object) {
              if (param2Object instanceof String && WifiDirectDeviceNameManager.validDeviceName((String)param2Object))
                return true; 
              AlertDialog.Builder builder = new AlertDialog.Builder((Context)FtcRobotControllerSettingsActivity.SettingsFragment.this.getActivity());
              builder.setTitle(FtcRobotControllerSettingsActivity.SettingsFragment.this.getString(R.string.prefedit_device_name_invalid_title));
              builder.setMessage(FtcRobotControllerSettingsActivity.SettingsFragment.this.getString(R.string.prefedit_device_name_invalid_text));
              builder.setPositiveButton(17039370, null);
              builder.show();
              return false;
            }
          });
      findPreference(getString(R.string.pref_launch_viewlogs)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference param2Preference) {
              Intent intent = new Intent(AppUtil.getDefContext(), ViewLogsActivity.class);
              intent.putExtra("org.firstinspires.ftc.ftccommon.logFilename", RobotLog.getLogFilename((Context)FtcRobotControllerSettingsActivity.SettingsFragment.this.getActivity()));
              FtcRobotControllerSettingsActivity.SettingsFragment.this.startActivity(intent);
              return true;
            }
          });
      if (!(new PreferencesHelper(getTag())).readBoolean(getString(R.string.pref_has_speaker), true))
        findPreference(getString(R.string.pref_sound_on_off)).setEnabled(false); 
    }
  }
  
  class null implements Preference.OnPreferenceChangeListener {
    public boolean onPreferenceChange(Preference param1Preference, Object param1Object) {
      if (param1Object instanceof String && WifiDirectDeviceNameManager.validDeviceName((String)param1Object))
        return true; 
      AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.this$0.getActivity());
      builder.setTitle(this.this$0.getString(R.string.prefedit_device_name_invalid_title));
      builder.setMessage(this.this$0.getString(R.string.prefedit_device_name_invalid_text));
      builder.setPositiveButton(17039370, null);
      builder.show();
      return false;
    }
  }
  
  class null implements Preference.OnPreferenceClickListener {
    public boolean onPreferenceClick(Preference param1Preference) {
      Intent intent = new Intent(AppUtil.getDefContext(), ViewLogsActivity.class);
      intent.putExtra("org.firstinspires.ftc.ftccommon.logFilename", RobotLog.getLogFilename((Context)this.this$0.getActivity()));
      this.this$0.startActivity(intent);
      return true;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\FtcRobotControllerSettingsActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */