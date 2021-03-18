package com.qualcomm.ftccommon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.FixWifiDirectSetup;
import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;

public class ConfigWifiDirectActivity extends ThemedActivity {
  public static final String TAG = "ConfigWifiDirectActivity";
  
  private static Flag flag = Flag.NONE;
  
  private Context context;
  
  private ProgressDialog progressDialog;
  
  private TextView textBadDeviceName;
  
  private TextView textPleaseWait;
  
  private WifiManager wifiManager;
  
  private void dismissProgressDialog() {
    runOnUiThread(new Runnable() {
          public void run() {
            ConfigWifiDirectActivity.this.progressDialog.dismiss();
          }
        });
  }
  
  public static void launch(Context paramContext) {
    launch(paramContext, Flag.WIFI_DIRECT_FIX_CONFIG);
  }
  
  public static void launch(Context paramContext, Flag paramFlag) {
    Intent intent = new Intent(paramContext, ConfigWifiDirectActivity.class);
    intent.addFlags(1342177280);
    paramContext.startActivity(intent);
    flag = paramFlag;
  }
  
  private void showProgressDialog() {
    runOnUiThread(new Runnable() {
          public void run() {
            ConfigWifiDirectActivity.access$702(ConfigWifiDirectActivity.this, new ProgressDialog(ConfigWifiDirectActivity.this.context, R.style.ConfigWifiDirectDialog));
            ConfigWifiDirectActivity.this.progressDialog.setMessage(ConfigWifiDirectActivity.this.getString(R.string.progressPleaseWait));
            ConfigWifiDirectActivity.this.progressDialog.setTitle("Configuring Wifi Direct");
            ConfigWifiDirectActivity.this.progressDialog.setIndeterminate(true);
            ConfigWifiDirectActivity.this.progressDialog.show();
          }
        });
  }
  
  public String getTag() {
    return "ConfigWifiDirectActivity";
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_config_wifi_direct);
    this.textPleaseWait = (TextView)findViewById(R.id.textPleaseWait);
    this.textBadDeviceName = (TextView)findViewById(R.id.textBadDeviceName);
    this.context = (Context)this;
  }
  
  protected void onPause() {
    super.onPause();
    flag = Flag.NONE;
    this.textBadDeviceName.setVisibility(4);
  }
  
  protected void onResume() {
    super.onResume();
    this.textPleaseWait.setVisibility(0);
    this.wifiManager = (WifiManager)getApplicationContext().getSystemService("wifi");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Processing flag ");
    stringBuilder.append(flag.toString());
    RobotLog.ii("ConfigWifiDirectActivity", stringBuilder.toString());
    int i = null.$SwitchMap$com$qualcomm$ftccommon$ConfigWifiDirectActivity$Flag[flag.ordinal()];
    if (i != 1) {
      if (i != 2)
        return; 
      (new Thread(new ToggleWifiRunnable())).start();
      return;
    } 
    (new Thread(new DisableWifiAndWarnBadDeviceName())).start();
  }
  
  private class DisableWifiAndWarnBadDeviceName implements Runnable {
    private DisableWifiAndWarnBadDeviceName() {}
    
    public void run() {
      RobotLog.ii("ConfigWifiDirectActivity", "attempting to disable Wifi due to bad wifi direct device name");
      ConfigWifiDirectActivity.this.showProgressDialog();
      try {
        FixWifiDirectSetup.disableWifiDirect(ConfigWifiDirectActivity.this.wifiManager);
      } catch (InterruptedException interruptedException) {
      
      } finally {
        ConfigWifiDirectActivity.this.dismissProgressDialog();
      } 
      ConfigWifiDirectActivity.this.dismissProgressDialog();
    }
  }
  
  class null implements Runnable {
    public void run() {
      ConfigWifiDirectActivity.this.textPleaseWait.setVisibility(4);
      ConfigWifiDirectActivity.this.textBadDeviceName.setVisibility(0);
    }
  }
  
  public enum Flag {
    NONE, WIFI_DIRECT_DEVICE_NAME_INVALID, WIFI_DIRECT_FIX_CONFIG;
    
    static {
      Flag flag = new Flag("WIFI_DIRECT_DEVICE_NAME_INVALID", 2);
      WIFI_DIRECT_DEVICE_NAME_INVALID = flag;
      $VALUES = new Flag[] { NONE, WIFI_DIRECT_FIX_CONFIG, flag };
    }
  }
  
  private class ToggleWifiRunnable implements Runnable {
    private ToggleWifiRunnable() {}
    
    public void run() {
      RobotLog.ii("ConfigWifiDirectActivity", "attempting to reconfigure Wifi Direct");
      ConfigWifiDirectActivity.this.showProgressDialog();
      try {
        FixWifiDirectSetup.fixWifiDirectSetup(ConfigWifiDirectActivity.this.wifiManager);
      } catch (InterruptedException interruptedException) {
      
      } finally {
        ConfigWifiDirectActivity.this.dismissProgressDialog();
      } 
      RobotLog.ii("ConfigWifiDirectActivity", "reconfigure Wifi Direct complete");
      ConfigWifiDirectActivity.this.runOnUiThread(new Runnable() {
            public void run() {
              ConfigWifiDirectActivity.this.finish();
            }
          });
    }
  }
  
  class null implements Runnable {
    public void run() {
      ConfigWifiDirectActivity.this.finish();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\ConfigWifiDirectActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */