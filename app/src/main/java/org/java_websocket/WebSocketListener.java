package org.java_websocket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;

public interface WebSocketListener {
  InetSocketAddress getLocalSocketAddress(WebSocket paramWebSocket);
  
  InetSocketAddress getRemoteSocketAddress(WebSocket paramWebSocket);
  
  void onWebsocketClose(WebSocket paramWebSocket, int paramInt, String paramString, boolean paramBoolean);
  
  void onWebsocketCloseInitiated(WebSocket paramWebSocket, int paramInt, String paramString);
  
  void onWebsocketClosing(WebSocket paramWebSocket, int paramInt, String paramString, boolean paramBoolean);
  
  void onWebsocketError(WebSocket paramWebSocket, Exception paramException);
  
  void onWebsocketHandshakeReceivedAsClient(WebSocket paramWebSocket, ClientHandshake paramClientHandshake, ServerHandshake paramServerHandshake) throws InvalidDataException;
  
  ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket paramWebSocket, Draft paramDraft, ClientHandshake paramClientHandshake) throws InvalidDataException;
  
  void onWebsocketHandshakeSentAsClient(WebSocket paramWebSocket, ClientHandshake paramClientHandshake) throws InvalidDataException;
  
  void onWebsocketMessage(WebSocket paramWebSocket, String paramString);
  
  void onWebsocketMessage(WebSocket paramWebSocket, ByteBuffer paramByteBuffer);
  
  void onWebsocketOpen(WebSocket paramWebSocket, Handshakedata paramHandshakedata);
  
  void onWebsocketPing(WebSocket paramWebSocket, Framedata paramFramedata);
  
  void onWebsocketPong(WebSocket paramWebSocket, Framedata paramFramedata);
  
  void onWriteDemand(WebSocket paramWebSocket);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\WebSocketListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */