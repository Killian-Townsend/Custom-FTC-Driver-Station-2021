package org.firstinspires.ftc.robotserver.internal.webserver;

import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler;

public class NoCachingWebHandler extends WebHandlerDecorator {
  public NoCachingWebHandler(WebHandler paramWebHandler) {
    super(paramWebHandler);
  }
  
  public static NanoHTTPD.Response setNoCache(NanoHTTPD.IHTTPSession paramIHTTPSession, NanoHTTPD.Response paramResponse) {
    paramResponse.addHeader("Cache-Control", "no-cache");
    return paramResponse;
  }
  
  public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession paramIHTTPSession) throws IOException, NanoHTTPD.ResponseException {
    return setNoCache(paramIHTTPSession, super.getResponse(paramIHTTPSession));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\NoCachingWebHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */