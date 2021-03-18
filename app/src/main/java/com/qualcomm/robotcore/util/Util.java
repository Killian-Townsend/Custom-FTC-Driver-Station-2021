package com.qualcomm.robotcore.util;

import android.widget.TextView;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Util {
  public static String ASCII_RECORD_SEPARATOR = "\036";
  
  public static final String LOWERCASE_ALPHA_NUM_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
  
  public static byte[] concatenateByteArrays(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    byte[] arrayOfByte = new byte[paramArrayOfbyte1.length + paramArrayOfbyte2.length];
    System.arraycopy(paramArrayOfbyte1, 0, arrayOfByte, 0, paramArrayOfbyte1.length);
    System.arraycopy(paramArrayOfbyte2, 0, arrayOfByte, paramArrayOfbyte1.length, paramArrayOfbyte2.length);
    return arrayOfByte;
  }
  
  public static byte[] concatenateByteArrays(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3) {
    byte[] arrayOfByte = new byte[paramArrayOfbyte1.length + paramArrayOfbyte2.length + paramArrayOfbyte3.length];
    System.arraycopy(paramArrayOfbyte1, 0, arrayOfByte, 0, paramArrayOfbyte1.length);
    System.arraycopy(paramArrayOfbyte2, 0, arrayOfByte, paramArrayOfbyte1.length, paramArrayOfbyte2.length);
    System.arraycopy(paramArrayOfbyte3, 0, arrayOfByte, paramArrayOfbyte1.length + paramArrayOfbyte2.length, paramArrayOfbyte3.length);
    return arrayOfByte;
  }
  
  public static String getRandomString(int paramInt, String paramString) {
    Random random = new Random();
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < paramInt; i++)
      stringBuilder.append(paramString.charAt(random.nextInt(paramString.length()))); 
    return stringBuilder.toString();
  }
  
  public static boolean isGoodString(String paramString) {
    return (paramString == null) ? false : (!paramString.trim().equals(paramString) ? false : (!(paramString.length() == 0)));
  }
  
  public static boolean isPrefixOf(String paramString1, String paramString2) {
    if (paramString1 == null)
      return true; 
    if (paramString2 == null)
      return false; 
    if (paramString1.length() <= paramString2.length()) {
      for (int i = 0; i < paramString1.length(); i++) {
        if (paramString1.charAt(i) != paramString2.charAt(i))
          return false; 
      } 
      return true;
    } 
    return false;
  }
  
  public static void sortFilesByName(File[] paramArrayOfFile) {
    Arrays.sort(paramArrayOfFile, new Comparator<File>() {
          public int compare(File param1File1, File param1File2) {
            return param1File1.getName().compareTo(param1File2.getName());
          }
        });
  }
  
  public static void updateTextView(final TextView textView, final String msg) {
    if (textView != null)
      textView.post(new Runnable() {
            public void run() {
              textView.setText(msg);
            }
          }); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */