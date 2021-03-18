package com.vuforia;

import android.app.Activity;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Vuforia {
  public static final int GL_20 = 1;
  
  public static final int GL_30 = 8;
  
  public static final int INIT_DEVICE_NOT_SUPPORTED = -2;
  
  public static final int INIT_ERROR = -1;
  
  public static final int INIT_EXTERNAL_DEVICE_NOT_DETECTED = -10;
  
  public static final int INIT_LICENSE_ERROR_CANCELED_KEY = -8;
  
  public static final int INIT_LICENSE_ERROR_INVALID_KEY = -5;
  
  public static final int INIT_LICENSE_ERROR_MISSING_KEY = -4;
  
  public static final int INIT_LICENSE_ERROR_NO_NETWORK_PERMANENT = -6;
  
  public static final int INIT_LICENSE_ERROR_NO_NETWORK_TRANSIENT = -7;
  
  public static final int INIT_LICENSE_ERROR_PRODUCT_TYPE_MISMATCH = -9;
  
  public static final int INIT_NO_CAMERA_ACCESS = -3;
  
  private static boolean initializedJava = false;
  
  private static UpdateCallback sUpdateCallback = null;
  
  private static UpdateCallbackInterface sUpdateCallbackInterface = null;
  
  protected static Map<Integer, Object> sUserDataMap = new ConcurrentHashMap<Integer, Object>(16, 0.75F, 4);
  
  protected static short[] convertStringToShortArray(String paramString) {
    if (paramString == null)
      return null; 
    int k = paramString.codePointCount(0, paramString.length()) + 1;
    short[] arrayOfShort = new short[k];
    int m = paramString.length();
    int j = 0;
    for (int i = j; j < m; i = n + 1) {
      int i1 = paramString.codePointAt(j);
      int n = i;
      if (i1 > 65535) {
        arrayOfShort[i] = (short)(i1 >> 16);
        n = i + 1;
      } 
      arrayOfShort[n] = (short)i1;
      j += Character.charCount(i1);
    } 
    arrayOfShort[k - 1] = 0;
    return arrayOfShort;
  }
  
  public static void deinit() {
    VuforiaJNI.deinit();
  }
  
  public static int getBitsPerPixel(int paramInt) {
    return VuforiaJNI.getBitsPerPixel(paramInt);
  }
  
  public static int getBufferSize(int paramInt1, int paramInt2, int paramInt3) {
    return VuforiaJNI.getBufferSize(paramInt1, paramInt2, paramInt3);
  }
  
  public static String getLibraryVersion() {
    return VuforiaJNI.getLibraryVersion();
  }
  
  public static int init() {
    return VuforiaJNI.init();
  }
  
  public static boolean isInitialized() {
    return VuforiaJNI.isInitialized();
  }
  
  private static boolean loadLibrary(String paramString) {
    try {
      System.loadLibrary(paramString);
      PrintStream printStream = System.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Native library lib");
      stringBuilder.append(paramString);
      stringBuilder.append(".so loaded");
      printStream.println(stringBuilder.toString());
      return true;
    } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
      PrintStream printStream = System.err;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("The library lib");
      stringBuilder.append(paramString);
      stringBuilder.append(".so could not be loaded");
      printStream.println(stringBuilder.toString());
    } catch (SecurityException securityException) {
      PrintStream printStream = System.err;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("The library lib");
      stringBuilder.append(paramString);
      stringBuilder.append(".so was not allowed to be loaded");
      printStream.println(stringBuilder.toString());
    } 
    return false;
  }
  
  public static void onPause() {
    VuforiaJNI.onPause();
  }
  
  public static void onResume() {
    VuforiaJNI.onResume();
  }
  
  public static void onSurfaceChanged(int paramInt1, int paramInt2) {
    VuforiaJNI.onSurfaceChanged(paramInt1, paramInt2);
  }
  
  public static void onSurfaceCreated() {
    VuforiaJNI.onSurfaceCreated();
  }
  
  private static native void privateSetInitParameters(Activity paramActivity, int paramInt, String paramString);
  
  public static void registerCallback(UpdateCallbackInterface paramUpdateCallbackInterface) {
    VuforiaJNI.registerCallback(UpdateCallback.getCPtr(registerLocalReference(paramUpdateCallbackInterface)), sUpdateCallback);
  }
  
  protected static UpdateCallback registerLocalReference(UpdateCallbackInterface paramUpdateCallbackInterface) {
    if (paramUpdateCallbackInterface == null) {
      sUpdateCallback = null;
      sUpdateCallbackInterface = null;
      return null;
    } 
    sUpdateCallbackInterface = paramUpdateCallbackInterface;
    UpdateCallback updateCallback = new UpdateCallback() {
        public void Vuforia_onUpdate(State param1State) {
          Vuforia.sUpdateCallbackInterface.Vuforia_onUpdate(param1State);
          param1State.delete();
        }
      };
    sUpdateCallback = updateCallback;
    return updateCallback;
  }
  
  public static boolean requiresAlpha() {
    return VuforiaJNI.requiresAlpha();
  }
  
  protected static Object retreiveFromUserDataMap(Integer paramInteger) {
    return !sUserDataMap.containsKey(paramInteger) ? null : sUserDataMap.get(paramInteger);
  }
  
  public static boolean setFrameFormat(int paramInt, boolean paramBoolean) {
    return VuforiaJNI.setFrameFormat(paramInt, paramBoolean);
  }
  
  protected static void setHint() {
    setHint(-858996736L, 2796202);
  }
  
  public static boolean setHint(long paramLong, int paramInt) {
    return VuforiaJNI.setHint(paramLong, paramInt);
  }
  
  public static void setInitParameters(Activity paramActivity, int paramInt, String paramString) {
    if (!initializedJava) {
      setHint();
      initializedJava = true;
    } 
    privateSetInitParameters(paramActivity, paramInt, paramString);
  }
  
  protected static boolean updateUserDataMap(Integer paramInteger, Object paramObject) {
    if (paramInteger == null)
      return false; 
    if (paramObject == null) {
      sUserDataMap.remove(paramInteger);
    } else {
      sUserDataMap.put(paramInteger, paramObject);
    } 
    return true;
  }
  
  protected static boolean wasInitializedJava() {
    return initializedJava;
  }
  
  static {
    if (!loadLibrary("Vuforia"))
      System.exit(1); 
  }
  
  public static interface UpdateCallbackInterface {
    void Vuforia_onUpdate(State param1State);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\Vuforia.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */