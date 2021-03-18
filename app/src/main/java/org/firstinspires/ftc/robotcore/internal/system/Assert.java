package org.firstinspires.ftc.robotcore.internal.system;

import com.qualcomm.robotcore.util.RobotLog;

public class Assert {
  public static final String TAG = "Assert";
  
  public static void assertEquals(int paramInt1, int paramInt2) {
    if (paramInt1 != paramInt2)
      assertFailed(); 
  }
  
  public static void assertFailed() {
    try {
      throw new RuntimeException("assertion failed");
    } catch (Exception exception) {
      RobotLog.aa("Assert", exception, "assertion failed");
      return;
    } 
  }
  
  public static void assertFailed(String paramString, Object[] paramArrayOfObject) {
    paramString = String.format(paramString, paramArrayOfObject);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("assertion failed: ");
    stringBuilder.append(paramString);
    paramString = stringBuilder.toString();
    try {
      throw new RuntimeException(paramString);
    } catch (Exception exception) {
      RobotLog.aa("Assert", exception, paramString);
      return;
    } 
  }
  
  public static void assertFalse(boolean paramBoolean) {
    if (paramBoolean)
      assertFailed(); 
  }
  
  public static void assertFalse(boolean paramBoolean, String paramString, Object... paramVarArgs) {
    if (paramBoolean)
      assertFailed(paramString, paramVarArgs); 
  }
  
  public static void assertNotNull(Object paramObject) {
    if (paramObject == null)
      assertFailed(); 
  }
  
  public static void assertNotNull(Object paramObject, String paramString, Object... paramVarArgs) {
    if (paramObject == null)
      assertFailed(paramString, paramVarArgs); 
  }
  
  public static void assertNull(Object paramObject) {
    if (paramObject != null)
      assertFailed(); 
  }
  
  public static void assertNull(Object paramObject, String paramString, Object... paramVarArgs) {
    if (paramObject != null)
      assertFailed(paramString, paramVarArgs); 
  }
  
  public static void assertTrue(boolean paramBoolean) {
    if (!paramBoolean)
      assertFailed(); 
  }
  
  public static void assertTrue(boolean paramBoolean, String paramString, Object... paramVarArgs) {
    if (!paramBoolean)
      assertFailed(paramString, paramVarArgs); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\Assert.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */