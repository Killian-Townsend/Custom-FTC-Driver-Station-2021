package org.java_websocket.server;

import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.WebSocketListener;
import org.java_websocket.WebSocketServerFactory;
import org.java_websocket.drafts.Draft;

public class DefaultWebSocketServerFactory implements WebSocketServerFactory {
  public void close() {}
  
  public WebSocketImpl createWebSocket(WebSocketAdapter paramWebSocketAdapter, List<Draft> paramList) {
    return new WebSocketImpl((WebSocketListener)paramWebSocketAdapter, paramList);
  }
  
  public WebSocketImpl createWebSocket(WebSocketAdapter paramWebSocketAdapter, Draft paramDraft) {
    return new WebSocketImpl((WebSocketListener)paramWebSocketAdapter, paramDraft);
  }
  
  public SocketChannel wrapChannel(SocketChannel paramSocketChannel, SelectionKey paramSelectionKey) {
    return paramSocketChannel;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\server\DefaultWebSocketServerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */