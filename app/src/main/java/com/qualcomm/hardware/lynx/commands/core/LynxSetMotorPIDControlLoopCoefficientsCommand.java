package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import java.nio.ByteBuffer;

public class LynxSetMotorPIDControlLoopCoefficientsCommand extends LynxDekaInterfaceCommand<LynxAck> {
  private static final int cbPayload = 14;
  
  private int d;
  
  private int i;
  
  private byte mode;
  
  private byte motor;
  
  private int p;
  
  public LynxSetMotorPIDControlLoopCoefficientsCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxSetMotorPIDControlLoopCoefficientsCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt1, DcMotor.RunMode paramRunMode, int paramInt2, int paramInt3, int paramInt4) {
    this(paramLynxModuleIntf);
    LynxConstants.validateMotorZ(paramInt1);
    this.motor = (byte)paramInt1;
    paramInt1 = null.$SwitchMap$com$qualcomm$robotcore$hardware$DcMotor$RunMode[paramRunMode.ordinal()];
    if (paramInt1 != 1) {
      if (paramInt1 == 2) {
        this.mode = 2;
      } else {
        throw new IllegalArgumentException(String.format("illegal mode: %s", new Object[] { paramRunMode.toString() }));
      } 
    } else {
      this.mode = 1;
    } 
    this.p = paramInt2;
    this.i = paramInt3;
    this.d = paramInt4;
  }
  
  public static double externalCoefficientFromInternal(int paramInt) {
    return paramInt / 65536.0D;
  }
  
  public static int internalCoefficientFromExternal(double paramDouble) {
    return (int)(Math.abs(paramDouble) * 65536.0D + 0.5D) * (int)Math.signum(paramDouble);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.motor = byteBuffer.get();
    this.mode = byteBuffer.get();
    this.p = byteBuffer.getInt();
    this.i = byteBuffer.getInt();
    this.d = byteBuffer.getInt();
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(14).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.motor);
    byteBuffer.put(this.mode);
    byteBuffer.putInt(this.p);
    byteBuffer.putInt(this.i);
    byteBuffer.putInt(this.d);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxSetMotorPIDControlLoopCoefficientsCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */