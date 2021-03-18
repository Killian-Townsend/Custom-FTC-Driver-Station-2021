package org.java_websocket.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import org.java_websocket.AbstractWebSocket;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.WebSocketListener;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.enums.Opcode;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;

public abstract class WebSocketClient extends AbstractWebSocket implements Runnable, WebSocket {
  private CountDownLatch closeLatch = new CountDownLatch(1);
  
  private CountDownLatch connectLatch = new CountDownLatch(1);
  
  private Thread connectReadThread;
  
  private int connectTimeout = 0;
  
  private Draft draft;
  
  private WebSocketImpl engine = null;
  
  private Map<String, String> headers;
  
  private OutputStream ostream;
  
  private Proxy proxy = Proxy.NO_PROXY;
  
  private Socket socket = null;
  
  private SocketFactory socketFactory = null;
  
  protected URI uri = null;
  
  private Thread writeThread;
  
  public WebSocketClient(URI paramURI) {
    this(paramURI, (Draft)new Draft_6455());
  }
  
  public WebSocketClient(URI paramURI, Map<String, String> paramMap) {
    this(paramURI, (Draft)new Draft_6455(), paramMap);
  }
  
  public WebSocketClient(URI paramURI, Draft paramDraft) {
    this(paramURI, paramDraft, (Map<String, String>)null, 0);
  }
  
  public WebSocketClient(URI paramURI, Draft paramDraft, Map<String, String> paramMap) {
    this(paramURI, paramDraft, paramMap, 0);
  }
  
  public WebSocketClient(URI paramURI, Draft paramDraft, Map<String, String> paramMap, int paramInt) {
    if (paramURI != null) {
      if (paramDraft != null) {
        this.uri = paramURI;
        this.draft = paramDraft;
        if (paramMap != null) {
          TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
          this.headers = (Map)treeMap;
          treeMap.putAll(paramMap);
        } 
        this.connectTimeout = paramInt;
        setTcpNoDelay(false);
        setReuseAddr(false);
        this.engine = new WebSocketImpl((WebSocketListener)this, paramDraft);
        return;
      } 
      throw new IllegalArgumentException("null as draft is permitted for `WebSocketServer` only!");
    } 
    throw new IllegalArgumentException();
  }
  
  private int getPort() {
    int i = this.uri.getPort();
    if (i == -1) {
      String str = this.uri.getScheme();
      if ("wss".equals(str))
        return 443; 
      if ("ws".equals(str))
        return 80; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unknown scheme: ");
      stringBuilder.append(str);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    return i;
  }
  
  private void handleIOException(IOException paramIOException) {
    if (paramIOException instanceof javax.net.ssl.SSLException)
      onError(paramIOException); 
    this.engine.eot();
  }
  
  private void reset() {
    Thread thread = Thread.currentThread();
    if (thread != this.writeThread && thread != this.connectReadThread)
      try {
        closeBlocking();
        if (this.writeThread != null) {
          this.writeThread.interrupt();
          this.writeThread = null;
        } 
        if (this.connectReadThread != null) {
          this.connectReadThread.interrupt();
          this.connectReadThread = null;
        } 
        this.draft.reset();
        if (this.socket != null) {
          this.socket.close();
          this.socket = null;
        } 
        this.connectLatch = new CountDownLatch(1);
        this.closeLatch = new CountDownLatch(1);
        this.engine = new WebSocketImpl((WebSocketListener)this, this.draft);
        return;
      } catch (Exception exception) {
        onError(exception);
        this.engine.closeConnection(1006, exception.getMessage());
        return;
      }  
    throw new IllegalStateException("You cannot initialize a reconnect out of the websocket thread. Use reconnect in another thread to insure a successful cleanup.");
  }
  
  private void sendHandshake() throws InvalidHandshakeException {
    // Byte code:
    //   0: aload_0
    //   1: getfield uri : Ljava/net/URI;
    //   4: invokevirtual getRawPath : ()Ljava/lang/String;
    //   7: astore_3
    //   8: aload_0
    //   9: getfield uri : Ljava/net/URI;
    //   12: invokevirtual getRawQuery : ()Ljava/lang/String;
    //   15: astore #4
    //   17: aload_3
    //   18: ifnull -> 30
    //   21: aload_3
    //   22: astore_2
    //   23: aload_3
    //   24: invokevirtual length : ()I
    //   27: ifne -> 33
    //   30: ldc '/'
    //   32: astore_2
    //   33: aload_2
    //   34: astore_3
    //   35: aload #4
    //   37: ifnull -> 73
    //   40: new java/lang/StringBuilder
    //   43: dup
    //   44: invokespecial <init> : ()V
    //   47: astore_3
    //   48: aload_3
    //   49: aload_2
    //   50: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: pop
    //   54: aload_3
    //   55: bipush #63
    //   57: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   60: pop
    //   61: aload_3
    //   62: aload #4
    //   64: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: pop
    //   68: aload_3
    //   69: invokevirtual toString : ()Ljava/lang/String;
    //   72: astore_3
    //   73: aload_0
    //   74: invokespecial getPort : ()I
    //   77: istore_1
    //   78: new java/lang/StringBuilder
    //   81: dup
    //   82: invokespecial <init> : ()V
    //   85: astore #4
    //   87: aload #4
    //   89: aload_0
    //   90: getfield uri : Ljava/net/URI;
    //   93: invokevirtual getHost : ()Ljava/lang/String;
    //   96: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: iload_1
    //   101: bipush #80
    //   103: if_icmpeq -> 142
    //   106: iload_1
    //   107: sipush #443
    //   110: if_icmpeq -> 142
    //   113: new java/lang/StringBuilder
    //   116: dup
    //   117: invokespecial <init> : ()V
    //   120: astore_2
    //   121: aload_2
    //   122: ldc ':'
    //   124: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   127: pop
    //   128: aload_2
    //   129: iload_1
    //   130: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   133: pop
    //   134: aload_2
    //   135: invokevirtual toString : ()Ljava/lang/String;
    //   138: astore_2
    //   139: goto -> 145
    //   142: ldc ''
    //   144: astore_2
    //   145: aload #4
    //   147: aload_2
    //   148: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: pop
    //   152: aload #4
    //   154: invokevirtual toString : ()Ljava/lang/String;
    //   157: astore #4
    //   159: new org/java_websocket/handshake/HandshakeImpl1Client
    //   162: dup
    //   163: invokespecial <init> : ()V
    //   166: astore_2
    //   167: aload_2
    //   168: aload_3
    //   169: invokevirtual setResourceDescriptor : (Ljava/lang/String;)V
    //   172: aload_2
    //   173: ldc_w 'Host'
    //   176: aload #4
    //   178: invokevirtual put : (Ljava/lang/String;Ljava/lang/String;)V
    //   181: aload_0
    //   182: getfield headers : Ljava/util/Map;
    //   185: astore_3
    //   186: aload_3
    //   187: ifnull -> 249
    //   190: aload_3
    //   191: invokeinterface entrySet : ()Ljava/util/Set;
    //   196: invokeinterface iterator : ()Ljava/util/Iterator;
    //   201: astore_3
    //   202: aload_3
    //   203: invokeinterface hasNext : ()Z
    //   208: ifeq -> 249
    //   211: aload_3
    //   212: invokeinterface next : ()Ljava/lang/Object;
    //   217: checkcast java/util/Map$Entry
    //   220: astore #4
    //   222: aload_2
    //   223: aload #4
    //   225: invokeinterface getKey : ()Ljava/lang/Object;
    //   230: checkcast java/lang/String
    //   233: aload #4
    //   235: invokeinterface getValue : ()Ljava/lang/Object;
    //   240: checkcast java/lang/String
    //   243: invokevirtual put : (Ljava/lang/String;Ljava/lang/String;)V
    //   246: goto -> 202
    //   249: aload_0
    //   250: getfield engine : Lorg/java_websocket/WebSocketImpl;
    //   253: aload_2
    //   254: invokevirtual startHandshake : (Lorg/java_websocket/handshake/ClientHandshakeBuilder;)V
    //   257: return
  }
  
  public void addHeader(String paramString1, String paramString2) {
    if (this.headers == null)
      this.headers = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER); 
    this.headers.put(paramString1, paramString2);
  }
  
  public void clearHeaders() {
    this.headers = null;
  }
  
  public void close() {
    if (this.writeThread != null)
      this.engine.close(1000); 
  }
  
  public void close(int paramInt) {
    this.engine.close(paramInt);
  }
  
  public void close(int paramInt, String paramString) {
    this.engine.close(paramInt, paramString);
  }
  
  public void closeBlocking() throws InterruptedException {
    close();
    this.closeLatch.await();
  }
  
  public void closeConnection(int paramInt, String paramString) {
    this.engine.closeConnection(paramInt, paramString);
  }
  
  public void connect() {
    if (this.connectReadThread == null) {
      Thread thread = new Thread(this);
      this.connectReadThread = thread;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("WebSocketConnectReadThread-");
      stringBuilder.append(this.connectReadThread.getId());
      thread.setName(stringBuilder.toString());
      this.connectReadThread.start();
      return;
    } 
    throw new IllegalStateException("WebSocketClient objects are not reuseable");
  }
  
  public boolean connectBlocking() throws InterruptedException {
    connect();
    this.connectLatch.await();
    return this.engine.isOpen();
  }
  
  public boolean connectBlocking(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
    connect();
    return (this.connectLatch.await(paramLong, paramTimeUnit) && this.engine.isOpen());
  }
  
  public <T> T getAttachment() {
    return (T)this.engine.getAttachment();
  }
  
  public WebSocket getConnection() {
    return (WebSocket)this.engine;
  }
  
  protected Collection<WebSocket> getConnections() {
    return (Collection)Collections.singletonList(this.engine);
  }
  
  public Draft getDraft() {
    return this.draft;
  }
  
  public InetSocketAddress getLocalSocketAddress() {
    return this.engine.getLocalSocketAddress();
  }
  
  public InetSocketAddress getLocalSocketAddress(WebSocket paramWebSocket) {
    Socket socket = this.socket;
    return (socket != null) ? (InetSocketAddress)socket.getLocalSocketAddress() : null;
  }
  
  public ReadyState getReadyState() {
    return this.engine.getReadyState();
  }
  
  public InetSocketAddress getRemoteSocketAddress() {
    return this.engine.getRemoteSocketAddress();
  }
  
  public InetSocketAddress getRemoteSocketAddress(WebSocket paramWebSocket) {
    Socket socket = this.socket;
    return (socket != null) ? (InetSocketAddress)socket.getRemoteSocketAddress() : null;
  }
  
  public String getResourceDescriptor() {
    return this.uri.getPath();
  }
  
  public Socket getSocket() {
    return this.socket;
  }
  
  public URI getURI() {
    return this.uri;
  }
  
  public boolean hasBufferedData() {
    return this.engine.hasBufferedData();
  }
  
  public boolean isClosed() {
    return this.engine.isClosed();
  }
  
  public boolean isClosing() {
    return this.engine.isClosing();
  }
  
  public boolean isFlushAndClose() {
    return this.engine.isFlushAndClose();
  }
  
  public boolean isOpen() {
    return this.engine.isOpen();
  }
  
  public abstract void onClose(int paramInt, String paramString, boolean paramBoolean);
  
  public void onCloseInitiated(int paramInt, String paramString) {}
  
  public void onClosing(int paramInt, String paramString, boolean paramBoolean) {}
  
  public abstract void onError(Exception paramException);
  
  public abstract void onMessage(String paramString);
  
  public void onMessage(ByteBuffer paramByteBuffer) {}
  
  public abstract void onOpen(ServerHandshake paramServerHandshake);
  
  public final void onWebsocketClose(WebSocket paramWebSocket, int paramInt, String paramString, boolean paramBoolean) {
    stopConnectionLostTimer();
    Thread thread = this.writeThread;
    if (thread != null)
      thread.interrupt(); 
    onClose(paramInt, paramString, paramBoolean);
    this.connectLatch.countDown();
    this.closeLatch.countDown();
  }
  
  public void onWebsocketCloseInitiated(WebSocket paramWebSocket, int paramInt, String paramString) {
    onCloseInitiated(paramInt, paramString);
  }
  
  public void onWebsocketClosing(WebSocket paramWebSocket, int paramInt, String paramString, boolean paramBoolean) {
    onClosing(paramInt, paramString, paramBoolean);
  }
  
  public final void onWebsocketError(WebSocket paramWebSocket, Exception paramException) {
    onError(paramException);
  }
  
  public final void onWebsocketMessage(WebSocket paramWebSocket, String paramString) {
    onMessage(paramString);
  }
  
  public final void onWebsocketMessage(WebSocket paramWebSocket, ByteBuffer paramByteBuffer) {
    onMessage(paramByteBuffer);
  }
  
  public final void onWebsocketOpen(WebSocket paramWebSocket, Handshakedata paramHandshakedata) {
    startConnectionLostTimer();
    onOpen((ServerHandshake)paramHandshakedata);
    this.connectLatch.countDown();
  }
  
  public final void onWriteDemand(WebSocket paramWebSocket) {}
  
  public void reconnect() {
    reset();
    connect();
  }
  
  public boolean reconnectBlocking() throws InterruptedException {
    reset();
    return connectBlocking();
  }
  
  public String removeHeader(String paramString) {
    Map<String, String> map = this.headers;
    return (map == null) ? null : map.remove(paramString);
  }
  
  public void run() {
    try {
      if (this.socketFactory != null) {
        this.socket = this.socketFactory.createSocket();
      } else {
        int i;
        if (this.socket == null) {
          this.socket = new Socket(this.proxy);
          i = 1;
        } else {
          if (this.socket.isClosed())
            throw new IOException(); 
          i = 0;
        } 
        this.socket.setTcpNoDelay(isTcpNoDelay());
        this.socket.setReuseAddress(isReuseAddr());
        if (!this.socket.isBound())
          this.socket.connect(new InetSocketAddress(this.uri.getHost(), getPort()), this.connectTimeout); 
        if (i && "wss".equals(this.uri.getScheme())) {
          SSLContext sSLContext = SSLContext.getInstance("TLSv1.2");
          sSLContext.init(null, null, null);
          this.socket = sSLContext.getSocketFactory().createSocket(this.socket, this.uri.getHost(), getPort(), true);
        } 
        InputStream inputStream = this.socket.getInputStream();
        this.ostream = this.socket.getOutputStream();
        sendHandshake();
        Thread thread = new Thread(new WebsocketWriteThread(this));
        this.writeThread = thread;
        thread.start();
        byte[] arrayOfByte = new byte[16384];
        try {
          while (!isClosing() && !isClosed()) {
            i = inputStream.read(arrayOfByte);
            if (i != -1)
              this.engine.decode(ByteBuffer.wrap(arrayOfByte, 0, i)); 
          } 
          this.engine.eot();
        } catch (IOException iOException) {
          handleIOException(iOException);
        } catch (RuntimeException runtimeException) {
          onError(runtimeException);
          this.engine.closeConnection(1006, runtimeException.getMessage());
        } 
        this.connectReadThread = null;
        return;
      } 
    } catch (Exception exception) {
      onWebsocketError((WebSocket)this.engine, exception);
      this.engine.closeConnection(-1, exception.getMessage());
      return;
    } 
    boolean bool = false;
  }
  
  public void send(String paramString) {
    this.engine.send(paramString);
  }
  
  public void send(ByteBuffer paramByteBuffer) {
    this.engine.send(paramByteBuffer);
  }
  
  public void send(byte[] paramArrayOfbyte) {
    this.engine.send(paramArrayOfbyte);
  }
  
  public void sendFragmentedFrame(Opcode paramOpcode, ByteBuffer paramByteBuffer, boolean paramBoolean) {
    this.engine.sendFragmentedFrame(paramOpcode, paramByteBuffer, paramBoolean);
  }
  
  public void sendFrame(Collection<Framedata> paramCollection) {
    this.engine.sendFrame(paramCollection);
  }
  
  public void sendFrame(Framedata paramFramedata) {
    this.engine.sendFrame(paramFramedata);
  }
  
  public void sendPing() {
    this.engine.sendPing();
  }
  
  public <T> void setAttachment(T paramT) {
    this.engine.setAttachment(paramT);
  }
  
  public void setProxy(Proxy paramProxy) {
    if (paramProxy != null) {
      this.proxy = paramProxy;
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  @Deprecated
  public void setSocket(Socket paramSocket) {
    if (this.socket == null) {
      this.socket = paramSocket;
      return;
    } 
    throw new IllegalStateException("socket has already been set");
  }
  
  public void setSocketFactory(SocketFactory paramSocketFactory) {
    this.socketFactory = paramSocketFactory;
  }
  
  private class WebsocketWriteThread implements Runnable {
    private final WebSocketClient webSocketClient;
    
    WebsocketWriteThread(WebSocketClient param1WebSocketClient1) {
      this.webSocketClient = param1WebSocketClient1;
    }
    
    private void closeSocket() {
      try {
        if (WebSocketClient.this.socket != null) {
          WebSocketClient.this.socket.close();
          return;
        } 
      } catch (IOException iOException) {
        WebSocketClient.this.onWebsocketError(this.webSocketClient, iOException);
      } 
    }
    
    private void runWriteData() throws IOException {
      try {
        while (!Thread.interrupted()) {
          ByteBuffer byteBuffer = WebSocketClient.this.engine.outQueue.take();
          WebSocketClient.this.ostream.write(byteBuffer.array(), 0, byteBuffer.limit());
          WebSocketClient.this.ostream.flush();
        } 
      } catch (InterruptedException interruptedException) {
        for (ByteBuffer byteBuffer : WebSocketClient.this.engine.outQueue) {
          WebSocketClient.this.ostream.write(byteBuffer.array(), 0, byteBuffer.limit());
          WebSocketClient.this.ostream.flush();
        } 
        Thread.currentThread().interrupt();
      } 
    }
    
    public void run() {
      Thread thread = Thread.currentThread();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("WebSocketWriteThread-");
      stringBuilder.append(Thread.currentThread().getId());
      thread.setName(stringBuilder.toString());
      try {
        runWriteData();
      } catch (IOException iOException) {
      
      } finally {}
      closeSocket();
      WebSocketClient.access$102(WebSocketClient.this, (Thread)null);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\client\WebSocketClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */