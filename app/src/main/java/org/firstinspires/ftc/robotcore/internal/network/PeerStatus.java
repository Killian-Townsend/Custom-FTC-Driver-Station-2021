package org.firstinspires.ftc.robotcore.internal.network;

import android.content.Context;
import com.qualcomm.robotcore.R;

public enum PeerStatus {
  CONNECTED,
  DISCONNECTED,
  UNKNOWN(-1);
  
  public final byte bVal;
  
  static {
    DISCONNECTED = new PeerStatus("DISCONNECTED", 1, 0);
    PeerStatus peerStatus = new PeerStatus("CONNECTED", 2, 1);
    CONNECTED = peerStatus;
    $VALUES = new PeerStatus[] { UNKNOWN, DISCONNECTED, peerStatus };
  }
  
  PeerStatus(int paramInt1) {
    this.bVal = (byte)paramInt1;
  }
  
  public static PeerStatus fromByte(byte paramByte) {
    for (PeerStatus peerStatus : values()) {
      if (peerStatus.bVal == paramByte)
        return peerStatus; 
    } 
    return UNKNOWN;
  }
  
  public String toString(Context paramContext) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$network$PeerStatus[ordinal()];
    return (i != 1) ? ((i != 2) ? ((i != 3) ? paramContext.getString(R.string.networkStatusInternalError) : paramContext.getString(R.string.peerStatusDisconnected)) : paramContext.getString(R.string.peerStatusConnected)) : paramContext.getString(R.string.networkStatusUnknown);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\PeerStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */