package org.firstinspires.ftc.robotcore.internal.opmode;

import com.qualcomm.robotcore.util.ClassUtil;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.firstinspires.ftc.robotcore.external.ExportToBlocks;

public class BlocksClassFilter implements ClassFilter {
  private static final Pattern methodLookupStringPattern = Pattern.compile("([^ ]+) ([^\\\\(]+).*");
  
  private final Map<String, Method> allMethods = new HashMap<String, Method>();
  
  private final Map<Class, Set<Method>> methodsByClass = (Map)new TreeMap<Class<?>, Set<Method>>((Comparator)new Comparator<Class>() {
        public int compare(Class param1Class1, Class param1Class2) {
          return param1Class1.getName().compareTo(param1Class2.getName());
        }
      });
  
  private BlocksClassFilter() {}
  
  private void exploreClass(Class paramClass) {
    if (getPackage(paramClass).equals("org.firstinspires.ftc.teamcode")) {
      TreeSet<Method> treeSet = new TreeSet(new Comparator<Method>() {
            public int compare(Method param1Method1, Method param1Method2) {
              int i = param1Method1.getName().compareToIgnoreCase(param1Method2.getName());
              if (i != 0)
                return i; 
              i = param1Method1.getName().compareTo(param1Method2.getName());
              if (i != 0)
                return i; 
              Class[] arrayOfClass1 = param1Method1.getParameterTypes();
              Class[] arrayOfClass2 = param1Method2.getParameterTypes();
              for (i = 0; i < arrayOfClass1.length && i < arrayOfClass2.length; i++) {
                int j = arrayOfClass1[i].getSimpleName().compareToIgnoreCase(arrayOfClass2[i].getSimpleName());
                if (j != 0)
                  return j; 
                j = arrayOfClass1[i].getSimpleName().compareTo(arrayOfClass2[i].getSimpleName());
                if (j != 0)
                  return j; 
              } 
              return arrayOfClass2.length - arrayOfClass1.length;
            }
          });
      for (Method method : ClassUtil.getLocalDeclaredMethods(paramClass)) {
        if ((method.getModifiers() & 0x9) != 9 || (method.getModifiers() & 0x400) != 0 || !method.isAnnotationPresent((Class)ExportToBlocks.class) || (method.getParameterTypes()).length > 21)
          continue; 
        treeSet.add(method);
      } 
      if (!treeSet.isEmpty()) {
        this.methodsByClass.put(paramClass, treeSet);
        for (Method method : treeSet)
          this.allMethods.put(getLookupString(method), method); 
      } 
    } 
  }
  
  public static BlocksClassFilter getInstance() {
    return InstanceHolder.theInstance;
  }
  
  public static String getLookupString(Method paramMethod) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramMethod.getDeclaringClass().getName());
    stringBuilder.append(" ");
    stringBuilder.append(paramMethod.getName());
    stringBuilder.append("(");
    Class[] arrayOfClass = paramMethod.getParameterTypes();
    int j = arrayOfClass.length;
    String str = "";
    int i = 0;
    while (i < j) {
      Class clazz = arrayOfClass[i];
      stringBuilder.append(str);
      stringBuilder.append(clazz.getName());
      i++;
      str = ",";
    } 
    stringBuilder.append(") ");
    stringBuilder.append(paramMethod.getReturnType().getName());
    return stringBuilder.toString();
  }
  
  private static String getPackage(Class<?> paramClass) {
    Package package_ = paramClass.getPackage();
    if (package_ != null)
      return package_.getName(); 
    while (paramClass.getEnclosingClass() != null)
      paramClass = paramClass.getEnclosingClass(); 
    String str = paramClass.getName();
    int i = str.lastIndexOf('.');
    return (i == -1) ? "" : str.substring(0, i);
  }
  
  public static String getUserVisibleName(String paramString) {
    Matcher matcher = methodLookupStringPattern.matcher(paramString);
    if (matcher.find()) {
      String str = matcher.group(1);
      int i = str.lastIndexOf(".");
      paramString = str;
      if (i != -1)
        paramString = str.substring(i + 1); 
      paramString = paramString.replace('$', '.');
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append(".");
      stringBuilder.append(matcher.group(2));
      return stringBuilder.toString();
    } 
    return "";
  }
  
  public void filterAllClassesComplete() {}
  
  public void filterAllClassesStart() {
    this.methodsByClass.clear();
    this.allMethods.clear();
  }
  
  public void filterClass(Class paramClass) {
    exploreClass(paramClass);
  }
  
  public void filterOnBotJavaClass(Class paramClass) {
    exploreClass(paramClass);
  }
  
  public void filterOnBotJavaClassesComplete() {}
  
  public void filterOnBotJavaClassesStart() {
    Iterator<Map.Entry> iterator = this.methodsByClass.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = iterator.next();
      if (OnBotJavaDeterminer.isOnBotJava((Class)entry.getKey())) {
        for (Method method : entry.getValue())
          this.allMethods.remove(getLookupString(method)); 
        iterator.remove();
      } 
    } 
  }
  
  public Method findMethod(String paramString) {
    return this.allMethods.get(paramString);
  }
  
  public Map<Class, Set<Method>> getMethodsByClass() {
    return this.methodsByClass;
  }
  
  private static class InstanceHolder {
    public static BlocksClassFilter theInstance = new BlocksClassFilter();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opmode\BlocksClassFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */