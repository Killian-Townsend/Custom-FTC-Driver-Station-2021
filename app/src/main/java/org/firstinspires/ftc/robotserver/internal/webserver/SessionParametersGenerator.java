package org.firstinspires.ftc.robotserver.internal.webserver;

import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.util.HashMap;
import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler;

public class SessionParametersGenerator extends WebHandlerDecorator {
  public SessionParametersGenerator(WebHandler paramWebHandler) {
    super(paramWebHandler);
  }
  
  private void generateParms(NanoHTTPD.IHTTPSession paramIHTTPSession) throws IOException, NanoHTTPD.ResponseException {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    NanoHTTPD.Method method = paramIHTTPSession.getMethod();
    if (NanoHTTPD.Method.PUT.equals(method) || NanoHTTPD.Method.POST.equals(method))
      paramIHTTPSession.parseBody(hashMap); 
  }
  
  public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession paramIHTTPSession) throws IOException, NanoHTTPD.ResponseException {
    generateParms(paramIHTTPSession);
    return super.getResponse(paramIHTTPSession);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\SessionParametersGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */