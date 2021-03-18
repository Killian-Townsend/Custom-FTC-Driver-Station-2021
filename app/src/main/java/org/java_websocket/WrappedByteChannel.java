package org.java_websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

public interface WrappedByteChannel extends ByteChannel {
  boolean isBlocking();
  
  boolean isNeedRead();
  
  boolean isNeedWrite();
  
  int readMore(ByteBuffer paramByteBuffer) throws IOException;
  
  void writeMore() throws IOException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\WrappedByteChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */