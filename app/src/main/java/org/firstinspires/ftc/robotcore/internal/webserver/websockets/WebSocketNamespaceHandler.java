package org.firstinspires.ftc.robotcore.internal.webserver.websockets;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class WebSocketNamespaceHandler {
  private final Map<String, WebSocketMessageTypeHandler> messageTypeHandlerMap;
  
  private final String namespace;
  
  public WebSocketNamespaceHandler(String paramString) {
    this(paramString, null);
  }
  
  public WebSocketNamespaceHandler(String paramString, Map<String, WebSocketMessageTypeHandler> paramMap) {
    this.namespace = paramString;
    ConcurrentHashMap<Object, Object> concurrentHashMap = new ConcurrentHashMap<Object, Object>();
    this.messageTypeHandlerMap = (Map)concurrentHashMap;
    if (paramMap != null)
      concurrentHashMap.putAll(paramMap); 
    registerMessageTypeHandlers(this.messageTypeHandlerMap);
  }
  
  public final String getNamespace() {
    return this.namespace;
  }
  
  public void onMessage(FtcWebSocketMessage paramFtcWebSocketMessage, FtcWebSocket paramFtcWebSocket) {
    WebSocketMessageTypeHandler webSocketMessageTypeHandler = this.messageTypeHandlerMap.get(paramFtcWebSocketMessage.getType());
    if (webSocketMessageTypeHandler != null)
      webSocketMessageTypeHandler.handleMessage(paramFtcWebSocketMessage, paramFtcWebSocket); 
  }
  
  public void onSubscribe(FtcWebSocket paramFtcWebSocket) {}
  
  public void onUnsubscribe(FtcWebSocket paramFtcWebSocket) {}
  
  protected void registerMessageTypeHandlers(Map<String, WebSocketMessageTypeHandler> paramMap) {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\webserver\websockets\WebSocketNamespaceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */