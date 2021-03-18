package org.firstinspires.ftc.robotserver.internal.webserver.tempfile;

import android.os.Environment;
import com.qualcomm.robotcore.util.RobotLog;
import fi.iki.elonen.NanoHTTPD;
import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UploadedTempFileManager implements NanoHTTPD.TempFileManager {
  private static final String TAG = "UploadedTempFileManager";
  
  private static final File tempDir = new File(Environment.getExternalStorageDirectory(), "tmp");
  
  private final List<NanoHTTPD.TempFile> tempFileList = new CopyOnWriteArrayList<NanoHTTPD.TempFile>();
  
  UploadedTempFileManager() {
    if (!tempDir.exists() && !tempDir.mkdirs())
      RobotLog.ee("UploadedTempFileManager", "Failed to create temp directory"); 
  }
  
  public void clear() {
    for (NanoHTTPD.TempFile tempFile : this.tempFileList) {
      try {
        tempFile.delete();
      } catch (Exception exception) {
        RobotLog.ee("UploadedTempFileManager", exception, "Failed to delete temp file");
      } 
    } 
    this.tempFileList.clear();
  }
  
  public NanoHTTPD.TempFile createTempFile(String paramString) throws Exception {
    UploadedTempFile uploadedTempFile = new UploadedTempFile(tempDir);
    this.tempFileList.add(uploadedTempFile);
    return uploadedTempFile;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\tempfile\UploadedTempFileManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */