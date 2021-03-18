package org.firstinspires.ftc.robotcore.internal.network;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.EnumSet;
import java.util.Set;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;

public class WifiDirectChannelManager implements ApChannelManager {
  private static final String TAG = "WifiDirectChannelManager";
  
  private EnumSet<ApChannel> supportedChannels = null;
  
  public ApChannel getCurrentChannel() {
    boolean bool;
    int i = (new PreferencesHelper("WifiDirectChannelManager")).readInt(AppUtil.getDefContext().getString(R.string.pref_wifip2p_channel), -1);
    if (i == 0)
      return ApChannel.AUTO_2_4_GHZ; 
    if (i > 14) {
      bool = true;
    } else {
      bool = false;
    } 
    return ApChannel.fromBandAndChannel(bool, i);
  }
  
  public Set<ApChannel> getSupportedChannels() {
    if (this.supportedChannels == null) {
      EnumSet<ApChannel> enumSet = EnumSet.copyOf(ApChannel.ALL_2_4_GHZ_CHANNELS);
      enumSet.add(ApChannel.AUTO_2_4_GHZ);
      if (WifiUtil.is5GHzAvailable())
        enumSet.addAll(ApChannel.NON_DFS_5_GHZ_CHANNELS); 
      this.supportedChannels = enumSet;
    } 
    return this.supportedChannels;
  }
  
  public ApChannel resetChannel(boolean paramBoolean) {
    ApChannel apChannel = ApChannel.AUTO_2_4_GHZ;
    try {
      setChannel(apChannel, paramBoolean);
      return apChannel;
    } catch (InvalidNetworkSettingException invalidNetworkSettingException) {
      RobotLog.ee("WifiDirectChannelManager", invalidNetworkSettingException, "Unable to reset channel to default");
      return apChannel;
    } 
  }
  
  public void setChannel(ApChannel paramApChannel, boolean paramBoolean) throws InvalidNetworkSettingException {
    if (getSupportedChannels().contains(paramApChannel)) {
      if (paramBoolean)
        (new WifiDirectChannelChanger()).changeToChannel(paramApChannel.channelNum); 
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("This device does not support channel ");
    stringBuilder.append(paramApChannel);
    throw new InvalidNetworkSettingException(stringBuilder.toString());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\WifiDirectChannelManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */