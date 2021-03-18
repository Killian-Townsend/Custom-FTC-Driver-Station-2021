package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;

public class LynxGetMotorChannelCurrentAlertLevelResponse extends LynxDekaInterfaceResponse {
  private static final int cbPayload = 1;
  
  private short mACurrentLimit;
  
  public LynxGetMotorChannelCurrentAlertLevelResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.mACurrentLimit = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN).getShort();
  }
  
  public int getCurrentLimit() {
    return TypeConversion.unsignedShortToInt(this.mACurrentLimit);
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.putShort(this.mACurrentLimit);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetMotorChannelCurrentAlertLevelResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */