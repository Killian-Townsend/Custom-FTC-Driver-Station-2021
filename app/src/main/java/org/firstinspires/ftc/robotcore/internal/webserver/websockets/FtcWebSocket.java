package org.firstinspires.ftc.robotcore.internal.webserver.websockets;

import java.net.InetAddress;

public interface FtcWebSocket {
  void close(CloseCode paramCloseCode, String paramString);
  
  int getPort();
  
  String getRemoteHostname();
  
  InetAddress getRemoteIpAddress();
  
  boolean isOpen();
  
  void send(FtcWebSocketMessage paramFtcWebSocketMessage);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\webserver\websockets\FtcWebSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */