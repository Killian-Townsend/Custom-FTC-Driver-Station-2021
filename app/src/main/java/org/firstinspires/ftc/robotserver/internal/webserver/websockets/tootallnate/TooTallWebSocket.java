package org.firstinspires.ftc.robotserver.internal.webserver.websockets.tootallnate;

import com.qualcomm.robotcore.util.RobotLog;
import java.net.InetAddress;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager;
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.FtcWebSocketImpl;
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.WebSocketManagerImpl;
import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

public class TooTallWebSocket extends FtcWebSocketImpl.RawWebSocket {
  private static final String TAG = "TooTallWebSocket";
  
  private final WebSocket webSocket;
  
  TooTallWebSocket(WebSocket paramWebSocket, int paramInt, InetAddress paramInetAddress, String paramString, WebSocketManagerImpl paramWebSocketManagerImpl) {
    super(paramInt, paramInetAddress, paramString, (WebSocketManager)paramWebSocketManagerImpl);
    this.webSocket = paramWebSocket;
  }
  
  public void close(int paramInt, String paramString) {
    this.webSocket.close(paramInt, paramString);
  }
  
  protected boolean isOpen() {
    return this.webSocket.isOpen();
  }
  
  public void send(String paramString) {
    try {
      this.webSocket.send(paramString);
      return;
    } catch (WebsocketNotConnectedException websocketNotConnectedException) {
      RobotLog.ww("TooTallWebSocket", "Attempted to send message to unconnected WebSocket");
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\websockets\tootallnate\TooTallWebSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */