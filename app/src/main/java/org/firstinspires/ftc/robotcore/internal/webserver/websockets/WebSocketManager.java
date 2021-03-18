package org.firstinspires.ftc.robotcore.internal.webserver.websockets;

public interface WebSocketManager {
  public static final String SYSTEM_NAMESPACE = "system";
  
  int broadcastToNamespace(String paramString, FtcWebSocketMessage paramFtcWebSocketMessage);
  
  void registerNamespaceAsBroadcastOnly(String paramString);
  
  void registerNamespaceHandler(WebSocketNamespaceHandler paramWebSocketNamespaceHandler);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\webserver\websockets\WebSocketManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */