package org.java_websocket.enums;

public enum HandshakeState {
  MATCHED, NOT_MATCHED;
  
  static {
    HandshakeState handshakeState = new HandshakeState("NOT_MATCHED", 1);
    NOT_MATCHED = handshakeState;
    $VALUES = new HandshakeState[] { MATCHED, handshakeState };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\enums\HandshakeState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */