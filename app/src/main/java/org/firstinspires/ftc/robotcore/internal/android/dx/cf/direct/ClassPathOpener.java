package org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.FileUtils;

public class ClassPathOpener {
  public static final FileNameFilter acceptAll = new FileNameFilter() {
      public boolean accept(String param1String) {
        return true;
      }
    };
  
  private final Consumer consumer;
  
  private FileNameFilter filter;
  
  private final String pathname;
  
  private final boolean sort;
  
  public ClassPathOpener(String paramString, boolean paramBoolean, Consumer paramConsumer) {
    this(paramString, paramBoolean, acceptAll, paramConsumer);
  }
  
  public ClassPathOpener(String paramString, boolean paramBoolean, FileNameFilter paramFileNameFilter, Consumer paramConsumer) {
    this.pathname = paramString;
    this.sort = paramBoolean;
    this.consumer = paramConsumer;
    this.filter = paramFileNameFilter;
  }
  
  private static int compareClassNames(String paramString1, String paramString2) {
    paramString1 = paramString1.replace('$', '0');
    paramString2 = paramString2.replace('$', '0');
    return paramString1.replace("package-info", "").compareTo(paramString2.replace("package-info", ""));
  }
  
  private boolean processArchive(File paramFile) throws IOException {
    ZipFile zipFile = new ZipFile(paramFile);
    ArrayList<? extends ZipEntry> arrayList = Collections.list(zipFile.entries());
    if (this.sort)
      Collections.sort(arrayList, new Comparator<ZipEntry>() {
            public int compare(ZipEntry param1ZipEntry1, ZipEntry param1ZipEntry2) {
              return ClassPathOpener.compareClassNames(param1ZipEntry1.getName(), param1ZipEntry2.getName());
            }
          }); 
    this.consumer.onProcessArchiveStart(paramFile);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(40000);
    byte[] arrayOfByte = new byte[20000];
    Iterator<? extends ZipEntry> iterator = arrayList.iterator();
    boolean bool = false;
    while (iterator.hasNext()) {
      ZipEntry zipEntry = iterator.next();
      boolean bool1 = zipEntry.isDirectory();
      String str = zipEntry.getName();
      if (this.filter.accept(str)) {
        byte[] arrayOfByte1;
        if (!bool1) {
          InputStream inputStream = zipFile.getInputStream(zipEntry);
          byteArrayOutputStream.reset();
          while (true) {
            int i = inputStream.read(arrayOfByte);
            if (i != -1) {
              byteArrayOutputStream.write(arrayOfByte, 0, i);
              continue;
            } 
            inputStream.close();
            arrayOfByte1 = byteArrayOutputStream.toByteArray();
            break;
          } 
        } else {
          arrayOfByte1 = new byte[0];
        } 
        bool |= this.consumer.processFileBytes(str, zipEntry.getTime(), arrayOfByte1);
      } 
    } 
    zipFile.close();
    return bool;
  }
  
  private boolean processDirectory(File paramFile, boolean paramBoolean) {
    throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge Z and I\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
  }
  
  private boolean processOne(File paramFile, boolean paramBoolean) {
    try {
      if (paramFile.isDirectory())
        return processDirectory(paramFile, paramBoolean); 
      String str = paramFile.getPath();
      if (str.endsWith(".zip") || str.endsWith(".jar") || str.endsWith(".apk"))
        return processArchive(paramFile); 
      if (this.filter.accept(str)) {
        byte[] arrayOfByte = FileUtils.readFile(paramFile);
        return this.consumer.processFileBytes(str, paramFile.lastModified(), arrayOfByte);
      } 
    } catch (Exception exception) {
      this.consumer.onException(exception);
      return false;
    } 
    return false;
  }
  
  public boolean process() {
    return processOne(new File(this.pathname), true);
  }
  
  public static interface Consumer {
    void onException(Exception param1Exception);
    
    void onProcessArchiveStart(File param1File);
    
    boolean processFileBytes(String param1String, long param1Long, byte[] param1ArrayOfbyte);
  }
  
  public static interface FileNameFilter {
    boolean accept(String param1String);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\direct\ClassPathOpener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */