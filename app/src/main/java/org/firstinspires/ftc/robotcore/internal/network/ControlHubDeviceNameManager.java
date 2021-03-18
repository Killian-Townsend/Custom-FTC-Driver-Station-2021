package org.firstinspires.ftc.robotcore.internal.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.AndroidSerialNumberNotFoundException;
import com.qualcomm.robotcore.util.Device;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ShortHash;
import java.util.zip.CRC32;
import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.CallbackRegistrar;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;

public class ControlHubDeviceNameManager implements DeviceNameManager {
  private static final int MAX_SSID_CHARS = 4;
  
  private static final String MISSING_SERIAL_SSID = "FTC-MISSING-SERIAL";
  
  private static final String TAG = "NetDiscover_ControlHubNameManager";
  
  private static final ControlHubDeviceNameManager theInstance = new ControlHubDeviceNameManager();
  
  private CallbackRegistrar<DeviceNameListener> callbacks = new CallbackRegistrar();
  
  private Context context;
  
  private String defaultMadeUpDeviceName;
  
  private String deviceName;
  
  private PreferencesHelper preferencesHelper;
  
  private SharedPreferences sharedPreferences;
  
  private SharedPreferencesListener sharedPreferencesListener = new SharedPreferencesListener();
  
  public ControlHubDeviceNameManager() {
    Context context = AppUtil.getDefContext();
    this.context = context;
    this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    this.preferencesHelper = new PreferencesHelper("NetDiscover_ControlHubNameManager", this.sharedPreferences);
    this.sharedPreferences.registerOnSharedPreferenceChangeListener(this.sharedPreferencesListener);
  }
  
  public static ControlHubDeviceNameManager getControlHubDeviceNameManager() {
    RobotLog.i("NetDiscover_ControlHubNameManager", new Object[] { "Getting name manager" });
    return theInstance;
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
    String str1 = this.preferencesHelper.readString(this.context.getString(R.string.pref_device_name), "");
    String str2 = this.preferencesHelper.readString(this.context.getString(R.string.pref_device_name_internal), "");
    if (str1.isEmpty() || str2.isEmpty())
      return DeviceNameTracking.UNINITIALIZED; 
    try {
      return DeviceNameTracking.valueOf(this.preferencesHelper.readString(this.context.getString(R.string.pref_device_name_tracking), DeviceNameTracking.UNINITIALIZED.toString()));
    } catch (Exception exception) {
      return DeviceNameTracking.UNINITIALIZED;
    } 
  }
  
  protected String handleFactoryReset() {
    RobotLog.dd("NetDiscover_ControlHubNameManager", "handleFactoryReset");
    try {
      String str = Device.getSerialNumber();
      RobotLog.dd("NetDiscover_ControlHubNameManager", "Serial: %s", new Object[] { str });
      CRC32 cRC32 = new CRC32();
      ShortHash shortHash = new ShortHash("FiRsTiNsPiReS");
      int i = (int)Math.pow(shortHash.getAlphabetLength(), 4.0D);
      cRC32.update(str.getBytes());
      str = shortHash.encode((int)(cRC32.getValue() % (i - 1)));
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("FTC-");
      stringBuilder.append(str);
      return stringBuilder.toString();
    } catch (AndroidSerialNumberNotFoundException androidSerialNumberNotFoundException) {
      RobotLog.ee("NetDiscover_ControlHubNameManager", "Failed to find Android serial number. Setting SSID to FTC-MISSING-SERIAL");
      return "FTC-MISSING-SERIAL";
    } 
  }
  
  protected void initializeDeviceNameFromMadeUp(boolean paramBoolean) {
    RobotLog.vv("NetDiscover_ControlHubNameManager", "initializeDeviceNameFromMadeUp(): name=%s ...", new Object[] { this.defaultMadeUpDeviceName });
    this.defaultMadeUpDeviceName = handleFactoryReset();
    setDeviceNameTracking(DeviceNameTracking.WIFIAP);
    internalSetDeviceName(this.defaultMadeUpDeviceName, Boolean.valueOf(paramBoolean));
    RobotLog.vv("NetDiscover_ControlHubNameManager", "..initializeDeviceNameFromMadeUp()");
  }
  
  protected void initializeDeviceNameFromSharedPrefs() {
    String str = internalGetDeviceName();
    this.deviceName = str;
    if (str != null) {
      setDeviceNameTracking(DeviceNameTracking.WIFIAP);
      internalSetDeviceName(this.deviceName, Boolean.valueOf(true));
    } 
  }
  
  public void initializeDeviceNameIfNecessary() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getDeviceNameTracking : ()Lorg/firstinspires/ftc/robotcore/internal/network/ControlHubDeviceNameManager$DeviceNameTracking;
    //   6: getstatic org/firstinspires/ftc/robotcore/internal/network/ControlHubDeviceNameManager$DeviceNameTracking.UNINITIALIZED : Lorg/firstinspires/ftc/robotcore/internal/network/ControlHubDeviceNameManager$DeviceNameTracking;
    //   9: if_acmpne -> 16
    //   12: aload_0
    //   13: invokevirtual initializeDeviceNameFromSharedPrefs : ()V
    //   16: aload_0
    //   17: invokevirtual getDeviceNameTracking : ()Lorg/firstinspires/ftc/robotcore/internal/network/ControlHubDeviceNameManager$DeviceNameTracking;
    //   20: astore_2
    //   21: getstatic org/firstinspires/ftc/robotcore/internal/network/ControlHubDeviceNameManager$DeviceNameTracking.UNINITIALIZED : Lorg/firstinspires/ftc/robotcore/internal/network/ControlHubDeviceNameManager$DeviceNameTracking;
    //   24: astore_3
    //   25: iconst_1
    //   26: istore_1
    //   27: aload_2
    //   28: aload_3
    //   29: if_acmpne -> 37
    //   32: aload_0
    //   33: iconst_1
    //   34: invokevirtual initializeDeviceNameFromMadeUp : (Z)V
    //   37: aload_0
    //   38: invokevirtual getDeviceNameTracking : ()Lorg/firstinspires/ftc/robotcore/internal/network/ControlHubDeviceNameManager$DeviceNameTracking;
    //   41: getstatic org/firstinspires/ftc/robotcore/internal/network/ControlHubDeviceNameManager$DeviceNameTracking.UNINITIALIZED : Lorg/firstinspires/ftc/robotcore/internal/network/ControlHubDeviceNameManager$DeviceNameTracking;
    //   44: if_acmpeq -> 62
    //   47: goto -> 50
    //   50: iload_1
    //   51: invokestatic assertTrue : (Z)V
    //   54: aload_0
    //   55: monitorexit
    //   56: return
    //   57: astore_2
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_2
    //   61: athrow
    //   62: iconst_0
    //   63: istore_1
    //   64: goto -> 50
    // Exception table:
    //   from	to	target	type
    //   2	16	57	finally
    //   16	25	57	finally
    //   32	37	57	finally
    //   37	47	57	finally
    //   50	54	57	finally
  }
  
  protected String internalGetDeviceName() {
    return this.preferencesHelper.readString(this.context.getString(R.string.pref_device_name_internal), this.defaultMadeUpDeviceName);
  }
  
  protected void internalSetAccessPointPassword(String paramString) {
    Intent intent = new Intent("org.firstinspires.ftc.intent.action.FTC_AP_PASSWORD_CHANGE");
    intent.putExtra("org.firstinspires.ftc.intent.extra.EXTRA_AP_PREF", paramString);
    this.context.sendBroadcast(intent);
  }
  
  protected void internalSetDeviceName(String paramString, Boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/lang/StringBuilder
    //   5: dup
    //   6: invokespecial <init> : ()V
    //   9: astore #5
    //   11: aload #5
    //   13: ldc_w 'Robot controller name: '
    //   16: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   19: pop
    //   20: aload #5
    //   22: aload_1
    //   23: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: pop
    //   27: ldc 'NetDiscover_ControlHubNameManager'
    //   29: aload #5
    //   31: invokevirtual toString : ()Ljava/lang/String;
    //   34: invokestatic ii : (Ljava/lang/String;Ljava/lang/String;)V
    //   37: iconst_0
    //   38: istore_3
    //   39: aload_2
    //   40: ifnull -> 48
    //   43: aload_2
    //   44: invokevirtual booleanValue : ()Z
    //   47: istore_3
    //   48: iload_3
    //   49: istore #4
    //   51: aload_0
    //   52: getfield preferencesHelper : Lorg/firstinspires/ftc/robotcore/internal/system/PreferencesHelper;
    //   55: aload_0
    //   56: getfield context : Landroid/content/Context;
    //   59: getstatic com/qualcomm/robotcore/R$string.pref_device_name_internal : I
    //   62: invokevirtual getString : (I)Ljava/lang/String;
    //   65: aload_1
    //   66: invokevirtual writeStringPrefIfDifferent : (Ljava/lang/String;Ljava/lang/String;)Z
    //   69: ifeq -> 121
    //   72: aload_2
    //   73: ifnonnull -> 78
    //   76: iconst_1
    //   77: istore_3
    //   78: aload_0
    //   79: getfield preferencesHelper : Lorg/firstinspires/ftc/robotcore/internal/system/PreferencesHelper;
    //   82: aload_0
    //   83: getfield context : Landroid/content/Context;
    //   86: getstatic com/qualcomm/robotcore/R$string.pref_device_name : I
    //   89: invokevirtual getString : (I)Ljava/lang/String;
    //   92: aload_1
    //   93: invokevirtual writeStringPrefIfDifferent : (Ljava/lang/String;Ljava/lang/String;)Z
    //   96: pop
    //   97: aload_0
    //   98: aload_1
    //   99: putfield deviceName : Ljava/lang/String;
    //   102: aload_0
    //   103: getfield callbacks : Lorg/firstinspires/ftc/robotcore/internal/system/CallbackRegistrar;
    //   106: new org/firstinspires/ftc/robotcore/internal/network/ControlHubDeviceNameManager$1
    //   109: dup
    //   110: aload_0
    //   111: aload_1
    //   112: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/network/ControlHubDeviceNameManager;Ljava/lang/String;)V
    //   115: invokevirtual callbacksDo : (Lorg/firstinspires/ftc/robotcore/external/Consumer;)V
    //   118: iload_3
    //   119: istore #4
    //   121: iload #4
    //   123: ifeq -> 154
    //   126: new android/content/Intent
    //   129: dup
    //   130: ldc_w 'org.firstinspires.ftc.intent.action.FTC_AP_NAME_CHANGE'
    //   133: invokespecial <init> : (Ljava/lang/String;)V
    //   136: astore_2
    //   137: aload_2
    //   138: ldc_w 'org.firstinspires.ftc.intent.extra.EXTRA_AP_PREF'
    //   141: aload_1
    //   142: invokevirtual putExtra : (Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
    //   145: pop
    //   146: aload_0
    //   147: getfield context : Landroid/content/Context;
    //   150: aload_2
    //   151: invokevirtual sendBroadcast : (Landroid/content/Intent;)V
    //   154: aload_0
    //   155: monitorexit
    //   156: return
    //   157: astore_1
    //   158: aload_0
    //   159: monitorexit
    //   160: aload_1
    //   161: athrow
    // Exception table:
    //   from	to	target	type
    //   2	37	157	finally
    //   43	48	157	finally
    //   51	72	157	finally
    //   78	118	157	finally
    //   126	154	157	finally
  }
  
  public void registerCallback(DeviceNameListener paramDeviceNameListener) {
    this.callbacks.registerCallback(paramDeviceNameListener);
    paramDeviceNameListener.onDeviceNameChanged(getDeviceName());
  }
  
  public String resetDeviceName(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokevirtual initializeDeviceNameFromMadeUp : (Z)V
    //   7: aload_0
    //   8: invokevirtual getDeviceName : ()Ljava/lang/String;
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: areturn
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public void setDeviceName(String paramString, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: iload_2
    //   5: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   8: invokevirtual internalSetDeviceName : (Ljava/lang/String;Ljava/lang/Boolean;)V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  protected void setDeviceNameTracking(DeviceNameTracking paramDeviceNameTracking) {
    this.preferencesHelper.writeStringPrefIfDifferent(this.context.getString(R.string.pref_device_name_tracking), paramDeviceNameTracking.toString());
  }
  
  public boolean start(StartResult paramStartResult) {
    return true;
  }
  
  public void stop(StartResult paramStartResult) {
    this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this.sharedPreferencesListener);
  }
  
  public void unregisterCallback(DeviceNameListener paramDeviceNameListener) {
    this.callbacks.unregisterCallback(paramDeviceNameListener);
  }
  
  protected enum DeviceNameTracking {
    UNINITIALIZED, WIFIAP;
    
    static {
      DeviceNameTracking deviceNameTracking = new DeviceNameTracking("WIFIAP", 1);
      WIFIAP = deviceNameTracking;
      $VALUES = new DeviceNameTracking[] { UNINITIALIZED, deviceNameTracking };
    }
  }
  
  protected class SharedPreferencesListener implements SharedPreferences.OnSharedPreferenceChangeListener {
    public void onSharedPreferenceChanged(SharedPreferences param1SharedPreferences, String param1String) {
      if (param1String.equals(ControlHubDeviceNameManager.this.context.getString(R.string.pref_device_name)))
        synchronized (ControlHubDeviceNameManager.this) {
          String str = param1SharedPreferences.getString(param1String, ControlHubDeviceNameManager.this.defaultMadeUpDeviceName);
          ControlHubDeviceNameManager.this.internalSetDeviceName(str, null);
          return;
        }  
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\ControlHubDeviceNameManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */