package org.java_websocket.enums;

public enum CloseHandshakeType {
  NONE, ONEWAY, TWOWAY;
  
  static {
    CloseHandshakeType closeHandshakeType = new CloseHandshakeType("TWOWAY", 2);
    TWOWAY = closeHandshakeType;
    $VALUES = new CloseHandshakeType[] { NONE, ONEWAY, closeHandshakeType };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\enums\CloseHandshakeType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */