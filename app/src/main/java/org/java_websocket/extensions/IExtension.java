package org.java_websocket.extensions;

import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.Framedata;

public interface IExtension {
  boolean acceptProvidedExtensionAsClient(String paramString);
  
  boolean acceptProvidedExtensionAsServer(String paramString);
  
  IExtension copyInstance();
  
  void decodeFrame(Framedata paramFramedata) throws InvalidDataException;
  
  void encodeFrame(Framedata paramFramedata);
  
  String getProvidedExtensionAsClient();
  
  String getProvidedExtensionAsServer();
  
  void isFrameValid(Framedata paramFramedata) throws InvalidDataException;
  
  void reset();
  
  String toString();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\extensions\IExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */