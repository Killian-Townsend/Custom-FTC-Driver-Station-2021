package com.qualcomm.ftccommon;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.robot.RobotStatus;
import com.qualcomm.robotcore.util.Dimmer;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import org.firstinspires.ftc.ftccommon.external.RobotStateMonitor;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameListener;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.NetworkStatus;
import org.firstinspires.ftc.robotcore.internal.network.PeerStatus;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class UpdateUI {
  public static final boolean DEBUG = false;
  
  private static final int NUM_GAMEPADS = 2;
  
  private static final String TAG = "UpdateUI";
  
  Activity activity;
  
  FtcRobotControllerService controllerService;
  
  Dimmer dimmer;
  
  protected NetworkStatus networkStatus = NetworkStatus.UNKNOWN;
  
  protected String networkStatusExtra = null;
  
  protected String networkStatusMessage = null;
  
  protected PeerStatus peerStatus = PeerStatus.DISCONNECTED;
  
  Restarter restarter;
  
  protected RobotState robotState = RobotState.NOT_STARTED;
  
  protected RobotStatus robotStatus = RobotStatus.NONE;
  
  protected String stateStatusMessage = null;
  
  protected TextView textDeviceName;
  
  protected TextView textErrorMessage;
  
  protected int textErrorMessageOriginalColor;
  
  protected TextView[] textGamepad = new TextView[2];
  
  protected TextView textNetworkConnectionStatus;
  
  protected TextView textOpMode;
  
  protected TextView textRobotStatus;
  
  public UpdateUI(Activity paramActivity, Dimmer paramDimmer) {
    this.activity = paramActivity;
    this.dimmer = paramDimmer;
  }
  
  private void requestRobotRestart() {
    this.restarter.requestRestart();
  }
  
  public void setControllerService(FtcRobotControllerService paramFtcRobotControllerService) {
    this.controllerService = paramFtcRobotControllerService;
  }
  
  public void setRestarter(Restarter paramRestarter) {
    this.restarter = paramRestarter;
  }
  
  protected void setText(TextView paramTextView, String paramString) {
    if (paramTextView != null && paramString != null) {
      paramString = paramString.trim();
      if (paramString.length() > 0) {
        paramTextView.setText(paramString);
        paramTextView.setVisibility(0);
        return;
      } 
      paramTextView.setVisibility(4);
      paramTextView.setText(" ");
    } 
  }
  
  public void setTextViews(TextView paramTextView1, TextView paramTextView2, TextView[] paramArrayOfTextView, TextView paramTextView3, TextView paramTextView4, TextView paramTextView5) {
    this.textNetworkConnectionStatus = paramTextView1;
    this.textRobotStatus = paramTextView2;
    this.textGamepad = paramArrayOfTextView;
    this.textOpMode = paramTextView3;
    this.textErrorMessage = paramTextView4;
    this.textErrorMessageOriginalColor = paramTextView4.getCurrentTextColor();
    this.textDeviceName = paramTextView5;
  }
  
  public class Callback {
    DeviceNameManagerCallback deviceNameManagerCallback = new DeviceNameManagerCallback();
    
    RobotStateMonitor stateMonitor = null;
    
    public Callback() {
      DeviceNameManagerFactory.getInstance().registerCallback(this.deviceNameManagerCallback);
    }
    
    public void close() {
      DeviceNameManagerFactory.getInstance().unregisterCallback(this.deviceNameManagerCallback);
    }
    
    protected void displayDeviceName(final String name) {
      UpdateUI.this.activity.runOnUiThread(new Runnable() {
            public void run() {
              UpdateUI.this.textDeviceName.setText(name);
            }
          });
    }
    
    public RobotStateMonitor getStateMonitor() {
      return this.stateMonitor;
    }
    
    public void networkConnectionUpdate(NetworkConnection.NetworkEvent param1NetworkEvent) {
      NetworkConnection networkConnection;
      switch (param1NetworkEvent) {
        default:
          return;
        case null:
          networkConnection = UpdateUI.this.controllerService.getNetworkConnection();
          updateNetworkConnectionStatus(NetworkStatus.CREATED_AP_CONNECTION, networkConnection.getConnectionOwnerName());
          return;
        case null:
          updateNetworkConnectionStatus(NetworkStatus.ACTIVE);
          return;
        case null:
          updateNetworkConnectionStatus(NetworkStatus.ERROR);
          return;
        case null:
          updateNetworkConnectionStatus(NetworkStatus.ENABLED);
          return;
        case null:
          updateNetworkConnectionStatus(NetworkStatus.INACTIVE);
          return;
        case null:
          break;
      } 
      updateNetworkConnectionStatus(NetworkStatus.UNKNOWN);
    }
    
    public void refreshErrorTextOnUiThread() {
      UpdateUI.this.activity.runOnUiThread(new Runnable() {
            public void run() {
              UpdateUI.Callback.this.refreshTextErrorMessage();
            }
          });
    }
    
    void refreshNetworkStatus() {
      String str2 = UpdateUI.this.activity.getString(R.string.networkStatusFormat);
      String str3 = UpdateUI.this.networkStatus.toString((Context)UpdateUI.this.activity, new Object[] { this.this$0.networkStatusExtra });
      if (UpdateUI.this.peerStatus == PeerStatus.UNKNOWN) {
        str1 = "";
      } else {
        str1 = String.format(", %s", new Object[] { this.this$0.peerStatus.toString((Context)this.this$0.activity) });
      } 
      final String message = String.format(str2, new Object[] { str3, str1 });
      if (!str1.equals(UpdateUI.this.networkStatusMessage))
        RobotLog.vv("UpdateUI", str1); 
      UpdateUI.this.networkStatusMessage = str1;
      UpdateUI.this.activity.runOnUiThread(new Runnable() {
            public void run() {
              UpdateUI.this.setText(UpdateUI.this.textNetworkConnectionStatus, message);
            }
          });
    }
    
    protected void refreshStateStatus() {
      String str2 = UpdateUI.this.activity.getString(R.string.robotStatusFormat);
      String str3 = UpdateUI.this.robotState.toString((Context)UpdateUI.this.activity);
      if (UpdateUI.this.robotStatus == RobotStatus.NONE) {
        str1 = "";
      } else {
        str1 = String.format(", %s", new Object[] { this.this$0.robotStatus.toString((Context)this.this$0.activity) });
      } 
      final String message = String.format(str2, new Object[] { str3, str1 });
      if (!str1.equals(UpdateUI.this.stateStatusMessage))
        RobotLog.v(str1); 
      UpdateUI.this.stateStatusMessage = str1;
      UpdateUI.this.activity.runOnUiThread(new Runnable() {
            public void run() {
              UpdateUI.this.setText(UpdateUI.this.textRobotStatus, message);
              UpdateUI.Callback.this.refreshTextErrorMessage();
            }
          });
    }
    
    void refreshTextErrorMessage() {
      String str1 = RobotLog.getGlobalErrorMsg();
      String str2 = RobotLog.getGlobalWarningMessage();
      if (!str1.isEmpty() || !str2.isEmpty()) {
        RobotStateMonitor robotStateMonitor1;
        if (!str1.isEmpty()) {
          str1 = UpdateUI.this.activity.getString(R.string.error_text_error, new Object[] { trimTextErrorMessage(str1) });
          UpdateUI updateUI1 = UpdateUI.this;
          updateUI1.setText(updateUI1.textErrorMessage, str1);
          TextView textView = UpdateUI.this.textErrorMessage;
          AppUtil.getInstance();
          textView.setTextColor(AppUtil.getColor(R.color.text_error));
          robotStateMonitor1 = this.stateMonitor;
          if (robotStateMonitor1 != null)
            robotStateMonitor1.updateErrorMessage(str1); 
        } else {
          str1 = UpdateUI.this.activity.getString(R.string.error_text_warning, new Object[] { trimTextErrorMessage((String)robotStateMonitor1) });
          UpdateUI updateUI1 = UpdateUI.this;
          updateUI1.setText(updateUI1.textErrorMessage, str1);
          TextView textView = UpdateUI.this.textErrorMessage;
          AppUtil.getInstance();
          textView.setTextColor(AppUtil.getColor(R.color.text_warning));
          RobotStateMonitor robotStateMonitor2 = this.stateMonitor;
          if (robotStateMonitor2 != null)
            robotStateMonitor2.updateWarningMessage(str1); 
        } 
        UpdateUI.this.dimmer.longBright();
        return;
      } 
      UpdateUI updateUI = UpdateUI.this;
      updateUI.setText(updateUI.textErrorMessage, "");
      UpdateUI.this.textErrorMessage.setTextColor(UpdateUI.this.textErrorMessageOriginalColor);
      RobotStateMonitor robotStateMonitor = this.stateMonitor;
      if (robotStateMonitor != null) {
        robotStateMonitor.updateErrorMessage(null);
        this.stateMonitor.updateWarningMessage(null);
        return;
      } 
    }
    
    public void restartRobot() {
      ThreadPool.getDefault().submit(new Runnable() {
            public void run() {
              AppUtil.getInstance().runOnUiThread(new Runnable() {
                    public void run() {
                      UpdateUI.this.requestRobotRestart();
                    }
                  });
            }
          });
    }
    
    public void setStateMonitor(RobotStateMonitor param1RobotStateMonitor) {
      this.stateMonitor = param1RobotStateMonitor;
    }
    
    String trimTextErrorMessage(String param1String) {
      return param1String;
    }
    
    public void updateNetworkConnectionStatus(NetworkStatus param1NetworkStatus) {
      if (UpdateUI.this.networkStatus != param1NetworkStatus) {
        UpdateUI.this.networkStatus = param1NetworkStatus;
        UpdateUI.this.networkStatusExtra = null;
        RobotStateMonitor robotStateMonitor = this.stateMonitor;
        if (robotStateMonitor != null)
          robotStateMonitor.updateNetworkStatus(param1NetworkStatus, null); 
        refreshNetworkStatus();
      } 
    }
    
    public void updateNetworkConnectionStatus(NetworkStatus param1NetworkStatus, String param1String) {
      if (UpdateUI.this.networkStatus != param1NetworkStatus || !param1String.equals(UpdateUI.this.networkStatusExtra)) {
        UpdateUI.this.networkStatus = param1NetworkStatus;
        UpdateUI.this.networkStatusExtra = param1String;
        RobotStateMonitor robotStateMonitor = this.stateMonitor;
        if (robotStateMonitor != null)
          robotStateMonitor.updateNetworkStatus(param1NetworkStatus, param1String); 
        refreshNetworkStatus();
      } 
    }
    
    public void updatePeerStatus(PeerStatus param1PeerStatus) {
      if (UpdateUI.this.peerStatus != param1PeerStatus) {
        UpdateUI.this.peerStatus = param1PeerStatus;
        RobotStateMonitor robotStateMonitor = this.stateMonitor;
        if (robotStateMonitor != null)
          robotStateMonitor.updatePeerStatus(param1PeerStatus); 
        refreshNetworkStatus();
      } 
    }
    
    public void updateRobotState(RobotState param1RobotState) {
      UpdateUI.this.robotState = param1RobotState;
      RobotStateMonitor robotStateMonitor = this.stateMonitor;
      if (robotStateMonitor != null)
        robotStateMonitor.updateRobotState(UpdateUI.this.robotState); 
      refreshStateStatus();
    }
    
    public void updateRobotStatus(RobotStatus param1RobotStatus) {
      UpdateUI.this.robotStatus = param1RobotStatus;
      RobotStateMonitor robotStateMonitor = this.stateMonitor;
      if (robotStateMonitor != null)
        robotStateMonitor.updateRobotStatus(UpdateUI.this.robotStatus); 
      refreshStateStatus();
    }
    
    public void updateUi(final String opModeName, final Gamepad[] gamepads) {
      UpdateUI.this.activity.runOnUiThread(new Runnable() {
            public void run() {
              String str;
              if (UpdateUI.this.textGamepad != null) {
                int i = 0;
                while (i < UpdateUI.this.textGamepad.length) {
                  Gamepad[] arrayOfGamepad = gamepads;
                  if (i < arrayOfGamepad.length) {
                    if (arrayOfGamepad[i].getGamepadId() == -1) {
                      UpdateUI.this.setText(UpdateUI.this.textGamepad[i], "");
                    } else {
                      UpdateUI.this.setText(UpdateUI.this.textGamepad[i], gamepads[i].toString());
                    } 
                    i++;
                  } 
                } 
              } 
              if (opModeName.equals("$Stop$Robot$")) {
                str = UpdateUI.this.activity.getString(R.string.defaultOpModeName);
              } else {
                str = opModeName;
              } 
              UpdateUI updateUI = UpdateUI.this;
              TextView textView = UpdateUI.this.textOpMode;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Op Mode: ");
              stringBuilder.append(str);
              updateUI.setText(textView, stringBuilder.toString());
              UpdateUI.Callback.this.refreshTextErrorMessage();
            }
          });
    }
    
    protected class DeviceNameManagerCallback implements DeviceNameListener {
      public void onDeviceNameChanged(String param2String) {
        UpdateUI.Callback.this.displayDeviceName(param2String);
      }
    }
  }
  
  class null implements Runnable {
    public void run() {
      AppUtil.getInstance().runOnUiThread(new Runnable() {
            public void run() {
              UpdateUI.this.requestRobotRestart();
            }
          });
    }
  }
  
  class null implements Runnable {
    public void run() {
      UpdateUI.this.requestRobotRestart();
    }
  }
  
  class null implements Runnable {
    public void run() {
      String str;
      if (UpdateUI.this.textGamepad != null) {
        int i = 0;
        while (i < UpdateUI.this.textGamepad.length) {
          Gamepad[] arrayOfGamepad = gamepads;
          if (i < arrayOfGamepad.length) {
            if (arrayOfGamepad[i].getGamepadId() == -1) {
              UpdateUI.this.setText(UpdateUI.this.textGamepad[i], "");
            } else {
              UpdateUI.this.setText(UpdateUI.this.textGamepad[i], gamepads[i].toString());
            } 
            i++;
          } 
        } 
      } 
      if (opModeName.equals("$Stop$Robot$")) {
        str = UpdateUI.this.activity.getString(R.string.defaultOpModeName);
      } else {
        str = opModeName;
      } 
      UpdateUI updateUI = UpdateUI.this;
      TextView textView = UpdateUI.this.textOpMode;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Op Mode: ");
      stringBuilder.append(str);
      updateUI.setText(textView, stringBuilder.toString());
      this.this$1.refreshTextErrorMessage();
    }
  }
  
  class null implements Runnable {
    public void run() {
      UpdateUI.this.textDeviceName.setText(name);
    }
  }
  
  class null implements Runnable {
    public void run() {
      UpdateUI.this.setText(UpdateUI.this.textNetworkConnectionStatus, message);
    }
  }
  
  class null implements Runnable {
    public void run() {
      UpdateUI.this.setText(UpdateUI.this.textRobotStatus, message);
      this.this$1.refreshTextErrorMessage();
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$1.refreshTextErrorMessage();
    }
  }
  
  protected class DeviceNameManagerCallback implements DeviceNameListener {
    public void onDeviceNameChanged(String param1String) {
      this.this$1.displayDeviceName(param1String);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\UpdateUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */