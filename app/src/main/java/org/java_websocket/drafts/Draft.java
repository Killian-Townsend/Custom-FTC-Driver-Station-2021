package org.java_websocket.drafts;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.enums.CloseHandshakeType;
import org.java_websocket.enums.HandshakeState;
import org.java_websocket.enums.Opcode;
import org.java_websocket.enums.Role;
import org.java_websocket.exceptions.IncompleteHandshakeException;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.framing.BinaryFrame;
import org.java_websocket.framing.ContinuousFrame;
import org.java_websocket.framing.DataFrame;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.TextFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ClientHandshakeBuilder;
import org.java_websocket.handshake.HandshakeBuilder;
import org.java_websocket.handshake.HandshakeImpl1Client;
import org.java_websocket.handshake.HandshakeImpl1Server;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.util.Charsetfunctions;

public abstract class Draft {
  protected Opcode continuousFrameType = null;
  
  protected Role role = null;
  
  public static ByteBuffer readLine(ByteBuffer paramByteBuffer) {
    ByteBuffer byteBuffer = ByteBuffer.allocate(paramByteBuffer.remaining());
    for (byte b = 48; paramByteBuffer.hasRemaining(); b = b1) {
      byte b1 = paramByteBuffer.get();
      byteBuffer.put(b1);
      if (b == 13 && b1 == 10) {
        byteBuffer.limit(byteBuffer.position() - 2);
        byteBuffer.position(0);
        return byteBuffer;
      } 
    } 
    paramByteBuffer.position(paramByteBuffer.position() - byteBuffer.position());
    return null;
  }
  
  public static String readStringLine(ByteBuffer paramByteBuffer) {
    paramByteBuffer = readLine(paramByteBuffer);
    return (paramByteBuffer == null) ? null : Charsetfunctions.stringAscii(paramByteBuffer.array(), 0, paramByteBuffer.limit());
  }
  
  public static HandshakeBuilder translateHandshakeHttp(ByteBuffer paramByteBuffer, Role paramRole) throws InvalidHandshakeException {
    String str = readStringLine(paramByteBuffer);
    if (str != null) {
      String[] arrayOfString = str.split(" ", 3);
      if (arrayOfString.length == 3) {
        HandshakeBuilder handshakeBuilder;
        String str1;
        if (paramRole == Role.CLIENT) {
          handshakeBuilder = translateHandshakeHttpClient(arrayOfString, str);
        } else {
          handshakeBuilder = translateHandshakeHttpServer(arrayOfString, str);
        } 
        str = readStringLine(paramByteBuffer);
        while (str != null && str.length() > 0) {
          String[] arrayOfString1 = str.split(":", 2);
          if (arrayOfString1.length == 2) {
            if (handshakeBuilder.hasFieldValue(arrayOfString1[0])) {
              String str2 = arrayOfString1[0];
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(handshakeBuilder.getFieldValue(arrayOfString1[0]));
              stringBuilder.append("; ");
              stringBuilder.append(arrayOfString1[1].replaceFirst("^ +", ""));
              handshakeBuilder.put(str2, stringBuilder.toString());
            } else {
              handshakeBuilder.put(arrayOfString1[0], arrayOfString1[1].replaceFirst("^ +", ""));
            } 
            str1 = readStringLine(paramByteBuffer);
            continue;
          } 
          throw new InvalidHandshakeException("not an http header");
        } 
        if (str1 != null)
          return handshakeBuilder; 
        throw new IncompleteHandshakeException();
      } 
      throw new InvalidHandshakeException();
    } 
    throw new IncompleteHandshakeException(paramByteBuffer.capacity() + 128);
  }
  
  private static HandshakeBuilder translateHandshakeHttpClient(String[] paramArrayOfString, String paramString) throws InvalidHandshakeException {
    HandshakeImpl1Server handshakeImpl1Server;
    if ("101".equals(paramArrayOfString[1])) {
      if ("HTTP/1.1".equalsIgnoreCase(paramArrayOfString[0])) {
        handshakeImpl1Server = new HandshakeImpl1Server();
        ServerHandshakeBuilder serverHandshakeBuilder = (ServerHandshakeBuilder)handshakeImpl1Server;
        serverHandshakeBuilder.setHttpStatus(Short.parseShort(paramArrayOfString[1]));
        serverHandshakeBuilder.setHttpStatusMessage(paramArrayOfString[2]);
        return (HandshakeBuilder)handshakeImpl1Server;
      } 
      throw new InvalidHandshakeException(String.format("Invalid status line received: %s Status line: %s", new Object[] { paramArrayOfString[0], handshakeImpl1Server }));
    } 
    throw new InvalidHandshakeException(String.format("Invalid status code received: %s Status line: %s", new Object[] { paramArrayOfString[1], handshakeImpl1Server }));
  }
  
  private static HandshakeBuilder translateHandshakeHttpServer(String[] paramArrayOfString, String paramString) throws InvalidHandshakeException {
    HandshakeImpl1Client handshakeImpl1Client;
    if ("GET".equalsIgnoreCase(paramArrayOfString[0])) {
      if ("HTTP/1.1".equalsIgnoreCase(paramArrayOfString[2])) {
        handshakeImpl1Client = new HandshakeImpl1Client();
        handshakeImpl1Client.setResourceDescriptor(paramArrayOfString[1]);
        return (HandshakeBuilder)handshakeImpl1Client;
      } 
      throw new InvalidHandshakeException(String.format("Invalid status line received: %s Status line: %s", new Object[] { paramArrayOfString[2], handshakeImpl1Client }));
    } 
    throw new InvalidHandshakeException(String.format("Invalid request method received: %s Status line: %s", new Object[] { paramArrayOfString[0], handshakeImpl1Client }));
  }
  
  public abstract HandshakeState acceptHandshakeAsClient(ClientHandshake paramClientHandshake, ServerHandshake paramServerHandshake) throws InvalidHandshakeException;
  
  public abstract HandshakeState acceptHandshakeAsServer(ClientHandshake paramClientHandshake) throws InvalidHandshakeException;
  
  protected boolean basicAccept(Handshakedata paramHandshakedata) {
    return (paramHandshakedata.getFieldValue("Upgrade").equalsIgnoreCase("websocket") && paramHandshakedata.getFieldValue("Connection").toLowerCase(Locale.ENGLISH).contains("upgrade"));
  }
  
  public int checkAlloc(int paramInt) throws InvalidDataException {
    if (paramInt >= 0)
      return paramInt; 
    throw new InvalidDataException(1002, "Negative count");
  }
  
  public List<Framedata> continuousFrame(Opcode paramOpcode, ByteBuffer paramByteBuffer, boolean paramBoolean) {
    if (paramOpcode == Opcode.BINARY || paramOpcode == Opcode.TEXT) {
      DataFrame dataFrame;
      if (this.continuousFrameType != null) {
        dataFrame = (DataFrame)new ContinuousFrame();
      } else {
        this.continuousFrameType = paramOpcode;
        if (paramOpcode == Opcode.BINARY) {
          dataFrame = (DataFrame)new BinaryFrame();
        } else if (paramOpcode == Opcode.TEXT) {
          dataFrame = (DataFrame)new TextFrame();
        } else {
          dataFrame = null;
        } 
      } 
      dataFrame.setPayload(paramByteBuffer);
      dataFrame.setFin(paramBoolean);
      try {
        dataFrame.isValid();
        if (paramBoolean) {
          this.continuousFrameType = null;
        } else {
          this.continuousFrameType = paramOpcode;
        } 
        return (List)Collections.singletonList(dataFrame);
      } catch (InvalidDataException invalidDataException) {
        throw new IllegalArgumentException(invalidDataException);
      } 
    } 
    throw new IllegalArgumentException("Only Opcode.BINARY or  Opcode.TEXT are allowed");
  }
  
  public abstract Draft copyInstance();
  
  public abstract ByteBuffer createBinaryFrame(Framedata paramFramedata);
  
  public abstract List<Framedata> createFrames(String paramString, boolean paramBoolean);
  
  public abstract List<Framedata> createFrames(ByteBuffer paramByteBuffer, boolean paramBoolean);
  
  public List<ByteBuffer> createHandshake(Handshakedata paramHandshakedata) {
    return createHandshake(paramHandshakedata, true);
  }
  
  @Deprecated
  public List<ByteBuffer> createHandshake(Handshakedata paramHandshakedata, Role paramRole) {
    return createHandshake(paramHandshakedata);
  }
  
  @Deprecated
  public List<ByteBuffer> createHandshake(Handshakedata paramHandshakedata, Role paramRole, boolean paramBoolean) {
    return createHandshake(paramHandshakedata, paramBoolean);
  }
  
  public List<ByteBuffer> createHandshake(Handshakedata paramHandshakedata, boolean paramBoolean) {
    int i;
    StringBuilder stringBuilder = new StringBuilder(100);
    if (paramHandshakedata instanceof ClientHandshake) {
      stringBuilder.append("GET ");
      stringBuilder.append(((ClientHandshake)paramHandshakedata).getResourceDescriptor());
      stringBuilder.append(" HTTP/1.1");
    } else if (paramHandshakedata instanceof ServerHandshake) {
      stringBuilder.append("HTTP/1.1 101 ");
      stringBuilder.append(((ServerHandshake)paramHandshakedata).getHttpStatusMessage());
    } else {
      throw new IllegalArgumentException("unknown role");
    } 
    stringBuilder.append("\r\n");
    Iterator<String> iterator = paramHandshakedata.iterateHttpFields();
    while (iterator.hasNext()) {
      String str1 = iterator.next();
      String str2 = paramHandshakedata.getFieldValue(str1);
      stringBuilder.append(str1);
      stringBuilder.append(": ");
      stringBuilder.append(str2);
      stringBuilder.append("\r\n");
    } 
    stringBuilder.append("\r\n");
    byte[] arrayOfByte = Charsetfunctions.asciiBytes(stringBuilder.toString());
    if (paramBoolean) {
      byte[] arrayOfByte1 = paramHandshakedata.getContent();
    } else {
      paramHandshakedata = null;
    } 
    if (paramHandshakedata == null) {
      i = 0;
    } else {
      i = paramHandshakedata.length;
    } 
    ByteBuffer byteBuffer = ByteBuffer.allocate(i + arrayOfByte.length);
    byteBuffer.put(arrayOfByte);
    if (paramHandshakedata != null)
      byteBuffer.put((byte[])paramHandshakedata); 
    byteBuffer.flip();
    return Collections.singletonList(byteBuffer);
  }
  
  public abstract CloseHandshakeType getCloseHandshakeType();
  
  public Role getRole() {
    return this.role;
  }
  
  public abstract ClientHandshakeBuilder postProcessHandshakeRequestAsClient(ClientHandshakeBuilder paramClientHandshakeBuilder) throws InvalidHandshakeException;
  
  public abstract HandshakeBuilder postProcessHandshakeResponseAsServer(ClientHandshake paramClientHandshake, ServerHandshakeBuilder paramServerHandshakeBuilder) throws InvalidHandshakeException;
  
  public abstract void processFrame(WebSocketImpl paramWebSocketImpl, Framedata paramFramedata) throws InvalidDataException;
  
  int readVersion(Handshakedata paramHandshakedata) {
    String str = paramHandshakedata.getFieldValue("Sec-WebSocket-Version");
    if (str.length() > 0)
      try {
        return (new Integer(str.trim())).intValue();
      } catch (NumberFormatException numberFormatException) {
        return -1;
      }  
    return -1;
  }
  
  public abstract void reset();
  
  public void setParseMode(Role paramRole) {
    this.role = paramRole;
  }
  
  public String toString() {
    return getClass().getSimpleName();
  }
  
  public abstract List<Framedata> translateFrame(ByteBuffer paramByteBuffer) throws InvalidDataException;
  
  public Handshakedata translateHandshake(ByteBuffer paramByteBuffer) throws InvalidHandshakeException {
    return (Handshakedata)translateHandshakeHttp(paramByteBuffer, this.role);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\drafts\Draft.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */