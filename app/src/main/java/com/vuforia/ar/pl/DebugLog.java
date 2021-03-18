package com.vuforia.ar.pl;

import android.util.Log;

public class DebugLog {
  private static final String LOGTAG = "AR";
  
  public static final void LOGD(String paramString1, String paramString2) {
    String str = paramString2;
    if (paramString1.length() > 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString1);
      stringBuilder.append(": ");
      stringBuilder.append(paramString2);
      str = stringBuilder.toString();
    } 
    Log.d("AR", str);
  }
  
  public static final void LOGE(String paramString1, String paramString2) {
    String str = paramString2;
    if (paramString1.length() > 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString1);
      stringBuilder.append(": ");
      stringBuilder.append(paramString2);
      str = stringBuilder.toString();
    } 
    Log.e("AR", str);
  }
  
  public static final void LOGI(String paramString1, String paramString2) {
    String str = paramString2;
    if (paramString1.length() > 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString1);
      stringBuilder.append(": ");
      stringBuilder.append(paramString2);
      str = stringBuilder.toString();
    } 
    Log.i("AR", str);
  }
  
  public static final void LOGW(String paramString1, String paramString2) {
    String str = paramString2;
    if (paramString1.length() > 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString1);
      stringBuilder.append(": ");
      stringBuilder.append(paramString2);
      str = stringBuilder.toString();
    } 
    Log.w("AR", str);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\DebugLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */