package org.java_websocket.handshake;

import java.util.Iterator;

public interface Handshakedata {
  byte[] getContent();
  
  String getFieldValue(String paramString);
  
  boolean hasFieldValue(String paramString);
  
  Iterator<String> iterateHttpFields();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\handshake\Handshakedata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */