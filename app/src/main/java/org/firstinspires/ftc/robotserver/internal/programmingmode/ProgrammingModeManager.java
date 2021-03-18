package org.firstinspires.ftc.robotserver.internal.programmingmode;

import com.qualcomm.robotcore.eventloop.opmode.FtcRobotControllerServiceState;
import com.qualcomm.robotcore.util.WebHandlerManager;
import com.qualcomm.robotcore.util.WebServer;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketNamespaceHandler;

public class ProgrammingModeManager {
  public static final String TAG = ProgrammingModeManager.class.getSimpleName();
  
  private final List<ProgrammingMode> registeredProgrammingModes = Collections.synchronizedList(new LinkedList<ProgrammingMode>());
  
  private final ProgrammingModeWebHandlerDecorator webHandlerDecorator = new ProgrammingModeWebHandlerDecorator(this);
  
  private volatile WebHandlerManager webHandlerManager;
  
  private volatile WebServer webServer;
  
  private volatile WebSocketManager webSocketManager;
  
  public WebHandler decorate(boolean paramBoolean, WebHandler paramWebHandler) {
    return this.webHandlerDecorator.decorate(paramWebHandler, paramBoolean);
  }
  
  public WebHandler getRegisteredHandler(String paramString) {
    return this.webHandlerManager.getRegistered(paramString);
  }
  
  public WebServer getWebServer() {
    return this.webServer;
  }
  
  public void register(String paramString, WebHandler paramWebHandler) {
    this.webHandlerManager.register(paramString, paramWebHandler);
  }
  
  public void register(WebSocketNamespaceHandler paramWebSocketNamespaceHandler) {
    this.webSocketManager.registerNamespaceHandler(paramWebSocketNamespaceHandler);
  }
  
  public void register(ProgrammingMode paramProgrammingMode) {
    this.registeredProgrammingModes.add(paramProgrammingMode);
    if (this.webHandlerManager != null)
      paramProgrammingMode.register(this); 
  }
  
  public void setState(FtcRobotControllerServiceState paramFtcRobotControllerServiceState) {
    this.webServer = paramFtcRobotControllerServiceState.getWebServer();
    this.webHandlerManager = this.webServer.getWebHandlerManager();
    this.webSocketManager = this.webServer.getWebSocketManager();
    Iterator<ProgrammingMode> iterator = this.registeredProgrammingModes.iterator();
    while (iterator.hasNext())
      ((ProgrammingMode)iterator.next()).register(this); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\programmingmode\ProgrammingModeManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */