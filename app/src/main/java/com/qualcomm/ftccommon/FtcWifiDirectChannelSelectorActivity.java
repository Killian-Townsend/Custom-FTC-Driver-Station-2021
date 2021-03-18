package com.qualcomm.ftccommon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Arrays;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectChannelAndDescription;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectChannelChanger;
import org.firstinspires.ftc.robotcore.internal.network.WifiUtil;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;
import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;
import org.firstinspires.inspection.R;

public class FtcWifiDirectChannelSelectorActivity extends ThemedActivity implements AdapterView.OnItemClickListener {
  public static final String TAG = "FtcWifiDirectChannelSelectorActivity";
  
  private final int INDEX_AUTO_AND_2_4_ITEMS = 12;
  
  private WifiDirectChannelChanger configurer = null;
  
  private PreferencesHelper preferencesHelper = new PreferencesHelper("FtcWifiDirectChannelSelectorActivity");
  
  private boolean remoteConfigure = AppUtil.getInstance().isDriverStation();
  
  protected ArrayAdapter<WifiDirectChannelAndDescription> getAdapter(AdapterView<?> paramAdapterView) {
    return (ArrayAdapter<WifiDirectChannelAndDescription>)paramAdapterView.getAdapter();
  }
  
  protected FrameLayout getBackBar() {
    return (FrameLayout)findViewById(R.id.backbar);
  }
  
  public String getTag() {
    return "FtcWifiDirectChannelSelectorActivity";
  }
  
  protected void loadAdapter(ListView paramListView) {
    WifiDirectChannelAndDescription[] arrayOfWifiDirectChannelAndDescription = (WifiDirectChannelAndDescription[])WifiDirectChannelAndDescription.load().toArray((Object[])new WifiDirectChannelAndDescription[0]);
    Arrays.sort((Object[])arrayOfWifiDirectChannelAndDescription);
    if (!WifiUtil.is5GHzAvailable()) {
      arrayOfWifiDirectChannelAndDescription = Arrays.<WifiDirectChannelAndDescription>copyOf(arrayOfWifiDirectChannelAndDescription, 12);
      RobotLog.vv("FtcWifiDirectChannelSelectorActivity", "5GHz radio not available.");
    } else {
      RobotLog.vv("FtcWifiDirectChannelSelectorActivity", "5GHz radio is available.");
    } 
    paramListView.setAdapter((ListAdapter)new WifiChannelItemAdapter((Context)this, 17367049, arrayOfWifiDirectChannelAndDescription));
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_ftc_wifi_channel_selector);
    ListView listView = (ListView)findViewById(R.id.channelPickList);
    loadAdapter(listView);
    listView.setOnItemClickListener(this);
    listView.setChoiceMode(1);
    if (!this.remoteConfigure)
      this.configurer = new WifiDirectChannelChanger(); 
  }
  
  protected void onDestroy() {
    super.onDestroy();
  }
  
  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
    WifiDirectChannelChanger wifiDirectChannelChanger = this.configurer;
    if (wifiDirectChannelChanger == null || !wifiDirectChannelChanger.isBusy()) {
      WifiDirectChannelAndDescription wifiDirectChannelAndDescription = (WifiDirectChannelAndDescription)getAdapter(paramAdapterView).getItem(paramInt);
      ((CheckedTextView)paramView).setChecked(true);
      if (this.remoteConfigure) {
        if (this.preferencesHelper.writePrefIfDifferent(getString(R.string.pref_wifip2p_channel), Integer.valueOf(wifiDirectChannelAndDescription.getChannel()))) {
          AppUtil.getInstance().showToast(UILocation.ONLY_LOCAL, getString(R.string.toastWifiP2pChannelChangeRequestedDS, new Object[] { wifiDirectChannelAndDescription.getDescription() }));
          return;
        } 
      } else {
        this.configurer.changeToChannel(wifiDirectChannelAndDescription.getChannel());
      } 
    } 
  }
  
  protected void onStart() {
    super.onStart();
    int j = this.preferencesHelper.readInt(getString(R.string.pref_wifip2p_channel), -1);
    if (j == -1) {
      RobotLog.vv("FtcWifiDirectChannelSelectorActivity", "pref_wifip2p_channel: No preferred channel defined. Will use a default value of %d", new Object[] { Integer.valueOf(0) });
      j = 0;
    } else {
      RobotLog.vv("FtcWifiDirectChannelSelectorActivity", "pref_wifip2p_channel: Found existing preferred channel (%d).", new Object[] { Integer.valueOf(j) });
    } 
    ListView listView = (ListView)findViewById(R.id.channelPickList);
    ArrayAdapter<WifiDirectChannelAndDescription> arrayAdapter = getAdapter((AdapterView<?>)listView);
    for (int i = 0; i < arrayAdapter.getCount(); i = k + 1) {
      WifiDirectChannelAndDescription wifiDirectChannelAndDescription = (WifiDirectChannelAndDescription)arrayAdapter.getItem(i);
      int k = i;
      if (j == wifiDirectChannelAndDescription.getChannel()) {
        listView.setItemChecked(i, true);
        RobotLog.vv("FtcWifiDirectChannelSelectorActivity", "preferred channel matches ListView index %d (%d).", new Object[] { Integer.valueOf(i), Integer.valueOf(wifiDirectChannelAndDescription.getChannel()) });
        k = arrayAdapter.getCount();
      } 
    } 
  }
  
  public void onWifiSettingsClicked(View paramView) {
    RobotLog.vv("FtcWifiDirectChannelSelectorActivity", "launch wifi settings");
    startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
  }
  
  protected class WifiChannelItemAdapter extends ArrayAdapter<WifiDirectChannelAndDescription> {
    int checkmark;
    
    public WifiChannelItemAdapter(Context param1Context, int param1Int, WifiDirectChannelAndDescription[] param1ArrayOfWifiDirectChannelAndDescription) {
      super(param1Context, param1Int, (Object[])param1ArrayOfWifiDirectChannelAndDescription);
      TypedValue typedValue = new TypedValue();
      FtcWifiDirectChannelSelectorActivity.this.getTheme().resolveAttribute(16843289, typedValue, true);
      this.checkmark = typedValue.resourceId;
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      param1View = super.getView(param1Int, param1View, param1ViewGroup);
      ((CheckedTextView)param1View).setCheckMarkDrawable(this.checkmark);
      return param1View;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\FtcWifiDirectChannelSelectorActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */