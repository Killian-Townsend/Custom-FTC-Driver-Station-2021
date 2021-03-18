package org.java_websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

public class SocketChannelIOHelper {
  private SocketChannelIOHelper() {
    throw new IllegalStateException("Utility class");
  }
  
  public static boolean batch(WebSocketImpl paramWebSocketImpl, ByteChannel paramByteChannel) throws IOException {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_0
    //   3: ifnonnull -> 8
    //   6: iconst_0
    //   7: ireturn
    //   8: aload_0
    //   9: getfield outQueue : Ljava/util/concurrent/BlockingQueue;
    //   12: invokeinterface peek : ()Ljava/lang/Object;
    //   17: checkcast java/nio/ByteBuffer
    //   20: astore #5
    //   22: aconst_null
    //   23: astore #4
    //   25: aload #5
    //   27: astore_3
    //   28: aload #5
    //   30: ifnonnull -> 75
    //   33: aload #4
    //   35: astore_3
    //   36: aload_1
    //   37: instanceof org/java_websocket/WrappedByteChannel
    //   40: ifeq -> 127
    //   43: aload_1
    //   44: checkcast org/java_websocket/WrappedByteChannel
    //   47: astore #4
    //   49: aload #4
    //   51: astore_3
    //   52: aload #4
    //   54: invokeinterface isNeedWrite : ()Z
    //   59: ifeq -> 127
    //   62: aload #4
    //   64: invokeinterface writeMore : ()V
    //   69: aload #4
    //   71: astore_3
    //   72: goto -> 127
    //   75: aload_1
    //   76: aload_3
    //   77: invokeinterface write : (Ljava/nio/ByteBuffer;)I
    //   82: pop
    //   83: aload_3
    //   84: invokevirtual remaining : ()I
    //   87: ifle -> 92
    //   90: iconst_0
    //   91: ireturn
    //   92: aload_0
    //   93: getfield outQueue : Ljava/util/concurrent/BlockingQueue;
    //   96: invokeinterface poll : ()Ljava/lang/Object;
    //   101: pop
    //   102: aload_0
    //   103: getfield outQueue : Ljava/util/concurrent/BlockingQueue;
    //   106: invokeinterface peek : ()Ljava/lang/Object;
    //   111: checkcast java/nio/ByteBuffer
    //   114: astore #5
    //   116: aload #5
    //   118: astore_3
    //   119: aload #5
    //   121: ifnonnull -> 75
    //   124: aload #4
    //   126: astore_3
    //   127: aload_0
    //   128: getfield outQueue : Ljava/util/concurrent/BlockingQueue;
    //   131: invokeinterface isEmpty : ()Z
    //   136: ifeq -> 180
    //   139: aload_0
    //   140: invokevirtual isFlushAndClose : ()Z
    //   143: ifeq -> 180
    //   146: aload_0
    //   147: invokevirtual getDraft : ()Lorg/java_websocket/drafts/Draft;
    //   150: ifnull -> 180
    //   153: aload_0
    //   154: invokevirtual getDraft : ()Lorg/java_websocket/drafts/Draft;
    //   157: invokevirtual getRole : ()Lorg/java_websocket/enums/Role;
    //   160: ifnull -> 180
    //   163: aload_0
    //   164: invokevirtual getDraft : ()Lorg/java_websocket/drafts/Draft;
    //   167: invokevirtual getRole : ()Lorg/java_websocket/enums/Role;
    //   170: getstatic org/java_websocket/enums/Role.SERVER : Lorg/java_websocket/enums/Role;
    //   173: if_acmpne -> 180
    //   176: aload_0
    //   177: invokevirtual closeConnection : ()V
    //   180: aload_3
    //   181: ifnull -> 196
    //   184: aload_1
    //   185: checkcast org/java_websocket/WrappedByteChannel
    //   188: invokeinterface isNeedWrite : ()Z
    //   193: ifne -> 198
    //   196: iconst_1
    //   197: istore_2
    //   198: iload_2
    //   199: ireturn
  }
  
  public static boolean read(ByteBuffer paramByteBuffer, WebSocketImpl paramWebSocketImpl, ByteChannel paramByteChannel) throws IOException {
    paramByteBuffer.clear();
    int i = paramByteChannel.read(paramByteBuffer);
    paramByteBuffer.flip();
    boolean bool = false;
    if (i == -1) {
      paramWebSocketImpl.eot();
      return false;
    } 
    if (i != 0)
      bool = true; 
    return bool;
  }
  
  public static boolean readMore(ByteBuffer paramByteBuffer, WebSocketImpl paramWebSocketImpl, WrappedByteChannel paramWrappedByteChannel) throws IOException {
    paramByteBuffer.clear();
    int i = paramWrappedByteChannel.readMore(paramByteBuffer);
    paramByteBuffer.flip();
    if (i == -1) {
      paramWebSocketImpl.eot();
      return false;
    } 
    return paramWrappedByteChannel.isNeedRead();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\SocketChannelIOHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */