package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.nio.ByteBuffer;

public class LynxGetMotorChannelModeResponse extends LynxDekaInterfaceResponse {
  private static final int cbPayload = 2;
  
  private byte floatAtZero;
  
  private byte mode;
  
  public LynxGetMotorChannelModeResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.mode = byteBuffer.get();
    this.floatAtZero = byteBuffer.get();
  }
  
  public DcMotor.RunMode getMode() {
    byte b = this.mode;
    if (b != 0) {
      if (b != 1) {
        if (b == 2)
          return DcMotor.RunMode.RUN_TO_POSITION; 
        throw new IllegalStateException(String.format("illegal mode byte: 0x%02x", new Object[] { Byte.valueOf(this.mode) }));
      } 
      return DcMotor.RunMode.RUN_USING_ENCODER;
    } 
    return DcMotor.RunMode.RUN_WITHOUT_ENCODER;
  }
  
  public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
    return (this.floatAtZero == 0) ? DcMotor.ZeroPowerBehavior.BRAKE : DcMotor.ZeroPowerBehavior.FLOAT;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(2).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.mode);
    byteBuffer.put(this.floatAtZero);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetMotorChannelModeResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */