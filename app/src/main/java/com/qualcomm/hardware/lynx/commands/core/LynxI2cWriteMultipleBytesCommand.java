package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class LynxI2cWriteMultipleBytesCommand extends LynxDekaInterfaceCommand<LynxAck> {
  public static final int cbFixed = 3;
  
  public static final int cbPayloadFirst = 1;
  
  public static final int cbPayloadLast = 100;
  
  private byte i2cAddr7Bit;
  
  private byte i2cBus;
  
  private byte[] payload;
  
  public LynxI2cWriteMultipleBytesCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxI2cWriteMultipleBytesCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt, I2cAddr paramI2cAddr, byte[] paramArrayOfbyte) {
    this(paramLynxModuleIntf);
    LynxConstants.validateI2cBusZ(paramInt);
    if (paramArrayOfbyte.length >= 1 && paramArrayOfbyte.length <= 100) {
      this.i2cBus = (byte)paramInt;
      this.i2cAddr7Bit = (byte)paramI2cAddr.get7Bit();
      this.payload = Arrays.copyOf(paramArrayOfbyte, paramArrayOfbyte.length);
      return;
    } 
    throw new IllegalArgumentException(String.format("illegal payload length: %d", new Object[] { Integer.valueOf(paramArrayOfbyte.length) }));
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.i2cBus = byteBuffer.get();
    this.i2cAddr7Bit = byteBuffer.get();
    byte[] arrayOfByte = new byte[TypeConversion.unsignedByteToInt(byteBuffer.get())];
    this.payload = arrayOfByte;
    byteBuffer.get(arrayOfByte);
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(this.payload.length + 3).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.i2cBus);
    byteBuffer.put(this.i2cAddr7Bit);
    byteBuffer.put((byte)this.payload.length);
    byteBuffer.put(this.payload);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxI2cWriteMultipleBytesCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */