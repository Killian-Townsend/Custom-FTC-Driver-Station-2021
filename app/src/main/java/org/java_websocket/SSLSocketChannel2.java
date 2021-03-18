package org.java_websocket;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSLSocketChannel2 implements ByteChannel, WrappedByteChannel {
  protected static ByteBuffer emptybuffer;
  
  private static final Logger log = LoggerFactory.getLogger(SSLSocketChannel2.class);
  
  protected int bufferallocations = 0;
  
  protected ExecutorService exec;
  
  protected ByteBuffer inCrypt;
  
  protected ByteBuffer inData;
  
  protected ByteBuffer outCrypt;
  
  protected SSLEngineResult readEngineResult;
  
  protected SelectionKey selectionKey;
  
  protected SocketChannel socketChannel;
  
  protected SSLEngine sslEngine;
  
  protected List<Future<?>> tasks;
  
  protected SSLEngineResult writeEngineResult;
  
  static {
    emptybuffer = ByteBuffer.allocate(0);
  }
  
  public SSLSocketChannel2(SocketChannel paramSocketChannel, SSLEngine paramSSLEngine, ExecutorService paramExecutorService, SelectionKey paramSelectionKey) throws IOException {
    if (paramSocketChannel != null && paramSSLEngine != null && paramExecutorService != null) {
      this.socketChannel = paramSocketChannel;
      this.sslEngine = paramSSLEngine;
      this.exec = paramExecutorService;
      SSLEngineResult sSLEngineResult = new SSLEngineResult(SSLEngineResult.Status.BUFFER_UNDERFLOW, paramSSLEngine.getHandshakeStatus(), 0, 0);
      this.writeEngineResult = sSLEngineResult;
      this.readEngineResult = sSLEngineResult;
      this.tasks = new ArrayList<Future<?>>(3);
      if (paramSelectionKey != null) {
        paramSelectionKey.interestOps(paramSelectionKey.interestOps() | 0x4);
        this.selectionKey = paramSelectionKey;
      } 
      createBuffers(paramSSLEngine.getSession());
      this.socketChannel.write(wrap(emptybuffer));
      processHandshake();
      return;
    } 
    throw new IllegalArgumentException("parameter must not be null");
  }
  
  private void consumeFutureUninterruptible(Future<?> paramFuture) {
    while (true) {
      try {
        paramFuture.get();
        return;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
      } catch (ExecutionException executionException) {
        throw new RuntimeException(executionException);
      } 
    } 
  }
  
  private boolean isHandShakeComplete() {
    SSLEngineResult.HandshakeStatus handshakeStatus = this.sslEngine.getHandshakeStatus();
    return (handshakeStatus == SSLEngineResult.HandshakeStatus.FINISHED || handshakeStatus == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING);
  }
  
  private void processHandshake() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield sslEngine : Ljavax/net/ssl/SSLEngine;
    //   6: invokevirtual getHandshakeStatus : ()Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   9: astore_1
    //   10: getstatic javax/net/ssl/SSLEngineResult$HandshakeStatus.NOT_HANDSHAKING : Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   13: astore_2
    //   14: aload_1
    //   15: aload_2
    //   16: if_acmpne -> 22
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: aload_0
    //   23: getfield tasks : Ljava/util/List;
    //   26: invokeinterface isEmpty : ()Z
    //   31: ifne -> 96
    //   34: aload_0
    //   35: getfield tasks : Ljava/util/List;
    //   38: invokeinterface iterator : ()Ljava/util/Iterator;
    //   43: astore_1
    //   44: aload_1
    //   45: invokeinterface hasNext : ()Z
    //   50: ifeq -> 96
    //   53: aload_1
    //   54: invokeinterface next : ()Ljava/lang/Object;
    //   59: checkcast java/util/concurrent/Future
    //   62: astore_2
    //   63: aload_2
    //   64: invokeinterface isDone : ()Z
    //   69: ifeq -> 81
    //   72: aload_1
    //   73: invokeinterface remove : ()V
    //   78: goto -> 44
    //   81: aload_0
    //   82: invokevirtual isBlocking : ()Z
    //   85: ifeq -> 93
    //   88: aload_0
    //   89: aload_2
    //   90: invokespecial consumeFutureUninterruptible : (Ljava/util/concurrent/Future;)V
    //   93: aload_0
    //   94: monitorexit
    //   95: return
    //   96: aload_0
    //   97: getfield sslEngine : Ljavax/net/ssl/SSLEngine;
    //   100: invokevirtual getHandshakeStatus : ()Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   103: getstatic javax/net/ssl/SSLEngineResult$HandshakeStatus.NEED_UNWRAP : Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   106: if_acmpne -> 210
    //   109: aload_0
    //   110: invokevirtual isBlocking : ()Z
    //   113: ifeq -> 129
    //   116: aload_0
    //   117: getfield readEngineResult : Ljavax/net/ssl/SSLEngineResult;
    //   120: invokevirtual getStatus : ()Ljavax/net/ssl/SSLEngineResult$Status;
    //   123: getstatic javax/net/ssl/SSLEngineResult$Status.BUFFER_UNDERFLOW : Ljavax/net/ssl/SSLEngineResult$Status;
    //   126: if_acmpne -> 160
    //   129: aload_0
    //   130: getfield inCrypt : Ljava/nio/ByteBuffer;
    //   133: invokevirtual compact : ()Ljava/nio/ByteBuffer;
    //   136: pop
    //   137: aload_0
    //   138: getfield socketChannel : Ljava/nio/channels/SocketChannel;
    //   141: aload_0
    //   142: getfield inCrypt : Ljava/nio/ByteBuffer;
    //   145: invokevirtual read : (Ljava/nio/ByteBuffer;)I
    //   148: iconst_m1
    //   149: if_icmpeq -> 200
    //   152: aload_0
    //   153: getfield inCrypt : Ljava/nio/ByteBuffer;
    //   156: invokevirtual flip : ()Ljava/nio/Buffer;
    //   159: pop
    //   160: aload_0
    //   161: getfield inData : Ljava/nio/ByteBuffer;
    //   164: invokevirtual compact : ()Ljava/nio/ByteBuffer;
    //   167: pop
    //   168: aload_0
    //   169: invokespecial unwrap : ()Ljava/nio/ByteBuffer;
    //   172: pop
    //   173: aload_0
    //   174: getfield readEngineResult : Ljavax/net/ssl/SSLEngineResult;
    //   177: invokevirtual getHandshakeStatus : ()Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   180: getstatic javax/net/ssl/SSLEngineResult$HandshakeStatus.FINISHED : Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   183: if_acmpne -> 210
    //   186: aload_0
    //   187: aload_0
    //   188: getfield sslEngine : Ljavax/net/ssl/SSLEngine;
    //   191: invokevirtual getSession : ()Ljavax/net/ssl/SSLSession;
    //   194: invokevirtual createBuffers : (Ljavax/net/ssl/SSLSession;)V
    //   197: aload_0
    //   198: monitorexit
    //   199: return
    //   200: new java/io/IOException
    //   203: dup
    //   204: ldc 'connection closed unexpectedly by peer'
    //   206: invokespecial <init> : (Ljava/lang/String;)V
    //   209: athrow
    //   210: aload_0
    //   211: invokevirtual consumeDelegatedTasks : ()V
    //   214: aload_0
    //   215: getfield tasks : Ljava/util/List;
    //   218: invokeinterface isEmpty : ()Z
    //   223: ifne -> 239
    //   226: aload_0
    //   227: getfield sslEngine : Ljavax/net/ssl/SSLEngine;
    //   230: invokevirtual getHandshakeStatus : ()Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   233: getstatic javax/net/ssl/SSLEngineResult$HandshakeStatus.NEED_WRAP : Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   236: if_acmpne -> 281
    //   239: aload_0
    //   240: getfield socketChannel : Ljava/nio/channels/SocketChannel;
    //   243: aload_0
    //   244: getstatic org/java_websocket/SSLSocketChannel2.emptybuffer : Ljava/nio/ByteBuffer;
    //   247: invokespecial wrap : (Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   250: invokevirtual write : (Ljava/nio/ByteBuffer;)I
    //   253: pop
    //   254: aload_0
    //   255: getfield writeEngineResult : Ljavax/net/ssl/SSLEngineResult;
    //   258: invokevirtual getHandshakeStatus : ()Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   261: getstatic javax/net/ssl/SSLEngineResult$HandshakeStatus.FINISHED : Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   264: if_acmpne -> 281
    //   267: aload_0
    //   268: aload_0
    //   269: getfield sslEngine : Ljavax/net/ssl/SSLEngine;
    //   272: invokevirtual getSession : ()Ljavax/net/ssl/SSLSession;
    //   275: invokevirtual createBuffers : (Ljavax/net/ssl/SSLSession;)V
    //   278: aload_0
    //   279: monitorexit
    //   280: return
    //   281: aload_0
    //   282: iconst_1
    //   283: putfield bufferallocations : I
    //   286: aload_0
    //   287: monitorexit
    //   288: return
    //   289: astore_1
    //   290: aload_0
    //   291: monitorexit
    //   292: aload_1
    //   293: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	289	finally
    //   22	44	289	finally
    //   44	78	289	finally
    //   81	93	289	finally
    //   96	129	289	finally
    //   129	160	289	finally
    //   160	197	289	finally
    //   200	210	289	finally
    //   210	239	289	finally
    //   239	278	289	finally
    //   281	286	289	finally
  }
  
  private int readRemaining(ByteBuffer paramByteBuffer) throws SSLException {
    if (this.inData.hasRemaining())
      return transfereTo(this.inData, paramByteBuffer); 
    if (!this.inData.hasRemaining())
      this.inData.clear(); 
    if (this.inCrypt.hasRemaining()) {
      unwrap();
      int i = transfereTo(this.inData, paramByteBuffer);
      if (this.readEngineResult.getStatus() == SSLEngineResult.Status.CLOSED)
        return -1; 
      if (i > 0)
        return i; 
    } 
    return 0;
  }
  
  private int transfereTo(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2) {
    int i = paramByteBuffer1.remaining();
    int j = paramByteBuffer2.remaining();
    if (i > j) {
      j = Math.min(i, j);
      for (i = 0; i < j; i++)
        paramByteBuffer2.put(paramByteBuffer1.get()); 
      return j;
    } 
    paramByteBuffer2.put(paramByteBuffer1);
    return i;
  }
  
  private ByteBuffer unwrap() throws SSLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield readEngineResult : Ljavax/net/ssl/SSLEngineResult;
    //   6: invokevirtual getStatus : ()Ljavax/net/ssl/SSLEngineResult$Status;
    //   9: getstatic javax/net/ssl/SSLEngineResult$Status.CLOSED : Ljavax/net/ssl/SSLEngineResult$Status;
    //   12: if_acmpne -> 36
    //   15: aload_0
    //   16: getfield sslEngine : Ljavax/net/ssl/SSLEngine;
    //   19: invokevirtual getHandshakeStatus : ()Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   22: astore_2
    //   23: getstatic javax/net/ssl/SSLEngineResult$HandshakeStatus.NOT_HANDSHAKING : Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   26: astore_3
    //   27: aload_2
    //   28: aload_3
    //   29: if_acmpne -> 36
    //   32: aload_0
    //   33: invokevirtual close : ()V
    //   36: aload_0
    //   37: getfield inData : Ljava/nio/ByteBuffer;
    //   40: invokevirtual remaining : ()I
    //   43: istore_1
    //   44: aload_0
    //   45: getfield sslEngine : Ljavax/net/ssl/SSLEngine;
    //   48: aload_0
    //   49: getfield inCrypt : Ljava/nio/ByteBuffer;
    //   52: aload_0
    //   53: getfield inData : Ljava/nio/ByteBuffer;
    //   56: invokevirtual unwrap : (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)Ljavax/net/ssl/SSLEngineResult;
    //   59: astore_2
    //   60: aload_0
    //   61: aload_2
    //   62: putfield readEngineResult : Ljavax/net/ssl/SSLEngineResult;
    //   65: aload_2
    //   66: invokevirtual getStatus : ()Ljavax/net/ssl/SSLEngineResult$Status;
    //   69: getstatic javax/net/ssl/SSLEngineResult$Status.OK : Ljavax/net/ssl/SSLEngineResult$Status;
    //   72: if_acmpne -> 99
    //   75: iload_1
    //   76: aload_0
    //   77: getfield inData : Ljava/nio/ByteBuffer;
    //   80: invokevirtual remaining : ()I
    //   83: if_icmpne -> 36
    //   86: aload_0
    //   87: getfield sslEngine : Ljavax/net/ssl/SSLEngine;
    //   90: invokevirtual getHandshakeStatus : ()Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   93: getstatic javax/net/ssl/SSLEngineResult$HandshakeStatus.NEED_UNWRAP : Ljavax/net/ssl/SSLEngineResult$HandshakeStatus;
    //   96: if_acmpeq -> 36
    //   99: aload_0
    //   100: getfield inData : Ljava/nio/ByteBuffer;
    //   103: invokevirtual flip : ()Ljava/nio/Buffer;
    //   106: pop
    //   107: aload_0
    //   108: getfield inData : Ljava/nio/ByteBuffer;
    //   111: astore_2
    //   112: aload_0
    //   113: monitorexit
    //   114: aload_2
    //   115: areturn
    //   116: astore_2
    //   117: aload_0
    //   118: monitorexit
    //   119: aload_2
    //   120: athrow
    //   121: astore_2
    //   122: goto -> 36
    // Exception table:
    //   from	to	target	type
    //   2	27	116	finally
    //   32	36	121	java/io/IOException
    //   32	36	116	finally
    //   36	99	116	finally
    //   99	112	116	finally
  }
  
  private ByteBuffer wrap(ByteBuffer paramByteBuffer) throws SSLException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield outCrypt : Ljava/nio/ByteBuffer;
    //   6: invokevirtual compact : ()Ljava/nio/ByteBuffer;
    //   9: pop
    //   10: aload_0
    //   11: aload_0
    //   12: getfield sslEngine : Ljavax/net/ssl/SSLEngine;
    //   15: aload_1
    //   16: aload_0
    //   17: getfield outCrypt : Ljava/nio/ByteBuffer;
    //   20: invokevirtual wrap : (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)Ljavax/net/ssl/SSLEngineResult;
    //   23: putfield writeEngineResult : Ljavax/net/ssl/SSLEngineResult;
    //   26: aload_0
    //   27: getfield outCrypt : Ljava/nio/ByteBuffer;
    //   30: invokevirtual flip : ()Ljava/nio/Buffer;
    //   33: pop
    //   34: aload_0
    //   35: getfield outCrypt : Ljava/nio/ByteBuffer;
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: areturn
    //   43: astore_1
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_1
    //   47: athrow
    // Exception table:
    //   from	to	target	type
    //   2	39	43	finally
  }
  
  public void close() throws IOException {
    this.sslEngine.closeOutbound();
    this.sslEngine.getSession().invalidate();
    if (this.socketChannel.isOpen())
      this.socketChannel.write(wrap(emptybuffer)); 
    this.socketChannel.close();
  }
  
  public SelectableChannel configureBlocking(boolean paramBoolean) throws IOException {
    return this.socketChannel.configureBlocking(paramBoolean);
  }
  
  public boolean connect(SocketAddress paramSocketAddress) throws IOException {
    return this.socketChannel.connect(paramSocketAddress);
  }
  
  protected void consumeDelegatedTasks() {
    while (true) {
      Runnable runnable = this.sslEngine.getDelegatedTask();
      if (runnable != null) {
        this.tasks.add(this.exec.submit(runnable));
        continue;
      } 
      break;
    } 
  }
  
  protected void createBuffers(SSLSession paramSSLSession) {
    int i = paramSSLSession.getPacketBufferSize();
    int j = Math.max(paramSSLSession.getApplicationBufferSize(), i);
    ByteBuffer byteBuffer = this.inData;
    if (byteBuffer == null) {
      this.inData = ByteBuffer.allocate(j);
      this.outCrypt = ByteBuffer.allocate(i);
      this.inCrypt = ByteBuffer.allocate(i);
    } else {
      if (byteBuffer.capacity() != j)
        this.inData = ByteBuffer.allocate(j); 
      if (this.outCrypt.capacity() != i)
        this.outCrypt = ByteBuffer.allocate(i); 
      if (this.inCrypt.capacity() != i)
        this.inCrypt = ByteBuffer.allocate(i); 
    } 
    if (this.inData.remaining() != 0 && log.isTraceEnabled())
      log.trace(new String(this.inData.array(), this.inData.position(), this.inData.remaining())); 
    this.inData.rewind();
    this.inData.flip();
    if (this.inCrypt.remaining() != 0 && log.isTraceEnabled())
      log.trace(new String(this.inCrypt.array(), this.inCrypt.position(), this.inCrypt.remaining())); 
    this.inCrypt.rewind();
    this.inCrypt.flip();
    this.outCrypt.rewind();
    this.outCrypt.flip();
    this.bufferallocations++;
  }
  
  public boolean finishConnect() throws IOException {
    return this.socketChannel.finishConnect();
  }
  
  public boolean isBlocking() {
    return this.socketChannel.isBlocking();
  }
  
  public boolean isConnected() {
    return this.socketChannel.isConnected();
  }
  
  public boolean isInboundDone() {
    return this.sslEngine.isInboundDone();
  }
  
  public boolean isNeedRead() {
    return (this.inData.hasRemaining() || (this.inCrypt.hasRemaining() && this.readEngineResult.getStatus() != SSLEngineResult.Status.BUFFER_UNDERFLOW && this.readEngineResult.getStatus() != SSLEngineResult.Status.CLOSED));
  }
  
  public boolean isNeedWrite() {
    return (this.outCrypt.hasRemaining() || !isHandShakeComplete());
  }
  
  public boolean isOpen() {
    return this.socketChannel.isOpen();
  }
  
  public int read(ByteBuffer paramByteBuffer) throws IOException {
    int i;
    while (true) {
      if (!paramByteBuffer.hasRemaining())
        return 0; 
      if (!isHandShakeComplete())
        if (isBlocking()) {
          while (!isHandShakeComplete())
            processHandshake(); 
        } else {
          processHandshake();
          if (!isHandShakeComplete())
            return 0; 
        }  
      i = readRemaining(paramByteBuffer);
      if (i != 0)
        return i; 
      this.inData.clear();
      if (!this.inCrypt.hasRemaining()) {
        this.inCrypt.clear();
      } else {
        this.inCrypt.compact();
      } 
      if ((isBlocking() || this.readEngineResult.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW) && this.socketChannel.read(this.inCrypt) == -1)
        return -1; 
      this.inCrypt.flip();
      unwrap();
      i = transfereTo(this.inData, paramByteBuffer);
      if (i == 0 && isBlocking())
        continue; 
      break;
    } 
    return i;
  }
  
  public int readMore(ByteBuffer paramByteBuffer) throws SSLException {
    return readRemaining(paramByteBuffer);
  }
  
  public Socket socket() {
    return this.socketChannel.socket();
  }
  
  public int write(ByteBuffer paramByteBuffer) throws IOException {
    if (!isHandShakeComplete()) {
      processHandshake();
      return 0;
    } 
    int i = this.socketChannel.write(wrap(paramByteBuffer));
    if (this.writeEngineResult.getStatus() != SSLEngineResult.Status.CLOSED)
      return i; 
    throw new EOFException("Connection is closed");
  }
  
  public void writeMore() throws IOException {
    write(this.outCrypt);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\SSLSocketChannel2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */