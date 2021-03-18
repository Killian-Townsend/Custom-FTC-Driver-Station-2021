package org.java_websocket.handshake;

public class HandshakeImpl1Client extends HandshakedataImpl1 implements ClientHandshakeBuilder {
  private String resourceDescriptor = "*";
  
  public String getResourceDescriptor() {
    return this.resourceDescriptor;
  }
  
  public void setResourceDescriptor(String paramString) {
    if (paramString != null) {
      this.resourceDescriptor = paramString;
      return;
    } 
    throw new IllegalArgumentException("http resource descriptor must not be null");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\handshake\HandshakeImpl1Client.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */