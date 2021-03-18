package org.firstinspires.ftc.robotserver.internal.webserver.controlhubupdater;

import fi.iki.elonen.NanoHTTPD;
import java.util.UUID;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;

public final class ChUpdaterUploadResponse {
  private final String uploadIdentifier;
  
  private ChUpdaterUploadResponse(UUID paramUUID) {
    this.uploadIdentifier = paramUUID.toString();
  }
  
  public static NanoHTTPD.Response create(UUID paramUUID) {
    ChUpdaterUploadResponse chUpdaterUploadResponse = new ChUpdaterUploadResponse(paramUUID);
    String str = SimpleGson.getInstance().toJson(chUpdaterUploadResponse);
    return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "application/json", str);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\controlhubupdater\ChUpdaterUploadResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */