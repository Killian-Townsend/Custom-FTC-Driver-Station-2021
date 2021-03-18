package org.java_websocket.enums;

public enum ReadyState {
  CLOSED, CLOSING, NOT_YET_CONNECTED, OPEN;
  
  static {
    CLOSING = new ReadyState("CLOSING", 2);
    ReadyState readyState = new ReadyState("CLOSED", 3);
    CLOSED = readyState;
    $VALUES = new ReadyState[] { NOT_YET_CONNECTED, OPEN, CLOSING, readyState };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\enums\ReadyState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */