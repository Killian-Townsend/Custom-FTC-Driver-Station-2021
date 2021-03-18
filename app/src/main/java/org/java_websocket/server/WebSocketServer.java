package org.java_websocket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.java_websocket.AbstractWebSocket;
import org.java_websocket.SocketChannelIOHelper;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketFactory;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.WebSocketServerFactory;
import org.java_websocket.WrappedByteChannel;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.Handshakedata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WebSocketServer extends AbstractWebSocket implements Runnable {
  private static final int AVAILABLE_PROCESSORS;
  
  private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
  
  private final InetSocketAddress address;
  
  private BlockingQueue<ByteBuffer> buffers;
  
  private final Collection<WebSocket> connections;
  
  protected List<WebSocketWorker> decoders;
  
  private List<Draft> drafts;
  
  private List<WebSocketImpl> iqueue;
  
  private final AtomicBoolean isclosed;
  
  private int queueinvokes;
  
  private final AtomicInteger queuesize;
  
  private Selector selector;
  
  private Thread selectorthread;
  
  private ServerSocketChannel server;
  
  private WebSocketServerFactory wsf;
  
  static {
    AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
  }
  
  public WebSocketServer() {
    this(new InetSocketAddress(80), AVAILABLE_PROCESSORS, (List<Draft>)null);
  }
  
  public WebSocketServer(InetSocketAddress paramInetSocketAddress) {
    this(paramInetSocketAddress, AVAILABLE_PROCESSORS, (List<Draft>)null);
  }
  
  public WebSocketServer(InetSocketAddress paramInetSocketAddress, int paramInt) {
    this(paramInetSocketAddress, paramInt, (List<Draft>)null);
  }
  
  public WebSocketServer(InetSocketAddress paramInetSocketAddress, int paramInt, List<Draft> paramList) {
    this(paramInetSocketAddress, paramInt, paramList, new HashSet<WebSocket>());
  }
  
  public WebSocketServer(InetSocketAddress paramInetSocketAddress, int paramInt, List<Draft> paramList, Collection<WebSocket> paramCollection) {
    int i = 0;
    this.isclosed = new AtomicBoolean(false);
    this.queueinvokes = 0;
    this.queuesize = new AtomicInteger(0);
    this.wsf = new DefaultWebSocketServerFactory();
    if (paramInetSocketAddress != null && paramInt >= 1 && paramCollection != null) {
      if (paramList == null) {
        this.drafts = Collections.emptyList();
      } else {
        this.drafts = paramList;
      } 
      this.address = paramInetSocketAddress;
      this.connections = paramCollection;
      setTcpNoDelay(false);
      setReuseAddr(false);
      this.iqueue = new LinkedList<WebSocketImpl>();
      this.decoders = new ArrayList<WebSocketWorker>(paramInt);
      this.buffers = new LinkedBlockingQueue<ByteBuffer>();
      while (i < paramInt) {
        WebSocketWorker webSocketWorker = new WebSocketWorker();
        this.decoders.add(webSocketWorker);
        i++;
      } 
      return;
    } 
    throw new IllegalArgumentException("address and connectionscontainer must not be null and you need at least 1 decoder");
  }
  
  public WebSocketServer(InetSocketAddress paramInetSocketAddress, List<Draft> paramList) {
    this(paramInetSocketAddress, AVAILABLE_PROCESSORS, paramList);
  }
  
  private void doAccept(SelectionKey paramSelectionKey, Iterator<SelectionKey> paramIterator) throws IOException, InterruptedException {
    if (!onConnect(paramSelectionKey)) {
      paramSelectionKey.cancel();
      return;
    } 
    SocketChannel socketChannel = this.server.accept();
    if (socketChannel == null)
      return; 
    socketChannel.configureBlocking(false);
    Socket socket = socketChannel.socket();
    socket.setTcpNoDelay(isTcpNoDelay());
    socket.setKeepAlive(true);
    WebSocketImpl webSocketImpl = this.wsf.createWebSocket((WebSocketAdapter)this, this.drafts);
    webSocketImpl.setSelectionKey(socketChannel.register(this.selector, 1, webSocketImpl));
    try {
      webSocketImpl.setChannel(this.wsf.wrapChannel(socketChannel, webSocketImpl.getSelectionKey()));
      paramIterator.remove();
      allocateBuffers((WebSocket)webSocketImpl);
      return;
    } catch (IOException iOException) {
      if (webSocketImpl.getSelectionKey() != null)
        webSocketImpl.getSelectionKey().cancel(); 
      handleIOException(webSocketImpl.getSelectionKey(), (WebSocket)null, iOException);
      return;
    } 
  }
  
  private void doAdditionalRead() throws InterruptedException, IOException {
    while (!this.iqueue.isEmpty()) {
      WebSocketImpl webSocketImpl = this.iqueue.remove(0);
      WrappedByteChannel wrappedByteChannel = (WrappedByteChannel)webSocketImpl.getChannel();
      ByteBuffer byteBuffer = takeBuffer();
      try {
        if (SocketChannelIOHelper.readMore(byteBuffer, webSocketImpl, wrappedByteChannel))
          this.iqueue.add(webSocketImpl); 
        if (byteBuffer.hasRemaining()) {
          webSocketImpl.inQueue.put(byteBuffer);
          queue(webSocketImpl);
          continue;
        } 
        pushBuffer(byteBuffer);
      } catch (IOException iOException) {
        pushBuffer(byteBuffer);
        throw iOException;
      } 
    } 
  }
  
  private void doBroadcast(Object<Object, Object> paramObject, Collection<WebSocket> paramCollection) {
    String str;
    boolean bool = paramObject instanceof String;
    ByteBuffer byteBuffer = null;
    if (bool) {
      str = (String)paramObject;
    } else {
      str = null;
    } 
    if (paramObject instanceof ByteBuffer)
      byteBuffer = (ByteBuffer)paramObject; 
    if (str == null && byteBuffer == null)
      return; 
    paramObject = (Object<Object, Object>)new HashMap<Object, Object>();
    Iterator<WebSocket> iterator = paramCollection.iterator();
    while (true) {
      if (iterator.hasNext()) {
        WebSocket webSocket = iterator.next();
        if (webSocket != null) {
          Draft draft = webSocket.getDraft();
          fillFrames(draft, (Map)paramObject, str, byteBuffer);
          try {
            webSocket.sendFrame((Collection)paramObject.get(draft));
          } catch (WebsocketNotConnectedException websocketNotConnectedException) {}
        } 
        continue;
      } 
      return;
    } 
  }
  
  private boolean doEnsureSingleThread() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield selectorthread : Ljava/lang/Thread;
    //   6: ifnonnull -> 34
    //   9: aload_0
    //   10: invokestatic currentThread : ()Ljava/lang/Thread;
    //   13: putfield selectorthread : Ljava/lang/Thread;
    //   16: aload_0
    //   17: getfield isclosed : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   20: invokevirtual get : ()Z
    //   23: ifeq -> 30
    //   26: aload_0
    //   27: monitorexit
    //   28: iconst_0
    //   29: ireturn
    //   30: aload_0
    //   31: monitorexit
    //   32: iconst_1
    //   33: ireturn
    //   34: new java/lang/StringBuilder
    //   37: dup
    //   38: invokespecial <init> : ()V
    //   41: astore_1
    //   42: aload_1
    //   43: aload_0
    //   44: invokevirtual getClass : ()Ljava/lang/Class;
    //   47: invokevirtual getName : ()Ljava/lang/String;
    //   50: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: pop
    //   54: aload_1
    //   55: ldc_w ' can only be started once.'
    //   58: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: pop
    //   62: new java/lang/IllegalStateException
    //   65: dup
    //   66: aload_1
    //   67: invokevirtual toString : ()Ljava/lang/String;
    //   70: invokespecial <init> : (Ljava/lang/String;)V
    //   73: athrow
    //   74: astore_1
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_1
    //   78: athrow
    // Exception table:
    //   from	to	target	type
    //   2	28	74	finally
    //   30	32	74	finally
    //   34	74	74	finally
    //   75	77	74	finally
  }
  
  private boolean doRead(SelectionKey paramSelectionKey, Iterator<SelectionKey> paramIterator) throws InterruptedException, IOException {
    WebSocketImpl webSocketImpl = (WebSocketImpl)paramSelectionKey.attachment();
    ByteBuffer byteBuffer = takeBuffer();
    if (webSocketImpl.getChannel() == null) {
      paramSelectionKey.cancel();
      handleIOException(paramSelectionKey, (WebSocket)webSocketImpl, new IOException());
      return false;
    } 
    try {
      if (SocketChannelIOHelper.read(byteBuffer, webSocketImpl, webSocketImpl.getChannel())) {
        if (byteBuffer.hasRemaining()) {
          webSocketImpl.inQueue.put(byteBuffer);
          queue(webSocketImpl);
          paramIterator.remove();
          if (webSocketImpl.getChannel() instanceof WrappedByteChannel && ((WrappedByteChannel)webSocketImpl.getChannel()).isNeedRead())
            this.iqueue.add(webSocketImpl); 
        } else {
          pushBuffer(byteBuffer);
        } 
      } else {
        pushBuffer(byteBuffer);
      } 
      return true;
    } catch (IOException iOException) {
      pushBuffer(byteBuffer);
      throw iOException;
    } 
  }
  
  private void doServerShutdown() {
    stopConnectionLostTimer();
    List<WebSocketWorker> list = this.decoders;
    if (list != null) {
      Iterator<WebSocketWorker> iterator = list.iterator();
      while (iterator.hasNext())
        ((WebSocketWorker)iterator.next()).interrupt(); 
    } 
    Selector selector = this.selector;
    if (selector != null)
      try {
        selector.close();
      } catch (IOException iOException) {
        log.error("IOException during selector.close", iOException);
        onError((WebSocket)null, iOException);
      }  
    ServerSocketChannel serverSocketChannel = this.server;
    if (serverSocketChannel != null)
      try {
        serverSocketChannel.close();
        return;
      } catch (IOException iOException) {
        log.error("IOException during server.close", iOException);
        onError((WebSocket)null, iOException);
      }  
  }
  
  private boolean doSetupSelectorAndServerThread() {
    Thread thread = this.selectorthread;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("WebSocketSelector-");
    stringBuilder.append(this.selectorthread.getId());
    thread.setName(stringBuilder.toString());
    try {
      ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
      this.server = serverSocketChannel;
      serverSocketChannel.configureBlocking(false);
      ServerSocket serverSocket = this.server.socket();
      serverSocket.setReceiveBufferSize(16384);
      serverSocket.setReuseAddress(isReuseAddr());
      serverSocket.bind(this.address);
      Selector selector = Selector.open();
      this.selector = selector;
      this.server.register(selector, this.server.validOps());
      startConnectionLostTimer();
      Iterator<WebSocketWorker> iterator = this.decoders.iterator();
      while (iterator.hasNext())
        ((WebSocketWorker)iterator.next()).start(); 
      onStart();
      return true;
    } catch (IOException iOException) {
      handleFatal((WebSocket)null, iOException);
      return false;
    } 
  }
  
  private void doWrite(SelectionKey paramSelectionKey) throws IOException {
    WebSocketImpl webSocketImpl = (WebSocketImpl)paramSelectionKey.attachment();
    if (SocketChannelIOHelper.batch(webSocketImpl, webSocketImpl.getChannel()) && paramSelectionKey.isValid())
      paramSelectionKey.interestOps(1); 
  }
  
  private void fillFrames(Draft paramDraft, Map<Draft, List<Framedata>> paramMap, String paramString, ByteBuffer paramByteBuffer) {
    if (!paramMap.containsKey(paramDraft)) {
      List<Framedata> list = null;
      if (paramString != null)
        list = paramDraft.createFrames(paramString, false); 
      if (paramByteBuffer != null)
        list = paramDraft.createFrames(paramByteBuffer, false); 
      if (list != null)
        paramMap.put(paramDraft, list); 
    } 
  }
  
  private Socket getSocket(WebSocket paramWebSocket) {
    return ((SocketChannel)((WebSocketImpl)paramWebSocket).getSelectionKey().channel()).socket();
  }
  
  private void handleFatal(WebSocket paramWebSocket, Exception paramException) {
    log.error("Shutdown due to fatal error", paramException);
    onError(paramWebSocket, paramException);
    List<WebSocketWorker> list = this.decoders;
    if (list != null) {
      Iterator<WebSocketWorker> iterator = list.iterator();
      while (iterator.hasNext())
        ((WebSocketWorker)iterator.next()).interrupt(); 
    } 
    Thread thread = this.selectorthread;
    if (thread != null)
      thread.interrupt(); 
    try {
      stop();
      return;
    } catch (IOException iOException) {
      log.error("Error during shutdown", iOException);
      onError((WebSocket)null, iOException);
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      log.error("Interrupt during stop", paramException);
      onError((WebSocket)null, interruptedException);
      return;
    } 
  }
  
  private void handleIOException(SelectionKey paramSelectionKey, WebSocket paramWebSocket, IOException paramIOException) {
    if (paramWebSocket != null) {
      paramWebSocket.closeConnection(1006, paramIOException.getMessage());
      return;
    } 
    if (paramSelectionKey != null) {
      SelectableChannel selectableChannel = paramSelectionKey.channel();
      if (selectableChannel != null && selectableChannel.isOpen()) {
        try {
          selectableChannel.close();
        } catch (IOException iOException) {}
        log.trace("Connection closed because of exception", paramIOException);
      } 
    } 
  }
  
  private void pushBuffer(ByteBuffer paramByteBuffer) throws InterruptedException {
    if (this.buffers.size() > this.queuesize.intValue())
      return; 
    this.buffers.put(paramByteBuffer);
  }
  
  private ByteBuffer takeBuffer() throws InterruptedException {
    return this.buffers.take();
  }
  
  protected boolean addConnection(WebSocket paramWebSocket) {
    if (!this.isclosed.get())
      synchronized (this.connections) {
        return this.connections.add(paramWebSocket);
      }  
    paramWebSocket.close(1001);
    return true;
  }
  
  protected void allocateBuffers(WebSocket paramWebSocket) throws InterruptedException {
    if (this.queuesize.get() >= this.decoders.size() * 2 + 1)
      return; 
    this.queuesize.incrementAndGet();
    this.buffers.put(createBuffer());
  }
  
  public void broadcast(String paramString) {
    broadcast(paramString, this.connections);
  }
  
  public void broadcast(String paramString, Collection<WebSocket> paramCollection) {
    if (paramString != null && paramCollection != null) {
      doBroadcast(paramString, paramCollection);
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public void broadcast(ByteBuffer paramByteBuffer) {
    broadcast(paramByteBuffer, this.connections);
  }
  
  public void broadcast(ByteBuffer paramByteBuffer, Collection<WebSocket> paramCollection) {
    if (paramByteBuffer != null && paramCollection != null) {
      doBroadcast(paramByteBuffer, paramCollection);
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public void broadcast(byte[] paramArrayOfbyte) {
    broadcast(paramArrayOfbyte, this.connections);
  }
  
  public void broadcast(byte[] paramArrayOfbyte, Collection<WebSocket> paramCollection) {
    if (paramArrayOfbyte != null && paramCollection != null) {
      broadcast(ByteBuffer.wrap(paramArrayOfbyte), paramCollection);
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public ByteBuffer createBuffer() {
    return ByteBuffer.allocate(16384);
  }
  
  public InetSocketAddress getAddress() {
    return this.address;
  }
  
  public Collection<WebSocket> getConnections() {
    return Collections.unmodifiableCollection(new ArrayList<WebSocket>(this.connections));
  }
  
  public List<Draft> getDraft() {
    return Collections.unmodifiableList(this.drafts);
  }
  
  public InetSocketAddress getLocalSocketAddress(WebSocket paramWebSocket) {
    return (InetSocketAddress)getSocket(paramWebSocket).getLocalSocketAddress();
  }
  
  public int getPort() {
    int j = getAddress().getPort();
    int i = j;
    if (j == 0) {
      ServerSocketChannel serverSocketChannel = this.server;
      i = j;
      if (serverSocketChannel != null)
        i = serverSocketChannel.socket().getLocalPort(); 
    } 
    return i;
  }
  
  public InetSocketAddress getRemoteSocketAddress(WebSocket paramWebSocket) {
    return (InetSocketAddress)getSocket(paramWebSocket).getRemoteSocketAddress();
  }
  
  public final WebSocketFactory getWebSocketFactory() {
    return (WebSocketFactory)this.wsf;
  }
  
  public abstract void onClose(WebSocket paramWebSocket, int paramInt, String paramString, boolean paramBoolean);
  
  public void onCloseInitiated(WebSocket paramWebSocket, int paramInt, String paramString) {}
  
  public void onClosing(WebSocket paramWebSocket, int paramInt, String paramString, boolean paramBoolean) {}
  
  protected boolean onConnect(SelectionKey paramSelectionKey) {
    return true;
  }
  
  public abstract void onError(WebSocket paramWebSocket, Exception paramException);
  
  public abstract void onMessage(WebSocket paramWebSocket, String paramString);
  
  public void onMessage(WebSocket paramWebSocket, ByteBuffer paramByteBuffer) {}
  
  public abstract void onOpen(WebSocket paramWebSocket, ClientHandshake paramClientHandshake);
  
  public abstract void onStart();
  
  public final void onWebsocketClose(WebSocket paramWebSocket, int paramInt, String paramString, boolean paramBoolean) {
    this.selector.wakeup();
    try {
      if (removeConnection(paramWebSocket))
        onClose(paramWebSocket, paramInt, paramString, paramBoolean); 
      return;
    } finally {
      try {
        releaseBuffers((WebSocket)interruptedException);
      } catch (InterruptedException interruptedException1) {
        Thread.currentThread().interrupt();
      } 
    } 
  }
  
  public void onWebsocketCloseInitiated(WebSocket paramWebSocket, int paramInt, String paramString) {
    onCloseInitiated(paramWebSocket, paramInt, paramString);
  }
  
  public void onWebsocketClosing(WebSocket paramWebSocket, int paramInt, String paramString, boolean paramBoolean) {
    onClosing(paramWebSocket, paramInt, paramString, paramBoolean);
  }
  
  public final void onWebsocketError(WebSocket paramWebSocket, Exception paramException) {
    onError(paramWebSocket, paramException);
  }
  
  public final void onWebsocketMessage(WebSocket paramWebSocket, String paramString) {
    onMessage(paramWebSocket, paramString);
  }
  
  public final void onWebsocketMessage(WebSocket paramWebSocket, ByteBuffer paramByteBuffer) {
    onMessage(paramWebSocket, paramByteBuffer);
  }
  
  public final void onWebsocketOpen(WebSocket paramWebSocket, Handshakedata paramHandshakedata) {
    if (addConnection(paramWebSocket))
      onOpen(paramWebSocket, (ClientHandshake)paramHandshakedata); 
  }
  
  public final void onWriteDemand(WebSocket paramWebSocket) {
    WebSocketImpl webSocketImpl = (WebSocketImpl)paramWebSocket;
    try {
      webSocketImpl.getSelectionKey().interestOps(5);
    } catch (CancelledKeyException cancelledKeyException) {
      webSocketImpl.outQueue.clear();
    } 
    this.selector.wakeup();
  }
  
  protected void queue(WebSocketImpl paramWebSocketImpl) throws InterruptedException {
    if (paramWebSocketImpl.getWorkerThread() == null) {
      List<WebSocketWorker> list = this.decoders;
      paramWebSocketImpl.setWorkerThread(list.get(this.queueinvokes % list.size()));
      this.queueinvokes++;
    } 
    paramWebSocketImpl.getWorkerThread().put(paramWebSocketImpl);
  }
  
  protected void releaseBuffers(WebSocket paramWebSocket) throws InterruptedException {}
  
  protected boolean removeConnection(WebSocket paramWebSocket) {
    synchronized (this.connections) {
      boolean bool;
      if (this.connections.contains(paramWebSocket)) {
        bool = this.connections.remove(paramWebSocket);
      } else {
        log.trace("Removing connection which is not in the connections collection! Possible no handshake recieved! {}", paramWebSocket);
        bool = false;
      } 
      if (this.isclosed.get() && this.connections.isEmpty())
        this.selectorthread.interrupt(); 
      return bool;
    } 
  }
  
  public void run() {
    if (!doEnsureSingleThread())
      return; 
    if (!doSetupSelectorAndServerThread())
      return; 
    int j = 0;
    int i = 5;
    label77: while (true) {
      try {
        boolean bool = this.selectorthread.isInterrupted();
        if (!bool && i != 0) {
          int k = j;
          int n = j;
          int i1 = i;
          int i2 = j;
          int i3 = i;
          int m = j;
          j = i;
          try {
            if (this.isclosed.get())
              k = 5; 
            int i4 = i;
            n = k;
            i1 = i;
            i2 = k;
            i3 = i;
            m = k;
            j = i;
            if (this.selector.select(k) == 0) {
              i4 = i;
              n = k;
              i1 = i;
              i2 = k;
              i3 = i;
              m = k;
              j = i;
              if (this.isclosed.get())
                i4 = i - 1; 
            } 
            n = k;
            i1 = i4;
            i2 = k;
            i3 = i4;
            m = k;
            j = i4;
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            for (object = null;; object = SYNTHETIC_LOCAL_VARIABLE_12) {
              n = k;
              i1 = i4;
              i2 = k;
              i3 = i4;
              try {
                if (iterator.hasNext()) {
                  n = k;
                  i1 = i4;
                  i2 = k;
                  i3 = i4;
                  SelectionKey selectionKey = iterator.next();
                  n = k;
                  i1 = i4;
                  i2 = k;
                  i3 = i4;
                  try {
                    if (!selectionKey.isValid())
                      continue; 
                    n = k;
                    i1 = i4;
                    i2 = k;
                    i3 = i4;
                    if (selectionKey.isAcceptable()) {
                      n = k;
                      i1 = i4;
                      i2 = k;
                      i3 = i4;
                      doAccept(selectionKey, iterator);
                      continue;
                    } 
                    n = k;
                    i1 = i4;
                    i2 = k;
                    i3 = i4;
                    if (selectionKey.isReadable()) {
                      n = k;
                      i1 = i4;
                      i2 = k;
                      i3 = i4;
                      if (!doRead(selectionKey, iterator))
                        continue; 
                    } 
                    n = k;
                    i1 = i4;
                    i2 = k;
                    i3 = i4;
                    if (selectionKey.isWritable()) {
                      n = k;
                      i1 = i4;
                      i2 = k;
                      i3 = i4;
                      doWrite(selectionKey);
                    } 
                    continue;
                  } catch (IOException iOException1) {
                    object = selectionKey;
                    i = i4;
                    iOException = iOException1;
                    break;
                  } 
                } 
                n = k;
                i1 = i4;
                i2 = k;
                i3 = i4;
                doAdditionalRead();
                j = k;
                i = i4;
              } catch (IOException null) {
                i = i4;
                break;
              } 
              continue label77;
            } 
          } catch (CancelledKeyException null) {
            j = n;
            i = i1;
            continue;
          } catch (ClosedByInterruptException null) {
            doServerShutdown();
            return;
          } catch (IOException iOException) {
            object = null;
            k = m;
            i = j;
          } catch (InterruptedException object) {
            Thread.currentThread().interrupt();
            j = i2;
            i = i3;
            continue;
          } 
          if (object != null)
            object.cancel(); 
          handleIOException((SelectionKey)object, (WebSocket)null, iOException);
          j = k;
          continue;
        } 
      } catch (RuntimeException runtimeException) {
        handleFatal((WebSocket)null, runtimeException);
      } finally {
        Exception exception;
      } 
      doServerShutdown();
      return;
    } 
  }
  
  public final void setWebSocketFactory(WebSocketServerFactory paramWebSocketServerFactory) {
    WebSocketServerFactory webSocketServerFactory = this.wsf;
    if (webSocketServerFactory != null)
      webSocketServerFactory.close(); 
    this.wsf = paramWebSocketServerFactory;
  }
  
  public void start() {
    if (this.selectorthread == null) {
      (new Thread(this)).start();
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getClass().getName());
    stringBuilder.append(" can only be started once.");
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public void stop() throws IOException, InterruptedException {
    stop(0);
  }
  
  public void stop(int paramInt) throws InterruptedException {
    // Byte code:
    //   0: aload_0
    //   1: getfield isclosed : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   4: iconst_0
    //   5: iconst_1
    //   6: invokevirtual compareAndSet : (ZZ)Z
    //   9: ifne -> 13
    //   12: return
    //   13: aload_0
    //   14: getfield connections : Ljava/util/Collection;
    //   17: astore_2
    //   18: aload_2
    //   19: monitorenter
    //   20: new java/util/ArrayList
    //   23: dup
    //   24: aload_0
    //   25: getfield connections : Ljava/util/Collection;
    //   28: invokespecial <init> : (Ljava/util/Collection;)V
    //   31: astore_3
    //   32: aload_2
    //   33: monitorexit
    //   34: aload_3
    //   35: invokeinterface iterator : ()Ljava/util/Iterator;
    //   40: astore_2
    //   41: aload_2
    //   42: invokeinterface hasNext : ()Z
    //   47: ifeq -> 70
    //   50: aload_2
    //   51: invokeinterface next : ()Ljava/lang/Object;
    //   56: checkcast org/java_websocket/WebSocket
    //   59: sipush #1001
    //   62: invokeinterface close : (I)V
    //   67: goto -> 41
    //   70: aload_0
    //   71: getfield wsf : Lorg/java_websocket/WebSocketServerFactory;
    //   74: invokeinterface close : ()V
    //   79: aload_0
    //   80: monitorenter
    //   81: aload_0
    //   82: getfield selectorthread : Ljava/lang/Thread;
    //   85: ifnull -> 112
    //   88: aload_0
    //   89: getfield selector : Ljava/nio/channels/Selector;
    //   92: ifnull -> 112
    //   95: aload_0
    //   96: getfield selector : Ljava/nio/channels/Selector;
    //   99: invokevirtual wakeup : ()Ljava/nio/channels/Selector;
    //   102: pop
    //   103: aload_0
    //   104: getfield selectorthread : Ljava/lang/Thread;
    //   107: iload_1
    //   108: i2l
    //   109: invokevirtual join : (J)V
    //   112: aload_0
    //   113: monitorexit
    //   114: return
    //   115: astore_2
    //   116: aload_0
    //   117: monitorexit
    //   118: aload_2
    //   119: athrow
    //   120: astore_3
    //   121: aload_2
    //   122: monitorexit
    //   123: aload_3
    //   124: athrow
    // Exception table:
    //   from	to	target	type
    //   20	34	120	finally
    //   81	112	115	finally
    //   112	114	115	finally
    //   116	118	115	finally
    //   121	123	120	finally
  }
  
  public class WebSocketWorker extends Thread {
    private BlockingQueue<WebSocketImpl> iqueue = new LinkedBlockingQueue<WebSocketImpl>();
    
    public WebSocketWorker() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("WebSocketWorker-");
      stringBuilder.append(getId());
      setName(stringBuilder.toString());
      setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread param2Thread, Throwable param2Throwable) {
              WebSocketServer.log.error("Uncaught exception in thread {}: {}", param2Thread.getName(), param2Throwable);
            }
          });
    }
    
    private void doDecode(WebSocketImpl param1WebSocketImpl, ByteBuffer param1ByteBuffer) throws InterruptedException {
      try {
        param1WebSocketImpl.decode(param1ByteBuffer);
      } catch (Exception exception) {
      
      } finally {}
      WebSocketServer.this.pushBuffer(param1ByteBuffer);
    }
    
    public void put(WebSocketImpl param1WebSocketImpl) throws InterruptedException {
      this.iqueue.put(param1WebSocketImpl);
    }
    
    public void run() {
      try {
        WebSocketImpl webSocketImpl;
        while (true) {
          webSocketImpl = this.iqueue.take();
          try {
            doDecode(webSocketImpl, webSocketImpl.inQueue.poll());
          } catch (RuntimeException runtimeException) {
            break;
          } 
        } 
        WebSocketServer.this.handleFatal((WebSocket)webSocketImpl, runtimeException);
        return;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        return;
      } catch (RuntimeException runtimeException) {
        WebSocket webSocket = null;
        WebSocketServer.this.handleFatal(webSocket, runtimeException);
        return;
      } 
    }
  }
  
  class null implements Thread.UncaughtExceptionHandler {
    public void uncaughtException(Thread param1Thread, Throwable param1Throwable) {
      WebSocketServer.log.error("Uncaught exception in thread {}: {}", param1Thread.getName(), param1Throwable);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\server\WebSocketServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */