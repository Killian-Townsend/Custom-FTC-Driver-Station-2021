package org.java_websocket.framing;

import org.java_websocket.enums.Opcode;
import org.java_websocket.exceptions.InvalidDataException;

public abstract class DataFrame extends FramedataImpl1 {
  public DataFrame(Opcode paramOpcode) {
    super(paramOpcode);
  }
  
  public void isValid() throws InvalidDataException {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\framing\DataFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */