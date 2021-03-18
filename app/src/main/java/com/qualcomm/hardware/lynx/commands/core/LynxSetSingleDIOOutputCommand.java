package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import java.nio.ByteBuffer;

public class LynxSetSingleDIOOutputCommand extends LynxDekaInterfaceCommand<LynxAck> {
  public final int cbPayload = 2;
  
  private byte pin;
  
  private byte value;
  
  public LynxSetSingleDIOOutputCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxSetSingleDIOOutputCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt, boolean paramBoolean) {}
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.pin = byteBuffer.get();
    this.value = byteBuffer.get();
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(2).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.pin);
    byteBuffer.put(this.value);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxSetSingleDIOOutputCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */