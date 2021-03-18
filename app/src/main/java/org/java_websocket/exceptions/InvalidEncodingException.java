package org.java_websocket.exceptions;

import java.io.UnsupportedEncodingException;

public class InvalidEncodingException extends RuntimeException {
  private final UnsupportedEncodingException encodingException;
  
  public InvalidEncodingException(UnsupportedEncodingException paramUnsupportedEncodingException) {
    if (paramUnsupportedEncodingException != null) {
      this.encodingException = paramUnsupportedEncodingException;
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public UnsupportedEncodingException getEncodingException() {
    return this.encodingException;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\exceptions\InvalidEncodingException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */