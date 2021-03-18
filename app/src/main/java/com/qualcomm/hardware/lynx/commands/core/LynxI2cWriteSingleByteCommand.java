package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import java.nio.ByteBuffer;

public class LynxI2cWriteSingleByteCommand extends LynxDekaInterfaceCommand<LynxAck> {
  public static final int cbPayload = 3;
  
  private byte bValue;
  
  private byte i2cAddr7Bit;
  
  private byte i2cBus;
  
  public LynxI2cWriteSingleByteCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxI2cWriteSingleByteCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt1, I2cAddr paramI2cAddr, int paramInt2) {
    this(paramLynxModuleIntf);
    LynxConstants.validateI2cBusZ(paramInt1);
    this.i2cBus = (byte)paramInt1;
    this.i2cAddr7Bit = (byte)paramI2cAddr.get7Bit();
    this.bValue = (byte)paramInt2;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.i2cBus = byteBuffer.get();
    this.i2cAddr7Bit = byteBuffer.get();
    this.bValue = byteBuffer.get();
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(3).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.i2cBus);
    byteBuffer.put(this.i2cAddr7Bit);
    byteBuffer.put(this.bValue);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxI2cWriteSingleByteCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */