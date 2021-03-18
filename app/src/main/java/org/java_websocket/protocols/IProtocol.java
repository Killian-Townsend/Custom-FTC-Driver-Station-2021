package org.java_websocket.protocols;

public interface IProtocol {
  boolean acceptProvidedProtocol(String paramString);
  
  IProtocol copyInstance();
  
  String getProvidedProtocol();
  
  String toString();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\protocols\IProtocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */