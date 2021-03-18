package com.qualcomm.robotcore.util;

import org.firstinspires.ftc.robotcore.internal.webserver.RobotControllerWebInfo;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager;

public interface WebServer {
  RobotControllerWebInfo getConnectionInformation();
  
  WebHandlerManager getWebHandlerManager();
  
  WebSocketManager getWebSocketManager();
  
  void start();
  
  void stop();
  
  boolean wasStarted();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\WebServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */