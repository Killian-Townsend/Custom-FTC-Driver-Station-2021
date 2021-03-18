package org.firstinspires.ftc.robotserver.internal.webserver.tempfile;

import fi.iki.elonen.NanoHTTPD;

public class UploadedTempFileManagerFactory implements NanoHTTPD.TempFileManagerFactory {
  public NanoHTTPD.TempFileManager create() {
    return new UploadedTempFileManager();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\tempfile\UploadedTempFileManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */