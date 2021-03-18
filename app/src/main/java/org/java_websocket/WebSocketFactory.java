package org.java_websocket;

import java.util.List;
import org.java_websocket.drafts.Draft;

public interface WebSocketFactory {
  WebSocket createWebSocket(WebSocketAdapter paramWebSocketAdapter, List<Draft> paramList);
  
  WebSocket createWebSocket(WebSocketAdapter paramWebSocketAdapter, Draft paramDraft);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\WebSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */