package org.java_websocket.framing;

import java.nio.ByteBuffer;
import org.java_websocket.enums.Opcode;

public interface Framedata {
  void append(Framedata paramFramedata);
  
  Opcode getOpcode();
  
  ByteBuffer getPayloadData();
  
  boolean getTransfereMasked();
  
  boolean isFin();
  
  boolean isRSV1();
  
  boolean isRSV2();
  
  boolean isRSV3();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\framing\Framedata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */