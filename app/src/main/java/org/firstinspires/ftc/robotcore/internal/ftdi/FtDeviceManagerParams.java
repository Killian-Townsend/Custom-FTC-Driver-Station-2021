package org.firstinspires.ftc.robotcore.internal.ftdi;

public class FtDeviceManagerParams {
  public static final String TAG = "FtDeviceManagerParams";
  
  private int cbReadBufferMax = 16384;
  
  private boolean debugRetainBuffers = true;
  
  private int msBulkInReadTimeout = 5000;
  
  private int packetBufferCacheSize = 16;
  
  private int retainedBufferCapacity = 5;
  
  public int getBulkInReadTimeout() {
    return this.msBulkInReadTimeout;
  }
  
  public int getMaxReadBufferSize() {
    return this.cbReadBufferMax;
  }
  
  public int getPacketBufferCacheSize() {
    return this.packetBufferCacheSize;
  }
  
  public int getRetainedBufferCapacity() {
    return this.retainedBufferCapacity;
  }
  
  public boolean isDebugRetainBuffers() {
    return this.debugRetainBuffers;
  }
  
  public void setBuildInReadTimeout(int paramInt) {
    this.msBulkInReadTimeout = paramInt;
  }
  
  public void setDebugRetainBuffers(boolean paramBoolean) {
    this.debugRetainBuffers = paramBoolean;
  }
  
  public void setMaxReadBufferSize(int paramInt) {
    this.cbReadBufferMax = paramInt;
  }
  
  public void setPacketBufferCacheSize(int paramInt) {
    this.packetBufferCacheSize = paramInt;
  }
  
  public void setRetainedBufferCapacity(int paramInt) {
    this.retainedBufferCapacity = paramInt;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\FtDeviceManagerParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */