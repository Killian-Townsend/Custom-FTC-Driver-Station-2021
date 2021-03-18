package com.qualcomm.robotcore.wifi;

public enum NetworkType {
  LOOPBACK, RCWIRELESSAP, SOFTAP, UNKNOWN_NETWORK_TYPE, WIFIDIRECT, WIRELESSAP;
  
  static {
    LOOPBACK = new NetworkType("LOOPBACK", 1);
    SOFTAP = new NetworkType("SOFTAP", 2);
    WIRELESSAP = new NetworkType("WIRELESSAP", 3);
    RCWIRELESSAP = new NetworkType("RCWIRELESSAP", 4);
    NetworkType networkType = new NetworkType("UNKNOWN_NETWORK_TYPE", 5);
    UNKNOWN_NETWORK_TYPE = networkType;
    $VALUES = new NetworkType[] { WIFIDIRECT, LOOPBACK, SOFTAP, WIRELESSAP, RCWIRELESSAP, networkType };
  }
  
  public static NetworkType fromString(String paramString) {
    try {
      return valueOf(paramString.toUpperCase());
    } catch (Exception exception) {
      return UNKNOWN_NETWORK_TYPE;
    } 
  }
  
  public static NetworkType globalDefault() {
    return WIFIDIRECT;
  }
  
  public static String globalDefaultAsString() {
    return globalDefault().toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\wifi\NetworkType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */