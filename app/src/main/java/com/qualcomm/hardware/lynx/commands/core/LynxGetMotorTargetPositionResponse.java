package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;

public class LynxGetMotorTargetPositionResponse extends LynxDekaInterfaceResponse {
  private static final int cbPayload = 6;
  
  private int target;
  
  private short tolerance;
  
  public LynxGetMotorTargetPositionResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.target = byteBuffer.getInt();
    this.tolerance = byteBuffer.getShort();
  }
  
  public int getTarget() {
    return this.target;
  }
  
  public int getTolerance() {
    return TypeConversion.unsignedShortToInt(this.tolerance);
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(6).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.putInt(this.target);
    byteBuffer.putShort(this.tolerance);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetMotorTargetPositionResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */