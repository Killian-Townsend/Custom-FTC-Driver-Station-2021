package org.java_websocket.extensions;

import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidFrameException;
import org.java_websocket.framing.Framedata;

public class DefaultExtension implements IExtension {
  public boolean acceptProvidedExtensionAsClient(String paramString) {
    return true;
  }
  
  public boolean acceptProvidedExtensionAsServer(String paramString) {
    return true;
  }
  
  public IExtension copyInstance() {
    return new DefaultExtension();
  }
  
  public void decodeFrame(Framedata paramFramedata) throws InvalidDataException {}
  
  public void encodeFrame(Framedata paramFramedata) {}
  
  public boolean equals(Object paramObject) {
    return (this == paramObject || (paramObject != null && getClass() == paramObject.getClass()));
  }
  
  public String getProvidedExtensionAsClient() {
    return "";
  }
  
  public String getProvidedExtensionAsServer() {
    return "";
  }
  
  public int hashCode() {
    return getClass().hashCode();
  }
  
  public void isFrameValid(Framedata paramFramedata) throws InvalidDataException {
    if (!paramFramedata.isRSV1() && !paramFramedata.isRSV2() && !paramFramedata.isRSV3())
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("bad rsv RSV1: ");
    stringBuilder.append(paramFramedata.isRSV1());
    stringBuilder.append(" RSV2: ");
    stringBuilder.append(paramFramedata.isRSV2());
    stringBuilder.append(" RSV3: ");
    stringBuilder.append(paramFramedata.isRSV3());
    throw new InvalidFrameException(stringBuilder.toString());
  }
  
  public void reset() {}
  
  public String toString() {
    return getClass().getSimpleName();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\extensions\DefaultExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */