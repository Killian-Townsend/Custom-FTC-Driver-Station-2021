package com.qualcomm.ftcdriverstation;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.qualcomm.robotcore.util.BatteryChecker;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;

public class FtcDriverStationActivityPortrait extends FtcDriverStationActivityBase {
  protected EditText matchNumField;
  
  protected void clearMatchNumber() {
    this.matchNumField.setText("");
  }
  
  protected void disableMatchLoggingUI() {
    RobotLog.ii("DriverStation", "Hide match logging UI");
    this.matchNumField.setVisibility(4);
    this.matchNumField.setEnabled(false);
    findViewById(2131230986).setVisibility(4);
  }
  
  protected void doMatchNumFieldBehaviorInit() {
    this.matchNumField.setText("");
    this.matchNumField.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            FtcDriverStationActivityPortrait.this.matchNumField.setText("");
          }
        });
    this.matchNumField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
          public boolean onEditorAction(TextView param1TextView, int param1Int, KeyEvent param1KeyEvent) {
            if (param1Int == 6) {
              param1Int = FtcDriverStationActivityPortrait.this.validateMatchEntry(param1TextView.getText().toString());
              if (param1Int == -1) {
                AppUtil.getInstance().showToast(UILocation.ONLY_LOCAL, FtcDriverStationActivityPortrait.this.getString(2131624244));
                FtcDriverStationActivityPortrait.this.matchNumField.setText("");
              } else {
                FtcDriverStationActivityPortrait.this.sendMatchNumber(param1Int);
              } 
            } 
            return false;
          }
        });
    findViewById(2131230818).requestFocus();
    if (!this.preferencesHelper.readBoolean(getString(2131624434), false))
      disableMatchLoggingUI(); 
  }
  
  protected void enableMatchLoggingUI() {
    RobotLog.ii("DriverStation", "Show match logging UI");
    this.matchNumField.setVisibility(0);
    this.matchNumField.setEnabled(true);
    findViewById(2131230986).setVisibility(0);
  }
  
  protected int getMatchNumber() {
    return Integer.parseInt(this.matchNumField.getText().toString());
  }
  
  public View getPopupMenuAnchor() {
    return (View)this.buttonMenu;
  }
  
  protected void pingStatus(String paramString) {
    setTextView(this.textPingStatus, paramString);
  }
  
  public void subclassOnCreate() {
    setContentView(2131427364);
    this.matchNumField = (EditText)findViewById(2131230987);
  }
  
  public void updateBatteryStatus(BatteryChecker.BatteryStatus paramBatteryStatus) {
    TextView textView = this.dsBatteryInfo;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Double.toString(paramBatteryStatus.percent));
    stringBuilder.append("%");
    setTextView(textView, stringBuilder.toString());
    setBatteryIcon(paramBatteryStatus, this.dsBatteryIcon);
  }
  
  protected void updateRcBatteryStatus(BatteryChecker.BatteryStatus paramBatteryStatus) {
    TextView textView = this.rcBatteryTelemetry;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Double.toString(paramBatteryStatus.percent));
    stringBuilder.append("%");
    setTextView(textView, stringBuilder.toString());
    setBatteryIcon(paramBatteryStatus, this.rcBatteryIcon);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\FtcDriverStationActivityPortrait.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */