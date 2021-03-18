package org.java_websocket.exceptions;

public class LimitExceededException extends InvalidDataException {
  private static final long serialVersionUID = 6908339749836826785L;
  
  private final int limit;
  
  public LimitExceededException() {
    this(2147483647);
  }
  
  public LimitExceededException(int paramInt) {
    super(1009);
    this.limit = paramInt;
  }
  
  public LimitExceededException(String paramString) {
    this(paramString, 2147483647);
  }
  
  public LimitExceededException(String paramString, int paramInt) {
    super(1009, paramString);
    this.limit = paramInt;
  }
  
  public int getLimit() {
    return this.limit;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\exceptions\LimitExceededException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */