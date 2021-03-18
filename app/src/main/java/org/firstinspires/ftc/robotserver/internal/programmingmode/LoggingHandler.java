package org.firstinspires.ftc.robotserver.internal.programmingmode;

import com.qualcomm.robotcore.util.RobotLog;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler;
import org.firstinspires.ftc.robotserver.internal.webserver.WebHandlerDecorator;

public class LoggingHandler extends WebHandlerDecorator {
  private ProgrammingModeManager programmingModeManager;
  
  public LoggingHandler(ProgrammingModeManager paramProgrammingModeManager, WebHandler paramWebHandler) {
    super(paramWebHandler);
    this.programmingModeManager = paramProgrammingModeManager;
  }
  
  private void addLogging(NanoHTTPD.IHTTPSession paramIHTTPSession) {
    RobotLog.vv(ProgrammingModeManager.TAG, "serve uri=%s", new Object[] { paramIHTTPSession.getUri() });
  }
  
  public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession paramIHTTPSession) throws IOException, NanoHTTPD.ResponseException {
    addLogging(paramIHTTPSession);
    return super.getResponse(paramIHTTPSession);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\programmingmode\LoggingHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */