package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import java.nio.ByteBuffer;

public class LynxSetMotorTargetVelocityCommand extends LynxDekaInterfaceCommand<LynxAck> {
  public static final int apiVelocityFirst = -32767;
  
  public static final int apiVelocityLast = 32767;
  
  public static final int cbPayload = 3;
  
  private byte motor;
  
  private short velocity;
  
  public LynxSetMotorTargetVelocityCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxSetMotorTargetVelocityCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt1, int paramInt2) {
    this(paramLynxModuleIntf);
    LynxConstants.validateMotorZ(paramInt1);
    if (paramInt2 >= -32767 && paramInt2 <= 32767) {
      this.motor = (byte)paramInt1;
      this.velocity = (short)paramInt2;
      return;
    } 
    throw new IllegalArgumentException(String.format("illegal velocity: %d", new Object[] { Integer.valueOf(paramInt2) }));
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.motor = byteBuffer.get();
    this.velocity = byteBuffer.getShort();
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(3).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.motor);
    byteBuffer.putShort(this.velocity);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxSetMotorTargetVelocityCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */