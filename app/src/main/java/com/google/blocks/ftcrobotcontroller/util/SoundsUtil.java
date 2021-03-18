package com.google.blocks.ftcrobotcontroller.util;

import android.text.Html;
import android.util.Base64;
import java.io.File;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class SoundsUtil {
  public static final String VALID_SOUND_REGEX = "^[a-zA-Z0-9 \\!\\#\\$\\%\\&\\'\\(\\)\\+\\,\\-\\.\\;\\=\\@\\[\\]\\^_\\{\\}\\~]+$";
  
  public static void copySound(String paramString1, String paramString2) throws IOException {
    if (isValidSoundName(paramString1) && isValidSoundName(paramString2)) {
      if (!AppUtil.BLOCKS_SOUNDS_DIR.exists())
        AppUtil.BLOCKS_SOUNDS_DIR.mkdirs(); 
      FileUtil.copyFile(new File(AppUtil.BLOCKS_SOUNDS_DIR, paramString1), new File(AppUtil.BLOCKS_SOUNDS_DIR, paramString2));
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public static boolean deleteSounds(String[] paramArrayOfString) {
    int j = paramArrayOfString.length;
    int i = 0;
    while (i < j) {
      if (isValidSoundName(paramArrayOfString[i])) {
        i++;
        continue;
      } 
      throw new IllegalArgumentException();
    } 
    j = paramArrayOfString.length;
    boolean bool = true;
    i = 0;
    while (i < j) {
      String str = paramArrayOfString[i];
      File file = new File(AppUtil.BLOCKS_SOUNDS_DIR, str);
      boolean bool1 = bool;
      if (file.exists()) {
        bool1 = bool;
        if (!file.delete())
          bool1 = false; 
      } 
      i++;
      bool = bool1;
    } 
    return bool;
  }
  
  public static String fetchSoundFileContent(String paramString) throws IOException {
    if (isValidSoundName(paramString))
      return Base64.encodeToString(FileUtil.readBinaryFile(new File(AppUtil.BLOCKS_SOUNDS_DIR, paramString)), 0); 
    throw new IllegalArgumentException();
  }
  
  public static String fetchSounds() throws IOException {
    File[] arrayOfFile = AppUtil.BLOCKS_SOUNDS_DIR.listFiles();
    if (arrayOfFile != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[");
      int i = 0;
      for (String str = ""; i < arrayOfFile.length; str = ",") {
        String str1 = arrayOfFile[i].getName();
        stringBuilder.append(str);
        stringBuilder.append("{");
        stringBuilder.append("\"name\":\"");
        stringBuilder.append(ProjectsUtil.escapeDoubleQuotes(str1));
        stringBuilder.append("\", ");
        stringBuilder.append("\"escapedName\":\"");
        stringBuilder.append(ProjectsUtil.escapeDoubleQuotes(Html.escapeHtml(str1)));
        stringBuilder.append("\", ");
        stringBuilder.append("\"dateModifiedMillis\":");
        stringBuilder.append(arrayOfFile[i].lastModified());
        stringBuilder.append("}");
        i++;
      } 
      stringBuilder.append("]");
      return stringBuilder.toString();
    } 
    return "[]";
  }
  
  public static String getPathForSound(String paramString) {
    return (new File(AppUtil.BLOCKS_SOUNDS_DIR, paramString)).getAbsolutePath();
  }
  
  public static boolean isValidSoundName(String paramString) {
    return (paramString != null) ? paramString.matches("^[a-zA-Z0-9 \\!\\#\\$\\%\\&\\'\\(\\)\\+\\,\\-\\.\\;\\=\\@\\[\\]\\^_\\{\\}\\~]+$") : false;
  }
  
  public static void renameSound(String paramString1, String paramString2) throws IOException {
    if (isValidSoundName(paramString1) && isValidSoundName(paramString2)) {
      if (!AppUtil.BLOCKS_SOUNDS_DIR.exists())
        AppUtil.BLOCKS_SOUNDS_DIR.mkdirs(); 
      (new File(AppUtil.BLOCKS_SOUNDS_DIR, paramString1)).renameTo(new File(AppUtil.BLOCKS_SOUNDS_DIR, paramString2));
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public static void saveSoundFile(String paramString1, String paramString2) throws IOException {
    if (isValidSoundName(paramString1)) {
      File file1;
      if (!AppUtil.BLOCKS_SOUNDS_DIR.exists())
        AppUtil.BLOCKS_SOUNDS_DIR.mkdirs(); 
      byte[] arrayOfByte = Base64.decode(paramString2, 0);
      File file2 = new File(AppUtil.BLOCKS_SOUNDS_DIR, paramString1);
      paramString2 = null;
      if (file2.exists()) {
        long l = System.currentTimeMillis();
        file1 = AppUtil.BLOCKS_SOUNDS_DIR;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("backup_");
        stringBuilder.append(l);
        stringBuilder.append("_");
        stringBuilder.append(paramString1);
        file1 = new File(file1, stringBuilder.toString());
        FileUtil.copyFile(file2, file1);
      } 
      FileUtil.writeBinaryFile(file2, arrayOfByte);
      if (file1 != null)
        file1.delete(); 
      return;
    } 
    throw new IllegalArgumentException();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontrolle\\util\SoundsUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */