package org.firstinspires.ftc.robotcore.internal.network;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.ClassUtil;
import com.qualcomm.robotcore.util.RobotLog;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.CallbackRegistrar;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;

public class WifiDirectDeviceNameManager extends WifiStartStoppable implements DeviceNameManager {
  public static final String TAG = "NetDiscover_name";
  
  protected static final String WIFI_P2P_DEVICE_NAME = "wifi_p2p_device_name";
  
  protected final Object callbackLock = new Object();
  
  protected CallbackRegistrar<DeviceNameListener> callbacks = new CallbackRegistrar();
  
  protected Context context;
  
  protected String defaultMadeUpDeviceName = null;
  
  protected StartResult deviceNameManagerStartResult = new StartResult();
  
  protected PreferencesHelper preferencesHelper;
  
  protected SharedPreferences sharedPreferences;
  
  protected SharedPreferencesListener sharedPreferencesListener = new SharedPreferencesListener();
  
  protected WifiAgentCallback wifiAgentCallback = new WifiAgentCallback();
  
  protected String wifiDirectName = null;
  
  public WifiDirectDeviceNameManager() {
    super(WifiDirectAgent.getInstance());
    Application application = AppUtil.getInstance().getApplication();
    this.context = (Context)application;
    this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)application);
    this.preferencesHelper = new PreferencesHelper("NetDiscover_name", this.sharedPreferences);
    this.sharedPreferences.registerOnSharedPreferenceChangeListener(this.sharedPreferencesListener);
  }
  
  public static boolean validDeviceName(String paramString) {
    return paramString.matches("[a-zA-Z0-9]+(-[a-zA-Z])?-(?i)(DS|RC)");
  }
  
  protected boolean doStart() {
    String str = generateNameUniquifier();
    if (AppUtil.getInstance().isRobotController()) {
      str = this.context.getString(R.string.device_name_format_rc, new Object[] { str });
    } else {
      str = this.context.getString(R.string.device_name_format_ds, new Object[] { str });
    } 
    this.defaultMadeUpDeviceName = str;
    return startWifiDirect();
  }
  
  protected void doStop() {
    this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this.sharedPreferencesListener);
    stopWifiDirect();
  }
  
  protected String generateNameUniquifier() {
    Random random = new Random();
    String str = "";
    for (int i = 0; i < 4; i++) {
      int j = random.nextInt(26);
      if (j < 26) {
        j += 65;
      } else {
        j = j + 97 - 26;
      } 
      char c = (char)j;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append(c);
      str = stringBuilder.toString();
    } 
    return str;
  }
  
  public String getDeviceName() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual initializeDeviceNameIfNecessary : ()V
    //   6: aload_0
    //   7: invokevirtual internalGetDeviceName : ()Ljava/lang/String;
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: areturn
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	15	finally
  }
  
  protected DeviceNameTracking getDeviceNameTracking() {
    return DeviceNameTracking.valueOf(this.preferencesHelper.readString(this.context.getString(R.string.pref_device_name_tracking), DeviceNameTracking.UNINITIALIZED.toString()));
  }
  
  public String getTag() {
    return "NetDiscover_name";
  }
  
  protected void initializeDeviceNameFromAndroidInternal() {
    RobotLog.vv("NetDiscover_name", "initializeDeviceNameFromAndroidInternal()...");
    String str = Settings.Global.getString(this.context.getContentResolver(), "wifi_p2p_device_name");
    if (str != null) {
      RobotLog.vv("NetDiscover_name", "initializeDeviceNameFromAndroidInternal(): name=%s", new Object[] { str });
      setDeviceNameTracking(DeviceNameTracking.WIFIDIRECT);
      this.wifiDirectName = str;
      internalSetDeviceName(str);
    } 
    RobotLog.vv("NetDiscover_name", "...initializeDeviceNameFromAndroidInternal()");
  }
  
  protected void initializeDeviceNameFromMadeUp(boolean paramBoolean) {
    RobotLog.vv("NetDiscover_name", "initializeDeviceNameFromMadeUp(): name=%s onlyUseAsPlaceholder=%b ...", new Object[] { this.defaultMadeUpDeviceName, Boolean.valueOf(paramBoolean) });
    if (paramBoolean) {
      setDeviceNameTracking(DeviceNameTracking.AWAITING_WIFIDIRECT);
    } else {
      setDeviceNameTracking(DeviceNameTracking.WIFIDIRECT);
    } 
    internalSetDeviceName(this.defaultMadeUpDeviceName);
    RobotLog.vv("NetDiscover_name", "..initializeDeviceNameFromMadeUp()");
  }
  
  protected void initializeDeviceNameFromWifiDirect() {
    RobotLog.vv("NetDiscover_name", "initializeDeviceNameFromWifiDirect()...");
    try {
      waitForWifiDirectName();
      RobotLog.vv("NetDiscover_name", "initializeDeviceNameFromWifiDirect(): name=%s", new Object[] { this.wifiDirectName });
      setDeviceNameTracking(DeviceNameTracking.WIFIDIRECT);
      internalSetDeviceName(this.wifiDirectName);
    } catch (TimeoutException timeoutException) {
    
    } finally {
      RobotLog.vv("NetDiscover_name", "...initializeDeviceNameFromWifiDirect()");
    } 
  }
  
  public void initializeDeviceNameIfNecessary() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getDeviceNameTracking : ()Lorg/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking;
    //   6: getstatic org/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking.UNINITIALIZED : Lorg/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking;
    //   9: if_acmpne -> 16
    //   12: aload_0
    //   13: invokevirtual initializeDeviceNameFromWifiDirect : ()V
    //   16: aload_0
    //   17: invokevirtual getDeviceNameTracking : ()Lorg/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking;
    //   20: getstatic org/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking.UNINITIALIZED : Lorg/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking;
    //   23: if_acmpne -> 30
    //   26: aload_0
    //   27: invokevirtual initializeDeviceNameFromAndroidInternal : ()V
    //   30: aload_0
    //   31: invokevirtual getDeviceNameTracking : ()Lorg/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking;
    //   34: astore_2
    //   35: getstatic org/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking.UNINITIALIZED : Lorg/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking;
    //   38: astore_3
    //   39: iconst_1
    //   40: istore_1
    //   41: aload_2
    //   42: aload_3
    //   43: if_acmpne -> 51
    //   46: aload_0
    //   47: iconst_1
    //   48: invokevirtual initializeDeviceNameFromMadeUp : (Z)V
    //   51: aload_0
    //   52: invokevirtual getDeviceNameTracking : ()Lorg/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking;
    //   55: getstatic org/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking.UNINITIALIZED : Lorg/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking;
    //   58: if_acmpeq -> 76
    //   61: goto -> 64
    //   64: iload_1
    //   65: invokestatic assertTrue : (Z)V
    //   68: aload_0
    //   69: monitorexit
    //   70: return
    //   71: astore_2
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_2
    //   75: athrow
    //   76: iconst_0
    //   77: istore_1
    //   78: goto -> 64
    // Exception table:
    //   from	to	target	type
    //   2	16	71	finally
    //   16	30	71	finally
    //   30	39	71	finally
    //   46	51	71	finally
    //   51	61	71	finally
    //   64	68	71	finally
  }
  
  protected String internalGetDeviceName() {
    return this.preferencesHelper.readString(this.context.getString(R.string.pref_device_name), this.defaultMadeUpDeviceName);
  }
  
  protected void internalRememberWifiDirectName(String paramString) {
    RobotLog.vv("NetDiscover_name", "remembering wifiDirectName: %s...", new Object[] { paramString });
    synchronized (this.callbackLock) {
      Assert.assertNotNull(paramString);
      if (!paramString.equals(this.wifiDirectName)) {
        this.wifiDirectName = paramString;
        RobotLog.vv("NetDiscover_name", "wifiDirectName=%s", new Object[] { paramString });
        DeviceNameTracking deviceNameTracking = getDeviceNameTracking();
        if (deviceNameTracking == DeviceNameTracking.WIFIDIRECT || deviceNameTracking == DeviceNameTracking.AWAITING_WIFIDIRECT) {
          if (deviceNameTracking == DeviceNameTracking.AWAITING_WIFIDIRECT)
            setDeviceNameTracking(DeviceNameTracking.WIFIDIRECT); 
          internalSetDeviceName(paramString);
        } 
        this.callbackLock.notifyAll();
      } 
      RobotLog.vv("NetDiscover_name", "...remembering wifiDirectName");
      return;
    } 
  }
  
  protected void internalSetDeviceName(String paramString) {
    this.preferencesHelper.writeStringPrefIfDifferent(this.context.getString(R.string.pref_device_name), paramString);
  }
  
  public void registerCallback(DeviceNameListener paramDeviceNameListener) {
    this.callbacks.registerCallback(paramDeviceNameListener);
    paramDeviceNameListener.onDeviceNameChanged(getDeviceName());
  }
  
  public String resetDeviceName(boolean paramBoolean) {
    initializeDeviceNameFromMadeUp(paramBoolean ^ true);
    return getDeviceName();
  }
  
  public void setDeviceName(String paramString, boolean paramBoolean) throws InvalidNetworkSettingException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic validDeviceName : (Ljava/lang/String;)Z
    //   6: ifeq -> 28
    //   9: iload_2
    //   10: ifne -> 20
    //   13: aload_0
    //   14: getstatic org/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking.AWAITING_WIFIDIRECT : Lorg/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking;
    //   17: invokevirtual setDeviceNameTracking : (Lorg/firstinspires/ftc/robotcore/internal/network/WifiDirectDeviceNameManager$DeviceNameTracking;)V
    //   20: aload_0
    //   21: aload_1
    //   22: invokevirtual internalSetDeviceName : (Ljava/lang/String;)V
    //   25: aload_0
    //   26: monitorexit
    //   27: return
    //   28: new org/firstinspires/ftc/robotcore/internal/network/InvalidNetworkSettingException
    //   31: dup
    //   32: ldc_w 'Name "%s" does not conform to FIRST Tech Challenge naming rules'
    //   35: iconst_1
    //   36: anewarray java/lang/Object
    //   39: dup
    //   40: iconst_0
    //   41: aload_1
    //   42: aastore
    //   43: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   46: invokespecial <init> : (Ljava/lang/String;)V
    //   49: athrow
    //   50: astore_1
    //   51: aload_0
    //   52: monitorexit
    //   53: aload_1
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	50	finally
    //   13	20	50	finally
    //   20	25	50	finally
    //   28	50	50	finally
  }
  
  protected void setDeviceNameTracking(DeviceNameTracking paramDeviceNameTracking) {
    this.preferencesHelper.writeStringPrefIfDifferent(this.context.getString(R.string.pref_device_name_tracking), paramDeviceNameTracking.toString());
  }
  
  protected void setWifiDirectDeviceName(final String deviceName) {
    RobotLog.vv("NetDiscover_name", "setWifiDirectDeviceName(%s)...", new Object[] { deviceName });
    synchronized (this.callbackLock) {
      if (this.wifiDirectName == null || !this.wifiDirectName.equals(deviceName)) {
        RobotLog.vv("NetDiscover_name", "setWifiDirectDeviceName(%s): changing", new Object[] { deviceName });
        Method method = ClassUtil.getDeclaredMethod(this.wifiDirectAgent.getWifiP2pManager().getClass(), "setDeviceName", new Class[] { WifiP2pManager.Channel.class, String.class, WifiP2pManager.ActionListener.class });
        ClassUtil.invoke(this.wifiDirectAgent.getWifiP2pManager(), method, new Object[] { this.wifiDirectAgent.getWifiP2pChannel(), deviceName, new WifiP2pManager.ActionListener() {
                public void onFailure(int param1Int) {
                  RobotLog.ee("NetDiscover_name", "setWifiDirectDeviceName(%s): failed; reason=%d", new Object[] { this.val$deviceName, Integer.valueOf(param1Int) });
                }
                
                public void onSuccess() {
                  RobotLog.vv("NetDiscover_name", "setWifiDirectDeviceName(%s): success", new Object[] { this.val$deviceName });
                }
              } });
        internalRememberWifiDirectName(deviceName);
      } 
      RobotLog.vv("NetDiscover_name", "...setWifiDirectDeviceName(%s)", new Object[] { deviceName });
      return;
    } 
  }
  
  public boolean start(StartResult paramStartResult) {
    return super.start(paramStartResult);
  }
  
  protected boolean startWifiDirect() {
    this.wifiDirectAgent.registerCallback(this.wifiAgentCallback);
    return this.wifiDirectAgent.start(this.wifiDirectAgentStarted);
  }
  
  public void stop(StartResult paramStartResult) {
    super.stop(paramStartResult);
  }
  
  protected void stopWifiDirect() {
    this.wifiDirectAgent.stop(this.wifiDirectAgentStarted);
    this.wifiDirectAgent.unregisterCallback(this.wifiAgentCallback);
  }
  
  public void unregisterCallback(DeviceNameListener paramDeviceNameListener) {
    this.callbacks.unregisterCallback(paramDeviceNameListener);
  }
  
  protected void waitForWifiDirectName() throws TimeoutException {
    RobotLog.vv("NetDiscover_name", "waitForWifiDirectName() thread=%d...", new Object[] { Long.valueOf(Thread.currentThread().getId()) });
    try {
    
    } catch (InterruptedException interruptedException) {
    
    } finally {
      RobotLog.vv("NetDiscover_name", "...waitForWifiDirectName()");
    } 
    RobotLog.vv("NetDiscover_name", "...waitForWifiDirectName()");
  }
  
  protected enum DeviceNameTracking {
    AWAITING_WIFIDIRECT, UNINITIALIZED, WIFIDIRECT;
    
    static {
      DeviceNameTracking deviceNameTracking = new DeviceNameTracking("WIFIDIRECT", 2);
      WIFIDIRECT = deviceNameTracking;
      $VALUES = new DeviceNameTracking[] { UNINITIALIZED, AWAITING_WIFIDIRECT, deviceNameTracking };
    }
  }
  
  protected class SharedPreferencesListener implements SharedPreferences.OnSharedPreferenceChangeListener {
    public void onSharedPreferenceChanged(SharedPreferences param1SharedPreferences, String param1String) {
      if (param1String.equals(WifiDirectDeviceNameManager.this.context.getString(R.string.pref_device_name))) {
        final String newDeviceName = WifiDirectDeviceNameManager.this.internalGetDeviceName();
        if (WifiDirectDeviceNameManager.this.preferencesHelper.writeStringPrefIfDifferent(WifiDirectDeviceNameManager.this.context.getString(R.string.pref_device_name_old), str)) {
          RobotLog.vv("NetDiscover_name", "deviceName pref changed: now=%s", new Object[] { str });
          if (WifiDirectDeviceNameManager.this.getDeviceNameTracking() == WifiDirectDeviceNameManager.DeviceNameTracking.WIFIDIRECT)
            WifiDirectDeviceNameManager.this.setWifiDirectDeviceName(str); 
          WifiDirectDeviceNameManager.this.callbacks.callbacksDo(new Consumer<DeviceNameListener>() {
                public void accept(DeviceNameListener param2DeviceNameListener) {
                  param2DeviceNameListener.onDeviceNameChanged(newDeviceName);
                }
              });
        } 
      } 
    }
  }
  
  class null implements Consumer<DeviceNameListener> {
    public void accept(DeviceNameListener param1DeviceNameListener) {
      param1DeviceNameListener.onDeviceNameChanged(newDeviceName);
    }
  }
  
  protected class WifiAgentCallback implements WifiDirectAgent.Callback {
    public void onReceive(Context param1Context, Intent param1Intent) {
      if ("android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(param1Intent.getAction())) {
        WifiP2pDevice wifiP2pDevice = (WifiP2pDevice)param1Intent.getParcelableExtra("wifiP2pDevice");
        WifiDirectDeviceNameManager.this.internalRememberWifiDirectName(wifiP2pDevice.deviceName);
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\WifiDirectDeviceNameManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */