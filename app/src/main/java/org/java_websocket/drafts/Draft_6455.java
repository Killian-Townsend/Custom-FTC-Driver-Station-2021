package org.java_websocket.drafts;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.enums.CloseHandshakeType;
import org.java_websocket.enums.HandshakeState;
import org.java_websocket.enums.Opcode;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.enums.Role;
import org.java_websocket.exceptions.IncompleteException;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidFrameException;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.exceptions.LimitExceededException;
import org.java_websocket.exceptions.NotSendableException;
import org.java_websocket.extensions.DefaultExtension;
import org.java_websocket.extensions.IExtension;
import org.java_websocket.framing.BinaryFrame;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.framing.TextFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ClientHandshakeBuilder;
import org.java_websocket.handshake.HandshakeBuilder;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.protocols.IProtocol;
import org.java_websocket.protocols.Protocol;
import org.java_websocket.util.Base64;
import org.java_websocket.util.Charsetfunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Draft_6455 extends Draft {
  private static final String CONNECTION = "Connection";
  
  private static final String SEC_WEB_SOCKET_ACCEPT = "Sec-WebSocket-Accept";
  
  private static final String SEC_WEB_SOCKET_EXTENSIONS = "Sec-WebSocket-Extensions";
  
  private static final String SEC_WEB_SOCKET_KEY = "Sec-WebSocket-Key";
  
  private static final String SEC_WEB_SOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
  
  private static final String UPGRADE = "Upgrade";
  
  private static final Logger log = LoggerFactory.getLogger(Draft_6455.class);
  
  private final List<ByteBuffer> byteBufferList;
  
  private Framedata currentContinuousFrame;
  
  private IExtension extension = (IExtension)new DefaultExtension();
  
  private ByteBuffer incompleteframe;
  
  private List<IExtension> knownExtensions;
  
  private List<IProtocol> knownProtocols;
  
  private int maxFrameSize;
  
  private IProtocol protocol;
  
  private final Random reuseableRandom = new Random();
  
  public Draft_6455() {
    this(Collections.emptyList());
  }
  
  public Draft_6455(List<IExtension> paramList) {
    this(paramList, (List)Collections.singletonList(new Protocol("")));
  }
  
  public Draft_6455(List<IExtension> paramList, int paramInt) {
    this(paramList, (List)Collections.singletonList(new Protocol("")), paramInt);
  }
  
  public Draft_6455(List<IExtension> paramList, List<IProtocol> paramList1) {
    this(paramList, paramList1, 2147483647);
  }
  
  public Draft_6455(List<IExtension> paramList, List<IProtocol> paramList1, int paramInt) {
    if (paramList != null && paramList1 != null && paramInt >= 1) {
      this.knownExtensions = new ArrayList<IExtension>(paramList.size());
      this.knownProtocols = new ArrayList<IProtocol>(paramList1.size());
      boolean bool = false;
      this.byteBufferList = new ArrayList<ByteBuffer>();
      Iterator<IExtension> iterator = paramList.iterator();
      while (iterator.hasNext()) {
        if (((IExtension)iterator.next()).getClass().equals(DefaultExtension.class))
          bool = true; 
      } 
      this.knownExtensions.addAll(paramList);
      if (!bool) {
        paramList = this.knownExtensions;
        paramList.add(paramList.size(), this.extension);
      } 
      this.knownProtocols.addAll(paramList1);
      this.maxFrameSize = paramInt;
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public Draft_6455(IExtension paramIExtension) {
    this(Collections.singletonList(paramIExtension));
  }
  
  private void addToBufferList(ByteBuffer paramByteBuffer) {
    synchronized (this.byteBufferList) {
      this.byteBufferList.add(paramByteBuffer);
      return;
    } 
  }
  
  private void checkBufferLimit() throws LimitExceededException {
    long l = getByteBufferListSize();
    if (l <= this.maxFrameSize)
      return; 
    clearBufferList();
    log.trace("Payload limit reached. Allowed: {} Current: {}", Integer.valueOf(this.maxFrameSize), Long.valueOf(l));
    throw new LimitExceededException(this.maxFrameSize);
  }
  
  private void clearBufferList() {
    synchronized (this.byteBufferList) {
      this.byteBufferList.clear();
      return;
    } 
  }
  
  private HandshakeState containsRequestedProtocol(String paramString) {
    for (IProtocol iProtocol : this.knownProtocols) {
      if (iProtocol.acceptProvidedProtocol(paramString)) {
        this.protocol = iProtocol;
        log.trace("acceptHandshake - Matching protocol found: {}", iProtocol);
        return HandshakeState.MATCHED;
      } 
    } 
    return HandshakeState.NOT_MATCHED;
  }
  
  private ByteBuffer createByteBufferFromFramedata(Framedata paramFramedata) {
    int i;
    boolean bool2;
    ByteBuffer byteBuffer1 = paramFramedata.getPayloadData();
    Role role1 = this.role;
    Role role2 = Role.CLIENT;
    boolean bool1 = false;
    if (role1 == role2) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    int j = getSizeBytes(byteBuffer1);
    if (j > 1) {
      i = j + 1;
    } else {
      i = j;
    } 
    if (bool2) {
      b = 4;
    } else {
      b = 0;
    } 
    ByteBuffer byteBuffer2 = ByteBuffer.allocate(i + 1 + b + byteBuffer1.remaining());
    byte b = fromOpcode(paramFramedata.getOpcode());
    if (paramFramedata.isFin()) {
      i = -128;
    } else {
      i = 0;
    } 
    byteBuffer2.put((byte)((byte)i | b));
    byte[] arrayOfByte = toByteArray(byteBuffer1.remaining(), j);
    if (j == 1) {
      byteBuffer2.put((byte)(arrayOfByte[0] | getMaskByte(bool2)));
    } else if (j == 2) {
      byteBuffer2.put((byte)(getMaskByte(bool2) | 0x7E));
      byteBuffer2.put(arrayOfByte);
    } else if (j == 8) {
      byteBuffer2.put((byte)(getMaskByte(bool2) | Byte.MAX_VALUE));
      byteBuffer2.put(arrayOfByte);
    } else {
      throw new IllegalStateException("Size representation not supported/specified");
    } 
    if (bool2) {
      ByteBuffer byteBuffer = ByteBuffer.allocate(4);
      byteBuffer.putInt(this.reuseableRandom.nextInt());
      byteBuffer2.put(byteBuffer.array());
      for (i = bool1; byteBuffer1.hasRemaining(); i++)
        byteBuffer2.put((byte)(byteBuffer1.get() ^ byteBuffer.get(i % 4))); 
    } else {
      byteBuffer2.put(byteBuffer1);
      byteBuffer1.flip();
    } 
    byteBuffer2.flip();
    return byteBuffer2;
  }
  
  private byte fromOpcode(Opcode paramOpcode) {
    if (paramOpcode == Opcode.CONTINUOUS)
      return 0; 
    if (paramOpcode == Opcode.TEXT)
      return 1; 
    if (paramOpcode == Opcode.BINARY)
      return 2; 
    if (paramOpcode == Opcode.CLOSING)
      return 8; 
    if (paramOpcode == Opcode.PING)
      return 9; 
    if (paramOpcode == Opcode.PONG)
      return 10; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Don't know how to handle ");
    stringBuilder.append(paramOpcode.toString());
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private String generateFinalKey(String paramString) {
    paramString = paramString.trim();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append("258EAFA5-E914-47DA-95CA-C5AB0DC85B11");
    paramString = stringBuilder.toString();
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
      return Base64.encodeBytes(messageDigest.digest(paramString.getBytes()));
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new IllegalStateException(noSuchAlgorithmException);
    } 
  }
  
  private long getByteBufferListSize() {
    synchronized (this.byteBufferList) {
      Iterator<ByteBuffer> iterator = this.byteBufferList.iterator();
      long l;
      for (l = 0L; iterator.hasNext(); l += ((ByteBuffer)iterator.next()).limit());
      return l;
    } 
  }
  
  private byte getMaskByte(boolean paramBoolean) {
    return paramBoolean ? Byte.MIN_VALUE : 0;
  }
  
  private ByteBuffer getPayloadFromByteBufferList() throws LimitExceededException {
    synchronized (this.byteBufferList) {
      Iterator<ByteBuffer> iterator1 = this.byteBufferList.iterator();
      long l;
      for (l = 0L; iterator1.hasNext(); l += ((ByteBuffer)iterator1.next()).limit());
      checkBufferLimit();
      ByteBuffer byteBuffer = ByteBuffer.allocate((int)l);
      Iterator<ByteBuffer> iterator2 = this.byteBufferList.iterator();
      while (iterator2.hasNext())
        byteBuffer.put(iterator2.next()); 
      byteBuffer.flip();
      return byteBuffer;
    } 
  }
  
  private String getServerTime() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return simpleDateFormat.format(calendar.getTime());
  }
  
  private int getSizeBytes(ByteBuffer paramByteBuffer) {
    return (paramByteBuffer.remaining() <= 125) ? 1 : ((paramByteBuffer.remaining() <= 65535) ? 2 : 8);
  }
  
  private void logRuntimeException(WebSocketImpl paramWebSocketImpl, RuntimeException paramRuntimeException) {
    log.error("Runtime exception during onWebsocketMessage", paramRuntimeException);
    paramWebSocketImpl.getWebSocketListener().onWebsocketError((WebSocket)paramWebSocketImpl, paramRuntimeException);
  }
  
  private void processFrameBinary(WebSocketImpl paramWebSocketImpl, Framedata paramFramedata) {
    try {
      paramWebSocketImpl.getWebSocketListener().onWebsocketMessage((WebSocket)paramWebSocketImpl, paramFramedata.getPayloadData());
      return;
    } catch (RuntimeException runtimeException) {
      logRuntimeException(paramWebSocketImpl, runtimeException);
      return;
    } 
  }
  
  private void processFrameClosing(WebSocketImpl paramWebSocketImpl, Framedata paramFramedata) {
    String str;
    char c;
    if (paramFramedata instanceof CloseFrame) {
      CloseFrame closeFrame = (CloseFrame)paramFramedata;
      c = closeFrame.getCloseCode();
      str = closeFrame.getMessage();
    } else {
      c = 'ϭ';
      str = "";
    } 
    if (paramWebSocketImpl.getReadyState() == ReadyState.CLOSING) {
      paramWebSocketImpl.closeConnection(c, str, true);
      return;
    } 
    if (getCloseHandshakeType() == CloseHandshakeType.TWOWAY) {
      paramWebSocketImpl.close(c, str, true);
      return;
    } 
    paramWebSocketImpl.flushAndClose(c, str, false);
  }
  
  private void processFrameContinuousAndNonFin(WebSocketImpl paramWebSocketImpl, Framedata paramFramedata, Opcode paramOpcode) throws InvalidDataException {
    if (paramOpcode != Opcode.CONTINUOUS) {
      processFrameIsNotFin(paramFramedata);
    } else if (paramFramedata.isFin()) {
      processFrameIsFin(paramWebSocketImpl, paramFramedata);
    } else if (this.currentContinuousFrame == null) {
      log.error("Protocol error: Continuous frame sequence was not started.");
      throw new InvalidDataException(1002, "Continuous frame sequence was not started.");
    } 
    if (paramOpcode != Opcode.TEXT || Charsetfunctions.isValidUTF8(paramFramedata.getPayloadData())) {
      if (paramOpcode == Opcode.CONTINUOUS && this.currentContinuousFrame != null)
        addToBufferList(paramFramedata.getPayloadData()); 
      return;
    } 
    log.error("Protocol error: Payload is not UTF8");
    throw new InvalidDataException(1007);
  }
  
  private void processFrameIsFin(WebSocketImpl paramWebSocketImpl, Framedata paramFramedata) throws InvalidDataException {
    if (this.currentContinuousFrame != null) {
      addToBufferList(paramFramedata.getPayloadData());
      checkBufferLimit();
      if (this.currentContinuousFrame.getOpcode() == Opcode.TEXT) {
        ((FramedataImpl1)this.currentContinuousFrame).setPayload(getPayloadFromByteBufferList());
        ((FramedataImpl1)this.currentContinuousFrame).isValid();
        try {
          paramWebSocketImpl.getWebSocketListener().onWebsocketMessage((WebSocket)paramWebSocketImpl, Charsetfunctions.stringUtf8(this.currentContinuousFrame.getPayloadData()));
        } catch (RuntimeException runtimeException) {
          logRuntimeException(paramWebSocketImpl, runtimeException);
        } 
      } else if (this.currentContinuousFrame.getOpcode() == Opcode.BINARY) {
        ((FramedataImpl1)this.currentContinuousFrame).setPayload(getPayloadFromByteBufferList());
        ((FramedataImpl1)this.currentContinuousFrame).isValid();
        try {
          paramWebSocketImpl.getWebSocketListener().onWebsocketMessage((WebSocket)paramWebSocketImpl, this.currentContinuousFrame.getPayloadData());
        } catch (RuntimeException runtimeException) {
          logRuntimeException(paramWebSocketImpl, runtimeException);
        } 
      } 
      this.currentContinuousFrame = null;
      clearBufferList();
      return;
    } 
    log.trace("Protocol error: Previous continuous frame sequence not completed.");
    throw new InvalidDataException(1002, "Continuous frame sequence was not started.");
  }
  
  private void processFrameIsNotFin(Framedata paramFramedata) throws InvalidDataException {
    if (this.currentContinuousFrame == null) {
      this.currentContinuousFrame = paramFramedata;
      addToBufferList(paramFramedata.getPayloadData());
      checkBufferLimit();
      return;
    } 
    log.trace("Protocol error: Previous continuous frame sequence not completed.");
    throw new InvalidDataException(1002, "Previous continuous frame sequence not completed.");
  }
  
  private void processFrameText(WebSocketImpl paramWebSocketImpl, Framedata paramFramedata) throws InvalidDataException {
    try {
      paramWebSocketImpl.getWebSocketListener().onWebsocketMessage((WebSocket)paramWebSocketImpl, Charsetfunctions.stringUtf8(paramFramedata.getPayloadData()));
      return;
    } catch (RuntimeException runtimeException) {
      logRuntimeException(paramWebSocketImpl, runtimeException);
      return;
    } 
  }
  
  private byte[] toByteArray(long paramLong, int paramInt) {
    byte[] arrayOfByte = new byte[paramInt];
    int i;
    for (i = 0; i < paramInt; i++)
      arrayOfByte[i] = (byte)(int)(paramLong >>> paramInt * 8 - 8 - i * 8); 
    return arrayOfByte;
  }
  
  private Opcode toOpcode(byte paramByte) throws InvalidFrameException {
    if (paramByte != 0) {
      if (paramByte != 1) {
        if (paramByte != 2) {
          StringBuilder stringBuilder;
          switch (paramByte) {
            default:
              stringBuilder = new StringBuilder();
              stringBuilder.append("Unknown opcode ");
              stringBuilder.append((short)paramByte);
              throw new InvalidFrameException(stringBuilder.toString());
            case 10:
              return Opcode.PONG;
            case 9:
              return Opcode.PING;
            case 8:
              break;
          } 
          return Opcode.CLOSING;
        } 
        return Opcode.BINARY;
      } 
      return Opcode.TEXT;
    } 
    return Opcode.CONTINUOUS;
  }
  
  private Framedata translateSingleFrame(ByteBuffer paramByteBuffer) throws IncompleteException, InvalidDataException {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 454
    //   4: aload_1
    //   5: invokevirtual remaining : ()I
    //   8: istore #7
    //   10: iconst_2
    //   11: istore #4
    //   13: aload_0
    //   14: iload #7
    //   16: iconst_2
    //   17: invokespecial translateSingleFrameCheckPacketSize : (II)V
    //   20: aload_1
    //   21: invokevirtual get : ()B
    //   24: istore_3
    //   25: iconst_0
    //   26: istore #6
    //   28: iload_3
    //   29: bipush #8
    //   31: ishr
    //   32: ifeq -> 41
    //   35: iconst_1
    //   36: istore #8
    //   38: goto -> 44
    //   41: iconst_0
    //   42: istore #8
    //   44: iload_3
    //   45: bipush #64
    //   47: iand
    //   48: ifeq -> 57
    //   51: iconst_1
    //   52: istore #9
    //   54: goto -> 60
    //   57: iconst_0
    //   58: istore #9
    //   60: iload_3
    //   61: bipush #32
    //   63: iand
    //   64: ifeq -> 73
    //   67: iconst_1
    //   68: istore #10
    //   70: goto -> 76
    //   73: iconst_0
    //   74: istore #10
    //   76: iload_3
    //   77: bipush #16
    //   79: iand
    //   80: ifeq -> 89
    //   83: iconst_1
    //   84: istore #11
    //   86: goto -> 92
    //   89: iconst_0
    //   90: istore #11
    //   92: aload_1
    //   93: invokevirtual get : ()B
    //   96: istore #5
    //   98: iload #5
    //   100: bipush #-128
    //   102: iand
    //   103: ifeq -> 111
    //   106: iconst_1
    //   107: istore_2
    //   108: goto -> 113
    //   111: iconst_0
    //   112: istore_2
    //   113: iload #5
    //   115: bipush #127
    //   117: iand
    //   118: i2b
    //   119: istore #5
    //   121: aload_0
    //   122: iload_3
    //   123: bipush #15
    //   125: iand
    //   126: i2b
    //   127: invokespecial toOpcode : (B)Lorg/java_websocket/enums/Opcode;
    //   130: astore #12
    //   132: iload #5
    //   134: iflt -> 147
    //   137: iload #5
    //   139: istore_3
    //   140: iload #5
    //   142: bipush #125
    //   144: if_icmple -> 174
    //   147: aload_0
    //   148: aload_1
    //   149: aload #12
    //   151: iload #5
    //   153: iload #7
    //   155: iconst_2
    //   156: invokespecial translateSingleFramePayloadLength : (Ljava/nio/ByteBuffer;Lorg/java_websocket/enums/Opcode;III)Lorg/java_websocket/drafts/Draft_6455$TranslatedPayloadMetaData;
    //   159: astore #13
    //   161: aload #13
    //   163: invokestatic access$000 : (Lorg/java_websocket/drafts/Draft_6455$TranslatedPayloadMetaData;)I
    //   166: istore_3
    //   167: aload #13
    //   169: invokestatic access$100 : (Lorg/java_websocket/drafts/Draft_6455$TranslatedPayloadMetaData;)I
    //   172: istore #4
    //   174: aload_0
    //   175: iload_3
    //   176: i2l
    //   177: invokespecial translateSingleFrameCheckLengthLimit : (J)V
    //   180: iload_2
    //   181: ifeq -> 190
    //   184: iconst_4
    //   185: istore #5
    //   187: goto -> 193
    //   190: iconst_0
    //   191: istore #5
    //   193: aload_0
    //   194: iload #7
    //   196: iload #4
    //   198: iload #5
    //   200: iadd
    //   201: iload_3
    //   202: iadd
    //   203: invokespecial translateSingleFrameCheckPacketSize : (II)V
    //   206: aload_0
    //   207: iload_3
    //   208: invokevirtual checkAlloc : (I)I
    //   211: invokestatic allocate : (I)Ljava/nio/ByteBuffer;
    //   214: astore #13
    //   216: iload_2
    //   217: ifeq -> 265
    //   220: iconst_4
    //   221: newarray byte
    //   223: astore #14
    //   225: aload_1
    //   226: aload #14
    //   228: invokevirtual get : ([B)Ljava/nio/ByteBuffer;
    //   231: pop
    //   232: iload #6
    //   234: istore_2
    //   235: iload_2
    //   236: iload_3
    //   237: if_icmpge -> 299
    //   240: aload #13
    //   242: aload_1
    //   243: invokevirtual get : ()B
    //   246: aload #14
    //   248: iload_2
    //   249: iconst_4
    //   250: irem
    //   251: baload
    //   252: ixor
    //   253: i2b
    //   254: invokevirtual put : (B)Ljava/nio/ByteBuffer;
    //   257: pop
    //   258: iload_2
    //   259: iconst_1
    //   260: iadd
    //   261: istore_2
    //   262: goto -> 235
    //   265: aload #13
    //   267: aload_1
    //   268: invokevirtual array : ()[B
    //   271: aload_1
    //   272: invokevirtual position : ()I
    //   275: aload #13
    //   277: invokevirtual limit : ()I
    //   280: invokevirtual put : ([BII)Ljava/nio/ByteBuffer;
    //   283: pop
    //   284: aload_1
    //   285: aload_1
    //   286: invokevirtual position : ()I
    //   289: aload #13
    //   291: invokevirtual limit : ()I
    //   294: iadd
    //   295: invokevirtual position : (I)Ljava/nio/Buffer;
    //   298: pop
    //   299: aload #12
    //   301: invokestatic get : (Lorg/java_websocket/enums/Opcode;)Lorg/java_websocket/framing/FramedataImpl1;
    //   304: astore #12
    //   306: aload #12
    //   308: iload #8
    //   310: invokevirtual setFin : (Z)V
    //   313: aload #12
    //   315: iload #9
    //   317: invokevirtual setRSV1 : (Z)V
    //   320: aload #12
    //   322: iload #10
    //   324: invokevirtual setRSV2 : (Z)V
    //   327: aload #12
    //   329: iload #11
    //   331: invokevirtual setRSV3 : (Z)V
    //   334: aload #13
    //   336: invokevirtual flip : ()Ljava/nio/Buffer;
    //   339: pop
    //   340: aload #12
    //   342: aload #13
    //   344: invokevirtual setPayload : (Ljava/nio/ByteBuffer;)V
    //   347: aload_0
    //   348: invokevirtual getExtension : ()Lorg/java_websocket/extensions/IExtension;
    //   351: aload #12
    //   353: invokeinterface isFrameValid : (Lorg/java_websocket/framing/Framedata;)V
    //   358: aload_0
    //   359: invokevirtual getExtension : ()Lorg/java_websocket/extensions/IExtension;
    //   362: aload #12
    //   364: invokeinterface decodeFrame : (Lorg/java_websocket/framing/Framedata;)V
    //   369: getstatic org/java_websocket/drafts/Draft_6455.log : Lorg/slf4j/Logger;
    //   372: invokeinterface isTraceEnabled : ()Z
    //   377: ifeq -> 446
    //   380: getstatic org/java_websocket/drafts/Draft_6455.log : Lorg/slf4j/Logger;
    //   383: astore #13
    //   385: aload #12
    //   387: invokevirtual getPayloadData : ()Ljava/nio/ByteBuffer;
    //   390: invokevirtual remaining : ()I
    //   393: istore_2
    //   394: aload #12
    //   396: invokevirtual getPayloadData : ()Ljava/nio/ByteBuffer;
    //   399: invokevirtual remaining : ()I
    //   402: sipush #1000
    //   405: if_icmple -> 415
    //   408: ldc_w 'too big to display'
    //   411: astore_1
    //   412: goto -> 431
    //   415: new java/lang/String
    //   418: dup
    //   419: aload #12
    //   421: invokevirtual getPayloadData : ()Ljava/nio/ByteBuffer;
    //   424: invokevirtual array : ()[B
    //   427: invokespecial <init> : ([B)V
    //   430: astore_1
    //   431: aload #13
    //   433: ldc_w 'afterDecoding({}): {}'
    //   436: iload_2
    //   437: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   440: aload_1
    //   441: invokeinterface trace : (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
    //   446: aload #12
    //   448: invokevirtual isValid : ()V
    //   451: aload #12
    //   453: areturn
    //   454: new java/lang/IllegalArgumentException
    //   457: dup
    //   458: invokespecial <init> : ()V
    //   461: athrow
  }
  
  private void translateSingleFrameCheckLengthLimit(long paramLong) throws LimitExceededException {
    if (paramLong <= 2147483647L) {
      int i = this.maxFrameSize;
      if (paramLong <= i) {
        if (paramLong >= 0L)
          return; 
        log.trace("Limit underflow: Payloadsize is to little...");
        throw new LimitExceededException("Payloadsize is to little...");
      } 
      log.trace("Payload limit reached. Allowed: {} Current: {}", Integer.valueOf(i), Long.valueOf(paramLong));
      throw new LimitExceededException("Payload limit reached.", this.maxFrameSize);
    } 
    log.trace("Limit exedeed: Payloadsize is to big...");
    throw new LimitExceededException("Payloadsize is to big...");
  }
  
  private void translateSingleFrameCheckPacketSize(int paramInt1, int paramInt2) throws IncompleteException {
    if (paramInt1 >= paramInt2)
      return; 
    log.trace("Incomplete frame: maxpacketsize < realpacketsize");
    throw new IncompleteException(paramInt2);
  }
  
  private TranslatedPayloadMetaData translateSingleFramePayloadLength(ByteBuffer paramByteBuffer, Opcode paramOpcode, int paramInt1, int paramInt2, int paramInt3) throws InvalidFrameException, IncompleteException, LimitExceededException {
    if (paramOpcode != Opcode.PING && paramOpcode != Opcode.PONG && paramOpcode != Opcode.CLOSING) {
      if (paramInt1 == 126) {
        paramInt3 += 2;
        translateSingleFrameCheckPacketSize(paramInt2, paramInt3);
        byte[] arrayOfByte = new byte[3];
        arrayOfByte[1] = paramByteBuffer.get();
        arrayOfByte[2] = paramByteBuffer.get();
        paramInt1 = (new BigInteger(arrayOfByte)).intValue();
        paramInt2 = paramInt3;
      } else {
        paramInt3 += 8;
        translateSingleFrameCheckPacketSize(paramInt2, paramInt3);
        byte[] arrayOfByte = new byte[8];
        for (paramInt1 = 0; paramInt1 < 8; paramInt1++)
          arrayOfByte[paramInt1] = paramByteBuffer.get(); 
        long l = (new BigInteger(arrayOfByte)).longValue();
        translateSingleFrameCheckLengthLimit(l);
        paramInt1 = (int)l;
        paramInt2 = paramInt3;
      } 
      return new TranslatedPayloadMetaData(paramInt1, paramInt2);
    } 
    log.trace("Invalid frame: more than 125 octets");
    throw new InvalidFrameException("more than 125 octets");
  }
  
  public HandshakeState acceptHandshakeAsClient(ClientHandshake paramClientHandshake, ServerHandshake paramServerHandshake) throws InvalidHandshakeException {
    HandshakeState handshakeState1;
    if (!basicAccept((Handshakedata)paramServerHandshake)) {
      log.trace("acceptHandshakeAsClient - Missing/wrong upgrade or connection in handshake.");
      return HandshakeState.NOT_MATCHED;
    } 
    if (!paramClientHandshake.hasFieldValue("Sec-WebSocket-Key") || !paramServerHandshake.hasFieldValue("Sec-WebSocket-Accept")) {
      log.trace("acceptHandshakeAsClient - Missing Sec-WebSocket-Key or Sec-WebSocket-Accept");
      return HandshakeState.NOT_MATCHED;
    } 
    String str1 = paramServerHandshake.getFieldValue("Sec-WebSocket-Accept");
    if (!generateFinalKey(paramClientHandshake.getFieldValue("Sec-WebSocket-Key")).equals(str1)) {
      log.trace("acceptHandshakeAsClient - Wrong key for Sec-WebSocket-Key.");
      return HandshakeState.NOT_MATCHED;
    } 
    HandshakeState handshakeState2 = HandshakeState.NOT_MATCHED;
    String str2 = paramServerHandshake.getFieldValue("Sec-WebSocket-Extensions");
    Iterator<IExtension> iterator = this.knownExtensions.iterator();
    while (true) {
      handshakeState1 = handshakeState2;
      if (iterator.hasNext()) {
        IExtension iExtension = iterator.next();
        if (iExtension.acceptProvidedExtensionAsClient(str2)) {
          this.extension = iExtension;
          handshakeState1 = HandshakeState.MATCHED;
          log.trace("acceptHandshakeAsClient - Matching extension found: {}", this.extension);
          break;
        } 
        continue;
      } 
      break;
    } 
    if (containsRequestedProtocol(paramServerHandshake.getFieldValue("Sec-WebSocket-Protocol")) == HandshakeState.MATCHED && handshakeState1 == HandshakeState.MATCHED)
      return HandshakeState.MATCHED; 
    log.trace("acceptHandshakeAsClient - No matching extension or protocol found.");
    return HandshakeState.NOT_MATCHED;
  }
  
  public HandshakeState acceptHandshakeAsServer(ClientHandshake paramClientHandshake) throws InvalidHandshakeException {
    HandshakeState handshakeState1;
    if (readVersion((Handshakedata)paramClientHandshake) != 13) {
      log.trace("acceptHandshakeAsServer - Wrong websocket version.");
      return HandshakeState.NOT_MATCHED;
    } 
    HandshakeState handshakeState2 = HandshakeState.NOT_MATCHED;
    String str = paramClientHandshake.getFieldValue("Sec-WebSocket-Extensions");
    Iterator<IExtension> iterator = this.knownExtensions.iterator();
    while (true) {
      handshakeState1 = handshakeState2;
      if (iterator.hasNext()) {
        IExtension iExtension = iterator.next();
        if (iExtension.acceptProvidedExtensionAsServer(str)) {
          this.extension = iExtension;
          handshakeState1 = HandshakeState.MATCHED;
          log.trace("acceptHandshakeAsServer - Matching extension found: {}", this.extension);
          break;
        } 
        continue;
      } 
      break;
    } 
    if (containsRequestedProtocol(paramClientHandshake.getFieldValue("Sec-WebSocket-Protocol")) == HandshakeState.MATCHED && handshakeState1 == HandshakeState.MATCHED)
      return HandshakeState.MATCHED; 
    log.trace("acceptHandshakeAsServer - No matching extension or protocol found.");
    return HandshakeState.NOT_MATCHED;
  }
  
  public Draft copyInstance() {
    ArrayList<IExtension> arrayList = new ArrayList();
    Iterator<IExtension> iterator = getKnownExtensions().iterator();
    while (iterator.hasNext())
      arrayList.add(((IExtension)iterator.next()).copyInstance()); 
    ArrayList<IProtocol> arrayList1 = new ArrayList();
    Iterator<IProtocol> iterator1 = getKnownProtocols().iterator();
    while (iterator1.hasNext())
      arrayList1.add(((IProtocol)iterator1.next()).copyInstance()); 
    return new Draft_6455(arrayList, arrayList1, this.maxFrameSize);
  }
  
  public ByteBuffer createBinaryFrame(Framedata paramFramedata) {
    getExtension().encodeFrame(paramFramedata);
    if (log.isTraceEnabled()) {
      String str;
      Logger logger = log;
      int i = paramFramedata.getPayloadData().remaining();
      if (paramFramedata.getPayloadData().remaining() > 1000) {
        str = "too big to display";
      } else {
        str = new String(paramFramedata.getPayloadData().array());
      } 
      logger.trace("afterEnconding({}): {}", Integer.valueOf(i), str);
    } 
    return createByteBufferFromFramedata(paramFramedata);
  }
  
  public List<Framedata> createFrames(String paramString, boolean paramBoolean) {
    TextFrame textFrame = new TextFrame();
    textFrame.setPayload(ByteBuffer.wrap(Charsetfunctions.utf8Bytes(paramString)));
    textFrame.setTransferemasked(paramBoolean);
    try {
      textFrame.isValid();
      return (List)Collections.singletonList(textFrame);
    } catch (InvalidDataException invalidDataException) {
      throw new NotSendableException(invalidDataException);
    } 
  }
  
  public List<Framedata> createFrames(ByteBuffer paramByteBuffer, boolean paramBoolean) {
    BinaryFrame binaryFrame = new BinaryFrame();
    binaryFrame.setPayload(paramByteBuffer);
    binaryFrame.setTransferemasked(paramBoolean);
    try {
      binaryFrame.isValid();
      return (List)Collections.singletonList(binaryFrame);
    } catch (InvalidDataException invalidDataException) {
      throw new NotSendableException(invalidDataException);
    } 
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject != null) {
      if (getClass() != paramObject.getClass())
        return false; 
      paramObject = paramObject;
      if (this.maxFrameSize != paramObject.getMaxFrameSize())
        return false; 
      IExtension iExtension = this.extension;
      if (iExtension != null) {
        if (!iExtension.equals(paramObject.getExtension()))
          return false; 
      } else if (paramObject.getExtension() != null) {
        return false;
      } 
      IProtocol iProtocol = this.protocol;
      paramObject = paramObject.getProtocol();
      return (iProtocol != null) ? iProtocol.equals(paramObject) : ((paramObject == null));
    } 
    return false;
  }
  
  public CloseHandshakeType getCloseHandshakeType() {
    return CloseHandshakeType.TWOWAY;
  }
  
  public IExtension getExtension() {
    return this.extension;
  }
  
  public List<IExtension> getKnownExtensions() {
    return this.knownExtensions;
  }
  
  public List<IProtocol> getKnownProtocols() {
    return this.knownProtocols;
  }
  
  public int getMaxFrameSize() {
    return this.maxFrameSize;
  }
  
  public IProtocol getProtocol() {
    return this.protocol;
  }
  
  public int hashCode() {
    byte b;
    IExtension iExtension = this.extension;
    int i = 0;
    if (iExtension != null) {
      b = iExtension.hashCode();
    } else {
      b = 0;
    } 
    IProtocol iProtocol = this.protocol;
    if (iProtocol != null)
      i = iProtocol.hashCode(); 
    int j = this.maxFrameSize;
    return (b * 31 + i) * 31 + (j ^ j >>> 32);
  }
  
  public ClientHandshakeBuilder postProcessHandshakeRequestAsClient(ClientHandshakeBuilder paramClientHandshakeBuilder) {
    paramClientHandshakeBuilder.put("Upgrade", "websocket");
    paramClientHandshakeBuilder.put("Connection", "Upgrade");
    byte[] arrayOfByte = new byte[16];
    this.reuseableRandom.nextBytes(arrayOfByte);
    paramClientHandshakeBuilder.put("Sec-WebSocket-Key", Base64.encodeBytes(arrayOfByte));
    paramClientHandshakeBuilder.put("Sec-WebSocket-Version", "13");
    StringBuilder stringBuilder = new StringBuilder();
    for (IExtension iExtension : this.knownExtensions) {
      if (iExtension.getProvidedExtensionAsClient() != null && iExtension.getProvidedExtensionAsClient().length() != 0) {
        if (stringBuilder.length() > 0)
          stringBuilder.append(", "); 
        stringBuilder.append(iExtension.getProvidedExtensionAsClient());
      } 
    } 
    if (stringBuilder.length() != 0)
      paramClientHandshakeBuilder.put("Sec-WebSocket-Extensions", stringBuilder.toString()); 
    stringBuilder = new StringBuilder();
    for (IProtocol iProtocol : this.knownProtocols) {
      if (iProtocol.getProvidedProtocol().length() != 0) {
        if (stringBuilder.length() > 0)
          stringBuilder.append(", "); 
        stringBuilder.append(iProtocol.getProvidedProtocol());
      } 
    } 
    if (stringBuilder.length() != 0)
      paramClientHandshakeBuilder.put("Sec-WebSocket-Protocol", stringBuilder.toString()); 
    return paramClientHandshakeBuilder;
  }
  
  public HandshakeBuilder postProcessHandshakeResponseAsServer(ClientHandshake paramClientHandshake, ServerHandshakeBuilder paramServerHandshakeBuilder) throws InvalidHandshakeException {
    paramServerHandshakeBuilder.put("Upgrade", "websocket");
    paramServerHandshakeBuilder.put("Connection", paramClientHandshake.getFieldValue("Connection"));
    String str = paramClientHandshake.getFieldValue("Sec-WebSocket-Key");
    if (str != null) {
      paramServerHandshakeBuilder.put("Sec-WebSocket-Accept", generateFinalKey(str));
      if (getExtension().getProvidedExtensionAsServer().length() != 0)
        paramServerHandshakeBuilder.put("Sec-WebSocket-Extensions", getExtension().getProvidedExtensionAsServer()); 
      if (getProtocol() != null && getProtocol().getProvidedProtocol().length() != 0)
        paramServerHandshakeBuilder.put("Sec-WebSocket-Protocol", getProtocol().getProvidedProtocol()); 
      paramServerHandshakeBuilder.setHttpStatusMessage("Web Socket Protocol Handshake");
      paramServerHandshakeBuilder.put("Server", "TooTallNate Java-WebSocket");
      paramServerHandshakeBuilder.put("Date", getServerTime());
      return (HandshakeBuilder)paramServerHandshakeBuilder;
    } 
    throw new InvalidHandshakeException("missing Sec-WebSocket-Key");
  }
  
  public void processFrame(WebSocketImpl paramWebSocketImpl, Framedata paramFramedata) throws InvalidDataException {
    Opcode opcode = paramFramedata.getOpcode();
    if (opcode == Opcode.CLOSING) {
      processFrameClosing(paramWebSocketImpl, paramFramedata);
      return;
    } 
    if (opcode == Opcode.PING) {
      paramWebSocketImpl.getWebSocketListener().onWebsocketPing((WebSocket)paramWebSocketImpl, paramFramedata);
      return;
    } 
    if (opcode == Opcode.PONG) {
      paramWebSocketImpl.updateLastPong();
      paramWebSocketImpl.getWebSocketListener().onWebsocketPong((WebSocket)paramWebSocketImpl, paramFramedata);
      return;
    } 
    if (!paramFramedata.isFin() || opcode == Opcode.CONTINUOUS) {
      processFrameContinuousAndNonFin(paramWebSocketImpl, paramFramedata, opcode);
      return;
    } 
    if (this.currentContinuousFrame == null) {
      if (opcode == Opcode.TEXT) {
        processFrameText(paramWebSocketImpl, paramFramedata);
        return;
      } 
      if (opcode == Opcode.BINARY) {
        processFrameBinary(paramWebSocketImpl, paramFramedata);
        return;
      } 
      log.error("non control or continious frame expected");
      throw new InvalidDataException(1002, "non control or continious frame expected");
    } 
    log.error("Protocol error: Continuous frame sequence not completed.");
    throw new InvalidDataException(1002, "Continuous frame sequence not completed.");
  }
  
  public void reset() {
    this.incompleteframe = null;
    IExtension iExtension = this.extension;
    if (iExtension != null)
      iExtension.reset(); 
    this.extension = (IExtension)new DefaultExtension();
    this.protocol = null;
  }
  
  public String toString() {
    String str2 = super.toString();
    String str1 = str2;
    if (getExtension() != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str2);
      stringBuilder1.append(" extension: ");
      stringBuilder1.append(getExtension().toString());
      str1 = stringBuilder1.toString();
    } 
    str2 = str1;
    if (getProtocol() != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str1);
      stringBuilder1.append(" protocol: ");
      stringBuilder1.append(getProtocol().toString());
      str2 = stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str2);
    stringBuilder.append(" max frame size: ");
    stringBuilder.append(this.maxFrameSize);
    return stringBuilder.toString();
  }
  
  public List<Framedata> translateFrame(ByteBuffer paramByteBuffer) throws InvalidDataException {
    ByteBuffer byteBuffer;
    while (true) {
      LinkedList<Framedata> linkedList = new LinkedList();
      if (this.incompleteframe != null)
        try {
          paramByteBuffer.mark();
          int i = paramByteBuffer.remaining();
          int j = this.incompleteframe.remaining();
          if (j > i) {
            this.incompleteframe.put(paramByteBuffer.array(), paramByteBuffer.position(), i);
            paramByteBuffer.position(paramByteBuffer.position() + i);
            return Collections.emptyList();
          } 
          this.incompleteframe.put(paramByteBuffer.array(), paramByteBuffer.position(), j);
          paramByteBuffer.position(paramByteBuffer.position() + j);
          linkedList.add(translateSingleFrame((ByteBuffer)this.incompleteframe.duplicate().position(0)));
          this.incompleteframe = null;
          break;
        } catch (IncompleteException incompleteException) {
          byteBuffer = ByteBuffer.allocate(checkAlloc(incompleteException.getPreferredSize()));
          this.incompleteframe.rewind();
          byteBuffer.put(this.incompleteframe);
          this.incompleteframe = byteBuffer;
          continue;
        }  
      break;
    } 
    while (paramByteBuffer.hasRemaining()) {
      paramByteBuffer.mark();
      try {
        byteBuffer.add(translateSingleFrame(paramByteBuffer));
      } catch (IncompleteException incompleteException) {
        paramByteBuffer.reset();
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(checkAlloc(incompleteException.getPreferredSize()));
        this.incompleteframe = byteBuffer1;
        byteBuffer1.put(paramByteBuffer);
        break;
      } 
    } 
    return (List<Framedata>)byteBuffer;
  }
  
  private class TranslatedPayloadMetaData {
    private int payloadLength;
    
    private int realPackageSize;
    
    TranslatedPayloadMetaData(int param1Int1, int param1Int2) {
      this.payloadLength = param1Int1;
      this.realPackageSize = param1Int2;
    }
    
    private int getPayloadLength() {
      return this.payloadLength;
    }
    
    private int getRealPackageSize() {
      return this.realPackageSize;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\drafts\Draft_6455.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */