package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;

public class LynxSetMotorChannelCurrentAlertLevelCommand extends LynxDekaInterfaceCommand<LynxAck> {
  public final int cbPayload = 3;
  
  private short mACurrentLimit;
  
  private byte motor;
  
  public LynxSetMotorChannelCurrentAlertLevelCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxSetMotorChannelCurrentAlertLevelCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt1, int paramInt2) {
    this(paramLynxModuleIntf);
    LynxConstants.validateMotorZ(paramInt1);
    this.motor = (byte)paramInt1;
    short s = (short)paramInt2;
    this.mACurrentLimit = s;
    if (TypeConversion.unsignedShortToInt(s) == paramInt2)
      return; 
    throw new IllegalArgumentException(String.format("illegal current limit: %d mA", new Object[] { Integer.valueOf(paramInt2) }));
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.motor = byteBuffer.get();
    this.mACurrentLimit = byteBuffer.getShort();
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(3).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.motor);
    byteBuffer.putShort(this.mACurrentLimit);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxSetMotorChannelCurrentAlertLevelCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */