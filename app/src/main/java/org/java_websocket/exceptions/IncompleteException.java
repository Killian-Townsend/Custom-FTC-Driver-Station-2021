package org.java_websocket.exceptions;

public class IncompleteException extends Exception {
  private static final long serialVersionUID = 7330519489840500997L;
  
  private final int preferredSize;
  
  public IncompleteException(int paramInt) {
    this.preferredSize = paramInt;
  }
  
  public int getPreferredSize() {
    return this.preferredSize;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\exceptions\IncompleteException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */