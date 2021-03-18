package org.firstinspires.ftc.robotserver.internal.webserver.websockets;

import com.google.gson.JsonSyntaxException;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.InetAddress;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.CloseCode;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.FtcWebSocket;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.FtcWebSocketMessage;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager;

public final class FtcWebSocketImpl implements FtcWebSocket {
  private static final boolean DEBUG = false;
  
  private static final String TAG = "FtcWebSocket";
  
  private final WebSocketManager manager;
  
  private final int port;
  
  private final RawWebSocket rawWebSocket;
  
  private final String remoteHostname;
  
  private final InetAddress remoteIpAddress;
  
  private FtcWebSocketImpl(int paramInt, InetAddress paramInetAddress, String paramString, WebSocketManager paramWebSocketManager, RawWebSocket paramRawWebSocket) {
    this.remoteIpAddress = paramInetAddress;
    this.remoteHostname = paramString;
    this.port = paramInt;
    this.rawWebSocket = paramRawWebSocket;
    this.manager = paramWebSocketManager;
  }
  
  private void onClose(CloseCode paramCloseCode, String paramString, boolean paramBoolean) {
    RobotLog.vv("FtcWebSocket", "%s has closed. closeCode:%s initiatedByRemote:%b Reason: %s", new Object[] { this, paramCloseCode.toString(), Boolean.valueOf(paramBoolean), paramString });
    ((WebSocketManagerImpl)this.manager).onWebSocketClose(this);
  }
  
  private void onException(Throwable paramThrowable) {
    RobotLog.ee("FtcWebSocket", "%s experienced an exception:", new Object[] { this });
    RobotLog.logStackTrace("FtcWebSocket", paramThrowable);
  }
  
  private void onMessage(String paramString) {
    try {
      FtcWebSocketMessage ftcWebSocketMessage = FtcWebSocketMessage.fromJson(paramString);
      ((WebSocketManagerImpl)this.manager).onWebSocketMessage(ftcWebSocketMessage, this);
      return;
    } catch (JsonSyntaxException jsonSyntaxException) {
      RobotLog.logExceptionHeader("FtcWebSocket", (Exception)jsonSyntaxException, "Malformed json received from %s", new Object[] { this });
      return;
    } 
  }
  
  private void onOpen() {
    RobotLog.vv("FtcWebSocket", "Opening %s", new Object[] { this });
    ((WebSocketManagerImpl)this.manager).onWebSocketConnected(this);
  }
  
  public void close(CloseCode paramCloseCode, String paramString) {
    this.rawWebSocket.close(paramCloseCode.getValue(), paramString);
  }
  
  public int getPort() {
    return this.port;
  }
  
  public String getRemoteHostname() {
    return this.remoteHostname;
  }
  
  public InetAddress getRemoteIpAddress() {
    return this.remoteIpAddress;
  }
  
  void internalSend(FtcWebSocketMessage paramFtcWebSocketMessage) {
    String str = paramFtcWebSocketMessage.toJson();
    this.rawWebSocket.send(str);
  }
  
  public boolean isOpen() {
    return this.rawWebSocket.isOpen();
  }
  
  public void send(FtcWebSocketMessage paramFtcWebSocketMessage) {
    if (!paramFtcWebSocketMessage.getNamespace().equals("system")) {
      internalSend(paramFtcWebSocketMessage);
      return;
    } 
    throw new IllegalArgumentException("System namespace messages cannot be sent using this method.");
  }
  
  public String toString() {
    return String.format(Locale.ROOT, "websocket (ip=%s port=%d)", new Object[] { getRemoteIpAddress(), Integer.valueOf(getPort()) });
  }
  
  public static abstract class RawWebSocket {
    private FtcWebSocketImpl ftcWebSocket;
    
    public RawWebSocket(int param1Int, InetAddress param1InetAddress, String param1String, WebSocketManager param1WebSocketManager) {
      this.ftcWebSocket = new FtcWebSocketImpl(param1Int, param1InetAddress, param1String, param1WebSocketManager, this);
    }
    
    protected abstract void close(int param1Int, String param1String);
    
    protected abstract boolean isOpen();
    
    public void onClose(CloseCode param1CloseCode, String param1String, boolean param1Boolean) {
      this.ftcWebSocket.onClose(param1CloseCode, param1String, param1Boolean);
    }
    
    public void onException(Throwable param1Throwable) {
      this.ftcWebSocket.onException(param1Throwable);
    }
    
    public void onMessage(String param1String) {
      this.ftcWebSocket.onMessage(param1String);
    }
    
    public void onOpen() {
      this.ftcWebSocket.onOpen();
    }
    
    protected abstract void send(String param1String);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\websockets\FtcWebSocketImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */