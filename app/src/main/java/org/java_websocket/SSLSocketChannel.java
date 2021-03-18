package org.java_websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSLSocketChannel implements WrappedByteChannel, ByteChannel {
  private static final Logger log = LoggerFactory.getLogger(SSLSocketChannel.class);
  
  private final SSLEngine engine;
  
  private ExecutorService executor;
  
  private ByteBuffer myAppData;
  
  private ByteBuffer myNetData;
  
  private ByteBuffer peerAppData;
  
  private ByteBuffer peerNetData;
  
  private final SocketChannel socketChannel;
  
  public SSLSocketChannel(SocketChannel paramSocketChannel, SSLEngine paramSSLEngine, ExecutorService paramExecutorService, SelectionKey paramSelectionKey) throws IOException {
    if (paramSocketChannel != null && paramSSLEngine != null && this.executor != paramExecutorService) {
      this.socketChannel = paramSocketChannel;
      this.engine = paramSSLEngine;
      this.executor = paramExecutorService;
      this.myNetData = ByteBuffer.allocate(paramSSLEngine.getSession().getPacketBufferSize());
      this.peerNetData = ByteBuffer.allocate(this.engine.getSession().getPacketBufferSize());
      this.engine.beginHandshake();
      if (doHandshake()) {
        if (paramSelectionKey != null) {
          paramSelectionKey.interestOps(paramSelectionKey.interestOps() | 0x4);
          return;
        } 
      } else {
        try {
          this.socketChannel.close();
          return;
        } catch (IOException iOException) {
          log.error("Exception during the closing of the channel", iOException);
        } 
      } 
      return;
    } 
    throw new IllegalArgumentException("parameter must not be null");
  }
  
  private void closeConnection() throws IOException {
    this.engine.closeOutbound();
    try {
      doHandshake();
    } catch (IOException iOException) {}
    this.socketChannel.close();
  }
  
  private boolean doHandshake() throws IOException {
    int i = this.engine.getSession().getApplicationBufferSize();
    this.myAppData = ByteBuffer.allocate(i);
    this.peerAppData = ByteBuffer.allocate(i);
    this.myNetData.clear();
    this.peerNetData.clear();
    SSLEngineResult.HandshakeStatus handshakeStatus = this.engine.getHandshakeStatus();
    i = 0;
    label89: while (true) {
      if (i == 0) {
        int j = null.$SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[handshakeStatus.ordinal()];
        if (j != 1) {
          if (j != 2) {
            if (j != 3) {
              if (j != 4) {
                if (j == 5)
                  continue; 
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid SSL status: ");
                stringBuilder.append(handshakeStatus);
                throw new IllegalStateException(stringBuilder.toString());
              } 
              while (true) {
                Runnable runnable = this.engine.getDelegatedTask();
                if (runnable != null) {
                  this.executor.execute(runnable);
                  continue;
                } 
                SSLEngineResult.HandshakeStatus handshakeStatus1 = this.engine.getHandshakeStatus();
              } 
              break;
            } 
            this.myNetData.clear();
            try {
              StringBuilder stringBuilder;
              SSLEngineResult sSLEngineResult = this.engine.wrap(this.myAppData, this.myNetData);
              SSLEngineResult.HandshakeStatus handshakeStatus1 = sSLEngineResult.getHandshakeStatus();
              j = null.$SwitchMap$javax$net$ssl$SSLEngineResult$Status[sSLEngineResult.getStatus().ordinal()];
              if (j != 1) {
                if (j != 2) {
                  if (j != 3) {
                    SSLEngineResult.HandshakeStatus handshakeStatus2;
                    if (j == 4) {
                      try {
                        this.myNetData.flip();
                        while (this.myNetData.hasRemaining())
                          this.socketChannel.write(this.myNetData); 
                        this.peerNetData.clear();
                        handshakeStatus2 = handshakeStatus1;
                      } catch (Exception exception) {
                        handshakeStatus2 = this.engine.getHandshakeStatus();
                      } 
                      continue;
                    } 
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid SSL status: ");
                    stringBuilder.append(handshakeStatus2.getStatus());
                    throw new IllegalStateException(stringBuilder.toString());
                  } 
                  this.myNetData = enlargePacketBuffer(this.myNetData);
                  StringBuilder stringBuilder1 = stringBuilder;
                  continue;
                } 
                throw new SSLException("Buffer underflow occured after a wrap. I don't think we should ever get here.");
              } 
              this.myNetData.flip();
              while (true) {
                StringBuilder stringBuilder1 = stringBuilder;
                if (this.myNetData.hasRemaining()) {
                  this.socketChannel.write(this.myNetData);
                  continue;
                } 
                continue label89;
              } 
              break;
            } catch (SSLException sSLException) {
              this.engine.closeOutbound();
              SSLEngineResult.HandshakeStatus handshakeStatus1 = this.engine.getHandshakeStatus();
              continue;
            } 
          } 
          if (this.socketChannel.read(this.peerNetData) < 0) {
            if (this.engine.isInboundDone() && this.engine.isOutboundDone())
              return false; 
            try {
              this.engine.closeInbound();
            } catch (SSLException sSLException) {}
            this.engine.closeOutbound();
            handshakeStatus = this.engine.getHandshakeStatus();
            continue;
          } 
          this.peerNetData.flip();
          try {
            SSLEngineResult sSLEngineResult = this.engine.unwrap(this.peerNetData, this.peerAppData);
            this.peerNetData.compact();
            SSLEngineResult.HandshakeStatus handshakeStatus1 = sSLEngineResult.getHandshakeStatus();
            j = null.$SwitchMap$javax$net$ssl$SSLEngineResult$Status[sSLEngineResult.getStatus().ordinal()];
            handshakeStatus = handshakeStatus1;
            if (j != 1) {
              if (j != 2) {
                if (j != 3) {
                  if (j == 4) {
                    if (this.engine.isOutboundDone())
                      return false; 
                    this.engine.closeOutbound();
                    handshakeStatus = this.engine.getHandshakeStatus();
                    continue;
                  } 
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append("Invalid SSL status: ");
                  stringBuilder.append(sSLEngineResult.getStatus());
                  throw new IllegalStateException(stringBuilder.toString());
                } 
                this.peerAppData = enlargeApplicationBuffer(this.peerAppData);
                handshakeStatus = handshakeStatus1;
                continue;
              } 
              this.peerNetData = handleBufferUnderflow(this.peerNetData);
              handshakeStatus = handshakeStatus1;
            } 
          } catch (SSLException sSLException) {
            this.engine.closeOutbound();
            SSLEngineResult.HandshakeStatus handshakeStatus1 = this.engine.getHandshakeStatus();
          } 
          continue;
        } 
        i = this.peerNetData.hasRemaining() ^ true;
        if (i != 0)
          return true; 
        this.socketChannel.write(this.peerNetData);
        continue;
      } 
      return true;
    } 
  }
  
  private ByteBuffer enlargeApplicationBuffer(ByteBuffer paramByteBuffer) {
    return enlargeBuffer(paramByteBuffer, this.engine.getSession().getApplicationBufferSize());
  }
  
  private ByteBuffer enlargeBuffer(ByteBuffer paramByteBuffer, int paramInt) {
    return (paramInt > paramByteBuffer.capacity()) ? ByteBuffer.allocate(paramInt) : ByteBuffer.allocate(paramByteBuffer.capacity() * 2);
  }
  
  private ByteBuffer enlargePacketBuffer(ByteBuffer paramByteBuffer) {
    return enlargeBuffer(paramByteBuffer, this.engine.getSession().getPacketBufferSize());
  }
  
  private ByteBuffer handleBufferUnderflow(ByteBuffer paramByteBuffer) {
    if (this.engine.getSession().getPacketBufferSize() < paramByteBuffer.limit())
      return paramByteBuffer; 
    ByteBuffer byteBuffer = enlargePacketBuffer(paramByteBuffer);
    paramByteBuffer.flip();
    byteBuffer.put(paramByteBuffer);
    return byteBuffer;
  }
  
  private void handleEndOfStream() throws IOException {
    try {
      this.engine.closeInbound();
    } catch (Exception exception) {
      log.error("This engine was forced to close inbound, without having received the proper SSL/TLS close notification message from the peer, due to end of stream.");
    } 
    closeConnection();
  }
  
  public void close() throws IOException {
    closeConnection();
  }
  
  public boolean isBlocking() {
    return this.socketChannel.isBlocking();
  }
  
  public boolean isNeedRead() {
    return (this.peerNetData.hasRemaining() || this.peerAppData.hasRemaining());
  }
  
  public boolean isNeedWrite() {
    return false;
  }
  
  public boolean isOpen() {
    return this.socketChannel.isOpen();
  }
  
  public int read(ByteBuffer paramByteBuffer) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual hasRemaining : ()Z
    //   6: istore_3
    //   7: iload_3
    //   8: ifne -> 15
    //   11: aload_0
    //   12: monitorexit
    //   13: iconst_0
    //   14: ireturn
    //   15: aload_0
    //   16: getfield peerAppData : Ljava/nio/ByteBuffer;
    //   19: invokevirtual hasRemaining : ()Z
    //   22: ifeq -> 46
    //   25: aload_0
    //   26: getfield peerAppData : Ljava/nio/ByteBuffer;
    //   29: invokevirtual flip : ()Ljava/nio/Buffer;
    //   32: pop
    //   33: aload_0
    //   34: getfield peerAppData : Ljava/nio/ByteBuffer;
    //   37: aload_1
    //   38: invokestatic transferByteBuffer : (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
    //   41: istore_2
    //   42: aload_0
    //   43: monitorexit
    //   44: iload_2
    //   45: ireturn
    //   46: aload_0
    //   47: getfield peerNetData : Ljava/nio/ByteBuffer;
    //   50: invokevirtual compact : ()Ljava/nio/ByteBuffer;
    //   53: pop
    //   54: aload_0
    //   55: getfield socketChannel : Ljava/nio/channels/SocketChannel;
    //   58: aload_0
    //   59: getfield peerNetData : Ljava/nio/ByteBuffer;
    //   62: invokevirtual read : (Ljava/nio/ByteBuffer;)I
    //   65: istore_2
    //   66: iload_2
    //   67: ifgt -> 94
    //   70: aload_0
    //   71: getfield peerNetData : Ljava/nio/ByteBuffer;
    //   74: invokevirtual hasRemaining : ()Z
    //   77: ifeq -> 83
    //   80: goto -> 94
    //   83: iload_2
    //   84: ifge -> 299
    //   87: aload_0
    //   88: invokespecial handleEndOfStream : ()V
    //   91: goto -> 299
    //   94: aload_0
    //   95: getfield peerNetData : Ljava/nio/ByteBuffer;
    //   98: invokevirtual flip : ()Ljava/nio/Buffer;
    //   101: pop
    //   102: aload_0
    //   103: getfield peerNetData : Ljava/nio/ByteBuffer;
    //   106: invokevirtual hasRemaining : ()Z
    //   109: ifeq -> 299
    //   112: aload_0
    //   113: getfield peerAppData : Ljava/nio/ByteBuffer;
    //   116: invokevirtual compact : ()Ljava/nio/ByteBuffer;
    //   119: pop
    //   120: aload_0
    //   121: getfield engine : Ljavax/net/ssl/SSLEngine;
    //   124: aload_0
    //   125: getfield peerNetData : Ljava/nio/ByteBuffer;
    //   128: aload_0
    //   129: getfield peerAppData : Ljava/nio/ByteBuffer;
    //   132: invokevirtual unwrap : (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)Ljavax/net/ssl/SSLEngineResult;
    //   135: astore #4
    //   137: getstatic org/java_websocket/SSLSocketChannel$1.$SwitchMap$javax$net$ssl$SSLEngineResult$Status : [I
    //   140: aload #4
    //   142: invokevirtual getStatus : ()Ljavax/net/ssl/SSLEngineResult$Status;
    //   145: invokevirtual ordinal : ()I
    //   148: iaload
    //   149: istore_2
    //   150: iload_2
    //   151: iconst_1
    //   152: if_icmpeq -> 263
    //   155: iload_2
    //   156: iconst_2
    //   157: if_icmpeq -> 242
    //   160: iload_2
    //   161: iconst_3
    //   162: if_icmpeq -> 220
    //   165: iload_2
    //   166: iconst_4
    //   167: if_icmpne -> 183
    //   170: aload_0
    //   171: invokespecial closeConnection : ()V
    //   174: aload_1
    //   175: invokevirtual clear : ()Ljava/nio/Buffer;
    //   178: pop
    //   179: aload_0
    //   180: monitorexit
    //   181: iconst_m1
    //   182: ireturn
    //   183: new java/lang/StringBuilder
    //   186: dup
    //   187: invokespecial <init> : ()V
    //   190: astore_1
    //   191: aload_1
    //   192: ldc 'Invalid SSL status: '
    //   194: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   197: pop
    //   198: aload_1
    //   199: aload #4
    //   201: invokevirtual getStatus : ()Ljavax/net/ssl/SSLEngineResult$Status;
    //   204: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   207: pop
    //   208: new java/lang/IllegalStateException
    //   211: dup
    //   212: aload_1
    //   213: invokevirtual toString : ()Ljava/lang/String;
    //   216: invokespecial <init> : (Ljava/lang/String;)V
    //   219: athrow
    //   220: aload_0
    //   221: aload_0
    //   222: aload_0
    //   223: getfield peerAppData : Ljava/nio/ByteBuffer;
    //   226: invokespecial enlargeApplicationBuffer : (Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   229: putfield peerAppData : Ljava/nio/ByteBuffer;
    //   232: aload_0
    //   233: aload_1
    //   234: invokevirtual read : (Ljava/nio/ByteBuffer;)I
    //   237: istore_2
    //   238: aload_0
    //   239: monitorexit
    //   240: iload_2
    //   241: ireturn
    //   242: aload_0
    //   243: getfield peerAppData : Ljava/nio/ByteBuffer;
    //   246: invokevirtual flip : ()Ljava/nio/Buffer;
    //   249: pop
    //   250: aload_0
    //   251: getfield peerAppData : Ljava/nio/ByteBuffer;
    //   254: aload_1
    //   255: invokestatic transferByteBuffer : (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
    //   258: istore_2
    //   259: aload_0
    //   260: monitorexit
    //   261: iload_2
    //   262: ireturn
    //   263: aload_0
    //   264: getfield peerAppData : Ljava/nio/ByteBuffer;
    //   267: invokevirtual flip : ()Ljava/nio/Buffer;
    //   270: pop
    //   271: aload_0
    //   272: getfield peerAppData : Ljava/nio/ByteBuffer;
    //   275: aload_1
    //   276: invokestatic transferByteBuffer : (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
    //   279: istore_2
    //   280: aload_0
    //   281: monitorexit
    //   282: iload_2
    //   283: ireturn
    //   284: astore_1
    //   285: getstatic org/java_websocket/SSLSocketChannel.log : Lorg/slf4j/Logger;
    //   288: ldc_w 'SSLExcpetion during unwrap'
    //   291: aload_1
    //   292: invokeinterface error : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   297: aload_1
    //   298: athrow
    //   299: aload_0
    //   300: getfield peerAppData : Ljava/nio/ByteBuffer;
    //   303: aload_1
    //   304: invokestatic transferByteBuffer : (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
    //   307: pop
    //   308: aload_0
    //   309: monitorexit
    //   310: iload_2
    //   311: ireturn
    //   312: astore_1
    //   313: aload_0
    //   314: monitorexit
    //   315: aload_1
    //   316: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	312	finally
    //   15	42	312	finally
    //   46	66	312	finally
    //   70	80	312	finally
    //   87	91	312	finally
    //   94	120	312	finally
    //   120	137	284	javax/net/ssl/SSLException
    //   120	137	312	finally
    //   137	150	312	finally
    //   170	179	312	finally
    //   183	220	312	finally
    //   220	238	312	finally
    //   242	259	312	finally
    //   263	280	312	finally
    //   285	299	312	finally
    //   299	308	312	finally
  }
  
  public int readMore(ByteBuffer paramByteBuffer) throws IOException {
    return read(paramByteBuffer);
  }
  
  public int write(ByteBuffer paramByteBuffer) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_2
    //   4: aload_1
    //   5: invokevirtual hasRemaining : ()Z
    //   8: ifeq -> 177
    //   11: aload_0
    //   12: getfield myNetData : Ljava/nio/ByteBuffer;
    //   15: invokevirtual clear : ()Ljava/nio/Buffer;
    //   18: pop
    //   19: aload_0
    //   20: getfield engine : Ljavax/net/ssl/SSLEngine;
    //   23: aload_1
    //   24: aload_0
    //   25: getfield myNetData : Ljava/nio/ByteBuffer;
    //   28: invokevirtual wrap : (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)Ljavax/net/ssl/SSLEngineResult;
    //   31: astore #4
    //   33: getstatic org/java_websocket/SSLSocketChannel$1.$SwitchMap$javax$net$ssl$SSLEngineResult$Status : [I
    //   36: aload #4
    //   38: invokevirtual getStatus : ()Ljavax/net/ssl/SSLEngineResult$Status;
    //   41: invokevirtual ordinal : ()I
    //   44: iaload
    //   45: istore_3
    //   46: iload_3
    //   47: iconst_1
    //   48: if_icmpeq -> 136
    //   51: iload_3
    //   52: iconst_2
    //   53: if_icmpeq -> 126
    //   56: iload_3
    //   57: iconst_3
    //   58: if_icmpeq -> 111
    //   61: iload_3
    //   62: iconst_4
    //   63: if_icmpne -> 74
    //   66: aload_0
    //   67: invokespecial closeConnection : ()V
    //   70: aload_0
    //   71: monitorexit
    //   72: iconst_0
    //   73: ireturn
    //   74: new java/lang/StringBuilder
    //   77: dup
    //   78: invokespecial <init> : ()V
    //   81: astore_1
    //   82: aload_1
    //   83: ldc 'Invalid SSL status: '
    //   85: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   88: pop
    //   89: aload_1
    //   90: aload #4
    //   92: invokevirtual getStatus : ()Ljavax/net/ssl/SSLEngineResult$Status;
    //   95: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   98: pop
    //   99: new java/lang/IllegalStateException
    //   102: dup
    //   103: aload_1
    //   104: invokevirtual toString : ()Ljava/lang/String;
    //   107: invokespecial <init> : (Ljava/lang/String;)V
    //   110: athrow
    //   111: aload_0
    //   112: aload_0
    //   113: aload_0
    //   114: getfield myNetData : Ljava/nio/ByteBuffer;
    //   117: invokespecial enlargePacketBuffer : (Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   120: putfield myNetData : Ljava/nio/ByteBuffer;
    //   123: goto -> 4
    //   126: new javax/net/ssl/SSLException
    //   129: dup
    //   130: ldc 'Buffer underflow occured after a wrap. I don't think we should ever get here.'
    //   132: invokespecial <init> : (Ljava/lang/String;)V
    //   135: athrow
    //   136: aload_0
    //   137: getfield myNetData : Ljava/nio/ByteBuffer;
    //   140: invokevirtual flip : ()Ljava/nio/Buffer;
    //   143: pop
    //   144: iload_2
    //   145: istore_3
    //   146: iload_3
    //   147: istore_2
    //   148: aload_0
    //   149: getfield myNetData : Ljava/nio/ByteBuffer;
    //   152: invokevirtual hasRemaining : ()Z
    //   155: ifeq -> 4
    //   158: aload_0
    //   159: getfield socketChannel : Ljava/nio/channels/SocketChannel;
    //   162: aload_0
    //   163: getfield myNetData : Ljava/nio/ByteBuffer;
    //   166: invokevirtual write : (Ljava/nio/ByteBuffer;)I
    //   169: istore_2
    //   170: iload_3
    //   171: iload_2
    //   172: iadd
    //   173: istore_3
    //   174: goto -> 146
    //   177: aload_0
    //   178: monitorexit
    //   179: iload_2
    //   180: ireturn
    //   181: astore_1
    //   182: aload_0
    //   183: monitorexit
    //   184: aload_1
    //   185: athrow
    // Exception table:
    //   from	to	target	type
    //   4	46	181	finally
    //   66	70	181	finally
    //   74	111	181	finally
    //   111	123	181	finally
    //   126	136	181	finally
    //   136	144	181	finally
    //   148	170	181	finally
  }
  
  public void writeMore() throws IOException {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\SSLSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */