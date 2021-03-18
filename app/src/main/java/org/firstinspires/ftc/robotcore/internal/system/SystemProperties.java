package org.firstinspires.ftc.robotcore.internal.system;

import com.qualcomm.robotcore.util.ClassUtil;
import java.lang.reflect.Method;

public class SystemProperties {
  public static final String TAG = "SystemProperties";
  
  protected static Class<?> clazzSystemProperties;
  
  protected static Method methodBooleanGet;
  
  protected static Method methodIntGet;
  
  protected static Method methodLongGet;
  
  protected static Method methodStringGetDefault;
  
  protected static Method methodStringSet;
  
  static {
    try {
      Class<?> clazz = Class.forName("android.os.SystemProperties");
      clazzSystemProperties = clazz;
      methodStringGetDefault = clazz.getMethod("get", new Class[] { String.class, String.class });
      methodStringSet = clazzSystemProperties.getMethod("set", new Class[] { String.class, String.class });
      methodIntGet = clazzSystemProperties.getMethod("getInt", new Class[] { String.class, int.class });
      methodLongGet = clazzSystemProperties.getMethod("getLong", new Class[] { String.class, long.class });
      methodBooleanGet = clazzSystemProperties.getMethod("getBoolean", new Class[] { String.class, boolean.class });
      return;
    } catch (ClassNotFoundException|NoSuchMethodException classNotFoundException) {
      return;
    } 
  }
  
  public static String get(String paramString1, String paramString2) {
    return (String)ClassUtil.invoke(null, methodStringGetDefault, new Object[] { paramString1, paramString2 });
  }
  
  public static boolean getBoolean(String paramString, boolean paramBoolean) {
    return ((Boolean)ClassUtil.invoke(null, methodBooleanGet, new Object[] { paramString, Boolean.valueOf(paramBoolean) })).booleanValue();
  }
  
  public static int getInt(String paramString, int paramInt) {
    return ((Integer)ClassUtil.invoke(null, methodIntGet, new Object[] { paramString, Integer.valueOf(paramInt) })).intValue();
  }
  
  public static long getLong(String paramString, long paramLong) {
    return ((Long)ClassUtil.invoke(null, methodLongGet, new Object[] { paramString, Long.valueOf(paramLong) })).longValue();
  }
  
  public static void set(String paramString1, String paramString2) {
    ClassUtil.invoke(null, methodStringSet, new Object[] { paramString1, paramString2 });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\SystemProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */