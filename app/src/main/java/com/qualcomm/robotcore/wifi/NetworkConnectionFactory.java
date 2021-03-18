package com.qualcomm.robotcore.wifi;

import android.content.Context;
import com.qualcomm.robotcore.util.RobotLog;

public class NetworkConnectionFactory {
  public static final String NETWORK_CONNECTION_TYPE = "NETWORK_CONNECTION_TYPE";
  
  public static NetworkConnection getNetworkConnection(NetworkType paramNetworkType, Context paramContext) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Getting network assistant of type: ");
    stringBuilder.append(paramNetworkType);
    RobotLog.v(stringBuilder.toString());
    int i = null.$SwitchMap$com$qualcomm$robotcore$wifi$NetworkType[paramNetworkType.ordinal()];
    return (NetworkConnection)((i != 1) ? ((i != 3) ? ((i != 4) ? ((i != 5) ? null : RobotControllerAccessPointAssistant.getRobotControllerAccessPointAssistant(paramContext)) : DriverStationAccessPointAssistant.getDriverStationAccessPointAssistant(paramContext)) : SoftApAssistant.getSoftApAssistant(paramContext)) : WifiDirectAssistant.getWifiDirectAssistant(paramContext));
  }
  
  public static NetworkType getTypeFromString(String paramString) {
    return NetworkType.fromString(paramString);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\wifi\NetworkConnectionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */