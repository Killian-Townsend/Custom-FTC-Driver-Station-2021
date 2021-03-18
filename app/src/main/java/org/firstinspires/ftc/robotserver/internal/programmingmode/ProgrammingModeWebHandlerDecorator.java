package org.firstinspires.ftc.robotserver.internal.programmingmode;

import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler;
import org.firstinspires.ftc.robotserver.internal.webserver.RobotControllerWebHandlers;

public class ProgrammingModeWebHandlerDecorator {
  private final ProgrammingModeManager manager;
  
  public ProgrammingModeWebHandlerDecorator(ProgrammingModeManager paramProgrammingModeManager) {
    this.manager = paramProgrammingModeManager;
  }
  
  public <T extends WebHandler> WebHandler decorate(T paramT, boolean paramBoolean) {
    WebHandler webHandler;
    T t = paramT;
    if (paramBoolean)
      webHandler = RobotControllerWebHandlers.decorateWithParms((WebHandler)paramT); 
    return decorateWithLogging(webHandler);
  }
  
  public WebHandler decorateWithLogging(WebHandler paramWebHandler) {
    return (WebHandler)((paramWebHandler instanceof LoggingHandler) ? paramWebHandler : new LoggingHandler(this.manager, paramWebHandler));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\programmingmode\ProgrammingModeWebHandlerDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */