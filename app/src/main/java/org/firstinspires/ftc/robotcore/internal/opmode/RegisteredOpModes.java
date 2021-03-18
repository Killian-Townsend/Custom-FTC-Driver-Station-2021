package org.firstinspires.ftc.robotcore.internal.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.exception.DuplicateNameException;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.internal.files.FileModifyObserver;
import org.firstinspires.ftc.robotcore.internal.files.RecursiveFileObserver;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class RegisteredOpModes implements OpModeManager {
  public static final String TAG = "RegisteredOpModes";
  
  protected RecursiveFileObserver blocksOpModeMonitor;
  
  protected volatile boolean blocksOpModesChanged = false;
  
  protected final List<InstanceOpModeRegistrar> instanceOpModeRegistrars = new ArrayList<InstanceOpModeRegistrar>();
  
  protected volatile boolean onBotJavaChanged = false;
  
  protected FileModifyObserver onBotJavaMonitor = new FileModifyObserver(OnBotJavaHelper.buildSuccessfulFile, new FileModifyObserver.Listener() {
        public void onFileChanged(int param1Int, File param1File) {
          RobotLog.vv("RegisteredOpModes", "noting that OnBotJava changed");
          RegisteredOpModes.this.onBotJavaChanged = true;
        }
      });
  
  protected Map<String, OpModeMetaAndClass> opModeClasses = new LinkedHashMap<String, OpModeMetaAndClass>();
  
  protected Map<String, OpModeMetaAndInstance> opModeInstances = new LinkedHashMap<String, OpModeMetaAndInstance>();
  
  protected boolean opModesAreLocked = false;
  
  protected final Object opModesLock = new Object();
  
  protected volatile boolean opmodesAreRegistered = false;
  
  public RegisteredOpModes() {
    RecursiveFileObserver recursiveFileObserver = new RecursiveFileObserver(AppUtil.BLOCK_OPMODES_DIR, 712, RecursiveFileObserver.Mode.NONRECURSVIVE, new RecursiveFileObserver.Listener() {
          public void onEvent(int param1Int, File param1File) {
            if ((param1Int & 0x2C8) != 0 && (param1File.getName().endsWith(".js") || param1File.getName().endsWith(".blk"))) {
              RobotLog.vv("RegisteredOpModes", "noting that Blocks changed");
              RegisteredOpModes.this.blocksOpModesChanged = true;
            } 
          }
        });
    this.blocksOpModeMonitor = recursiveFileObserver;
    recursiveFileObserver.startWatching();
  }
  
  public static RegisteredOpModes getInstance() {
    return InstanceHolder.theInstance;
  }
  
  public void addInstanceOpModeRegistrar(InstanceOpModeRegistrar paramInstanceOpModeRegistrar) {
    synchronized (this.instanceOpModeRegistrars) {
      this.instanceOpModeRegistrars.add(paramInstanceOpModeRegistrar);
      return;
    } 
  }
  
  protected void callInstanceOpModeRegistrars() {
    synchronized (this.instanceOpModeRegistrars) {
      for (InstanceOpModeRegistrar instanceOpModeRegistrar : this.instanceOpModeRegistrars) {
        instanceOpModeRegistrar.register(new InstanceOpModeManager() {
              public void register(OpModeMeta param1OpModeMeta, OpMode param1OpMode) {
                RegisteredOpModes.this.register(param1OpModeMeta, param1OpMode, instanceOpModeRegistrar);
              }
            });
      } 
      return;
    } 
  }
  
  public void clearBlocksOpModesChanged() {
    this.blocksOpModesChanged = false;
  }
  
  public void clearOnBotJavaChanged() {
    this.onBotJavaChanged = false;
  }
  
  public boolean getBlocksOpModesChanged() {
    return this.blocksOpModesChanged;
  }
  
  public boolean getOnBotJavaChanged() {
    return this.onBotJavaChanged;
  }
  
  public OpMode getOpMode(final String opModeName) {
    return lockOpModesWhile(new Supplier<OpMode>() {
          public OpMode get() {
            try {
              return RegisteredOpModes.this.opModeInstances.containsKey(opModeName) ? ((OpModeMetaAndInstance)RegisteredOpModes.this.opModeInstances.get(opModeName)).instance : (RegisteredOpModes.this.opModeClasses.containsKey(opModeName) ? ((OpModeMetaAndClass)RegisteredOpModes.this.opModeClasses.get(opModeName)).clazz.newInstance() : null);
            } catch (InstantiationException|IllegalAccessException instantiationException) {
              return null;
            } 
          }
        });
  }
  
  public List<OpModeMeta> getOpModes() {
    return lockOpModesWhile(new Supplier<List<OpModeMeta>>() {
          public List<OpModeMeta> get() {
            Assert.assertTrue(RegisteredOpModes.this.opmodesAreRegistered);
            LinkedList<OpModeMeta> linkedList = new LinkedList();
            for (OpModeMetaAndClass opModeMetaAndClass : RegisteredOpModes.this.opModeClasses.values()) {
              if (!opModeMetaAndClass.meta.name.equals("$Stop$Robot$"))
                linkedList.add(opModeMetaAndClass.meta); 
            } 
            Iterator iterator = RegisteredOpModes.this.opModeInstances.values().iterator();
            while (iterator.hasNext())
              linkedList.add(((OpModeMetaAndInstance)iterator.next()).meta); 
            return linkedList;
          }
        });
  }
  
  protected boolean isOpmodeRegistered(OpModeMeta paramOpModeMeta) {
    Assert.assertTrue(this.opModesAreLocked);
    return (this.opModeClasses.containsKey(paramOpModeMeta.name) || this.opModeInstances.containsKey(paramOpModeMeta.name));
  }
  
  public <T> T lockOpModesWhile(Supplier<T> paramSupplier) {
    synchronized (this.opModesLock) {
      this.opModesAreLocked = true;
      try {
        return (T)paramSupplier.get();
      } finally {
        this.opModesAreLocked = false;
      } 
    } 
  }
  
  public void lockOpModesWhile(Runnable paramRunnable) {
    synchronized (this.opModesLock) {
      this.opModesAreLocked = true;
      try {
        paramRunnable.run();
        return;
      } finally {
        this.opModesAreLocked = false;
      } 
    } 
  }
  
  public void register(String paramString, OpMode paramOpMode) {
    register(new OpModeMeta(paramString), paramOpMode);
  }
  
  public void register(String paramString, Class paramClass) {
    register(new OpModeMeta(paramString), paramClass);
  }
  
  public void register(OpModeMeta paramOpModeMeta, OpMode paramOpMode) {
    register(paramOpModeMeta, paramOpMode, null);
  }
  
  public void register(final OpModeMeta meta, final OpMode opModeInstance, final InstanceOpModeRegistrar instanceOpModeRegistrar) {
    lockOpModesWhile(new Runnable() {
          public void run() {
            if (RegisteredOpModes.this.reportIfOpModeAlreadyRegistered(meta)) {
              RegisteredOpModes.this.opModeInstances.put(meta.name, new OpModeMetaAndInstance(meta, opModeInstance, instanceOpModeRegistrar));
              RobotLog.vv("OpmodeRegistration", String.format("registered instance as {%s}", new Object[] { this.val$meta }));
            } 
          }
        });
  }
  
  public void register(final OpModeMeta meta, final Class opMode) {
    lockOpModesWhile(new Runnable() {
          public void run() {
            if (RegisteredOpModes.this.reportIfOpModeAlreadyRegistered(meta)) {
              RegisteredOpModes.this.opModeClasses.put(meta.name, new OpModeMetaAndClass(meta, opMode));
              RobotLog.vv("OpmodeRegistration", String.format("registered {%s} as {%s}", new Object[] { this.val$opMode.getSimpleName(), this.val$meta.name }));
              return;
            } 
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Duplicate for ");
            stringBuilder.append(meta.name);
            throw new DuplicateNameException(stringBuilder.toString());
          }
        });
  }
  
  public void registerAllOpModes(final OpModeRegister userOpmodeRegister) {
    lockOpModesWhile(new Runnable() {
          public void run() {
            RegisteredOpModes.this.opModeClasses.clear();
            RegisteredOpModes.this.opModeInstances.clear();
            RegisteredOpModes.this.register("$Stop$Robot$", OpModeManagerImpl.DefaultOpMode.class);
            RegisteredOpModes.this.callInstanceOpModeRegistrars();
            userOpmodeRegister.register(RegisteredOpModes.this);
            AnnotatedOpModeClassFilter.getInstance().registerAllClasses(RegisteredOpModes.this);
            RegisteredOpModes.this.opmodesAreRegistered = true;
          }
        });
  }
  
  public void registerInstanceOpModes() {
    lockOpModesWhile(new Runnable() {
          public void run() {
            synchronized (RegisteredOpModes.this.instanceOpModeRegistrars) {
              ArrayList arrayList = new ArrayList(RegisteredOpModes.this.opModeInstances.values());
              for (InstanceOpModeRegistrar instanceOpModeRegistrar : RegisteredOpModes.this.instanceOpModeRegistrars) {
                for (OpModeMetaAndInstance opModeMetaAndInstance : arrayList) {
                  if (opModeMetaAndInstance.instanceOpModeRegistrar == instanceOpModeRegistrar)
                    RegisteredOpModes.this.unregister(opModeMetaAndInstance.meta); 
                } 
              } 
              RegisteredOpModes.this.callInstanceOpModeRegistrars();
              return;
            } 
          }
        });
  }
  
  public void registerOnBotJavaOpModes() {
    lockOpModesWhile(new Runnable() {
          public void run() {
            for (OpModeMetaAndClass opModeMetaAndClass : new ArrayList(RegisteredOpModes.this.opModeClasses.values())) {
              if (opModeMetaAndClass.isOnBotJava()) {
                boolean bool;
                if (RegisteredOpModes.this.opModeClasses.get(opModeMetaAndClass.meta.name) == opModeMetaAndClass) {
                  bool = true;
                } else {
                  bool = false;
                } 
                Assert.assertTrue(bool);
                RegisteredOpModes.this.unregister(opModeMetaAndClass.meta);
              } 
            } 
            ClassManager.getInstance().processOnBotJavaClasses();
            AnnotatedOpModeClassFilter.getInstance().registerOnBotJavaClasses(RegisteredOpModes.this);
          }
        });
  }
  
  protected boolean reportIfOpModeAlreadyRegistered(OpModeMeta paramOpModeMeta) {
    if (isOpmodeRegistered(paramOpModeMeta)) {
      String str = String.format("An OpMode with the name '%s' is already registered; renaming duplicate opmode", new Object[] { paramOpModeMeta.name });
      RobotLog.ww("RegisteredOpModes", "configuration error: %s", new Object[] { str });
      RobotLog.setGlobalWarningMessage(str);
      return false;
    } 
    return true;
  }
  
  protected void unregister(final OpModeMeta meta) {
    lockOpModesWhile(new Runnable() {
          public void run() {
            RobotLog.vv("OpmodeRegistration", "unregistered {%s}", new Object[] { this.val$meta.name });
            RegisteredOpModes.this.opModeClasses.remove(meta.name);
            RegisteredOpModes.this.opModeInstances.remove(meta.name);
            Assert.assertFalse(RegisteredOpModes.this.isOpmodeRegistered(meta));
          }
        });
  }
  
  public void waitOpModesRegistered() {
    while (!this.opmodesAreRegistered)
      Thread.yield(); 
  }
  
  protected static class InstanceHolder {
    public static final RegisteredOpModes theInstance = new RegisteredOpModes();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opmode\RegisteredOpModes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */