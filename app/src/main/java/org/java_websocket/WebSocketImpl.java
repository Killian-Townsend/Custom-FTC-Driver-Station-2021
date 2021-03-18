package org.java_websocket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.enums.CloseHandshakeType;
import org.java_websocket.enums.HandshakeState;
import org.java_websocket.enums.Opcode;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.enums.Role;
import org.java_websocket.exceptions.IncompleteHandshakeException;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.exceptions.LimitExceededException;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.PingFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ClientHandshakeBuilder;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.util.Charsetfunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketImpl implements WebSocket {
  public static final int DEFAULT_PORT = 80;
  
  public static final int DEFAULT_WSS_PORT = 443;
  
  public static final int RCVBUF = 16384;
  
  private static final Logger log = LoggerFactory.getLogger(WebSocketImpl.class);
  
  private Object attachment;
  
  private ByteChannel channel;
  
  private Integer closecode = null;
  
  private Boolean closedremotely = null;
  
  private String closemessage = null;
  
  private Draft draft = null;
  
  private boolean flushandclosestate = false;
  
  private ClientHandshake handshakerequest = null;
  
  public final BlockingQueue<ByteBuffer> inQueue;
  
  private SelectionKey key;
  
  private List<Draft> knownDrafts;
  
  private long lastPong = System.nanoTime();
  
  public final BlockingQueue<ByteBuffer> outQueue;
  
  private PingFrame pingFrame;
  
  private volatile ReadyState readyState = ReadyState.NOT_YET_CONNECTED;
  
  private String resourceDescriptor = null;
  
  private Role role;
  
  private final Object synchronizeWriteObject = new Object();
  
  private ByteBuffer tmpHandshakeBytes = ByteBuffer.allocate(0);
  
  private WebSocketServer.WebSocketWorker workerThread;
  
  private final WebSocketListener wsl;
  
  public WebSocketImpl(WebSocketListener paramWebSocketListener, List<Draft> paramList) {
    this(paramWebSocketListener, (Draft)null);
    this.role = Role.SERVER;
    if (paramList == null || paramList.isEmpty()) {
      ArrayList<Draft> arrayList = new ArrayList();
      this.knownDrafts = arrayList;
      arrayList.add(new Draft_6455());
      return;
    } 
    this.knownDrafts = paramList;
  }
  
  public WebSocketImpl(WebSocketListener paramWebSocketListener, Draft paramDraft) {
    if (paramWebSocketListener != null && (paramDraft != null || this.role != Role.SERVER)) {
      this.outQueue = new LinkedBlockingQueue<ByteBuffer>();
      this.inQueue = new LinkedBlockingQueue<ByteBuffer>();
      this.wsl = paramWebSocketListener;
      this.role = Role.CLIENT;
      if (paramDraft != null)
        this.draft = paramDraft.copyInstance(); 
      return;
    } 
    throw new IllegalArgumentException("parameters must not be null");
  }
  
  private void closeConnectionDueToInternalServerError(RuntimeException paramRuntimeException) {
    write(generateHttpResponseDueToError(500));
    flushAndClose(-1, paramRuntimeException.getMessage(), false);
  }
  
  private void closeConnectionDueToWrongHandshake(InvalidDataException paramInvalidDataException) {
    write(generateHttpResponseDueToError(404));
    flushAndClose(paramInvalidDataException.getCloseCode(), paramInvalidDataException.getMessage(), false);
  }
  
  private void decodeFrames(ByteBuffer paramByteBuffer) {
    try {
      for (Framedata framedata : this.draft.translateFrame(paramByteBuffer)) {
        log.trace("matched frame: {}", framedata);
        this.draft.processFrame(this, framedata);
      } 
    } catch (LimitExceededException limitExceededException) {
      if (limitExceededException.getLimit() == Integer.MAX_VALUE) {
        log.error("Closing due to invalid size of frame", (Throwable)limitExceededException);
        this.wsl.onWebsocketError(this, (Exception)limitExceededException);
      } 
      close((InvalidDataException)limitExceededException);
    } catch (InvalidDataException invalidDataException) {
      log.error("Closing due to invalid data in frame", (Throwable)invalidDataException);
      this.wsl.onWebsocketError(this, (Exception)invalidDataException);
      close(invalidDataException);
      return;
    } 
  }
  
  private boolean decodeHandshake(ByteBuffer paramByteBuffer) {
    ByteBuffer byteBuffer;
    if (this.tmpHandshakeBytes.capacity() == 0) {
      byteBuffer = paramByteBuffer;
    } else {
      if (this.tmpHandshakeBytes.remaining() < paramByteBuffer.remaining()) {
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(this.tmpHandshakeBytes.capacity() + paramByteBuffer.remaining());
        this.tmpHandshakeBytes.flip();
        byteBuffer1.put(this.tmpHandshakeBytes);
        this.tmpHandshakeBytes = byteBuffer1;
      } 
      this.tmpHandshakeBytes.put(paramByteBuffer);
      this.tmpHandshakeBytes.flip();
      byteBuffer = this.tmpHandshakeBytes;
    } 
    byteBuffer.mark();
    try {
      Role role1 = this.role;
      Role role2 = Role.SERVER;
      if (role1 == role2) {
        if (this.draft == null) {
          Iterator<Draft> iterator = this.knownDrafts.iterator();
          while (true) {
            if (iterator.hasNext()) {
              Draft draft = ((Draft)iterator.next()).copyInstance();
              try {
                draft.setParseMode(this.role);
                byteBuffer.reset();
                Handshakedata handshakedata1 = draft.translateHandshake(byteBuffer);
                if (!(handshakedata1 instanceof ClientHandshake)) {
                  log.trace("Closing due to wrong handshake");
                  closeConnectionDueToWrongHandshake(new InvalidDataException(1002, "wrong http function"));
                  return false;
                } 
                ClientHandshake clientHandshake1 = (ClientHandshake)handshakedata1;
                if (draft.acceptHandshakeAsServer(clientHandshake1) == HandshakeState.MATCHED) {
                  this.resourceDescriptor = clientHandshake1.getResourceDescriptor();
                  try {
                    ServerHandshakeBuilder serverHandshakeBuilder = this.wsl.onWebsocketHandshakeReceivedAsServer(this, draft, clientHandshake1);
                    write(draft.createHandshake((Handshakedata)draft.postProcessHandshakeResponseAsServer(clientHandshake1, serverHandshakeBuilder)));
                    this.draft = draft;
                    open((Handshakedata)clientHandshake1);
                    return true;
                  } catch (InvalidDataException invalidDataException) {
                    log.trace("Closing due to wrong handshake. Possible handshake rejection", (Throwable)invalidDataException);
                    closeConnectionDueToWrongHandshake(invalidDataException);
                    return false;
                  } catch (RuntimeException runtimeException) {
                    log.error("Closing due to internal server error", runtimeException);
                    this.wsl.onWebsocketError(this, runtimeException);
                    closeConnectionDueToInternalServerError(runtimeException);
                    return false;
                  } 
                } 
              } catch (InvalidHandshakeException invalidHandshakeException) {}
              continue;
            } 
            if (this.draft == null) {
              log.trace("Closing due to protocol error: no draft matches");
              closeConnectionDueToWrongHandshake(new InvalidDataException(1002, "no draft matches"));
              return false;
            } 
            return false;
          } 
        } 
        Handshakedata handshakedata = this.draft.translateHandshake(byteBuffer);
        if (!(handshakedata instanceof ClientHandshake)) {
          log.trace("Closing due to protocol error: wrong http function");
          flushAndClose(1002, "wrong http function", false);
          return false;
        } 
        ClientHandshake clientHandshake = (ClientHandshake)handshakedata;
        if (this.draft.acceptHandshakeAsServer(clientHandshake) == HandshakeState.MATCHED) {
          open((Handshakedata)clientHandshake);
          return true;
        } 
        log.trace("Closing due to protocol error: the handshake did finally not match");
        close(1002, "the handshake did finally not match");
        return false;
      } 
      if (this.role == Role.CLIENT) {
        this.draft.setParseMode(this.role);
        Handshakedata handshakedata = this.draft.translateHandshake(byteBuffer);
        if (!(handshakedata instanceof ServerHandshake)) {
          log.trace("Closing due to protocol error: wrong http function");
          flushAndClose(1002, "wrong http function", false);
          return false;
        } 
        ServerHandshake serverHandshake = (ServerHandshake)handshakedata;
        HandshakeState handshakeState1 = this.draft.acceptHandshakeAsClient(this.handshakerequest, serverHandshake);
        HandshakeState handshakeState2 = HandshakeState.MATCHED;
        if (handshakeState1 == handshakeState2)
          try {
            this.wsl.onWebsocketHandshakeReceivedAsClient(this, this.handshakerequest, serverHandshake);
            open((Handshakedata)serverHandshake);
            return true;
          } catch (InvalidDataException invalidDataException) {
            log.trace("Closing due to invalid data exception. Possible handshake rejection", (Throwable)invalidDataException);
            flushAndClose(invalidDataException.getCloseCode(), invalidDataException.getMessage(), false);
            return false;
          } catch (RuntimeException runtimeException) {
            log.error("Closing since client was never connected", runtimeException);
            this.wsl.onWebsocketError(this, runtimeException);
            flushAndClose(-1, runtimeException.getMessage(), false);
            return false;
          }  
        log.trace("Closing due to protocol error: draft {} refuses handshake", this.draft);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("draft ");
        stringBuilder.append(this.draft);
        stringBuilder.append(" refuses handshake");
        close(1002, stringBuilder.toString());
        return false;
      } 
    } catch (InvalidHandshakeException invalidHandshakeException) {
      log.trace("Closing due to invalid handshake", (Throwable)invalidHandshakeException);
      close((InvalidDataException)invalidHandshakeException);
      return false;
    } catch (IncompleteHandshakeException incompleteHandshakeException) {}
    return false;
  }
  
  private ByteBuffer generateHttpResponseDueToError(int paramInt) {
    String str;
    if (paramInt != 404) {
      str = "500 Internal Server Error";
    } else {
      str = "404 WebSocket Upgrade Failure";
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("HTTP/1.1 ");
    stringBuilder.append(str);
    stringBuilder.append("\r\nContent-Type: text/html\nServer: TooTallNate Java-WebSocket\r\nContent-Length: ");
    stringBuilder.append(str.length() + 48);
    stringBuilder.append("\r\n\r\n<html><head></head><body><h1>");
    stringBuilder.append(str);
    stringBuilder.append("</h1></body></html>");
    return ByteBuffer.wrap(Charsetfunctions.asciiBytes(stringBuilder.toString()));
  }
  
  private void open(Handshakedata paramHandshakedata) {
    log.trace("open using draft: {}", this.draft);
    this.readyState = ReadyState.OPEN;
    try {
      this.wsl.onWebsocketOpen(this, paramHandshakedata);
      return;
    } catch (RuntimeException runtimeException) {
      this.wsl.onWebsocketError(this, runtimeException);
      return;
    } 
  }
  
  private void send(Collection<Framedata> paramCollection) {
    if (isOpen()) {
      if (paramCollection != null) {
        ArrayList<ByteBuffer> arrayList = new ArrayList();
        for (Framedata framedata : paramCollection) {
          log.trace("send frame: {}", framedata);
          arrayList.add(this.draft.createBinaryFrame(framedata));
        } 
        write(arrayList);
        return;
      } 
      throw new IllegalArgumentException();
    } 
    throw new WebsocketNotConnectedException();
  }
  
  private void write(ByteBuffer paramByteBuffer) {
    String str;
    Logger logger = log;
    int i = paramByteBuffer.remaining();
    if (paramByteBuffer.remaining() > 1000) {
      str = "too big to display";
    } else {
      str = new String(paramByteBuffer.array());
    } 
    logger.trace("write({}): {}", Integer.valueOf(i), str);
    this.outQueue.add(paramByteBuffer);
    this.wsl.onWriteDemand(this);
  }
  
  private void write(List<ByteBuffer> paramList) {
    synchronized (this.synchronizeWriteObject) {
      Iterator<ByteBuffer> iterator = paramList.iterator();
      while (iterator.hasNext())
        write(iterator.next()); 
      return;
    } 
  }
  
  public void close() {
    close(1000);
  }
  
  public void close(int paramInt) {
    close(paramInt, "", false);
  }
  
  public void close(int paramInt, String paramString) {
    close(paramInt, paramString, false);
  }
  
  public void close(int paramInt, String paramString, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield readyState : Lorg/java_websocket/enums/ReadyState;
    //   6: getstatic org/java_websocket/enums/ReadyState.CLOSING : Lorg/java_websocket/enums/ReadyState;
    //   9: if_acmpeq -> 256
    //   12: aload_0
    //   13: getfield readyState : Lorg/java_websocket/enums/ReadyState;
    //   16: getstatic org/java_websocket/enums/ReadyState.CLOSED : Lorg/java_websocket/enums/ReadyState;
    //   19: if_acmpeq -> 256
    //   22: aload_0
    //   23: getfield readyState : Lorg/java_websocket/enums/ReadyState;
    //   26: getstatic org/java_websocket/enums/ReadyState.OPEN : Lorg/java_websocket/enums/ReadyState;
    //   29: if_acmpne -> 200
    //   32: iload_1
    //   33: sipush #1006
    //   36: if_icmpne -> 56
    //   39: aload_0
    //   40: getstatic org/java_websocket/enums/ReadyState.CLOSING : Lorg/java_websocket/enums/ReadyState;
    //   43: putfield readyState : Lorg/java_websocket/enums/ReadyState;
    //   46: aload_0
    //   47: iload_1
    //   48: aload_2
    //   49: iconst_0
    //   50: invokevirtual flushAndClose : (ILjava/lang/String;Z)V
    //   53: aload_0
    //   54: monitorexit
    //   55: return
    //   56: aload_0
    //   57: getfield draft : Lorg/java_websocket/drafts/Draft;
    //   60: invokevirtual getCloseHandshakeType : ()Lorg/java_websocket/enums/CloseHandshakeType;
    //   63: astore #4
    //   65: getstatic org/java_websocket/enums/CloseHandshakeType.NONE : Lorg/java_websocket/enums/CloseHandshakeType;
    //   68: astore #5
    //   70: aload #4
    //   72: aload #5
    //   74: if_acmpeq -> 190
    //   77: iload_3
    //   78: ifne -> 110
    //   81: aload_0
    //   82: getfield wsl : Lorg/java_websocket/WebSocketListener;
    //   85: aload_0
    //   86: iload_1
    //   87: aload_2
    //   88: invokeinterface onWebsocketCloseInitiated : (Lorg/java_websocket/WebSocket;ILjava/lang/String;)V
    //   93: goto -> 110
    //   96: astore #4
    //   98: aload_0
    //   99: getfield wsl : Lorg/java_websocket/WebSocketListener;
    //   102: aload_0
    //   103: aload #4
    //   105: invokeinterface onWebsocketError : (Lorg/java_websocket/WebSocket;Ljava/lang/Exception;)V
    //   110: aload_0
    //   111: invokevirtual isOpen : ()Z
    //   114: ifeq -> 190
    //   117: new org/java_websocket/framing/CloseFrame
    //   120: dup
    //   121: invokespecial <init> : ()V
    //   124: astore #4
    //   126: aload #4
    //   128: aload_2
    //   129: invokevirtual setReason : (Ljava/lang/String;)V
    //   132: aload #4
    //   134: iload_1
    //   135: invokevirtual setCode : (I)V
    //   138: aload #4
    //   140: invokevirtual isValid : ()V
    //   143: aload_0
    //   144: aload #4
    //   146: invokevirtual sendFrame : (Lorg/java_websocket/framing/Framedata;)V
    //   149: goto -> 190
    //   152: astore #4
    //   154: getstatic org/java_websocket/WebSocketImpl.log : Lorg/slf4j/Logger;
    //   157: ldc_w 'generated frame is invalid'
    //   160: aload #4
    //   162: invokeinterface error : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   167: aload_0
    //   168: getfield wsl : Lorg/java_websocket/WebSocketListener;
    //   171: aload_0
    //   172: aload #4
    //   174: invokeinterface onWebsocketError : (Lorg/java_websocket/WebSocket;Ljava/lang/Exception;)V
    //   179: aload_0
    //   180: sipush #1006
    //   183: ldc_w 'generated frame is invalid'
    //   186: iconst_0
    //   187: invokevirtual flushAndClose : (ILjava/lang/String;Z)V
    //   190: aload_0
    //   191: iload_1
    //   192: aload_2
    //   193: iload_3
    //   194: invokevirtual flushAndClose : (ILjava/lang/String;Z)V
    //   197: goto -> 241
    //   200: iload_1
    //   201: bipush #-3
    //   203: if_icmpne -> 217
    //   206: aload_0
    //   207: bipush #-3
    //   209: aload_2
    //   210: iconst_1
    //   211: invokevirtual flushAndClose : (ILjava/lang/String;Z)V
    //   214: goto -> 241
    //   217: iload_1
    //   218: sipush #1002
    //   221: if_icmpne -> 234
    //   224: aload_0
    //   225: iload_1
    //   226: aload_2
    //   227: iload_3
    //   228: invokevirtual flushAndClose : (ILjava/lang/String;Z)V
    //   231: goto -> 241
    //   234: aload_0
    //   235: iconst_m1
    //   236: aload_2
    //   237: iconst_0
    //   238: invokevirtual flushAndClose : (ILjava/lang/String;Z)V
    //   241: aload_0
    //   242: getstatic org/java_websocket/enums/ReadyState.CLOSING : Lorg/java_websocket/enums/ReadyState;
    //   245: putfield readyState : Lorg/java_websocket/enums/ReadyState;
    //   248: aload_0
    //   249: aconst_null
    //   250: putfield tmpHandshakeBytes : Ljava/nio/ByteBuffer;
    //   253: aload_0
    //   254: monitorexit
    //   255: return
    //   256: aload_0
    //   257: monitorexit
    //   258: return
    //   259: astore_2
    //   260: aload_0
    //   261: monitorexit
    //   262: aload_2
    //   263: athrow
    // Exception table:
    //   from	to	target	type
    //   2	32	259	finally
    //   39	53	259	finally
    //   56	70	259	finally
    //   81	93	96	java/lang/RuntimeException
    //   81	93	152	org/java_websocket/exceptions/InvalidDataException
    //   81	93	259	finally
    //   98	110	152	org/java_websocket/exceptions/InvalidDataException
    //   98	110	259	finally
    //   110	149	152	org/java_websocket/exceptions/InvalidDataException
    //   110	149	259	finally
    //   154	190	259	finally
    //   190	197	259	finally
    //   206	214	259	finally
    //   224	231	259	finally
    //   234	241	259	finally
    //   241	253	259	finally
  }
  
  public void close(InvalidDataException paramInvalidDataException) {
    close(paramInvalidDataException.getCloseCode(), paramInvalidDataException.getMessage(), false);
  }
  
  public void closeConnection() {
    if (this.closedremotely != null) {
      closeConnection(this.closecode.intValue(), this.closemessage, this.closedremotely.booleanValue());
      return;
    } 
    throw new IllegalStateException("this method must be used in conjunction with flushAndClose");
  }
  
  public void closeConnection(int paramInt, String paramString) {
    closeConnection(paramInt, paramString, false);
  }
  
  public void closeConnection(int paramInt, String paramString, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield readyState : Lorg/java_websocket/enums/ReadyState;
    //   6: astore #4
    //   8: getstatic org/java_websocket/enums/ReadyState.CLOSED : Lorg/java_websocket/enums/ReadyState;
    //   11: astore #5
    //   13: aload #4
    //   15: aload #5
    //   17: if_acmpne -> 23
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: aload_0
    //   24: getfield readyState : Lorg/java_websocket/enums/ReadyState;
    //   27: getstatic org/java_websocket/enums/ReadyState.OPEN : Lorg/java_websocket/enums/ReadyState;
    //   30: if_acmpne -> 47
    //   33: iload_1
    //   34: sipush #1006
    //   37: if_icmpne -> 47
    //   40: aload_0
    //   41: getstatic org/java_websocket/enums/ReadyState.CLOSING : Lorg/java_websocket/enums/ReadyState;
    //   44: putfield readyState : Lorg/java_websocket/enums/ReadyState;
    //   47: aload_0
    //   48: getfield key : Ljava/nio/channels/SelectionKey;
    //   51: ifnull -> 61
    //   54: aload_0
    //   55: getfield key : Ljava/nio/channels/SelectionKey;
    //   58: invokevirtual cancel : ()V
    //   61: aload_0
    //   62: getfield channel : Ljava/nio/channels/ByteChannel;
    //   65: astore #4
    //   67: aload #4
    //   69: ifnull -> 141
    //   72: aload_0
    //   73: getfield channel : Ljava/nio/channels/ByteChannel;
    //   76: invokeinterface close : ()V
    //   81: goto -> 141
    //   84: astore #4
    //   86: aload #4
    //   88: invokevirtual getMessage : ()Ljava/lang/String;
    //   91: ldc_w 'Broken pipe'
    //   94: invokevirtual equals : (Ljava/lang/Object;)Z
    //   97: ifeq -> 116
    //   100: getstatic org/java_websocket/WebSocketImpl.log : Lorg/slf4j/Logger;
    //   103: ldc_w 'Caught IOException: Broken pipe during closeConnection()'
    //   106: aload #4
    //   108: invokeinterface trace : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   113: goto -> 141
    //   116: getstatic org/java_websocket/WebSocketImpl.log : Lorg/slf4j/Logger;
    //   119: ldc_w 'Exception during channel.close()'
    //   122: aload #4
    //   124: invokeinterface error : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   129: aload_0
    //   130: getfield wsl : Lorg/java_websocket/WebSocketListener;
    //   133: aload_0
    //   134: aload #4
    //   136: invokeinterface onWebsocketError : (Lorg/java_websocket/WebSocket;Ljava/lang/Exception;)V
    //   141: aload_0
    //   142: getfield wsl : Lorg/java_websocket/WebSocketListener;
    //   145: aload_0
    //   146: iload_1
    //   147: aload_2
    //   148: iload_3
    //   149: invokeinterface onWebsocketClose : (Lorg/java_websocket/WebSocket;ILjava/lang/String;Z)V
    //   154: goto -> 169
    //   157: astore_2
    //   158: aload_0
    //   159: getfield wsl : Lorg/java_websocket/WebSocketListener;
    //   162: aload_0
    //   163: aload_2
    //   164: invokeinterface onWebsocketError : (Lorg/java_websocket/WebSocket;Ljava/lang/Exception;)V
    //   169: aload_0
    //   170: getfield draft : Lorg/java_websocket/drafts/Draft;
    //   173: ifnull -> 183
    //   176: aload_0
    //   177: getfield draft : Lorg/java_websocket/drafts/Draft;
    //   180: invokevirtual reset : ()V
    //   183: aload_0
    //   184: aconst_null
    //   185: putfield handshakerequest : Lorg/java_websocket/handshake/ClientHandshake;
    //   188: aload_0
    //   189: getstatic org/java_websocket/enums/ReadyState.CLOSED : Lorg/java_websocket/enums/ReadyState;
    //   192: putfield readyState : Lorg/java_websocket/enums/ReadyState;
    //   195: aload_0
    //   196: monitorexit
    //   197: return
    //   198: astore_2
    //   199: aload_0
    //   200: monitorexit
    //   201: aload_2
    //   202: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	198	finally
    //   23	33	198	finally
    //   40	47	198	finally
    //   47	61	198	finally
    //   61	67	198	finally
    //   72	81	84	java/io/IOException
    //   72	81	198	finally
    //   86	113	198	finally
    //   116	141	198	finally
    //   141	154	157	java/lang/RuntimeException
    //   141	154	198	finally
    //   158	169	198	finally
    //   169	183	198	finally
    //   183	195	198	finally
  }
  
  protected void closeConnection(int paramInt, boolean paramBoolean) {
    closeConnection(paramInt, "", paramBoolean);
  }
  
  public void decode(ByteBuffer paramByteBuffer) {
    String str;
    Logger logger = log;
    int i = paramByteBuffer.remaining();
    if (paramByteBuffer.remaining() > 1000) {
      str = "too big to display";
    } else {
      str = new String(paramByteBuffer.array(), paramByteBuffer.position(), paramByteBuffer.remaining());
    } 
    logger.trace("process({}): ({})", Integer.valueOf(i), str);
    if (this.readyState != ReadyState.NOT_YET_CONNECTED) {
      if (this.readyState == ReadyState.OPEN) {
        decodeFrames(paramByteBuffer);
        return;
      } 
    } else if (decodeHandshake(paramByteBuffer) && !isClosing() && !isClosed()) {
      if (paramByteBuffer.hasRemaining()) {
        decodeFrames(paramByteBuffer);
        return;
      } 
      if (this.tmpHandshakeBytes.hasRemaining())
        decodeFrames(this.tmpHandshakeBytes); 
    } 
  }
  
  public void eot() {
    if (this.readyState == ReadyState.NOT_YET_CONNECTED) {
      closeConnection(-1, true);
      return;
    } 
    if (this.flushandclosestate) {
      closeConnection(this.closecode.intValue(), this.closemessage, this.closedremotely.booleanValue());
      return;
    } 
    if (this.draft.getCloseHandshakeType() == CloseHandshakeType.NONE) {
      closeConnection(1000, true);
      return;
    } 
    if (this.draft.getCloseHandshakeType() == CloseHandshakeType.ONEWAY) {
      if (this.role == Role.SERVER) {
        closeConnection(1006, true);
        return;
      } 
      closeConnection(1000, true);
      return;
    } 
    closeConnection(1006, true);
  }
  
  public void flushAndClose(int paramInt, String paramString, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield flushandclosestate : Z
    //   6: istore #4
    //   8: iload #4
    //   10: ifeq -> 16
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: aload_0
    //   17: iload_1
    //   18: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   21: putfield closecode : Ljava/lang/Integer;
    //   24: aload_0
    //   25: aload_2
    //   26: putfield closemessage : Ljava/lang/String;
    //   29: aload_0
    //   30: iload_3
    //   31: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   34: putfield closedremotely : Ljava/lang/Boolean;
    //   37: aload_0
    //   38: iconst_1
    //   39: putfield flushandclosestate : Z
    //   42: aload_0
    //   43: getfield wsl : Lorg/java_websocket/WebSocketListener;
    //   46: aload_0
    //   47: invokeinterface onWriteDemand : (Lorg/java_websocket/WebSocket;)V
    //   52: aload_0
    //   53: getfield wsl : Lorg/java_websocket/WebSocketListener;
    //   56: aload_0
    //   57: iload_1
    //   58: aload_2
    //   59: iload_3
    //   60: invokeinterface onWebsocketClosing : (Lorg/java_websocket/WebSocket;ILjava/lang/String;Z)V
    //   65: goto -> 92
    //   68: astore_2
    //   69: getstatic org/java_websocket/WebSocketImpl.log : Lorg/slf4j/Logger;
    //   72: ldc_w 'Exception in onWebsocketClosing'
    //   75: aload_2
    //   76: invokeinterface error : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   81: aload_0
    //   82: getfield wsl : Lorg/java_websocket/WebSocketListener;
    //   85: aload_0
    //   86: aload_2
    //   87: invokeinterface onWebsocketError : (Lorg/java_websocket/WebSocket;Ljava/lang/Exception;)V
    //   92: aload_0
    //   93: getfield draft : Lorg/java_websocket/drafts/Draft;
    //   96: ifnull -> 106
    //   99: aload_0
    //   100: getfield draft : Lorg/java_websocket/drafts/Draft;
    //   103: invokevirtual reset : ()V
    //   106: aload_0
    //   107: aconst_null
    //   108: putfield handshakerequest : Lorg/java_websocket/handshake/ClientHandshake;
    //   111: aload_0
    //   112: monitorexit
    //   113: return
    //   114: astore_2
    //   115: aload_0
    //   116: monitorexit
    //   117: aload_2
    //   118: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	114	finally
    //   16	52	114	finally
    //   52	65	68	java/lang/RuntimeException
    //   52	65	114	finally
    //   69	92	114	finally
    //   92	106	114	finally
    //   106	111	114	finally
  }
  
  public <T> T getAttachment() {
    return (T)this.attachment;
  }
  
  public ByteChannel getChannel() {
    return this.channel;
  }
  
  public Draft getDraft() {
    return this.draft;
  }
  
  long getLastPong() {
    return this.lastPong;
  }
  
  public InetSocketAddress getLocalSocketAddress() {
    return this.wsl.getLocalSocketAddress(this);
  }
  
  public ReadyState getReadyState() {
    return this.readyState;
  }
  
  public InetSocketAddress getRemoteSocketAddress() {
    return this.wsl.getRemoteSocketAddress(this);
  }
  
  public String getResourceDescriptor() {
    return this.resourceDescriptor;
  }
  
  public SelectionKey getSelectionKey() {
    return this.key;
  }
  
  public WebSocketListener getWebSocketListener() {
    return this.wsl;
  }
  
  public WebSocketServer.WebSocketWorker getWorkerThread() {
    return this.workerThread;
  }
  
  public boolean hasBufferedData() {
    return this.outQueue.isEmpty() ^ true;
  }
  
  public boolean isClosed() {
    return (this.readyState == ReadyState.CLOSED);
  }
  
  public boolean isClosing() {
    return (this.readyState == ReadyState.CLOSING);
  }
  
  public boolean isFlushAndClose() {
    return this.flushandclosestate;
  }
  
  public boolean isOpen() {
    return (this.readyState == ReadyState.OPEN);
  }
  
  public void send(String paramString) {
    if (paramString != null) {
      boolean bool;
      Draft draft = this.draft;
      if (this.role == Role.CLIENT) {
        bool = true;
      } else {
        bool = false;
      } 
      send(draft.createFrames(paramString, bool));
      return;
    } 
    throw new IllegalArgumentException("Cannot send 'null' data to a WebSocketImpl.");
  }
  
  public void send(ByteBuffer paramByteBuffer) {
    if (paramByteBuffer != null) {
      boolean bool;
      Draft draft = this.draft;
      if (this.role == Role.CLIENT) {
        bool = true;
      } else {
        bool = false;
      } 
      send(draft.createFrames(paramByteBuffer, bool));
      return;
    } 
    throw new IllegalArgumentException("Cannot send 'null' data to a WebSocketImpl.");
  }
  
  public void send(byte[] paramArrayOfbyte) {
    send(ByteBuffer.wrap(paramArrayOfbyte));
  }
  
  public void sendFragmentedFrame(Opcode paramOpcode, ByteBuffer paramByteBuffer, boolean paramBoolean) {
    send(this.draft.continuousFrame(paramOpcode, paramByteBuffer, paramBoolean));
  }
  
  public void sendFrame(Collection<Framedata> paramCollection) {
    send(paramCollection);
  }
  
  public void sendFrame(Framedata paramFramedata) {
    send(Collections.singletonList(paramFramedata));
  }
  
  public void sendPing() {
    if (this.pingFrame == null)
      this.pingFrame = new PingFrame(); 
    sendFrame((Framedata)this.pingFrame);
  }
  
  public <T> void setAttachment(T paramT) {
    this.attachment = paramT;
  }
  
  public void setChannel(ByteChannel paramByteChannel) {
    this.channel = paramByteChannel;
  }
  
  public void setSelectionKey(SelectionKey paramSelectionKey) {
    this.key = paramSelectionKey;
  }
  
  public void setWorkerThread(WebSocketServer.WebSocketWorker paramWebSocketWorker) {
    this.workerThread = paramWebSocketWorker;
  }
  
  public void startHandshake(ClientHandshakeBuilder paramClientHandshakeBuilder) throws InvalidHandshakeException {
    this.handshakerequest = (ClientHandshake)this.draft.postProcessHandshakeRequestAsClient(paramClientHandshakeBuilder);
    this.resourceDescriptor = paramClientHandshakeBuilder.getResourceDescriptor();
    try {
      this.wsl.onWebsocketHandshakeSentAsClient(this, this.handshakerequest);
      write(this.draft.createHandshake((Handshakedata)this.handshakerequest));
      return;
    } catch (InvalidDataException invalidDataException) {
      throw new InvalidHandshakeException("Handshake data rejected by client.");
    } catch (RuntimeException runtimeException) {
      log.error("Exception in startHandshake", runtimeException);
      this.wsl.onWebsocketError(this, runtimeException);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("rejected because of ");
      stringBuilder.append(runtimeException);
      throw new InvalidHandshakeException(stringBuilder.toString());
    } 
  }
  
  public String toString() {
    return super.toString();
  }
  
  public void updateLastPong() {
    this.lastPong = System.nanoTime();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\WebSocketImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */