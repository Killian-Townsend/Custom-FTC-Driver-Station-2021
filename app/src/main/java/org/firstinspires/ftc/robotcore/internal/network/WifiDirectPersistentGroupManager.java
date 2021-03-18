package org.firstinspires.ftc.robotcore.internal.network;

import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import com.qualcomm.robotcore.util.ClassUtil;
import com.qualcomm.robotcore.util.RobotLog;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.external.Func;

public class WifiDirectPersistentGroupManager extends WifiStartStoppable {
  public static final String TAG = "WifiDirectPersistentGroupManager";
  
  public static final String WIFI_P2P_PERSISTENT_GROUPS_CHANGED_ACTION = "android.net.wifi.p2p.PERSISTENT_GROUPS_CHANGED";
  
  protected static Class classPersistentGroupInfoListener;
  
  protected static Class classWifiP2pGroupList;
  
  protected static Method methodDeletePersistentGroup;
  
  protected static Method methodGetGroupList;
  
  protected static Method methodGetNetworkId;
  
  protected static Method methodRequestPersistentGroupInfo;
  
  static {
    try {
      classWifiP2pGroupList = Class.forName("android.net.wifi.p2p.WifiP2pGroupList");
      classPersistentGroupInfoListener = Class.forName("android.net.wifi.p2p.WifiP2pManager$PersistentGroupInfoListener");
      methodGetGroupList = ClassUtil.getDeclaredMethod(classWifiP2pGroupList, "getGroupList", new Class[0]);
      methodRequestPersistentGroupInfo = ClassUtil.getDeclaredMethod(WifiP2pManager.class, "requestPersistentGroupInfo", new Class[] { WifiP2pManager.Channel.class, classPersistentGroupInfoListener });
      methodDeletePersistentGroup = ClassUtil.getDeclaredMethod(WifiP2pManager.class, "deletePersistentGroup", new Class[] { WifiP2pManager.Channel.class, int.class, WifiP2pManager.ActionListener.class });
      methodGetNetworkId = ClassUtil.getDeclaredMethod(WifiP2pGroup.class, "getNetworkId", new Class[0]);
      return;
    } catch (ClassNotFoundException classNotFoundException) {
      RobotLog.ee("WifiDirectPersistentGroupManager", classNotFoundException, "exception thrown in static initialization");
      return;
    } 
  }
  
  public WifiDirectPersistentGroupManager(WifiDirectAgent paramWifiDirectAgent) {
    super(paramWifiDirectAgent);
  }
  
  protected Object createProxy(final PersistentGroupInfoListener target) {
    ClassLoader classLoader = classPersistentGroupInfoListener.getClassLoader();
    Class clazz = classPersistentGroupInfoListener;
    InvocationHandler invocationHandler = new InvocationHandler() {
        public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
          if (param1Method.getName().equals("onPersistentGroupInfoAvailable")) {
            param1Object = ClassUtil.invoke(param1ArrayOfObject[0], WifiDirectPersistentGroupManager.methodGetGroupList, new Object[0]);
            target.onPersistentGroupInfoAvailable((Collection<WifiP2pGroup>)param1Object);
          } 
          return null;
        }
      };
    return Proxy.newProxyInstance(classLoader, new Class[] { clazz }, invocationHandler);
  }
  
  public void deleteAllPersistentGroups() {
    Iterator<WifiP2pGroup> iterator = getPersistentGroups().iterator();
    while (iterator.hasNext())
      deletePersistentGroup(iterator.next()); 
  }
  
  public void deletePersistentGroup(int paramInt, WifiP2pManager.ActionListener paramActionListener) {
    RobotLog.vv("WifiDirectPersistentGroupManager", "deletePersistentGroup() netId=%d", new Object[] { Integer.valueOf(paramInt) });
    ClassUtil.invoke(this.wifiDirectAgent.getWifiP2pManager(), methodDeletePersistentGroup, new Object[] { this.wifiDirectAgent.getWifiP2pChannel(), Integer.valueOf(paramInt), paramActionListener });
  }
  
  public boolean deletePersistentGroup(final int netId) {
    return ((Boolean)lockCompletion(Boolean.valueOf(false), new Func<Boolean>() {
          public Boolean value() {
            boolean bool;
            WifiDirectPersistentGroupManager.this.resetCompletion();
            try {
              WifiDirectPersistentGroupManager.this.deletePersistentGroup(netId, new WifiP2pManager.ActionListener() {
                    public void onFailure(int param2Int) {
                      RobotLog.vv("WifiDirectPersistentGroupManager", "failed to delete persistent group: netId=%d", new Object[] { Integer.valueOf(this.this$1.val$netId) });
                      WifiDirectPersistentGroupManager.this.releaseCompletion(false);
                    }
                    
                    public void onSuccess() {
                      WifiDirectPersistentGroupManager.this.releaseCompletion(true);
                    }
                  });
              bool = WifiDirectPersistentGroupManager.this.waitForCompletion();
            } catch (InterruptedException interruptedException) {
              bool = WifiDirectPersistentGroupManager.this.receivedCompletionInterrupt(interruptedException);
            } 
            return Boolean.valueOf(bool);
          }
        })).booleanValue();
  }
  
  public boolean deletePersistentGroup(WifiP2pGroup paramWifiP2pGroup) {
    return deletePersistentGroup(getNetworkId(paramWifiP2pGroup));
  }
  
  protected boolean doStart() throws InterruptedException {
    return true;
  }
  
  protected void doStop() throws InterruptedException {}
  
  public int getNetworkId(WifiP2pGroup paramWifiP2pGroup) {
    return ((Integer)ClassUtil.invoke(paramWifiP2pGroup, methodGetNetworkId, new Object[0])).intValue();
  }
  
  public Collection<WifiP2pGroup> getPersistentGroups() {
    final ArrayList<WifiP2pGroup> defRefResult = new ArrayList();
    return lockCompletion(arrayList, new Func<Collection<WifiP2pGroup>>() {
          Collection<WifiP2pGroup> result;
          
          public Collection<WifiP2pGroup> value() {
            this.result = defRefResult;
            WifiDirectPersistentGroupManager.this.resetCompletion();
            try {
              WifiDirectPersistentGroupManager.this.requestPersistentGroups(new WifiDirectPersistentGroupManager.PersistentGroupInfoListener() {
                    public void onPersistentGroupInfoAvailable(Collection<WifiP2pGroup> param2Collection) {
                      WifiDirectPersistentGroupManager.null.this.result = param2Collection;
                      WifiDirectPersistentGroupManager.this.releaseCompletion(true);
                    }
                  });
              WifiDirectPersistentGroupManager.this.waitForCompletion();
            } catch (InterruptedException interruptedException) {
              WifiDirectPersistentGroupManager.this.receivedCompletionInterrupt(interruptedException);
            } 
            return this.result;
          }
        });
  }
  
  public String getTag() {
    return "WifiDirectPersistentGroupManager";
  }
  
  public void requestPersistentGroups(PersistentGroupInfoListener paramPersistentGroupInfoListener) {
    Object object = createProxy(paramPersistentGroupInfoListener);
    ClassUtil.invoke(this.wifiDirectAgent.getWifiP2pManager(), methodRequestPersistentGroupInfo, new Object[] { this.wifiDirectAgent.getWifiP2pChannel(), object });
  }
  
  public static interface PersistentGroupInfoListener {
    void onPersistentGroupInfoAvailable(Collection<WifiP2pGroup> param1Collection);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\WifiDirectPersistentGroupManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */