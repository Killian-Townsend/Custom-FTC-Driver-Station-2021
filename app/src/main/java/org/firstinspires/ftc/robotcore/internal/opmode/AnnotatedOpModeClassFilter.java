package org.firstinspires.ftc.robotcore.internal.opmode;

import android.content.Context;
import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.AnnotatedOpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.DuplicateNameException;
import com.qualcomm.robotcore.util.ClassUtil;
import com.qualcomm.robotcore.util.RobotLog;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.firstinspires.ftc.robotcore.external.Predicate;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class AnnotatedOpModeClassFilter implements ClassFilter {
  public static final String TAG = "OpmodeRegistration";
  
  private final HashMap<Class, OpModeMetaAndClass> classNameOverrides = new HashMap<Class<?>, OpModeMetaAndClass>();
  
  private Context context = AppUtil.getDefContext();
  
  private final String defaultOpModeGroupName = "$$$$$$$";
  
  private final Set<Class<OpMode>> filteredAnnotatedOpModeClasses = new HashSet<Class<OpMode>>();
  
  private final List<OpModeMetaAndClass> knownOpModes = new ArrayList<OpModeMetaAndClass>();
  
  private final List<OpModeMetaAndClass> newOpModes = new ArrayList<OpModeMetaAndClass>();
  
  private RegisteredOpModes registeredOpModes = null;
  
  private final Set<Method> registrarMethods = new HashSet<Method>();
  
  private AnnotatedOpModeClassFilter() {}
  
  private boolean addAnnotatedOpMode(Class<OpMode> paramClass) {
    if (paramClass.isAnnotationPresent((Class)TeleOp.class)) {
      String str = ((TeleOp)paramClass.<TeleOp>getAnnotation(TeleOp.class)).group();
      return addOpModeWithGroupName(paramClass, OpModeMeta.Flavor.TELEOP, str);
    } 
    if (paramClass.isAnnotationPresent((Class)Autonomous.class)) {
      String str = ((Autonomous)paramClass.<Autonomous>getAnnotation(Autonomous.class)).group();
      return addOpModeWithGroupName(paramClass, OpModeMeta.Flavor.AUTONOMOUS, str);
    } 
    return false;
  }
  
  private boolean addOpModeWithGroupName(Class<OpMode> paramClass, OpModeMeta.Flavor paramFlavor, String paramString) {
    OpModeMetaAndClass opModeMetaAndClass = new OpModeMetaAndClass(new OpModeMeta(paramFlavor, paramString), paramClass);
    return paramString.equals("") ? addToOpModeGroup("$$$$$$$", opModeMetaAndClass) : addToOpModeGroup(paramString, opModeMetaAndClass);
  }
  
  private boolean addToOpModeGroup(String paramString, OpModeMetaAndClass paramOpModeMetaAndClass) {
    Class<OpMode> clazz = paramOpModeMetaAndClass.clazz;
    OpModeMetaAndClass opModeMetaAndClass = new OpModeMetaAndClass(OpModeMeta.forGroup(paramString, paramOpModeMetaAndClass.meta), clazz);
    if (!isKnown(clazz)) {
      this.knownOpModes.add(opModeMetaAndClass);
      this.newOpModes.add(opModeMetaAndClass);
      return true;
    } 
    return false;
  }
  
  private boolean addUserNamedOpMode(Class<OpMode> paramClass, OpModeMeta paramOpModeMeta) {
    OpModeMetaAndClass opModeMetaAndClass = new OpModeMetaAndClass(paramOpModeMeta, paramClass);
    this.classNameOverrides.put(paramClass, opModeMetaAndClass);
    return addToOpModeGroup("$$$$$$$", opModeMetaAndClass);
  }
  
  private void callOpModeRegistrarMethods(Predicate<Class> paramPredicate) {
    OpModeRegistrarMethodManager opModeRegistrarMethodManager = new OpModeRegistrarMethodManager();
    Iterator<Method> iterator = this.registrarMethods.iterator();
    while (true) {
      if (iterator.hasNext()) {
        Method method = iterator.next();
        if (paramPredicate.test(method.getDeclaringClass()))
          try {
            if (getParameterCount(method) == 1) {
              method.invoke((Object)null, new Object[] { opModeRegistrarMethodManager });
              continue;
            } 
            if (getParameterCount(method) == 2)
              method.invoke((Object)null, new Object[] { this.context, opModeRegistrarMethodManager }); 
          } catch (Exception exception) {} 
        continue;
      } 
      return;
    } 
  }
  
  public static AnnotatedOpModeClassFilter getInstance() {
    return InstanceHolder.theInstance;
  }
  
  private String getOpModeName(Class<OpMode> paramClass) {
    String str1;
    if (this.classNameOverrides.containsKey(paramClass)) {
      str1 = ((OpModeMetaAndClass)this.classNameOverrides.get(paramClass)).meta.name;
    } else if (paramClass.isAnnotationPresent((Class)TeleOp.class)) {
      str1 = ((TeleOp)paramClass.<TeleOp>getAnnotation(TeleOp.class)).name();
    } else if (paramClass.isAnnotationPresent((Class)Autonomous.class)) {
      str1 = ((Autonomous)paramClass.<Autonomous>getAnnotation(Autonomous.class)).name();
    } else {
      str1 = paramClass.getSimpleName();
    } 
    String str2 = str1;
    if (str1.trim().equals(""))
      str2 = paramClass.getSimpleName(); 
    return str2;
  }
  
  private String getOpModeName(OpModeMetaAndClass paramOpModeMetaAndClass) {
    return getOpModeName(paramOpModeMetaAndClass.clazz);
  }
  
  private int getParameterCount(Method paramMethod) {
    return (paramMethod.getParameterTypes()).length;
  }
  
  private boolean isKnown(Class<OpMode> paramClass) {
    Iterator<OpModeMetaAndClass> iterator = this.knownOpModes.iterator();
    while (iterator.hasNext()) {
      if (((OpModeMetaAndClass)iterator.next()).clazz == paramClass)
        return true; 
    } 
    return false;
  }
  
  private boolean isLegalOpModeName(String paramString) {
    return (paramString == null) ? false : (!paramString.equals("$Stop$Robot$") ? (!paramString.trim().equals("")) : false);
  }
  
  private boolean isOpMode(Class paramClass) {
    return ClassUtil.inheritsFrom(paramClass, OpMode.class);
  }
  
  boolean checkOpModeClassConstraints(Class<OpMode> paramClass, String paramString) {
    if (!isOpMode(paramClass)) {
      reportOpModeConfigurationError("'%s' class doesn't inherit from the class 'OpMode'", new Object[] { paramClass.getSimpleName() });
      return false;
    } 
    if (!Modifier.isPublic(paramClass.getModifiers())) {
      reportOpModeConfigurationError("'%s' class is not declared 'public'", new Object[] { paramClass.getSimpleName() });
      return false;
    } 
    String str = paramString;
    if (paramString == null)
      str = getOpModeName(paramClass); 
    if (!isLegalOpModeName(str)) {
      reportOpModeConfigurationError("\"%s\" is not a legal OpMode name", new Object[] { str });
      return false;
    } 
    return true;
  }
  
  public void filterAllClassesComplete() {}
  
  public void filterAllClassesStart() {
    this.newOpModes.clear();
    this.classNameOverrides.clear();
    this.knownOpModes.clear();
    this.filteredAnnotatedOpModeClasses.clear();
    this.registrarMethods.clear();
  }
  
  public void filterClass(Class<OpMode> paramClass) {
    filterOpModeRegistrarMethods(paramClass);
    boolean bool1 = paramClass.isAnnotationPresent((Class)TeleOp.class);
    boolean bool2 = paramClass.isAnnotationPresent((Class)Autonomous.class);
    if (!bool1 && !bool2)
      return; 
    if (bool1 && bool2) {
      reportOpModeConfigurationError("'%s' class is annotated both as 'TeleOp' and 'Autonomous'; please choose at most one", new Object[] { paramClass.getSimpleName() });
      return;
    } 
    if (!checkOpModeClassConstraints(paramClass, null))
      return; 
    if (paramClass.isAnnotationPresent((Class)Disabled.class))
      return; 
    this.filteredAnnotatedOpModeClasses.add(paramClass);
  }
  
  public void filterOnBotJavaClass(Class paramClass) {
    filterClass(paramClass);
  }
  
  public void filterOnBotJavaClassesComplete() {}
  
  public void filterOnBotJavaClassesStart() {
    this.newOpModes.clear();
    for (OpModeMetaAndClass opModeMetaAndClass : new ArrayList(this.classNameOverrides.values())) {
      if (opModeMetaAndClass.isOnBotJava())
        this.classNameOverrides.remove(opModeMetaAndClass.clazz); 
    } 
    for (OpModeMetaAndClass opModeMetaAndClass : new ArrayList(this.knownOpModes)) {
      if (opModeMetaAndClass.isOnBotJava())
        this.knownOpModes.remove(opModeMetaAndClass); 
    } 
    for (Class clazz : new ArrayList(this.filteredAnnotatedOpModeClasses)) {
      if (OnBotJavaDeterminer.isOnBotJava(clazz))
        this.filteredAnnotatedOpModeClasses.remove(clazz); 
    } 
    for (Method method : new ArrayList(this.registrarMethods)) {
      if (OnBotJavaDeterminer.isOnBotJava(method.getDeclaringClass()))
        this.registrarMethods.remove(method); 
    } 
  }
  
  void filterOpModeRegistrarMethods(Class paramClass) {
    for (Method method : ClassUtil.getLocalDeclaredMethods(paramClass)) {
      if ((method.getModifiers() & 0x9) == 9 && (method.getModifiers() & 0x400) == 0 && method.isAnnotationPresent((Class)OpModeRegistrar.class) && (getParameterCount(method) == 1 || getParameterCount(method) == 2))
        this.registrarMethods.add(method); 
    } 
  }
  
  void registerAllClasses(RegisteredOpModes paramRegisteredOpModes) {
    this.registeredOpModes = paramRegisteredOpModes;
    try {
      callOpModeRegistrarMethods(new Predicate<Class>() {
            public boolean test(Class param1Class) {
              return true;
            }
          });
      Iterator<Class<OpMode>> iterator = this.filteredAnnotatedOpModeClasses.iterator();
      while (iterator.hasNext())
        addAnnotatedOpMode(iterator.next()); 
      iterator = (Iterator)this.newOpModes.iterator();
      while (true) {
        if (iterator.hasNext()) {
          OpModeMetaAndClass opModeMetaAndClass = (OpModeMetaAndClass)iterator.next();
          String str = getOpModeName(opModeMetaAndClass);
          try {
            this.registeredOpModes.register(OpModeMeta.forName(str, opModeMetaAndClass.meta), opModeMetaAndClass.clazz);
          } catch (DuplicateNameException duplicateNameException) {
            String str1 = resolveDuplicateName(opModeMetaAndClass);
            this.registeredOpModes.register(OpModeMeta.forName(str1, opModeMetaAndClass.meta), opModeMetaAndClass.clazz);
          } 
          continue;
        } 
        iterator = (Iterator)this.knownOpModes.iterator();
        while (true) {
          if (iterator.hasNext()) {
            OpModeMetaAndClass opModeMetaAndClass = (OpModeMetaAndClass)iterator.next();
            if (!this.newOpModes.contains(opModeMetaAndClass)) {
              String str = getOpModeName(opModeMetaAndClass);
              try {
                this.registeredOpModes.register(OpModeMeta.forName(str, opModeMetaAndClass.meta), opModeMetaAndClass.clazz);
              } catch (DuplicateNameException duplicateNameException) {
                String str1 = resolveDuplicateName(opModeMetaAndClass);
                this.registeredOpModes.register(OpModeMeta.forName(str1, opModeMetaAndClass.meta), opModeMetaAndClass.clazz);
              } 
            } 
            continue;
          } 
          return;
        } 
        break;
      } 
    } finally {
      this.registeredOpModes = null;
    } 
  }
  
  public void registerOnBotJavaClasses(RegisteredOpModes paramRegisteredOpModes) {
    this.registeredOpModes = paramRegisteredOpModes;
    try {
      callOpModeRegistrarMethods(new Predicate<Class>() {
            public boolean test(Class param1Class) {
              return OnBotJavaDeterminer.isOnBotJava(param1Class);
            }
          });
      for (Class<OpMode> clazz : this.filteredAnnotatedOpModeClasses) {
        if (OnBotJavaDeterminer.isOnBotJava(clazz))
          addAnnotatedOpMode(clazz); 
      } 
      for (OpModeMetaAndClass opModeMetaAndClass : this.newOpModes) {
        String str = getOpModeName(opModeMetaAndClass);
        this.registeredOpModes.register(OpModeMeta.forName(str, opModeMetaAndClass.meta), opModeMetaAndClass.clazz);
      } 
      return;
    } finally {
      this.registeredOpModes = null;
    } 
  }
  
  void reportOpModeConfigurationError(String paramString, Object... paramVarArgs) {
    paramString = String.format(paramString, paramVarArgs);
    Log.w("OpmodeRegistration", String.format("configuration error: %s", new Object[] { paramString }));
    RobotLog.setGlobalErrorMsg(paramString);
  }
  
  protected String resolveDuplicateName(OpModeMetaAndClass paramOpModeMetaAndClass) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getOpModeName(paramOpModeMetaAndClass));
    stringBuilder.append("-");
    stringBuilder.append(paramOpModeMetaAndClass.clazz.getSimpleName());
    return stringBuilder.toString();
  }
  
  private static class InstanceHolder {
    public static AnnotatedOpModeClassFilter theInstance = new AnnotatedOpModeClassFilter();
  }
  
  class OpModeRegistrarMethodManager implements AnnotatedOpModeManager {
    public void register(Class param1Class) {
      if (AnnotatedOpModeClassFilter.this.checkOpModeClassConstraints(param1Class, null))
        AnnotatedOpModeClassFilter.this.addAnnotatedOpMode(param1Class); 
    }
    
    public void register(String param1String, OpMode param1OpMode) {
      AnnotatedOpModeClassFilter.this.registeredOpModes.register(param1String, param1OpMode);
      RobotLog.dd("OpmodeRegistration", String.format("registered instance {%s} as {%s}", new Object[] { param1OpMode.toString(), param1String }));
    }
    
    public void register(String param1String, Class param1Class) {
      if (AnnotatedOpModeClassFilter.this.checkOpModeClassConstraints(param1Class, param1String))
        AnnotatedOpModeClassFilter.this.addUserNamedOpMode(param1Class, new OpModeMeta(param1String)); 
    }
    
    public void register(OpModeMeta param1OpModeMeta, OpMode param1OpMode) {
      AnnotatedOpModeClassFilter.this.registeredOpModes.register(param1OpModeMeta, param1OpMode);
      RobotLog.dd("OpmodeRegistration", String.format("registered instance {%s} as {%s}", new Object[] { param1OpMode.toString(), param1OpModeMeta.name }));
    }
    
    public void register(OpModeMeta param1OpModeMeta, Class param1Class) {
      if (AnnotatedOpModeClassFilter.this.checkOpModeClassConstraints(param1Class, param1OpModeMeta.name))
        AnnotatedOpModeClassFilter.this.addUserNamedOpMode(param1Class, param1OpModeMeta); 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opmode\AnnotatedOpModeClassFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */