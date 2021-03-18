package com.qualcomm.robotcore.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class ReadWriteFile {
  public static final String TAG = "ReadWriteFile";
  
  protected static Charset charset = Charset.forName("UTF-8");
  
  public static byte[] readAssetBytes(File paramFile) {
    try {
      return readAssetBytesOrThrow(paramFile);
    } catch (IOException iOException) {
      RobotLog.ee("ReadWriteFile", iOException, "error reading asset: %s", new Object[] { paramFile.getPath() });
      return new byte[0];
    } 
  }
  
  public static byte[] readAssetBytesOrThrow(File paramFile) throws IOException {
    return readBytesOrThrow(0, AppUtil.getDefContext().getAssets().open(paramFile.getPath()));
  }
  
  public static byte[] readBytes(RobotCoreCommandList.FWImage paramFWImage) {
    return paramFWImage.isAsset ? readAssetBytes(paramFWImage.file) : readFileBytes(paramFWImage.file);
  }
  
  protected static byte[] readBytesOrThrow(int paramInt, InputStream paramInputStream) throws IOException {
    null = new ByteArrayOutputStream(paramInt);
    byte[] arrayOfByte = new byte[1000];
    try {
      while (true) {
        paramInt = paramInputStream.read(arrayOfByte);
        if (paramInt == -1)
          return null.toByteArray(); 
        null.write(arrayOfByte);
      } 
    } finally {
      paramInputStream.close();
    } 
  }
  
  public static String readFile(File paramFile) {
    try {
      return readFileOrThrow(paramFile);
    } catch (IOException iOException) {
      RobotLog.ee("ReadWriteFile", iOException, "error reading file: %s", new Object[] { paramFile.getPath() });
      return "";
    } 
  }
  
  public static byte[] readFileBytes(File paramFile) {
    try {
      return readFileBytesOrThrow(paramFile);
    } catch (IOException iOException) {
      RobotLog.ee("ReadWriteFile", iOException, "error reading file: %s", new Object[] { paramFile.getPath() });
      return new byte[0];
    } 
  }
  
  public static byte[] readFileBytesOrThrow(File paramFile) throws IOException {
    FileInputStream fileInputStream = new FileInputStream(paramFile);
    return readBytesOrThrow((int)paramFile.length(), fileInputStream);
  }
  
  public static String readFileOrThrow(File paramFile) throws IOException {
    FileInputStream fileInputStream = new FileInputStream(paramFile);
    null = new ByteArrayOutputStream(fileInputStream.available());
    try {
      AppUtil.getInstance().copyStream(fileInputStream, null);
      return charset.decode(ByteBuffer.wrap(null.toByteArray())).toString();
    } finally {
      fileInputStream.close();
    } 
  }
  
  public static void writeFile(File paramFile, String paramString) {
    writeFile(paramFile.getParentFile(), paramFile.getName(), paramString);
  }
  
  public static void writeFile(File paramFile, String paramString1, String paramString2) {
    try {
      writeFileOrThrow(paramFile, paramString1, paramString2);
      return;
    } catch (IOException iOException) {
      RobotLog.ee("ReadWriteFile", iOException, "error writing file: %s", new Object[] { (new File(paramFile, paramString1)).getPath() });
      return;
    } 
  }
  
  public static void writeFileOrThrow(File paramFile, String paramString) throws IOException {
    writeFileOrThrow(paramFile.getParentFile(), paramFile.getName(), paramString);
  }
  
  public static void writeFileOrThrow(File paramFile, String paramString1, String paramString2) throws IOException {
    AppUtil.getInstance().ensureDirectoryExists(paramFile);
    ByteBuffer byteBuffer = charset.encode(paramString2);
    FileOutputStream fileOutputStream = new FileOutputStream(new File(paramFile, paramString1));
    try {
      fileOutputStream.write(byteBuffer.array(), 0, byteBuffer.limit());
      fileOutputStream.flush();
      return;
    } finally {
      fileOutputStream.close();
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\ReadWriteFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */