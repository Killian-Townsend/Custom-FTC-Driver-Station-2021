package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.LynxInterfaceResponse;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import java.nio.ByteBuffer;

public class LynxGetServoConfigurationCommand extends LynxDekaInterfaceCommand<LynxGetServoConfigurationResponse> {
  public static final int cbPayload = 1;
  
  private byte channel;
  
  public LynxGetServoConfigurationCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
    this.response = (LynxMessage)new LynxGetServoConfigurationResponse(paramLynxModuleIntf);
  }
  
  public LynxGetServoConfigurationCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt) {
    this(paramLynxModuleIntf);
    LynxConstants.validateServoChannelZ(paramInt);
    this.channel = (byte)paramInt;
  }
  
  public static Class<? extends LynxInterfaceResponse> getResponseClass() {
    return (Class)LynxGetServoConfigurationResponse.class;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.channel = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN).get();
  }
  
  public boolean isResponseExpected() {
    return true;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.channel);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetServoConfigurationCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */