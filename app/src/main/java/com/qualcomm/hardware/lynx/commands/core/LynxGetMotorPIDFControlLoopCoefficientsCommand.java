package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.LynxInterfaceResponse;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import java.nio.ByteBuffer;

public class LynxGetMotorPIDFControlLoopCoefficientsCommand extends LynxDekaInterfaceCommand<LynxGetMotorPIDFControlLoopCoefficientsResponse> {
  private static final int cbPayload = 2;
  
  private byte mode;
  
  private byte motor;
  
  public LynxGetMotorPIDFControlLoopCoefficientsCommand(LynxModuleIntf paramLynxModuleIntf) {
    this(paramLynxModuleIntf, 0, DcMotor.RunMode.RUN_USING_ENCODER);
  }
  
  public LynxGetMotorPIDFControlLoopCoefficientsCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt, DcMotor.RunMode paramRunMode) {
    super(paramLynxModuleIntf);
    LynxConstants.validateMotorZ(paramInt);
    this.motor = (byte)paramInt;
    paramInt = null.$SwitchMap$com$qualcomm$robotcore$hardware$DcMotor$RunMode[paramRunMode.ordinal()];
    if (paramInt != 1) {
      if (paramInt == 2) {
        this.mode = 2;
      } else {
        throw new IllegalArgumentException(String.format("illegal mode: %s", new Object[] { paramRunMode.toString() }));
      } 
    } else {
      this.mode = 1;
    } 
    this.response = (LynxMessage)new LynxGetMotorPIDFControlLoopCoefficientsResponse(paramLynxModuleIntf);
  }
  
  public static Class<? extends LynxInterfaceResponse> getResponseClass() {
    return (Class)LynxGetMotorPIDFControlLoopCoefficientsResponse.class;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.motor = byteBuffer.get();
    this.mode = byteBuffer.get();
  }
  
  public boolean isResponseExpected() {
    return true;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(2).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.motor);
    byteBuffer.put(this.mode);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetMotorPIDFControlLoopCoefficientsCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */