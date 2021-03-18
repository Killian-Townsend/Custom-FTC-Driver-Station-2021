package com.qualcomm.hardware.lynx;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.qualcomm.hardware.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.GlobalWarningSource;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class LynxModuleWarningManager {
  private static final int LOW_BATTERY_LOG_FREQUENCY_SECONDS = 2;
  
  private static final int LOW_BATTERY_STATUS_TIMEOUT_SECONDS = 2;
  
  private static final int MIN_FW_VERSION_ENG = 2;
  
  private static final int MIN_FW_VERSION_MAJOR = 1;
  
  private static final int MIN_FW_VERSION_MINOR = 8;
  
  private static final String TAG = "LynxModuleWarningManager";
  
  private static final int UNRESPONSIVE_LOG_FREQUENCY_SECONDS = 2;
  
  private static final LynxModuleWarningManager instance = new LynxModuleWarningManager();
  
  private String cachedWarningMessage = null;
  
  private volatile HardwareMap hardwareMap = null;
  
  private final ConcurrentMap<Integer, LowBatteryStatus> modulesReportedLowBattery = new ConcurrentHashMap<Integer, LowBatteryStatus>();
  
  private final Set<String> modulesReportedReset = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
  
  private final ConcurrentMap<Integer, UnresponsiveStatus> modulesReportedUnresponsive = new ConcurrentHashMap<Integer, UnresponsiveStatus>();
  
  private final OpModeManagerNotifier.Notifications opModeNotificationListener = new WarningManagerOpModeListener();
  
  private final Set<String> outdatedModules = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
  
  private final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AppUtil.getDefContext());
  
  private volatile boolean userOpModeRunning = false;
  
  private final Object warningMessageLock = new Object();
  
  private final GlobalWarningSource warningSource = new LynxModuleWarningSource();
  
  public static LynxModuleWarningManager getInstance() {
    return instance;
  }
  
  private String getModuleName(LynxModule paramLynxModule) {
    try {
      return this.hardwareMap.getNamesOf(paramLynxModule).iterator().next();
    } catch (RuntimeException runtimeException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Expansion Hub ");
      stringBuilder.append(paramLynxModule.getModuleAddress());
      return stringBuilder.toString();
    } 
  }
  
  private boolean isFwVersionOutdated(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool = false;
    if (paramInt1 > 1)
      return false; 
    if (paramInt1 < 1)
      return true; 
    if (paramInt2 > 8)
      return false; 
    if (paramInt2 < 8)
      return true; 
    if (paramInt3 < 2)
      bool = true; 
    return bool;
  }
  
  private void lookForOutdatedModules() {
    this.outdatedModules.clear();
    for (LynxModule lynxModule : this.hardwareMap.getAll(LynxModule.class)) {
      try {
        String str = lynxModule.getNullableFirmwareVersionString();
        if (str == null)
          continue; 
        String[] arrayOfString = Arrays.<String>copyOfRange(str.split("(, )?\\w*: "), 2, 5);
        if (isFwVersionOutdated(Integer.parseInt(arrayOfString[0]), Integer.parseInt(arrayOfString[1]), Integer.parseInt(arrayOfString[2])))
          this.outdatedModules.add(getModuleName(lynxModule)); 
      } catch (RuntimeException runtimeException) {
        RobotLog.ee("LynxModuleWarningManager", runtimeException, "Exception caught while checking if module is outdated");
      } 
    } 
  }
  
  public void init(OpModeManagerImpl paramOpModeManagerImpl, HardwareMap paramHardwareMap) {
    this.hardwareMap = paramHardwareMap;
    paramOpModeManagerImpl.registerListener(this.opModeNotificationListener);
    this.warningSource.clearGlobalWarning();
    RobotLog.registerGlobalWarningSource(this.warningSource);
    if (this.sharedPrefs.getBoolean(AppUtil.getDefContext().getString(R.string.pref_warn_about_outdated_firmware), true))
      lookForOutdatedModules(); 
  }
  
  public void reportModuleLowBattery(LynxModule paramLynxModule) {
    if (!paramLynxModule.isUserModule())
      return; 
    int i = paramLynxModule.getModuleAddress();
    LowBatteryStatus lowBatteryStatus2 = this.modulesReportedLowBattery.get(Integer.valueOf(i));
    LowBatteryStatus lowBatteryStatus1 = lowBatteryStatus2;
    if (lowBatteryStatus2 == null) {
      lowBatteryStatus1 = new LowBatteryStatus(paramLynxModule, getModuleName(paramLynxModule));
      this.modulesReportedLowBattery.put(Integer.valueOf(i), lowBatteryStatus1);
    } 
    lowBatteryStatus1.reportConditionAndLogWithThrottle(this.userOpModeRunning);
  }
  
  public void reportModuleReset(LynxModule paramLynxModule) {
    String str1;
    if (!paramLynxModule.isUserModule())
      return; 
    String str2 = getModuleName(paramLynxModule);
    if (this.userOpModeRunning) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("%s regained power after a complete power loss.");
      stringBuilder.append(" A user Op Mode was running, so unexpected behavior may occur.");
      String str = stringBuilder.toString();
      str1 = str;
      if (this.modulesReportedReset.add(str2))
        synchronized (this.warningMessageLock) {
          this.cachedWarningMessage = null;
          null = str;
        }  
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("%s regained power after a complete power loss.");
      stringBuilder.append(" No user Op Mode was running.");
      str1 = stringBuilder.toString();
    } 
    RobotLog.ww("HubPowerCycle", str1, new Object[] { str2 });
  }
  
  public void reportModuleUnresponsive(LynxModule paramLynxModule) {
    if (paramLynxModule.isUserModule()) {
      if (!paramLynxModule.isOpen)
        return; 
      int i = paramLynxModule.getModuleAddress();
      UnresponsiveStatus unresponsiveStatus2 = this.modulesReportedUnresponsive.get(Integer.valueOf(i));
      UnresponsiveStatus unresponsiveStatus1 = unresponsiveStatus2;
      if (unresponsiveStatus2 == null) {
        unresponsiveStatus1 = new UnresponsiveStatus(paramLynxModule, getModuleName(paramLynxModule));
        this.modulesReportedUnresponsive.put(Integer.valueOf(i), unresponsiveStatus1);
      } 
      unresponsiveStatus1.reportConditionAndLogWithThrottle(this.userOpModeRunning);
    } 
  }
  
  private static abstract class ConditionStatus {
    boolean conditionPreviouslyTrue = false;
    
    boolean conditionTrueDuringOpModeRun = false;
    
    final int logFrequencySeconds;
    
    final LynxModuleIntf lynxModule;
    
    final String moduleName;
    
    final ElapsedTime timeSinceConditionLastReported = new ElapsedTime();
    
    final ElapsedTime timeSinceConditionLogged = new ElapsedTime(0L);
    
    ConditionStatus(LynxModuleIntf param1LynxModuleIntf, String param1String, int param1Int) {
      this.lynxModule = param1LynxModuleIntf;
      this.logFrequencySeconds = param1Int;
      this.moduleName = param1String;
    }
    
    abstract boolean conditionCurrentlyTrue();
    
    final boolean hasChangedSinceLastCheck() {
      boolean bool;
      boolean bool1 = conditionCurrentlyTrue();
      if (bool1 != this.conditionPreviouslyTrue) {
        bool = true;
      } else {
        bool = false;
      } 
      this.conditionPreviouslyTrue = bool1;
      return bool;
    }
    
    abstract void logCondition();
    
    void reportConditionAndLogWithThrottle(boolean param1Boolean) {
      if (param1Boolean)
        this.conditionTrueDuringOpModeRun = true; 
      this.timeSinceConditionLastReported.reset();
      if (this.timeSinceConditionLogged.seconds() > this.logFrequencySeconds) {
        logCondition();
        this.timeSinceConditionLogged.reset();
      } 
    }
  }
  
  private static class LowBatteryStatus extends ConditionStatus {
    private LowBatteryStatus(LynxModule param1LynxModule, String param1String) {
      super(param1LynxModule, param1String, 2);
    }
    
    boolean conditionCurrentlyTrue() {
      return (this.timeSinceConditionLastReported.seconds() < 2.0D);
    }
    
    void logCondition() {
      RobotLog.w("%s currently has a battery too low to run motors and servos.", new Object[] { this.moduleName });
    }
  }
  
  private class LynxModuleWarningSource implements GlobalWarningSource {
    private int warningMessageSuppressionCount = 0;
    
    private LynxModuleWarningSource() {}
    
    private boolean composeBatteryLowWarning(StringBuilder param1StringBuilder) {
      int i = LynxModuleWarningManager.this.modulesReportedLowBattery.size();
      boolean bool = false;
      if (i < 1)
        return false; 
      ArrayList<String> arrayList1 = new ArrayList();
      ArrayList<String> arrayList2 = new ArrayList();
      for (LynxModuleWarningManager.LowBatteryStatus lowBatteryStatus : LynxModuleWarningManager.this.modulesReportedLowBattery.values()) {
        if (lowBatteryStatus.conditionCurrentlyTrue()) {
          arrayList1.add(lowBatteryStatus.moduleName);
          continue;
        } 
        if (lowBatteryStatus.conditionTrueDuringOpModeRun)
          arrayList2.add(lowBatteryStatus.moduleName); 
      } 
      if (arrayList1.size() > 0) {
        composeModuleList(arrayList1, param1StringBuilder);
        param1StringBuilder.append(AppUtil.getDefContext().getString(R.string.lynxModuleBatteryIsCurrentlyLow));
        param1StringBuilder.append(" ");
        bool = true;
      } 
      if (arrayList2.size() > 0) {
        composeModuleList(arrayList2, param1StringBuilder);
        param1StringBuilder.append(AppUtil.getDefContext().getString(R.string.lynxModuleBatteryWasLow));
        param1StringBuilder.append(" ");
        return true;
      } 
      return bool;
    }
    
    private String composeNotRespondingWarning() {
      int i = LynxModuleWarningManager.this.modulesReportedUnresponsive.size();
      String str = null;
      boolean bool = true;
      if (i < 1)
        return null; 
      ArrayList<String> arrayList1 = new ArrayList();
      ArrayList<String> arrayList2 = new ArrayList();
      for (LynxModuleWarningManager.UnresponsiveStatus unresponsiveStatus : LynxModuleWarningManager.this.modulesReportedUnresponsive.values()) {
        if (unresponsiveStatus.lynxModule.isNotResponding()) {
          arrayList1.add(unresponsiveStatus.moduleName);
          continue;
        } 
        if (unresponsiveStatus.conditionTrueDuringOpModeRun && !LynxModuleWarningManager.this.modulesReportedReset.contains(unresponsiveStatus.moduleName))
          arrayList2.add(unresponsiveStatus.moduleName); 
      } 
      i = 0;
      StringBuilder stringBuilder = new StringBuilder();
      if (arrayList1.size() > 0) {
        composeModuleList(arrayList1, stringBuilder);
        stringBuilder.append(AppUtil.getDefContext().getString(R.string.lynxModuleCurrentlyNotResponding));
        i = 1;
      } 
      if (arrayList2.size() > 0) {
        if (arrayList1.size() > 0)
          stringBuilder.append("; "); 
        composeModuleList(arrayList2, stringBuilder);
        stringBuilder.append(AppUtil.getDefContext().getString(R.string.lynxModulePreviouslyNotResponding));
        i = bool;
      } 
      if (i != 0)
        str = stringBuilder.toString(); 
      return str;
    }
    
    private String composeOutdatedHubsWarning() {
      if (LynxModuleWarningManager.this.outdatedModules.size() < 1)
        return null; 
      StringBuilder stringBuilder = new StringBuilder();
      composeModuleList(LynxModuleWarningManager.this.outdatedModules, stringBuilder);
      stringBuilder.append(AppUtil.getDefContext().getString(R.string.lynxModuleFirmwareOutdated));
      stringBuilder.append(" ");
      return stringBuilder.toString();
    }
    
    private void composePowerIssueTip(boolean param1Boolean, StringBuilder param1StringBuilder) {
      if (param1Boolean) {
        param1StringBuilder.append(AppUtil.getDefContext().getString(R.string.powerIssueTip));
        return;
      } 
      param1StringBuilder.append(AppUtil.getDefContext().getString(R.string.robotOffTip));
    }
    
    private String composePowerIssuesWarning() {
      if (LynxModuleWarningManager.this.modulesReportedReset.size() < 1 && LynxModuleWarningManager.this.modulesReportedLowBattery.size() < 1)
        return null; 
      StringBuilder stringBuilder = new StringBuilder();
      boolean bool1 = composePowerLossWarning(stringBuilder);
      boolean bool2 = composeBatteryLowWarning(stringBuilder);
      if (bool1 || bool2) {
        composePowerIssueTip(LynxModuleWarningManager.this.userOpModeRunning, stringBuilder);
        return stringBuilder.toString();
      } 
      return null;
    }
    
    private boolean composePowerLossWarning(StringBuilder param1StringBuilder) {
      if (LynxModuleWarningManager.this.modulesReportedReset.size() < 1)
        return false; 
      composeModuleList(LynxModuleWarningManager.this.modulesReportedReset, param1StringBuilder);
      param1StringBuilder.append(AppUtil.getDefContext().getString(R.string.lynxModulePowerLost));
      param1StringBuilder.append(" ");
      return true;
    }
    
    private String composeWarning() {
      String str1 = composeNotRespondingWarning();
      String str2 = composePowerIssuesWarning();
      String str3 = composeOutdatedHubsWarning();
      StringBuilder stringBuilder = new StringBuilder();
      if (str1 != null) {
        stringBuilder.append(str1);
        if (str2 != null || str3 != null)
          stringBuilder.append("; "); 
      } 
      if (str2 != null) {
        stringBuilder.append(str2);
        if (str3 != null)
          stringBuilder.append("; "); 
      } 
      if (str3 != null)
        stringBuilder.append(str3); 
      return stringBuilder.toString();
    }
    
    public void clearGlobalWarning() {
      synchronized (LynxModuleWarningManager.this.warningMessageLock) {
        LynxModuleWarningManager.access$502(LynxModuleWarningManager.this, null);
        LynxModuleWarningManager.this.modulesReportedUnresponsive.clear();
        LynxModuleWarningManager.this.modulesReportedReset.clear();
        LynxModuleWarningManager.this.modulesReportedLowBattery.clear();
        LynxModuleWarningManager.this.outdatedModules.clear();
        this.warningMessageSuppressionCount = 0;
        return;
      } 
    }
    
    void composeModuleList(Collection<String> param1Collection, StringBuilder param1StringBuilder) {
      Iterator<String> iterator = param1Collection.iterator();
      while (iterator.hasNext()) {
        param1StringBuilder.append(iterator.next());
        if (iterator.hasNext())
          param1StringBuilder.append(", "); 
      } 
      param1StringBuilder.append(" ");
    }
    
    public String getGlobalWarning() {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;
      //   4: invokestatic access$400 : (Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;)Ljava/lang/Object;
      //   7: astore #5
      //   9: aload #5
      //   11: monitorenter
      //   12: aload_0
      //   13: getfield warningMessageSuppressionCount : I
      //   16: ifle -> 25
      //   19: aload #5
      //   21: monitorexit
      //   22: ldc ''
      //   24: areturn
      //   25: aload_0
      //   26: getfield this$0 : Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;
      //   29: invokestatic access$500 : (Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;)Ljava/lang/String;
      //   32: astore #6
      //   34: iconst_0
      //   35: istore #4
      //   37: aload #6
      //   39: ifnull -> 201
      //   42: iconst_1
      //   43: istore_1
      //   44: goto -> 47
      //   47: iload_1
      //   48: istore_3
      //   49: iload_1
      //   50: ifeq -> 206
      //   53: aload_0
      //   54: getfield this$0 : Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;
      //   57: invokestatic access$600 : (Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;)Ljava/util/concurrent/ConcurrentMap;
      //   60: invokeinterface values : ()Ljava/util/Collection;
      //   65: invokeinterface iterator : ()Ljava/util/Iterator;
      //   70: astore #6
      //   72: iload_1
      //   73: istore_2
      //   74: aload #6
      //   76: invokeinterface hasNext : ()Z
      //   81: ifeq -> 102
      //   84: aload #6
      //   86: invokeinterface next : ()Ljava/lang/Object;
      //   91: checkcast com/qualcomm/hardware/lynx/LynxModuleWarningManager$LowBatteryStatus
      //   94: invokevirtual hasChangedSinceLastCheck : ()Z
      //   97: ifeq -> 72
      //   100: iconst_0
      //   101: istore_2
      //   102: aload_0
      //   103: getfield this$0 : Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;
      //   106: invokestatic access$700 : (Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;)Ljava/util/concurrent/ConcurrentMap;
      //   109: invokeinterface values : ()Ljava/util/Collection;
      //   114: invokeinterface iterator : ()Ljava/util/Iterator;
      //   119: astore #6
      //   121: iload_2
      //   122: istore_3
      //   123: aload #6
      //   125: invokeinterface hasNext : ()Z
      //   130: ifeq -> 206
      //   133: iload #4
      //   135: istore_1
      //   136: aload #6
      //   138: invokeinterface next : ()Ljava/lang/Object;
      //   143: checkcast com/qualcomm/hardware/lynx/LynxModuleWarningManager$UnresponsiveStatus
      //   146: invokevirtual hasChangedSinceLastCheck : ()Z
      //   149: ifne -> 162
      //   152: iload_2
      //   153: ifne -> 121
      //   156: iload #4
      //   158: istore_1
      //   159: goto -> 162
      //   162: iload_1
      //   163: ifne -> 178
      //   166: aload_0
      //   167: getfield this$0 : Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;
      //   170: aload_0
      //   171: invokespecial composeWarning : ()Ljava/lang/String;
      //   174: invokestatic access$502 : (Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;Ljava/lang/String;)Ljava/lang/String;
      //   177: pop
      //   178: aload_0
      //   179: getfield this$0 : Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;
      //   182: invokestatic access$500 : (Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;)Ljava/lang/String;
      //   185: astore #6
      //   187: aload #5
      //   189: monitorexit
      //   190: aload #6
      //   192: areturn
      //   193: astore #6
      //   195: aload #5
      //   197: monitorexit
      //   198: aload #6
      //   200: athrow
      //   201: iconst_0
      //   202: istore_1
      //   203: goto -> 47
      //   206: iload_3
      //   207: istore_1
      //   208: goto -> 162
      // Exception table:
      //   from	to	target	type
      //   12	22	193	finally
      //   25	34	193	finally
      //   53	72	193	finally
      //   74	100	193	finally
      //   102	121	193	finally
      //   123	133	193	finally
      //   136	152	193	finally
      //   166	178	193	finally
      //   178	190	193	finally
      //   195	198	193	finally
    }
    
    public void setGlobalWarning(String param1String) {}
    
    public void suppressGlobalWarning(boolean param1Boolean) {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;
      //   4: invokestatic access$400 : (Lcom/qualcomm/hardware/lynx/LynxModuleWarningManager;)Ljava/lang/Object;
      //   7: astore_2
      //   8: aload_2
      //   9: monitorenter
      //   10: iload_1
      //   11: ifeq -> 27
      //   14: aload_0
      //   15: aload_0
      //   16: getfield warningMessageSuppressionCount : I
      //   19: iconst_1
      //   20: iadd
      //   21: putfield warningMessageSuppressionCount : I
      //   24: goto -> 37
      //   27: aload_0
      //   28: aload_0
      //   29: getfield warningMessageSuppressionCount : I
      //   32: iconst_1
      //   33: isub
      //   34: putfield warningMessageSuppressionCount : I
      //   37: aload_2
      //   38: monitorexit
      //   39: return
      //   40: astore_3
      //   41: aload_2
      //   42: monitorexit
      //   43: aload_3
      //   44: athrow
      // Exception table:
      //   from	to	target	type
      //   14	24	40	finally
      //   27	37	40	finally
      //   37	39	40	finally
      //   41	43	40	finally
    }
  }
  
  private static class UnresponsiveStatus extends ConditionStatus {
    private UnresponsiveStatus(LynxModuleIntf param1LynxModuleIntf, String param1String) {
      super(param1LynxModuleIntf, param1String, 2);
    }
    
    boolean conditionCurrentlyTrue() {
      return this.lynxModule.isNotResponding();
    }
    
    void logCondition() {
      RobotLog.w("%s is currently unresponsive.", new Object[] { this.moduleName });
    }
  }
  
  private class WarningManagerOpModeListener implements OpModeManagerNotifier.Notifications {
    private WarningManagerOpModeListener() {}
    
    public void onOpModePostStop(OpMode param1OpMode) {
      LynxModuleWarningManager.access$902(LynxModuleWarningManager.this, false);
    }
    
    public void onOpModePreInit(OpMode param1OpMode) {
      LynxModuleWarningManager.access$902(LynxModuleWarningManager.this, param1OpMode instanceof OpModeManagerImpl.DefaultOpMode ^ true);
    }
    
    public void onOpModePreStart(OpMode param1OpMode) {}
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxModuleWarningManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */