package com.qualcomm.ftcdriverstation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.Heartbeat;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.DriverStationAccessPointAssistant;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable;
import org.firstinspires.ftc.robotcore.internal.ui.BaseActivity;

public class FtcWirelessApNetworkConnectionActivity extends BaseActivity implements View.OnClickListener {
  private static final String TAG = "FtcWirelessApNetworkConnection";
  
  private Heartbeat heartbeat = new Heartbeat();
  
  private NetworkConnection networkConnection;
  
  private NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
  
  private RobotState robotState;
  
  private TextView textViewCurrentAp;
  
  private TextView textViewWirelessApStatus;
  
  protected FrameLayout getBackBar() {
    return (FrameLayout)findViewById(2131230804);
  }
  
  public String getTag() {
    return "FtcWirelessApNetworkConnection";
  }
  
  public void onClick(View paramView) {}
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131427370);
    this.networkConnection = (NetworkConnection)DriverStationAccessPointAssistant.getDriverStationAccessPointAssistant(getBaseContext());
    this.textViewCurrentAp = (TextView)findViewById(2131231138);
    ((Button)findViewById(2131230827)).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            FtcWirelessApNetworkConnectionActivity.this.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
          }
        });
  }
  
  public void onStart() {
    super.onStart();
    this.textViewCurrentAp.setText(this.networkConnection.getConnectionOwnerName());
    this.networkConnection.discoverPotentialConnections();
  }
  
  public void onStop() {
    super.onStop();
    this.networkConnection.cancelPotentialConnections();
  }
  
  protected void setTextView(final TextView textView, final String text) {
    runOnUiThread(new Runnable() {
          public void run() {
            textView.setText(text);
          }
        });
  }
  
  protected class RecvLoopCallback extends RecvLoopRunnable.DegenerateCallback {
    public CallbackResult heartbeatEvent(RobocolDatagram param1RobocolDatagram, long param1Long) {
      try {
        FtcWirelessApNetworkConnectionActivity.this.heartbeat.fromByteArray(param1RobocolDatagram.getData());
        FtcWirelessApNetworkConnectionActivity.access$102(FtcWirelessApNetworkConnectionActivity.this, RobotState.fromByte(FtcWirelessApNetworkConnectionActivity.this.heartbeat.getRobotState()));
        FtcWirelessApNetworkConnectionActivity.this.setTextView(FtcWirelessApNetworkConnectionActivity.this.textViewWirelessApStatus, FtcWirelessApNetworkConnectionActivity.this.robotState.toString());
      } catch (RobotCoreException robotCoreException) {
        RobotLog.logStackTrace((Throwable)robotCoreException);
      } 
      return CallbackResult.HANDLED;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\FtcWirelessApNetworkConnectionActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */