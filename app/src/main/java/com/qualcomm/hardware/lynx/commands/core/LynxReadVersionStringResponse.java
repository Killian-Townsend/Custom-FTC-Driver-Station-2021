package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class LynxReadVersionStringResponse extends LynxDekaInterfaceResponse {
  private byte cbText = 0;
  
  private byte[] rgbText = null;
  
  public LynxReadVersionStringResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    byte b = byteBuffer.get();
    this.cbText = b;
    byte[] arrayOfByte = new byte[b];
    this.rgbText = arrayOfByte;
    byteBuffer.get(arrayOfByte);
  }
  
  public String getNullableVersionString() {
    return (this.rgbText == null) ? null : new String(this.rgbText, Charset.forName("UTF-8"));
  }
  
  public byte[] toPayloadByteArray() {
    int i;
    byte[] arrayOfByte1 = this.rgbText;
    if (arrayOfByte1 == null) {
      i = 0;
    } else {
      i = arrayOfByte1.length;
    } 
    ByteBuffer byteBuffer = ByteBuffer.allocate(i + 1).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.cbText);
    byte[] arrayOfByte2 = this.rgbText;
    if (arrayOfByte2 != null)
      byteBuffer.put(arrayOfByte2); 
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxReadVersionStringResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */