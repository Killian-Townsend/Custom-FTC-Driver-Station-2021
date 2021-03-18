package org.java_websocket.framing;

import org.java_websocket.enums.Opcode;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidFrameException;

public abstract class ControlFrame extends FramedataImpl1 {
  public ControlFrame(Opcode paramOpcode) {
    super(paramOpcode);
  }
  
  public void isValid() throws InvalidDataException {
    if (isFin()) {
      if (!isRSV1()) {
        if (!isRSV2()) {
          if (!isRSV3())
            return; 
          throw new InvalidFrameException("Control frame cant have rsv3==true set");
        } 
        throw new InvalidFrameException("Control frame cant have rsv2==true set");
      } 
      throw new InvalidFrameException("Control frame cant have rsv1==true set");
    } 
    throw new InvalidFrameException("Control frame cant have fin==false set");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\framing\ControlFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */