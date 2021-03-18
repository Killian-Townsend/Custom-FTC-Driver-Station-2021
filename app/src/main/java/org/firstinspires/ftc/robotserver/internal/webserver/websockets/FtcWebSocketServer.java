package org.firstinspires.ftc.robotserver.internal.webserver.websockets;

import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager;

public interface FtcWebSocketServer {
  WebSocketManager getWebSocketManager();
  
  void start();
  
  void stop(int paramInt) throws InterruptedException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\websockets\FtcWebSocketServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */