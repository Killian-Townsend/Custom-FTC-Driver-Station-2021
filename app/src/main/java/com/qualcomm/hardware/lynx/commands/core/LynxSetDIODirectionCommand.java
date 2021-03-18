package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import java.nio.ByteBuffer;

public class LynxSetDIODirectionCommand extends LynxDekaInterfaceCommand<LynxAck> {
  public final int cbPayload = 2;
  
  private int direction;
  
  private int pin;
  
  public LynxSetDIODirectionCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxSetDIODirectionCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt, DigitalChannel.Mode paramMode) {
    this(paramLynxModuleIntf);
    LynxConstants.validateDigitalIOZ(paramInt);
    this.pin = paramInt;
    if (paramMode == DigitalChannel.Mode.INPUT) {
      paramInt = 0;
    } else {
      paramInt = 1;
    } 
    this.direction = paramInt;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.pin = byteBuffer.get();
    this.direction = byteBuffer.get();
  }
  
  public DigitalChannel.Mode getMode() {
    return (this.direction == 0) ? DigitalChannel.Mode.INPUT : DigitalChannel.Mode.OUTPUT;
  }
  
  public int getPin() {
    return this.pin;
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(2).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put((byte)this.pin);
    byteBuffer.put((byte)this.direction);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxSetDIODirectionCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */