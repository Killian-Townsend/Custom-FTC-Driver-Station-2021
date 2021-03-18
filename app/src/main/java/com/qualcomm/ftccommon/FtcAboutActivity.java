package com.qualcomm.ftccommon;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.widget.FrameLayout;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.util.Version;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;
import org.firstinspires.inspection.R;

public class FtcAboutActivity extends ThemedActivity {
  public static final String TAG = "FtcDriverStationAboutActivity";
  
  private static String buildTimeFromBuildConfig;
  
  private static final SimpleDateFormat iso860ZuluFormatter822 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ROOT);
  
  protected AboutFragment aboutFragment;
  
  protected final Context context = AppUtil.getDefContext();
  
  final RecvLoopRunnable.RecvLoopCallback recvLoopCallback = (RecvLoopRunnable.RecvLoopCallback)new RecvLoopRunnable.DegenerateCallback() {
      public CallbackResult commandEvent(Command param1Command) {
        String str = param1Command.getName();
        byte b = 0;
        RobotLog.vv("FtcDriverStationAboutActivity", "commandEvent: %s", new Object[] { str });
        if (FtcAboutActivity.this.remoteConfigure) {
          str = param1Command.getName();
          if (str.hashCode() != 436396154 || !str.equals("CMD_REQUEST_ABOUT_INFO_RESP"))
            b = -1; 
          if (b == 0) {
            final RobotCoreCommandList.AboutInfo aboutInfo = RobotCoreCommandList.AboutInfo.deserialize(param1Command.getExtra());
            AppUtil.getInstance().runOnUiThread(new Runnable() {
                  public void run() {
                    FtcAboutActivity.this.refreshRemote(aboutInfo);
                  }
                });
            return CallbackResult.HANDLED;
          } 
        } 
        return CallbackResult.NOT_HANDLED;
      }
    };
  
  protected Future refreshFuture = null;
  
  protected final boolean remoteConfigure = AppUtil.getInstance().isDriverStation();
  
  static {
    buildTimeFromBuildConfig = null;
  }
  
  protected static String getAppVersion() {
    Context context = AppUtil.getDefContext();
    try {
      return (context.getPackageManager().getPackageInfo(context.getPackageName(), 0)).versionName;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      return context.getString(R.string.unavailable);
    } 
  }
  
  protected static String getBuildTime() {
    try {
      Date date = iso860ZuluFormatter822.parse(buildTimeFromBuildConfig);
      if (date != null) {
        DateFormat dateFormat = SimpleDateFormat.getInstance();
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(date);
      } 
    } catch (ParseException parseException) {
      RobotLog.ee("FtcDriverStationAboutActivity", parseException, "exception determining build time");
    } 
    return AppUtil.getDefContext().getString(R.string.unavailable);
  }
  
  public static RobotCoreCommandList.AboutInfo getLocalAboutInfo() {
    RobotCoreCommandList.AboutInfo aboutInfo = new RobotCoreCommandList.AboutInfo();
    aboutInfo.appVersion = getAppVersion();
    aboutInfo.libVersion = Version.getLibraryVersion();
    aboutInfo.buildTime = getBuildTime();
    aboutInfo.networkProtocolVersion = String.format(Locale.US, "v%d", new Object[] { Byte.valueOf((byte)121) });
    aboutInfo.osVersion = LynxConstants.getControlHubOsVersion();
    NetworkConnection networkConnection = NetworkConnectionHandler.getInstance().getNetworkConnection();
    if (networkConnection != null) {
      aboutInfo.networkConnectionInfo = networkConnection.getInfo();
      return aboutInfo;
    } 
    aboutInfo.networkConnectionInfo = AppUtil.getDefContext().getString(R.string.unavailable);
    return aboutInfo;
  }
  
  public static void setBuildTimeFromBuildConfig(String paramString) {
    buildTimeFromBuildConfig = paramString;
  }
  
  protected FrameLayout getBackBar() {
    return (FrameLayout)findViewById(R.id.backbar);
  }
  
  public String getTag() {
    return "FtcDriverStationAboutActivity";
  }
  
  protected void onCreate(Bundle paramBundle) {
    RobotLog.vv("FtcDriverStationAboutActivity", "onCreate()");
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_generic_settings);
    DeviceNameManagerFactory.getInstance().initializeDeviceNameIfNecessary();
    this.aboutFragment = new AboutFragment();
    getFragmentManager().beginTransaction().replace(R.id.container, (Fragment)this.aboutFragment).commit();
    NetworkConnectionHandler.getInstance().pushReceiveLoopCallback(this.recvLoopCallback);
  }
  
  protected void onDestroy() {
    RobotLog.vv("FtcDriverStationAboutActivity", "onDestroy()");
    super.onDestroy();
    NetworkConnectionHandler.getInstance().removeReceiveLoopCallback(this.recvLoopCallback);
  }
  
  protected void onPause() {
    stopRefreshing();
    super.onPause();
  }
  
  protected void onResume() {
    super.onResume();
    startRefreshing();
  }
  
  protected void refresh() {
    this.aboutFragment.refreshLocal(getLocalAboutInfo());
    if (this.remoteConfigure)
      NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_REQUEST_ABOUT_INFO")); 
  }
  
  protected void refreshRemote(RobotCoreCommandList.AboutInfo paramAboutInfo) {
    this.aboutFragment.refreshRemote(paramAboutInfo);
  }
  
  protected void startRefreshing() {
    stopRefreshing();
    this.refreshFuture = ThreadPool.getDefaultScheduler().scheduleAtFixedRate(new Runnable() {
          public void run() {
            AppUtil.getInstance().runOnUiThread(new Runnable() {
                  public void run() {
                    FtcAboutActivity.this.refresh();
                  }
                },  );
          }
        },  0L, 5000L, TimeUnit.MILLISECONDS);
  }
  
  protected void stopRefreshing() {
    Future future = this.refreshFuture;
    if (future != null) {
      future.cancel(false);
      this.refreshFuture = null;
    } 
  }
  
  public static class AboutFragment extends PreferenceFragment {
    private boolean firstRemoteRefresh = true;
    
    protected final boolean remoteConfigure = AppUtil.getInstance().isDriverStation();
    
    public void onCreate(Bundle param1Bundle) {
      int i;
      super.onCreate(param1Bundle);
      addPreferencesFromResource(R.xml.ftc_about_activity);
      PreferenceCategory preferenceCategory = (PreferenceCategory)findPreference(getString(R.string.pref_app_category));
      if (this.remoteConfigure) {
        i = R.string.prefcat_about_ds;
      } else {
        i = R.string.prefcat_about_rc;
      } 
      preferenceCategory.setTitle(i);
      if (this.remoteConfigure) {
        addPreferencesFromResource(R.xml.ftc_about_activity_rc);
        findPreference(getString(R.string.pref_app_category_rc)).setTitle(R.string.prefcat_about_rc);
      } else if (LynxConstants.getControlHubOsVersion() != null) {
        Preference preference = new Preference(getPreferenceScreen().getContext());
        preference.setTitle(getString(R.string.about_ch_os_version));
        preference.setKey(getString(R.string.pref_os_version));
        preferenceCategory.addPreference(preference);
      } 
      refreshAllUnavailable();
    }
    
    public void refreshAllUnavailable() {
      setPreferenceSummary(R.string.pref_app_version, (String)null);
      setPreferenceSummary(R.string.pref_lib_version, (String)null);
      setPreferenceSummary(R.string.pref_network_protocol_version, (String)null);
      setPreferenceSummary(R.string.pref_build_time, (String)null);
      setPreferenceSummary(R.string.pref_network_connection_info, (String)null);
      setPreferenceSummary(R.string.pref_os_version, (String)null);
      setPreferenceSummary(R.string.pref_app_version_rc, (String)null);
      setPreferenceSummary(R.string.pref_lib_version_rc, (String)null);
      setPreferenceSummary(R.string.pref_network_protocol_version_rc, (String)null);
      setPreferenceSummary(R.string.pref_build_time_rc, (String)null);
      setPreferenceSummary(R.string.pref_network_connection_info_rc, (String)null);
      setPreferenceSummary(R.string.pref_os_version_rc, (String)null);
    }
    
    public void refreshLocal(RobotCoreCommandList.AboutInfo param1AboutInfo) {
      setPreferenceSummary(R.string.pref_app_version, param1AboutInfo.appVersion);
      setPreferenceSummary(R.string.pref_lib_version, param1AboutInfo.libVersion);
      setPreferenceSummary(R.string.pref_network_protocol_version, param1AboutInfo.networkProtocolVersion);
      setPreferenceSummary(R.string.pref_build_time, param1AboutInfo.buildTime);
      setPreferenceSummary(R.string.pref_network_connection_info, param1AboutInfo.networkConnectionInfo);
      setPreferenceSummary(R.string.pref_os_version, param1AboutInfo.osVersion);
    }
    
    public void refreshRemote(RobotCoreCommandList.AboutInfo param1AboutInfo) {
      if (this.remoteConfigure) {
        if (this.firstRemoteRefresh && param1AboutInfo.osVersion != null) {
          PreferenceCategory preferenceCategory = (PreferenceCategory)findPreference(getString(R.string.pref_app_category_rc));
          Preference preference = new Preference(getPreferenceScreen().getContext());
          preference.setTitle(getString(R.string.about_ch_os_version));
          preference.setKey(getString(R.string.pref_os_version_rc));
          preferenceCategory.addPreference(preference);
        } 
        this.firstRemoteRefresh = false;
        setPreferenceSummary(R.string.pref_app_version_rc, param1AboutInfo.appVersion);
        setPreferenceSummary(R.string.pref_lib_version_rc, param1AboutInfo.libVersion);
        setPreferenceSummary(R.string.pref_network_protocol_version_rc, param1AboutInfo.networkProtocolVersion);
        setPreferenceSummary(R.string.pref_build_time_rc, param1AboutInfo.buildTime);
        setPreferenceSummary(R.string.pref_network_connection_info_rc, param1AboutInfo.networkConnectionInfo);
        setPreferenceSummary(R.string.pref_os_version_rc, param1AboutInfo.osVersion);
      } 
    }
    
    protected void setPreferenceSummary(int param1Int, String param1String) {
      setPreferenceSummary(AppUtil.getDefContext().getString(param1Int), param1String);
    }
    
    protected void setPreferenceSummary(String param1String1, String param1String2) {
      String str = param1String2;
      if (TextUtils.isEmpty(param1String2))
        str = AppUtil.getDefContext().getString(R.string.unavailable); 
      Preference preference = findPreference(param1String1);
      if (preference != null)
        preference.setSummary(str); 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\FtcAboutActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */