package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.LynxInterfaceResponse;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import java.nio.ByteBuffer;

public class LynxI2cReadStatusQueryCommand extends LynxDekaInterfaceCommand<LynxI2cReadStatusQueryResponse> {
  public static final int cbPayload = 1;
  
  private byte i2cBus;
  
  public LynxI2cReadStatusQueryCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
    this.response = (LynxMessage)new LynxI2cReadStatusQueryResponse(paramLynxModuleIntf, 0);
  }
  
  public LynxI2cReadStatusQueryCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt1, int paramInt2) {
    this(paramLynxModuleIntf);
    LynxConstants.validateI2cBusZ(paramInt1);
    this.response = (LynxMessage)new LynxI2cReadStatusQueryResponse(paramLynxModuleIntf, paramInt2);
    this.i2cBus = (byte)paramInt1;
  }
  
  public static Class<? extends LynxInterfaceResponse> getResponseClass() {
    return (Class)LynxI2cReadStatusQueryResponse.class;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.i2cBus = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN).get();
  }
  
  public boolean isResponseExpected() {
    return true;
  }
  
  public void onResponseReceived(LynxMessage paramLynxMessage) {
    super.onResponseReceived(paramLynxMessage);
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.i2cBus);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxI2cReadStatusQueryCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */