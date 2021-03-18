package com.google.blocks.ftcrobotcontroller.util;

import android.content.res.AssetManager;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

public class FileUtil {
  public static void copyFile(File paramFile1, File paramFile2) throws IOException {
    Exception exception1;
    Exception exception2;
    Exception exception3 = null;
    File file = null;
    try {
      FileChannel fileChannel2 = (new FileInputStream(paramFile1)).getChannel();
    } finally {
      exception2 = null;
      paramFile2 = null;
    } 
    if (exception1 != null)
      exception1.close(); 
    if (paramFile2 != null)
      paramFile2.close(); 
    throw exception2;
  }
  
  public static void readAsset(StringBuilder paramStringBuilder, AssetManager paramAssetManager, String paramString) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramAssetManager.open(paramString)));
    try {
      while (true) {
        paramString = bufferedReader.readLine();
        if (paramString != null) {
          paramStringBuilder.append(paramString);
          paramStringBuilder.append("\n");
          continue;
        } 
        return;
      } 
    } finally {
      paramStringBuilder = null;
    } 
  }
  
  public static byte[] readBinaryFile(File paramFile) throws IOException {
    int i = (int)paramFile.length();
    null = new byte[i];
    new StringBuilder();
    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
    try {
      bufferedInputStream.read(null, 0, i);
      return null;
    } finally {
      bufferedInputStream.close();
    } 
  }
  
  public static String readFile(File paramFile) throws IOException {
    null = new StringBuilder();
    BufferedReader bufferedReader = new BufferedReader(new FileReader(paramFile));
    try {
      while (true) {
        String str = bufferedReader.readLine();
        if (str != null) {
          null.append(str);
          null.append("\n");
          continue;
        } 
        return null.toString();
      } 
    } finally {
      bufferedReader.close();
    } 
  }
  
  public static void writeBinaryFile(File paramFile, byte[] paramArrayOfbyte) throws IOException {
    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramFile));
    try {
      bufferedOutputStream.write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
      return;
    } finally {
      bufferedOutputStream.close();
    } 
  }
  
  public static void writeFile(File paramFile, String paramString) throws IOException {
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(paramFile));
    try {
      bufferedWriter.write(paramString);
      return;
    } finally {
      bufferedWriter.close();
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontrolle\\util\FileUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */