package org.firstinspires.ftc.robotcore.internal.opmode;

import android.content.Context;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.Util;
import dalvik.system.DexFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class ClassManager {
  private static final String TAG = "ClassManager";
  
  private final boolean DEBUG = false;
  
  private ClassLoader classLoader = null;
  
  private Context context;
  
  private DexFile dexFile;
  
  private List<ClassFilter> filters;
  
  private OnBotJavaHelper onBotJavaHelper = null;
  
  private List<String> packagesAndClassesToIgnore;
  
  private ClassManager() {
    try {
      this.context = (Context)AppUtil.getInstance().getApplication();
      this.dexFile = new DexFile(this.context.getPackageCodePath());
      this.filters = new LinkedList<ClassFilter>();
      clearIgnoredList();
      return;
    } catch (Exception exception) {
      throw AppUtil.getInstance().unreachable("ClassManager", exception);
    } 
  }
  
  private List<String> getAllClassNames() {
    ArrayList<String> arrayList = new ArrayList(Collections.list(this.dexFile.entries()));
    arrayList.addAll(InstantRunHelper.getAllClassNames(this.context));
    OnBotJavaHelper onBotJavaHelper = this.onBotJavaHelper;
    if (onBotJavaHelper != null) {
      arrayList.addAll(onBotJavaHelper.getOnBotJavaClassNames());
      setClassLoader(this.onBotJavaHelper.getOnBotJavaClassLoader());
    } 
    return arrayList;
  }
  
  public static ClassManager getInstance() {
    return InstanceHolder.theInstance;
  }
  
  protected List<Class> classNamesToClasses(Collection<String> paramCollection) {
    LinkedList<Class<?>> linkedList = new LinkedList();
    try {
      Iterator<String> iterator = paramCollection.iterator();
      while (true) {
        if (iterator.hasNext()) {
          boolean bool;
          String str2 = iterator.next();
          Iterator<String> iterator1 = this.packagesAndClassesToIgnore.iterator();
          while (true) {
            if (iterator1.hasNext()) {
              boolean bool1 = Util.isPrefixOf(iterator1.next(), str2);
              if (bool1) {
                boolean bool2 = true;
                break;
              } 
              continue;
            } 
            bool = false;
            break;
          } 
          if (bool)
            continue; 
          try {
            Class<?> clazz;
            if (this.classLoader == null) {
              clazz = Class.forName(str2, false, getClass().getClassLoader());
            } else {
              clazz = Class.forName(str2, false, this.classLoader);
            } 
            linkedList.add(clazz);
            continue;
          } catch (NoClassDefFoundError noClassDefFoundError) {
          
          } catch (ClassNotFoundException classNotFoundException) {}
          if (logClassNotFound(str2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(" ");
            stringBuilder.append(classNotFoundException.toString());
            RobotLog.ww("ClassManager", classNotFoundException, stringBuilder.toString());
          } 
          String str1 = str2;
          if (str2.contains("$"))
            str1 = str2.substring(0, str2.indexOf("$")); 
          this.packagesAndClassesToIgnore.add(str1);
          continue;
        } 
        return (List)linkedList;
      } 
    } finally {
      OnBotJavaHelper onBotJavaHelper = this.onBotJavaHelper;
      if (onBotJavaHelper != null)
        onBotJavaHelper.close(this.classLoader); 
    } 
  }
  
  protected void clearIgnoredList() {
    ArrayList<String> arrayList = new ArrayList();
    this.packagesAndClassesToIgnore = arrayList;
    arrayList.addAll(Arrays.asList(new String[] { "com.android.dex", "com.google", "com.sun.tools", "gnu.kawa.swingviews", "io.netty", "javax.tools", "kawa", "org.firstinspires.ftc.robotcore.internal.android", "android.support.v4", "androidx" }));
  }
  
  protected void clearOnBotJava() {}
  
  protected boolean logClassNotFound(String paramString) {
    for (int i = 0; i < 1; i++) {
      (new String[1])[0] = "com.vuforia.";
      if (paramString.startsWith((new String[1])[i]))
        return false; 
    } 
    return true;
  }
  
  public void processAllClasses() {
    clearIgnoredList();
    List<Class> list = classNamesToClasses(getAllClassNames());
    for (ClassFilter classFilter : this.filters) {
      classFilter.filterAllClassesStart();
      Iterator<Class> iterator = list.iterator();
      while (iterator.hasNext())
        classFilter.filterClass(iterator.next()); 
      classFilter.filterAllClassesComplete();
    } 
  }
  
  public void processOnBotJavaClasses() {
    if (this.onBotJavaHelper == null)
      return; 
    clearIgnoredList();
    Set<String> set = this.onBotJavaHelper.getOnBotJavaClassNames();
    setClassLoader(this.onBotJavaHelper.getOnBotJavaClassLoader());
    List<Class> list = classNamesToClasses(set);
    for (ClassFilter classFilter : this.filters) {
      classFilter.filterOnBotJavaClassesStart();
      for (Class clazz : list) {
        Assert.assertTrue(OnBotJavaDeterminer.isOnBotJava(clazz), "class %s isn't OnBotJava: loader=%s", new Object[] { clazz.getSimpleName(), clazz.getClassLoader().getClass().getSimpleName() });
        classFilter.filterOnBotJavaClass(clazz);
      } 
      classFilter.filterOnBotJavaClassesComplete();
    } 
  }
  
  public void registerFilter(ClassFilter paramClassFilter) {
    this.filters.add(paramClassFilter);
  }
  
  protected void setClassLoader(ClassLoader paramClassLoader) {
    this.classLoader = paramClassLoader;
  }
  
  public void setOnBotJavaClassHelper(OnBotJavaHelper paramOnBotJavaHelper) {
    this.onBotJavaHelper = paramOnBotJavaHelper;
  }
  
  private static class InstanceHolder {
    public static ClassManager theInstance = new ClassManager();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opmode\ClassManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */