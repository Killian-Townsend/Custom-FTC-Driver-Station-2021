package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import java.nio.ByteBuffer;

public class LynxSetMotorTargetPositionCommand extends LynxDekaInterfaceCommand<LynxAck> {
  public static final int apiToleranceFirst = 0;
  
  public static final int apiToleranceLast = 65535;
  
  public static final int cbPayload = 7;
  
  private byte motor;
  
  private int target;
  
  private short tolerance;
  
  public LynxSetMotorTargetPositionCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxSetMotorTargetPositionCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt1, int paramInt2, int paramInt3) {
    this(paramLynxModuleIntf);
    LynxConstants.validateMotorZ(paramInt1);
    if (paramInt3 >= 0 && paramInt3 <= 65535) {
      this.motor = (byte)paramInt1;
      this.target = paramInt2;
      this.tolerance = (short)paramInt3;
      return;
    } 
    throw new IllegalArgumentException(String.format("illegal tolerance: %d", new Object[] { Integer.valueOf(paramInt3) }));
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.motor = byteBuffer.get();
    this.target = byteBuffer.getInt();
    this.tolerance = byteBuffer.getShort();
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(7).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.motor);
    byteBuffer.putInt(this.target);
    byteBuffer.putShort(this.tolerance);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxSetMotorTargetPositionCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */