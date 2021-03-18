package org.firstinspires.ftc.robotcore.internal.webserver;

import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;

public interface WebHandler {
  NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession paramIHTTPSession) throws IOException, NanoHTTPD.ResponseException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\webserver\WebHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */