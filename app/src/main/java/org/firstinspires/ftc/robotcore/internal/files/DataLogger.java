package org.firstinspires.ftc.robotcore.internal.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class DataLogger {
  protected File file;
  
  protected BufferedWriter writer;
  
  public DataLogger(String paramString) throws IOException {
    File file2 = new File(paramString);
    this.file = file2;
    if (!file2.isAbsolute())
      this.file = new File(AppUtil.ROBOT_DATA_DIR, paramString); 
    File file1 = this.file.getParentFile();
    AppUtil.getInstance().ensureDirectoryExists(file1);
    this.writer = new BufferedWriter(new FileWriter(this.file));
  }
  
  public static String createFileName(String paramString) {
    Date date = new Date(System.currentTimeMillis());
    String str = AppUtil.getInstance().getIso8601DateFormat().format(date);
    return String.format(Locale.US, "%s-%s.txt", new Object[] { paramString, str });
  }
  
  public void addDataLine(Object... paramVarArgs) throws IOException {
    int k = paramVarArgs.length;
    int j = 1;
    int i = 0;
    while (i < k) {
      Object object = paramVarArgs[i];
      if (!j)
        this.writer.append('\t'); 
      if (object instanceof String) {
        this.writer.append('"');
        for (char c : ((String)object).toCharArray()) {
          if (c == '"') {
            this.writer.append('"');
            this.writer.append('"');
          } else {
            this.writer.append(c);
          } 
        } 
        this.writer.append('"');
      } else {
        this.writer.append(object.toString());
      } 
      i++;
      j = 0;
    } 
    newLine();
  }
  
  public void addHeaderLine(String... paramVarArgs) throws IOException {
    int j = paramVarArgs.length;
    boolean bool = true;
    int i = 0;
    while (i < j) {
      String str = paramVarArgs[i];
      if (!bool)
        this.writer.append('\t'); 
      this.writer.append(str);
      i++;
      bool = false;
    } 
    newLine();
  }
  
  public void close() {
    try {
      this.writer.close();
      return;
    } catch (IOException iOException) {
      return;
    } 
  }
  
  void newLine() throws IOException {
    this.writer.append("\r\n");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\files\DataLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */