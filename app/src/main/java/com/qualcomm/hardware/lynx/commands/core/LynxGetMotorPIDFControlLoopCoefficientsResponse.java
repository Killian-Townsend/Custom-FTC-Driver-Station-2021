package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import java.nio.ByteBuffer;

public class LynxGetMotorPIDFControlLoopCoefficientsResponse extends LynxDekaInterfaceResponse {
  private static final int cbPayload = 17;
  
  private int d = 0;
  
  private int f = 0;
  
  private int i = 0;
  
  private byte motorControlAlgorithm = 0;
  
  private int p = 0;
  
  public LynxGetMotorPIDFControlLoopCoefficientsResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.p = byteBuffer.getInt();
    this.i = byteBuffer.getInt();
    this.d = byteBuffer.getInt();
    this.f = byteBuffer.getInt();
    this.motorControlAlgorithm = byteBuffer.get();
  }
  
  public int getD() {
    return this.d;
  }
  
  public int getF() {
    return this.f;
  }
  
  public int getI() {
    return this.i;
  }
  
  public LynxSetMotorPIDFControlLoopCoefficientsCommand.InternalMotorControlAlgorithm getInternalMotorControlAlgorithm() {
    return LynxSetMotorPIDFControlLoopCoefficientsCommand.InternalMotorControlAlgorithm.fromByte(this.motorControlAlgorithm);
  }
  
  public int getP() {
    return this.p;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(17).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.putInt(this.p);
    byteBuffer.putInt(this.i);
    byteBuffer.putInt(this.d);
    byteBuffer.putInt(this.f);
    byteBuffer.put(this.motorControlAlgorithm);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetMotorPIDFControlLoopCoefficientsResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */