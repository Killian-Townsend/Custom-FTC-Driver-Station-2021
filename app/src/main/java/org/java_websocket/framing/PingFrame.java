package org.java_websocket.framing;

import org.java_websocket.enums.Opcode;

public class PingFrame extends ControlFrame {
  public PingFrame() {
    super(Opcode.PING);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\framing\PingFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */