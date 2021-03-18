package org.firstinspires.ftc.robotserver.internal.webserver.websockets.tootallnate;

import com.qualcomm.robotcore.util.RobotLog;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.CloseCode;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager;
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.FtcWebSocketImpl;
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.FtcWebSocketServer;
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.WebSocketManagerImpl;
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.WebSocketNamespaceHandlerRegistry;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class TooTallWebSocketServer extends WebSocketServer implements FtcWebSocketServer {
  private static final int DECODER_THREAD_COUNT = 1;
  
  private static final String TAG = "TooTallWebSocketServer";
  
  private final WebSocketManagerImpl manager = new WebSocketManagerImpl();
  
  private final Map<WebSocket, FtcWebSocketImpl.RawWebSocket> wsMap = new ConcurrentHashMap<WebSocket, FtcWebSocketImpl.RawWebSocket>();
  
  public TooTallWebSocketServer(InetSocketAddress paramInetSocketAddress) {
    super(paramInetSocketAddress, 1);
    setReuseAddr(true);
    setConnectionLostTimeout(5);
    WebSocketNamespaceHandlerRegistry.onWebSocketServerCreation(this.manager);
  }
  
  public WebSocketManager getWebSocketManager() {
    return (WebSocketManager)this.manager;
  }
  
  public void onClose(WebSocket paramWebSocket, int paramInt, String paramString, boolean paramBoolean) {
    ((FtcWebSocketImpl.RawWebSocket)this.wsMap.get(paramWebSocket)).onClose(CloseCode.find(paramInt), paramString, paramBoolean);
  }
  
  public void onError(WebSocket paramWebSocket, Exception paramException) {
    if (paramWebSocket != null) {
      ((FtcWebSocketImpl.RawWebSocket)this.wsMap.get(paramWebSocket)).onException(paramException);
      return;
    } 
    RobotLog.ee("TooTallWebSocketServer", paramException, "WebSocket server error");
  }
  
  public void onMessage(WebSocket paramWebSocket, String paramString) {
    ((FtcWebSocketImpl.RawWebSocket)this.wsMap.get(paramWebSocket)).onMessage(paramString);
  }
  
  public void onOpen(WebSocket paramWebSocket, ClientHandshake paramClientHandshake) {
    TooTallWebSocket tooTallWebSocket = new TooTallWebSocket(paramWebSocket, getPort(), paramWebSocket.getRemoteSocketAddress().getAddress(), paramWebSocket.getRemoteSocketAddress().getHostName(), this.manager);
    this.wsMap.put(paramWebSocket, tooTallWebSocket);
    tooTallWebSocket.onOpen();
  }
  
  public void onStart() {
    RobotLog.vv("TooTallWebSocketServer", "Started WebSocket server on port %d", new Object[] { Integer.valueOf(getPort()) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\websockets\tootallnate\TooTallWebSocketServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */