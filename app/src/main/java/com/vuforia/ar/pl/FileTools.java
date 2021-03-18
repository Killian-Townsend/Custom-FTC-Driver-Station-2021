package com.vuforia.ar.pl;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Environment;
import java.io.File;

public class FileTools {
  private static final int FILE_PATHTYPEINDEX_ABSOLUTE = -1;
  
  private static final int FILE_PATHTYPEINDEX_ANDROID_ASSETS = 0;
  
  private static final int FILE_PATHTYPEINDEX_ANDROID_DATALOCAL = 4;
  
  private static final int FILE_PATHTYPEINDEX_ANDROID_MEDIASTORAGE = 3;
  
  private static final int FILE_PATHTYPEINDEX_ANDROID_PRIVATEAPPCACHE = 2;
  
  private static final int FILE_PATHTYPEINDEX_ANDROID_PRIVATEAPPSTORAGE = 1;
  
  private static final String MODULENAME = "FileTools";
  
  public static String getAbsolutePath(int paramInt, String paramString) {
    String str1;
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt != 3) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        File file = Environment.getExternalStorageDirectory();
        if (file == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        str1 = file.getAbsolutePath();
      } else {
        Activity activity = SystemTools.getActivityFromNative();
        if (activity == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        File file = activity.getCacheDir();
        if (file == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        str1 = file.getAbsolutePath();
      } 
    } else {
      Activity activity = SystemTools.getActivityFromNative();
      if (activity == null) {
        SystemTools.setSystemErrorCode(6);
        return null;
      } 
      File file = activity.getFilesDir();
      if (file == null) {
        SystemTools.setSystemErrorCode(6);
        return null;
      } 
      str1 = file.getAbsolutePath();
    } 
    String str2 = str1;
    if (str1 != null) {
      str2 = str1;
      if (paramString != null) {
        str2 = str1;
        if (str1.length() > 0) {
          str2 = str1;
          if (str1.charAt(str1.length() - 1) != '/') {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append(str1);
            stringBuilder1.append("/");
            str2 = stringBuilder1.toString();
          } 
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str2);
        stringBuilder.append(paramString);
        str2 = stringBuilder.toString();
      } 
    } 
    return str2;
  }
  
  public static AssetManager get_assetmanager() {
    Activity activity = SystemTools.getActivityFromNative();
    if (activity == null) {
      SystemTools.setSystemErrorCode(6);
      return null;
    } 
    return activity.getAssets();
  }
  
  public static boolean mediastorage_isAvailable() {
    String str = Environment.getExternalStorageState();
    return ("mounted".equals(str) || "mounted_ro".equals(str));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\FileTools.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */