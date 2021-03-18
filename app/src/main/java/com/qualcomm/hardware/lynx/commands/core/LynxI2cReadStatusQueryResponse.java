package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import java.nio.ByteBuffer;

public class LynxI2cReadStatusQueryResponse extends LynxI2cResponse {
  public static final int cbPayload = 2;
  
  private byte cbRead;
  
  private byte[] payload;
  
  public LynxI2cReadStatusQueryResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxI2cReadStatusQueryResponse(LynxModuleIntf paramLynxModuleIntf, int paramInt) {
    super(paramLynxModuleIntf);
    this.payload = new byte[paramInt];
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.i2cStatus = byteBuffer.get();
    byte b = byteBuffer.get();
    this.cbRead = b;
    byte[] arrayOfByte = new byte[b];
    this.payload = arrayOfByte;
    byteBuffer.get(arrayOfByte);
  }
  
  public byte[] getBytes() {
    return this.payload;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(2).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.i2cStatus);
    byteBuffer.put(this.cbRead);
    byteBuffer.put(this.payload);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxI2cReadStatusQueryResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */