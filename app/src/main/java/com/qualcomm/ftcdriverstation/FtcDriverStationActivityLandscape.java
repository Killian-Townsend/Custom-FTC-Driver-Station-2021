package com.qualcomm.ftcdriverstation;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qualcomm.robotcore.util.BatteryChecker;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.DriverStationAccessPointAssistant;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import com.qualcomm.robotcore.wifi.NetworkType;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;

public class FtcDriverStationActivityLandscape extends FtcDriverStationActivityBase implements DriverStationAccessPointAssistant.ConnectedNetworkHealthListener {
  View configAndTimerRegion;
  
  View dividerRcBatt12vBatt;
  
  ImageView headerColorLeft;
  
  LinearLayout headerColorRight;
  
  LinearLayout layoutPingChan;
  
  View matchLoggingContainer;
  
  TextView matchNumTxtView;
  
  ImageView networkSignalLevel;
  
  TextView network_ssid;
  
  PracticeTimerManager practiceTimerManager;
  
  View telemetryRegion;
  
  TextView textDbmLink;
  
  WiFiStatsView wiFiStatsView = WiFiStatsView.PING_CHAN;
  
  protected void assumeClientConnect(FtcDriverStationActivityBase.ControlPanelBack paramControlPanelBack) {
    RobotLog.vv("DriverStation", "Assuming client connected");
    setClientConnected(true);
    uiRobotControllerIsConnected(paramControlPanelBack);
  }
  
  protected void clearMatchNumber() {
    this.matchNumTxtView.setText("NONE");
  }
  
  protected void dimAndDisableAllControls() {
    super.dimAndDisableAllControls();
    setOpacity(this.configAndTimerRegion, 0.3F);
    setOpacity(this.telemetryRegion, 0.3F);
  }
  
  protected void disableMatchLoggingUI() {
    RobotLog.ii("DriverStation", "Hide match logging UI");
    this.matchLoggingContainer.setVisibility(8);
  }
  
  protected void displayRcBattery(boolean paramBoolean) {
    byte b;
    super.displayRcBattery(paramBoolean);
    View view = this.dividerRcBatt12vBatt;
    if (paramBoolean) {
      b = 0;
    } else {
      b = 8;
    } 
    view.setVisibility(b);
  }
  
  protected void doMatchNumFieldBehaviorInit() {
    this.matchLoggingContainer.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            (new ManualKeyInDialog(FtcDriverStationActivityLandscape.this.context, "Enter Match Number", new ManualKeyInDialog.Listener() {
                  public void onInput(String param2String) {
                    int i = FtcDriverStationActivityLandscape.this.validateMatchEntry(param2String);
                    if (i == -1) {
                      AppUtil.getInstance().showToast(UILocation.ONLY_LOCAL, FtcDriverStationActivityLandscape.this.getString(2131624244));
                      FtcDriverStationActivityLandscape.this.clearMatchNumber();
                      return;
                    } 
                    FtcDriverStationActivityLandscape.this.matchNumTxtView.setText(Integer.toString(i));
                    FtcDriverStationActivityLandscape.this.sendMatchNumber(i);
                  }
                })).show();
          }
        });
    if (!this.preferencesHelper.readBoolean(getString(2131624434), false))
      disableMatchLoggingUI(); 
  }
  
  protected void enableAndBrightenForConnected(FtcDriverStationActivityBase.ControlPanelBack paramControlPanelBack) {
    super.enableAndBrightenForConnected(paramControlPanelBack);
    setOpacity(this.configAndTimerRegion, 1.0F);
    setOpacity(this.telemetryRegion, 1.0F);
  }
  
  protected void enableMatchLoggingUI() {
    RobotLog.ii("DriverStation", "Show match logging UI");
    this.matchLoggingContainer.setVisibility(0);
  }
  
  protected int getMatchNumber() throws NumberFormatException {
    return Integer.parseInt(this.matchNumTxtView.getText().toString());
  }
  
  public View getPopupMenuAnchor() {
    return this.wifiInfo;
  }
  
  public int linkSpeedToWiFiSignal(int paramInt1, int paramInt2) {
    float f = paramInt1;
    return (f <= 6.0F) ? 0 : ((f >= 54.0F) ? paramInt2 : Math.round((f - 6.0F) / 48.0F / (paramInt2 - 1)));
  }
  
  public void onNetworkHealthUpdate(final int rssi, final int linkSpeed) {
    final int finalId = (int)Math.round((rssiToWiFiSignal(rssi, 5) + linkSpeedToWiFiSignal(linkSpeed, 5)) / 2.0D);
    if (i != 0) {
      if (i != 1) {
        if (i != 2) {
          if (i != 3) {
            if (i != 4) {
              if (i != 5) {
                i = 0;
              } else {
                i = 2131165323;
              } 
            } else {
              i = 2131165322;
            } 
          } else {
            i = 2131165321;
          } 
        } else {
          i = 2131165320;
        } 
      } else {
        i = 2131165319;
      } 
    } else {
      i = 2131165318;
    } 
    runOnUiThread(new Runnable() {
          public void run() {
            FtcDriverStationActivityLandscape.this.networkSignalLevel.setBackgroundResource(finalId);
            FtcDriverStationActivityLandscape.this.textDbmLink.setText(String.format("%ddBm Link %dMb", new Object[] { Integer.valueOf(this.val$rssi), Integer.valueOf(this.val$linkSpeed) }));
          }
        });
  }
  
  protected void onPause() {
    super.onPause();
    this.practiceTimerManager.reset();
    NetworkConnection networkConnection = this.networkConnectionHandler.getNetworkConnection();
    if (networkConnection.getNetworkType() == NetworkType.WIRELESSAP)
      ((DriverStationAccessPointAssistant)networkConnection).unregisterNetworkHealthListener(this); 
  }
  
  protected void onResume() {
    super.onResume();
    NetworkConnection networkConnection = this.networkConnectionHandler.getNetworkConnection();
    if (networkConnection.getNetworkType() == NetworkType.WIRELESSAP)
      ((DriverStationAccessPointAssistant)networkConnection).registerNetworkHealthListener(this); 
  }
  
  protected void pingStatus(String paramString) {
    TextView textView = this.textPingStatus;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Ping: ");
    stringBuilder.append(paramString);
    stringBuilder.append(" - ");
    setTextView(textView, stringBuilder.toString());
  }
  
  public int rssiToWiFiSignal(int paramInt1, int paramInt2) {
    float f = paramInt1;
    return (f <= -90.0F) ? 0 : ((f >= -55.0F) ? paramInt2 : Math.round((f + 90.0F) / 35.0F / (paramInt2 - 1)));
  }
  
  protected void showWifiStatus(final boolean showingRCName, final String status) {
    runOnUiThread(new Runnable() {
          public void run() {
            FtcDriverStationActivityLandscape.this.textWifiDirectStatusShowingRC = showingRCName;
            FtcDriverStationActivityLandscape.this.textWifiDirectStatus.setText(status);
            if (status.equals(FtcDriverStationActivityLandscape.this.getString(2131624680)) || status.equals(FtcDriverStationActivityLandscape.this.getString(2131623995)) || status.equals(FtcDriverStationActivityLandscape.this.getString(2131624683))) {
              FtcDriverStationActivityLandscape.this.headerColorRight.setBackground(FtcDriverStationActivityLandscape.this.getResources().getDrawable(2131165345));
              FtcDriverStationActivityLandscape.this.headerColorLeft.setBackgroundColor(FtcDriverStationActivityLandscape.this.getResources().getColor(2131034198));
              return;
            } 
            if (status.equals(FtcDriverStationActivityLandscape.this.getString(2131624679)) || status.equals(FtcDriverStationActivityLandscape.this.getString(2131624684))) {
              FtcDriverStationActivityLandscape.this.headerColorRight.setBackground(FtcDriverStationActivityLandscape.this.getResources().getDrawable(2131165344));
              FtcDriverStationActivityLandscape.this.headerColorLeft.setBackgroundColor(FtcDriverStationActivityLandscape.this.getResources().getColor(2131034200));
              return;
            } 
            FtcDriverStationActivityLandscape.this.textWifiDirectStatus.setText("Robot Connected");
            String str2 = status;
            String str1 = str2;
            if (str2.contains("DIRECT-")) {
              str1 = str2;
              if (status.contains("RC"))
                str1 = status.substring(10); 
            } 
            TextView textView = FtcDriverStationActivityLandscape.this.network_ssid;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Network: ");
            stringBuilder.append(str1);
            textView.setText(stringBuilder.toString());
            FtcDriverStationActivityLandscape.this.headerColorRight.setBackground(FtcDriverStationActivityLandscape.this.getResources().getDrawable(2131165343));
            FtcDriverStationActivityLandscape.this.headerColorLeft.setBackgroundColor(FtcDriverStationActivityLandscape.this.getResources().getColor(2131034196));
          }
        });
  }
  
  public void subclassOnCreate() {
    setContentView(2131427359);
    this.headerColorLeft = (ImageView)findViewById(2131230918);
    this.headerColorRight = (LinearLayout)findViewById(2131230919);
    this.configAndTimerRegion = findViewById(2131230850);
    this.telemetryRegion = findViewById(2131231110);
    this.practiceTimerManager = new PracticeTimerManager((Context)this, (ImageView)findViewById(2131231031), (TextView)findViewById(2131231032));
    this.matchLoggingContainer = findViewById(2131230984);
    this.matchNumTxtView = (TextView)findViewById(2131230987);
    this.networkSignalLevel = (ImageView)findViewById(2131230997);
    this.layoutPingChan = (LinearLayout)findViewById(2131230948);
    this.textDbmLink = (TextView)findViewById(2131231117);
    this.network_ssid = (TextView)findViewById(2131230999);
    this.dividerRcBatt12vBatt = findViewById(2131230882);
  }
  
  public void toggleWifiStatsView(View paramView) {
    if (this.wiFiStatsView == WiFiStatsView.PING_CHAN) {
      this.wiFiStatsView = WiFiStatsView.DBM_LINK;
      this.textDbmLink.setVisibility(0);
      this.layoutPingChan.setVisibility(8);
      return;
    } 
    if (this.wiFiStatsView == WiFiStatsView.DBM_LINK) {
      this.wiFiStatsView = WiFiStatsView.PING_CHAN;
      this.layoutPingChan.setVisibility(0);
      this.textDbmLink.setVisibility(8);
    } 
  }
  
  protected void uiRobotControllerIsConnected(FtcDriverStationActivityBase.ControlPanelBack paramControlPanelBack) {
    super.uiRobotControllerIsConnected(paramControlPanelBack);
    runOnUiThread(new Runnable() {
          public void run() {
            FtcDriverStationActivityLandscape.this.headerColorRight.setBackground(FtcDriverStationActivityLandscape.this.getResources().getDrawable(2131165343));
            FtcDriverStationActivityLandscape.this.headerColorLeft.setBackgroundColor(FtcDriverStationActivityLandscape.this.getResources().getColor(2131034196));
            FtcDriverStationActivityLandscape.this.textWifiDirectStatus.setText("Robot Connected");
          }
        });
  }
  
  protected void uiRobotControllerIsDisconnected() {
    super.uiRobotControllerIsDisconnected();
    runOnUiThread(new Runnable() {
          public void run() {
            FtcDriverStationActivityLandscape.this.headerColorRight.setBackground(FtcDriverStationActivityLandscape.this.getResources().getDrawable(2131165345));
            FtcDriverStationActivityLandscape.this.headerColorLeft.setBackgroundColor(FtcDriverStationActivityLandscape.this.getResources().getColor(2131034198));
            FtcDriverStationActivityLandscape.this.textWifiDirectStatus.setText("Disconnected");
          }
        });
  }
  
  public void updateBatteryStatus(BatteryChecker.BatteryStatus paramBatteryStatus) {
    TextView textView = this.dsBatteryInfo;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("DS: ");
    stringBuilder.append(Math.round(paramBatteryStatus.percent));
    stringBuilder.append("%");
    setTextView(textView, stringBuilder.toString());
    setBatteryIcon(paramBatteryStatus, this.dsBatteryIcon);
  }
  
  protected void updateRcBatteryStatus(BatteryChecker.BatteryStatus paramBatteryStatus) {
    TextView textView = this.rcBatteryTelemetry;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("RC: ");
    stringBuilder.append(Math.round(paramBatteryStatus.percent));
    stringBuilder.append("%");
    setTextView(textView, stringBuilder.toString());
    setBatteryIcon(paramBatteryStatus, this.rcBatteryIcon);
  }
  
  enum WiFiStatsView {
    DBM_LINK, PING_CHAN;
    
    static {
      WiFiStatsView wiFiStatsView = new WiFiStatsView("DBM_LINK", 1);
      DBM_LINK = wiFiStatsView;
      $VALUES = new WiFiStatsView[] { PING_CHAN, wiFiStatsView };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\FtcDriverStationActivityLandscape.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */