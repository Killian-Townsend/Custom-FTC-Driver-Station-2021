package com.qualcomm.ftccommon;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectAgent;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectGroupName;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectPersistentGroupManager;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;
import org.firstinspires.inspection.R;

public class FtcWifiDirectRememberedGroupsActivity extends ThemedActivity {
  public static final String TAG = "FtcWifiDirectRememberedGroupsActivity";
  
  private final NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
  
  private WifiDirectPersistentGroupManager persistentGroupManager;
  
  private final RecvLoopCallback recvLoopCallback = new RecvLoopCallback();
  
  private final boolean remoteConfigure = AppUtil.getInstance().isDriverStation();
  
  private Future requestGroupsFuture = null;
  
  private final Object requestGroupsFutureLock = new Object();
  
  protected FrameLayout getBackBar() {
    return (FrameLayout)findViewById(R.id.backbar);
  }
  
  protected List<WifiDirectGroupName> getLocalGroupList() {
    return WifiDirectGroupName.namesFromGroups(this.persistentGroupManager.getPersistentGroups());
  }
  
  public String getTag() {
    return "FtcWifiDirectRememberedGroupsActivity";
  }
  
  protected CallbackResult handleCommandRequestRememberedGroupsResp(String paramString) throws RobotCoreException {
    RobotLog.vv("FtcWifiDirectRememberedGroupsActivity", "handleCommandRequestRememberedGroupsResp()");
    loadGroupList(WifiDirectGroupName.deserializeNames(paramString));
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleRememberedGroupsChanged() {
    synchronized (this.requestGroupsFutureLock) {
      if (this.requestGroupsFuture != null) {
        this.requestGroupsFuture.cancel(false);
        this.requestGroupsFuture = null;
      } 
      this.requestGroupsFuture = ThreadPool.getDefaultScheduler().schedule(new Callable() {
            public Object call() throws Exception {
              synchronized (FtcWifiDirectRememberedGroupsActivity.this.requestGroupsFutureLock) {
                FtcWifiDirectRememberedGroupsActivity.this.requestRememberedGroups();
                FtcWifiDirectRememberedGroupsActivity.access$102(FtcWifiDirectRememberedGroupsActivity.this, (Future)null);
                return null;
              } 
            }
          }250L, TimeUnit.MILLISECONDS);
      return CallbackResult.HANDLED_CONTINUE;
    } 
  }
  
  protected void loadGroupList(final List<WifiDirectGroupName> names) {
    AppUtil.getInstance().runOnUiThread(new Runnable() {
          public void run() {
            ListView listView = (ListView)FtcWifiDirectRememberedGroupsActivity.this.findViewById(R.id.groupList);
            Collections.sort(names);
            if (names.isEmpty())
              names.add(new WifiDirectGroupName(FtcWifiDirectRememberedGroupsActivity.this.getString(R.string.noRememberedGroupsFound))); 
            listView.setAdapter((ListAdapter)new FtcWifiDirectRememberedGroupsActivity.WifiP2pGroupItemAdapter((Context)AppUtil.getInstance().getActivity(), 17367049, names));
          }
        });
  }
  
  protected void loadLocalGroups() {
    loadGroupList(getLocalGroupList());
  }
  
  public void onClearRememberedGroupsClicked(View paramView) {
    RobotLog.vv("FtcWifiDirectRememberedGroupsActivity", "onClearRememberedGroupsClicked()");
    if (!this.remoteConfigure) {
      this.persistentGroupManager.deleteAllPersistentGroups();
      AppUtil.getInstance().showToast(UILocation.BOTH, getString(R.string.toastWifiP2pRememberedGroupsCleared));
      loadLocalGroups();
      return;
    } 
    this.networkConnectionHandler.sendCommand(new Command("CMD_CLEAR_REMEMBERED_GROUPS"));
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_ftc_wifi_remembered_groups);
    if (!this.remoteConfigure) {
      this.persistentGroupManager = new WifiDirectPersistentGroupManager(WifiDirectAgent.getInstance());
      return;
    } 
    this.networkConnectionHandler.pushReceiveLoopCallback((RecvLoopRunnable.RecvLoopCallback)this.recvLoopCallback);
  }
  
  protected void onDestroy() {
    super.onDestroy();
    if (this.remoteConfigure)
      this.networkConnectionHandler.removeReceiveLoopCallback((RecvLoopRunnable.RecvLoopCallback)this.recvLoopCallback); 
  }
  
  protected void onStart() {
    super.onStart();
    if (!this.remoteConfigure) {
      loadLocalGroups();
      return;
    } 
    requestRememberedGroups();
  }
  
  protected void requestRememberedGroups() {
    RobotLog.vv("FtcWifiDirectRememberedGroupsActivity", "requestRememberedGroups()");
    this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_REMEMBERED_GROUPS"));
  }
  
  protected class RecvLoopCallback extends RecvLoopRunnable.DegenerateCallback {
    public CallbackResult commandEvent(Command param1Command) {
      byte b;
      CallbackResult callbackResult = CallbackResult.NOT_HANDLED;
      try {
        String str = param1Command.getName();
        b = -1;
        int i = str.hashCode();
        if (i != -1830844033) {
          if (i == 45075229 && str.equals("CMD_REQUEST_REMEMBERED_GROUPS_RESP"))
            b = 0; 
        } else if (str.equals("CMD_NOTIFY_WIFI_DIRECT_REMEMBERED_GROUPS_CHANGED")) {
          b = 1;
        } 
      } catch (RobotCoreException robotCoreException) {
        RobotLog.logStacktrace((Throwable)robotCoreException);
        return callbackResult;
      } 
      if (b != 0) {
        if (b != 1)
          return callbackResult; 
        FtcWifiDirectRememberedGroupsActivity.this.handleRememberedGroupsChanged();
        return callbackResult;
      } 
      return FtcWifiDirectRememberedGroupsActivity.this.handleCommandRequestRememberedGroupsResp(robotCoreException.getExtra());
    }
  }
  
  protected class WifiP2pGroupItemAdapter extends ArrayAdapter<WifiDirectGroupName> {
    public WifiP2pGroupItemAdapter(Context param1Context, int param1Int, List<WifiDirectGroupName> param1List) {
      super(param1Context, param1Int, param1List);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\FtcWifiDirectRememberedGroupsActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */