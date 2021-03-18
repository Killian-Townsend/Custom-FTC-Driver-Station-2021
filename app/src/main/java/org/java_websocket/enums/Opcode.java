package org.java_websocket.enums;

public enum Opcode {
  BINARY, CLOSING, CONTINUOUS, PING, PONG, TEXT;
  
  static {
    BINARY = new Opcode("BINARY", 2);
    PING = new Opcode("PING", 3);
    PONG = new Opcode("PONG", 4);
    Opcode opcode = new Opcode("CLOSING", 5);
    CLOSING = opcode;
    $VALUES = new Opcode[] { CONTINUOUS, TEXT, BINARY, PING, PONG, opcode };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\enums\Opcode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */