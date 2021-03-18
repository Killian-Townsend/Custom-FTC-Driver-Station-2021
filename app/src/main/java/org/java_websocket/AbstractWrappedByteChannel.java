package org.java_websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;

@Deprecated
public class AbstractWrappedByteChannel implements WrappedByteChannel {
  private final ByteChannel channel;
  
  @Deprecated
  public AbstractWrappedByteChannel(ByteChannel paramByteChannel) {
    this.channel = paramByteChannel;
  }
  
  @Deprecated
  public AbstractWrappedByteChannel(WrappedByteChannel paramWrappedByteChannel) {
    this.channel = paramWrappedByteChannel;
  }
  
  public void close() throws IOException {
    this.channel.close();
  }
  
  public boolean isBlocking() {
    ByteChannel byteChannel = this.channel;
    return (byteChannel instanceof SocketChannel) ? ((SocketChannel)byteChannel).isBlocking() : ((byteChannel instanceof WrappedByteChannel) ? ((WrappedByteChannel)byteChannel).isBlocking() : false);
  }
  
  public boolean isNeedRead() {
    ByteChannel byteChannel = this.channel;
    return (byteChannel instanceof WrappedByteChannel && ((WrappedByteChannel)byteChannel).isNeedRead());
  }
  
  public boolean isNeedWrite() {
    ByteChannel byteChannel = this.channel;
    return (byteChannel instanceof WrappedByteChannel && ((WrappedByteChannel)byteChannel).isNeedWrite());
  }
  
  public boolean isOpen() {
    return this.channel.isOpen();
  }
  
  public int read(ByteBuffer paramByteBuffer) throws IOException {
    return this.channel.read(paramByteBuffer);
  }
  
  public int readMore(ByteBuffer paramByteBuffer) throws IOException {
    ByteChannel byteChannel = this.channel;
    return (byteChannel instanceof WrappedByteChannel) ? ((WrappedByteChannel)byteChannel).readMore(paramByteBuffer) : 0;
  }
  
  public int write(ByteBuffer paramByteBuffer) throws IOException {
    return this.channel.write(paramByteBuffer);
  }
  
  public void writeMore() throws IOException {
    ByteChannel byteChannel = this.channel;
    if (byteChannel instanceof WrappedByteChannel)
      ((WrappedByteChannel)byteChannel).writeMore(); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\AbstractWrappedByteChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */