package org.java_websocket.framing;

import java.nio.ByteBuffer;
import org.java_websocket.enums.Opcode;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidFrameException;
import org.java_websocket.util.ByteBufferUtils;
import org.java_websocket.util.Charsetfunctions;

public class CloseFrame extends ControlFrame {
  public static final int ABNORMAL_CLOSE = 1006;
  
  public static final int BAD_GATEWAY = 1014;
  
  public static final int BUGGYCLOSE = -2;
  
  public static final int EXTENSION = 1010;
  
  public static final int FLASHPOLICY = -3;
  
  public static final int GOING_AWAY = 1001;
  
  public static final int NEVER_CONNECTED = -1;
  
  public static final int NOCODE = 1005;
  
  public static final int NORMAL = 1000;
  
  public static final int NO_UTF8 = 1007;
  
  public static final int POLICY_VALIDATION = 1008;
  
  public static final int PROTOCOL_ERROR = 1002;
  
  public static final int REFUSE = 1003;
  
  public static final int SERVICE_RESTART = 1012;
  
  public static final int TLS_ERROR = 1015;
  
  public static final int TOOBIG = 1009;
  
  public static final int TRY_AGAIN_LATER = 1013;
  
  public static final int UNEXPECTED_CONDITION = 1011;
  
  private int code;
  
  private String reason;
  
  public CloseFrame() {
    super(Opcode.CLOSING);
    setReason("");
    setCode(1000);
  }
  
  private void updatePayload() {
    byte[] arrayOfByte = Charsetfunctions.utf8Bytes(this.reason);
    ByteBuffer byteBuffer1 = ByteBuffer.allocate(4);
    byteBuffer1.putInt(this.code);
    byteBuffer1.position(2);
    ByteBuffer byteBuffer2 = ByteBuffer.allocate(arrayOfByte.length + 2);
    byteBuffer2.put(byteBuffer1);
    byteBuffer2.put(arrayOfByte);
    byteBuffer2.rewind();
    super.setPayload(byteBuffer2);
  }
  
  private void validateUtf8(ByteBuffer paramByteBuffer, int paramInt) throws InvalidDataException {
    try {
      paramByteBuffer.position(paramByteBuffer.position() + 2);
      this.reason = Charsetfunctions.stringUtf8(paramByteBuffer);
      return;
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new InvalidDataException(1007);
    } finally {
      paramByteBuffer.position(paramInt);
    } 
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject != null) {
      if (getClass() != paramObject.getClass())
        return false; 
      if (!super.equals(paramObject))
        return false; 
      CloseFrame closeFrame = (CloseFrame)paramObject;
      if (this.code != closeFrame.code)
        return false; 
      paramObject = this.reason;
      String str = closeFrame.reason;
      return (paramObject != null) ? paramObject.equals(str) : ((str == null));
    } 
    return false;
  }
  
  public int getCloseCode() {
    return this.code;
  }
  
  public String getMessage() {
    return this.reason;
  }
  
  public ByteBuffer getPayloadData() {
    return (this.code == 1005) ? ByteBufferUtils.getEmptyByteBuffer() : super.getPayloadData();
  }
  
  public int hashCode() {
    byte b;
    int i = super.hashCode();
    int j = this.code;
    String str = this.reason;
    if (str != null) {
      b = str.hashCode();
    } else {
      b = 0;
    } 
    return (i * 31 + j) * 31 + b;
  }
  
  public void isValid() throws InvalidDataException {
    super.isValid();
    if (this.code != 1007 || !this.reason.isEmpty()) {
      if (this.code != 1005 || this.reason.length() <= 0) {
        int i = this.code;
        if (i <= 1015 || i >= 3000) {
          i = this.code;
          if (i != 1006 && i != 1015 && i != 1005 && i <= 4999 && i >= 1000 && i != 1004)
            return; 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("closecode must not be sent over the wire: ");
          stringBuilder.append(this.code);
          throw new InvalidFrameException(stringBuilder.toString());
        } 
        throw new InvalidDataException(1002, "Trying to send an illegal close code!");
      } 
      throw new InvalidDataException(1002, "A close frame must have a closecode if it has a reason");
    } 
    throw new InvalidDataException(1007, "Received text is no valid utf8 string!");
  }
  
  public void setCode(int paramInt) {
    this.code = paramInt;
    if (paramInt == 1015) {
      this.code = 1005;
      this.reason = "";
    } 
    updatePayload();
  }
  
  public void setPayload(ByteBuffer paramByteBuffer) {
    this.code = 1005;
    this.reason = "";
    paramByteBuffer.mark();
    if (paramByteBuffer.remaining() == 0) {
      this.code = 1000;
      return;
    } 
    if (paramByteBuffer.remaining() == 1) {
      this.code = 1002;
      return;
    } 
    if (paramByteBuffer.remaining() >= 2) {
      ByteBuffer byteBuffer = ByteBuffer.allocate(4);
      byteBuffer.position(2);
      byteBuffer.putShort(paramByteBuffer.getShort());
      byteBuffer.position(0);
      this.code = byteBuffer.getInt();
    } 
    paramByteBuffer.reset();
    try {
      validateUtf8(paramByteBuffer, paramByteBuffer.position());
      return;
    } catch (InvalidDataException invalidDataException) {
      this.code = 1007;
      this.reason = null;
      return;
    } 
  }
  
  public void setReason(String paramString) {
    String str = paramString;
    if (paramString == null)
      str = ""; 
    this.reason = str;
    updatePayload();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(super.toString());
    stringBuilder.append("code: ");
    stringBuilder.append(this.code);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\framing\CloseFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */