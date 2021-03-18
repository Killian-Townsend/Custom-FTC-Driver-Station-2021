package org.java_websocket.protocols;

import java.util.regex.Pattern;

public class Protocol implements IProtocol {
  private static final Pattern patternComma;
  
  private static final Pattern patternSpace = Pattern.compile(" ");
  
  private final String providedProtocol;
  
  static {
    patternComma = Pattern.compile(",");
  }
  
  public Protocol(String paramString) {
    if (paramString != null) {
      this.providedProtocol = paramString;
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public boolean acceptProvidedProtocol(String paramString) {
    paramString = patternSpace.matcher(paramString).replaceAll("");
    for (String str : patternComma.split(paramString)) {
      if (this.providedProtocol.equals(str))
        return true; 
    } 
    return false;
  }
  
  public IProtocol copyInstance() {
    return new Protocol(getProvidedProtocol());
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject == null || getClass() != paramObject.getClass())
      return false; 
    paramObject = paramObject;
    return this.providedProtocol.equals(((Protocol)paramObject).providedProtocol);
  }
  
  public String getProvidedProtocol() {
    return this.providedProtocol;
  }
  
  public int hashCode() {
    return this.providedProtocol.hashCode();
  }
  
  public String toString() {
    return getProvidedProtocol();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\protocols\Protocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */