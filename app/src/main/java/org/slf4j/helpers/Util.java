package org.slf4j.helpers;

import java.io.PrintStream;

public final class Util {
  private static ClassContextSecurityManager SECURITY_MANAGER;
  
  private static boolean SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = false;
  
  public static Class<?> getCallingClass() {
    ClassContextSecurityManager classContextSecurityManager = getSecurityManager();
    if (classContextSecurityManager == null)
      return null; 
    Class[] arrayOfClass = classContextSecurityManager.getClassContext();
    String str = Util.class.getName();
    int i;
    for (i = 0; i < arrayOfClass.length && !str.equals(arrayOfClass[i].getName()); i++);
    if (i < arrayOfClass.length) {
      i += 2;
      if (i < arrayOfClass.length)
        return arrayOfClass[i]; 
    } 
    throw new IllegalStateException("Failed to find org.slf4j.helpers.Util or its caller in the stack; this should not happen");
  }
  
  private static ClassContextSecurityManager getSecurityManager() {
    ClassContextSecurityManager classContextSecurityManager = SECURITY_MANAGER;
    if (classContextSecurityManager != null)
      return classContextSecurityManager; 
    if (SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED)
      return null; 
    classContextSecurityManager = safeCreateSecurityManager();
    SECURITY_MANAGER = classContextSecurityManager;
    SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = true;
    return classContextSecurityManager;
  }
  
  public static final void report(String paramString) {
    PrintStream printStream = System.err;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SLF4J: ");
    stringBuilder.append(paramString);
    printStream.println(stringBuilder.toString());
  }
  
  public static final void report(String paramString, Throwable paramThrowable) {
    System.err.println(paramString);
    System.err.println("Reported exception:");
    paramThrowable.printStackTrace();
  }
  
  private static ClassContextSecurityManager safeCreateSecurityManager() {
    try {
      return new ClassContextSecurityManager();
    } catch (SecurityException securityException) {
      return null;
    } 
  }
  
  public static boolean safeGetBooleanSystemProperty(String paramString) {
    paramString = safeGetSystemProperty(paramString);
    return (paramString == null) ? false : paramString.equalsIgnoreCase("true");
  }
  
  public static String safeGetSystemProperty(String paramString) {
    if (paramString != null)
      try {
        return System.getProperty(paramString);
      } catch (SecurityException securityException) {
        return null;
      }  
    throw new IllegalArgumentException("null input");
  }
  
  private static final class ClassContextSecurityManager extends SecurityManager {
    private ClassContextSecurityManager() {}
    
    protected Class<?>[] getClassContext() {
      return super.getClassContext();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\helpers\Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */