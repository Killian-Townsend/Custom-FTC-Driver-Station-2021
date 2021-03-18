package com.vuforia.ar.pl;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import java.lang.reflect.Method;

public class SystemTools {
  public static final int AR_DEVICE_ORIENTATION_0 = 268455954;
  
  public static final int AR_DEVICE_ORIENTATION_180 = 268455955;
  
  public static final int AR_DEVICE_ORIENTATION_270 = 268455957;
  
  public static final int AR_DEVICE_ORIENTATION_90 = 268455956;
  
  public static final int AR_DEVICE_ORIENTATION_UNKNOWN = 268455952;
  
  public static final int AR_ERROR_INVALID_ENUM = 3;
  
  public static final int AR_ERROR_INVALID_HANDLE = 4;
  
  public static final int AR_ERROR_INVALID_OPERATION = 5;
  
  public static final int AR_ERROR_INVALID_VALUE = 2;
  
  public static final int AR_ERROR_NONE = 0;
  
  public static final int AR_ERROR_OPERATION_CANCELED = 7;
  
  public static final int AR_ERROR_OPERATION_FAILED = 6;
  
  public static final int AR_ERROR_OPERATION_TIMEOUT = 8;
  
  public static final int AR_ERROR_UNKNOWN = 1;
  
  public static final int AR_RENDERING_TEXTURE_ROTATION_AUTO = 268455953;
  
  public static final int AR_RENDERING_TEXTURE_ROTATION_LANDSCAPE_LEFT = 268455956;
  
  public static final int AR_RENDERING_TEXTURE_ROTATION_LANDSCAPE_RIGHT = 268455957;
  
  public static final int AR_RENDERING_TEXTURE_ROTATION_PORTRAIT = 268455954;
  
  public static final int AR_RENDERING_TEXTURE_ROTATION_PORTRAIT_UPSIDEDOWN = 268455955;
  
  public static final int AR_VIDEOTEXTURE_ROTATION_UNKNOWN = 268455952;
  
  private static final String MODULENAME = "SystemTools";
  
  public static boolean checkMinimumApiLevel(int paramInt) {
    return (Build.VERSION.SDK_INT >= paramInt);
  }
  
  public static native Activity getActivityFromNative();
  
  public static int getActivityOrientation(Activity paramActivity) {
    if (paramActivity == null)
      return 268455952; 
    Configuration configuration = paramActivity.getResources().getConfiguration();
    Display display = ((WindowManager)paramActivity.getSystemService("window")).getDefaultDisplay();
    if (checkMinimumApiLevel(8)) {
      null = display.getRotation();
    } else {
      null = display.getOrientation();
    } 
    int i = configuration.orientation;
    if (i != 1)
      if (i != 2) {
        if (i != 3)
          return 268455952; 
      } else {
        return (null == 0 || null == 1) ? 268455956 : 268455957;
      }  
    return (null == 0 || null == 3) ? 268455954 : 268455955;
  }
  
  public static int[] getActivitySize(Activity paramActivity) {
    int[] arrayOfInt;
    Activity activity = null;
    if (paramActivity == null)
      return null; 
    DisplayMetrics displayMetrics = new DisplayMetrics();
    paramActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    int i = displayMetrics.widthPixels;
    int j = displayMetrics.heightPixels;
    paramActivity = activity;
    if (i > 0) {
      paramActivity = activity;
      if (j > 0) {
        arrayOfInt = new int[2];
        arrayOfInt[0] = i;
        arrayOfInt[1] = j;
      } 
    } 
    return arrayOfInt;
  }
  
  public static int getDeviceOrientation(Activity paramActivity) {
    int i;
    int j = 268455952;
    if (paramActivity == null)
      return 268455952; 
    paramActivity.getResources().getConfiguration();
    Display display = ((WindowManager)paramActivity.getSystemService("window")).getDefaultDisplay();
    if (checkMinimumApiLevel(8)) {
      i = display.getRotation();
    } else {
      i = display.getOrientation();
    } 
    if (i == 0)
      return 268455954; 
    if (i == 1)
      return 268455956; 
    if (i == 2)
      return 268455955; 
    if (i == 3)
      j = 268455957; 
    return j;
  }
  
  public static float[] getDisplayDpi(Activity paramActivity) {
    float[] arrayOfFloat;
    Activity activity = null;
    if (paramActivity == null)
      return null; 
    DisplayMetrics displayMetrics = new DisplayMetrics();
    if (checkMinimumApiLevel(17)) {
      paramActivity.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
    } else {
      paramActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    } 
    float f1 = displayMetrics.xdpi;
    float f2 = displayMetrics.ydpi;
    paramActivity = activity;
    if (f1 > 0.0F) {
      paramActivity = activity;
      if (f2 > 0.0F) {
        arrayOfFloat = new float[2];
        arrayOfFloat[0] = f1;
        arrayOfFloat[1] = f2;
      } 
    } 
    return arrayOfFloat;
  }
  
  public static int[] getDisplaySize(Activity paramActivity) {
    if (paramActivity == null)
      return null; 
    Point point = new Point();
    try {
      paramActivity.getWindowManager().getDefaultDisplay().getRealSize(point);
      if (point.x > 0 && point.y > 0) {
        int[] arrayOfInt = new int[2];
        if (point.y > point.x) {
          arrayOfInt[0] = point.y;
          arrayOfInt[1] = point.x;
          return arrayOfInt;
        } 
        arrayOfInt[0] = point.x;
        arrayOfInt[1] = point.y;
        return arrayOfInt;
      } 
    } catch (NoSuchMethodError noSuchMethodError) {
      DebugLog.LOGE("SystemTools", "Display.getRealSize is not supported on this platform");
    } 
    return null;
  }
  
  public static String getNativeLibraryPath(Activity paramActivity) {
    StringBuilder stringBuilder = null;
    if (paramActivity == null)
      return null; 
    try {
      ApplicationInfo applicationInfo = paramActivity.getApplicationInfo();
      if (applicationInfo != null) {
        String str;
        if (checkMinimumApiLevel(9)) {
          str = applicationInfo.nativeLibraryDir;
          if (str != null && str.length() > 0 && str.charAt(str.length() - 1) != '/') {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append('/');
            return stringBuilder.toString();
          } 
        } else {
          stringBuilder = new StringBuilder();
          stringBuilder.append("/data/data/");
          stringBuilder.append(str.getPackageName());
          stringBuilder.append("/lib/");
          return stringBuilder.toString();
        } 
      } else {
        return (String)stringBuilder;
      } 
    } catch (Exception exception) {
      return null;
    } 
    return (String)exception;
  }
  
  public static native void logSystemError(String paramString);
  
  public static Method retrieveClassMethod(Class<?> paramClass, String paramString, Class<?>... paramVarArgs) {
    try {
      return paramClass.getMethod(paramString, paramVarArgs);
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public static void sendKillSignal(final int errorCode) {
    final Activity activity = getActivityFromNative();
    if (activity == null)
      return; 
    activity.runOnUiThread(new Runnable() {
          public void run() {
            activity.setResult(errorCode);
            activity.finish();
          }
        });
  }
  
  public static native void setSystemErrorCode(int paramInt);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\SystemTools.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */