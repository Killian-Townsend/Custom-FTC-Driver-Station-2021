package org.java_websocket.exceptions;

public class IncompleteHandshakeException extends RuntimeException {
  private static final long serialVersionUID = 7906596804233893092L;
  
  private final int preferredSize = 0;
  
  public IncompleteHandshakeException() {}
  
  public IncompleteHandshakeException(int paramInt) {}
  
  public int getPreferredSize() {
    return this.preferredSize;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\exceptions\IncompleteHandshakeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */