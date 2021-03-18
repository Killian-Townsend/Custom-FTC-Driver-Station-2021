package com.qualcomm.ftcdriverstation;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import com.qualcomm.robotcore.wifi.NetworkConnectionFactory;
import com.qualcomm.robotcore.wifi.NetworkType;
import com.qualcomm.robotcore.wifi.SoftApAssistant;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.PreferenceRemoterDS;
import org.firstinspires.ftc.robotcore.internal.ui.BaseActivity;

public class FtcPairNetworkConnectionActivity extends BaseActivity implements View.OnClickListener, NetworkConnection.NetworkConnectionCallback {
  public static final String TAG = "FtcPairNetworkConnection";
  
  private String connectionOwnerIdentity;
  
  private String connectionOwnerPassword;
  
  private ScheduledFuture<?> discoveryFuture;
  
  private EditText editTextSoftApPassword;
  
  private boolean filterForTeam = true;
  
  private NetworkConnection networkConnection;
  
  private SharedPreferences sharedPref;
  
  private int teamNum;
  
  private TextView textViewSoftApPasswordLabel;
  
  private int getTeamNumber(String paramString) {
    int i = paramString.indexOf('-');
    if (i == -1)
      return 0; 
    paramString = paramString.substring(0, i);
    try {
      return Integer.parseInt(paramString);
    } catch (NumberFormatException numberFormatException) {
      return 0;
    } 
  }
  
  private void updateDevicesList() {
    Map<String, String> map;
    RadioGroup radioGroup = (RadioGroup)findViewById(2131231036);
    radioGroup.clearCheck();
    radioGroup.removeAllViews();
    PeerRadioButton peerRadioButton = new PeerRadioButton((Context)this);
    String str = getString(2131624149);
    peerRadioButton.setId(0);
    peerRadioButton.setText("None\nDo not pair with any device");
    peerRadioButton.setPadding(0, 0, 0, 24);
    peerRadioButton.setOnClickListener(this);
    peerRadioButton.setDeviceIdentity(str);
    if (this.connectionOwnerIdentity.equalsIgnoreCase(str))
      peerRadioButton.setChecked(true); 
    radioGroup.addView((View)peerRadioButton);
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    if (this.networkConnection.getNetworkType() == NetworkType.WIFIDIRECT) {
      map = buildMap(((WifiDirectAssistant)this.networkConnection).getPeers());
    } else if (this.networkConnection.getNetworkType() == NetworkType.SOFTAP) {
      map = buildResultsMap(((SoftApAssistant)this.networkConnection).getScanResults());
    } 
    Iterator<String> iterator = map.keySet().iterator();
    for (int i = 1; iterator.hasNext(); i++) {
      str = iterator.next();
      if (this.filterForTeam) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.teamNum);
        stringBuilder.append("-");
        if (!str.contains(stringBuilder.toString()) && !str.startsWith("0000-"))
          continue; 
      } 
      String str1 = map.get(str);
      PeerRadioButton peerRadioButton1 = new PeerRadioButton((Context)this);
      peerRadioButton1.setId(i);
      if (this.networkConnection.getNetworkType() == NetworkType.WIFIDIRECT) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("\n");
        stringBuilder.append(str1);
        str = stringBuilder.toString();
      } else if (this.networkConnection.getNetworkType() != NetworkType.SOFTAP) {
        str = "";
      } 
      peerRadioButton1.setText(str);
      peerRadioButton1.setPadding(0, 0, 0, 24);
      peerRadioButton1.setDeviceIdentity(str1);
      if (str1.equalsIgnoreCase(this.connectionOwnerIdentity))
        peerRadioButton1.setChecked(true); 
      peerRadioButton1.setOnClickListener(this);
      radioGroup.addView((View)peerRadioButton1);
    } 
  }
  
  public Map<String, String> buildMap(List<WifiP2pDevice> paramList) {
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    for (WifiP2pDevice wifiP2pDevice : paramList)
      treeMap.put(PreferenceRemoterDS.getInstance().getDeviceNameForWifiP2pGroupOwner(wifiP2pDevice.deviceName), wifiP2pDevice.deviceAddress); 
    return (Map)treeMap;
  }
  
  public Map<String, String> buildResultsMap(List<ScanResult> paramList) {
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    for (ScanResult scanResult : paramList)
      treeMap.put(scanResult.SSID, scanResult.SSID); 
    return (Map)treeMap;
  }
  
  protected FrameLayout getBackBar() {
    return (FrameLayout)findViewById(2131230804);
  }
  
  public String getTag() {
    return "FtcPairNetworkConnection";
  }
  
  public void onClick(View paramView) {
    if (paramView instanceof PeerRadioButton) {
      PeerRadioButton peerRadioButton = (PeerRadioButton)paramView;
      if (peerRadioButton.getId() == 0) {
        this.connectionOwnerIdentity = getString(2131624149);
        this.connectionOwnerPassword = getString(2131624150);
      } else {
        this.connectionOwnerIdentity = peerRadioButton.getDeviceIdentity();
      } 
      SharedPreferences.Editor editor = this.sharedPref.edit();
      editor.putString(getString(2131624404), this.connectionOwnerIdentity);
      editor.apply();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Setting Driver Station name to ");
      stringBuilder.append(this.connectionOwnerIdentity);
      RobotLog.ii("FtcPairNetworkConnection", stringBuilder.toString());
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131427367);
    NetworkType networkType = NetworkType.fromString(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("NETWORK_CONNECTION_TYPE", NetworkType.globalDefaultAsString()));
    this.editTextSoftApPassword = (EditText)findViewById(2131230887);
    this.textViewSoftApPasswordLabel = (TextView)findViewById(2131231143);
    NetworkConnection networkConnection = NetworkConnectionFactory.getNetworkConnection(networkType, getBaseContext());
    this.networkConnection = networkConnection;
    String str = networkConnection.getDeviceName();
    if (str != "") {
      this.teamNum = getTeamNumber(str);
    } else {
      this.teamNum = 0;
      str = getString(2131624687);
    } 
    TextView textView1 = (TextView)findViewById(2131231148);
    TextView textView2 = (TextView)findViewById(2131231144);
    TextView textView3 = (TextView)findViewById(2131231145);
    if (networkType == NetworkType.WIFIDIRECT) {
      textView1.setText(getString(2131624372));
      textView2.setVisibility(0);
      textView2.setText(str);
      textView3.setVisibility(0);
    } else if (networkType == NetworkType.SOFTAP) {
      textView1.setText(getString(2131624571));
      textView2.setVisibility(4);
      textView3.setVisibility(4);
    } 
    ((Switch)findViewById(2131231191)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            if (param1Boolean) {
              FtcPairNetworkConnectionActivity.access$002(FtcPairNetworkConnectionActivity.this, true);
            } else {
              FtcPairNetworkConnectionActivity.access$002(FtcPairNetworkConnectionActivity.this, false);
            } 
            FtcPairNetworkConnectionActivity.this.updateDevicesList();
          }
        });
  }
  
  public CallbackResult onNetworkConnectionEvent(NetworkConnection.NetworkEvent paramNetworkEvent) {
    CallbackResult callbackResult = CallbackResult.NOT_HANDLED;
    if (null.$SwitchMap$com$qualcomm$robotcore$wifi$NetworkConnection$NetworkEvent[paramNetworkEvent.ordinal()] != 1)
      return callbackResult; 
    updateDevicesList();
    return CallbackResult.HANDLED;
  }
  
  public void onStart() {
    super.onStart();
    RobotLog.ii("FtcPairNetworkConnection", "Starting Pairing with Driver Station activity");
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)this);
    this.sharedPref = sharedPreferences;
    this.connectionOwnerIdentity = sharedPreferences.getString(getString(2131624404), getString(2131624149));
    TextView textView = (TextView)findViewById(2131231142);
    if (this.networkConnection.getNetworkType() == NetworkType.SOFTAP) {
      this.connectionOwnerPassword = this.sharedPref.getString(getString(2131624405), getString(2131624150));
      this.textViewSoftApPasswordLabel.setVisibility(0);
      this.editTextSoftApPassword.setVisibility(0);
      this.editTextSoftApPassword.setText(this.connectionOwnerPassword);
      textView.setVisibility(0);
    } else {
      this.textViewSoftApPasswordLabel.setVisibility(4);
      this.editTextSoftApPassword.setVisibility(4);
      textView.setVisibility(4);
    } 
    this.networkConnection.enable();
    this.networkConnection.setCallback(this);
    updateDevicesList();
    this.discoveryFuture = ThreadPool.getDefaultScheduler().scheduleAtFixedRate(new Runnable() {
          public void run() {
            FtcPairNetworkConnectionActivity.this.networkConnection.discoverPotentialConnections();
          }
        },  0L, 10000L, TimeUnit.MILLISECONDS);
  }
  
  public void onStop() {
    super.onStop();
    this.discoveryFuture.cancel(false);
    this.networkConnection.cancelPotentialConnections();
    this.networkConnection.disable();
    this.connectionOwnerPassword = this.editTextSoftApPassword.getText().toString();
    SharedPreferences.Editor editor = this.sharedPref.edit();
    editor.putString(getString(2131624405), this.connectionOwnerPassword);
    editor.apply();
  }
  
  public static class PeerRadioButton extends RadioButton {
    private String deviceIdentity = "";
    
    public PeerRadioButton(Context param1Context) {
      super(param1Context);
    }
    
    public String getDeviceIdentity() {
      return this.deviceIdentity;
    }
    
    public void setDeviceIdentity(String param1String) {
      this.deviceIdentity = param1String;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\FtcPairNetworkConnectionActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */