package org.java_websocket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Collection;
import org.java_websocket.drafts.Draft;
import org.java_websocket.enums.Opcode;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.framing.Framedata;

public interface WebSocket {
  void close();
  
  void close(int paramInt);
  
  void close(int paramInt, String paramString);
  
  void closeConnection(int paramInt, String paramString);
  
  <T> T getAttachment();
  
  Draft getDraft();
  
  InetSocketAddress getLocalSocketAddress();
  
  ReadyState getReadyState();
  
  InetSocketAddress getRemoteSocketAddress();
  
  String getResourceDescriptor();
  
  boolean hasBufferedData();
  
  boolean isClosed();
  
  boolean isClosing();
  
  boolean isFlushAndClose();
  
  boolean isOpen();
  
  void send(String paramString);
  
  void send(ByteBuffer paramByteBuffer);
  
  void send(byte[] paramArrayOfbyte);
  
  void sendFragmentedFrame(Opcode paramOpcode, ByteBuffer paramByteBuffer, boolean paramBoolean);
  
  void sendFrame(Collection<Framedata> paramCollection);
  
  void sendFrame(Framedata paramFramedata);
  
  void sendPing();
  
  <T> void setAttachment(T paramT);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\WebSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */