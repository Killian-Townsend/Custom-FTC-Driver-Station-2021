package org.firstinspires.ftc.robotcore.internal.network;

import java.util.Set;

public interface ApChannelManager {
  ApChannel getCurrentChannel();
  
  Set<ApChannel> getSupportedChannels();
  
  ApChannel resetChannel(boolean paramBoolean);
  
  void setChannel(ApChannel paramApChannel, boolean paramBoolean) throws InvalidNetworkSettingException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\ApChannelManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */