package org.firstinspires.ftc.robotcore.internal.network;

import android.content.Context;
import com.qualcomm.robotcore.R;

public enum NetworkStatus {
  ACTIVE, CREATED_AP_CONNECTION, ENABLED, ERROR, INACTIVE, UNKNOWN;
  
  static {
    INACTIVE = new NetworkStatus("INACTIVE", 1);
    ACTIVE = new NetworkStatus("ACTIVE", 2);
    ENABLED = new NetworkStatus("ENABLED", 3);
    ERROR = new NetworkStatus("ERROR", 4);
    NetworkStatus networkStatus = new NetworkStatus("CREATED_AP_CONNECTION", 5);
    CREATED_AP_CONNECTION = networkStatus;
    $VALUES = new NetworkStatus[] { UNKNOWN, INACTIVE, ACTIVE, ENABLED, ERROR, networkStatus };
  }
  
  public String toString(Context paramContext, Object... paramVarArgs) {
    switch (this) {
      default:
        return paramContext.getString(R.string.networkStatusInternalError);
      case null:
        return String.format(paramContext.getString(R.string.networkStatusCreatedAPConnection), paramVarArgs);
      case null:
        return paramContext.getString(R.string.networkStatusError);
      case null:
        return paramContext.getString(R.string.networkStatusEnabled);
      case null:
        return paramContext.getString(R.string.networkStatusInactive);
      case null:
        return paramContext.getString(R.string.networkStatusActive);
      case null:
        break;
    } 
    return paramContext.getString(R.string.networkStatusUnknown);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\NetworkStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */