package org.firstinspires.ftc.robotcore.internal.webserver;

import android.content.pm.PackageManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.RobotLog;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.firstinspires.ftc.robotcore.internal.hardware.CachedLynxFirmwareVersions;
import org.firstinspires.ftc.robotcore.internal.hardware.android.AndroidBoard;
import org.firstinspires.ftc.robotcore.internal.network.ApChannel;
import org.firstinspires.ftc.robotcore.internal.network.ApChannelManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.WifiUtil;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class RobotControllerWebInfo {
  public static final String TAG = RobotControllerWebInfo.class.getSimpleName();
  
  private static final String cachedChOsVersion;
  
  private static String cachedRcVersion;
  
  private static final Gson gson = (new GsonBuilder()).registerTypeAdapter(ApChannel.class, new ApChannel.GsonTypeAdapter()).create();
  
  private final boolean appUpdateRequiresReboot;
  
  private final Set<ApChannel> availableChannels;
  
  private final String chOsVersion = cachedChOsVersion;
  
  private final ApChannel currentChannel;
  
  private final String deviceName;
  
  private FtcUserAgentCategory ftcUserAgentCategory;
  
  private final String includedFirmwareFileVersion;
  
  private final boolean isREVControlHub;
  
  private final String networkName;
  
  private final String passphrase;
  
  private final String rcVersion = cachedRcVersion;
  
  private final List<CachedLynxFirmwareVersions.LynxModuleInfo> revHubNamesAndVersions;
  
  private final boolean serverIsAlive;
  
  private final String serverUrl;
  
  private final boolean supports5GhzAp;
  
  private final boolean supportsOtaUpdate;
  
  private final String timeServerStarted;
  
  private final long timeServerStartedMillis;
  
  static {
    cachedChOsVersion = LynxConstants.getControlHubOsVersion();
    try {
      cachedRcVersion = (AppUtil.getDefContext().getPackageManager().getPackageInfo(AppUtil.getDefContext().getPackageName(), 0)).versionName;
      return;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      RobotLog.ee(TAG, (Throwable)nameNotFoundException, "Unable to find the RC version name");
      cachedRcVersion = "unknown";
      return;
    } 
  }
  
  public RobotControllerWebInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean, long paramLong) {
    String str2 = DeviceNameManagerFactory.getInstance().getDeviceName();
    this.deviceName = str2;
    String str1 = paramString1;
    if (paramString1 == null)
      str1 = str2; 
    this.networkName = str1;
    this.passphrase = paramString2;
    this.serverUrl = paramString3;
    this.serverIsAlive = paramBoolean;
    this.timeServerStartedMillis = paramLong;
    this.isREVControlHub = LynxConstants.isRevControlHub();
    this.ftcUserAgentCategory = FtcUserAgentCategory.OTHER;
    this.supports5GhzAp = WifiUtil.is5GHzAvailable();
    this.appUpdateRequiresReboot = AndroidBoard.getInstance().hasControlHubUpdater() ^ true;
    this.supportsOtaUpdate = AndroidBoard.getInstance().hasControlHubUpdater();
    this.availableChannels = ApChannelManagerFactory.getInstance().getSupportedChannels();
    this.currentChannel = ApChannelManagerFactory.getInstance().getCurrentChannel();
    this.revHubNamesAndVersions = CachedLynxFirmwareVersions.getFormattedVersions();
    this.includedFirmwareFileVersion = "1.8.2";
    this.timeServerStarted = (new SimpleDateFormat("MMM dd, h:mm aa", Locale.getDefault())).format(new Date(paramLong));
  }
  
  public static RobotControllerWebInfo fromJson(String paramString) {
    return (RobotControllerWebInfo)gson.fromJson(paramString, RobotControllerWebInfo.class);
  }
  
  public boolean doesAppUpdateRequireReboot() {
    return this.appUpdateRequiresReboot;
  }
  
  public String getDeviceName() {
    return this.deviceName;
  }
  
  public FtcUserAgentCategory getFtcUserAgentCategory() {
    return this.ftcUserAgentCategory;
  }
  
  public String getNetworkName() {
    return this.networkName;
  }
  
  public String getPassphrase() {
    return this.passphrase;
  }
  
  public String getServerUrl() {
    return this.serverUrl;
  }
  
  public String getTimeServerStarted() {
    return this.timeServerStarted;
  }
  
  public long getTimeServerStartedMillis() {
    return this.timeServerStartedMillis;
  }
  
  public boolean is5GhzApSupported() {
    return this.supports5GhzAp;
  }
  
  public boolean isOtaUpdateSupported() {
    return this.supportsOtaUpdate;
  }
  
  public boolean isREVControlHub() {
    return this.isREVControlHub;
  }
  
  public boolean isServerAlive() {
    return this.serverIsAlive;
  }
  
  public void setFtcUserAgentCategory(Map<String, String> paramMap) {
    this.ftcUserAgentCategory = FtcUserAgentCategory.fromUserAgent(paramMap.get("user-agent"));
  }
  
  public String toJson() {
    return gson.toJson(this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\webserver\RobotControllerWebInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */