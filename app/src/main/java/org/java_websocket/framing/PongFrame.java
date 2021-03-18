package org.java_websocket.framing;

import org.java_websocket.enums.Opcode;

public class PongFrame extends ControlFrame {
  public PongFrame() {
    super(Opcode.PONG);
  }
  
  public PongFrame(PingFrame paramPingFrame) {
    super(Opcode.PONG);
    setPayload(paramPingFrame.getPayloadData());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\framing\PongFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */