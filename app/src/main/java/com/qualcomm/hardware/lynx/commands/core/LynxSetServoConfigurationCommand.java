package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import java.nio.ByteBuffer;

public class LynxSetServoConfigurationCommand extends LynxDekaInterfaceCommand<LynxAck> {
  public static final int apiFramePeriodFirst = 0;
  
  public static final int apiFramePeriodLast = 65535;
  
  public static final int cbPayload = 3;
  
  private byte channel;
  
  private short framePeriod;
  
  public LynxSetServoConfigurationCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxSetServoConfigurationCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt1, int paramInt2) {
    this(paramLynxModuleIntf);
    LynxConstants.validateServoChannelZ(paramInt1);
    if (paramInt2 >= 0 && paramInt2 <= 65535) {
      this.channel = (byte)paramInt1;
      this.framePeriod = (short)paramInt2;
      return;
    } 
    throw new IllegalArgumentException(String.format("illegal frame period: %d", new Object[] { Integer.valueOf(paramInt2) }));
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.channel = byteBuffer.get();
    this.framePeriod = byteBuffer.getShort();
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(3).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.channel);
    byteBuffer.putShort(this.framePeriod);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxSetServoConfigurationCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */