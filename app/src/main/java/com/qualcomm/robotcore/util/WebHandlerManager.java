package com.qualcomm.robotcore.util;

import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler;
import org.firstinspires.ftc.robotcore.internal.webserver.WebObserver;

public interface WebHandlerManager {
  WebHandler getRegistered(String paramString);
  
  WebServer getWebServer();
  
  void register(String paramString, WebHandler paramWebHandler);
  
  void registerObserver(String paramString, WebObserver paramWebObserver);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\WebHandlerManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */