package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.R;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.firstinspires.ftc.robotcore.external.Predicate;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class ClassUtil {
  public static final String TAG = ClassUtil.class.getSimpleName();
  
  public static String decodeStringRes(String paramString) {
    String str = paramString;
    if (paramString.startsWith("@string/")) {
      int i = getStringResId(paramString.substring(8), R.string.class);
      str = AppUtil.getDefContext().getString(i);
    } 
    return str;
  }
  
  protected static Class findClass(String paramString) {
    try {
      return Class.forName(paramString);
    } catch (ClassNotFoundException classNotFoundException) {
      RobotLog.ee(TAG, classNotFoundException, "class not found: %s", new Object[] { paramString });
      throw new RuntimeException("class not found");
    } 
  }
  
  public static List<Field> getAllDeclaredFields(Class<?> paramClass) {
    ArrayList<Field> arrayList = new ArrayList();
    Class clazz = paramClass.getSuperclass();
    if (clazz != null)
      arrayList.addAll(getAllDeclaredFields(clazz)); 
    arrayList.addAll(getLocalDeclaredFields(paramClass));
    return arrayList;
  }
  
  public static List<Method> getAllDeclaredMethods(Class<?> paramClass) {
    ArrayList<Method> arrayList = new ArrayList();
    Class clazz = paramClass.getSuperclass();
    if (clazz != null)
      arrayList.addAll(getAllDeclaredMethods(clazz)); 
    arrayList.addAll(getLocalDeclaredMethods(paramClass));
    return arrayList;
  }
  
  public static List<Constructor> getDeclaredConstructors(Class<?> paramClass) {
    Constructor[] arrayOfConstructor;
    try {
      arrayOfConstructor = (Constructor[])paramClass.getDeclaredConstructors();
    } catch (Exception|LinkageError exception) {
      arrayOfConstructor = new Constructor[0];
    } 
    LinkedList<Constructor> linkedList = new LinkedList();
    linkedList.addAll(Arrays.asList(arrayOfConstructor));
    return linkedList;
  }
  
  public static Field getDeclaredField(Class paramClass, String paramString) {
    try {
      Field field = paramClass.getDeclaredField(paramString);
      field.setAccessible(true);
      return field;
    } catch (LinkageError linkageError) {
      return null;
    } catch (NoSuchFieldException noSuchFieldException) {
      Class clazz = linkageError.getSuperclass();
      if (clazz != null)
        return getDeclaredField(clazz, paramString); 
      RobotLog.ee("NetDiscover_wifiDirectAgent", noSuchFieldException, "field not found: %s.%s", new Object[] { linkageError.getName(), paramString });
      return null;
    } 
  }
  
  public static Method getDeclaredMethod(Class paramClass, String paramString, Class<?>... paramVarArgs) {
    try {
      Method method = paramClass.getMethod(paramString, paramVarArgs);
      method.setAccessible(true);
      return method;
    } catch (LinkageError linkageError) {
      return null;
    } catch (NoSuchMethodException noSuchMethodException) {
      Class clazz = linkageError.getSuperclass();
      if (clazz != null)
        return getDeclaredMethod(clazz, paramString, paramVarArgs); 
      RobotLog.ee("NetDiscover_wifiDirectAgent", noSuchMethodException, "method not found: %s", new Object[] { paramString });
      return null;
    } 
  }
  
  public static List<Field> getLocalDeclaredFields(Class<?> paramClass) {
    Field[] arrayOfField;
    try {
      arrayOfField = paramClass.getDeclaredFields();
    } catch (Exception|LinkageError exception) {
      arrayOfField = new Field[0];
    } 
    return Arrays.asList(arrayOfField);
  }
  
  public static List<Method> getLocalDeclaredMethods(Class<?> paramClass) {
    Method[] arrayOfMethod;
    try {
      arrayOfMethod = paramClass.getDeclaredMethods();
    } catch (Exception|LinkageError exception) {
      arrayOfMethod = new Method[0];
    } 
    return Arrays.asList(arrayOfMethod);
  }
  
  public static int getStringResId(String paramString, Class<?> paramClass) {
    return AppUtil.getDefContext().getResources().getIdentifier(paramString, "string", AppUtil.getDefContext().getPackageName());
  }
  
  public static boolean inheritsFrom(Class paramClass1, Class paramClass2) {
    while (true) {
      int i = 0;
      if (paramClass1 != null) {
        if (paramClass1 == paramClass2)
          return true; 
        if (paramClass2.isInterface()) {
          Class[] arrayOfClass = paramClass1.getInterfaces();
          int j = arrayOfClass.length;
          while (i < j) {
            if (inheritsFrom(arrayOfClass[i], paramClass2))
              return true; 
            i++;
          } 
        } 
        paramClass1 = paramClass1.getSuperclass();
        continue;
      } 
      return false;
    } 
  }
  
  public static Object invoke(Object paramObject, Method paramMethod, Object... paramVarArgs) {
    try {
      return paramMethod.invoke(paramObject, paramVarArgs);
    } catch (InvocationTargetException invocationTargetException) {
      Throwable throwable = invocationTargetException.getCause();
      if (throwable instanceof RuntimeException)
        throw (RuntimeException)throwable; 
      throw new RuntimeException(String.format("exception in %s#%s", new Object[] { paramMethod.getDeclaringClass().getSimpleName(), paramMethod.getName() }), throwable);
    } catch (IllegalAccessException illegalAccessException) {
      throw new RuntimeException(String.format("access denied in %s#%s", new Object[] { paramMethod.getDeclaringClass().getSimpleName(), paramMethod.getName() }), illegalAccessException);
    } 
  }
  
  public static long memoryAddressFrom(MappedByteBuffer paramMappedByteBuffer) {
    try {
      return ((Long)MappedByteBufferInfo.addressField.get(object)).longValue();
    } finally {
      paramMappedByteBuffer = null;
      RobotLog.ee(TAG, (Throwable)paramMappedByteBuffer, "internal error: can't extract address from MappedByteBuffer");
    } 
  }
  
  public static boolean searchInheritance(Class paramClass, Predicate<Class<?>> paramPredicate) {
    return searchInheritance(paramClass, paramPredicate, new HashSet<Class<?>>());
  }
  
  private static boolean searchInheritance(Class<?> paramClass, Predicate<Class<?>> paramPredicate, Set<Class<?>> paramSet) {
    while (true) {
      int i = 0;
      if (paramClass != null) {
        if (paramSet.contains(paramClass))
          return false; 
        paramSet.add(paramClass);
        if (paramPredicate.test(paramClass))
          return true; 
        Class[] arrayOfClass = paramClass.getInterfaces();
        int j = arrayOfClass.length;
        while (i < j) {
          if (searchInheritance(arrayOfClass[i], paramPredicate, paramSet))
            return true; 
          i++;
        } 
        paramClass = paramClass.getSuperclass();
        continue;
      } 
      return false;
    } 
  }
  
  protected static class MappedByteBufferInfo {
    public static Field addressField;
    
    public static Field blockField;
    
    static {
      Field field = ClassUtil.getDeclaredField(MappedByteBuffer.class, "block");
      blockField = field;
      addressField = ClassUtil.getDeclaredField(field.getType(), "address");
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\ClassUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */