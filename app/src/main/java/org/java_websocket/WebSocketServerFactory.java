package org.java_websocket;

import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import org.java_websocket.drafts.Draft;

public interface WebSocketServerFactory extends WebSocketFactory {
  void close();
  
  WebSocketImpl createWebSocket(WebSocketAdapter paramWebSocketAdapter, List<Draft> paramList);
  
  WebSocketImpl createWebSocket(WebSocketAdapter paramWebSocketAdapter, Draft paramDraft);
  
  ByteChannel wrapChannel(SocketChannel paramSocketChannel, SelectionKey paramSelectionKey) throws IOException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\WebSocketServerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */