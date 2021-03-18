package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import java.nio.ByteBuffer;

public class LynxGetMotorPIDControlLoopCoefficientsResponse extends LynxDekaInterfaceResponse {
  private static final int cbPayload = 12;
  
  private int d = 0;
  
  private int i = 0;
  
  private int p = 0;
  
  public LynxGetMotorPIDControlLoopCoefficientsResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.p = byteBuffer.getInt();
    this.i = byteBuffer.getInt();
    this.d = byteBuffer.getInt();
  }
  
  public int getD() {
    return this.d;
  }
  
  public int getI() {
    return this.i;
  }
  
  public int getP() {
    return this.p;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(12).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.putInt(this.p);
    byteBuffer.putInt(this.i);
    byteBuffer.putInt(this.d);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetMotorPIDControlLoopCoefficientsResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */