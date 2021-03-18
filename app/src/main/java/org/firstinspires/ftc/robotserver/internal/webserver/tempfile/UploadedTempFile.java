package org.firstinspires.ftc.robotserver.internal.webserver.tempfile;

import fi.iki.elonen.NanoHTTPD;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class UploadedTempFile implements NanoHTTPD.TempFile {
  private final File file;
  
  private OutputStream outputStream;
  
  UploadedTempFile(File paramFile) throws IOException {
    this.file = File.createTempFile("upload-", "", paramFile);
  }
  
  public void delete() throws IOException {
    OutputStream outputStream = this.outputStream;
    if (outputStream != null)
      outputStream.close(); 
    if (!this.file.exists())
      return; 
    if (this.file.delete())
      return; 
    throw new IOException("File failed to delete successfully");
  }
  
  public String getName() {
    return this.file.getAbsolutePath();
  }
  
  public OutputStream open() throws FileNotFoundException {
    if (this.outputStream == null)
      this.outputStream = new FileOutputStream(this.file); 
    return this.outputStream;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\tempfile\UploadedTempFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */