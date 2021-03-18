package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;

public class LynxGetPWMPulseWidthResponse extends LynxDekaInterfaceResponse {
  public static final int cbPayload = 2;
  
  private short pulseWidth = 0;
  
  public LynxGetPWMPulseWidthResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.pulseWidth = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN).getShort();
  }
  
  public int getPulseWidth() {
    return TypeConversion.unsignedShortToInt(this.pulseWidth);
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(2).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.putShort(this.pulseWidth);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetPWMPulseWidthResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */