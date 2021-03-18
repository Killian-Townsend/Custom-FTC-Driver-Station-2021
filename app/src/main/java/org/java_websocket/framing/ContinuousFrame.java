package org.java_websocket.framing;

import org.java_websocket.enums.Opcode;

public class ContinuousFrame extends DataFrame {
  public ContinuousFrame() {
    super(Opcode.CONTINUOUS);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\framing\ContinuousFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */