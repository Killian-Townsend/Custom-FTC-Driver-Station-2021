package org.firstinspires.ftc.robotcore.internal.network;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.Device;
import com.qualcomm.robotcore.util.RobotLog;
import java.lang.reflect.InvocationTargetException;
import org.firstinspires.ftc.robotcore.internal.hardware.android.AndroidBoard;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class WifiUtil {
  private static final String NO_AP = "None";
  
  private static boolean showingLocationServicesDlg = false;
  
  public static void doLocationServicesCheck() {
    if (Build.VERSION.SDK_INT >= 26 && !AppUtil.getInstance().isRobotController()) {
      if (showingLocationServicesDlg)
        return; 
      int i = 0;
      try {
        int j = Settings.Secure.getInt(AppUtil.getDefContext().getContentResolver(), "location_mode");
        i = j;
      } catch (android.provider.Settings.SettingNotFoundException settingNotFoundException) {}
      if (i == 0) {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context)AppUtil.getInstance().getActivity());
        builder.setMessage(Misc.formatForUser(R.string.locationServices));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                AppUtil.getInstance().getActivity().startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                WifiUtil.access$002(false);
              }
            });
        builder.create();
        builder.show();
        showingLocationServicesDlg = true;
      } 
    } 
  }
  
  public static String getConnectedSsid() {
    return !isWifiConnected() ? "None" : getWifiManager().getConnectionInfo().getSSID().replace("\"", "");
  }
  
  protected static WifiManager getWifiManager() {
    return (WifiManager)AppUtil.getDefContext().getApplicationContext().getSystemService("wifi");
  }
  
  public static boolean is5GHzAvailable() {
    return Device.isRevControlHub() ? AndroidBoard.getInstance().supports5GhzAp() : getWifiManager().is5GHzBandSupported();
  }
  
  public static boolean isAirplaneModeOn() {
    ContentResolver contentResolver = AppUtil.getDefContext().getContentResolver();
    boolean bool = false;
    if (Settings.Global.getInt(contentResolver, "airplane_mode_on", 0) != 0)
      bool = true; 
    return bool;
  }
  
  public static boolean isBluetoothOn() {
    return (BluetoothAdapter.getDefaultAdapter() != null && BluetoothAdapter.getDefaultAdapter().isEnabled());
  }
  
  public static boolean isWifiApEnabled() {
    WifiManager wifiManager = getWifiManager();
    try {
      return ((Boolean)wifiManager.getClass().getMethod("isWifiApEnabled", new Class[0]).invoke(wifiManager, new Object[0])).booleanValue();
    } catch (NoSuchMethodException noSuchMethodException) {
    
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Could not invoke isWifiApEnabled ");
    stringBuilder.append(invocationTargetException.toString());
    RobotLog.e(stringBuilder.toString());
    return false;
  }
  
  public static boolean isWifiConnected() {
    boolean bool1 = isWifiEnabled();
    boolean bool = false;
    if (!bool1)
      return false; 
    NetworkInfo.DetailedState detailedState = WifiInfo.getDetailedStateOf(getWifiManager().getConnectionInfo().getSupplicantState());
    if (detailedState == NetworkInfo.DetailedState.CONNECTED || detailedState == NetworkInfo.DetailedState.OBTAINING_IPADDR)
      bool = true; 
    return bool;
  }
  
  public static boolean isWifiEnabled() {
    int i = getWifiManager().getWifiState();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("state = ");
    stringBuilder.append(i);
    RobotLog.i(stringBuilder.toString());
    return getWifiManager().isWifiEnabled();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\WifiUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */