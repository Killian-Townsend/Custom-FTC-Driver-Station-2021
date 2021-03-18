package org.firstinspires.ftc.robotserver.internal.webserver;

import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler;

public class WebHandlerDecorator implements WebHandler {
  private final WebHandler delegate;
  
  public WebHandlerDecorator(WebHandler paramWebHandler) {
    this.delegate = paramWebHandler;
  }
  
  public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession paramIHTTPSession) throws IOException, NanoHTTPD.ResponseException {
    return this.delegate.getResponse(paramIHTTPSession);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\WebHandlerDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */