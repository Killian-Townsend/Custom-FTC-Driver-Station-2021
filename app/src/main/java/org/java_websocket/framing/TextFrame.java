package org.java_websocket.framing;

import org.java_websocket.enums.Opcode;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.util.Charsetfunctions;

public class TextFrame extends DataFrame {
  public TextFrame() {
    super(Opcode.TEXT);
  }
  
  public void isValid() throws InvalidDataException {
    super.isValid();
    if (Charsetfunctions.isValidUTF8(getPayloadData()))
      return; 
    throw new InvalidDataException(1007, "Received text is no valid utf8 string!");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\framing\TextFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */