package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import java.nio.ByteBuffer;

public class LynxSetMotorChannelModeCommand extends LynxDekaInterfaceCommand<LynxAck> {
  public final int cbPayload = 3;
  
  private byte floatAtZero;
  
  private byte mode;
  
  private byte motor;
  
  public LynxSetMotorChannelModeCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxSetMotorChannelModeCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt, DcMotor.RunMode paramRunMode, DcMotor.ZeroPowerBehavior paramZeroPowerBehavior) {
    this(paramLynxModuleIntf);
    LynxConstants.validateMotorZ(paramInt);
    this.motor = (byte)paramInt;
    paramInt = null.$SwitchMap$com$qualcomm$robotcore$hardware$DcMotor$RunMode[paramRunMode.ordinal()];
    boolean bool = false;
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt == 3) {
          this.mode = 2;
        } else {
          throw new IllegalArgumentException(String.format("illegal mode %s", new Object[] { paramRunMode.toString() }));
        } 
      } else {
        this.mode = 1;
      } 
    } else {
      this.mode = 0;
    } 
    if (paramZeroPowerBehavior != DcMotor.ZeroPowerBehavior.BRAKE)
      bool = true; 
    this.floatAtZero = bool;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.motor = byteBuffer.get();
    this.mode = byteBuffer.get();
    this.floatAtZero = byteBuffer.get();
  }
  
  public DcMotor.RunMode getMode() {
    byte b = this.mode;
    return (b != 1) ? ((b != 2) ? DcMotor.RunMode.RUN_WITHOUT_ENCODER : DcMotor.RunMode.RUN_TO_POSITION) : DcMotor.RunMode.RUN_USING_ENCODER;
  }
  
  public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
    return (this.floatAtZero == 0) ? DcMotor.ZeroPowerBehavior.BRAKE : DcMotor.ZeroPowerBehavior.FLOAT;
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(3).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.motor);
    byteBuffer.put(this.mode);
    byteBuffer.put(this.floatAtZero);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxSetMotorChannelModeCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */