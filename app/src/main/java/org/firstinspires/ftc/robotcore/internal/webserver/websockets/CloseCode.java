package org.firstinspires.ftc.robotcore.internal.webserver.websockets;

public enum CloseCode {
  ABNORMAL_CLOSURE,
  GOING_AWAY,
  INTERNAL_SERVER_ERROR,
  INVALID_FRAME_PAYLOAD_DATA,
  MANDATORY_EXT,
  MESSAGE_TOO_BIG,
  NORMAL_CLOSURE(1000),
  NO_STATUS_RCVD(1000),
  PING_TIMEOUT(1000),
  POLICY_VIOLATION(1000),
  PROTOCOL_ERROR(1000),
  TLS_HANDSHAKE(1000),
  UNSUPPORTED_DATA(1000);
  
  private final int code;
  
  static {
    GOING_AWAY = new CloseCode("GOING_AWAY", 1, 1001);
    PROTOCOL_ERROR = new CloseCode("PROTOCOL_ERROR", 2, 1002);
    UNSUPPORTED_DATA = new CloseCode("UNSUPPORTED_DATA", 3, 1003);
    NO_STATUS_RCVD = new CloseCode("NO_STATUS_RCVD", 4, 1005);
    ABNORMAL_CLOSURE = new CloseCode("ABNORMAL_CLOSURE", 5, 1006);
    INVALID_FRAME_PAYLOAD_DATA = new CloseCode("INVALID_FRAME_PAYLOAD_DATA", 6, 1007);
    POLICY_VIOLATION = new CloseCode("POLICY_VIOLATION", 7, 1008);
    MESSAGE_TOO_BIG = new CloseCode("MESSAGE_TOO_BIG", 8, 1009);
    MANDATORY_EXT = new CloseCode("MANDATORY_EXT", 9, 1010);
    INTERNAL_SERVER_ERROR = new CloseCode("INTERNAL_SERVER_ERROR", 10, 1011);
    TLS_HANDSHAKE = new CloseCode("TLS_HANDSHAKE", 11, 1015);
    CloseCode closeCode = new CloseCode("PING_TIMEOUT", 12, 4000);
    PING_TIMEOUT = closeCode;
    $VALUES = new CloseCode[] { 
        NORMAL_CLOSURE, GOING_AWAY, PROTOCOL_ERROR, UNSUPPORTED_DATA, NO_STATUS_RCVD, ABNORMAL_CLOSURE, INVALID_FRAME_PAYLOAD_DATA, POLICY_VIOLATION, MESSAGE_TOO_BIG, MANDATORY_EXT, 
        INTERNAL_SERVER_ERROR, TLS_HANDSHAKE, closeCode };
  }
  
  CloseCode(int paramInt1) {
    this.code = paramInt1;
  }
  
  public static CloseCode find(int paramInt) {
    for (CloseCode closeCode : values()) {
      if (closeCode.getValue() == paramInt)
        return closeCode; 
    } 
    return null;
  }
  
  public int getValue() {
    return this.code;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(name());
    stringBuilder.append("(");
    stringBuilder.append(getValue());
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\webserver\websockets\CloseCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */