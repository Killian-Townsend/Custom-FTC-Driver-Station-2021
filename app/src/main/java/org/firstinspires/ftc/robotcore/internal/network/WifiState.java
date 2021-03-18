package org.firstinspires.ftc.robotcore.internal.network;

public enum WifiState {
  DISABLED,
  DISABLING(0),
  ENABLED(0),
  ENABLING(0),
  FAILED(0),
  UNKNOWN(0);
  
  public int state;
  
  static {
    DISABLED = new WifiState("DISABLED", 1, 1);
    ENABLING = new WifiState("ENABLING", 2, 2);
    ENABLED = new WifiState("ENABLED", 3, 3);
    UNKNOWN = new WifiState("UNKNOWN", 4, 4);
    WifiState wifiState = new WifiState("FAILED", 5, 5);
    FAILED = wifiState;
    $VALUES = new WifiState[] { DISABLING, DISABLED, ENABLING, ENABLED, UNKNOWN, wifiState };
  }
  
  WifiState(int paramInt1) {
    this.state = paramInt1;
  }
  
  public static WifiState from(int paramInt) {
    for (WifiState wifiState : values()) {
      if (wifiState.state == paramInt)
        return wifiState; 
    } 
    return UNKNOWN;
  }
  
  public boolean isEnabled() {
    return (this == ENABLED);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\WifiState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */